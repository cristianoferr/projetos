<?

class SecurityBase {

    private $module;

    function __construct($module) {
        $this->module = $module;
    }

    function getModule() {
        return $this->module;
    }

    function readableOnly($flagEdit) {
        if ($flagEdit) {
            return isAdmin();
        }
        return true;
    }

    function controlsModule($modulo) {
        // $arr =;
        if (in_array($modulo, $this->getTables())) {
            return true;
        }
        return false;
    }

    /*    function validaAcesso($modulo, $id_modulo); //Verifica acesso leitura, die se falhar


      function validaEscrita($modulo, $id_modulo);

      function checaEscrita($modulo, $id_modulo);

      function checaLeitura($modulo, $id_modulo); */
}

interface SecurityCheck {

    function getTables();

    function checkPerm($modulo, $id_modulo, $flagEdit);
}

?>