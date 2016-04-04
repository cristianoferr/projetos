<?

class TableManagerConstrubook extends TableManager {

    function carregaTabelaComNome($tableName) {
        if ($tableName == "fornecedor") {
            return $this->carregaFornecedor();
        }
        if ($tableName == "produto") {
            return $this->carregaProduto();
        }
        if ($tableName == "tabela_calculo") {
            return $this->carregaTabelaCalculo();
        }

        if ($tableName == "media") {
            return $this->carregaMedia();
        }
        if ($tableName == "produto_media") {
            return $this->carregaProdutoMedia();
        }
        if ($tableName == "mural") {
            return $this->carregaMural();
        }
        if ($tableName == "tipo_mural") {
            return $this->carregaTipoMural();
        }
         if ($tableName == "categoria_produto") {
            return $this->carregaCategoriaProduto();
        }
    }
    
    
     function carregaCategoriaProduto() {
        $tabela = new TabelaModel("categoria_produto", "id_categoria_produto", "nm_categoria_produto");
        $coluna = $this->adicionaColunaNormal($tabela, "ID categoria_produto", "id_categoria_produto", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_categoria_produto"), "nm_categoria_produto", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
       // $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_categoria_pai"), "id_categoria_produto_pai", true, false, "categoria_produto", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_nm_fornecedor"), "id_fornecedor", true, false, "fornecedor", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }
    
     function carregaTabelaCalculo() {
        $tabela = new TabelaModel("tabela_calculo", "id_tabela_calculo", "nm_tabela_calculo");
        $coluna = $this->adicionaColunaNormal($tabela, "ID tabela_calculo", "id_tabela_calculo", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tabela_calculo"), "nm_tabela_calculo", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_nm_fornecedor"), "id_fornecedor", true, false, "fornecedor", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }
     function carregaTipoMural() {
        $tabela = new TabelaModel("tipo_mural", "id_tipo_mural", "nm_tipo_mural");
        $coluna = $this->adicionaColunaNormal($tabela, "ID tipo_mural", "id_tipo_mural", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tipo_mural"), "nm_tipo_mural", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }

    function carregaMural() {
        $tabela = new TabelaModel("mural", "id_mural", "texto_mural");
        $coluna = $this->adicionaColunaNormal($tabela, "ID mural", "id_mural", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_mural"), "texto_mural", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_nm_fornecedor"), "id_fornecedor", true, false, "fornecedor", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tipo_mural"), "id_tipo_mural", true, false, "tipo_mural", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }
    function carregaMedia() {
        $tabela = new TabelaModel("media", "id_media", "nm_media");
        $coluna = $this->adicionaColunaNormal($tabela, "ID media", "id_media", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_media"), "nm_media", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }
    function carregaProdutoMedia() {
        $tabela = new TabelaModel("produto_media", "id_produto_media");
        $coluna = $this->adicionaColunaNormal($tabela, "ID produto_media", "id_produto_media", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_produto"), "id_produto", true, false, "produto", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_media"), "id_media", true, false, "media", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }

    function carregaFornecedor() {
        $tabela = new TabelaModel("fornecedor", "id_fornecedor", "nm_fornecedor");
        $coluna = $this->adicionaColunaNormal($tabela, "ID fornecedor", "id_fornecedor", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_nm_fornecedor"), "nm_fornecedor", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_usuario"), "id_usuario", true, false, "usuario", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }

    function carregaProduto() {
        $tabela = new TabelaModel("produto", "id_produto", "nm_produto");
        $coluna = $this->adicionaColunaNormal($tabela, "ID produto", "id_produto", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_nm_produto"), "nm_produto", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("hint_nm_produto");
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_vlr_unitario"), "vlr_unitario", false, TIPO_INPUT_CURRENCY);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_desc_produto"), "desc_produto", false, TIPO_INPUT_TEXTAREA);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("hint_desc_produto");

        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_nm_fornecedor"), "id_fornecedor", true, false, "fornecedor", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_categoria"), "id_categoria_produto", false, false, "categoria_produto", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tabela_calculo"), "id_tabela_calculo", false, false, "tabela_calculo", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        
        $this->adicionaColunaDescreveFK($tabela, translateKey("txt_nm_fornecedor"), "nm_fornecedor", "fornecedor", "fornecedor.id_fornecedor=produto.id_fornecedor");
        $this->adicionaColunaDescreveFK($tabela, translateKey("txt_categoria"), "nm_categoria_produto", "categoria_produto", "categoria_produto.id_categoria_produto=produto.id_categoria_produto");

        return $tabela;
    }

}

?>