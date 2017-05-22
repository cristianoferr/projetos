<?

//WIDGETS
//id=idFornecedor
class WidgetManterCategorias extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("categoria_produto");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_CATEGORIAS_MANTER);
        $controller->loadRegistros("and categoria_produto.id_fornecedor=$id", $painel);
        
        $painel->setController($controller);
        
        $elPainel=$painel->generate();
        return $elPainel;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}
//id=idCategoria_produto
class WidgetManterCategoria extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("categoria_produto");
        $painel = getPainelManager()->getPainel(PAINEL_EDITAR_CATEGORIA);
        $controller->loadRegistros("and categoria_produto.id_categoria_produto=$id", $painel);
        $painel->setController($controller);
        $painel->setTitulo(translateKey("txt_editar_categoria"));
        $elPainel=$painel->generate();
        return $elPainel;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}
