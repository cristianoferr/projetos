<?

class DataView extends ViewBase {

    private $id_entidade;
    private $colunas;
    private $matriz;
    private $metaInputs;
    private $inputCreator;
    private $elTable;

    function DataView($id_entidade) {
        parent::__construct();
        $this->id_entidade = $id_entidade;
        $this->inputCreator = new MetaInputs();
    }

    function getColunas() {
        return $this->colunas;
    }

    function getInputCreator() {
        return $this->inputCreator;
    }

    function determinaColunas() {
        $this->colunas = $this->getController()->determinaColunas($this->id_entidade);
    }

    function cabecalho() {
        ViewBase::cabecalho();
        $this->determinaColunas();
        $this->metaInputs = new MetaInputs($this);

        $count = 0;
        $this->elTable = elMaker("table")->setClass("data fundo caixa");
        $this->escreveTitulo($this->elTable);

        $elTr = elMaker("tr", $this->elTable);
        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setClass("nobg");
        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setAtributo("style", "width:5%;");

        if ($this->getEditLink()) {
            $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setAtributo("style", "width:5%;");
        }


        while ($count < sizeof($this->colunas)) {
            $coluna = $this->colunas[$count];
            $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setAtributo("onclick", "mopen('m$count')");
            $elTh->setValue($coluna->getName());
            $count++;
        }
        if ($this->getDeleteLink()) {
            $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setAtributo("style", "width:5%;");
        }
    }

    function rodape() {
        ViewBase::rodape();
        $elTr = elMaker("tr", $this->elTable)->setAtributo("style", "background:#fafafa");
        $elTd = elMaker("td", $elTr)->setAtributo("colspan", "100");
        $elDiv = elMaker("div", $elTd)->setAtributo("style", "float:right");

        $this->drawLinks($elDiv);
        $this->elTable->mostra();
    }

    function escreveRegistros() {
        $this->matriz = $this->getController()->criaMatriz();

        $this->desenhaMatriz();
    }

    function desenhaMatriz() {
        $matriz = $this->matriz;

        for ($row = 0; $row < sizeof($matriz); $row++) {
            $elTr = elMaker("tr", $this->elTable);
            $elTh = elMaker("th", $elTr)->setAtributo("scope", "col");
            $elTh->setValue(($row + 1));

            $registro = $matriz[$row];
            if ($this->getEditWidget()) {
                $elTd = elMaker("td", $elTr);
                $elTd->addElement($this->showEditWidget($this->getEditWidget(), $registro->getId()));
            }
            if ($this->getEditLink()) {
                $elTd = elMaker("td", $elTr);
                $elTd->addElement($this->generateEditLink($this->getEditLink(), $registro->getId(), 'dv'));
            }
            $registro->printValuesDinamicos($row, $this->metaInputs, $elTr);
            if ($this->getDeleteLink()) {
                $elTd = elMaker("td", $elTr);
                $elTd->addElement($this->generateDeleteLink($this->getDeleteLink(), $registro->getId(), 'dv'));
            }
        }
    }

    function debug_desenhaMatriz() {
        $matriz = $this->matriz;

        //echo "sizeof(matriz): ".sizeof($matriz);		
        for ($row = 0; $row < sizeof($matriz); $row++) {
            echo "<tr><th scope=\"col\">" . ($row + 1) . "</th>";

            $registro = $matriz[$row];
            $registro->printValues("<td>", "</td>");
            ?><td><? $this->drawAcoesID($modelID); ?></td><?
            echo "</tr>";
        }
    }

}
?>