<?

class DesenhaComponenteEntidade extends DesenhaComponenteBase {

    private $flagPropriedades;
    private $flagFuncoes;
    private $modo; //0=propriedade / 1 = funcao

    function __construct() {
        $this->setController(getControllerForTable("entidade"));
    }

    function mostraPropriedades() {
        $this->flagPropriedades = true;
    }

    function mostraFuncoes() {
        $this->flagFuncoes = true;
    }

    function cabecalho() {
        $id = $this->getModelId();
        $this->setDragElement("entity" . $id);
        $titulo = $this->getController()->getFieldFromModel($id, ENTIDADE_DB_NAME);
        $this->escreveComponenteInicio($titulo);
    }

    function mostra() {
        $this->cabecalho();
        if ($this->flagPropriedades) {
            $this->modo = 1;
            $controller = getControllerForTable("coluna");
            $controller->loadRegistros("and coluna.id_entidade_pai=" . $this->getModelId());
            $this->setController($controller);
            $this->itera();
        }

        if ($this->flagFuncoes) {
            $this->modo = 2;
            $controller = getControllerForTable("funcao");
            $controller->loadRegistros("and funcao.id_entidade_pai=" . $this->getModelId());
            $this->setController($controller);

            $this->itera();
        }

        $this->rodape();
    }

    function registro(BaseModel $model) {
        if ($this->modo == 1) {
            $this->registroPropriedade($model);
        }
        if ($this->modo == 2) {
            //$this->registroFuncao($model);
        }
    }

    function registroPropriedade(BaseModel $model) {
        $id_relacao_datatype = $model->getValorCampo('id_relacao_datatype');
        $classKey = "";
        $mudouTipo = false;

        $icone = ICONE_OPCIONAL;

        if ($id_relacao_datatype == RELACAO_DATATYPE_PK) {
            $classKey = "primaryKey";
            $this->prevRelacaoDatatype = 1;
            $icone = ICONE_PK;
        }

        $line_color = "black";
        if ($id_relacao_datatype > 2) {
            $line_color = 'gray';
        }

        if ($id_relacao_datatype == RELACAO_DATATYPE_OBRIGATORIO) {
            $classKey = "requiredKey";
            $icone = ICONE_OBRIGATORIO;
        }

        $id_entidade_combo = $model->getValorCampo('id_entidade_combo');

        if ($this->prevRelacaoDatatype != $id_relacao_datatype) {
            $mudouTipo = true;
        }
        $this->prevRelacaoDatatype = $id_relacao_datatype;

        $tr = criaEl("tr", $this->elRoot);
        $this->dragElement($tr);

        $td = criaEl("td", $tr)->setId("coluna" . $model->getId())->setClass("cellUML " . $classKey);
        $this->dragElement($td);

        if ($id_entidade_combo) {
            $td->addClass("foreignKey");
        }
        $td->addClass($model->getValorCampo('css_primitive_type'));

        if ($mudouTipo) {
            $td->setStyle("border-top: 2px solid black;");
        }
        $this->escreveIcone($icone, $td);
        $strong = criaEl("strong", $td);
        $this->dragElement($strong);
        $strong->addElement(Out::linkG("property/" . projetoAtual() . "/" . $this->getModelId() . "/" . $model->getId(), $model->getValorCampo('dbname_coluna')));
        $strong->setValue("<pk/>");

        if ($id_entidade_combo) {
            $this->elRoot->setScript("add_line('coluna" . $model->getId() . "','entity" . $id_entidade_combo . "','$line_color',0,'one2many');");
        }
    }

    function escreveIcone($icone, ElementMaker $td) {
        if (!$icone) {
            return;
        }
        $img = criaEl("img", $td)->setClass("property-icon")->setSrc(getHomeDir() . "images/$icone");
        $this->dragElement($img);
    }

}

?>
