<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
$modulo = $_GET['modulo'];
$full = $_GET['full'];
$redir = $_GET['redir'];
if ($modulo) {
    adicionaModuloCarga($modulo);
}
inicializa();
if ($full == "true") {
    escreveHeader();
}

$widgetName = $_GET['widget'];

if (!$widgetName) {
    writeErro("NO WIDGET DEFINED!!", $widgetName . "--" . $modulo);
}

$id = $_GET['id'];

$widget = getWidgetManager()->getWidgetFor($widgetName);
if ($full != "true") {
    $widget->setModal();
}
$widget->show($id);

if ($redir) {
    redirect(unWebify($redir));
}

if ($full == "true") {
    escreveFooter();
}
?>