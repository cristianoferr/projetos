<?

interface ISimplePainel {

    function setController(IController $controller);

    function mostra();

    function rodape(ElementMaker $elRoot);

    function getController();

    function registro(IModel $model);

    function generate();
}

interface IPainel extends ISimplePainel {

    function getColunas();

    function addColunaWithDBName($dbname, $aba = null);

    function limpaColunas();

    function getSQLColunas($pkName);

    function getFromSQL($pkName);

    function getWhereSQL($pkName);

    function getOrderbySQL();

    function defineWidgetForAction($acao, $widgetName);

    function getWidgetForAction($acao);

    function adicionaAba($id, $texto, $visible = true);

    function getAbas();
}

?>