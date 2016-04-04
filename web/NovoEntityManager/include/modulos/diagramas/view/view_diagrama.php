<?

class DesenhaDiagrama {

    private $controller, $idDiagrama, $scriptDesenho;
    private $arrComponents;
    private $randomizeLine; //usado para dar um valor aleatório à linha, evitando que fiquem uma em cima da outra.

    function __construct($idDiagrama) {
        $this->idDiagrama = $idDiagrama;
        $this->arrComponents = array();
        $this->randomizeLine = 1;
    }

    function setRandomizeLine($v) {
        $this->randomizeLine = $v;
    }

    function addColunaWithDBName($dbname) {
        erroFatal("addColunaWithDBName not implemented.");
    }

    //interface
    function cabecalho() {
        $canvas = new ElementMaker("canvas");
        $canvas->setAtributo("id", "background")->setAtributo("width", MAX_WIDTH)->setAtributo("height", MAX_HEIGHT)->setValue(" ");
        $canvas->mostra();
        $this->iniciaVariaveisScript();
        $this->scriptDesenho = "";
    }

    function iniciaVariaveisScript() {
        ?><script>
            var idDiagrama =<? echo $this->idDiagrama; ?>;
            var idProjeto =<? echo projetoAtual(); ?>;
            var _maxX =<? echo MAX_WIDTH; ?>;
            var _maxY =<? echo MAX_HEIGHT; ?>;
            var _minX =<? echo MIN_WIDTH; ?>;
            var _minY =<? echo MIN_HEIGHT; ?>;
            var flagRandomizeLine = <? echo $this->randomizeLine; ?>;
            InitDragDrop();
        </script><?
    }

    function attribSourceToView(IController $controller) {
        $arr = array();
        while ($model = $controller->next()) {
            array_push($arr, $model);
            foreach ($this->arrComponents as $comp) {
                if ($comp->getModelId() == $model->getId()) {
                    $comp->setModelSource($model);
                    $model->setViewComponent($comp);
                }
            }
        }
        return $arr;
    }

    function getIdDiagrama() {
        return $this->idDiagrama;
    }

    function rodape() {
        echo "<script>" . $this->scriptDesenho . "</script>";
    }

    function mostra() {
        $this->cabecalho();


        while ($model = $this->getController()->next()) {
            $this->registro($model);
        }

        $this->verificaPosicoes($this->arrComponents);
        foreach ($this->arrComponents as $comp) {
            $this->desenha($comp);
        }

        $this->verificaOverlapJS($this->arrComponents);

        $this->rodape();
    }

    function verificaPosicoes($arrComponents) {
        
    }

    function verificaOverlapJS($arrComponents) {
        $script = "var arrElements=[";
        foreach ($arrComponents as $comp) {
            $script.=$comp->getDragElement() . ",";
        }
        $script.="$";
        $script = replaceString($script, ",$", "];");
        echo "<script>$script";
        echo " checkOverlaps();</script>";
    }

    function desenha(DesenhaComponenteBase $comp) {
        $comp->mostra();
    }

    function registro(BaseModel $model) {
        //$tipo = $model->getValorCampo("id_tipo_diagrama");
        writeDebug("id:" . $model->getId());
        $componente = DesenhaComponenteBase::CriaComponenteVisual($model);
        array_push($this->arrComponents, $componente);
    }

    function addScriptDesenho($script) {
        $this->scriptDesenho.=$script;
    }

    function setController($controller) {
        $this->controller = $controller;
    }

    function getController() {
        return $this->controller;
    }

    function getSQLColunas($pkName) {
        return "flag_camada,flag_projeto,flag_funcao,flag_propriedade,flag_entidade,flag_entregavel";
    }

    function getFromSQL($pkName) {
        
    }

    function getWhereSQL($pkName) {
        
    }

    //resto
}
?>
