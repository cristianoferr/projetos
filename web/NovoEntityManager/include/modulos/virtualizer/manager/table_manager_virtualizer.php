<?

class TableManagerVirtualizer extends TableManager {

    function carregaTabelaComNome($tableName) {


        if ($tableName == "screen") {
            return $this->carregaScreen();
        }
        if ($tableName == "tile") {
            return $this->carregaTile();
        }
        if ($tableName == "tile_size") {
            return $this->carregaTileSize();
        }
        if ($tableName == "tile_orientation") {
            return $this->carregaTileOrientation();
        }
        if ($tableName == "widget") {
            return $this->carregaWidget();
        }
        if ($tableName == "painel") {
            return $this->carregaPainel();
        }
        if ($tableName == "widget_section") {
            return $this->carregaWidgetSection();
        }
        if ($tableName == "widget_coluna") {
            return $this->carregaWidgetColuna();
        }
        if ($tableName == "tipo_painel") {
            return $this->carregaTipoPainel();
        }
        if ($tableName == "link") {
            return $this->carregaLink();
        }
        if ($tableName == "site_projeto") {
            return $this->carregaSiteProjeto();
        }
    }

    function carregaSiteProjeto() {//coberto
        $tabela = new TabelaModel("site_projeto", "id_projeto", "label_projeto");
        $coluna = $this->adicionaColunaNormal($tabela, "ID projeto", "id_projeto", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_screen_inicial"), "id_screen_inicial", false, true, "screen", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_label_projeto"), "label_projeto", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }

    function carregaWidgetColuna() {//coberto
        $tabela = new TabelaModel("widget_coluna", "id_widget_coluna");
        $coluna = $this->adicionaColunaNormal($tabela, "ID widget_coluna", "id_widget_coluna", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_property"), "id_coluna", false, true, "coluna", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_widget_section"), "id_widget_section", true, false, "widget_section", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_widget_coluna_seq"), "seq_widget_coluna", false, TIPO_INPUT_SEQUENCE);
        $coluna->setDetalhesInclusao(true, true, null);

        $this->adicionaColunaDescreveFK($tabela, translateKey("txt_widget_section"), "nm_widget_coluna", "widget_section", "widget_coluna.id_widget_section=widget_section.id_widget_section");

        $tabela->setPaginaInclusao(getHomeDir() . "widgetColuna");

        return $tabela;
    }

    function carregaWidgetSection() {//coberto
        $tabela = new TabelaModel("widget_section", "id_widget_section", "nm_widget_section");
        $coluna = $this->adicionaColunaNormal($tabela, "ID widget_section", "id_widget_section", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_widget"), "id_widget", true, false, "widget", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_widget_section"), "nm_widget_section", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_widget_section_seq"), "seq_widget_section", false, TIPO_INPUT_SEQUENCE);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "widgetSection");

        return $tabela;
    }

    function carregaWidget() {//coberto
        $tabela = new TabelaModel("widget", "id_widget", "nm_widget");
        $coluna = $this->adicionaColunaNormal($tabela, "ID widget", "id_widget", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_project"), "id_projeto", true, false, "projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_entity"), "id_entidade", true, false, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_painel"), "id_painel", false, false, "painel", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_widget_name"), "nm_widget", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_widget_title"), "title_widget", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, false, null);

        $tabela->setPaginaInclusao(getHomeDir() . "widget");

        return $tabela;
    }

    function carregaPainel() {//coberto
        $tabela = new TabelaModel("painel", "id_painel", "nm_painel");
        $coluna = $this->adicionaColunaNormal($tabela, "ID painel", "id_painel", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tipo_painel"), "id_tipo_painel", true, false, "tipo_painel", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_painel_name"), "nm_painel", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, "Classe do Painel", "classe_painel", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_painel_description"), "desc_painel", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "paineis");
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaLink() {//coberto
        $tabela = new TabelaModel("link", "id_link", "nm_link");
        $coluna = $this->adicionaColunaNormal($tabela, "ID link", "id_link", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tipo_painel"), "id_tipo_painel", true, false, "tipo_painel", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_arte_visual"), "id_arte_visual", false, false, "arte_visual", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_screen_redirect"), "id_screen_redirect", false, false, "screen", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_widget_redirect"), "id_widget_redirect", false, false, "widget", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_widget_owner"), "id_widget_owner", true, true, "widget", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tile_owner"), "id_tile_owner", true, true, "tile", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_link"), "nm_link", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);


        $tabela->setPaginaInclusao(getHomeDir() . "link");
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaTipoPainel() {//coberto
        $tabela = new TabelaModel("tipo_painel", "id_tipo_painel", "nm_tipo_painel");
        $coluna = $this->adicionaColunaNormal($tabela, "ID tipo_painel", "id_tipo_painel", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tipo_painel"), "nm_tipo_painel", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tipo_painel_multi"), "flag_multi", false, TIPO_INPUT_BOOLEAN);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "painel");
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaTileSize() {//coberto
        $tabela = new TabelaModel("tile_size", "id_tile_size", "nm_tile_size");
        $coluna = $this->adicionaColunaNormal($tabela, "ID tile_size", "id_tile_size", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tile_size"), "nm_tile_size", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_perc_tile_size"), "perc_tile_size", false, TIPO_INPUT_INTEIRO);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "tile_size");
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaTileOrientation() {//coberto
        $tabela = new TabelaModel("tile_orientation", "id_tile_orientation", "nm_tile_orientation");
        $coluna = $this->adicionaColunaNormal($tabela, "ID tile_orientation", "id_tile_orientation", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tile_orientation"), "nm_tile_orientation", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "tileorientation");
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaScreen() {//coberto
        $tabela = new TabelaModel("screen", "id_screen", "nm_screen");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Screen", "id_screen", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_project"), "id_projeto", true, false, "projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_screen_name"), "nm_screen", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_screen_caption"), "caption_screen", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        $tabela->setPaginaInclusao(getHomeDir() . "screens");

        return $tabela;
    }

    function carregaTile() {//coberto
        $tabela = new TabelaModel("tile", "id_tile", "nm_tile");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Tile", "id_tile", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_tile"), "nm_tile", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_screen"), "id_screen", true, false, "screen", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tile_parent"), "id_tile_parent", true, false, "tile", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_orientation"), "id_tile_orientation", false, false, "tile_orientation", TIPO_INPUT_RADIO);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_widget"), "id_widget", false, true, "widget", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(false, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_tile_size"), "id_tile_size", false, false, "tile_size", TIPO_INPUT_RADIO);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_order"), "seq_tile", false, TIPO_INPUT_SEQUENCE);
        $coluna->setDetalhesInclusao(true, true, null);

//inclusao
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_how_many"), "qtd_tile", true, TIPO_INPUT_INTEIRO);
        $coluna->setDetalhesInclusao(true, true, 1);



        $tabela->setPaginaInclusao(getHomeDir() . "screens/tile");

        return $tabela;
    }

}

?>