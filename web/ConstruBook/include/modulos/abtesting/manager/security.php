<?

class ABTestSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("abtest", "abtest_variacao", "tipo_abtest", "abtest_usuario");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        if (($modulo == "abtest") || ($modulo == "abtest_variacao") || ($modulo == "tipo_abtest") || ($modulo == "abtest_usuario")) {
            return $this->readableOnly($flagEdit);
        }
    }

}

?>