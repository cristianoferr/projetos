<?

define("WIDGET_BUSCA_FORNECEDOR", 'WIDGET_BUSCA_FORNECEDOR');
define("WIDGET_LISTA_PRODUTOS_FORNECEDOR", 'WIDGET_LISTA_PRODUTOS_FORNECEDOR');
define("WIDGET_VISUALIZA_FORNECEDOR", 'WIDGET_VISUALIZA_FORNECEDOR');
define("WIDGET_VISUALIZA_MURAL_FORNECEDOR", 'WIDGET_VISUALIZA_MURAL_FORNECEDOR');
define("WIDGET_VISUALIZA_PARCEIROS_FORNECEDOR", 'WIDGET_VISUALIZA_PARCEIROS_FORNECEDOR');
define("WIDGET_MANTER_PRODUTOS", 'WIDGET_MANTER_PRODUTOS');
define("WIDGET_MANTER_PRODUTO", 'WIDGET_MANTER_PRODUTO');
define("WIDGET_MANTER_CATEGORIAS", 'WIDGET_MANTER_CATEGORIAS');
define("WIDGET_MANTER_CATEGORIA", 'WIDGET_MANTER_CATEGORIA');

class WidgetConstrubook extends WidgetBase {

    function getWidgetsArray() {
        return array(WIDGET_BUSCA_FORNECEDOR => null, WIDGET_LISTA_PRODUTOS_FORNECEDOR => FORNECEDOR_TESTE);
    }

    function getWidgetFor($nome) {
        if ($nome == WIDGET_BUSCA_FORNECEDOR) {
            return new WidgetBuscaFornecedor();
        }
        if ($nome == WIDGET_LISTA_PRODUTOS_FORNECEDOR) {
            return new WidgetListaProdutosFornecedor();
        }

        if ($nome == WIDGET_VISUALIZA_FORNECEDOR) {
            return new WidgetVisualizaFornecedor();
        }
        if ($nome == WIDGET_VISUALIZA_MURAL_FORNECEDOR) {
            return new WidgetVisualizaMuralFornecedor();
        }
        if ($nome == WIDGET_VISUALIZA_PARCEIROS_FORNECEDOR) {
            return new WidgetVisualizaParceirosFornecedor();
        }
        if ($nome == WIDGET_MANTER_PRODUTOS) {
            return new WidgetManterProdutos();
        }
        if ($nome == WIDGET_MANTER_PRODUTO) {
            return new WidgetManterProduto();
        }
        if ($nome == WIDGET_MANTER_CATEGORIAS) {
            return new WidgetManterCategorias();
        }
        if ($nome == WIDGET_MANTER_CATEGORIA) {
            return new WidgetManterCategoria();
        }
        if ($nome == WIDGET_MANTER_TABELAS) {
            return new WidgetManterTabelas();
        }
        if ($nome == WIDGET_MANTER_TABELA) {
            return new WidgetManterTabela();
        }
    }

}

//WIDGETS
//id=id_fornecedor
class WidgetVisualizaParceirosFornecedor extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("fornecedor");
        $painel = getPainelManager()->getPainel(PAINEL_VISUALIZAR_FORNECEDORES);
        $model = $controller->listaParceirosDoFornecedor($id, $painel);
        $painel->setController($controller);
        
        $elDiv=criaEl("div");
        $elP=criaEl("p",$elDiv)->setClass("titulo_area")->setValue(translateKey("txt_parceiros"));
        $elPainel=$painel->generate();
        $elDiv->addElement($elPainel);
        return $elDiv;
    }

    function getTitle() {
        return translateKey("txt_fornecedor");
    }

}

//id=id_fornecedor
class WidgetVisualizaFornecedor extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);

        $controller = getControllerForTable("fornecedor");
        $painel = getPainelManager()->getPainel(PAINEL_VISUALIZA_FORNECEDOR);
        $model = $controller->loadSingle($id, $painel);
        $painel->setTitulo($model->getDescricao());
        $painel->setController($controller);
        $painel->registro($model);
       // $painel->mostra();

        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_fornecedor");
    }

}

//id=id_fornecedor
class WidgetListaProdutosFornecedor extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);
        $controller = getControllerForTable("produto");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_PRODUTOS);
        $controller->listaProdutosDoFornecedor($id, $painel);
        $painel->setController($controller);
        
        $elDiv=criaEl("div");
        $elP=criaEl("p",$elDiv)->setClass("titulo_area")->setValue(translateKey("txt_produtos"));
        $elPainel=$painel->generate();
        $elDiv->addElement($elPainel);
        return $elDiv;
    }

    function getTitle() {
        return translateKey("txt_produtos");
    }

}

class WidgetBuscaFornecedor extends WidgetConstrubook {

    function generate($id) {
        $id = validaNumero($id);
        $projetoController = getControllerForTable("projeto");
        $painelProjetos = getPainelManager()->getPainel(PAINEL_PROJETOS_USUARIO);
        $projetoController->loadRegistros("and produto.id_projeto in (select id_projeto from usuario_projeto where id_usuario=$id)", $painelProjetos);
        $painelProjetos->setController($projetoController);
        return $painelProjetos->generate();
    }

    function getTitle() {
        return translateKey("txt_user_projects");
    }

}
