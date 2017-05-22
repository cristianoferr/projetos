<?php

/**
 * Input Generator: Não conhece a camada de negócio mas é capaz de montar os inputs a partir dos dados informados
 *
 * @author CMM4
 */
class InputGenerator extends InputProperties {

    function iniciaInput($input, $type = null) {
        $elInput = elMaker($input)->setType($type);

        return $elInput;
    }

    function reset() {
        
    }

    function configuraOptionedInput($inputName, $optionName, $dictValores = null) {
        $this->flagUsesSize = false;
        $el = $this->iniciaInput($inputName);
        $this->complementaInput($el);
        $el->setAtributo("value");
        $this->preencheOptions($el, $optionName, $dictValores);
        return $el;
    }

    function getScriptMudaOrdem($dif) {
        return "return mudaOrdem('" . $this->formID . "', arr" . $this->formName . ", $dif, '" . $this->coluna->getTable()->getTableName() . "');";
    }

    function configuraSequence() {
        $this->showInput = true;
        $elInput = elMaker("");
        $elLinkUp = elMaker("a", $elInput)->setHref("#")->setValue("&#9650;");
        $mudaOrdemUp = $this->getScriptMudaOrdem(-1);
        //echo $mudaOrdemUp;
        $elLinkUp->setOnclick($mudaOrdemUp);

        $elLinkDown = elMaker("a", $elInput)->setHref("#")->setValue("&#9660;");
        $mudaOrdemDown = $this->getScriptMudaOrdem(1);
        $elLinkDown->setOnclick($mudaOrdemDown);

        return $elInput;
    }

    function configuraSelectFK($dictValores = null) {
        $this->showValueIfReadonly = true;
        return $this->configuraOptionedInput("select", "option", $dictValores);
    }

    function configuraInputDatatype() {
        
    }

    function configuraRadioSelectFK() {
        $this->showValueIfReadonly = true;
        $dictValores = $this->getDictValoresFK();
        if ($dictValores->count() > LIMITE_RADIO_SELECT) {
            return $this->configuraSelectFK($dictValores);
        } else {
            return $this->configuraRadioFK($dictValores);
        }
    }

    function getDictValoresFK($dictValores = null) {
        if (!$dictValores) {
            $filtro = "";
            if ($this->painel) {
                $filtro = $this->painel->getFiltroColuna($this->coluna->getDbName());
            }
            $dictValores = $this->controller->geraArrayParaColuna($this->coluna, $this->model, $filtro);
        }
        return $dictValores;
    }

    function configuraRadioFK($dictValores = null) {
        $elRoot = elMaker("div");
        $this->showHints = false;
        $dictValores = $this->getDictValoresFK($dictValores);

        $array = $dictValores->getArray();
        $valorAtual = $this->getValue();
        $c = 0;
        foreach ($array as $chave => $valor) {
            $elOption = elMaker("input", $elRoot)->setType("radio");
            $this->complementaInput($elOption);
            $elOption->addClass("radio");
            $id = $elOption->getAtributo("id") . "_" . $chave;
            $elOption->setId($id);

            $elRoot->addElement($this->criaLabel($id, $valor['name']));

            if ((($valorAtual == FIRST_OPTION) && ($c == 0)) || ($chave == $valorAtual)) {
                $elOption->ativaFlag("checked");
            }
            $elOption->setAtributo("value", $chave);
            $c++;
        }

        return $elRoot;
    }

    function criaLabel($for, $value) {
        $el = elMaker("label")->setAtributo("for", $for)->setValue($value)->setClass("labelToggle");
        return $el;
    }

    function preencheOptions(ElementMaker $elInput, $optionType, $dictValores = null) {
        $dictValores = $this->getDictValoresFK($dictValores);

        $array = $dictValores->getArray();
        if ($this->allowNull()) {
            $elOption = elMaker($optionType, $elInput)->setAtributo("value", "null")->setValue(" ");
        }
        $prevGroup = "";
        $c = 0;
        $valorAtual = $this->getValue();
        foreach ($array as $chave => $valor) {
            $grupo = $dictValores->getAuxiliar($chave);
// write("grupo: $grupo");
            if ($grupo != $prevGroup) {

                $elInput = elMaker("optgroup", $elInput)->setAtributo("label", translateKey($grupo));
                $prevGroup = $grupo;
            }
            $elOption = elMaker($optionType, $elInput)->setAtributo("value", $chave)->setValue(" ");
            if ((($valorAtual == FIRST_OPTION) && ($c == 0)) || ($chave == $valorAtual)) {
                $elOption->ativaFlag("selected");
            }
            $elOption->setValue($valor['name']);
            $c++;
        }
    }

    function configuraInputIDUniqueProjeto() {
        $elRoot = elMaker("div");
        $elInputHidden = $this->iniciaInput("input", "hidden")->setName($this->getDbName())->setId("hid_" . $this->uniqueID());
        $elRoot->addElement($elInputHidden);
        $elForm = elMaker("form", $elRoot)->setId("srchProject")->setAtributo("method", "post");
        $elInput = elMaker("input", $elForm)->setId("search_project")->setAtributo("type", "text");
        $this->complementaInput($elInput);
        $elInput->setAtributo("onchange", "$('#hid_" . $this->getInputId() . "').val($(this).val())");
        $this->criaScriptVerificacaoUnique($elRoot);
        return $elRoot;
    }

    function criaScriptVerificacaoUnique($elRoot) {
        $elSpan = elMaker("span", $elRoot)->setId("msg_" . $this->getInputId())->setValue("");
        $script = "     var flag" . $this->getDbName() . " = '';" . PHP_EOL;
        $script.= "     $(document).ready(function() {" . PHP_EOL;
        $script.= "         $(\"#msg_" . $this->getInputId() . "\").slideUp();" . PHP_EOL;
        $script.= "         $(\"#" . $this->getInputId() . "\").keyup(function(event) {" . PHP_EOL;
        $script.= "             event.preventDefault();" . PHP_EOL;
        $script.= "             search_unique(\"msg_" . $this->getInputId() . "\", \"" . $this->getInputId() . "\", '" . $this->getInputId() . "', '" . $this->getTableName()
                . "', '" . $this->getDbName() . "'";
        if ($this->flagInclusao) {
            $script.= ",true);" . PHP_EOL;
        } else {
            $script.= ");" . PHP_EOL;
        }
        $script.= "         });" . PHP_EOL;
        $script.= "     });" . PHP_EOL;

        $elSpan->setScript($script);
    }

    function configuraInputHtml() {
        getFileManager()->loadUniqueJS(getHomeDir() . "ckeditor/ckeditor.js", 'ckeditor');
        $elInput = $this->iniciaInput("textarea");
        $this->flagValueInside = false;
        $script = $this->geraScriptUpdateTextArea();


        $elInput->setValue("");
        $this->complementaInput($elInput);
        $elInput->setScript($script);
        $elInput->setAtributo("onchange");

        return $elInput;
    }

    function configuraInputTextarea() {
        $elInput = $this->iniciaInput("textarea");
        $this->flagValueInside = false;
        $elInput->setValue("");
        $this->complementaInput($elInput);
        return $elInput;
    }

    function configuraInputInteiro() {
        $this->isText = false;
        return $this->iniciaInput("input", "number");
    }

    function configuraBooleanHorizontal() {
        $this->showValueIfReadonly = true;
        $dict = new Dicionario();
        $dict->setValor("T", array("name" => translateKey("txt_true")));
        $dict->setValor("F", array("name" => translateKey("txt_false")));
        return $this->configuraSelectFK($dict);
    }

    function configuraBoolean() {
        if ($this->isHorizontal) {
            return $this->configuraBooleanHorizontal();
        } else {
            $input = $this->iniciaInput("input", "checkbox");
            $input->addClass("ios-switch");
            $this->complementaInput($input);
        }
        return $input;
    }
    
    
    function configuraInputCurrency() {
        $this->isText = false;
        $input = $this->iniciaInput("input", "text");
        $this->setOnChangeEvent("onblur");
        $this->complementaInput($input);
        $input->addClass("currency");
        return $input;
    }

    function configuraInputFloat() {
        $this->isText = false;
        $input = $this->iniciaInput("input", "text");
        $this->setOnChangeEvent("onblur");
        $this->complementaInput($input);
        $input->addClass("currency");
        $input->setScript("$('#" . $this->getInputId() . "').maskMoney({thousands: '', decimal: '.'});");

        return $input;
    }

    function configuraTextoCurto() {
        return $this->iniciaInput("input", "text");
    }

    function configuraEmail() {
        return $this->iniciaInput("input", "email");
    }

    function configuraInputData() {
        return $this->iniciaInput("input", "datetime-local");
    }

    function configuraInputSenha() {
        return $this->iniciaInput("input", "password");
    }

    function configuraUniqueEmail() {
        $elRoot = elMaker("div");
        $elInputHidden = $this->iniciaInput("input", "hidden")->setName($this->getDbName())->setId("hid_" . $this->uniqueID());
        $elRoot->addElement($elInputHidden);
        $elForm = elMaker("form", $elRoot)->setId("searchform2")->setAtributo("method", "post");
        $elInput = elMaker("input", $elForm)->setId("searchform2")->setAtributo("type", "email");
        $this->complementaInput($elInput);
        $elInput->setAtributo("onchange", "$('#hid_" . $this->getInputId() . "').val($(this).val())");
        $this->criaScriptVerificacaoUnique($elRoot);
        return $elRoot;
    }

    function geraScriptUpdateTextArea() {
        $script = "var countUpdate = 0;";
        if (!$this->flagInclusao) {
            $script.="function delayedUpdate(dado, count) { ";
            $script.="  if (count != countUpdate){return;} ";
            $script.="  atualizaGenericoComValor(dado, '" . $this->coluna->getDbName() . "', " . $this->model->getId() . ", 'T', '" . $this->model->getTableName() . "');";
            $script.="  updateLock = false;";
            $script.="} ";
            $script.="function startDelayedUpdate(dado, c) { ";
            $script.="  setTimeout(function() { ";
            $script.="      delayedUpdate(dado, c);";
            $script.="  }, 2000);";
            $script.="} ";
            $script.="CKEDITOR.on('instanceCreated', function(e) { ";
            $script.="  e.editor.on('change', function(ev) { ";
            $script.="      countUpdate++;";
            $script.="      c = countUpdate;";
            $script.="      startDelayedUpdate(ev.editor.getData(), c);";
            $script.="  }); });";
        }
        $script.="var config = {extraPlugins: 'onchange'};";
        $script.="CKEDITOR.replace('input_" . $this->uniqueID() . "', config);";
        return $script;
    }

}

?>