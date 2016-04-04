<?

function loader_abtesting($fileManager, $modulo) {
    $folder = $modulo->getName();

    $fileManager->addPHP('consts_abtest.php', $folder);
    $fileManager->addPHP('manager/controller_manager_abtest.php', $folder);
    $fileManager->addPHP('manager/table/table_manager_abtest.php', $folder);
    $fileManager->addPHP('manager/painel/painel_manager_abtest.php', $folder);
    $fileManager->addPHP('controller/controller_abtest.php', $folder);
    $fileManager->addPHP('controller/controller_feature_check.php', $folder);
    $fileManager->addPHP('controller/controller_abtest_variacao.php', $folder);
    $fileManager->addPHP('controller/controller_tipo_abtest.php', $folder);

    $fileManager->addPHP('manager/security.php', $folder);
}

function initialize_abtesting($fileManager, $modulo) {
    $folder = $modulo->getName();
    $controller = new ControllerManager_ABTest();
    $fileManager->setControllerManager($controller, $folder);

    $fileManager->addPainelManager(new PainelManagerABTest());

    $fileManager->setTableManager(new TableManagerABTest(), $folder);

    $fileManager->addSecurityManager(new ABTestSecurity($folder));
}

function abtest() {
    return getControllerForTable("abtest");
}

function isAbtestAtivo($abtest, $codinterno) {
    $controller = getControllerForTable("abtest");
    return $controller->isAbtestAtivo($abtest, $codinterno);
}

//aumenta o hit count do abtest... unico para cada usuário
function incAbtest($abtest) {
    $controller = getControllerForTable("abtest");
    return $controller->incAbtest($abtest);
}

function abTestPageReached($id_pagina) {
    $controller = getControllerForTable("abtest");
    $controller->abTestPageReached($id_pagina);
}

function abtestAtual() {
    return $_GET['abtest'];
}

?>