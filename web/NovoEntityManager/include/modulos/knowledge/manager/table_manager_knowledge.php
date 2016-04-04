<?

class TableManagerKnowledge extends TableManager {

    function carregaTabelaComNome($tableName) {

        if ($tableName == "artigo") {
            return $this->carregaArtigo();
        }
    }

    function carregaArtigo() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("artigo", "id_artigo", "title_artigo");
        $coluna = $this->adicionaColunaNormal($tabela, "ID artigo", "id_artigo", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, "id_elemento", "id_elemento", true, false, "elemento", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_language"), "id_lingua", true, false, "lingua", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, getManager()->getDefaultLang());
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_title"), "title_artigo", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_conteudo"), "texto_artigo", false, TIPO_INPUT_HTML);
        $coluna->setDetalhesInclusao(true, true, null);


        $tabela->setPaginaInclusao(getHomeDir() . "article");

        return $tabela;
    }

}

?>