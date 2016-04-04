<?

//id=entidade
class WidgetVisualizaEntidadeVirtual extends WidgetManagerEntidadeVirtual {

    function generate($id) {//coberto
        $painel = getPainelManager()->getPainel(PAINEL_VIEW_VIRTUAL_ENTITY);
        $painel->setTitulo($this->getTitle());
        $controller = getControllerForTable("virtual")->initFromEntity($id);
        $controller->loadAllColumnsIntoPanel($painel);
        $controller->loadRegistros(null, $painel);
        $painel->setController($controller);

        return $painel->generate();
    }

    function show($id) {//coberto
        $this->generate($id)->mostra();
    }

    function getTitle() {//coberto
        return translateKey("txt_data_input");
    }

}

?>