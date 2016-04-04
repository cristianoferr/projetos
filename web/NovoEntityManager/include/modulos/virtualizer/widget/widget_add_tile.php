<?

//id=tile_pai
class WidgetAddTile extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("tile");
        $painel = getPainelManager()->getPainel(PAINEL_ADD_TILE);
        $painel->adicionaInputForm("projeto", projetoAtual());
        $painel->adicionaInputForm("tile_pai", $id);
        $painel->setController($controller);
        $painel->setModal($this->isModal());
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_tile");
    }

}

?>