<?

class ControllerManager_SiteManager implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "elemento") {
            return $this->getElementoController();
        }

        if ($tableName == "pagina") {
            return $this->getPaginaController();
        }

        if ($tableName == "tipo_pagina") {
            return $this->getTipoPaginaController();
        }

        if ($tableName == "chave_lingua") {
            return $this->getChaveLinguaController();
        }
        if ($tableName == "valor_lingua") {
            return $this->getValorLinguaController();
        }
        if ($tableName == "ticket_reset") {
            return $this->getTicketResetController();
        }
    }

    function getValorLinguaController() {
        $table = getTableManager()->getTabelaComNome("valor_lingua");
        $controller = new ValorLinguaController($table, "txt_valores_lingua", "txt_valor_lingua");
        return $controller;
    }

    function getTicketResetController() {
        $table = getTableManager()->getTabelaComNome("valor_lingua");
        $controller = new TicketResetController($table, "txt_ticket_reset", "txt_ticket_reset");
        return $controller;
    }

    function getChaveLinguaController() {
        $table = getTableManager()->getTabelaComNome("chave_lingua");
        $controller = new ChaveLinguaController($table, "txt_chaves_lingua", "txt_chave_lingua");
        return $controller;
    }

    function getElementoController() {
        $table = getTableManager()->getTabelaComNome("elemento");
        $controller = new ElementoController($table, "txt_elements", "txt_element");
        return $controller;
    }

    function getPaginaController() {
        $table = getTableManager()->getTabelaComNome("pagina");
        $controller = new PaginaController($table, "txt_paginas", "txt_pagina");
        return $controller;
    }

    function getTipoPaginaController() {
        $table = getTableManager()->getTabelaComNome("tipo_pagina");
        $controller = new TipoPaginaController($table, "txt_tipo_paginas", "txt_tipo_pagina");
        return $controller;
    }

}

?>