<?

class ControllerManager_Construbook implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "fornecedor") {
            return $this->getFornecedorController();
        }
        if ($tableName == "produto") {
            return $this->getProdutoController();
        }
        if ($tableName == "tabela_calculo") {
            return $this->getTabelaCalculoController();
        }
        if ($tableName == "categoria_produto") {
            return $this->getCategoriaProdutoController();
        }
        if ($tableName == "media") {
            return $this->getMediaController();
        }
        if ($tableName == "produto_media") {
            return $this->getProdutoMediaController();
        }
        if ($tableName == "mural") {
            return $this->getMuralController();
        }
    }
    
    function getTabelaCalculoController() {
        $table = getTableManager()->getTabelaComNome("tabela_calculo");
        $controller = new TabelaCalculoController($table, "Tabelas", "Tabela", new LinkView("Nova Tabela", "fornecedores/tabela/novo"));
        return $controller;
    }
    function getCategoriaProdutoController() {
        $table = getTableManager()->getTabelaComNome("categoria_produto");
        $controller = new MuralController($table, "Categorias", "Categoria", new LinkView("Nova Categoria", "fornecedores/categorias/novo"));
        return $controller;
    }
    function getMuralController() {
        $table = getTableManager()->getTabelaComNome("mural");
        $controller = new MuralController($table, "Murais", "Mural", new LinkView("Novo Mural", "mural/novo"));
        return $controller;
    }
    function getProdutoMediaController() {
        $table = getTableManager()->getTabelaComNome("produto_media");
        $controller = new ProdutoMediaController($table, "Medias", "Media", new LinkView("Nova Media", "media/novo"));
        return $controller;
    }
    function getMediaController() {
        $table = getTableManager()->getTabelaComNome("media");
        $controller = new MediaController($table, "Medias", "Media", new LinkView("Nova Media", "media/novo"));
        return $controller;
    }

    function getFornecedorController() {
        $table = getTableManager()->getTabelaComNome("fornecedor");
        $controller = new FornecedorController($table, "Fornecedores", "Fornecedor", new LinkView("Novo Fornecedor", "fornecedor/novo"));
        return $controller;
    }

    function getProdutoController() {
        $table = getTableManager()->getTabelaComNome("produto");
        $controller = new ProdutoController($table, "Produtos", "Produto", new LinkView("Novo Produto", "produto/novo"));
        return $controller;
    }

}

?>