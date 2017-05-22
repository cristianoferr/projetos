<?

//WIDGETS
//id=idFornecedor
class WidgetManterProdutos extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("produto");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_PRODUTOS_MANTER);
        $controller->loadRegistros("and produto.id_fornecedor=$id", $painel);
        $painel->setController($controller);
        
        $elPainel=$painel->generate();
        return $elPainel;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}
//id=idProduto
class WidgetManterProduto extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("produto");
        $painel = getPainelManager()->getPainel(PAINEL_EDITAR_PRODUTO);
        $controller->loadRegistros("and produto.id_produto=$id", $painel);
        $painel->setController($controller);
        $painel->setTitulo(translateKey("txt_editar_produto"));
        $elPainel=$painel->generate();
        return $elPainel;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}
