<?php
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
inicializa();

$term = strip_tags(substr($_POST['searchit'], 0, 100));
$modulo = strip_tags(substr($_GET['modulo'], 0, 100));
$fieldname = strip_tags(substr($_GET['fieldname'], 0, 100));
$dbfield = strip_tags(substr($_GET['dbfield'], 0, 100));
$novo = strip_tags(substr($_GET['novo'], 0, 100));
$term = mysql_escape_string($term);

//echo $_GET['modulo']."-".$_POST['modulo'];
if ($modulo == "usuario") {
    $table = "usuario";
    if (usuarioAtual() != "") {
        if ($novo == "") {
            $filtro = " and id_usuario<>" . usuarioAtual();
        }
    }
}

if ($modulo == "projeto") {
    $table = "projeto";
}

if ($dbfield == "nm_usuario_novo") {
    $field = "nm_usuario";
    $erro = ERROR_ID_EXISTS;
}

if ($dbfield == "cod_projeto_novo") {
    $field = "cod_projeto";
    $erro = ERROR_ID_EXISTS;
}

if ($dbfield == "email_usuario_novo") {
    $field = "email_usuario";
    $erro = ERROR_EMAIL_EXISTS;
}


if (($table) && ($term != "")) {
    //echo "table: $table $modulo !";
    $controller = getControllerManager()->getControllerForTable($table);
    $saida = $controller->checkIfValueExists($field, $term, $filtro);
    if (!$saida) {
        $erro = "";
    } else {
        ?><div class="alert alert-danger"><? echo $erro; ?></div><? }
    ?><script>

        formStatus['<? echo $fieldname; ?>'] = '<? echo $erro; ?>';
        //alert('<? echo $saida; ?>');
        atualizaFormStatus();
    </script><?
}
?>