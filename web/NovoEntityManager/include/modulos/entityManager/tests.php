<?php

class TestsEntityManager extends TestBase {

    function testCorrigeColunaPkNula() {
        $entidade = ENTIDADE_TESTE;
        executaSQL("update entidade set id_coluna_pk=null where id_entidade=?", array($entidade));
        $controller = getControllerForTable("entidade");
        $controller->corrigeDados();

        $tot = countQuery("select * from entidade where id_coluna_pk is null and id_entidade=?", array($entidade));
        $this->assertEqual($tot, 0, "coluna_pk não foi preenchida: $tot");
    }

    function testGetterEntidade() {
        $controller = getControllerForTable("entidade");
        $id = $controller->getEntidadeID("entidadeTeste");
        $this->assertEqual($id, ENTIDADE_TESTE, "Id retornado do getEntidadeID diferente do esperado: $id");

        $count = $controller->countForProjeto(PROJETO_TESTE);
        $this->assertEqual(5, $count, "Count diferente de 5: $count");
    }

    function testGetIDProjeto() {
        $this->verificaIDProjeto("funcao", 3, 131);
        $this->verificaIDProjeto("entidade", 388, 131);
        $this->verificaIDProjeto("coluna", 1379, 131);
    }

    function getArrayTabelas() {
        return array("funcao", "entidade", "coluna", "funcao", "projeto", "camada", "valor", "relacao_datatype",
            "usuario_projeto",
            "parametro_funcao",
            "datatype", "datatype_param",
            "meta_type",
            "linguagem",
            "papel",
            "coluna",
            "registro",
            "primitive_type",
            "primitive_type_param",
            "sql_coluna",
            "acesso_coluna",
            "tipo_banco",
            "metodologia",
            "visibilidade_projeto",
            "categoria_projeto");
    }

    function testCargaTabelas() {
        $this->validaTabelas();
    }

    function testEntidade() {
        $controller = getControllerForTable("entidade");
        $name = $controller->getDescricao(ENTIDADE_TESTE);
        $nameEsp = "entidadeTeste";
        $this->assertEqual($name, $nameEsp, "Nome Entidade: $name diferente de: $nameEsp");


        $arr = array(
            "projeto" => PROJETO_TESTE, "nm_entidade" => "testeAutoInsereEntidade", "package_entidade" => null, "id_camada" => 1, "id_entregavel" => ENTREGAVEL_TESTE);
        $this->verificaInclusaoRemocao($arr, $controller, "entidade");
    }

    function testFuncao() {
        $controller = getControllerForTable("funcao");
        $name = $controller->getName(3);
        $nameEsp = "testeFuncao";
        $this->assertEqual($name, $nameEsp, "Nome Funcao: $name diferente de: $nameEsp");

        $arr = array(
            "entidade" => ENTIDADE_TESTE, "nm_funcao" => "testeAutoInsereFuncao", "id_entidade_retorno" => null, "id_datatype_retorno" => null);
        $this->verificaInclusaoRemocao($arr, $controller, "funcao");
    }

    function testColuna() {
        $controller = getControllerForTable("coluna");
        $pk_result = $controller->calcPKForEntidade(ENTIDADE_TESTE);
        $pk_expect = 1380;
        $this->assertEqual($pk_result, $pk_expect, "coluna: calcPKForEntidade: $pk_result diferente de: $pk_expect");

        $arr = array("entidade" => 388, "nm_coluna" => "testeAutoInsere", "id_entidade_combo" => null, "id_datatype" => null, "id_relacao_datatype" => 2);
        $this->verificaInclusaoRemocao($arr, $controller, "coluna");

        $coluna = 1398;
        $entRet = $controller->getEntidade($coluna);
        $this->assertEqual($entRet, ENTIDADE_TESTE, "entidade retornada da coluna diferente: $entRet ");

        $id_coluna = $controller->getIdColunaComNome("tipo_char", ENTIDADE_TESTE);
        $this->assertEqual($id_coluna, 1397, "id_coluna retornou diferente: $id_coluna ");

        //testa valor
        $controllerValor = getControllerForTable("valor");
        $valorExp = "valor qualquer";
        executaSQL("update valor set valor_coluna='vazio' where id_coluna_pai=? and id_registro=?", array($id_coluna, REGISTRO_TESTE));
        $controllerValor->atualizaCelula(REGISTRO_TESTE, $id_coluna, $valorExp);
        $valor = $controllerValor->getValorCelula(REGISTRO_TESTE, $id_coluna);
        $this->assertEqual($valor, $valorExp, "valores divergem: '$valor' <> '$valorExp'");
    }

    function testRegistro() {
        $controller = getControllerForTable("registro");

        $entRet = $controller->getEntidade(REGISTRO_TESTE);
        $this->assertEqual($entRet, ENTIDADE_TESTE, "entidade retornada do registro diferente: $entRet ");
    }

    function testProjeto() {
        $controller = getControllerForTable("projeto");
        $codPictuly = 129;
        $codEntityManager = 117;
        $this->assertTrue($controller->isProjetoPublico($codPictuly), "Pictuly não está retornando como projeto publico");
        $this->assertFalse($controller->isProjetoPublico($codEntityManager), "EntityManager está retornando como projeto publico");

        $arr = array("cod_projeto" => "ProjetoTesteAuto3", "nm_projeto" => "ProjetoTesteAuto", "iniciais_projeto" => "PTA", "package_projeto" => "package", id_tipo_banco => 1, id_metodologia => 1, id_visibilidade_projeto => 1);
        $this->verificaInclusaoRemocao($arr, $controller, "projeto");
    }

    function testSecurity() {
        $this->validaPermissoesDefault("projeto", PROJETO_TESTE);
        $this->validaPermissoesDefault("entidade", 388);
    }

    function testDatatype_getTipoDadoFrom() {


        $this->verificaTipoDado(162, TIPO_INPUT_FLOAT);
        $this->verificaTipoDado(160, TIPO_INPUT_DATA);
        $this->verificaTipoDado(156, TIPO_INPUT_INTEIRO);
        $this->verificaTipoDado(162, TIPO_INPUT_FLOAT);
        $this->verificaTipoDado(159, TIPO_INPUT_BOOLEAN);
        $this->verificaTipoDado(157, TIPO_INPUT_TEXTO_CURTO);
    }

    function verificaTipoDado($idDatatype, $tipoEsp) {
        $controller = getControllerForTable("datatype");
        $tipoRet = $controller->getTipoDadoFrom($idDatatype);
        $this->assertEqual($tipoRet, $tipoEsp, "getTipoDadoFrom: tipo retornado diferente: $tipoEsp <> $tipoRet ");
    }

}

?>
