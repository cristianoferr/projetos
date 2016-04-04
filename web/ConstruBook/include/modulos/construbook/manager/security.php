<?

class ConstrubookSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("fornecedor");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        if (($modulo == "fornecedor") ) {
            return $this->readableOnly($flagEdit);
        }
    }

}

?>