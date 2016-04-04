<?

function loader_entityManager($fileManager, $modulo) {//coberto
    $folder = $modulo->getName();
    $fileManager->addCSS("css/" . $folder . "/projetos.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/toggle.css", $folder);



    $fileManager->addPHP('consts_entidade.php', $folder);
    $fileManager->addPHP('controller/controller_metodologia.php', $folder);

    $fileManager->addPHP('manager/controller_manager_entity.php', $folder);

    $fileManager->addPHP('widget/widget_entidade.php', $folder);
    $fileManager->addPHP('widget/widget_edit_coluna.php', $folder);
    $fileManager->addPHP('widget/widget_edit_funcao.php', $folder);
    $fileManager->addPHP('widget/widget_edit_entidade.php', $folder);
    $fileManager->addPHP('widget/widget_edit_datatype.php', $folder);
    $fileManager->addPHP('widget/widget_membros_projeto.php', $folder);
    $fileManager->addPHP('widget/widget_entidades_projeto.php', $folder);
    $fileManager->addPHP('widget/widget_entidades.php', $folder);
    $fileManager->addPHP('widget/widget_colunas_entidade.php', $folder);
    $fileManager->addPHP('widget/widget_funcoes_entidade.php', $folder);
    $fileManager->addPHP('widget/widget_visualiza_entidade.php', $folder);

    $fileManager->addPHP('model/import/table_import.php', $folder);
    $fileManager->addPHP('model/import/import_parser.php', $folder);

    $fileManager->addPHP('manager/table_manager_entidades.php', $folder);

    $fileManager->addPHP('controller/controller_coluna.php', $folder);
    //$fileManager->addPHP('controller/controller_camada_projeto.php', $folder);
    $fileManager->addPHP('controller/controller_projeto.php', $folder);
    $fileManager->addPHP('controller/controller_entidade.php', $folder);
    $fileManager->addPHP('controller/controller_funcao.php', $folder);
    $fileManager->addPHP('controller/controller_camada.php', $folder);
    $fileManager->addPHP('controller/controller_linguagem.php', $folder);
    $fileManager->addPHP('controller/controller_membro_projeto.php', $folder);
    $fileManager->addPHP('controller/controller_relacao_datatype.php', $folder);
    $fileManager->addPHP('controller/controller_meta_type.php', $folder);
    $fileManager->addPHP('controller/controller_parametro_funcao.php', $folder);
    $fileManager->addPHP('controller/controller_primitive_type.php', $folder);
    $fileManager->addPHP('controller/controller_primitive_type_param.php', $folder);
    $fileManager->addPHP('controller/controller_datatype.php', $folder);
    $fileManager->addPHP('controller/controller_datatype_param.php', $folder);
    $fileManager->addPHP('controller/controller_sql_coluna.php', $folder);
    $fileManager->addPHP('controller/controller_acesso_coluna.php', $folder);
    $fileManager->addPHP('controller/controller_tipo_banco.php', $folder);
    $fileManager->addPHP('controller/controller_visibilidade_projeto.php', $folder);
    $fileManager->addPHP('controller/controller_categoria_projeto.php', $folder);
    $fileManager->addPHP('controller/controller_valor.php', $folder);
    $fileManager->addPHP('controller/controller_registro.php', $folder);

    $fileManager->addPHP('view/uml/painel_entity.php', $folder);




    $fileManager->addPHP('manager/security.php', $folder);

    $fileManager->addPHP('manager/painel/painel_manager_camada.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_coluna.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_datatype.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_entidade.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_funcao.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_parametro_funcao.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_projeto.php', $folder);
}

function initialize_entityManager($fileManager, $modulo) {//coberto
    $folder = $modulo->getName();
    $controller = new ControllerManager_EntityManager();
    $fileManager->setControllerManager($controller, $folder);

    getWidgetManager()->addManager(new WidgetEntidade());
    $fileManager->addSecurityManager(new EntityManagerSecurity($folder));

    $fileManager->addPainelManager(new PainelManagerCamada());
    $fileManager->addPainelManager(new PainelManagerColuna());
    $fileManager->addPainelManager(new PainelManagerDatatype());
    $fileManager->addPainelManager(new PainelManagerEntidade());
    $fileManager->addPainelManager(new PainelManagerFuncao());
    $fileManager->addPainelManager(new PainelManagerParametroFuncao());
    $fileManager->addPainelManager(new PainelManagerProjeto());

    $fileManager->setTableManager(new TableManagerEntidades(), $folder);
}

?>