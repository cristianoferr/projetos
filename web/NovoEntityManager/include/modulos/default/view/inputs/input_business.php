<?php

interface IInputMaker {

    function reset();

    function setPosAtual($c);

    function setFlagAjax($flag);

    function setColuna(IColuna $coluna);

    function setModel(IModel $model);

    function setFormID($formId, $rowId = null);

    function setFlagInclusao($flag);

    function setPlaceholder($caption);

    function criaCelula($flagInclusao); //retorna um elemento

    function conclui(ElementMaker $elroot);

    function setPainel($painel);
}

/**
 * Essa classe conhece o negócio, podendo acessar os controllers para pegar os dados, de forma genérica
 *
 * @author CMM4
 */
class InputBusiness extends InputGenerator implements IInputMaker {

    protected $controller;
    protected $elInput;

    function __construct(IController $controller = null) {
        $this->controller = $controller;
        parent::__construct();
    }

    function reset() {
        parent::reset();
        $this->elInput = false;
        $this->flagValueInside = true;
        $this->isText = true;
        $this->showHints = true;
        $this->setOnChangeEvent("onchange");
        $this->showValueIfReadonly = false;
    }

    function inicializaInput($tipoDado) {
        // write("tipoDado:$tipoDado ");
        if ($tipoDado == TIPO_INPUT_INTEIRO) {
            $this->elInput = $this->configuraInputInteiro();
            $this->complementaInput($this->elInput);
            return;
        }
        if ($tipoDado == TIPO_INPUT_BOOLEAN) {
            $this->elInput = $this->configuraBoolean();
            return;
        }

        if ($tipoDado == TIPO_INPUT_CURRENCY) {
            $this->elInput = $this->configuraInputFloat();
            return;
        }
        if ($tipoDado == TIPO_INPUT_FLOAT) {
            $this->elInput = $this->configuraInputFloat();
            return;
        }


        if ($tipoDado == TIPO_INPUT_UNIQUE_ID_PROJETO) {
            $this->elInput = $this->configuraInputIDUniqueProjeto();
            return;
        }
        if ($tipoDado == TIPO_INPUT_EMAIL) {
            $this->elInput = $this->configuraEmail();
            $this->complementaInput($this->elInput);
            return;
        }
        if ($tipoDado == TIPO_INPUT_TEXTO_CURTO) {
            $this->elInput = $this->configuraTextoCurto();
            $this->complementaInput($this->elInput);
            return;
        }
        if ($tipoDado == TIPO_INPUT_SEQUENCE) {
            $this->elInput = $this->configuraSequence();
            return;
        }
        if ($tipoDado == TIPO_INPUT_SELECT_FK) {
            $this->elInput = $this->configuraSelectFK();
            return;
        }


        if ($tipoDado == TIPO_INPUT_RADIO) {
            if ($this->isHorizontal) {
                $this->elInput = $this->configuraSelectFK();
            } else {
                $this->elInput = $this->configuraRadioFK();
            }
            return;
        }

        if ($tipoDado == TIPO_INPUT_RADIO) {
            if ($this->isHorizontal) {
                $this->elInput = $this->configuraSelectFK();
            } else {
                $this->elInput = $this->configuraRadioFK();
            }
            return;
        }



        if ($tipoDado == TIPO_INPUT_RADIO_SELECT) {
            if ($this->isHorizontal) {
                $this->elInput = $this->configuraSelectFK();
            } else {
                $this->elInput = $this->configuraRadioSelectFK();
            }
            return;
        }

        if ($tipoDado == TIPO_INPUT_UNIQUE_EMAIL) {
            $this->elInput = $this->configuraUniqueEmail();
        }
        if ($tipoDado == TIPO_INPUT_SENHA) {
            $this->elInput = $this->configuraInputSenha();
            $this->complementaInput($this->elInput);
            return;
        }

        if ($this->coluna->getTipoInput() == TIPO_INPUT_HTML) {
            $this->elInput = $this->configuraInputHtml();
            return;
        }
        if ($this->coluna->getTipoInput() == TIPO_INPUT_TEXTAREA) {
            $this->elInput = $this->configuraInputTextarea();
            return;
        }
        if ($this->coluna->getTipoInput() == TIPO_INPUT_DATA) {
            $this->elInput = $this->configuraInputData();
            $this->complementaInput($this->elInput);
            return;
        }

        if ($this->coluna->getTipoInput() == TIPO_INPUT_DATATYPE) {
            $this->elInput = $this->configuraInputDatatype();
            $this->complementaInput($this->elInput);
            return;
        }
    }

    function criaInput() {
        $this->reset();
        $tipoDado = $this->coluna->getTipoInput();
        $this->inicializaInput($tipoDado);
        if (!$this->elInput) {
            erroFatal("Tipo $tipoDado desconhecido");
        }

        return $this->elInput;
    }

    function getDictOptions() {


        $dictValores = $this->getArrayValoresCombo();
        return $dictValores;
    }

    function complementaInput(ElementMaker $elInput) {
        $this->placeholder($elInput);
        $this->name($elInput);
        $this->id($elInput);
        $this->css($elInput);
        $this->readonly($elInput);
        $this->required($elInput);
        $this->value($elInput);
        $this->hintText($elInput);
        $this->size($elInput);
        $this->maxSize($elInput);
        $this->autoFocus($elInput);
        $this->onFocus($elInput);
        $this->onChange($elInput
        );
    }

    function escreveCelula($flagInclusao) {
        $elCelula = $this->criaCelula($flagInclusao);
        $elCelula->mostra
        ();
    }

    function preparaCelula() {
        $elCelula = elMaker("div")->setId("celula_" . $this->uniqueID());
        if ((!$this->flagInclusao) && ($this->coluna->isReadOnly())) {
            $elCelula->addClass("noteditable");
        }
        $elCelula->addClass($this->formName . "_" . $this->coluna->getDbName());
        if ($this->isHideable()) {
            $elCelula->setAtributo("onmouseover ", "mostraInput('" . $this->uniqueID() . "' );");
            $elCelula->setAtributo("onclick ", "mostraInput('" . $this->uniqueID() . "' );");
        }
        return $elCelula;
    }

    function criaCelula($flagInclusao) {
        $this->flagInclusao = $flagInclusao;
        $elCelula = $this->preparaCelula();

        if ($this->isInputVisible($flagInclusao)) {
            $elCelula->addElement($this->criaInput());
        } else {
            if ($this->coluna->isReadOnly()) {
                $this->criaDivValor($elCelula, "block", false);
                //$elCelula->setValue($this->model->getValor($this->coluna));
            } else {
                $elCelula->setValue(" ");
                $this->criaDivEditavel($elCelula, "none", $this->criaInput());
                $this->criaDivValor($elCelula, "block", true);
                $elHack = elMaker("div", $elCelula)->setStyle("position:absolute")->setValue("&nbsp;
                ");
            }
        }
        return $elCelula;
    }

    function criaDivEditavel($elCelula, $display, $elInput) {
        $elDiv = elMaker("div", $elCelula)->setId("txtDiv_" . $this->uniqueID())->setStyle("display:$display;
                ");
        $elDiv->addElement($elInput
        );
    }

    function criaDivValor($elCelula, $display, $flagPegaValorInput) {
        $elDiv = elMaker("div", $elCelula)->setId("viewDiv_" . $this->uniqueID())->setStyle("display:$display;
                ");
        if ($flagPegaValorInput) {
            $elDiv->setScript("pegaConteudoInput(" . $this->getInputId() . ", viewDiv_" . $this->uniqueID() . ");");
            $elDiv->setValue($this->getValue());
        } else {
            $elDiv->setValue($this->getValueFormatted());
        }
    }

    function conclui(ElementMaker $elroot) {
        $elroot->addElement($this->generateScriptRowArray());
    }

}

?>