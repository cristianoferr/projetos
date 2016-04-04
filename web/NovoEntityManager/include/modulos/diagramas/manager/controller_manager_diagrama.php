<?

class ControllerManager_Diagrama implements IControllerManager {

    function getControllerForTable($tableName) {
        //diagrama
        if ($tableName == "diagrama") {
            return $this->getDiagramController();
        }
        if ($tableName == "tipo_diagrama") {
            return $this->getTipoDiagramaController();
        }
        if ($tableName == "componente_diagrama") {
            return $this->getComponentDiagramController();
        }
    }

    function getComponentDiagramController() {
        $table = getTableManager()->getTabelaComNome("componente_diagrama");
        $controller = new ComponentDiagramController($table);
        return $controller;
    }

    function getTipoDiagramaController() {
        $table = getTableManager()->getTabelaComNome("tipo_diagrama");
        $controller = new TipoDiagramaController($table, "txt_diagram_types", "txt_diagram_type");
        return $controller;
    }

    function getDiagramController() {
        $table = getTableManager()->getTabelaComNome("diagrama");
        $controller = new DiagramController($table, "txt_diagrams", "txt_diagram", new LinkKey("txt_new_diagram", "diagram/new/<projeto>"));
        return $controller;
    }

}

?>