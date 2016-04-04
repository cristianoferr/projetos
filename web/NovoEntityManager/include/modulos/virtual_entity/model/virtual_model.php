<?php

class VirtualModel extends AbstractModel implements IModel {

    function __construct($table) {
        $this->table = $table;
    }

}

?>