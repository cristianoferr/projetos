<?

define("PAINEL_VIEW_VIRTUAL_ENTITY", 'p_view_virtual_ent');

class PainelManagerEntidadeVirtual {

    function getPainelComNome($nome) {//coberto
        if ($nome == PAINEL_VIEW_VIRTUAL_ENTITY) {
            return $this->getPainelVisualizaVirtualEntity();
        }
    }

    function getPainelVisualizaVirtualEntity() {//coberto
        $table = getTableManager()->getTabelaComNome("virtual");
        $painel = new PainelHorizontal("pvvr", $table);

        /* $painel->addColunaWithDBName("id_camada");
          $painel->addColunaWithDBName("id_linguagem");
          $painel->addColunaWithDBName("cont_entidades"); */
        //	$painel->addColunaWithDBName("cor_camada_projeto");
        //$painel->adicionaAcao(getHomeDir()."layers/".projetoAtual()."/",txt_edit,false);
        $painel->adicionaAcaoImportante("view/delete/".projetoAtual()."/".entidadeAtual()."/",txt_remove,true);
        $painel->modoEdicao();
        $painel->adicionaLinkImportante(getHomeDir() . "view/new/" . projetoAtual()."/".entidadeAtual(), translateKey("txt_create_new"), false);

        return $painel;
    }

}

?>