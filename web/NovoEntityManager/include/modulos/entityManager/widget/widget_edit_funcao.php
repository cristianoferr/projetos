<?

//id=funcao
class WidgetEditFuncao extends WidgetEntidade {

    function generate($id) {//coberto
        $controller = getControllerManager()->getControllerForTable("funcao");
        $painel = getPainelManager()->getPainel(PAINEL_FUNCAO);
        $painel->setController($controller);
        $modelEntidade = $controller->loadSingle($id, $painel);
        $painel->setTitulo($modelEntidade->getDescricao());
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_function");
    }

}

?>