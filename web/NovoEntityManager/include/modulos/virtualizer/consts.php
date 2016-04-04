<?php

define("TILE_ORIENT_HORIZONTAL", 1);
define("TILE_ORIENT_VERTICAL", 2);

define("TILE_SIZE_SMALL", 1);
define("TILE_SIZE_MEDIUM", 2);
define("TILE_SIZE_BIG", 3);

function telaAtual() {
    return $_GET['tela'] . $_POST['tela'];
}
function widgetAtual() {
    return $_GET['widget'] . $_POST['widget'];
}

?>
