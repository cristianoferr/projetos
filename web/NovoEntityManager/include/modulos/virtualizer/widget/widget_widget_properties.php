<?

//ID=widget
class WidgetWidgetProperties extends WidgetManagerVirtualizer {

    function generate($id) {//coberto
        $painel = getPainelManager()->getPainel(PAINEL_SHOW_WIDGET_PROPERTIES);
        $painel->addFiltroColuna("id_coluna", "and id_entidade_pai=(select id_entidade from widget w where w.id_widget=$id)");
        $painel->load("id_widget", $id);
        $painel->setTitulo(translateKey("txt_widget_fields"));

        $painel->defineWidgetForAction(ACAO_INSERIR, WIDGET_ADD_WIDGET_SECTION);
        $painel->defineWidgetForAction(ACAO_EXCLUIR, WIDGET_DELETE_WIDGET_SECTION);
        $painel->defineWidgetForAction(ACAO_INSERIR_DETALHE, WIDGET_ADD_WIDGET_COLUNA);
        $painel->defineWidgetForAction(ACAO_EXCLUIR_DETALHE, WIDGET_DELETE_WIDGET_COLUNA);

        $ret = $painel->generate();
        return $ret;
    }

    function show($id) {//coberto
        $this->generate($id)->mostra();
    }

    function getTitle() {//coberto
        return translateKey("txt_widget_properties");
    }

}

?>