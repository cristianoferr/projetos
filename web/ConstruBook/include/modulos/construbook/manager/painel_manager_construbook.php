<?

define("PAINEL_VISUALIZA_FORNECEDOR", 'PAINEL_VISUALIZA_FORNECEDOR');
define("PAINEL_LISTA_FORNECEDORES", 'PAINEL_LISTA_FORNECEDORES');
define("PAINEL_VISUALIZA_MURAL", 'PAINEL_VISUALIZA_MURAL');
define("PAINEL_LISTA_MURAL", 'PAINEL_LISTA_MURAL');
define("PAINEL_LISTA_PRODUTOS", 'PAINEL_LISTA_PRODUTOS');
define("PAINEL_LISTA_PRODUTOS_MANTER", 'PAINEL_LISTA_PRODUTOS_MANTER');
define("PAINEL_VISUALIZAR_FORNECEDORES", 'PAINEL_VISUALIZAR_FORNECEDORES');
define("PAINEL_EDITAR_PRODUTO ", 'PAINEL_EDITAR_PRODUTO ');
define("PAINEL_EDITAR_CATEGORIA ", 'PAINEL_EDITAR_CATEGORIA ');
define("PAINEL_LISTA_CATEGORIAS_MANTER ", 'PAINEL_LISTA_CATEGORIAS_MANTER ');

class PainelManagerConstrubook {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_VISUALIZA_FORNECEDOR) {
            return $this->getPainelVisualizaFornecedor();
        }
        if ($nome == PAINEL_LISTA_FORNECEDORES) {
            return $this->getPainelListaFornecedores();
        }
        if ($nome == PAINEL_LISTA_MURAL) {
            return $this->getPainelListaMural();
        }
        if ($nome == PAINEL_LISTA_PRODUTOS) {
            return $this->getPainelListaProdutos();
        }
        if ($nome == PAINEL_LISTA_PRODUTOS_MANTER) {
            return $this->getPainelListaProdutosManter();
        }
        if ($nome == PAINEL_VISUALIZAR_FORNECEDORES) {
            return $this->getPainelVisualizaFornecedores();
        }
        if ($nome == PAINEL_EDITAR_PRODUTO ) {
            return $this->getPainelEditarProduto();
        }
        
        //Categoria
        if ($nome == PAINEL_EDITAR_CATEGORIA ) {
            return $this->getPainelEditarCategoria();
        }
        if ($nome == PAINEL_LISTA_CATEGORIAS_MANTER) {
            return $this->getPainelListaCategoriasManter();
        }
        //Tabela de Calculo
        if ($nome == PAINEL_EDITAR_TABELA ) {
            return $this->getPainelEditarTabela();
        }
        if ($nome == PAINEL_LISTA_TABELAS_MANTER) {
            return $this->getPainelListaTabelasManter();
        }
        
        
       
    }
    
    //Tabela de calculo
 function getPainelEditarTabela() {
         
        $table = getTableManager()->getTabelaComNome("tabela_calculo");
        $painel = new PainelVertical("pet", $table);
        
        $painel->addColunaWithDBName("id_tabela_calculo");
        $painel->addColunaWithDBName("nm_tabela_calculo");
        $painel->addColunaWithDBName("id_fornecedor")->setInvisible();

        $painel->modoEdicao();
        $painel->ativaAjax();

        return $painel;
    }
    
    function getPainelListaTabelasManter() {
        $table = getTableManager()->getTabelaComNome("tabela_calculo");
        $painel = new PainelHorizontal("plcm", $table);
        $painel->addColunaWithDBName("id_tabela_calculo");
        $painel->addColunaWithDBName("nm_tabela_calculo");

        $painel->modoEdicao();

        $painel->setEditLink("fornecedor/tabela/");
        $painel->setDeleteLink("fornecedor/tabela/remover/");
        return $painel;
    }
    
    //Categoria Produto
    function getPainelEditarCategoria() {
         
        $table = getTableManager()->getTabelaComNome("categoria_produto");
        $painel = new PainelVertical("pec", $table);
        
        $painel->addColunaWithDBName("id_categoria_produto");
        $painel->addColunaWithDBName("nm_categoria_produto");
        $painel->addColunaWithDBName("id_fornecedor")->setInvisible();

        $painel->modoEdicao();
        $painel->ativaAjax();

        return $painel;
    }
    
    function getPainelListaCategoriasManter() {
        $table = getTableManager()->getTabelaComNome("categoria_produto");
        $painel = new PainelHorizontal("plcm", $table);
        $painel->addColunaWithDBName("id_categoria_produto");
        $painel->addColunaWithDBName("nm_categoria_produto");

        $painel->modoEdicao();

        $painel->setEditLink("fornecedor/categorias/");
        $painel->setDeleteLink("fornecedor/categorias/remover/");
        return $painel;
    }

    //Fornecedor
    function getPainelVisualizaFornecedores() {
        $table = getTableManager()->getTabelaComNome("fornecedor");
        $painel = new PainelListaFornecedor("pvf", $table);
        $painel->addColunaWithDBName("id_fornecedor");
        $painel->addColunaWithDBName("nm_fornecedor");
        return $painel;
    }

    function getPainelVisualizaFornecedor() {
        $table = getTableManager()->getTabelaComNome("fornecedor");
        $painel = new PainelFornecedor("pvf", $table);

        return $painel;
    }

    function getPainelListaFornecedores() {
        $table = getTableManager()->getTabelaComNome("fornecedor");
        $painel = new PainelListaFornecedor("plf", $table);
        $painel->addColunaWithDBName("id_fornecedor");
        $painel->addColunaWithDBName("nm_fornecedor");

        $painel->modoVisualizacao();

        $painel->setEditLink("fornecedores/");
        return $painel;
    }
    
     function getPainelListaProdutosManter() {
        $table = getTableManager()->getTabelaComNome("produto");
        $painel = new PainelHorizontal("plpm", $table);
        $painel->addColunaWithDBName("id_produto");
        $painel->addColunaWithDBName("nm_categoria_produto");
        $painel->addColunaWithDBName("nm_produto");
        $painel->addColunaWithDBName("desc_produto");
        $painel->addColunaWithDBName("id_fornecedor");
        $painel->addColunaWithDBName("vlr_unitario");

        $painel->modoEdicao();

        $painel->setEditLink("fornecedor/produtos/");
        $painel->setDeleteLink("fornecedor/produtos/remover/");
        return $painel;
    }
     function getPainelEditarProduto() {
         
        $table = getTableManager()->getTabelaComNome("produto");
        $painel = new PainelVertical("plps", $table);
        
        $painel->addColunaWithDBName("id_produto");
        $painel->addColunaWithDBName("id_categoria_produto");
        $painel->addColunaWithDBName("id_tabela_calculo");
        $painel->addColunaWithDBName("nm_produto");
        $painel->addColunaWithDBName("desc_produto");
        $painel->addColunaWithDBName("id_fornecedor")->setInvisible();
        $painel->addColunaWithDBName("vlr_unitario");

        $painel->modoEdicao();
        $painel->ativaAjax();


        return $painel;
    }
     function getPainelListaProdutos() {
        $table = getTableManager()->getTabelaComNome("produto");
        $painel = new PainelListaProduto("plps", $table);
        $painel->addColunaWithDBName("id_produto");
        $painel->addColunaWithDBName("id_categoria_produto");
        $painel->addColunaWithDBName("nm_produto");
        $painel->addColunaWithDBName("id_fornecedor");
        $painel->addColunaWithDBName("nm_fornecedor");
        $painel->addColunaWithDBName("vlr_unitario");

        $painel->modoVisualizacao();

        return $painel;
    }
    
    function getPainelListaMural() {
        $table = getTableManager()->getTabelaComNome("mural");
        $painel = new PainelListaMural("plm", $table);
        $painel->addColunaWithDBName("id_mural");
        $painel->addColunaWithDBName("texto_mural");
        $painel->addColunaWithDBName("id_tipo_mural");
        $painel->addColunaWithDBName("id_fornecedor");

        $painel->modoVisualizacao();

        return $painel;
    }

}

?>