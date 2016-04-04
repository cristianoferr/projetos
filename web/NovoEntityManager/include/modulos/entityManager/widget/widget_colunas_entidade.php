<?

//ID=ENTIDADE
class WidgetColunasEntidade extends WidgetEntidade {

    function generate($id_entidade) {//coberto
        $controller = getControllerManager()->getControllerForTable("coluna");
        $id_projeto = projetoAtual();
        $painel = getPainelManager()->getPainel(PAINEL_COLUNAS_ENTIDADE);
        $painel->setController($controller);
        $controller->loadRegistros("and coluna.id_entidade_pai=$id_entidade", $painel);
        $painel->adicionaLink(getHomeDir() . "property/new/$id_projeto/$id_entidade", translateKey("txt_new_property"), false);
        $painel->adicionaLink(getHomeDir() . "view/$id_projeto/$id_entidade", translateKey("txt_data_input"), false);
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_properties");
    }

}

?>