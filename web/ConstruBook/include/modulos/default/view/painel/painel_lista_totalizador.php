<?

//Usado para listar registros em lista com um numero à direita (artigos com quantidade de registro, por ex)
class ListaTotalizadorPainel extends BasePainel {

    private $linkBase;

    function setLinkBase($v) {
        $this->linkBase = $v;
    }

    function cabecalho() {
        BasePainel::cabecalho();
        echo "<div class=\"lista \">";
        echo "<ul>";
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();
        $colunaTot = $colunas[2];

        echo "<li>";
        $texto = $model->getDescricao();

        $texto = "$texto <span>" . $model->getValor($colunaTot) . "</span>";
        Out::link($this->linkBase . $model->getId(), $texto);
        echo "</li>";
    }

    function rodape(ElementMaker $elRoot) {
        ?></ul></div><?
        BasePainel::rodape();
    }

}
?>