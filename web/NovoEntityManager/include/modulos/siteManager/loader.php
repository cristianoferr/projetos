<?

function loader_siteManager($fileManager, $modulo) {
    $folder = $modulo->getName();
    $fileManager->addPHP('consts_site.php', $folder);

    $fileManager->addPHP('openid.php', $folder);
    $fileManager->addPHP('manager/table_manager_siteManager.php', $folder);
    $fileManager->addPHP('manager/controller_manager_siteManager.php', $folder);
    $fileManager->addPHP('controller/controller_elemento.php', $folder);
    $fileManager->addPHP('controller/controller_pagina.php', $folder);
    $fileManager->addPHP('controller/controller_tipo_pagina.php', $folder);
    $fileManager->addPHP('controller/controller_usuario.php', $folder);
    $fileManager->addPHP('controller/controller_chave_lingua.php', $folder);
    $fileManager->addPHP('controller/controller_valor_lingua.php', $folder);
    $fileManager->addPHP('controller/controller_lingua.php', $folder);
    $fileManager->addPHP('controller/controller_ticket_reset.php', $folder);
    $fileManager->addPHP('controller/controller_papel.php', $folder);

    $fileManager->addPHP('manager/painel/painel_manager_chave_lingua.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_pagina.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_usuario.php', $folder);
    $fileManager->addPHP('manager/MenuMaker.php', $folder);

    $fileManager->addPHP('view/painel/painel_top_menu.php', $folder);
    $fileManager->addPHP('view/painel/painel_sub_menu.php', $folder);


    $fileManager->addPHP('manager/security.php', $folder);

    $fileManager->addPHP('widget/widget_siteManager.php', $folder);
    $fileManager->addPHP('widget/widget_login_google.php', $folder);
    $fileManager->addPHP('widget/widget_projetos_usuario.php', $folder);
    $fileManager->addPHP('widget/widget_edit_usuario.php', $folder);
    $fileManager->addPHP('widget/widget_registro_usuario.php', $folder);
}

function initialize_siteManager($fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_SiteManager();
    $fileManager->setControllerManager($controller, $folder);

    $fileManager->setTableManager(new TableManagerSiteManager(), $folder);
    $fileManager->addSecurityManager(new SiteManagerSecurity($folder));

    getWidgetManager()->addManager(new WidgetSiteManager());
    $fileManager->addPainelManager(new PainelManagerChaveLingua());
    $fileManager->addPainelManager(new PainelManagerPagina());
}

s
?>