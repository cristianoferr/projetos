<?

function loader_knowledge($fileManager, $modulo) {
    $folder = $modulo->getName();
    //$fileManager->addPHP('consts_site.php',$folder);
    $fileManager->addPHP('consts_knowledge.php', $folder);
    $fileManager->addPHP('manager/table_manager_knowledge.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_artigo.php', $folder);
    $fileManager->addPHP('manager/controller_manager_knowledge.php', $folder);
    $fileManager->addPHP('controller/controller_artigo.php', $folder);

    $fileManager->addPHP('manager/security.php', $folder);

    $fileManager->addPHP('widget/widget_knowledge.php', $folder);
    $fileManager->addPHP('widget/widget_mostra_artigo.php', $folder);
    $fileManager->addPHP('widget/widget_edit_artigo.php', $folder);
    $fileManager->addPHP('widget/widget_new_artigo.php', $folder);
    $fileManager->addPHP('widget/widget_view_artigo.php', $folder);
    $fileManager->addPHP('widget/widget_lista_artigos.php', $folder);
    $fileManager->addPHP('widget/widget_lista_artigos_pagina.php', $folder);

    $fileManager->addPHP('view/painel_artigos.php', $folder);
}

function initialize_knowledge($fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_Knowledge();
    $fileManager->setControllerManager($controller, $folder);

    $fileManager->setTableManager(new TableManagerKnowledge(), $folder);
    $fileManager->addSecurityManager(new KnowledgeSecurity($folder));


    getWidgetManager()->addManager(new WidgetKnowledge());
    $fileManager->addPainelManager(new PainelManagerArtigo());

    /* $controller=new ControllerManager_Tarefa();
      $fileManager->setControllerManager($controller,MODULO_TAREFAS);

      getWidgetManager()->addManager(new WidgetTarefa());


      $fileManager->addPainelManager(new PainelManagerEntregavel());

     */
}

s
?>