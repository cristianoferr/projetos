<?

//essa classe vai ter um comportamento similar a painel_base_inputs, servindo para desenhar os elementos virtuais na tela
class MetaInputs {

    private $coluna, $model, $uniqueId;

    function MetaInputs() {
        
    }

    function setValor($valor) {
        $this->model = $valor;
        $this->coluna = $valor->getColuna();
    }

    function escreveCelula($rowCount, $colCount, ElementMaker $elRoot) {
        $this->uniqueID = "cell_" . $rowCount . "_" . $colCount;
        $elTd = elMaker("td", $elRoot)->setId("celula_" . $this->uniqueID);
        $elTd->setAtributo("onclick", "mostraInput('" . $this->uniqueID . "')");

        $this->escreveValor($elTd);
        $this->escreveInput($elTd);
    }

    function escreveValor(ElementMaker $elRoot) {
        $elDiv = elMaker("div", $elRoot)->setAtributo("style", "display:block;");
        $elDiv->setId("viewDiv_" . $this->uniqueID)->setValue($this->model->getValorFormatado());
    }

    function escreveInput(ElementMaker $elRoot) {
        $elDiv = elMaker("div", $elRoot)->setAtributo("style", "display:none;");
        $elDiv->setId("txtDiv_" . $this->uniqueID)->setValue($this->model->getValorFormatado());

        $this->escreveInputBaseadoNoTipo($elDiv);
    }

    function escreveInputBaseadoNoTipo(ElementMaker $elRoot) {
        $tipo = $this->coluna->getTipoInput();
        $inputMaker = new InputBusiness();

        //$inputMaker->setId("input_" . $this->uniqueID);
        $inputMaker->setValue($this->model->getValorFormatado());
        $flagTexto = "T";
        if ($tipo == TIPO_INPUT_SELECT_FK) {
            $flagTexto = "F";
            $inputMaker->setDictOptions($this->model->getComboDict());
        }
        if ($tipo == TIPO_INPUT_INTEIRO) {
            $flagTexto = "F";
        }
        $inputMaker->addOnChange($this->getInputOnChange($flagTexto));
        $inputMaker->mostra($elRoot);
    }

    /*
      function escreveInputTexto($tipo, $flagNumeric) {
      ?>!!!<input type="<? echo $tipo ?>" style="width:90%;" id="input_<? echo $this->uniqueID; ?>" value="<? echo $this->model->getValorFormatado(); ?>" <? echo $this->getInputOnChange("T"); ?>>
      <?
      //$this->habilitaScriptConteudo();
      }

      function escreveInputSelect() {
      ?><select style="width:90%;" id="input_<? echo $this->uniqueID; ?>" <? echo $this->getInputOnChange("F"); ?>">
      <option value="null"><? echo txt_none_selected; ?></option><?
      $this->model->escreveSelectOptions();
      ?></select><?
      //$this->habilitaScriptConteudo();
      } */

    function getInputOnChange($flagTexto) {
        $atualiza = "atualizaGenerico(this,\"valor_coluna\"," . $this->model->getId() . ",\"" . $flagTexto . "\",\"valor\");";
        return $atualiza;
    }

}

?>