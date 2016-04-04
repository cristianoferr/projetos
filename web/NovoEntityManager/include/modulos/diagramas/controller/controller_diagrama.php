<?

class DiagramController extends BaseController {

    function insereRegistro($array) {
        $array['projeto'] = validaNumero($array['projeto'], "id_projeto insereRegistro DiagramController");
        validaEscrita("projeto", $array['id_projeto']);
        $array['nm_diagrama'] = validaTexto($array['nm_diagrama'], "nm_diagrama insereRegistro DiagramController");
        $array['id_tipo_diagrama'] = validaNumero($array['id_tipo_diagrama'], "id_tipo_diagrama insereRegistro DiagramController");

        if ($array['id_tipo_diagrama'] == "null")
            $array['id_tipo_diagrama'] = 1;

        $sql = "insert into diagrama (nm_diagrama,id_tipo_diagrama,id_projeto) values ";
        $sql.="(:nm_diagrama,:id_tipo_diagrama,:projeto)";
        $id_diagrama = executaSQL($sql, $array);

        return $id_diagrama;
    }

    //retorna a quantidade de diagramas do projeto
    function countForProjeto($id_projeto) {
        $id_projeto = validaNumero($id_projeto, "id_projeto countForProjeto EntidadeController");
        $id = "count_diagrama_" . projetoAtual();
        if ($this->getCacheInfo($id))
            return $this->getCacheInfo($id);


        $rs = executaQuery("select count(*) as tot from diagrama where id_projeto=?", array(projetoAtual()));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['tot']);
            return $this->getCacheInfo($id);
        }
    }

    function excluirRegistro($id) {
        $id = validaNumero($id);
        if ($id == "null") {
            erroFatal("null passed as ID");
        }
        validaEscrita("diagrama", $id);
        executaSQL("delete from componente_diagrama where id_diagrama=?", array($id));
        executaSQL("delete from diagrama where id_diagrama=?", array($id));
    }

    function filtrosExtras() {
        return " and diagrama.id_projeto=" . projetoAtual();
    }

    function getProjeto($id) {
        $id = validaNumero($id, "id getProjeto DiagramController");
        $row = executaQuerySingleRow("select id_projeto from diagrama c where c.id_diagrama=$id");
        return $row['id_projeto'];
    }

    function desenhaDiagrama($id_diagrama, $painelSeletor) {
        $flagTodosComponentes = $this->isShowingAllComponents($id_diagrama);

        if (!$flagTodosComponentes) {
            $painelSeletor->mostraBotao();
        }
        $this->desenhaDiagramaDefault($id_diagrama, $flagTodosComponentes);
    }

    function isShowingAllComponents($id_diagrama) {
        $tipo = $this->getFieldFromModel($id_diagrama, "id_tipo_diagrama");
        $controller = getControllerForTable("tipo_diagrama");
        return $controller->isShowingAllComponents($tipo);
    }

    function desenhaDiagramaDefault($id_diagrama, $flagTodosComponentes) {
        $tipo = $this->getFieldFromModel($id_diagrama, "id_tipo_diagrama");
        if ($tipo == TIPO_DIAGRAMA_WBS) {
            $diagrama = new DesenhaDiagramaWbs($id_diagrama);
        } else {
            $diagrama = new DesenhaDiagrama($id_diagrama);
        }
        $componentDiagramController = getControllerForTable("componente_diagrama");

        $componentDiagramController->verificaComponentes($id_diagrama, $tipo, $this->isShowingAllComponents($id_diagrama));
        if (!$flagTodosComponentes) {
            $filtroAtivos = "and flag_inativo<>'T'";
        }
        $componentDiagramController->loadRegistros("$filtroAtivos and componente_diagrama.id_diagrama=$id_diagrama");
        $diagrama->setController($componentDiagramController);
        $diagrama->mostra();
    }

}

?>