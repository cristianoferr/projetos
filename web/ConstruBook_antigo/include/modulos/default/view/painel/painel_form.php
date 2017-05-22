<?

class FormPainel extends BasePainel {

    private $textoErro;
    private $elBody;

    function setTextoErro($v) {
        $this->textoErro = $v;
    }

    //necessario para paineis pequenos...
    function getSmallClass() {
        
    }

    function cabecalho() {
        $elRoot = BasePainel::cabecalho();
        $this->flagInputSomente = true;
        $divRoot = elMaker("div", $elRoot)->setClass("panel panel-projeto");

        if (!$this->isModal()) {
            $divHeading = elMaker("div", $divRoot)->setClass("panel-heading");
            $h3 = elMaker("h3", $divHeading)->setClass("panel-title")->setValue($this->getTitulo());
        }
        if ($this->textoErro) {
            $divAlert = elMaker("div", $divRoot)->setClass("alert alert-danger")->setValue($this->textoErro);
        }
        $divBody = elMaker("div", $divRoot)->setClass("panel-body");
        $divBody->addClass($this->getSmallClass());
        $this->elBody = $divBody;
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        //$colunas = $this->getColunas();

        $this->escreveColunasEmAbas($model, $this->elBody);
    }

    function escreveTituloAba($idaba, $nmAba, ElementMaker $elRoot) {
        $class = "col-md-6";
        if ($idaba == ABA_COLUNA_DOC) {
            $class = "col-md-10";
        }
        $div = criaEl("div", $elRoot)->setClass($class);
        $hr = elMaker("hr", $div);
        $h3 = elMaker("h2", $div)->setValue($nmAba);
        return $div;
    }

    function escreveColuna(IColuna $coluna, $c, IModel $model, ElementMaker $elRoot) {
        if ($this->podeMostrarColuna($coluna)) {
            $input = $this->getInputCreator();
            if ($this->flagInputSomente) {
                $input->inputSomente();
            }
            $input->setPosAtual($c);
            $input->setFlagAjax($this->isAjaxON());
            $input->setColuna($coluna);
            $input->setModel($model);
            $input->setFormID($this->formName);
            $input->setFlagInclusao($this->isModoInclusao());
            $input->setPlaceholder($coluna->getCaption());

            $elDiv = elMaker("div", $elRoot)->setClass("form-group")->setId("div_" . $input->getUniqueID());
            $elP = elMaker("p", $elDiv);
            $elLabel = elMaker("label", $elP)->setAtributo("for", "input_" . $input->getUniqueID())->setValue($coluna->getCaption());
            $elDiv2 = elMaker("div", $elDiv)->setClass("input");
            $elDiv2->addElement($input->criaInput());
            $elP = elMaker("p", $elDiv)->setId("msg_" . $input->getUniqueID());
            return $elDiv2;
        }
    }

    function setDetalhesNovoRegistro($link, $texto) {
        $this->textoNovo = $texto;
        $this->linkNovo = $link;
    }

    function rodape(ElementMaker $elRoot) {
        if ($this->textoNovo) {
            $this->escreveCardNovo();
        }
        if ($this->isModoInclusao()) {
            $elDiv = elMaker("div", $this->elRoot)->setClass("buttons");
            $this->createSubmitButton($elDiv, "register_confirm");
        }
    }

    function createSubmitButton($elRoot, $key) {
        $elInput = elMaker("input", $elRoot)->setClass("btn btn-primary")->setType("submit")->setAtributo("value", translateKey($key));
    }

}

class RedirectPainel extends FormPainel {

    private $redirLink;

    function escreveColuna(IColuna $coluna, $c, BaseModel $model, ElementMaker $elRoot) {
        if ($this->podeMostrarColuna($coluna)) {
            $redir = "redirect('" . getHomeDir() . $this->redirLink . "'+this.value);";
            $this->getInputCreator()->addOnChange($redir);
            $elDiv = parent::escreveColuna($coluna, $c, $model, $elRoot);
        }
    }

    function setRedirLink($v) {
        $this->redirLink = $v;
    }

    function getSmallClass() {
        return "panel-small";
    }

}

?>