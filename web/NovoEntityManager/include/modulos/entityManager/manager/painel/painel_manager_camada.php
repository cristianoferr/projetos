<?

define("PAINEL_CAMADAS_PROJETO", 'p_campro');

class PainelManagerCamada {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_CAMADAS_PROJETO)
            return $this->getPainelCamadasProjeto();
    }

    function getPainelCamadasProjeto() {
        $table = getTableManager()->getTabelaComNome("camada_projeto");
        $painel = new PainelHorizontal("pcp", $table);
        $painel->addColunaWithDBName("id_camada");
        $painel->addColunaWithDBName("id_linguagem");
        $painel->addColunaWithDBName("cont_entidades");
        //	$painel->addColunaWithDBName("cor_camada_projeto");
        //$painel->adicionaAcao(getHomeDir()."layers/".projetoAtual()."/",txt_edit,false);
        //$painel->adicionaAcaoImportante(getHomeDir()."layers/delete/".projetoAtual()."/",txt_remove,true);
        $painel->modoEdicao();

        return $painel;
    }

}

?>