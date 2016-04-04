<?

class WidgetBase {

    private $flagModal;
    private $paginaReferrer;
    private $token;

    function setModal() {
        $this->flagModal = true;
    }

    function getWidgetsArray() {
        erroFatal("getWidgetsArray() undefined for " . get_class($this));
    }

    function generate($id) {
        erroFatal("generate() undefined for " . get_class($this));
    }

    function isModal() {
        return $this->flagModal;
    }

    function __construct() {
        //parent::__construct();
    }

    function initToken($token) {
        $this->paginaReferrer = getPaginaAtual();
        $this->token = $token;
        $_SESSION["w_pagina_$token"] = getPaginaAtual();
    }

    function setToken($token) {
        $this->token = $token;
    }

    function getPaginaReferrer() {

        if ($this->paginaReferrer) {
            return $this->paginaReferrer;
        }
        if ($this->token) {
            return $_SESSION["w_pagina_" . $this->token];
        }
    }

    function getWidgetFor($nome) {
        
    }

    function getTitle() {
        return "Title";
    }

    function show($id) {
        $this->generate($id)->mostra();
    }

    function getVars() {
        $vars = "projeto=" . projetoAtual();
        return $vars;
    }

}

?>