<?

define("COMP_ENTR_WIDTH", 120);
define("COMP_ENTR_HEIGHT", 80);

class DesenhaComponenteEntregavel extends DesenhaComponenteBase {

    function __construct() {
        $this->setController(getControllerForTable("entregavel"));
    }

    function cabecalho($titulo = null, $id = null) {
        parent::cabecalho();
        if (!$id)
            $id = $this->getModelId();
        $this->setDragElement("entity" . $id);
        $this->escreveComponenteInicio($titulo);
    }

    function rodape() {
        $this->escreveComponenteFim();
    }

    function setAbsoluteLeft($left) {
        $this->setPosX(MIN_WIDTH + $left * COMP_ENTR_WIDTH);
    }

    function setAbsoluteTop($top) {
        $this->setPosY(MIN_HEIGHT + $top * COMP_ENTR_HEIGHT);
    }

    function getWidthPerItemAtLevel($nivel) {
        return $nivel % 2;
    }

    function getHeightPerItemAtLevel($nivel) {
        return !($nivel % 2);
    }

    function mostra() {

        $controller = $this->getController();
        $model = $this->getModelSource();
        $id = $model->getId();
        writeDebug("modelId:" . $id);
        //$model = $controller->loadSingle($this->getModelId());
        $this->cabecalho($model->getDescricao());
        $this->registro($model);
        $this->rodape();

        $idPai = $model->getValorCampo("id_entregavel_pai");
        //write("idPai:$idPai $model");
        $this->addScriptDesenho("add_line('entity" . $model->getId() . "','entity" . $idPai . "','black',0,'wbs');");
    }

    function registro(BaseModel $model) {
        // writeAdmin("model:" . $model);
    }

    function escreveComponenteInicio($titulo) {
        $id = $this->getModelId();

        echo "<div " . $this->dragElement();
        // $this->posicao("width:" . COMP_ENTR_WIDTH . "px;");
        $this->posicao();
        $this->idComponente();

        $contrStatus = getControllerForTable("status_entregavel");
        $codStatus = $this->getModelSource()->getValorCampo('id_status_entregavel');
        $txtStatus = translateKey($contrStatus->getStatusEntregavel($codStatus));
        $id_tipo = $this->getModelSource()->getValorCampo("id_status_entregavel");
        echo "data-toggle=\"tooltip\" data-placement=\"left\" title=\"$txtStatus\" ";

        echo " class= \"hint--left  caixa drag status_del_$id_tipo\" id=\"" . $this->getDragElement() . "\">";
        echo "<strong class='wbs_title'>";
        echo $titulo;
        echo "</strong>";
        $this->addScriptDesenho("$('#" . $this->getDragElement() . "').draggable();");
        $this->addScriptDesenho("$('#" . $this->getDragElement() . "').tooltip();");
    }

    function escreveComponenteFim() {
        echo "</div>";
    }

}

?>
