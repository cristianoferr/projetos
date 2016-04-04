<?

define("WIDGET_LISTA_ARTIGOS", 'list_artigos');
define("WIDGET_LISTA_ARTIGOS_PAGINA", 'list_artigos_pagina');
define("WIDGET_VIEW_ARTIGO", 'VIEW_ARTIGO');
define("WIDGET_LISTA_ARTIGOS_PAGINA_SUMARIO", 'wlaps');
define("WIDGET_EDIT_ARTIGO", 'edit_artigo');
define("WIDGET_MOSTRA_ARTIGO", 'mostra_artigo');
define("WIDGET_NEW_ARTIGO", 'new_artigo');

class WidgetKnowledge extends WidgetBase {

    function getWidgetsArray() {//coberto
        return array(WIDGET_LISTA_ARTIGOS => ELEMENTO_TESTE,
            WIDGET_LISTA_ARTIGOS_PAGINA => PAGINA_TESTE,
            WIDGET_VIEW_ARTIGO => ARTIGO_TESTE,
            WIDGET_LISTA_ARTIGOS_PAGINA_SUMARIO => PAGINA_TESTE,
            WIDGET_EDIT_ARTIGO => ARTIGO_TESTE,
            WIDGET_MOSTRA_ARTIGO => ARTIGO_TESTE,
            WIDGET_NEW_ARTIGO => ELEMENTO_TESTE);
    }

    function getWidgetFor($nome) {//coberto
        if ($nome == WIDGET_LISTA_ARTIGOS) {
            return new WidgetListaArtigos();
        }

        if ($nome == WIDGET_LISTA_ARTIGOS_PAGINA) {
            return new WidgetListaArtigosPagina();
        }
        if ($nome == WIDGET_VIEW_ARTIGO) {
            return new WidgetViewArtigo();
        }

        if ($nome == WIDGET_LISTA_ARTIGOS_PAGINA_SUMARIO) {
            return new WidgetListaArtigosPagina(true);
        }


        if ($nome == WIDGET_EDIT_ARTIGO) {
            return new WidgetEditArtigo();
        }

        if ($nome == WIDGET_NEW_ARTIGO) {
            return new WidgetNewArtigo();
        }

        if ($nome == WIDGET_MOSTRA_ARTIGO) {
            return new WidgetMostraArtigo();
        }
    }

    function getVars() {//coberto
        $vars = parent::getVars();
        return $vars . "&modulo=" . MODULO_KNOWLEDGE;
    }

}

?>