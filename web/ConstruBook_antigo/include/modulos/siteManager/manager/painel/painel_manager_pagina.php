<?

define("PAINEL_PAGINAS", 'p_PAGINAS');
define("PAINEL_PAGINA", 'p_PAGINA');
define("PAINEL_INCLUSAO_PAGINA", 'p_INC_PAGINA');

class PainelManagerPagina {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_PAGINAS) {
            return $this->getPainelPaginas();
        }
        if ($nome == PAINEL_PAGINA) {
            return $this->getPainelPagina();
        }
        if ($nome == PAINEL_INCLUSAO_PAGINA) {
            return $this->getPainelInclusaoPagina();
        }
    }

    function getPainelPagina() {
        $table = getTableManager()->getTabelaComNome("pagina");
        $painel = new PainelVertical("pp", $table);
        $painel->addColunaWithDBName("nminterno_pagina");
        $painel->addColunaWithDBName("id_tipo_pagina");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelInclusaoPagina() {
        $table = getTableManager()->getTabelaComNome("pagina");
        $painel = new FormPainel("pip", $table);
        $painel->addColunaWithDBName("nminterno_pagina");
        $painel->addColunaWithDBName("id_tipo_pagina");

        $painel->modoInclusao();
        $painel->desativaAjax();

        return $painel;
    }

    function getPainelPaginas() {
        $table = getTableManager()->getTabelaComNome("pagina");
        $painel = new PainelHorizontal("pcs", $table);
        $painel->addColunaWithDBName("nminterno_pagina");
        $painel->addColunaWithDBName("id_tipo_pagina");

        $painel->modoEdicao();
        $painel->setEditLink("paginas/");
        $painel->setDeleteLink("paginas/delete/");

        return $painel;
    }

}

?>