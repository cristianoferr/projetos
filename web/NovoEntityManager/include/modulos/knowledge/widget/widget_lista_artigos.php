<?php

//id=elemento
class WidgetListaArtigos extends WidgetSiteManager {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("artigo");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_ARTIGOS);
        $filtro = "and id_lingua=" . getManager()->getDefaultLang();
        if ($this->isEditavel()) {
            $filtro = "";
        }
        //inicioDebug();
        $controller->loadRegistros(" and id_elemento=$id $filtro", $painel);
        // fimDebug();
        $painel->setController($controller);
        $painel->setIdElemento($id);
        $painel->setModal($this->isModal());
        $painel->setEditavel($this->isEditavel());
        $painel->setTitulo($this->getTitle());
        return $painel->generate();
    }

    function isEditavel() {
        return false;
    }

    function getTitle() {
        return "";
    }

}

?>