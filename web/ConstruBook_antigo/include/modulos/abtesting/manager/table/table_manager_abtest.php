<?

class TableManagerABTest extends TableManager {

    function carregaTabelaComNome($tableName) {
        if ($tableName == "abtest") {
            return $this->carregaABTest();
        }

        if ($tableName == "tipo_abtest") {
            return $this->carregaTipoABTest();
        }

        if ($tableName == "feature_check") {
            return $this->carregaFeatureCheck();
        }

        if ($tableName == "abtest_variacao") {
            return $this->carregaABTestVariacao();
        }

        if ($tableName == "abtest_usuario") {
            return $this->carregaABTestUsuario();
        }
    }

    function carregaFeatureCheck() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("feature_check", "id_feature_check");
        $coluna = $this->adicionaColunaNormal($tabela, "ID feature_check", "id_feature_check", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Nome do feature_check", "nm_feature_check", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);


        return $tabela;
    }

    function carregaABTestUsuario() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("abtest_usuario", "id_abtest_variacao");
        $coluna = $this->adicionaColunaNormal($tabela, "ID abtest", "id_abtest_variacao", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, "Usuario", "id_usuario", true, false, "usuario", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "abtest/usuario");

        return $tabela;
    }

    function carregaABTest() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("abtest", "id_abtest", "nm_abtest");
        $coluna = $this->adicionaColunaNormal($tabela, "ID abtest", "id_abtest", true, TIPO_INPUT_INTEIRO);
        // $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Nome do ABTest", "nm_abtest", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, "novo test AB");
        $coluna = $this->adicionaColunaNormal($tabela, "URL ABTest (opcional)", "url_abtest", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setHintText("Exemplo: login/new");
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, "Descrição", "desc_abtest", false, TIPO_INPUT_TEXTAREA, 250);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, "Tipo ABTest", "id_tipo_abtest", false, false, "tipo_abtest", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);

        $this->adicionaColunaCalculada($tabela, "Participantes", "cont_participantes", "select count(*) as cont_participantes from abtest_usuario,abtest_variacao where abtest_variacao.id_abtest_variacao=abtest_usuario.id_abtest_variacao and abtest_variacao.id_abtest=");
        $this->adicionaColunaCalculada($tabela, "Variações", "cont_variacoes", "select count(*) as cont_variacoes from abtest_variacao where abtest_variacao.id_abtest=");
        $this->adicionaColunaCalculada($tabela, "Votos", "count_votos", "select sum(count_abtest_variacao) as count_votos from abtest_variacao where abtest_variacao.id_abtest=");

        $tabela->setPaginaInclusao(getHomeDir() . "abtest");

        return $tabela;
    }

    function carregaTipoABTest() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("tipo_abtest", "id_tipo_abtest", "nm_tipo_abtest");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Tipo abtest", "id_tipo_abtest", true, TIPO_INPUT_INTEIRO);
        //$coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Tipo do ABTest", "nm_tipo_abtest", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, "Novo tipo Teste AB");
        $coluna = $this->adicionaColunaComFK($tabela, "Pagina", "id_pagina", false, true, "pagina", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, "Guest only?", "flag_guest_only", false, TIPO_INPUT_BOOLEAN);
        $coluna->setDetalhesInclusao(true, false, null);


        $tabela->setPaginaInclusao(getHomeDir() . "tipoabtest");

        return $tabela;
    }

    function carregaABTestVariacao() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("abtest_variacao", "id_abtest_variacao", "nm_abtest_variacao");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Variação ABTest", "id_abtest_variacao", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Código interno", "cod_abtest_variacao", true, TIPO_INPUT_INTEIRO);
        $coluna = $this->adicionaColunaNormal($tabela, "Variação ABTest", "nm_abtest_variacao", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, "Nova variação");
        $coluna = $this->adicionaColunaNormal($tabela, "Contador", "count_abtest_variacao", true, TIPO_INPUT_INTEIRO);

        $coluna = $this->adicionaColunaComFK($tabela, "ABTest", "id_abtest", true, false, "abtest", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);

        $this->adicionaColunaCalculada($tabela, "Participantes", "count_participantes", "select count(*) as cont_participantes from abtest_usuario where abtest_usuario.id_abtest_variacao=");


        $tabela->setPaginaInclusao(getHomeDir() . "abtest/variacao");

        return $tabela;
    }

}

?>