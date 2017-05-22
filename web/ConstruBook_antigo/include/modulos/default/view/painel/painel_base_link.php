<?

class GerenciaLinks extends GerenciaAba {

    private $linksRodape;
    private $linksAcao; //para cada registro (exemplo: editar, remover)
    private $linksForm;
    private $linksFiltro;

    function __construct() {
        parent::__construct();
        $this->linksRodape = array();
        $this->linksAcao = array();
        $this->linksForm = array();
        $this->linksFiltro = array();
    }

    function adicionaLink($url, $texto, $flagConfirm) {
        $link = new LinkView($texto, $url, false, $flagConfirm);

        array_push($this->linksRodape, $link);
        return $link;
    }

    function adicionaInputForm($texto, $valor) {
        $link = new LinkView($texto, $valor, false, false);
        array_push($this->linksForm, $link);
        return $link;
    }

    function getArrayWithPostValues() {
        $arr = array();
//$painel->adicionaInputForm("entidade",$id_entidade);
        for ($c = 0; $c < sizeOf($this->linksForm); $c++) {
            $input = $this->linksForm[$c]->getText();
            $new_array = array($input => $_POST[$input]);
            $arr = array_merge($arr, $new_array);
        }
        return $arr;
    }

    function adicionaLinkImportante($url, $texto, $flagConfirm) {
        $link = new LinkView($texto, $url, true, $flagConfirm);
        array_push($this->linksRodape, $link);
        return $link;
    }

    function adicionaAcao($url, $texto, $flagConfirm) {
        $link = new LinkView($texto, $url, false, $flagConfirm);
        array_push($this->linksAcao, $link);
        return $link;
    }

    function adicionaAcaoIcone($url, $texto, $icone, $flagConfirm) {
        $link = new LinkView($texto, $url, false, $flagConfirm);
        $link->setIcone($icone);
        array_push($this->linksAcao, $link);
        return $link;
    }

    function adicionaAcaoGlyph($url, $texto, $icone, $flagConfirm) {
        $link = new LinkView($texto, $url, false, $flagConfirm);
        $link->setGlyph($icone);
        array_push($this->linksAcao, $link);
        return $link;
    }

    function defineCondicaoAcao($link, $nomeCampo, $condicao, $valor) {
        $link->setCondicao($nomeCampo, $condicao, $valor);
    }

    //$painel->adicionaAcaoIcone(getHomeDir()."property/".projetoAtual()."/".entidadeAtual()."/",txt_edit,ICON_EDIT,false);
    //$painel->adicionaAcaoImportante(getHomeDir()."property/delete/".projetoAtual()."/".entidadeAtual()."/",txt_remove,true);
    //$painel->adicionaAcaoIcone(getHomeDir()."property/delete/".projetoAtual()."/".entidadeAtual()."/",txt_remove,ICON_DELETE,true);
    function adicionaAcaoEditar($url) {
        return $this->adicionaAcaoGlyph($url, translateKey("txt_edit"), "glyphicon glyphicon-edit", false);
    }

    function adicionaAcaoSeguir($url) {
        return $this->adicionaAcaoIcone($url, translateKey("txt_edit"), ICON_SEGUIR, false);
    }

    function adicionaAcaoRemover($url) {
        return $this->adicionaAcaoIcone($url, translateKey("txt_remove"), ICON_DELETE, true);
    }

    function adicionaAcaoCancelar($url) {
        return $this->adicionaAcaoIcone($url, translateKey("txt_cancel"), ICON_CANCEL, true);
    }

    function adicionaAcaoImportante($url, $texto, $flagConfirm) {
        $link = new LinkView($texto, $url, true, $flagConfirm);
        array_push($this->linksAcao, $link);
        return $link;
    }

    function drawInputs(ElementMaker $elRoot) {
        $links = $this->linksForm;
        //echo "drawInputs:".sizeOf($links);
        for ($c = 0; $c < sizeOf($links); $c++) {
            $link = $links[$c];
            $link->drawInput($elRoot);
        }
    }

    function drawAcoes($model, ElementMaker $elRoot = null) {
        $this->drawAcoesID($model->getId(), $model, $elRoot);
    }

    function temAcoes() {
        return sizeOf($this->linksAcao);
    }

    function drawAcoesID($modelID, $model, ElementMaker $elRoot = null) {
        $links = $this->linksAcao;
        for ($c = 0; $c < sizeOf($links); $c++) {
            $link = $links[$c];
            $link->drawAcao($modelID, $model, $elRoot);
        }
    }

    function drawLinks(ElementMaker $elRoot = null) {
        $links = $this->linksRodape;
        for ($c = 0; $c < sizeOf($links); $c++) {
            $link = $links[$c];
            $link->draw($elRoot);
        }
    }

    function adicionaFiltro($ident, $url, $texto) {
        $link = new LinkView($texto, $url . $ident, $ident, false);
        array_push($this->linksFiltro, $link);
    }

    function drawFiltros(ElementMaker $elRoot = null) {
        $links = $this->linksFiltro;
        for ($c = 0; $c < sizeOf($links); $c++) {
            $link = $links[$c];
            $link->drawFiltro($elRoot);
        }
    }

}

?>