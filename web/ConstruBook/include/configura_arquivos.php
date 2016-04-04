<?

include $ROOT_FOLDER . '/include/manager/file_manager.php';

define("ROOT_MODULO", $ROOT_FOLDER . "/include/modulos/");
//define("USE_MINIFIED", true);
define("USE_MINIFIED", false);

define("MODULO_DEFAULT", "default");
define("MODULO_ADMIN", "admin");
define("MODULO_MAIN_PAGE", "main_page");
define("MODULO_SITE_MANAGER", "siteManager");
define("MODULO_ABTEST", "abtesting");
define("MODULO_CONSTRUBOOK", "construbook");

function getFileManager() {
    if (isset($GLOBALS["file_manager"])) {
        return $GLOBALS["file_manager"];
    }
    $controller = new FileManager();
    $GLOBALS["file_manager"] = $controller;
    return $GLOBALS["file_manager"];
}

function adicionaModuloCarga($moduloName) {
    getFileManager()->adicionaModulo($moduloName);
}

function isModuloCarregado($moduloName) {
    return getFileManager()->isModuloCarregado($moduloName);
}

function defineArquivos() {
    adicionaModuloCarga(MODULO_DEFAULT);
    adicionaModuloCarga(MODULO_SITE_MANAGER);
    adicionaModuloCarga(MODULO_ABTEST);
    adicionaModuloCarga(MODULO_CONSTRUBOOK);
    
    if (isAdmin()) {
        adicionaModuloCarga(MODULO_ADMIN);
    }
}

function chamaLoaders(FileManager $fileManager) {
    $modulos = $fileManager->getModulosCarregados();
    for ($i = 0; $i < sizeOf($modulos); $i++) {
        $modulo = $modulos[$i];

        include(ROOT_MODULO . $modulo->getName() . "/loader.php");
        call_user_func("loader_" . $modulo->getName(), $fileManager, $modulo);
    }
}

function incluiPHP(FileManager $fileManager) {
    if (USE_MINIFIED) {
        include($ROOT_FOLDER . "/build/minified.php-inc");
    } else {
        $files = $fileManager->getPHP();
        foreach ($files as $file) {
            include(ROOT_MODULO . $file);
        }
    }
}

function inicializaModulos(FileManager $fileManager) {
    $modulos = $fileManager->getModulosCarregados();
    for ($i = 0; $i < sizeOf($modulos); $i++) {
        $modulo = $modulos[$i];

        call_user_func("initialize_" . $modulo->getName(), $fileManager, $modulo);
    }
}

function incluiArquivos() {

    $fileManager = getFileManager();
    chamaLoaders($fileManager);
    incluiPHP($fileManager);
    $widgetManager = new WidgetManager();
    getManager()->setWidgetManager($widgetManager);
    inicializaModulos($fileManager);
}

function runUnitTests() {
    $modulos = getFileManager()->getModulosCarregados();
    for ($i = 0; $i < sizeOf($modulos); $i++) {
        $modulo = $modulos[$i];
        //write("Carregando Teste unitário do módulo " . $modulo->getName());
        include(ROOT_MODULO . $modulo->getName() . "/tests.php");
        //  call_user_func("runTests_" . $modulo->getName());
    }
}

defineArquivos();
?>