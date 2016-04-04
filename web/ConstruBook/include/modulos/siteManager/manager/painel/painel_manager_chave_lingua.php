<?

define("PAINEL_STRINGS", 'p_STRINGS');
define("PAINEL_STRING", 'p_STRING');
define("PAINEL_VALOR_STRING", 'p_V_STRING');
define("PAINEL_INCLUSAO_STRING", 'p_INC_STRING');

class PainelManagerChaveLingua {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_STRINGS)
            return $this->getPainelChaves();
        if ($nome == PAINEL_STRING)
            return $this->getPainelChave();
        if ($nome == PAINEL_INCLUSAO_STRING)
            return $this->getPainelInclusaoChave();

        if ($nome == PAINEL_VALOR_STRING)
            return $this->getPainelValorChave();
    }

    function getPainelValorChave() {
        $table = getTableManager()->getTabelaComNome("valor_lingua");
        $painel = new PainelHorizontal("pvc", $table);
        $painel->addColunaWithDBName("id_valor_lingua");
        $painel->addColunaWithDBName("id_lingua");
        $painel->addColunaWithDBName("nm_chave_lingua");
        $painel->addColunaWithDBName("nm_valor_lingua");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelChave() {
        $table = getTableManager()->getTabelaComNome("chave_lingua");
        $painel = new PainelVertical("pcs", $table);
        $painel->addColunaWithDBName("nm_chave_lingua");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelInclusaoChave() {
        $table = getTableManager()->getTabelaComNome("chave_lingua");
        $painel = new FormPainel("pc", $table);
        $painel->addColunaWithDBName("nm_chave_lingua");
        $painel->addColunaWithDBName("valor_en_chave_lingua_novo");
        $painel->addColunaWithDBName("valor_pt_chave_lingua_novo");
        //	$painel->addColunaWithDBName("cor_camada_projeto");
        //$painel->adicionaAcao(getHomeDir()."layers/".projetoAtual()."/",txt_edit,false);
        //$painel->adicionaAcaoImportante(getHomeDir()."layers/delete/".projetoAtual()."/",txt_remove,true);
        $painel->modoInclusao();
        $painel->desativaAjax();

        return $painel;
    }

    function getPainelChaves() {
        $table = getTableManager()->getTabelaComNome("chave_lingua");
        $painel = new PainelHorizontal("pcs", $table);
        $painel->addColunaWithDBName("nm_chave_lingua");
        $painel->addColunaWithDBName("valor_en_chave_lingua");
        $painel->addColunaWithDBName("valor_pt_chave_lingua");
        //	$painel->addColunaWithDBName("cor_camada_projeto");
        //$painel->adicionaAcao(getHomeDir()."layers/".projetoAtual()."/",txt_edit,false);
        //$painel->adicionaAcaoImportante(getHomeDir()."layers/delete/".projetoAtual()."/",txt_remove,true);
        $painel->modoEdicao();
        $painel->setEditLink("strings/");
        $painel->setDeleteLink("strings/delete/");

        return $painel;
    }

}

?>