<?

//id=projeto
class WidgetEntidadesProjeto extends WidgetEntidade {

    function generate($id_projeto) {//coberto
        $controller = getControllerManager()->getControllerForTable("entidade");
        $painel = getPainelManager()->getPainel(PAINEL_ENTIDADES);
        $painel->setTitulo(translateKey("txt_entities"));
        $painel->setController($controller);
        $controller->loadRegistros("and entidade.id_projeto=$id_projeto", $painel);
        if (checkPerm("projeto", $id_projeto, true)) {
            $painel->adicionaLink(getHomeDir() . "entity/new/$id_projeto", translateKey("txt_new_entity"), false);
        }
        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_entities");
    }

}

?>