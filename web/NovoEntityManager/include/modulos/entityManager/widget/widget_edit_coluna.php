<?

//id=coluna
class WidgetEditColuna extends WidgetEntidade {

    function generate($id) {//coberto
        $controller = getControllerManager()->getControllerForTable("coluna");
        $painel = getPainelManager()->getPainel(PAINEL_COLUNA);
        $painel->setController($controller);
        $modelEntidade = $controller->loadSingle($id, $painel);
        $painel->setTitulo($modelEntidade->getDescricao());
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_property");
    }

}

?>