<?

class PainelModal {

    private $codSeletor, $txtBotao, $txtBody, $txtBotaoSave, $classBotao;
    private $url;
    private $urlForm, $acao, $txtTitle, $hintBotao, $idBotao;
    private $scripts;

    function __construct($codSeletor, $txtBotao, $txtBody, $txtBotaoSave, $classBotao = null) {
        $this->codSeletor = $codSeletor;
        $this->txtBotao = $txtBotao;
        $this->txtBody = $txtBody;
        $this->txtBotaoSave = $txtBotaoSave;
        $this->classBotao = $classBotao;
        $this->txtTitle = translateKey("txt_edit");
        $this->scripts = "";
    }

    function setHintBotao($v) {
        $this->hintBotao = $v;
    }

    function setIdBotao($v) {
        $this->idBotao = $v;
    }

    function setTitle($title) {
        $this->txtTitle = $title;
    }

    function setUrlConteudo($url) {
        $this->url = $url;
    }

    //preparaSubmit() precisa ser definido na pagina componente
    function dadosForm($urlForm, $acao) {
        $this->urlForm = $urlForm;
        $this->acao = $acao;
    }

    function hintBotao(ElementMaker $elSpan) {
        if ($this->hintBotao) {
            $elSpan->setId('btn_' . $this->idBotao)->setAtributo("data-toggle", 'tooltip')->setAtributo("data-placement", 'top')->setTitle($this->hintBotao);
            $elSpan->setScript("$('#btn_" . $this->idBotao . "').tooltip();");
        }
    }

    function escreveBotao() {
        $elLink = elMaker("a")->setAtributo("data-toggle", "modal")->setHref("#" . $this->codSeletor)->setClass("btn btn-warning");
        $elSpan = elMaker("span", $elLink)->setClass($this->classBotao)->setValue($this->txtBotao);
        $this->hintBotao($elSpan);
        return $elLink;
    }

    function checkIfIsForm() {
        if ($this->urlForm) {
            $form = elMaker("form")->setMethod("post")->setId("modalFrm")->setAtributo("onsubmit", "return preparaSubmit();")->
                    setAtributo("action", getHomeDir() . $this->urlForm . "#" . ANCORA_INICIO);
            $input = elMaker("input", $form)->setType("hidden")->setName("acao")->setAtributo("value", $this->acao);
            return $form;
        } else {
            return elMaker("");
        }
    }

    function generate() {
        $elRoot = elMaker("");
        $elRoot->addElement($this->escreveBotao());

        $form = $this->checkIfIsForm($elRoot);
        $elRoot->addElement($form);

        $form->addElement($this->modalWindow());

        $this->carregaUrl($elRoot);
        $elRoot->setScript($this->scripts);
        return $elRoot;
    }

    function mostra() {
        $this->generate()->mostra();
    }

    function modalWindow() {
        $divRoot = elMaker("div")->setClass("modal fade")->setId($this->codSeletor)->setAtributo("tabindex", "-1")->setAtributo("role", "dialog");
        $div1 = elMaker("div", $divRoot)->setClass("modal-dialog");
        $div2 = elMaker("div", $div1)->setClass("modal-content");
        $this->windowTitle($div2);
        $this->windowBody($div2);
        $this->submitButton($div2);

        return $divRoot;
    }

    function windowBody(ElementMaker $elRoot) {
        $div1 = elMaker("div", $elRoot)->setClass("modal-body modal-body-" . $this->codSeletor)->setValue($this->txtBody);
    }

    function windowTitle(ElementMaker $elRoot) {
        $div1 = elMaker("div", $elRoot)->setClass("modal-header");
        $button = elMaker("button", $div1)->setClass("close")->setType("button")->setAtributo("data-dismiss", "modal")->setAtributo("aria-hidden", "true")->setValue("&times;");
        $h4 = elMaker("h4", $div1)->setClass("modal-title")->setValue($this->txtTitle);
    }

    function submitButton(ElementMaker $elRoot) {
        $div1 = elMaker("div", $elRoot)->setClass("modal-footer");
        if ($this->urlForm) {
            $button = elMaker("button", $div1)->setClass("btn btn-default")->setType("button")->setAtributo("data-dismiss", "modal")->setValue(translateKey("txt_close"));
            $input = elMaker("input", $div1)->setClass("btn btn-primary")->setType("submit")->setAtributo("value", $this->txtBotaoSave);
        } else {
            $button = elMaker("button", $div1)->setClass("btn btn-primary")->setType("button")->setAtributo("data-dismiss", "modal")->setValue($this->txtBotaoSave);
        }
    }

    function carregaUrl() {
        if ($this->url) {
            $url = getHomeDir() . $this->url;
            $this->scripts.=" var loaded_" . $this->codSeletor . "=false;" . PHP_EOL;
            $this->scripts.="if (document.getElementById('" . $this->codSeletor . "') != undefined) { " . PHP_EOL;
            $this->scripts.=" $('#" . $this->codSeletor . "').on('show.bs.modal', function() {" . PHP_EOL;
            $this->scripts.=" if (loaded_" . $this->codSeletor . ") { " . PHP_EOL;
            $this->scripts.="return; " . PHP_EOL;
            $this->scripts.=" }" . PHP_EOL;
            $this->scripts.=" " . PHP_EOL;
            $this->scripts.=" var url = \"$url\";" . PHP_EOL;
            $this->scripts.=" $('.modal-body-" . $this->codSeletor . "').load(url);" . PHP_EOL;
            $this->scripts.=" loaded_" . $this->codSeletor . " = true;" . PHP_EOL;
            $this->scripts.="  });" . PHP_EOL;
            $this->scripts.="  }" . PHP_EOL;
        }
    }

}

?>