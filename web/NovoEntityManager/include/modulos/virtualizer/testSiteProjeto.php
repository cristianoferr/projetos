<?php

class TestsSiteProjeto extends TestBase {

    function testTitulo() {
        $controller = getControllerForTable("site_projeto");
        $this->assertNotNull($controller, "site_projeto null");
        $tituloEsp = "Projeto Teste";
        $titulo = $controller->getDescricao(PROJETO_TESTE);
        $this->assertEqual($titulo, $tituloEsp, "Titulo retornado diferente do esperado: $titulo<>$tituloEsp");
    }

}

?>
