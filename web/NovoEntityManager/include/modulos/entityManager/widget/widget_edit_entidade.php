<?

//id=entidade
class WidgetEditEntidade extends WidgetEntidade {

    function generate($id) {//coberto
        $controller = getControllerManager()->getControllerForTable("entidade");

        $painel = getPainelManager()->getPainel(PAINEL_ENTIDADE);
        $painel->setController($controller);
        $modelEntidade = $controller->loadSingle($id, $painel);
        $painel->setTitulo($modelEntidade->getDescricao());
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_entity");
    }

}

?>