<?

class MetaValor {

    private $coluna, $valor, $id;

    function MetaValor($coluna) {
        $this->coluna = $coluna;
    }

    function setValor($v) {
        $this->valor = $v;
    }

    function setId($v) {
        $this->id = $v;
    }

    function getValor() {
        return $this->valor;
    }

    function getColuna() {
        return $this->coluna;
    }

    function getId() {
        return $this->id;
    }

    function getComboDict() {
        return $this->coluna->getComboDictionary();
    }

    function escreveSelectOptions() {

        $dict = $this->coluna->getComboDictionary()->getArray();
        echo "MetaValor valor:" . $this->valor . " tamanho:" . sizeof($dict);

        foreach ($dict as $chave => $valor) {
            //echo "\$a[$k] => $v.\n";
            $sel = "";
            if ($chave == $this->valor) {
                $sel = "selected";
            }
            echo "<option value='$chave' $sel >$valor</option>";
        }
    }

    function getValorFormatado() {
        $v = $this->getValor();

        return $this->coluna->formataValor($v);
    }

}

?>