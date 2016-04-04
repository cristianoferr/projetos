<?

/*
  Classe que gerencia os paineis que aparecem em projeto.php ou projetos.php
 */
define("PAINEL_PROJETO", 'P_PROJ');
define("PAINEL_FINC_PROJETO", 'P_FINC_PROJ');
define("PAINEL_IMP_PROJETO", 'P_IMP_PROJ');
define("PAINEL_SIDE_MENU", 'P_SIDE_MENU');
define("PAINEL_PROJETOS", 'P_PROJS');
define("PAINEL_PROJETOS_USUARIO", 'P_PROJS_USER');

class PainelManagerProjeto extends PainelManagerEntidade {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_PROJETO) {
            return $this->getPainelProjeto();
        }
        if ($nome == PAINEL_FINC_PROJETO) {
            return $this->getFormInclusaoProjeto();
        }
        if ($nome == PAINEL_IMP_PROJETO) {
            return $this->getFormImportProjeto();
        }
        if ($nome == PAINEL_SIDE_MENU) {
            return $this->getPainelSideMenu();
        }
        if ($nome == PAINEL_PROJETOS) {
            return $this->getPainelProjetos();
        }
        if ($nome == PAINEL_PROJETOS_USUARIO) {
            return $this->getPainelProjetosUsuario();
        }
    }

    function getPainelProjetosUsuario() {
        $table = getTableManager()->getTabelaComNome("projeto");
        $painel = new PainelHorizontal("ppu", $table);
        $painel->addColunaWithDBName("id_projeto");
        $painel->addColunaWithDBName("cod_projeto");
        $painel->addColunaWithDBName("nm_projeto");
        $painel->addColunaWithDBName("id_tipo_banco");
        $painel->addColunaWithDBName("id_metodologia");
        $painel->addColunaWithDBName("id_visibilidade_projeto");
        $painel->addColunaWithDBName("id_categoria_projeto");
        $painel->setTitulo(translateKey("txt_user_projects"));
        $painel->adicionaAcaoEditar("project/");


        $painel->modoEdicao();
        $painel->ativaAjax();
        return $painel;
    }

    function getPainelProjeto() {
        $table = getTableManager()->getTabelaComNome("projeto");
        $painel = new FormPainel("pp", $table);
        $painel->adicionaAba(ABA_COLUNA_GERAL, translateKey("txt_aba_geral"), false, true);
        $painel->adicionaAba(ABA_COLUNA_TECNICO, translateKey("txt_dados_tecnicos"), false, false);
        $painel->adicionaAba(ABA_COLUNA_INFO, translateKey("txt_misc_info"), false, false);
        $painel->adicionaAba(ABA_COLUNA_DOC, translateKey("txt_documentation"), false, false);


        $painel->addColunaWithDBName("id_projeto");
        $painel->addColunaWithDBName("cod_projeto");
        $painel->addColunaWithDBName("nm_projeto", ABA_COLUNA_GERAL);
        $painel->addColunaWithDBName("iniciais_projeto", ABA_COLUNA_GERAL);
        $painel->addColunaWithDBName("id_tipo_banco", ABA_COLUNA_TECNICO);
        $painel->addColunaWithDBName("id_metodologia", ABA_COLUNA_TECNICO);
        $painel->addColunaWithDBName("id_visibilidade_projeto", ABA_COLUNA_INFO);
        $painel->addColunaWithDBName("id_categoria_projeto", ABA_COLUNA_INFO);
        $painel->addColunaWithDBName("desc_projeto", ABA_COLUNA_DOC);
        $painel->modoEdicao();
        $painel->ativaAjax();
        return $painel;
    }

    function getFormInclusaoProjeto() {
        $table = getTableManager()->getTabelaComNome("projeto");
        $painel = new PainelVertical("pp", $table);
        $painel->addColunaWithDBName("id_projeto");
        $painel->addColunaWithDBName("cod_projeto_novo");
        $painel->addColunaWithDBName("nm_projeto");
        $painel->addColunaWithDBName("iniciais_projeto");
        $painel->addColunaWithDBName("id_tipo_banco");
        $painel->addColunaWithDBName("id_metodologia");
        $painel->addColunaWithDBName("id_visibilidade_projeto");
        $painel->addColunaWithDBName("id_categoria_projeto");
        $painel->modoInclusao();
        return $painel;
    }

    function getFormImportProjeto() {
        $table = getTableManager()->getTabelaComNome("projeto");
        $painel = new PainelVertical("pp", $table);
        $painel->addColunaWithDBName("sql_import");
        $painel->modoInclusao();
        return $painel;
    }

    function getPainelSideMenu() {
        $table = getTableManager()->getTabelaComNome("projeto");
        $painel = new SideMenuPainel("psm", $table);
        $painel->addColunaWithDBName("id_projeto");
        $painel->addColunaWithDBName("cod_projeto");
        $painel->addColunaWithDBName("nm_projeto");
        $painel->setTitulo(translateKey('txt_projects'));
        $painel->setLinkTitulo("projects");
        $painel->setLinkItem("project/");

        $table = getTableManager()->getTabelaComNome("entidade");
        $painelFilho = new SubSideMenuPainel("pssm", $table);
        $painelFilho->addColunaWithDBName("id_entidade");
        $painelFilho->addColunaWithDBName("nm_entidade");

        $pagina = $this->getPaginaAtual();
        $painelFilho->setLinkItem($pagina);

        $painel->setPainelFilho($painelFilho, "and id_projeto=");
        return $painel;
    }

    function getPaginaAtual() {
        $pagina = "entity/";
        if (getIDPaginaAtual() == PAGINA_VISUALIZA) {
            $pagina = "view/";
        }
        if (getIDPaginaAtual() == PAGINA_TAREFAS) {
            //    $pagina = "tasks/";
        }
        return $pagina;
    }

    //usado em projetos.php
    function getPainelProjetos() {
        if (!isset($this->painelProjetos)) {
            $table = getTableManager()->getTabelaComNome("projeto");
            $painel = new CardPainel("pps", $table);
            $painel->addColunaWithDBName("id_projeto");
            $painel->addColunaWithDBName("cod_projeto");
            $painel->addColunaWithDBName("cont_entidades");
            $painel->addColunaWithDBName("cont_tarefas");
            $painel->addColunaWithDBName("nm_metodologia");
            $painel->addColunaWithDBName("id_categoria_projeto");
            $painel->addColunaWithDBName("id_visibilidade_projeto");
            $painel->addColunaWithDBName("cont_sub_projetos");

            $painel->adicionaAcao("import/", translateKey("txt_import"), false);
            $painel->adicionaAcao("diagrams/", translateKey("txt_diagrams"), false);
            $painel->adicionaAcao("tasks/", translateKey("txt_tasks"), false);
            $painel->adicionaAcao("deliverables/", translateKey("txt_deliverables"), false);
            $painel->adicionaAcao("export/", translateKey("txt_export"), false);


            $painel->adicionaLink("project/new", translateKey("txt_new_project"), false);
            if (!isGuest())
                $painel->setDetalhesNovoRegistro("project/new", translateKey("txt_new_project"));
            $painel->modoVisualizacao();

            $this->painelProjetos = $painel;
        }
        return $this->painelProjetos;
    }

}

?>