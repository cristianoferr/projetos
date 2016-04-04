<?

class TableManagerDefault extends TableManager {

    function carregaTabelaComNome($tableName) {


        if ($tableName == "lingua") {
            return $this->carregaLingua();
        }
    }

    function carregaLingua() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("lingua", "id_lang", "nmlongo_lang");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Lingua", "id_lang", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Lingua", "nmlongo_lang", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, false, null);

        return $tabela;
    }

}

?>