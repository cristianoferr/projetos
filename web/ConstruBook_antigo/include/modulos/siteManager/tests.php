<?php

class TestsMain_siteManager extends TestBase {

    function testMenuMaker() {
        $menu = new MenuMaker();
        $this->assertNotNull($menu, "menu vazio");
        $topMenu = new TopMenuPainel(translateKey("site_name"), getHomeDir() . "#top");
        $this->assertNotNull($topMenu, "topmenu vazio");
        $ret = $menu->geraMenuAdmin($topMenu);
        $this->assertNotNull($ret, "geraMenuAdmin deu erro");
        $ret = $menu->geraMenuUsuario($topMenu);
        $this->assertNotNull($ret, "geraMenuUsuario deu erro");
        $ret = $menu->geraMenuGuest($topMenu);
        $this->assertNotNull($ret, "geraMenuGuest deu erro");
        $ret = $menu->geraMenuTopoHelp($topMenu);
        $this->assertNotNull($ret, "geraMenuTopoHelp deu erro");
        $ret = $menu->geraMenuTopo();
        $this->assertNotNull($ret, "geraMenuTopo deu erro");
    }

    function testItemMenu() {
        $menu = new Menu_TM("txt_ok", "url", "classe");
        $this->assertNotNull($menu, "menu vazio");

        $count = $menu->count();
        $this->assertTrue($count == 0, "count do menu >0:", $count);
        $menu->addSeparador();
        $count = $menu->count();
        $this->assertTrue($count == 1, "count do menu<>1:", $count);
        $menu->addSeparador("txt");
        $count = $menu->count();
        $this->assertTrue($count == 2, "count do menu<>1:", $count);
    }

    function testeControllerValores() {
        $controller = getControllerForTable("usuario");
        $table = getTableManager()->getTabelaComNome("usuario");
        $id_lingua = $table->getColunaWithName("id_lingua");
        $this->assertNotNull($id_lingua, "Coluna id_lingua nulo");
        $this->assertTrue(get_class($id_lingua) == "Coluna", "Class retorno<>Coluna:" . get_class($id_lingua));
        //inicioDebug();
        $dict = $controller->geraArrayParaColuna($id_lingua);
        $array = $dict->getArray();
        $c = 0;
        foreach ($array as $value) {
            $ok = false;
            $id = $value["pk"];
            $name = $value["name"];
            $this->assertNotNull($name, "Nome da lingua vazio!");
            $this->assertNotNull($id, "id da lingua vazio!");
            if (($id == 1) || ($id == 2)) {
                $ok = true;
            }
            $this->assertNotEqual($id, 3, "id da lingua 3 retornado (lorem ipsum)");
            $c++;
        }
        $this->assertTrue($c > 1, "total de itens retornado da tabela lingua menor que 2: $c");
        $this->assertTrue($ok, "Ok falhou (nem todos os ids esperados foram encontrados");
        //fimDebug();
    }

    function testPagina() {
        $controller = getControllerForTable("pagina");
        $arr = array(
            "id_tipo_pagina" => 2, "nminterno_pagina" => "testeAutoInserePagina");
        $this->verificaInclusaoRemocao($arr, $controller, "pagina");
    }

    function testTickets() {
        $controller = getControllerForTable("ticket_reset");

        $ticket = $controller->geraNovoTicket();
        $this->assertNotNull($ticket, "Cod.Ticket Vazio");
    }

    function testUsuario() {
        $controller = getControllerForTable("usuario");
        $email = "teste@teste.com";
        $idTeste = 10;
        $id = $controller->emailExists($email);
        $this->assertNotNull($id, "Email $email não encontrado.");
        $this->assertEqual($id, $idTeste, "$id <> $idTeste (recebido)");

        $this->assertNotNull($controller->loadSingle(10), "load single do usuario retornou nulo");

        $email = "teste123456@teste.com";
        $id = $controller->emailExists($email);
        $this->assertNull($id, "Email $email encontrado: $id (não devia ser)");


        $nmRet = $controller->getUserLogin($idTeste);
        $nmEsp = "testeNome";
        $this->assertEqual($nmRet, $nmEsp, "Nomes: $nmEsp <> $nmRet ");

        $emailEsperado = "teste@teste.com";
        $email = $controller->getEmailFromId(10);
        $this->assertEqual($email, $emailEsperado, "$emailEsperado <> $email (recebido)");
    }

    function verificaTermo($term) {
        $controller = getControllerForTable("usuario");
        $dict = $controller->buscaUsuarios($term);
        $count = $dict->count();
        $this->assertTrue($count > 0, "Count: $count para o termo '$term' (devia ser pelo menos 1)");
    }

    function testBuscaUsuario() {
        $controller = getControllerForTable("usuario");
        $this->verificaTermo("testenome");
        $this->verificaTermo("testeNome");
        $this->verificaTermo("TESTE");
    }

    function testPaineisSiteManager() {
        $painelManager = getPainelManager();
        $this->assertNotNull($painelManager);
        $painel = $painelManager->getPainel(PAINEL_INCLUSAO_PAGINA);
        $this->assertNotNull($painel);
        $painel = $painelManager->getPainel(PAINEL_PAGINA);
        $this->assertNotNull($painel);
        $painel = $painelManager->getPainel(PAINEL_PAGINAS);
        $this->assertNotNull($painel);
    }

    function getArrayTabelas() {
        return array("tipo_pagina", "pagina", "prereq_tarefa", "status_entregavel", "urgencia", "complexidade", "entregavel");
    }

    function testCargaTabelas() {
        $this->validaTabelas();
    }

}

?>
