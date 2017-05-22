<?

function loader_main_page($fileManager, $modulo) {
    $folder = $modulo->getName();
    $fileManager->addCSS("css/" . $folder . "/main_page.css", $folder);
    $fileManager->addPHP('main_page_assist.php', $folder);
}

function initialize_main_page($fileManager, $modulo) {
    
}

?>