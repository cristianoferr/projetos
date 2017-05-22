<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Condicao
 *
 * @author CMM4
 */
class Condicao {

    private $condicao;
    private $valor;
    private $rotulo;
    private $campo;

    function __construct($condicao, $valor, $rotulo, $campo) {
        $this->condicao = $condicao;
        $this->valor = $valor;
        $this->rotulo = $rotulo;
        $this->campo = $campo;
    }

    function condicaoOk($row) {
        $valorModel = $row[$this->campo];
        if (verficaCondicao($valorModel, $this->condicao, $this->valor))
            return $this->rotulo;
        return false;
    }

    function getRotulo() {
        return $this->rotulo;
    }

    function getCondicao() {
        return $this->condicao;
    }

    function getValor() {
        return $this->valor;
    }

    function getCampo() {
        return $this->campo;
    }

}

?>
