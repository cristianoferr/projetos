<?

//id=tile
class WidgetEditTile extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("tile");
        $painel = getPainelManager()->getPainel(PAINEL_EDIT_TILE);
        $painel->setDeleteLink("screens/tile/delete/" . projetoAtual() . "/");
        $controller->loadSingle($id, $painel);
        $painel->setController($controller);
        $painel->setFormName("PET_" . $id);
        $painel->setModal($this->isModal());
        //$ret = criaEl("div")->setClass("block");
        //$ret->addElement(
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_tile");
    }

}

?>