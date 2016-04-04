<?

/*
 * Essa classe representa um componente diagrama, deve ser extendida para cada variação do mesmo
 */

class DesenhaComponenteBase {

    private $posX;
    private $posY;
    private $controller, $modelComponente, $modelId, $modelSource;
    private $prevRelacaoDatatype;
    private $dragElement;
    private $editUrl;
    protected $elRoot;

    function CriaComponenteVisual(BaseModel $modelComponente) {
        if ($modelComponente->getValorCampo("id_tipo_diagrama") == TIPO_DIAGRAMA_ER) {
            $view = new DesenhaComponenteEntidade();
            $view->setModelId($modelComponente->getValorCampo("id_entidade"));
            $view->setEditUrl("entity/" . projetoAtual() . "/");
            $view->mostraPropriedades();
        }
        if ($modelComponente->getValorCampo("id_tipo_diagrama") == TIPO_DIAGRAMA_WBS) {
            $view = new DesenhaComponenteEntregavel();
            $view->setEditUrl("deliverable/" . projetoAtual() . "/");
            $view->setModelId($modelComponente->getValorCampo("id_entregavel"));
        }

        if ($view) {
            $view->setModelComponente($modelComponente);

            $view->setPosX($modelComponente->getValorCampo("posx"));
            $view->setPosY($modelComponente->getValorCampo("posy"));
            return $view;
        }

        erroFatal("CriaComponenteVisual com tipo desconhecido [" . $modelComponente->getValorCampo("id_tipo_diagrama") . "]");
    }

    function getWidthPerItemAtLevel($nivel) {
        return 1;
    }

    function getHeightPerItemAtLevel($nivel) {
        return 0;
    }

    public function __toString() {
        return $this->modelId . " Class:" . get_class($this) . " Comp:" . $this->modelComponente->getId();
    }

    public function setModelSource($m) {
        $this->modelSource = $m;
    }

    public function getModelSource() {
        return $this->modelSource;
    }

    function setEditUrl($v) {
        $this->editUrl = $v;
    }

    function escreveComponenteInicio($titulo) {
        $id = $this->getModelId();
        $this->elRoot = criaEl("table")->setAtributo("dragel", $this->dragElement)->setAtributo("id", $this->dragElement);
        $this->dragElement($this->elRoot);

        $this->posicao();
        $this->idComponente();
        $this->elRoot->setClass("drag");
        $tr = criaEl("tr", $this->elRoot);
        $this->dragElement($tr);
        $th = criaEl("th", $tr)->setScope("col")->setClass("titulo-diagrama")->setId("cell" . $id);
        $this->dragElement($th);
        $th->setValue($titulo);


        Out::iconeEditar($this->editUrl . $id, $th);
        $this->elRoot->setScript("$('#" . $this->getDragElement() . "').draggable();");
    }

    function dragElement(ElementMaker $tr) {
        $tr->setAtributo("dragel", $this->dragElement);
    }

    function rodape() {
        $this->elRoot->mostra();
    }

    function getDragElement() {
        return $this->dragElement;
    }

    function setDragElement($v) {
        $this->dragElement = $v;
    }

    function setPosX($p) {
        $this->posX = $p;
    }

    function setPosY($p) {
        $this->posY = $p;
    }

    function posicao($extra = null) {

        if ($this->posX) {
            $this->elRoot->setStyle("$extra left:" . $this->posX . "px;top:" . $this->posY . "px;");
        }
    }

    function idComponente() {
        $this->elRoot->setAtributo("idcomp", $this->modelComponente->getValorCampo("id_componente_diagrama"));
    }

    function setModelComponente($modelComponente) {
        $this->modelComponente = $modelComponente;
    }

    function setModelId($modelId) {
        $this->modelId = $modelId;
    }

    function getModelId() {
        return $this->modelId;
    }

    function cabecalho($titulo = null) {
        
    }

    function mostra() {
        $this->cabecalho();
        $this->itera();

        $this->rodape();
    }

    function itera() {
        while ($model = $this->controller->next()) {
            $this->registro($model);
        }
    }

    function registro(BaseModel $model) {
        echo get_class($this) . "->registro()";
    }

    function setController($controller) {
        $this->controller = $controller;
    }

    function getController() {
        return $this->controller;
    }

}

?>
