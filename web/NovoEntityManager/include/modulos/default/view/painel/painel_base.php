<?

//Essa classe adiciona um filtro no input multiplo (select por exemplo)
//modo de uso: defino a coluna (id_cidade) e um filtro que será repassado ao inputMaker)
class ColunaFiltro {

    private $coluna;
    private $filtro;

    function __construct($col, $filtro) {
        $this->coluna = $col;
        $this->filtro = $filtro;
    }

    function getFiltro($colName) {
        if ($this->coluna == $colName) {
            return $this->filtro;
        }
    }

}

class BasePainel extends ViewBase implements IPainel {

    private $colunas;
    protected $formName;
    private $table;
    private $modoUtilizacao; //0=inclusão,1=edição,2=visualização (default)
    private $flagAjaxEnabled;
    private $flagOrderByEnabled;
    protected $countAtual;
    private $paginaInclusao;
    protected $elRoot;
    protected $filtroColuna;
    private $widgetActionDict; //dicionario com o nome da acao e o widget a ser chamado

    function BasePainel($formName = null, ITable $table = null) {
        $this->colunas = array();
        $this->formName = $formName;
        $this->table = $table;
        parent::__construct();
        $this->modoVisualizacao();
        $this->ativaAjax();
    }

    function setFormName($formName) {
        $this->formName = $formName;
    }

    function defineWidgetForAction($acao, $widgetName) {
        if (!$this->widgetActionDict) {
            $this->widgetActionDict = new Dicionario();
        }
        $this->widgetActionDict->setValor($acao, $widgetName);
    }

    function getWidgetForAction($acao) {
        if (!$this->widgetActionDict) {
            return;
        }
        return $this->widgetActionDict->getValor($acao);
    }

    function adicionaGlyphAcao($widgetKey, $btnType, $id, $btnLevel = "btn-warning") {
        $widget = $this->getWidgetForAction($widgetKey);
        if ($widget) {
            $link = getWidgetManager()->generateRedirectLinkForWidget($widget, $id);

            $link->setClass("btn-xs $btnLevel aa inline");
            $span = criaEl("span", $link)->setClass("glyphicon $btnType");
            return $link;
        }
    }

    function addFiltroColuna($coluna, $filtro) {
        if (!$this->filtroColuna) {
            $this->filtroColuna = array();
        }
        $fil = new ColunaFiltro($coluna, $filtro);
        array_push($this->filtroColuna, $fil);
    }

    function getFiltroColuna($coluna) {
        if (!$this->filtroColuna) {
            return "";
        }
        $ret = "";
        foreach ($this->filtroColuna as $fil) {
            $ret.=" " . $fil->getFiltro($coluna);
        }
        return $ret;
    }

    function getRootElement() {
        return $this->elRoot;
    }

    function setController(IController $controller) {
        parent::setController($controller);
        if (!$this->table) {
            $this->table = $controller->getTable();
        }
    }

    function setPaginaInclusao($pagina) {
        $this->paginaInclusao = $pagina;
    }

    function enableOrderBy() {
        $this->flagOrderByEnabled = true;
    }

    function isOrderByEnabled() {
        return $this->flagOrderByEnabled;
    }

    function addColuna($coluna, $idAba = null) {
        array_push($this->colunas, $coluna);
        if (!isset($idAba)) {
            $idAba = ABA_PRINCIPAL;
        }
        $this->adicionaColunaNaAba($coluna, $idAba);
    }

    function getArrayWithPostValues() {
        $arr = GerenciaLinks::getArrayWithPostValues();

        $colunas = $this->colunas;
        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            $v = $_POST[$coluna->getDbName()];
            if ($coluna->isNumber()) {
                $v = validaNumero($v, $coluna->getDbName() . " getArrayWithPostValues");
            } else {
                // $v = validaTexto($v, $coluna->getDbName() . " getArrayWithPostValues");
            }
            $new_array = array($coluna->getDbName() => $v);
            $arr = array_merge($arr, $new_array);
        }
        return $arr;
    }

    function addColunaWithDBName($dbname, $idAba = null) {//coberto
        if (!$this->table) {
            erroFatal("Table do painel não definida (campo: $dbname");
        }
        $colunas = $this->table->getColunas();
        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            if ($coluna->getDbName() == $dbname) {
                $this->addColuna($coluna, $idAba);
                return $coluna;
            }
        }
        erroFatal("Column not found: $dbname on form: " . $this->formName);
    }

    function podeMostrarColuna($coluna) {
        //writeDebug("vis?".$coluna->isVisible()." visInc?".$coluna->isVisibleInclusao());
        if (!$coluna->isVisible()) {
            return false;
        }

        if ($this->isModoInclusao()) {
            return $coluna->isVisibleInclusao();
        }
        return true;
    }

    function corpoInclusao() {
        $model = $this->getController()->loadEmptyModel();
        if ($this->getWidgetId()) {
            $model->setId($this->getWidgetId());
        }
        $model->setFlagInclusao(true);
        $this->registro($model);
    }

    function corpoNormal() {
        if ($this->getController()) {
            $this->getController()->showInfo();
            //echo get_class($this).get_class($this->getController())."|"."<br>";
            while ($model = $this->getController()->next()) {
                writeDebug("loop painel_base");
                $this->getController()->showInfo();
                $this->registro($model);
                $this->countAtual++;
            }
        }
    }

    function mostra() {
        if (!$this->elRoot) {
            $this->generate();
        }
        $this->elRoot->mostra();
    }

    function generate() {
        $this->cabecalho();
        $this->countAtual = 0;
        if ($this->isModoInclusao()) {
            $this->corpoInclusao();
        } else {
            $this->corpoNormal();
        }

        $this->rodape($this->elRoot);
        //  write("elRoot: " . $this->elRoot);
        return $this->elRoot;
    }

    function getCountAtual() {
        return $this->countAtual;
    }

    function cabecalho() {
        if ($this->isModoInclusao()) {
            $pagina = $this->table->getPaginaInclusao();
            if ($this->paginaInclusao) {
                $pagina = $this->paginaInclusao;
            }
            if (!$pagina) {
                die("Tabela " . $this->table->getTableName() . " não definiu pagina de inclusao");
            }
            $this->elRoot = criaEl("form")->setAtributo("role", "form")->setAction($pagina)->setMethod("post")->
                            setAtributo("onsubmit", "return isFormOk" . $this->formName . "();")->setScript("var formStatus = new Object;");

            $this->elRoot->setScript($this->atualizaFormStatus());

            $elInput = new ElementMaker("input", $this->elRoot);
            $elInput->setAtributo("type", "hidden")->setAtributo("name", "acao")->setAtributo("value", $this->inclusaoAcao);
            $this->drawInputs($this->elRoot);
        } else {
            $this->elRoot = criaEl("");
        }
        return $this->elRoot;
    }

    function atualizaFormStatus() {
        $script = " function isFormOk" . $this->formName . "() {";
        $script.= "   for (var i in formStatus){";
        $script.= "     if (formStatus[i] != '') {";
        $script.= "       return false;";
        $script.= "     }";
        $script.= "   }";
        $script.= "   return true;";
        $script.= "   }";
        $script.= "   function atualizaFormStatus() {";
        $script.= "     for (var i in formStatus){";
        $script.= "       var el = document.getElementById('div_' + i);";
        $script.= "       var elDesc = document.getElementById('msg_' + i);";
        $script.= "       if (formStatus[i] != '') {";
        $script.= "         $(el).addClass('has-error');";
        $script.= "       } else {";
        $script.= "         $(el).removeClass('has-error');";
        $script.= "       }";
        $script.= "    }";
        $script.= " }";
        return $script;
    }

    function registro(IModel $model) {
        
    }

    function infoRodape() {
        return "";
    }

    function mostraInfoRodape(ElementMaker $elTable = null) {
        $elDiv = elMaker("div", $elTable)->setStyle("float:left")->setValue($this->infoRodape());
    }

    function rodape(ElementMaker $elRoot) {
        if ((!$this->isModoInclusao()) && ($this->isModal())) {
            return;
        }

        if ($this->infoRodape()) {
            $this->mostraInfoRodape($elRoot);
        }

        $this->getInputCreator()->conclui($elRoot);

        $elDiv = elMaker("div")->setStyle("float:right");

        $this->showNewWidget($elDiv);
        if ($this->isModoInclusao()) {
            $elinput = elMaker("input", $elRoot)->setType("submit")->setClass("btn btn-sm btn-primary")->setAtributo("value", translateKey("txt_insert"));
        }
        $this->drawLinks($elDiv);

        if ($elDiv->count() > 0) {
            $elRoot->addElement($elDiv);
        }
    }

    //input
    function escreveCelula($coluna, $model, $formID, $pos, $tipo = "td") {
        $inputCreator = $this->getInputCreator();
        $inputCreator->setFlagAjax($this->isAjaxON());
        $inputCreator->setColuna($coluna);
        $inputCreator->setModel($model);
        $inputCreator->setFormID($formID);
        $inputCreator->setPosAtual($pos);

        //echo "escreve:" . $this->flagInputSomente;
        /* if ($this->flagInputSomente) {
          $this->getInputCreator()->inputSomente();
          } */
        $inputCreator->setFormName($this->formName);
        $inputCreator->setPainel($this);

        $elTd = elMaker($tipo)->setId("td_" . $this->getInputCreator()->uniqueID());
        $elTd->addElement($inputCreator->criaCelula($this->isModoInclusao()));
        return $elTd;
    }

    //sql
    function getSQLColunas($pkID) {
        return GeraSQLStatic::getSQLColunas($pkID, $this->colunas);
    }

    function getFromSQL($pkID) {
        return GeraSQLStatic::getFromSQL($this->colunas);
    }

    function getWhereSQL($pkID) {
        return GeraSQLStatic::getWhereSQL($this->colunas);
    }

    function getOrderbySQL() {
        if ($this->isOrderByEnabled()) {
            if (!$this->getCampoOrderBy()) {
                return;
            }
            return $this->getCampoOrderBy() . " " . $this->getAscOrderBy();
        }
    }

//getters e setters


    function getFormName() {
        return $this->formName;
    }

    function getColunas() {//coberto
        return $this->colunas;
    }

    function limpaColunas() {//coberto
        $this->colunas = array();
    }

    function modoEdicao() {
        $this->modoUtilizacao = 1;
    }

    function modoInclusao($acao = ACAO_INSERIR) {
        $this->inclusaoAcao = $acao;
        $this->modoUtilizacao = 0;
    }

    function modoVisualizacao() {
        $this->modoUtilizacao = 2;
    }

    function ativaAjax() {
        $this->flagAjaxEnabled = true;
        //echo "ativaAjax: ".$this->isAjaxON();
    }

    function desativaAjax() {
        $this->flagAjaxEnabled = false;
        //echo "desativaAjax: ".$this->isAjaxON();
    }

    function isAjaxON() {
        return $this->flagAjaxEnabled;
    }

    function isModoEdicao() {
        return ($this->modoUtilizacao == 1);
    }

    function isModoInclusao() {
        return ($this->modoUtilizacao == 0);
    }

    function isModoVisualizacao() {
        return ($this->modoUtilizacao == 2);
    }

    function modoAtual() {
        return $this->modoUtilizacao;
    }

}

?>