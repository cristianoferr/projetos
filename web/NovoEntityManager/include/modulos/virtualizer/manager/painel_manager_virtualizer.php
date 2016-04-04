<?

define("PAINEL_SHOW_SCREEN", 'p_SHOW_SCREEN');
define("PAINEL_SHOW_WIDGET", 'p_SHOW_WIDGET');
define("PAINEL_EDIT_WIDGET", 'p_EDIT_WIDGET');
define("PAINEL_LIST_SCREENS", 'LIST_SCREENS');
define("PAINEL_LIST_WIDGETS", 'LIST_WIDGETS');
define("PAINEL_INCLUSAO_PAINEL", 'P_INC_PAINEL');
define("PAINEL_PAINEIS", 'PAINEL_PAINEIS');
define("PAINEL_PAINEL", 'PAINEL_PAINEL');
define("PAINEL_SHOW_WIDGET_PROPERTIES", 'PAINEL_SHOW_WIDGET_PROPERTIES');
define("PAINEL_FORM_INC_SCREEN", 'P_FINC_SCREEN');
define("PAINEL_EDIT_TILE", 'P_EDIT_TILE');
define("PAINEL_ADD_TILE", 'P_ADD_TILE');

class PainelManagerVirtualizer {

    function getPainelComNome($nome) {//coberto
        if ($nome == PAINEL_FORM_INC_SCREEN) {
            return $this->getFormInclusaoScreen();
        }
        if ($nome == PAINEL_ADD_TILE) {
            return $this->getFormAddTile();
        }

        if ($nome == PAINEL_EDIT_TILE) {
            return $this->getPainelEditTile();
        }
        if ($nome == PAINEL_SHOW_SCREEN) {
            return $this->getPainelShowScreen();
        }
        if ($nome == PAINEL_SHOW_WIDGET_PROPERTIES) {
            return $this->getPainelShowWidgetProperties();
        }
        if ($nome == PAINEL_SHOW_WIDGET) {
            return $this->getPainelShowWidget();
        }
        if ($nome == PAINEL_EDIT_WIDGET) {
            return $this->getPainelEditWidget();
        }
        if ($nome == PAINEL_LIST_SCREENS) {
            return $this->getPainelListScreens();
        }
        if ($nome == PAINEL_LIST_WIDGETS) {
            return $this->getPainelListWidgets();
        }
        if ($nome == PAINEL_INCLUSAO_PAINEL) {
            return $this->getPainelInclusaoPainel();
        }
        if ($nome == PAINEL_PAINEIS) {
            return $this->getPainelPaineis();
        }
        if ($nome == PAINEL_PAINEL) {
            return $this->getPainelPainel();
        }
    }

    function getFormInclusaoScreen() {
        $table = getTableManager()->getTabelaComNome("screen");
        $painel = new PainelVertical("pis", $table);
        $painel->addColunaWithDBName("nm_screen");
        $painel->addColunaWithDBName("caption_screen");

        $painel->adicionaLinkImportante(getHomeDir() . "screens/" . projetoAtual(), translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getFormAddTile() {
        $table = getTableManager()->getTabelaComNome("tile");
        $painel = new PainelVertical("fat", $table);
        $painel->addColunaWithDBName("qtd_tile");
        $painel->addColunaWithDBName("id_tile_orientation");
        $painel->addColunaWithDBName("id_tile_size");
        $painel->modoInclusao();
        $painel->desativaAjax();
        $painel->setTitulo(translateKey("txt_add_new_tile"));
        return $painel;
    }

    function getPainelShowWidgetProperties() {
        $painel = new PainelMestreDetalhe();
        $painel->setFormName("md");
        $painel->defineMaster("widget_section", "id_widget_section", "seq_widget_section");
        $painel->defineDetail("widget_coluna", "seq_widget_coluna");

        $painel->addColunaMestre("id_widget_section");
        $painel->addColunaMestre("seq_widget_section");
        $painel->addColunaMestre("nm_widget_section");
        $painel->addColunaDetalhe("id_widget_coluna");
        $painel->addColunaDetalhe("seq_widget_coluna");
        $painel->addColunaDetalhe("id_coluna");


        return $painel;
    }

    function getPainelEditTile() {
        $table = getTableManager()->getTabelaComNome("tile");
        $painel = new PainelVertical("pet", $table);
        $painel->addColunaWithDBName("id_tile");
        $painel->addColunaWithDBName("id_widget");
        $painel->addColunaWithDBName("id_tile_size");
        $painel->addColunaWithDBName("id_tile_orientation");

        $painel->modoEdicao();

        return $painel;
    }

    function getPainelPaineis() {
        $table = getTableManager()->getTabelaComNome("painel");
        $painel = new PainelHorizontal("pps", $table);
        $painel->addColunaWithDBName("id_tipo_painel");
        $painel->addColunaWithDBName("nm_painel");
        $painel->addColunaWithDBName("classe_painel");

        $painel->modoEdicao();
        $painel->setEditLink("paineis/");
        $painel->setDeleteLink("paineis/delete/");

        return $painel;
    }

    function getPainelPainel() {
        $table = getTableManager()->getTabelaComNome("painel");
        $painel = new PainelVertical("pp", $table);
        $painel->addColunaWithDBName("id_tipo_painel");
        $painel->addColunaWithDBName("nm_painel");
        $painel->addColunaWithDBName("desc_painel");
        $painel->addColunaWithDBName("classe_painel");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelEditWidget() {
        $table = getTableManager()->getTabelaComNome("widget");
        $painel = new PainelVertical("pew", $table);
        $painel->addColunaWithDBName("id_widget");
        $painel->addColunaWithDBName("id_entidade");
        $painel->addColunaWithDBName("nm_widget");
        $painel->addColunaWithDBName("title_widget");
        $painel->addColunaWithDBName("id_painel");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelShowWidget() {
        $table = getTableManager()->getTabelaComNome("widget");
        $painel = new PainelVertical("psw", $table);
        $painel->addColunaWithDBName("id_widget");
        $painel->addColunaWithDBName("id_entidade");
        $painel->addColunaWithDBName("id_painel");
        $painel->addColunaWithDBName("nm_widget");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelInclusaoPainel() {
        $table = getTableManager()->getTabelaComNome("painel");
        $painel = new FormPainel("pipa", $table);
        $painel->addColunaWithDBName("id_tipo_painel");
        $painel->addColunaWithDBName("nm_painel");
        $painel->addColunaWithDBName("desc_painel");
        $painel->addColunaWithDBName("classe_painel");

        $painel->modoInclusao();
        $painel->desativaAjax();

        return $painel;
    }

    function getPainelListWidgets() {//coberto
        $table = getTableManager()->getTabelaComNome("widget");
        $painel = new PainelHorizontal("plw", $table);
        $painel->addColunaWithDBName("id_widget");
        $painel->addColunaWithDBName("nm_widget");
        $painel->addColunaWithDBName("title_widget");
        $painel->addColunaWithDBName("id_entidade");
        $painel->addColunaWithDBName("id_painel");
        //$painel->setEditWidget(WIDGET_EDIT_SCREEN);
        $painel->setEditLink("widgets/" . projetoAtual() . "/");
        $painel->setDeleteLink("widgets/delete/" . projetoAtual() . "/");
        return $painel;
    }

    function getPainelListScreens() {//coberto
        $table = getTableManager()->getTabelaComNome("screen");
        $painel = new PainelHorizontal("pls", $table);
        $painel->addColunaWithDBName("id_screen");
        $painel->addColunaWithDBName("nm_screen");
        $painel->addColunaWithDBName("caption_screen");
        //$painel->setEditWidget(WIDGET_EDIT_SCREEN);
        $painel->setEditLink("screens/" . projetoAtual() . "/");
        $painel->setDeleteLink("screens/delete/" . projetoAtual() . "/");
        return $painel;
    }

    function getPainelShowScreen() {//coberto
        $painel = new PainelScreen();
        return $painel;
    }

}

?>