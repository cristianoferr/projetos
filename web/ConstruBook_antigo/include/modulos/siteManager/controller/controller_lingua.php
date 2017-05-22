<?php

class LinguaController extends BaseController {

    function filtrosExtras() {
        return " and id_lang<>" . LINGUA_IPSUM;
    }

}

?>
