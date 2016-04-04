<?

//WIDGETS
//id=id_fornecedor
class WidgetVisualizaMuralFornecedor extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("mural");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_MURAL);
        $controller->setDefaultOrderBy("data_mural desc");
        $model = $controller->loadRegistros("and id_fornecedor=$id", $painel);
        $painel->setController($controller);
        
        $elDiv=criaEl("div");
        $elP=criaEl("p",$elDiv)->setClass("titulo_area")->setValue(translateKey("txt_atividade"));
        $elPainel=$painel->generate();
        $elDiv->addElement($elPainel);
        return $elDiv;
    }

    function getTitle() {
        return translateKey("txt_mural");
    }

}
