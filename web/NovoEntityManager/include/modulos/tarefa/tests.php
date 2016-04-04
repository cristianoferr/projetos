<?php

class TestsMain_tarefa extends TestBase {

    function getArrayTabelas() {
        return array("tarefa", "entregavel");
    }

    function test_getHierarchicalDeliverables() {
        $controller = getControllerForTable("entregavel");

        $entregavelRaiz = ENTREGAVEL_TESTE;
        $arr = $controller->getHierarchicalDeliverables($entregavelRaiz);
        $this->assertEqual(sizeof($arr), 2, "quantidade de entregaveis diferente do esperado(2): " . sizeof($arr));

        $entregavelRaiz = ENTREGAVEL_TESTE_RAIZ;
        $arr = $controller->getHierarchicalDeliverables($entregavelRaiz);
        $this->assertEqual(sizeof($arr), 5, "quantidade de entregaveis raiz diferente do esperado(5): " . sizeof($arr));

        $entregavelRaiz = 619;
        $arr = $controller->getHierarchicalDeliverables($entregavelRaiz);
        $this->assertEqual(sizeof($arr), 1, "quantidade de entregaveis submodelos diferente do esperado(1): " . sizeof($arr));
    }

    function testGetIDProjeto() {
        $this->verificaIDProjeto("tarefa", 18, 131);
        $this->verificaIDProjeto("entregavel", 139, 131);
    }

    function testCargaTabelas() {
        $this->validaTabelas();
    }

    function testTarefa() {
        $controller = getControllerForTable("tarefa");


        $arr = array(
            "projeto" => PROJETO_TESTE, "nm_tarefa" => "testeAutoInsereTarefa", "desc_tarefa" => "desc!", "id_complexidade" => 1, id_urgencia => 2, id_entregavel => 139, hh_tarefa => 4, custo_tarefa => 10);
        $this->verificaInclusaoRemocao($arr, $controller, "tarefa");

        $countEsperado = executaQuerySingleRow("select count(*) as tot from tarefa where id_projeto=?", array(PROJETO_TESTE));
        $countEsperado = $countEsperado["tot"];
        $countRet = $controller->countForProjeto(PROJETO_TESTE);
        $this->assertEqual($countRet, $countEsperado, "Tarefa countForProjeto: $countEsperado<>$countRet");
    }

    function testEntregavel() {
        $controller = getControllerForTable("entregavel");

        $arr = array(
            "id_projeto" => PROJETO_TESTE, "nm_entregavel" => "testeAutoInsereEntregavel", "desc_entregavel" => "desc do entregavel!",
            "id_entregavel_pai" => 138, "id_status_entregavel" => 1);
        $this->verificaInclusaoRemocao($arr, $controller, "entregavel");

        $countEsperado = executaQuerySingleRow("select count(*) as tot from entregavel where id_projeto=?", array(PROJETO_TESTE));
        $countEsperado = $countEsperado["tot"];
        $countRet = $controller->countForProjeto(PROJETO_TESTE);
        $this->assertEqual($countRet, $countEsperado, "Entregavel countForProjeto: $countEsperado<>$countRet");

        $entrRaiz = $controller->getEntregavelRaiz();
        $this->assertEqual($entrRaiz, 138, "getEntregavelRaiz falhou: $entrRaiz");

        $status = $controller->getIdStatusEntregavel(138);
        $this->assertNotNull($status, "status entregavel nulo");
    }

    function testEntregavelTree() {
        $controller = getControllerForTable("entregavel");
        $controller->loadRegistros();

        //$arrTree = $controller->createModelTree("id_entregavel_pai");

        $dict = $controller->geraArrayComChaveDescricao();
        $arr = $dict->getArray();
        $this->assertEqual(sizeof($arr), 5, "Devia haver 5 entregaveis, existem:" . sizeof($arr));

        foreach ($arr as $item) {
            $name = $item["name"];
            $ok = false;
            if (trim($name) == "1. Projeto Teste") {
                $ok = true;
            }
            if (trim($name) == "1.1. DeliverableTest") {
                $ok = true;
            }
            if (trim($name) == "1.1.1. Outro entregavel teste") {
                $ok = true;
            }
            if (trim($name) == "1.2. Modelos") {
                $ok = true;
            }
            if (trim($name) == "1.2.1. SubModelos") {
                $ok = true;
            }
            $this->assertTrue($ok, "Nome não encontrado na relação de entregaveis: '$name'");
        }
    }

    function testSecurity() {
        $this->validaPermissoesDefault("tarefa", 18);
        $this->validaPermissoesDefault("entregavel", 138);
    }

}

?>
