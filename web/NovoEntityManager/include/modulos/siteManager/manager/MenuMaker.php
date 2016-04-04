<?php

/**
 * Classe responsável pela geração do menu do usuário
 *
 * @author CMM4
 */
class MenuMaker {

    function geraMenuTopo() {//coberto
        $topMenu = new TopMenuPainel(translateKey("site_name"), getHomeDir() . "#top");
        $id_projeto = projetoAtual();

        $this->geraMenuTopoProjetos($topMenu, $id_projeto);
        $this->geraMenuTopoHelp($topMenu, $id_projeto);
        $this->geraMenuTopoProjeto($topMenu, $id_projeto);
        if (isGuest()) {
            $this->geraMenuGuest($topMenu, $id_projeto);
        } else {
            $this->geraMenuUsuario($topMenu, $id_projeto);
        }

        if (isAdmin()) {
            $this->geraMenuAdmin($topMenu);
        }
        return $topMenu;
    }

    function geraMenuAdmin(TopMenuPainel $topMenu) {//coberto
        $mnAdmin = $topMenu->addMenu('Admin', true);
        $mnAdmin->addOpcao('Usuários', getHomeDir() . "admin/usuarios.php");
        $mnAdmin->addSeparador();
        $mnAdmin->addOpcao('Primitive Type', getHomeDir() . "admin/primitive_type.php");
        $mnAdmin->addOpcao('Meta Type', getHomeDir() . "admin/meta_type.php");
        $mnAdmin->addSeparador();
        $mnAdmin->addOpcao('Gerencia ABTests', getHomeDir() . "abtest");
        $mnAdmin->addOpcao('Tipo ABTests', getHomeDir() . "tipoabtest");
        $mnAdmin->addSeparador();
        $mnAdmin->addOpcao('Gerencia Strings Internacionais', getHomeDir() . "strings");
        $mnAdmin->addOpcao('Gerencia Páginas', getHomeDir() . "paginas");
        $mnAdmin->addSeparador();
        $mnAdmin->addOpcao('Gerencia Painéis', getHomeDir() . "paineis");
        $mnAdmin->addSeparador();
        $mnAdmin->addOpcao('Testes Unitários', getHomeDir() . "testes/testeUnitario.php");
        return $mnAdmin;
    }

    function geraMenuTopoProjetos($topMenu, $id_projeto) {
        if (usuarioAtual() != "") {
            $mnProjects = $topMenu->addMenu(translateKey("txt_projects"), false);
            $subMyProjects = $mnProjects->addOpcao(translateKey('txt_my_projects'), getHomeDir() . "projects/$id_projeto", "glyphicon glyphicon-list-alt");
            $subMyProjects->setQuantidade(getControllerManager()->getControllerForTable("projeto")->countProjetosUsuario(usuarioAtual()));
            if (!isGuest()) {
                //$subNewProject=$mnProjects->addOpcao(translateKey('txt_new_project'),getHomeDir()."project/new","glyphicon glyphicon-plus");
            }

            // $mnProjects->addSeparador(translateKey("txt_explore"));
            //$subCategorias = $mnProjects->addOpcao(translateKey('txt_categories'), getHomeDir() . "explore/categories", "glyphicon glyphicon-tags");
            // $subHotNew = $mnProjects->addOpcao(translateKey('txt_hot_new'), getHomeDir() . "explore/hotandnew", "glyphicon glyphicon-fire");
        }
    }

    function geraMenuTopoHelp($topMenu) {//coberto
        $mnHelp = $topMenu->addMenu(translateKey("txt_help"), true);
        $mnKnowledgeBase = $mnHelp->addOpcao(translateKey("know_knowledge_base"), getHomeDir() . "knowledge", "glyphicon glyphicon-info-sign");
        $mnContact = $mnHelp->addOpcao(translateKey("know_contact_us"), getHomeDir() . "#", "glyphicon glyphicon-envelope", "BAROMETER.show();return false;");
        return $mnHelp;
    }

    function geraMenuTarefasProjeto($mnProjeto, $id_projeto) {
        $mnProjeto->addSeparador(translateKey("txt_tasks"));
        //$mnTasks=$topMenu->addMenu(translateKey("txt_tasks"));
        $subDeliverables = $mnProjeto->addOpcaoComAdd(translateKey('txt_deliverables'), getHomeDir() . "deliverables/$id_projeto", getHomeDir() . "deliverables/new/$id_projeto", "glyphicon glyphicon-briefcase");
        $subDeliverables->setQuantidade(getControllerManager()->getControllerForTable("entregavel")->countForProjeto(projetoAtual()));
        $subTarefas = $mnProjeto->addOpcaoComAdd(translateKey('txt_task_manager'), getHomeDir() . "tasks/$id_projeto", getHomeDir() . "tasks/new/$id_projeto", "glyphicon glyphicon-tasks");
        $subTarefas->setQuantidade(getControllerManager()->getControllerForTable("tarefa")->countForProjeto(projetoAtual()));
    }

    function geraMenuDiagramasProjeto($mnProjeto, $id_projeto) {
        $mnProjeto->addSeparador(translateKey("txt_diagrams"));
        $subProjectDiagrams = $mnProjeto->addOpcaoComAdd(translateKey('txt_project_diagrams'), getHomeDir() . "diagrams/$id_projeto", getHomeDir() . "diagrams/new/$id_projeto", "glyphicon glyphicon-tint");
        $subMatrizDependencia = $mnProjeto->addOpcao(translateKey('txt_matriz_dependencia'), getHomeDir() . "diagrams/matrix/$id_projeto");
    }

    function geraMenuScreenProjeto($mnProjeto, $id_projeto) {
        $mnProjeto->addSeparador(translateKey("txt_screens"));
        $subProjectScreens = $mnProjeto->addOpcaoComAdd(translateKey('txt_project_screens'), getHomeDir() . "screens/$id_projeto", getHomeDir() . "screens/new/$id_projeto");
        $subProjectWidgets = $mnProjeto->addOpcaoComAdd(translateKey('txt_project_widgets'), getHomeDir() . "widgets/$id_projeto", getHomeDir() . "widgets/new/$id_projeto");
    }

    function geraMenuSettingsProjeto($mnProjeto, $id_projeto) {
        $mnProjeto->addSeparador(translateKey("txt_settings"));
        $subDatatypes = $mnProjeto->addOpcao(translateKey('txt_datatypes'), getHomeDir() . "datatypes/$id_projeto", "glyphicon glyphicon-adjust");
        $subDatatypes->setQuantidade(getControllerManager()->getControllerForTable("datatype")->countForProjeto(projetoAtual()));
        $mnProjeto->addSeparador(translateKey("txt_import_export"));
        $subImport = $mnProjeto->addOpcao(translateKey('txt_import_to_project'), getHomeDir() . "import/$id_projeto", "glyphicon glyphicon-import");
        $mnProjeto->addOpcao(" ", "");
        $subEstrutura = $mnProjeto->addOpcao(translateKey('txt_sql_structure'), getHomeDir() . "export/$id_projeto/2", "glyphicon glyphicon-export");
        $subConteudo = $mnProjeto->addOpcao(translateKey('txt_sql_content'), getHomeDir() . "export/$id_projeto/4", "glyphicon glyphicon-export");
    }

    function geraMenuTopoProjeto($topMenu, $id_projeto) {
        if ($id_projeto) {
            $projController = getControllerManager()->getControllerForTable('projeto');
            $nm_projeto = $projController->getProjectCod($id_projeto);

            //menuProjeto
            $mnProjeto = $topMenu->addMenu($nm_projeto);
            $subPaginaProjeto = $mnProjeto->addOpcao(translateKey('txt_pagina_projeto'), getHomeDir() . "project/$id_projeto", "glyphicon glyphicon-home");

            $mnProjeto->addSeparador();
            $subEntidades = $mnProjeto->addOpcaoComAdd(translateKey('txt_entities'), getHomeDir() . "entities/$id_projeto", getHomeDir() . "entities/new/$id_projeto", "glyphicon glyphicon-list");
            $subEntidades->setQuantidade(getControllerManager()->getControllerForTable("entidade")->countForProjeto(projetoAtual()));
            $subMembros = $mnProjeto->addOpcaoComAdd(translateKey('txt_members'), getHomeDir() . "members/$id_projeto", getHomeDir() . "members/new/$id_projeto", "glyphicon glyphicon-user");
            $subMembros->setQuantidade(getControllerManager()->getControllerForTable("usuario_projeto")->countForProjeto(projetoAtual()));


            $this->geraMenuTarefasProjeto($mnProjeto, $id_projeto);
            $this->geraMenuDiagramasProjeto($mnProjeto, $id_projeto);
            $this->geraMenuScreenProjeto($mnProjeto, $id_projeto);
            $this->geraMenuSettingsProjeto($mnProjeto, $id_projeto);
        } else {
            if (!isGuest()) {

                $topMenu->addLink(translateKey("txt_start_new_project"), "project/new");
            }
        }
    }

    function geraMenuGuest($topMenu) {//coberto
        $usuarioContronller = getControllerManager()->getControllerForTable("usuario");
        $nomeUsuario = $usuarioContronller->getUserLogin(usuarioAtual());
        //menu Usuario
        $mnUsuario = $topMenu->addMenu($nomeUsuario, true);
        $mnUsuario->addOpcao(translateKey("txt_create_account"), getHomeDir() . "login/new", "glyphicon glyphicon-pencil");
        $mnUsuario->addOpcao(translateKey("login_account"), getHomeDir() . "login", "glyphicon glyphicon-user");
        return $mnUsuario;
    }

    function geraMenuUsuario($topMenu) {//coberto
        $usuarioContronller = getControllerManager()->getControllerForTable("usuario");
        $nomeUsuario = $usuarioContronller->getUserLogin(usuarioAtual());
        //menu Usuario
        $mnUsuario = $topMenu->addMenu($nomeUsuario, true);
        $mnUsuario->addOpcao(translateKey("txt_my_profile"), getHomeDir() . "profile", "glyphicon glyphicon-user");
        //$mnUsuario->addOpcaoComValor(translateKey("txt_notificacoes"), getHomeDir() . "profile/notifications", '50');
        $mnUsuario->addSeparador();
        $mnUsuario->addOpcao(translateKey("txt_logout"), getHomeDir() . "logout", "glyphicon glyphicon-log-out");
        return $mnUsuario;
    }

}

?>
