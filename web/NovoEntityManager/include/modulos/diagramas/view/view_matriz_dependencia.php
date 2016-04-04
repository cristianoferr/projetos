<?

class MatrizDependenciaView {

    private $controller, $matriz, $titulo;
    private $elRoot, $elTable;

    function DesenhaDiagramas() {
        
    }

    function setTitulo($titulo) {
        $this->titulo = $titulo;
    }

    //interface
    function cabecalho() {
        Out::titulo($this->titulo);
        $this->elRoot = elMaker("div")->setClass("table-responsive");
        $this->elTable = elMaker("table", $this->elRoot)->setClass("panelsize caixa panelsizetable table-bordered table-striped");
    }

    function rodape() {
        $this->elRoot->mostra();
    }

    function mostra() {
        $matriz = $this->controller->getMatriz();
        $this->cabecalho();

        $colsSize = sizeof($matriz);
        //echo "Matriz Dependecia  size:$colsSize<br>";

        for ($row = 0; $row < $colsSize; $row++) {
            $vetorEntidade = $matriz[$row];
            if ($row == 0) {
                $elTr = elMaker("tr", $this->elTable);
                $elTd = elMaker("td", $elTr)->setValue("");

                for ($col = 0; $col < $colsSize; $col++) {
                    $elTh = elMaker("th", $elTr)->setAtributo("scope", "row");
                    $elTh->setValue($vetorEntidade[$col][1]);
                }
            }

            $elTr = elMaker("tr", $this->elTable);
            $elTh = elMaker("th", $elTr);
            $elTh->setValue($vetorEntidade[$row][1]);
            for ($col = 0; $col < $colsSize; $col++) {
                $class = "";

                if ($vetorEntidade[$col][2] != "") {
                    $class.=" alert-info";
                }
                $elTd = elMaker("td", $elTr)->setClass($class)->setValue($vetorEntidade[$col][2]);
            }
        }

        $this->rodape();
    }

    function setController($controller) {
        //echo get_class($this)."->setController()";
        $this->controller = $controller;
    }

    function getController() {
        return $this->controller;
    }

    function getSQLColunas($pkName) {
        return "*";
    }

}
?>