<?

//essa classe desenha a entidade uml, o controlle base é coluna
class ERDiagramView extends BasePainel {

    private $entidadeModel;
    private $prevRelacaoDatatype;
    private $flagSolido;
    private $posX;
    private $posY;
    private $linhasDiagrama;
    private $elTable;

    function setEntidadeModel($entidadeModel) {
        $this->entidadeModel = $entidadeModel;
    }

    function setSolido($s) {
        $this->flagSolido = $s;
    }

    function setPosX($p) {
        $this->posX = $p;
    }

    function setPosY($p) {
        $this->posY = $p;
    }

    function cabecalho() {
        $root = parent::cabecalho();
        $this->linhas = "";
        if ($this->posX) {
            $style = "left:" . $this->posX . "px;top:" . $this->posY . "px;";
        }
        $elTable = criaEl("table", $root)->setStyle($style);

        $elTable->setId("entity" . $this->entidadeModel->getId());
        $this->escreveTitulo($elTable);

        $elTr = criaEl("tr", $elTable);
        $elTh = criaEl("th")->setScope("col")->setClass("titulo-diagrama")->setId("cell" . $this->entidadeModel->getId());
        $elTh->setValue($this->entidadeModel->getValorCampo('dbname_entidade'));

        if (!$this->flagSolido) {
            $elTable->setClass("drag");
            Out::iconeEditar(getHomeDir() . "entity/" . projetoAtual() . "/" . $this->entidadeModel->getId(), $elTh);
        }
        $this->elTable = $elTable;
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        //$coluna=$colunas[0];
        $id_relacao_datatype = $model->getValorCampo('id_relacao_datatype');
        $classKey = "";
        $mudouTipo = false;

        //echo $model->getId()."||".$model->get

        if ($id_relacao_datatype == 1) {
            $classKey = "primaryKey";
            $this->prevRelacaoDatatype = 1;
        }

        $line_color = "black";
        if ($id_relacao_datatype > 2) {
            $line_color = 'gray';
        }

        if ($id_relacao_datatype == 2) {
            $classKey = "requiredKey";
        }

        $id_entidade_combo = $model->getValorCampo('id_entidade_combo');

        if ($this->prevRelacaoDatatype != $id_relacao_datatype) {
            $mudouTipo = true;
        }
        $this->prevRelacaoDatatype = $id_relacao_datatype;

        $elTr = criaEl("tr", $this->elTable);
        $elTd = criaEl("td", $elTr)->setId("coluna" . $model->getId())->setClass("cellUML $classKey");


        if ($id_entidade_combo) {
            $elTd->addClass("foreignKey");
        }
        $elTd->addClass($model->getValorCampo('css_primitive_type'));


        if ($mudouTipo) {
            $elTd->setStyle("border-top: 2px solid black;");
        }
        $elStrong = criaEl("strong", $elTd)->setValue($model->getValorCampo('dbname_coluna'));
        $elPk = criaEl("pk", $elStrong)->closedTag();

        if ($id_entidade_combo) {
            $this->elTable->setScript("add_line('coluna" . $model->getId() . "','entity" . $id_entidade_combo . "','$line_color',0,'one2many');");
        }
    }

}
?>