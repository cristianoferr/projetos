<?

/*
 * Essa classe é usada para selecionar objetos na view_seletor
 */

class ComponentModel {

    private $idRegistro, $nmRegistro, $flagSelecionavel;
    private $parent;
    private $arrChildren, $tipo;
    private $flagSelected;
    private $idComponente;
    private $model; //aponta para a tabela propriamente dita

    function __construct($idRegistro, $nmRegistro, $flagSelecionavel, $tipo, $parent) {
        $this->idRegistro = $idRegistro;
        $this->nmRegistro = $nmRegistro;
        $this->parent = $parent;
        $this->flagSelected = false;
        $this->flagSelecionavel = $flagSelecionavel;
        $this->arrChildren = array();
        $this->tipo = $tipo;
        if ($parent)
            $parent->addChild($this);
    }

    function getId() {
        return $this->idRegistro;
    }

    public function __toString() {
        return $this->idRegistro . " - " . $this->nmRegistro;
    }

    //returns an array with selectable components
    function getArrayUseableObjects($arr = array()) {
        $size = sizeof($this->arrChildren);
        for ($c = 0; $c < $size; $c++) {
            $child = $this->arrChildren[$c];
            if ($child->isSelecionavel() == "T") {
                array_push($arr, $child);
            }
            $arr = $child->getArrayUseableObjects($arr);
        }
        return $arr;
    }

    function getIdUnico() {
        return $this->getId() . "_" . $this->getTipo();
    }

    function size() {
        return sizeof($this->arrChildren);
    }

    function getNome() {
        return $this->nmRegistro;
    }

    function getTipo() {
        return $this->tipo;
    }

    function seleciona() {
        $this->flagSelected = true;
    }

    function isSelected() {
        return $this->flagSelected;
    }

    function isSelecionavel() {
        return $this->flagSelecionavel;
    }

    function addChild(ComponentModel $comp) {
        array_push($this->arrChildren, $comp);
    }

    function verificaTipo($tipo, BaseModel $modelComponente) {
        $pk = ComponentLoader::getPKDoTipo($tipo);
        if (!$this->idRegistro) {
            return false;
        }
        if (($this->tipo == $tipo) && ($modelComponente->getValorCampo($pk) == $this->idRegistro)) {

            $this->seleciona();
            //writeAdmin("selecionando $tipo: ".$this->idRegistro,"pk: $pk = ".$modelComponente->getValorCampo($pk)."==".$this->idRegistro);
            return true;
        }
        return false;
    }

    function verificaESeleciona(BaseModel $modelComponente) {

        if ($this->verificaTipo(TIPO_COMPONENTE_PROJETO, $modelComponente)) {
            return;
        }
        if ($this->verificaTipo(TIPO_COMPONENTE_ENTIDADE, $modelComponente)) {
            return;
        }
        if ($this->verificaTipo(TIPO_COMPONENTE_ENTREGAVEL, $modelComponente)) {
            return;
        }
        if ($this->verificaTipo(TIPO_COMPONENTE_PROPRIEDADE, $modelComponente)) {
            return;
        }
        if ($this->verificaTipo(TIPO_COMPONENTE_FUNCAO, $modelComponente)) {
            return;
        }
        if ($this->verificaTipo(TIPO_COMPONENTE_ENTREGAVEL, $modelComponente)) {
            return;
        }

        for ($c = 0; $c < sizeof($this->arrChildren); $c++) {
            $child = $this->arrChildren[$c];
            $child->verificaESeleciona($modelComponente);
        }
    }

    function mostraLivres(SeletorComponentesDiagrama $painel, ElementMaker $elroot) {
        if ($this->isSelected()) {
            return;
        }
        $painel->comecoComponente($this, $elroot);
        for ($c = 0; $c < sizeof($this->arrChildren); $c++) {
            $child = $this->arrChildren[$c];
            $child->mostraLivres($painel, $elroot);
        }
        $painel->fimComponente($this);
    }

    function mostra(SeletorComponentesDiagrama $painel) {
        if (!$this->hasSelectedChild() &&
                ($this->isSelected())) {
            return;
        }
        $painel->comecoComponente($this);
        for ($c = 0; $c < sizeof($this->arrChildren); $c++) {
            $child = $this->arrChildren[$c];
            $child->mostra($painel);
        }
        //if ($this->flagSelecionavel=="T")
        $painel->fimComponente($this);
    }

    function mostraEmUso(SeletorComponentesDiagrama $painel, ElementMaker $elroot) {
        //writeAdmin("nome: $nmRegistro","hasSelectedChild:".$this->hasSelectedChild());
        if (!$this->hasSelectedChild()) {
            return false;
        }
        $painel->comecoComponente($this, $elroot);
        for ($c = 0; $c < sizeof($this->arrChildren); $c++) {
            $child = $this->arrChildren[$c];
            $mostrou = $child->mostraEmUso($painel, $elroot);
        }
        $painel->fimComponente($this);

        if ($this->isSelected == "T") {
            return true;
        }
        if ($mostrou) {
            return true;
        }
        return false;
    }

    //Se o componente é selecionado ou possui algum filho selecionado então retorna true
    function hasSelectedChild() {
        if ($this->isSelected() == "T") {
            return true;
        }
        if ($this->getId() == TIPO_COMPONENTE_TITULO) {
            return true;
        }
        for ($c = 0; $c < sizeof($this->arrChildren); $c++) {
            $child = $this->arrChildren[$c];
            if ($child->hasSelectedChild()) {
                return true;
            }
        }
        return false;
    }

}

?>