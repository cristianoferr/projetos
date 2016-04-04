<?

//ID=projeto
class WidgetNewScreen extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerForTable("screen");
        $painel = getPainelManager()->getPainel(PAINEL_FORM_INC_SCREEN);
        $painel->setTitulo(translateKey("txt_new_screen"));
        $painel->adicionaInputForm("projeto", $id);
        $painel->setController($controller);

        return $painel->generate();
    }

    function show($id) {
        $this->generate($id)->mostra();
    }

    function getTitle() {
        return translateKey("txt_new_screen");
    }

}

?>