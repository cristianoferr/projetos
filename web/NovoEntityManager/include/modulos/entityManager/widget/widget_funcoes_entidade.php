<?

class WidgetFuncoesEntidade extends WidgetEntidade {

    function generate($id) {//coberto
        $controller = getControllerManager()->getControllerForTable("funcao");
        $painel = getPainelManager()->getPainel(PAINEL_FUNC_ENT);
        $painel->setController($controller);
        $controller->loadRegistros("and funcao.id_entidade_pai=$id", $painel);
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_properties");
    }

}

?>