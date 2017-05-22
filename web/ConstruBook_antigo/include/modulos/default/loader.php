<?

function loader_default($fileManager, $modulo) {
    $folder = $modulo->getName();

    $fileManager->addCSS("css/" . $folder . "/bootstrap.min.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/bootstrap-theme.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/barometer.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/estilos_table.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/estilos.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/estilos_inputs.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/menu.css", $folder);
    $fileManager->addCSS("css/" . $folder . "/jquery-ui.css", $folder);

    $fileManager->addJS("js/" . $folder . "/jquery-2.0.3.min.js", $folder);
    $fileManager->addJS("js/" . $folder . "/jquery-1.10.2-ui.min.js", $folder);
    $fileManager->addJS("js/" . $folder . "/bootstrap.min.js", $folder);
    $fileManager->addJS("js/" . $folder . "/barometer.js", $folder);
    $fileManager->addJS("js/" . $folder . "/jquery.top_menu.js", $folder);
    $fileManager->addJS("js/" . $folder . "/scripts.js", MODULO_ENTITY_MANAGER);
    $fileManager->addJS("js/" . $folder . "/menu.js", $folder);
    $fileManager->addJS("js/" . $folder . "/seletor.js", $folder);
    $fileManager->addJS("js/" . $folder . "/scripts_ajax.js", $folder);
    $fileManager->addJS("js/" . $folder . "/scripts_jquery.js", $folder);
    $fileManager->addJS("js/" . $folder . "/jquery.maskMoney.js", $folder);

    $fileManager->addPHP('manager/widget/widget_base.php', $folder);
    $fileManager->addPHP('manager/widget/widget_manager.php', $folder);
    $fileManager->addPHP('consts_default.php', $folder);

    $fileManager->addPHP('utils/seguranca_base.php', $folder);
    $fileManager->addPHP('manager/table/table_manager.php', $folder);
    $fileManager->addPHP('manager/table/table_manager_default.php', $folder);
    $fileManager->addPHP('manager/controller/interface_controller_manager.php', $folder);
    $fileManager->addPHP('manager/controller_manager_default.php', $folder);
    $fileManager->addPHP('manager/base_manager.php', $folder);
    $fileManager->addPHP('manager/controller/controller_manager.php', $folder);
    $fileManager->addPHP('manager/manager.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager.php', $folder);

    $fileManager->addPHP('controller/controller_abstract.php', $folder);
    $fileManager->addPHP('controller/controller_base.php', $folder);
    $fileManager->addPHP('controller/dicionarios.php', $folder);

    $fileManager->addPHP('view/interface_view.php', $folder);
    $fileManager->addPHP('view/view_link.php', $folder);
    $fileManager->addPHP('view/painel/painel_base_aba.php', $folder);
    $fileManager->addPHP('view/painel/painel_base_link.php', $folder);
    $fileManager->addPHP('view/painel/painel_base_sql.php', $folder);

    $fileManager->addPHP('view/inputs/input_data.php', $folder);
    $fileManager->addPHP('view/inputs/input_prop.php', $folder);
    $fileManager->addPHP('view/inputs/input_generator.php', $folder);
    $fileManager->addPHP('view/inputs/input_business.php', $folder);

    $fileManager->addPHP('view/view_base.php', $folder);
    $fileManager->addPHP('view/painel/painel_modal.php', $folder);
    $fileManager->addPHP('view/view_dependencia.php', $folder);
    $fileManager->addPHP('view/view_aba.php', $folder);
    $fileManager->addPHP('view/view_menu.php', $folder);
    $fileManager->addPHP('view/view_breadcrumb.php', $folder);
    $fileManager->addPHP('view/element_maker.php', $folder);

    $fileManager->addPHP('view/painel/painel_base.php', $folder);
    $fileManager->addPHP('view/painel/painel_form.php', $folder);
    $fileManager->addPHP('view/painel/painel_vertical.php', $folder);
    $fileManager->addPHP('view/painel/painel_horizontal.php', $folder);
    $fileManager->addPHP('view/painel/painel_horizontal_mini.php', $folder);
    $fileManager->addPHP('view/painel/painel_resumo.php', $folder);
    $fileManager->addPHP('view/painel/painel_mestre_detalhe.php', $folder);
    $fileManager->addPHP('view/painel/painel_card.php', $folder);
    $fileManager->addPHP('view/painel/painel_lista_totalizador.php', $folder);
    $fileManager->addPHP('view/painel/painel_menu.php', $folder);
    $fileManager->addPHP('utils/html_output.php', $folder);
    $fileManager->addPHP('utils/IOUtils.php', $folder);
    $fileManager->addPHP('utils/include_validadores.php', $folder);
    $fileManager->addPHP('include_seguranca.php', $folder);
    $fileManager->addPHP('model/tabela_abstract.php', $folder);
    $fileManager->addPHP('model/tabela_model.php', $folder);
    $fileManager->addPHP('model/condicao.php', $folder);
    $fileManager->addPHP('model/model_abstract.php', $folder);
    $fileManager->addPHP('model/model_base.php', $folder);
    $fileManager->addPHP('model/coluna_abstract.php', $folder);
    $fileManager->addPHP('model/coluna_base.php', $folder);
}

function initialize_default($fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_Default();
    $fileManager->setControllerManager($controller, $folder);

    $fileManager->addPainelManager(new PainelManagerUsuario());

    $fileManager->setTableManager(new TableManagerDefault(), $folder);
}

?>