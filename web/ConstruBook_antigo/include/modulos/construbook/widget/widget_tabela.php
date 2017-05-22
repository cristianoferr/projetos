<?

//WIDGETS
//id=idFornecedor
class WidgetManterTabelas extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("tabela_calculo");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_TABELAS_MANTER);
        $controller->loadRegistros("and tabela_calculo.id_fornecedor=$id", $painel);
        
        $painel->setController($controller);
        
        $elPainel=$painel->generate();
        return $elPainel;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}
//id=idtabela_calculo
class WidgetManterTabela extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("tabela_calculo");
        $painel = getPainelManager()->getPainel(PAINEL_EDITAR_TABELA);
        $controller->loadRegistros("and tabela_calculo.id_tabela_calculo=$id", $painel);
        $painel->setController($controller);
        $painel->setTitulo(translateKey("txt_editar_tabela_calculo"));
        $elPainel=$painel->generate();
        return $elPainel;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}
