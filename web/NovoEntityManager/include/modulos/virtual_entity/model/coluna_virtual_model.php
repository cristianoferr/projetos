<?php

class ColunaVirtualModel extends ColunaAbstract implements IColuna {

    private $id; //id é especifico da coluna virtual: id_coluna

    function __construct($table, $id) {
        $this->table = $table;
        $this->id = $id;
        $this->setVisible();
    }

    function iniciaColunaComFK($caption = null, $dbName, $flagReadOnly, $fkTable, $tipoDado) {
        $this->iniciaColuna($caption, $dbName, $flagReadOnly, $tipoDado);
        $this->fkTable = $fkTable;
        return $this;
    }

    function getId() {//coberto
        return $this->id;
    }

    function isAllowingNull() {
        return true;
    }

    function getHintText() {
        return $this->caption;
    }

    function limiteTamanho() {
        return 100;
    }

}

?>