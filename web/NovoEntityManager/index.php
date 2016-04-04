<?
/*foreach ($_SERVER as $key => $value) 
    { 
    echo $key."=".$value."<br>";
}*/
include 'include/include.php';

adicionaModuloCarga(MODULO_MAIN_PAGE);

inicializa();
definePaginaAtual(PAGINA_PRINCIPAL);
//writeAdmin("Elemento:",getControllerForTable("elemento")->idForPage(getIDPaginaAtual()));

escreveHeader();


$assist = new MainPageAssist();

$elDivContainer = elMaker("div")->setClass("container marketing");
$elDivContainer->addElement($assist->criaJumboTron());

$eldivSubcollumns = elMaker("div", $elDivContainer)->setClass("row");
$elSub = $assist->listSubColumns(3, $eldivSubcollumns);
$elDivContainer->mostra();

escreveFooter();
?>