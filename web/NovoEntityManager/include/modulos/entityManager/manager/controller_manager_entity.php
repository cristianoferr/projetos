<?

class ControllerManager_EntityManager implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "entidade") {
            return $this->getEntidadeController();
        }
        if ($tableName == "projeto") {
            return $this->getProjetoController();
        }
        if ($tableName == "camada") {
            return $this->getCamadaController();
        }

        if ($tableName == "valor") {
            return $this->getValorController();
        }
        if ($tableName == "relacao_datatype") {
            return $this->getRelacaoDatatypeController();
        }
        if ($tableName == "usuario_projeto") {
            return $this->getUsuarioProjetoController();
        }
        if ($tableName == "parametro_funcao") {
            return $this->getFuncaoParametroController();
        }



        if ($tableName == "datatype") {
            return $this->getDatatypeController();
        }
        if ($tableName == "datatype_param") {
            return $this->getDatatypeParamController();
        }

        if ($tableName == "meta_type") {
            return $this->getMetaTypeController();
        }

        if ($tableName == "linguagem") {
            return $this->getLinguagemController();
        }

        if ($tableName == "funcao") {
            return $this->getFuncaoController();
        }

        if ($tableName == "papel") {
            return $this->getPapelController();
        }
        if ($tableName == "coluna") {
            return $this->getColunaController();
        }
        if ($tableName == "registro") {
            return $this->getRegistroController();
        }

        if ($tableName == "primitive_type") {
            return $this->getPrimitiveTypeController();
        }
        if ($tableName == "primitive_type_param") {
            return $this->getPrimitiveTypeParamController();
        }

        if ($tableName == "sql_coluna") {
            return $this->getSqlColunaController();
        }

        if ($tableName == "acesso_coluna") {
            return $this->getAcessoColunaController();
        }

        if ($tableName == "tipo_banco") {
            return $this->getTipoBancoController();
        }
        if ($tableName == "metodologia") {
            return $this->getMetodologiaController();
        }

        if ($tableName == "visibilidade_projeto") {
            return $this->getVisibilidadeProjetoController();
        }

        if ($tableName == "categoria_projeto") {
            return $this->getCategoriaProjetoController();
        }
    }

    function getValorController() {
        $table = getTableManager()->getTabelaComNome("valor");
        $controller = new ValorController($table, "txt_entity_contents", "txt_entity_contents", new LinkKey("txt_entity_contents", "view/new/<projeto>/<entidade>"));
        return $controller;
    }

    function getMetaTypeController() {
        $table = getTableManager()->getTabelaComNome("meta_type");
        $controller = new MetaTypeController($table, "txt_meta_types", "txt_meta_type");
        return $controller;
    }

    function getDatatypeParamController() {
        $table = getTableManager()->getTabelaComNome("datatype_param");
        $controller = new DataTypeParamController($table, "txt_datatype_parameters", "txt_datatype_parameters");
        return $controller;
    }

    function getCategoriaProjetoController() {
        $table = getTableManager()->getTabelaComNome("categoria_projeto");
        $controller = new CategoriaProjetoController($table, "txt_categorias_projeto", "txt_categoria_projeto");
        return $controller;
    }

    function getVisibilidadeProjetoController() {
        $table = getTableManager()->getTabelaComNome("visibilidade_projeto");
        $controller = new VisibilidadeProjetoController($table, "txt_visibility_property", "txt_visibility_property");
        return $controller;
    }

    function getColunaController() {
        $table = getTableManager()->getTabelaComNome("coluna");
        $controller = new ColunaController($table, "txt_properties", "txt_property", new LinkKey("txt_new_property", "property/new/<projeto>/<entidade>"));
        return $controller;
    }

    function getFuncaoController() {
        $table = getTableManager()->getTabelaComNome("funcao");
        $controller = new FuncaoController($table, "txt_functions", "txt_function", new LinkKey("txt_new_function", "function/new/<projeto>/<entidade>"));
        return $controller;
    }

    function getLinguagemController() {
        $table = getTableManager()->getTabelaComNome("linguagem");
        $controller = new LinguagemController($table, "txt_languages", "txt_language");
        return $controller;
    }

    function getPapelController() {
        $table = getTableManager()->getTabelaComNome("papel");
        $controller = new PapelController($table, "txt_roles", "txt_role");
        return $controller;
    }

    function getCamadaController() {
        $table = getTableManager()->getTabelaComNome("camada");
        $controller = new CamadaController($table, "txt_layers", "txt_layer");
        return $controller;
    }

    function getEntidadeController() {
        $table = getTableManager()->getTabelaComNome("entidade");
        $controller = new EntidadeController($table, "txt_entities", "txt_entity", new LinkKey("txt_new_entity", "entity/new/<projeto>"));
        return $controller;
    }

    function getAcessoColunaController() {
        $table = getTableManager()->getTabelaComNome("acesso_coluna");
        $controller = new AcessoColunaController($table, "txt_visibility_property", "txt_visibility_property");
        return $controller;
    }

    function getSqlColunaController() {
        $table = getTableManager()->getTabelaComNome("sql_coluna");
        $controller = new SqlColunaController($table, "txt_property_use", "txt_property_use");
        return $controller;
    }

    function getRegistroController() {
        $table = getTableManager()->getTabelaComNome("registro");
        $controller = new RegistroController();
        return $controller;
    }

    function getDatatypeController() {
        $table = getTableManager()->getTabelaComNome("datatype");
        $controller = new DatatypeController($table, "txt_datatypes", "txt_datatype", new LinkKey("txt_new_datatype", "datatypes/new/<projeto>"));
        return $controller;
    }

    function getRelacaoDatatypeController() {
        $table = getTableManager()->getTabelaComNome("relacao_datatype");
        $controller = new RelacaoDatatypeController($table, "txt_relacao_datatype", "txt_relacao_datatype");
        return $controller;
    }

    function getPrimitiveTypeController() {
        $table = getTableManager()->getTabelaComNome("primitive_type");
        $controller = new PrimitiveTypeController($table, "txt_nm_primitive_type", "txt_nm_primitive_type");
        return $controller;
    }

    function getPrimitiveTypeParamController() {
        $table = getTableManager()->getTabelaComNome("primitive_type_param");
        $controller = new PrimitiveTypeParamController($table, "txt_parameter_name", "txt_parameter_name");
        return $controller;
    }

    function getUsuarioProjetoController() {
        $table = getTableManager()->getTabelaComNome("usuario_projeto");
        $controller = new MembroProjetoController($table, "txt_members", "txt_member", new LinkKey("txt_new_member", "members/new"));
        return $controller;
    }

    function getFuncaoParametroController() {
        $table = getTableManager()->getTabelaComNome("parametro_funcao");
        $controller = new FuncaoParametroController($table, "txt_parameters", "txt_parameters", new LinkKey("txt_new_parameter", "parameter/new/<projeto>/<entidade>/<funcao>"));
        return $controller;
    }

    function getProjetoController() {
        $tableManager = getTableManager();
        $table = $tableManager->getTabelaComNome("projeto");
        $controller = new ProjetoController($table, "txt_projects", "txt_project", new LinkKey("txt_new_project", "project/new"));
        return $controller;
    }

    function getMetodologiaController() {
        $table = getTableManager()->getTabelaComNome("metodologia");
        $controller = new MetodologiaController($table, "txt_metodologies", "txt_metodology");
        return $controller;
    }

    function getTipoBancoController() {
        $table = getTableManager()->getTabelaComNome("tipo_banco");
        $controller = new TipoBancoController($table, "txt_database_type", "txt_database_type");
        return $controller;
    }

}

?>