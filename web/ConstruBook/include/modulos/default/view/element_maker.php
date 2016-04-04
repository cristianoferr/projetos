<?php

function elMaker($elName, ElementMaker $parent = null) {//coberto
    return ElementMaker::cria($elName, $parent);
}

function criaEl($elName, ElementMaker $parent = null) {//coberto
    return elMaker($elName, $parent);
}

/**
 * Nessa classe será possível gerar elementos html com seus atributos.
 *
 * @author CMM4
 */
class ElementMaker {

    private $arrAtributos;
    private $arrFlags; //flags são atributos sem valor, exemplo: autofocus, checked,selected, etc
    private $arrSub; //sub-elementos
    private $elName, $parent;
    private $value; //valor puro, fora das tags... seria o texto fora do <a></a>
    private $script; //script escrito logo depois do elemento

    static function cria($elName, ElementMaker $parent = null) {//coberto
        return new ElementMaker($elName, $parent);
    }

    static function criaScript($script) {
        $elScript = elMaker("script")->setType("text/javascript");
        $elScript->setValue($script);
        return $elScript;
    }

    function count() {
        return sizeof($this->arrSub);
    }

    function __construct($elName, ElementMaker $parent = null) {
        $this->arrAtributos = array();
        $this->arrSub = array();
        $this->elName = $elName;
        $this->arrFlags = array();
        if ($parent) {
            $this->parent = $parent;
            $parent->addElement($this);
        }

        if ($this->checkIfTagIsOpen($elName)) {
            $this->value = " ";
        }
    }

    function closedTag() {
        $this->value = null;
    }

    function checkIfTagIsOpen($tag) {//coberto
        if (($tag == "br") || ($tag == "hr") || ($tag == "link") || ($tag == "input") || ($tag == "!DOCTYPE") || ($tag == "meta")) {
            return false;
        }
        return true;
    }

    function addClass($val) {//coberto
        $c = $this->getAtributo("class");
        $c.=" $val";
        $this->setClass($c);
        return $this;
    }

    function setScript($script) {
        $this->script .= $script;
        return $this;
    }

    function setClass($val) {//coberto
        $this->setAtributo("class", $val);
        return $this;
    }

    function setScope($val) {//coberto
        $this->setAtributo("scope", $val);
        return $this;
    }

    function setSrc($val) {//coberto
        $this->setAtributo("src", $val);
        return $this;
    }

    function setId($val) {//coberto
        $this->setAtributo("id", $val);
        return $this;
    }

    function setRole($val) {//coberto
        $this->setAtributo("role", $val);
        return $this;
    }

    function setName($val) {//coberto
        $this->setAtributo("name", $val);
        return $this;
    }

    function setType($val) {//coberto
        $this->setAtributo("type", $val);
        return $this;
    }

    function setTitle($val) {
        $this->setAtributo("title", $val);
        return $this;
    }

    function setMethod($val) {//coberto
        $this->setAtributo("method", $val);
        return $this;
    }

    function setAction($val) {//coberto
        $this->setAtributo("action", $val);
        return $this;
    }

    function setStyle($val) {//coberto
        $this->setAtributo("style", $val);
        return $this;
    }

    function setOnclick($val) {//coberto
        $this->setAtributo("onclick", $val);
        return $this;
    }

    function setHref($val) {//coberto
        $this->setAtributo("href", $val);
        return $this;
    }

    function ativaFlag($flag) {//coberto
        $this->arrFlags[$flag] = true;
        return $this;
    }

    function setAtributo($atrib, $val = null) {//coberto
        $val = replaceString($val, "'", "\"");
        if (!$val) {
            unset($this->arrAtributos[$atrib]);
        } else {
            $this->arrAtributos[$atrib] = $val;
        }
        return $this;
    }

    function getAtributo($atrib) {//coberto
        if (isset($this->arrAtributos[$atrib])) {
            return $this->arrAtributos[$atrib];
        }
    }

    function addToParent(ElementMaker $el) {
        $this->parent->addElement($el);
    }

    function addElement(ElementMaker $el = null) {//coberto
        if (!$el) {
            return $this;
        }
        array_push($this->arrSub, $el);
        return $this;
    }

    function setValue($v = null) {//coberto
        if (!$v) {
            $v = " ";
        }
        $this->value = $v;
        return $this;
    }

    function getValue() {//coberto
        return $this->value;
    }

    function atributos() {
        $ret = "";
        foreach ($this->arrAtributos as $key => $value) {
            $ret.=" " . $key . "='$value'";
        }
        return $ret;
    }

    function flags() {
        $ret = "";
        foreach ($this->arrFlags as $key => $value) {
            $ret.=" " . $key;
        }
        return $ret;
    }

    function subElementos() {//coberto
        $ret = "";
        for ($c = 0; $c < sizeof($this->arrSub); $c++) {
            $sub = $this->arrSub[$c];
            $ret.= $sub->html();
        }
        $suf = "";
        if (AMBIENTE == AMBIENTE_LOCAL) {
            //$suf = PHP_EOL;
        }
        return trim($ret) . $suf;
    }

    function __toString() {//coberto
        return $this->html();
    }

    function html() {//coberto
        $ret = "";
        $close = "/";
        if ($this->isTagOpen()) {
            $close = "";
        }
        if (!$this->checkIfTagIsOpen($this->elName)) {
            $close = "";
        }

        if ($this->elName != "") {
            $ret = "<" . $this->elName . $this->atributos() . $this->flags() . "$close>";
        }
        $ret.= $this->subElementos();
        if ($this->value) {
            $ret.=trim($this->value);
        }
        if ($this->isTagOpen()) {
            $ret.= "</" . $this->elName . ">";
        }
        $ret.=$this->script();
        $suf = "";
        if (AMBIENTE == AMBIENTE_LOCAL) {
            $suf = PHP_EOL;
        }
        return $ret . $suf;
    }

    function script() {
        if ($this->script) {
            return "<script>$( document ).ready(function() {" . $this->script . "});</script>";
        }
    }

    function mostra() {
        echo $this->html();
    }

    /**
     * Retorna se a tag é aberta: "br" é fechada porque não tem valores nem sub-elementos
     */
    function isTagOpen() {
        if ($this->elName == "") {
            return false;
        }
        if ($this->value) {
            return true;
        }
        if (sizeof($this->arrSub) > 0) {
            return true;
        }
        return false;
    }

    function getElName() {
        return $this->elName;
    }

}

?>