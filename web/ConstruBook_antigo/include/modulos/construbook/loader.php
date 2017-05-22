<?

function loader_construbook($fileManager, $modulo) {
    $folder = $modulo->getName();

    $fileManager->addPHP('consts_construbook.php', $folder);
    $fileManager->addPHP('manager/controller_manager_construbook.php', $folder);
    $fileManager->addPHP('manager/table_manager_construbook.php', $folder);
    $fileManager->addPHP('manager/painel_manager_construbook.php', $folder);
    
    $fileManager->addPHP('controller/controller_fornecedor.php', $folder);
    $fileManager->addPHP('controller/controller_produto.php', $folder);
    $fileManager->addPHP('controller/controller_media.php', $folder);
    $fileManager->addPHP('controller/controller_tabela_calculo.php', $folder);
    $fileManager->addPHP('controller/controller_mural.php', $folder);
    
    $fileManager->addPHP('widget/widget_construbook.php', $folder);
    $fileManager->addPHP('widget/widget_mural.php', $folder);
    $fileManager->addPHP('widget/widget_produto.php', $folder);
    $fileManager->addPHP('widget/widget_categoria.php', $folder);
    $fileManager->addPHP('widget/widget_tabela.php', $folder);
    $fileManager->addPHP('view/painel_fornecedor.php', $folder);
    $fileManager->addPHP('view/painel_visualiza_fornecedor.php', $folder);
    $fileManager->addPHP('view/painel_mural.php', $folder);
    $fileManager->addPHP('view/painel_produto.php', $folder);
    $fileManager->addPHP('manager/security.php', $folder);
}

function initialize_construbook($fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_Construbook();
    $fileManager->setControllerManager($controller, $folder);

    $fileManager->addPainelManager(new PainelManagerConstrubook());

    $fileManager->setTableManager(new TableManagerConstrubook(), $folder);
    getWidgetManager()->addManager(new WidgetConstrubook());
    $fileManager->addSecurityManager(new ConstrubookSecurity($folder));
}


?>