<?

class MetaRegistro {

    private $entidade, $id, $sequence;
    private $valores;

    function MetaRegistro() {
        $this->valores = array();
    }

    function adicionaValor($valor) {
        array_push($this->valores, $valor);
    }

    function printValuesDinamicos($rowCount, $metaInputs, ElementMaker $elRoot) {
        //writeDebug("tamanho:".sizeof($this->valores));
        //echo "tamanho:".sizeof($this->valores);
        for ($c = 0; $c < sizeof($this->valores); $c++) {
            $valor = $this->valores[$c];
            $metaInputs->setValor($valor);
            $metaInputs->escreveCelula($rowCount, $c, $elRoot);
        }
    }

    function getValorFromColunaWithName($nm_coluna) {
        for ($c = 0; $c < sizeof($this->valores); $c++) {
            $valor = $this->valores[$c];
            $coluna = $valor->getColuna();
            if ($coluna->getName() == $nm_coluna) {
                return $valor;
            }
        }
    }

    function printValues($tagInicial, $tagFinal) {
        for ($c = 0; $c < sizeof($this->valores); $c++) {
            $valor = $this->valores[$c];
            echo $tagInicial . $valor->getValorFormatado() . $tagFinal;
        }
    }

    function getValores() {
        return $this->valores;
    }

    //getters e setters


    function setId($v) {
        $this->id = $v;
    }

    function getId() {
        return $this->id;
    }

    function setSequence($v) {
        $this->sequence = $v;
    }

    function getSequence() {
        return $this->sequence;
    }

    function setEntidade($v) {
        $this->entidade = $v;
    }

    function getEntidade() {
        return $this->id_entidade;
    }

}

?>