<?

//id=projeto
class WidgetEntidades extends WidgetEntidade {

    function generate($id_projeto) {//coberto
        $controller = getControllerManager()->getControllerForTable("entidade");

        //Painel Entidades
        $painel = getPainelManager()->getPainel(PAINEL_ENTIDADES);
        $painel->setController($controller);
        $painel->setTitulo(translateKey("txt_entities"));
        $controller->loadRegistros("and entidade.id_projeto=$id_projeto", $painel);
        $painel->adicionaLink(getHomeDir() . "entity/new/$id_projeto", translateKey("txt_new_entity"), false);
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_entities");
    }

}

?>