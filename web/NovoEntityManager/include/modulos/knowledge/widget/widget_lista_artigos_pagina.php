<?php

//id=pagina
class WidgetListaArtigosPagina extends WidgetListaArtigos {

    private $silent;

    function __construct($silent = false) {
        parent::__construct();
        $this->silent = $silent;
    }

    function generate($id) {
        $ret = parent::generate(getControllerForTable("elemento")->idForPage($id));
        return $ret;
    }

    function isEditavel() {
        return isAdmin();
    }

    function getTitle() {
        if ($this->silent) {
            return;
        }
        return translateKey("txt_want_to_know_more");
    }

}

?>