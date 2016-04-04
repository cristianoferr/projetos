<?

class PainelScreen implements ISimplePainel {

    protected $elRoot;
    private $controller;
    private $modelTree;
    protected $elBody;
    protected $flagEdit;

    function __construct() {
        $this->editMode();
    }

    function editMode($v = true) {
        $this->flagEdit = $v;
    }

    function showMode() {
        $this->flagEdit = false;
    }

    function setTileTree(IModel $modelTree) {//coberto
        $this->modelTree = $modelTree;
    }

    function hasChild() {//coberto
        if (!$this->modelTree) {
            erroFatal("modeltree vazio");
            return false;
        }
        return (sizeof($this->modelTree->getChildren()));
    }

    function cabecalho() {//coberto
        $this->elRoot = criaEl("div");

        $this->elRoot->setClass("tile_col");
        if ($this->hasChild()) {
            $this->elBody = criaEl("div", $this->elRoot);
        } else {
            $this->elBody = $this->elRoot;
        }

        $this->addTileContent($this->elBody);

        if ($this->modelTree->getValorCampo("id_tile_orientation") == TILE_ORIENT_HORIZONTAL) {
            $class = "col-md-";

            $class.=($this->modelTree->getValorCampo("id_tile_size") * 2); //se tamanho=1 então class=col-md-2
            $this->elBody->setClass("tile_col");
            //  $this->elRoot->addClass($class);
        } else {
            $class = "tile_row";
            $this->elBody->setClass($class);
        }
    }

    function addTileContent(ElementMaker $elRoot) {
        if (!$this->hasChild()) {
            $id_widget = $this->modelTree->getValorCampo("id_widget");
            if ($id_widget) {
                $painel = $this->showWidget($id_widget);
            } else {
                $painel = $this->showEmptyTile();
            }
            $elRoot->addElement($painel);

            $elRoot->setValue($this->modelTree->getDescricao() . "&nbsp;");
        }
    }

    function showWidget($id_widget) {
        write("showWidget");
        if ($this->flagEdit) {
            $widget = getWidgetManager()->getWidgetFor(WIDGET_EDIT_TILE);
            $painel = $widget->generate($this->modelTree->getId());
        } else {
            $widget = getWidgetManager()->getWidgetFor(WIDGET_SHOW_TILE);
            $painel = $widget->generate($this->modelTree->getId());
            //$painel = $widget->generate($id_widget);
        }
        return $painel;
    }

    function showEmptyTile() {
        write("showEmptyTile");
        if ($this->flagEdit) {
            $painel = criaEl("");
            $painelEdit = getWidgetManager()->generateButtonForWidget(WIDGET_EDIT_TILE, $this->modelTree->getId(), true, "glyphicon glyphicon-new-window", translateKey("txt_popup_edit"));
            $painelAdd = getWidgetManager()->generateButtonForWidget(WIDGET_ADD_TILE, $this->modelTree->getId(), true, "glyphicon glyphicon-plus", translateKey("txt_create_new"));
            //$linkRemove = getWidgetManager()->generateRedirectLinkForWidget(WIDGET_DELETE_TILE, $this->modelTree->getId());
            $linkRemove = Out::createDeleteLinkForLink("screens/tile/delete/" . projetoAtual() . "/", $this->modelTree->getId(), "del");
            $painel->addElement($painelEdit->generate());
            $painel->addElement($painelAdd->generate());
            $painel->addElement($linkRemove);
            // $widget = getWidgetManager()->getWidgetFor(WIDGET_EDIT_TILE);
        } else {
            $widget = getWidgetManager()->getWidgetFor(WIDGET_SHOW_TILE);
            $painel = $widget->generate($this->modelTree->getId());
        }
        return $painel;
    }

    function generate() {//coberto
        $this->cabecalho();
        $array = $this->ordenaArray($this->modelTree->getChildren());
        foreach ($array as $model) {
            $this->registro($model);
        }
        $this->rodape($this->elRoot);
        return $this->elRoot;
    }

    function mostra() {//coberto
        $this->generate();
        $this->elRoot->mostra();
    }

    function ordenaArray($array) {
        for ($c = 0; $c < sizeof($array) - 1; $c++) {
            $modelC = $array[$c];
            for ($d = $c + 1; $d < sizeof($array); $d++) {
                $modelD = $array[$d];
                if ($modelC->getValorCampo("seq_tile") > $modelD->getValorCampo("seq_tile")) {
                    $array[$d] = $modelC;
                    $array[$c] = $modelD;
                }
            }
        }
        return $array;
    }

    function setController(IController $controller) {//coberto
        $this->controller = $controller;
    }

    function registro(IModel $model) {//coberto
        $painelScreen = getPainelManager()->getPainel(PAINEL_SHOW_SCREEN);

        $painelScreen->setTileTree($model);
        $painelScreen->setController($this->controller);
        $painelScreen->editMode($this->flagEdit);
        $ret = $painelScreen->generate();
        $this->elBody->addElement($ret);
    }

    function rodape(ElementMaker $elRoot) {//coberto
    }

    function getController() {//coberto
        return $this->controller;
    }

}

?>