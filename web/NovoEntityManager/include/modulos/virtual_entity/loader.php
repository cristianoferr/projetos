<?

function loader_virtual_entity($fileManager, $modulo) {//coberto
    $folder = $modulo->getName();

//$fileManager->addPHP('manager/security.php', $folder);
    $fileManager->addPHP('model/tabela_virtual_model.php', $folder);
    $fileManager->addPHP('model/virtual_model.php', $folder);
    $fileManager->addPHP('model/coluna_virtual_model.php', $folder);
    $fileManager->addPHP('controller/controller_virtual.php', $folder);

    $fileManager->addPHP('manager/controller_manager_virtual_entity.php', $folder);
    $fileManager->addPHP('manager/painel_manager_virtual_entity.php', $folder);
    $fileManager->addPHP('manager/table_manager_virtual_entity.php', $folder);
    $fileManager->addPHP('view/data_view.php', $folder);
    $fileManager->addPHP('view/meta_inputs.php', $folder);
    $fileManager->addPHP('model/quarentena/meta_coluna_model.php', $folder);

    $fileManager->addPHP('widget/widget_manager.php', $folder);
    $fileManager->addPHP('widget/widget_visualiza_entidade_virtual.php', $folder);
}

function initialize_virtual_entity($fileManager, $modulo) {//coberto
    $folder = $modulo->getName();
    $controller = new ControllerManager_VirtualEntity();
    $fileManager->setControllerManager($controller, $folder);
    $fileManager->setTableManager(new TableManagerVirtualEntidade(), $folder);

    $fileManager->addPainelManager(new PainelManagerEntidadeVirtual());
    getWidgetManager()->addManager(new WidgetManagerEntidadeVirtual());
//$fileManager->addSecurityManager(new EntityManagerSecurity($folder));
    /*

      $fileManager->addPainelManager(new PainelManagerColuna());
      $fileManager->addPainelManager(new PainelManagerDatatype());
      $fileManager->addPainelManager(new PainelManagerEntidade());
      $fileManager->addPainelManager(new PainelManagerFuncao());
      $fileManager->addPainelManager(new PainelManagerParametroFuncao());
      $fileManager->addPainelManager(new PainelManagerProjeto());
     */
}

?>