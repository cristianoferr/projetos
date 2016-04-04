<?php

class TestsDiagrams extends TestBase {

    function getArrayTabelas() {
        return array("diagrama", "tipo_diagrama",);
    }

    function testCargaTabelas() {
        $this->validaTabelas();
    }

    function testEntidadesDoEntregavel() {
        $controllerEntidade = getControllerForTable("entidade");

        $id_entregavel_raiz = ENTREGAVEL_TESTE_RAIZ;
        $rs = $controllerEntidade->consultaEntidadesProjeto(PROJETO_TESTE, $id_entregavel_raiz = null);
        $c = $this->rsCount($rs);

        $this->assertEqual($c, 5, "Entidades do modulo raiz <> 5 : $c");

        $id_entregavel_raiz = 618;
        $rs = $controllerEntidade->consultaEntidadesProjeto(PROJETO_TESTE, $id_entregavel_raiz);
        $c = $this->rsCount($rs);

        $this->assertEqual($c, 3, "Entidades do modulo modelos <> 3 : $c");
    }

    function testDiagrama() {
        $idDiagramaTeste = 12;
        $controller = getControllerForTable("diagrama");
        $name = $controller->getDescricao($idDiagramaTeste);
        $nameEsp = "TesteDiagrama";
        $this->assertEqual($name, $nameEsp, "Nome Diagrama: $name diferente de: $nameEsp");


        $arr = array(
            "projeto" => 131, "nm_diagrama" => "testeAutoInsereDiagrama", "id_tipo_diagrama" => 1);
        $this->verificaInclusaoRemocao($arr, $controller, "diagrama");
    }

    function testDiagramaName() {
        $controller = getControllerForTable("diagrama");
        $idDiagramaTeste = 12;

        $model = $controller->loadSingle($idDiagramaTeste);
        $name = $model->getDescricao();
        $idRet = $model->getId();
        $nameEsp = "TesteDiagrama";

        $this->assertEqual($name, $nameEsp, "Nome Diagrama: $name diferente de: $nameEsp (loadSingle)");
        $this->assertEqual($idRet, $idDiagramaTeste, "Id diagrama retornado: $idRet diferente do esperado: $idDiagramaTeste");
    }

    function testModelWidthEHEight() {
        $model1 = new BaseModel(null, null);
        $model1->carregaDados(1, array("id" <= 1, "nome" <= 'a'));

        $model11 = new BaseModel(null, null);
        $model11->carregaDados(11, array("id" <= 11, "nome" <= 'a1'));

        $model12 = new BaseModel(null, null);
        $model12->carregaDados(12, array("id" <= 12, "nome" <= 'a2'));

        $model13 = new BaseModel(null, null);
        $model13->carregaDados(13, array("id" <= 13, "nome" <= 'a3'));

        $model14 = new BaseModel(null, null);
        $model14->carregaDados(14, array("id" <= 14, "nome" <= 'a4'));

        $model1->addChild($model11);
        $model1->addChild($model12);
        $model1->addChild($model13);
        $model1->addChild($model14);

        /* a
          -- a1 a2 a3 a4 */

        $w = $model1->getWidth();
        $espW = 5;
        $h = $model1->getHeight();
        $espH = 2;
        // echo $w, $esp;
        $this->assertEqual($w, $espW, "1. Width[$w] diferente de $espW");
        $this->assertEqual($h, $espH, "1. Height[$h] diferente de $espH");

        $model121 = new BaseModel(null, null);
        $model121->carregaDados(121, array("id" <= 121, "nome" <= 'a21'));
        $model12->addChild($model121);

        /* 1  2  3  4   5
         * a
         *    a1 a2 a3  a4 
         *          a21 */

        $w = $model1->getWidth();
        $h = $model1->getHeight();
        $espW = 5;
        $espH = 3;
        $this->assertEqual($w, $espW, "2. Width[$w] diferente de $espW");
        $this->assertEqual($h, $espH, "2. Height[$h] diferente de $espH");
    }

    function testMatrizDependencia() {
        $matrizController = new MatrizDependenciaController();
        $this->assertNotNull($matrizController);

        $arr = $matrizController->getVetorDependenciaEntidade(ENTIDADE_TESTE);
        $this->assertNotNull($arr, "getVetorDependenciaEntidade nulo");
        $this->assertEqual(sizeof($arr), 2, "quantidade de colunas diferente de 2:" . sizeof($arr));

        foreach ($arr as $value) {
            $ent = $value[0];
            $tipo = trim($value[1]);
            $this->assertTrue((($tipo == DEP_FK) || ($tipo == DEP_EXTENDS)), "Tipo de dependencia diferente do esperado: $tipo");
            $this->assertTrue((($ent == 392) || ($ent == 393)), "Entidade de dependencia diferente do esperado: $ent");
        }

        //criaMatrizDependencia

        $matriz = $matrizController->criaMatrizDependencia(PROJETO_TESTE, ENTREGAVEL_TESTE);
        $this->assertNotNull($matriz, "matriz nulo");
        $this->assertEqual(sizeof($matriz), 0, "Entidades encontrada no entregavel_teste: " . sizeof($matriz));

        $matriz = $matrizController->criaMatrizDependencia(PROJETO_TESTE, ENTREGAVEL_TESTE_RAIZ);
        $this->assertNotNull($matriz, "matriz nulo");
        $this->assertEqual(sizeof($matriz), 5, "Entidades diferentes do esperado para raiz(5): " . sizeof($matriz));

        $matriz = $matrizController->criaMatrizDependencia(PROJETO_TESTE, 618);
        $this->assertNotNull($matriz, "matriz nulo");
        $this->assertEqual(sizeof($matriz), 3, "Entidades diferentes do esperado para entregavel 'Modelos'(3): " . sizeof($matriz));
    }

    function testExport() {
        $controller = new ExportController();
        $table = "tabela";
        $campos = "a,b,c,d,";
        $valores = "1,null,,'abc',";
        $saida = $controller->getInsertString($table, $campos, $valores);
        $saidaEsp = "insert into $table (a,b,c,d) values (1,null,null,'abc');\n";
        $this->assertEqual($saida, $saidaEsp, "testExport: '$saida' diferente de '$saidaEsp'");
    }

}

?>
