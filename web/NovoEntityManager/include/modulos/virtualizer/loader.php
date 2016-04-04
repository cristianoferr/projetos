<?

function loader_virtualizer($fileManager, $modulo) {//coberto
    $folder = $modulo->getName();

    $fileManager->addPHP('controller/controller_screen.php', $folder);
    $fileManager->addPHP('controller/controller_tile.php', $folder);
    $fileManager->addPHP('controller/controller_tile_size.php', $folder);
    $fileManager->addPHP('controller/controller_tile_orientation.php', $folder);
    $fileManager->addPHP('controller/controller_widget.php', $folder);
    $fileManager->addPHP('controller/controller_widget_section.php', $folder);
    $fileManager->addPHP('controller/controller_widget_coluna.php', $folder);
    $fileManager->addPHP('controller/controller_panel.php', $folder);
    $fileManager->addPHP('controller/controller_panel_type.php', $folder);
    $fileManager->addPHP('controller/controller_site_projeto.php', $folder);
    $fileManager->addPHP('controller/controller_panel_type_action.php', $folder);

    $fileManager->addPHP('manager/controller_manager_virtualizer.php', $folder);
    $fileManager->addPHP('manager/painel_manager_virtualizer.php', $folder);
    $fileManager->addPHP('manager/table_manager_virtualizer.php', $folder);
    $fileManager->addPHP('manager/security.php', $folder);
    $fileManager->addPHP('consts.php', $folder);

    $fileManager->addPHP('view/painel_show_screen.php', $folder);

    $fileManager->addPHP('widget/widget_manager.php', $folder);
    $fileManager->addPHP('widget/widget_show_screen.php', $folder);
    $fileManager->addPHP('widget/widget_new_screen.php', $folder);
    $fileManager->addPHP('widget/widget_show_widget.php', $folder);
    $fileManager->addPHP('widget/widget_edit_widget.php', $folder);
    $fileManager->addPHP('widget/widget_list_screens.php', $folder);
    $fileManager->addPHP('widget/widget_list_widgets.php', $folder);
    $fileManager->addPHP('widget/widget_widget_properties.php', $folder);
    $fileManager->addPHP('widget/widget_add_widget_section.php', $folder);
    $fileManager->addPHP('widget/widget_add_widget_coluna.php', $folder);
    $fileManager->addPHP('widget/widget_delete_widget_section.php', $folder);
    $fileManager->addPHP('widget/widget_delete_widget_coluna.php', $folder);
    $fileManager->addPHP('widget/widget_edit_tile.php', $folder);
    $fileManager->addPHP('widget/widget_show_tile.php', $folder);
    $fileManager->addPHP('widget/widget_add_tile.php', $folder);

    $fileManager->addCSS("css/" . $folder . "/virtualizer_styles.css", $folder);
}

function initialize_virtualizer($fileManager, $modulo) {//coberto
    $folder = $modulo->getName();
    $controller = new ControllerManager_Virtualizer();
    $fileManager->setControllerManager($controller, $folder);
    $fileManager->setTableManager(new TableManagerVirtualizer(), $folder);

    $fileManager->addPainelManager(new PainelManagerVirtualizer());
    getWidgetManager()->addManager(new WidgetManagerVirtualizer());
    $fileManager->addSecurityManager(new VirtualizerSecurity($folder));
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