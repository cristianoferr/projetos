<?

function loader_diagramas($fileManager, $modulo) {
    $folder = $modulo->getName();
    $fileManager->addCSS("css/" . $folder . "/estilos_uml.css", $folder);

    //$fileManager->addJS("js/" . $folder . "/kinetic-v4.7.2.min.js", $folder);
    $fileManager->addJS("js/" . $folder . "/DragDrop.js", $folder);
    //$fileManager->addJS("js/" . $folder . "/jquery.ui.touch-punch.min.js", $folder); //touch punch é um script para fazer drag drop em mobile
    $fileManager->addJS("js/" . $folder . "/jquery.ui.touch-punch-dev.js", $folder); //touch punch é um script para fazer drag drop em mobile

    $fileManager->addPHP('consts_diagrama.php', $folder);
    $fileManager->addPHP('model/componente_model.php', $folder);
    $fileManager->addPHP('manager/controller_manager_diagrama.php', $folder);
    $fileManager->addPHP('controller/controller_tipo_diagrama.php', $folder);
    $fileManager->addPHP('controller/controller_diagrama.php', $folder);
    $fileManager->addPHP('controller/component_loader.php', $folder);
    $fileManager->addPHP('controller/controller_componente_diagrama.php', $folder);
    $fileManager->addPHP('controller/controller_matriz_dependencia.php', $folder);
    $fileManager->addPHP('controller/controller_export_conteudo.php', $folder);
    $fileManager->addPHP('controller/controller_export.php', $folder);

    $fileManager->addPHP('manager/security.php', $folder);

    $fileManager->addPHP('manager/table/table_manager_diagrama.php', $folder);
    $fileManager->addPHP('manager/widget/widget_diagrama.php', $folder);
    $fileManager->addPHP('view/view_diagrama.php', $folder);
    $fileManager->addPHP('view/view_diagrama_wbs.php', $folder);
    $fileManager->addPHP('view/view_componente_base.php', $folder);
    $fileManager->addPHP('view/view_componente_entidade.php', $folder);
    $fileManager->addPHP('view/view_componente_entregavel.php', $folder);
    $fileManager->addPHP('view/view_seletor_componentes.php', $folder);
    $fileManager->addPHP('view/view_matriz_dependencia.php', $folder);

    $fileManager->addPHP('manager/painel/painel_manager_diagrama.php', $folder);
}

function initialize_diagramas($fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_Diagrama();
    $fileManager->setControllerManager($controller, $folder);
    $fileManager->addSecurityManager(new DiagramSecurity($folder));

    $fileManager->addPainelManager(new PainelManagerDiagrama());

    $fileManager->setTableManager(new TableManagerDiagrama(), $folder);
}

?>