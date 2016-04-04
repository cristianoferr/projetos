<?

/*
  Classe responsável por mostrar na tela os componentes que o usuário pode selecionar para incluir no diagrama
 */

class SeletorComponentesDiagrama {

    private $componenteRaiz, $nivel;
    private $tipoDiagrama;
    private $idDiagrama;
    private $modelTipoDiagrama;
    private $controller;
    private $diagramController;
    private $tipodiagramaController;

    function __construct($idDiagrama) {
        $idDiagrama = validaNumero($idDiagrama, "idDiagrama SeletorComponentesDiagrama");
        $this->idDiagrama = $idDiagrama;
        $this->controller = getControllerManager()->getcontrollerForTable("componente_diagrama");
    }

    function trataAcao($acao) {
        if (!checkPerm("diagrama", $this->idDiagrama, true)) {
            return;
        }
        if ($acao == ACAO_ATUALIZA_ELEMENTOS) {
            $arr = array();
            foreach ($_POST['select_use'] as $selectedOption) {
                $comp = $this->extraiId($selectedOption, null, TIPO_COMPONENTE_PROJETO);
                $comp = $this->extraiId($selectedOption, $comp, TIPO_COMPONENTE_ENTIDADE);
                $comp = $this->extraiId($selectedOption, $comp, TIPO_COMPONENTE_CAMADA);
                $comp = $this->extraiId($selectedOption, $comp, TIPO_COMPONENTE_PROPRIEDADE);
                $comp = $this->extraiId($selectedOption, $comp, TIPO_COMPONENTE_FUNCAO);
                $comp = $this->extraiId($selectedOption, $comp, TIPO_COMPONENTE_ENTREGAVEL);
                $comp = $this->extraiId($selectedOption, $comp, TIPO_COMPONENTE_TITULO);

                if ($comp) {
                    array_push($arr, $comp);
                } else {
                    writeAdmin("option não encontrada", $selectedOption);
                }
            }
            $id_diagrama = diagramaAtual();
            $this->controller->atualizaComponentes($id_diagrama, $arr);
            redirect(getHomeDir() . "diagram/" . projetoAtual() . "/" . $this->idDiagrama);
        }
    }

    function extraiId($option, $comp, $tipo) {
        if ($comp) {
            return $comp;
        }
        if (constainsSubstring($option, $tipo)) {
            $id = replaceString($option, "_" . $tipo, "");
            //  write("id:$id tipo: $tipo option:$option");

            $comp = new ComponentModel($id, "", is_numeric($id), $tipo, null);
            return $comp;
        }
    }

    function mostraBotao() {
        if (!checkPerm("diagrama", $this->idDiagrama, true)) {
            return;
        }
        $painel = new PainelModal("seletor", "", "Loading", translateKey("txt_save_changes"), "glyphicon glyphicon-wrench");
        $painel->setUrlConteudo("diagram/field_seletor/" . projetoAtual() . "/" . $this->idDiagrama);
        $painel->dadosForm("diagram/" . projetoAtual() . "/" . $this->idDiagrama, ACAO_ATUALIZA_ELEMENTOS);
        $painel->mostra();
    }

    function inicializaControllers() {
        $this->diagramController = getControllerManager()->getcontrollerForTable("diagrama");
        $this->tipodiagramaController = getControllerManager()->getcontrollerForTable("tipo_diagrama");

        $this->tipoDiagrama = $this->diagramController->getFieldFromModel($this->idDiagrama, "id_tipo_diagrama");
        $this->modelTipoDiagrama = $this->tipodiagramaController->loadSingle($this->tipoDiagrama);

        $this->componenteRaiz = $this->controller->loadComponentes($idDiagrama, $this->modelTipoDiagrama);
        $this->controller->loadRegistros("and flag_inativo<>'T' and componente_diagrama.id_diagrama=" . $this->idDiagrama);
    }

    function verificaESeleciona() {
        $controller = $this->controller;
        while ($model = $controller->next()) {
            //writeAdmin("id:".$model->getValorCampo("id_componente_diagrama"),"id_projeto:".$model->getValorCampo("id_projeto")."  id_entidade:".$model->getValorCampo("id_entidade"));
            $this->componenteRaiz->verificaESeleciona($model);
        }

        $this->nivel = 0;
    }

    function criaBotoesDoMeio(ElementMaker $elRoot) {
        //celula do meio
        $elTd = elMaker("td", $elRoot);
        $elCenter = new ElementMaker("center", $elTd);

        $this->criaBotao($elCenter, "btnAdd", "adicionaElementos(\"select_avail\", \"select_use\")", "glyphicon-arrow-right", "txt_add");
        $this->criaBotao($elCenter, "btnAddAll", "adicionaTodosElementos(\"select_avail\", \"select_use\")", "glyphicon-forward", "txt_add_all");

        $elP = new ElementMaker("p", $elCenter);
        $elB = new ElementMaker("b", $elP);
        $elB->setValue(translateKey("txt_or"));

        $this->criaBotao($elCenter, "btnRemoveAll", "adicionaTodosElementos(\"select_use\", \"select_avail\")", "glyphicon-backward", "txt_remove_all");
        $this->criaBotao($elCenter, "btnRemove", "adicionaElementos(\"select_use\", \"select_avail\")", "glyphicon-arrow-left", "txt_remove");
    }

    function mostra() {
        $this->inicializaControllers();
        $this->verificaESeleciona();

        /* ?><div class="btn-group" data-toggle="buttons"><? */
        $elDivRoot = elMaker("div")->setClass("table-responsive");
        $elTable = elMaker("table", $elDivRoot)->setClass("panelsize panelsizetable");
        $elTr = elMaker("tr", $elTable);
        //celula da esquerda
        $elTd = elMaker("td", $elTr);
        $elStrong = elMaker("strong", $elTd)->setValue(translateKey("txt_seletor_available_fields"));
        $elSelect = elMaker("select", $elTd)->setId("select_avail")->ativaFlag("multiple")->setAtributo("size", 20)->setAtributo("onfocus", "habilitaBotoes(true);");
        $this->componenteRaiz->mostraLivres($this, $elSelect);

        $this->criaBotoesDoMeio($elTr);

        //celula da direita
        $elTd = elMaker("td", $elTr);
        $elStrong = elMaker("strong", $elTd)->setValue(translateKey("txt_seletor_in_use_fields"));
        $elSelect = elMaker("select", $elTd)->setName("select_use[]")->setId("select_use")->ativaFlag("multiple")->setAtributo("size", 20)->setAtributo("onfocus", "habilitaBotoes(false);");
        $this->componenteRaiz->mostraEmUso($this, $elSelect);

        $script = "function habilitaBotoes(isLeft) {";
        $script.= "    if (isLeft) {";
        $script.= "        $('#btnAdd').removeClass(\"disabled\");";
        $script.= " $('#btnAddAll').removeClass(\"disabled\");";
        $script.= "        $('#btnRemove').addClass(\"disabled\");";
        $script.= "        $('#btnRemoveAll').addClass(\"disabled\");";
        $script.= "    } else {";
        $script.= "        $('#btnRemove').removeClass(\"disabled\");";
        $script.= "        $('#btnRemoveAll').removeClass(\"disabled\");";
        $script.= "        $('#btnAdd').addClass(\"disabled\");";
        $script.= "        $('#btnAddAll').addClass(\"disabled\");";
        $script.= "    }";
        $script.= "}";
        $script.= "$('#select_avail').focus();";
        $elDivRoot->setScript($script);

        $elDivRoot->mostra();
    }

    function comecoComponente($comp, ElementMaker $elroot) {
        $this->nivel++;
        $elOption = elMaker("option", $elroot)->setAtributo("value", $comp->getIdUnico())->setAtributo(selectable, $comp->isSelecionavel());
        $elOption->setAtributo("tipo", $comp->getTipo())->setId($comp->getId());
        $elOption->setValue(str_repeat("&nbsp;&nbsp;", $this->nivel) . $comp->getNome());
    }

    function criaBotao($elCenter, $id, $onclick, $glyph, $textKey) {
        $elButton = new ElementMaker("button", $elCenter);
        $elButton->setAtributo("type", "button")->setAtributo("class", "btn btn-primary")->
                setAtributo("id", $id)->setAtributo("onclick", $onclick);
        $elButton->setValue(translateKey($textKey));
        $elSpan = new ElementMaker("span", $elButton);
        $elSpan->setAtributo("class", "glyphicon $glyph");
    }

    function fimComponente($comp) {
        $this->nivel--;
    }

}
?>