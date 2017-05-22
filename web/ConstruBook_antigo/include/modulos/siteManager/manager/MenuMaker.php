<?php

/**
 * Classe responsável pela geração do menu do usuário
 *
 * @author CMM4
 */
class MenuMaker {

    function geraMenuTopo() {//coberto
        $topMenu = new TopMenuPainel(translateKey("site_name"), getHomeDir() . "#top");

        //Busca_TM
        if (usuarioAtual() != "") {
            $this->geraMenuTopoFornecedores($topMenu);
            //$this->geraMenuTopoProdutos($topMenu);
        }
        $this->geraBusca($topMenu);
        // $this->geraMenuTopoHelp($topMenu, $id_projeto);



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

    function geraMenuUsuario($topMenu) {//coberto
        if (isFornecedor()) {
            $mnUsuario=$this->geraMenuFornecedor($topMenu);
        } else {
            $mnUsuario=$this->geraMenuCliente($topMenu);
        }
        $mnUsuario->addSeparador(translateKey("txt_usuario"));

        $mnUsuario->addOpcao(translateKey("txt_my_profile"), getHomeDir() . "profile", "glyphicon glyphicon-user");
        //$mnUsuario->addOpcaoComValor(translateKey("txt_notificacoes"), getHomeDir() . "profile/notifications", '50');
        $mnUsuario->addSeparador();
        $mnUsuario->addOpcao(translateKey("txt_logout"), getHomeDir() . "logout", "glyphicon glyphicon-log-out");
        return $mnUsuario;
    }
    
    function menuSeletorPerfil($menu){
        $usuarioController = getControllerManager()->getControllerForTable("usuario");
        $nomeUsuario = $usuarioController->getUserLogin(usuarioAtual());
        
        $controller = getControllerManager()->getControllerForTable("fornecedor");
        $controller->loadPerfisUsuario(usuarioAtual());
        $menu->addSeparador(translateKey("txt_escolher_perfil"));
        $this->addPerfilMenu($menu,$nomeUsuario,PERFIL_CLIENTE);
        while ($model=$controller->next()){
            $this->addPerfilMenu($menu,$model->getDescricao(),$model->getId());
        }
    }
    
    function addPerfilMenu($menu,$desc,$id){
        $controller = getControllerManager()->getControllerForTable("fornecedor");
        $perfil=$controller->perfilAtual();
        if ($id==$perfil){
            $icon= "glyphicon glyphicon glyphicon-check";
        } else {
            $icon= "glyphicon glyphicon glyphicon-unchecked";
        }
        $menu->addOpcao($desc, getHomeDir() . "perfil/$id",$icon);
    }

    //Menu quando o usuário é cliente
    function geraMenuCliente(TopMenuPainel $topMenu) {
        $usuarioContronller = getControllerManager()->getControllerForTable("usuario");
        $nomeUsuario = $usuarioContronller->getUserLogin(usuarioAtual());
        //menu Usuario
        $mnAdmin = $topMenu->addMenu($nomeUsuario, true);
        $this->menuSeletorPerfil($mnAdmin);

        $mnAdmin->addSeparador(translateKey("txt_menu_cliente"));
        $mnAdmin->addOpcao(translateKey("txt_minhas_solicitacoes"), getHomeDir() . "cliente/solicitacoes");
        return $mnAdmin;
    }

    //Menu quando o usuário é fornecedor
    function geraMenuFornecedor(TopMenuPainel $topMenu) {
        $controller = fornecedor();
        $fornecedor = $controller->loadSingle(fornecedorAtual());
        $mnAdmin = $topMenu->addMenu($fornecedor->getDescricao(), true);
        
        $this->menuSeletorPerfil($mnAdmin);
        
        $mnAdmin->addSeparador(translateKey("txt_fornecedor"));
        $mnAdmin->addOpcao(translateKey("txt_meus_produtos"), getHomeDir() . "fornecedor/produtos");
        $mnAdmin->addOpcao(translateKey("txt_categorias"), getHomeDir() . "fornecedor/categorias");
        $mnAdmin->addOpcao(translateKey("txt_tabela_calculo"), getHomeDir() . "fornecedor/tabela");
        $mnAdmin->addOpcao(translateKey("txt_meus_clientes"), getHomeDir() . "fornecedor/clientes");
        $mnAdmin->addOpcao(translateKey("txt_solicitacoes_clientes"), getHomeDir() . "fornecedor/solicitacoes");
        $mnAdmin->addOpcao(translateKey("txt_configuracao"), getHomeDir() . "fornecedor");
        return $mnAdmin;
    }

    function geraBusca(TopMenuPainel $topMenu) {
        $topMenu->addBusca(translateKey("txt_busca"), "busca", false);
    }

    function geraMenuAdmin(TopMenuPainel $topMenu) {
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

    function geraMenuTopoProdutos($topMenu) {
        $menu = $topMenu->addMenu(translateKey("txt_produtos"), true);
        $menu->addOpcao(translateKey('txt_buscar_produtos'), getHomeDir() . "produto/buscar", "glyphicon glyphicon-search");
        $menu->addOpcao(translateKey('txt_categorias'), getHomeDir() . "produto/categorias", "glyphicon glyphicon-th-list");
    }

    function geraMenuTopoFornecedores($topMenu) {

        $menu = $topMenu->addMenu(translateKey("txt_fornecedores"), false);
        // $menu->addOpcao(translateKey('txt_buscar_fornecedores'), getHomeDir() . "fornecedor/buscar", "glyphicon glyphicon-search");
        $subFornecedores = $menu->addOpcao(translateKey('txt_meus_fornecedores'), getHomeDir() . "fornecedores", "glyphicon glyphicon-th-list");
        //   $subMyProjects->setQuantidade(getControllerManager()->getControllerForTable("projeto")->countProjetosUsuario(usuarioAtual()));

        $controller = getControllerForTable("fornecedor");

        $menu->addSeparador("");
        $controller->listaFornecedoresConectados();
        $i = 0;
        while ($model = $controller->next()) {
            $id = $model->getValorCampo("id_fornecedor");
            $nm = $model->getValorCampo("nm_fornecedor");
            $menu->addOpcao($nm, getHomeDir() . "fornecedores/$id", "");
            $i++;
        }
        $subFornecedores->setQuantidade($i);


        //$subCategorias = $mnProjects->addOpcao(translateKey('txt_categories'), getHomeDir() . "explore/categories", "glyphicon glyphicon-tags");
        // $subHotNew = $mnProjects->addOpcao(translateKey('txt_hot_new'), getHomeDir() . "explore/hotandnew", "glyphicon glyphicon-fire");
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

}

?>
