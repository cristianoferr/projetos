<?

function loader_tarefa($fileManager, $modulo) {
    $folder = $modulo->getName();
    $fileManager->addCSS("css/" . $folder . "/estilos_tarefas.css", $folder);

    $fileManager->addPHP('consts_tarefa.php', $folder);

    $fileManager->addPHP('widget/widget_tarefa.php', $folder);
    $fileManager->addPHP('widget/widget_edit_tarefa.php', $folder);
    $fileManager->addPHP('widget/widget_prereq_tarefa.php', $folder);
    $fileManager->addPHP('widget/widget_prereqs_tarefa.php', $folder);
    $fileManager->addPHP('widget/widget_edit_entregavel.php', $folder);
    $fileManager->addPHP('widget/widget_entregaveis_projeto.php', $folder);
    $fileManager->addPHP('widget/widget_open_tasks.php', $folder);
    $fileManager->addPHP('widget/widget_lista_tarefas.php', $folder);
    $fileManager->addPHP('widget/widget_select_deliverable.php', $folder);

    $fileManager->addPHP('view/painel_entregavel.php', $folder);

    $fileManager->addPHP('manager/table_manager_tarefa.php', $folder);
    $fileManager->addPHP('manager/controller_manager_tarefa.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_tarefa.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_entregavel.php', $folder);

    $fileManager->addPHP('controller/controller_prereq_tarefa.php', $folder);
    $fileManager->addPHP('controller/controller_status_tarefa.php', $folder);
    $fileManager->addPHP('controller/controller_urgencia.php', $folder);
    $fileManager->addPHP('controller/controller_complexidade.php', $folder);
    $fileManager->addPHP('controller/controller_area_tarefa.php', $folder);
    $fileManager->addPHP('controller/controller_tarefa.php', $folder);
    $fileManager->addPHP('controller/controller_entregavel.php', $folder);
    $fileManager->addPHP('controller/controller_status_entregavel.php', $folder);

    $fileManager->addPHP('manager/security_tarefa.php', $folder);
}

function initialize_tarefa(FileManager $fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_Tarefa();
    $fileManager->setControllerManager($controller, $folder);

    getWidgetManager()->addManager(new WidgetTarefa());

    $fileManager->addPainelManager(new PainelManagerTarefa());
    $fileManager->addPainelManager(new PainelManagerEntregavel());
    $fileManager->addSecurityManager(new TarefaSecurity($folder));

    $fileManager->setTableManager(new TableManagerTarefa(), $folder);
}

?>