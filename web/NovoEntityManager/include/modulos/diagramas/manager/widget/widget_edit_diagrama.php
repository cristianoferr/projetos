<?

class WidgetEditDiagrama extends WidgetDiagrama {
    /*
     * ??
      function generate($id) {
      $controller = getControllerManager()->getControllerForTable("datatype");

      $painel = getpainelManager()->getPainelDatatype();
      $painel->setController($controller);
      $painel->setTitulo($this->getTitle());
      return $painel;
      }

      function show($id) {
      $this->generate($id)->mostra();
      } */

    function getTitle() {
        return translateKey("txt_datatype");
    }

}
