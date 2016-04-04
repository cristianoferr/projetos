<?php

/**
 * Classe responsável por montar as propriedades...
 *
 * @author CMM4
 */
abstract class InputProperties extends InputData {

    function placeholder(ElementMaker $elInput) {
        if ($this->coluna->isNumber()) {
            return;
        }
        if ($this->placeholder) {
            $elInput->setAtributo("placeholder", $this->placeholder);
        }
    }

    function value(ElementMaker $elInput) {
        
        $value = $this->getValue();
        if ($this->coluna->getTipoInput()==TIPO_INPUT_CURRENCY){
            $value=  convertcash($value, "");
        }

        if ($this->flagValueInside) {
            $elInput->setAtributo("value", $value);
        } else {
            $elInput->setValue($value);
        }
    }

    function generateScriptRowArray() {
        if (sizeOf($this->arrRowIDs) > 0) {
            $rows = "#";
            $script = "var arr" . $this->formName . " = [";
            for ($c = 0; $c < sizeOf($this->arrRowIDs); $c++) {
                $rowID = $this->arrRowIDs[$c];
                $rows.=",\"$rowID\"";
            }
            $rows = str_replace("#,", "", $rows);
            $script.=$rows;
            $script.="];";

            return ElementMaker::criaScript($script);
        }
    }

    function onFocus(ElementMaker $elInput) {
        $v = "";
        for ($c = 0; $c < sizeof($this->arrOnfocus); $c++) {
            $v.=$this->arrOnfocus[$c] . " ";
        }
        $elInput->setAtributo("onfocus", $v);
    }

    function autoFocus(ElementMaker $elInput) {
        if ($this->isFirst) {
            $this->isFirst = false;
            $elInput->ativaFlag("autofocus");
        }
    }

    function css(ElementMaker $elInput) {
        $v = $this->getClasses() . " form-control";
        $elInput->setClass($v);
    }

    function onChange(ElementMaker $elInput) {
        $flagTexto = "F";
        if ($this->isText) {
            $flagTexto = "T";
        }

        $atualiza = " atualizaGenerico(this,\"" . $this->coluna->getDbName() . "\",\"" . $this->model->getId() . "\",\"" . $flagTexto . "\",\"" . $this->model->getTableName() . "\");";
        if (!$this->flagAjax) {
            $atualiza = "";
        }
        if ($this->flagInclusao) {
            $atualiza = "";
        }
        if ($this->arrOnchange) {
            foreach ($this->arrOnchange as $value) {
                $atualiza.=$value;
            }
        }

        $elInput->setAtributo($this->onChangeEvent, $atualiza);
    }

    function readonly(ElementMaker $elInput) {
        if ($this->flagInclusao) {
            return;
        }
        if ($this->coluna->isReadOnly()) {
            $elInput->ativaFlag("disabled");
        }
    }

    function id(ElementMaker $elInput) {
        $elInput->setId($this->getInputId());
    }

    function name(ElementMaker $elInput) {
        $elInput->setName($this->getDbName());
    }

    function size(ElementMaker $elInput) {
        if (!$this->flagUsesSize) {
            return;
        }
        $elInput->setAtributo("size", $this->coluna->limiteTamanho());
    }

    function maxSize(ElementMaker $elInput) {
        if (!$this->flagUsesSize) {
            return;
        }
        $elInput->setAtributo("maxlength", $this->coluna->limiteTamanho());
    }

    function hintText(ElementMaker $elInput) {
        if (!$this->showHints) {
            return;
        }
        $v = $this->coluna->getHintText();
        if (isset($v)) {
            $nmCampo = $this->getInputId();

            $elInput->setScript("$('#$nmCampo').tooltip();");
            $elInput->setAtributo("data-toggle", "tooltip")->setAtributo("data-placement", "top")->setAtributo("title", $v);
        }
    }

    function required(ElementMaker $elInput) {
        if ($this->coluna->isRequired()) {
            $elInput->ativaFlag("required");
        }
    }

}

?>