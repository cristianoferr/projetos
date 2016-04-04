<?

class TableManagerEntidades extends TableManager {

    function carregaTabelaComNome($tableName) {

        if ($tableName == "acesso_coluna") {
            return $this->carregaAcessoColuna();
        }
        if ($tableName == "camada") {
            return $this->carregaCamada();
        }
        if ($tableName == "categoria_projeto") {
            return $this->carregaCategoriaProjeto();
        }
        if ($tableName == "coluna") {
            return $this->carregaColuna();
        }
        if ($tableName == "datatype") {
            return $this->carregaDatatype();
        }
        if ($tableName == "datatype_param") {
            return $this->carregaDatatypeParam();
        }
        if ($tableName == "entidade") {
            return $this->carregaEntidade();
        }
        if ($tableName == "funcao") {
            return $this->carregaFuncao();
        }
        if ($tableName == "invite_usuario_projeto") {
            return $this->carregaInviteUsuarioProjeto();
        }
        if ($tableName == "linguagem") {
            return $this->carregaLinguagem();
        }
        if ($tableName == "meta_type") {
            return $this->carregaMetaType();
        }
        if ($tableName == "metodologia") {
            return $this->carregaMetodologia();
        }
        if ($tableName == "papel") {
            return $this->carregaPapel();
        }
        if ($tableName == "parametro_funcao") {
            return $this->carregaParametroFuncao();
        }
        if ($tableName == "primitive_type") {
            return $this->carregaPrimitiveType();
        }
        if ($tableName == "primitive_type_param") {
            return $this->carregaPrimitiveTypeParam();
        }
        if ($tableName == "projeto") {
            return $this->carregaProjeto();
        }
        if ($tableName == "registro") {
            return $this->carregaRegistro();
        }
        if ($tableName == "relacao_datatype") {
            return $this->carregaRelacaoDatatype();
        }
        if ($tableName == "sql_coluna") {
            return $this->carregaSQLColuna();
        }
        if ($tableName == "tipo_banco") {
            return $this->carregaTipoBanco();
        }
        if ($tableName == "usuario_projeto") {
            return $this->carregaUsuarioProjeto();
        }
        if ($tableName == "valor") {
            return $this->carregaValor();
        }
        if ($tableName == "visibilidade_projeto") {
            return $this->carregaVisibilidadeProjeto();
        }
    }

    function carregaVisibilidadeProjeto() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("visibilidade_projeto", "id_visibilidade_projeto", "nm_visibilidade_projeto");
        $coluna = $this->adicionaColunaNormal($tabela, "ID visibilidade_projeto", "id_visibilidade_projeto", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_visibilidade_projeto"), "nm_visibilidade_projeto", false, TIPO_INPUT_TEXTO_CURTO);
        $tabela->setSortBy('id_visibilidade_projeto');
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaCategoriaProjeto() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("categoria_projeto", "id_categoria_projeto", "nm_categoria_projeto");
        $coluna = $this->adicionaColunaNormal($tabela, "ID categoria_projeto", "id_categoria_projeto", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_categoria_projeto"), "nm_categoria_projeto", false, TIPO_INPUT_TEXTO_CURTO);
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaProjeto() {
        //$coluna->setDetalhesInclusao(true,flagrequired,defaultValue);
        //projeto
        $tabela = new TabelaModel("projeto", "id_projeto", "nm_projeto");
        $coluna = $this->adicionaColunaNormal($tabela, "ProjetoID", "id_projeto", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_cod_project"), "cod_projeto", true, TIPO_INPUT_TEXTO_CURTO, 30);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_cod_project_hint");

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_project_name"), "nm_projeto", false, TIPO_INPUT_TEXTO_CURTO, 50);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_project_hint");

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_project_initials"), "iniciais_projeto", false, TIPO_INPUT_TEXTO_CURTO, 10);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna->setHintKey("txt_project_initials_hint");

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_project_package"), "package_projeto", false, TIPO_INPUT_TEXTO_CURTO, 80);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna->setHintKey("txt_project_package_hint");

        $coluna = $this->adicionaColunaComFK($tabela, null, "id_tipo_banco", false, false, "tipo_banco", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, "2");
        $coluna->setHintKey("txt_project_database_type_hint");

        $coluna = $this->adicionaColunaComFK($tabela, null, "id_metodologia", false, true, "metodologia", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, 1);
        $coluna->setHintKey("txt_metodology_hint");

        $coluna = $this->adicionaColunaComFK($tabela, null, "id_visibilidade_projeto", false, false, "visibilidade_projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, 2);
        $coluna->setHintKey("txt_visibilidade_projeto_hint");

        $coluna = $this->adicionaColunaComFK($tabela, null, "id_categoria_projeto", false, false, "categoria_projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, 1);
        $coluna->setHintKey("txt_categoria_projeto_hint");

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_project_desc"), "desc_projeto", false, TIPO_INPUT_HTML);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_project_desc_hint");



        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_project_public"), "flag_publico", false, TIPO_INPUT_BOOLEAN);
        $coluna->setDetalhesInclusao(true, true, 'T');
        $coluna->setHintKey("txt_project_public_hint");

        $tabela->setPaginaInclusao(getHomeDir() . "project/");

        //campos calculados...

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_cod_project"), "cod_projeto_novo", false, TIPO_INPUT_TEXTO_CURTO, 30); //TIPO_INPUT_UNIQUE_ID_PROJETO
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_import_from_sql"), "sql_import", false, TIPO_INPUT_TEXTAREA);
        $coluna->setDetalhesInclusao(true, false, null);


        $this->adicionaColunaCalculada($tabela, translateKey("txt_entities"), "cont_entidades", "select count(*) as cont_entidades from entidade where entidade.id_projeto=");
        $this->adicionaColunaCalculada($tabela, translateKey("txt_open_tasks"), "cont_tarefas", "select count(*) as cont_tarefas from tarefa  where (tarefa.id_status_tarefa>=1 and tarefa.id_status_tarefa<=6) and tarefa.id_projeto=");
        $this->adicionaColunaCalculada($tabela, translateKey("txt_sub_projects"), "cont_sub_projetos", "select count(*) as cont_sub_projetos from projeto_projeto where projeto_projeto.id_projeto_pai=");
        $this->adicionaColunaDescreveFK($tabela, translateKey("txt_database"), "nm_tipo_banco", "tipo_banco", "projeto.id_tipo_banco=tipo_banco.id_tipo_banco");
        $this->adicionaColunaDescreveFK($tabela, translateKey("txt_metodology"), "nm_metodologia", "metodologia", "projeto.id_metodologia=metodologia.id_metodologia");

        return $tabela;
    }

    function carregaEntidade() {
        //entidade
        $tabela = new TabelaModel("entidade", "id_entidade", "nm_entidade");
        //echo "tabela entidade: ".$tabela->getTableName();
        $coluna = $this->adicionaColunaNormal($tabela, "EntidadeID", "id_entidade", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_entity"), "nm_entidade", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_entity_hint");

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_description"), "desc_entidade", false, TIPO_INPUT_HTML);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_entity_description_hint");

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_table_name"), "dbname_entidade", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_class_name"), "classname_entidade", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_package"), "package_entidade", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_primary_key"), "id_coluna_pk", false, true, "coluna", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(false, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_description_key"), "id_coluna_desc", false, true, "coluna", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(false, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_projeto", false, false, "projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(false, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_extends"), "id_entidade_extends", false, true, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_flag_banco"), "flag_banco", false, TIPO_INPUT_BOOLEAN);
        $coluna->setDetalhesInclusao(true, true, 'T');
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_flag_classe"), "flag_classe", false, TIPO_INPUT_BOOLEAN);
        $coluna->setDetalhesInclusao(true, true, 'F');
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_camada", false, true, "camada", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_entregavel_entidade"), "id_entregavel", false, false, "entregavel", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, false, FIRST_OPTION);
        $tabela->setPaginaInclusao(getHomeDir() ."/entity/");

        $tabela->addDependencia("flag_classe", "T", "classname_entidade", true);
        $tabela->addDependencia("flag_classe", "T", "package_entidade", true);
        $tabela->addDependencia("flag_classe", "T", "id_entidade_extends", true);
        $tabela->addDependencia("flag_classe", "T", "table_pfe", false); //painel coluna entidade




        $tabela->addDependencia("flag_banco", "T", "dbname_entidade", true);
        //$tabela->addDependencia("flag_banco","T","table_pce",false);//painel coluna entidade
        //campos calculados...

        $this->adicionaColunaCalculada($tabela, translateKey("txt_properties"), "cont_colunas", "select count(*) as cont_colunas from coluna where coluna.id_entidade_pai=");
        $this->adicionaColunaCalculada($tabela, translateKey("txt_records"), "cont_registros", "select count(*) as cont_registros from registro where registro.id_entidade_pai=");
        $this->adicionaColunaCalculada($tabela, translateKey("txt_functions"), "cont_funcoes", "select count(*) as cont_funcoes from funcao  where funcao.id_entidade_pai=");
        return $tabela;
    }

    function carregaRegistro() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("registro", "id_registro");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Registro", "id_registro", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();

        $coluna = $this->adicionaColunaNormal($tabela, "Seq.", "id_registro", true, TIPO_INPUT_SEQUENCE);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_entidade_pai", true, false, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();

        return $tabela;
    }

    function carregaParametroFuncao() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("parametro_funcao", "id_parametro_funcao", "nm_parametro_funcao");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Parametro_funcao", "id_parametro_funcao", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_parameter_name"), "nm_parametro_funcao", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_parameter_var"), "var_parametro_funcao", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, "arg");

        $coluna = $this->adicionaColunaComFK($tabela, null, "id_funcao", true, false, "funcao", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_entidade_param", false, true, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_datatype_param", false, true, "datatype", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);

        /* $condicaoColunaComposta = new Condicao(CONDITION_EQUALS, translateKey("txt_entities"), translateKey("txt_entities"), COND_FIELD_OPTGROUP);

          $coluna = $this->adicionaColunaComposta($tabela, "tipo_de_dado", translateKey("txt_function_param"), translateKey("txt_datatype_param"), "id_primitive_param", "datatype", translateKey("txt_param_entity"), "id_entidade_param", "entidade", false, TIPO_INPUT_SELECT_FK, $condicaoColunaComposta);
          $coluna->setDetalhesInclusao(true, true, null); */

        $tabela->setPaginaInclusao(getHomeDir() ."parameter/");
        return $tabela;
    }

    function carregaFuncao() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("funcao", "id_funcao", "nm_funcao");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Funcao", "id_funcao", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_function_name"), "nm_funcao", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_entidade_pai", true, false, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_return_entity"), "id_entidade_retorno", false, true, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_datatype_return"), "id_datatype_retorno", false, true, "datatype", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);


        /* $condicaoColunaComposta = new Condicao(CONDITION_EQUALS, translateKey("txt_entities"), translateKey("txt_entities"), COND_FIELD_OPTGROUP);
          $coluna = $this->adicionaColunaComposta($tabela, "tipo_de_dado", translateKey("txt_function_return"), translateKey("txt_return_entity"), "id_datatype_retorno", "datatype", translateKey("txt_parent_entity"), "id_entidade_retorno", "entidade", false, TIPO_INPUT_SELECT_FK, $condicaoColunaComposta);
          $coluna->setDetalhesInclusao(true, false, null); */


        $this->adicionaColunaCalculada($tabela, translateKey("txt_parameters_count"), "cont_params", "select count(*) as cont_params from parametro_funcao  where parametro_funcao.id_funcao=");

        $tabela->setPaginaInclusao(getHomeDir() ."/function/");
        return $tabela;
    }

    function carregaUsuarioProjeto() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("usuario_projeto", "id_usuario");
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_member"), "id_usuario", true, false, "usuario", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_projeto", true, false, "projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_papel", true, false, "papel", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);

        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_invite_member"), "id_usuario_novo", true, false, "usuario", TIPO_INPUT_BUSCA_USUARIO);
        $coluna->setDetalhesInclusao(true, false, null);

        $coluna = $this->adicionaColunaDescreveFK($tabela, translateKey("txt_member"), "nm_usuario", "usuario", "usuario.id_usuario=usuario_projeto.id_usuario");
        $coluna = $this->adicionaColunaDescreveFK($tabela, translateKey("txt_email"), "email_usuario", "usuario", "usuario.id_usuario=usuario_projeto.id_usuario");

        $coluna = $this->adicionaColunaCalculada($tabela, translateKey("txt_email"), "email_usuario_hidden", "concat(left(email_usuario,instr(email_usuario,'@')+1),\"...\")", false);

        $tabela->setPaginaInclusao(getHomeDir() ."/members/");

        return $tabela;
    }

    function carregaInviteUsuarioProjeto() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("invite_usuario_projeto", "id_invite");
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_member"), "id_usuario", false, true, "usuario", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_projeto", true, false, "projeto", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_papel", true, false, "papel", TIPO_INPUT_RADIO);
        $coluna->setDetalhesInclusao(true, false, 1);

        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_invite_member"), "id_usuario_novo", true, false, "usuario", TIPO_INPUT_BUSCA_USUARIO);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_external_member"), "email_externo", true, TIPO_INPUT_TEXTAREA);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_texto_invite"), "texto_invite", true, TIPO_INPUT_TEXTAREA);
        $coluna->setDetalhesInclusao(true, false, null);

        $tabela->setPaginaInclusao(getHomeDir() ."/members/");

        return $tabela;
    }

    function carregaValor() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("valor", "id_valor", "valor_coluna");
        $coluna = $this->adicionaColunaNormal($tabela, "ID valor", "id_valor", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_registro", true, false, "registro", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_coluna", true, false, "coluna", TIPO_INPUT_SELECT_FK);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, "Valor", "valor_coluna", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaPapel() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("papel", "id_papel", "nm_papel");
        $coluna = $this->adicionaColunaNormal($tabela, "ID papel", "id_papel", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_role"), "nm_papel", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $tabela->traduzDescricao();
        return $tabela;

        //campos calculados...
    }

    function carregaCamada() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("camada", "id_camada", "nm_camada");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Camada", "id_camada", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_layer"), "nm_camada", false, TIPO_INPUT_TEXTO_CURTO);
        $this->adicionaColunaNormal($tabela, translateKey("txt_color_layer"), "cor_camada", false, TIPO_INPUT_COR);
        return $tabela;

        //campos calculados...

        $this->adicionaColunaCalculada($tabela, translateKey("txt_entities"), "cont_entidades", "select count(*) as cont_entidades from entidade  where entidade.id_camada=");
    }

    function carregaMetodologia() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("metodologia", "id_metodologia", "nm_metodologia");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Metodologia", "id_metodologia", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_metodology"), "nm_metodologia", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaMetaType() {
        $tabela = new TabelaModel("meta_type", "id_meta_type", "nm_meta_type");
        $tabela->traduzDescricao();
        $coluna = $this->adicionaColunaNormal($tabela, "ID Primitive Type", "id_meta_type", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_nm_primitive_type"), "nm_meta_type", false, TIPO_INPUT_TEXTO_CURTO);
        $this->adicionaColunaNormal($tabela, "Classe CSS no UML", "css_meta_type", false, TIPO_INPUT_TEXTO_CURTO);

        return $tabela;
    }

    function carregaPrimitiveType() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("primitive_type", "id_primitive_type", "nm_primitive_type");
        $tabela->traduzDescricao();
        $coluna = $this->adicionaColunaNormal($tabela, "ID Primitive Type", "id_primitive_type", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_nm_primitive_type"), "nm_primitive_type", false, TIPO_INPUT_TEXTO_CURTO);
        $this->adicionaColunaNormal($tabela, "Classe input", "form_primitive_type", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_meta_type", false, false, "meta_type", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        return $tabela;
    }

    function carregaPrimitiveTypeParam() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("primitive_type_param", "id_primitive_type_param", "nm_primitive_type_param");
        $tabela->traduzDescricao();
        $coluna = $this->adicionaColunaNormal($tabela, "ID Primitive Type", "id_primitive_type_param", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_primitive_type", false, false, "primitive_type", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $this->adicionaColunaNormal($tabela, "Valor Default", "default_primitive_type_param", false, TIPO_INPUT_TEXTO_CURTO);
        $this->adicionaColunaNormal($tabela, "Nome", "nm_primitive_type_param", false, TIPO_INPUT_TEXTO_CURTO);
        $this->adicionaColunaNormal($tabela, "Sequencial", "seq_primitive_type_param", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaColuna() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("coluna", "id_coluna", "nm_coluna");

        $coluna = $this->adicionaColunaNormal($tabela, "ID Coluna", "id_coluna", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_property"), "nm_coluna", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_entidade_pai", false, false, "entidade", TIPO_INPUT_RADIO_SELECT);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_sequence"), "seq_coluna", false, TIPO_INPUT_SEQUENCE);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_database_field"), "dbname_coluna", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setCopyFrom("nm_coluna");
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_property_name"), "prop_name_coluna", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setCopyFrom("nm_coluna");
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_foreign_entity"), "id_entidade_combo", false, true, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_relacao_datatype", false, false, "relacao_datatype", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, 2);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_sql_coluna", false, false, "sql_coluna", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, 1);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_datatype", false, false, "datatype", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_acesso_coluna", false, false, "acesso_coluna", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, 4);



        $this->adicionaColunaCalculada($tabela, "css", "css_meta_type", "select css_meta_type from primitive_type p,datatype d,meta_type m where m.id_meta_type=p.id_meta_type and p.id_primitive_type=d.id_datatype and d.id_datatype=coluna.id_datatype and coluna.id_coluna=");


        $tabela->setPaginaInclusao(getHomeDir() . "property");
        return $tabela;
    }

    function carregaLinguagem() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("linguagem", "id_linguagem", "nm_linguagem");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Linguagem", "id_linguagem", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_language"), "nm_linguagem", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaSQLColuna() {
        $tabela = new TabelaModel("sql_coluna", "id_sql_coluna", "nm_sql_coluna");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Uso Coluna", "id_sql_coluna", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_property_use"), "nm_sql_coluna", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_sql_coluna_flag_create"), "flag_create", false, TIPO_INPUT_BOOLEAN);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_sql_coluna_flag_export"), "flag_export", false, TIPO_INPUT_BOOLEAN);
        $tabela->traduzDescricao();
        return $tabela;
    }

    function carregaAcessoColuna() {
        $tabela = new TabelaModel("acesso_coluna", "id_acesso_coluna", "nm_acesso_coluna");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Visibilidade Coluna", "id_acesso_coluna", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_visibility_property"), "nm_acesso_coluna", false, TIPO_INPUT_TEXTO_CURTO);
        $tabela->traduzDescricao();

        return $tabela;
    }

    /* function carregaCamadaProjeto() {
      //tipo de banco (mysql,oracle,etc)
      $tabela = new TabelaModel("camada_projeto", "id_camada", "");
      $this->adicionaColunaComFK($tabela, translateKey("txt_layer"), "id_camada", true, false, "camada", TIPO_INPUT_SELECT_FK);
      $this->adicionaColunaComFK($tabela, translateKey("txt_project"), "id_projeto", true, false, "projeto", TIPO_INPUT_SELECT_FK);
      $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_language"), "id_linguagem", false, true, "linguagem", TIPO_INPUT_SELECT_FK);
      $coluna->setDetalhesInclusao(true, true, 8);
      $this->adicionaColunaNormal($tabela, translateKey("txt_color_layer"), "cor_camada_projeto", false, TIPO_INPUT_COR);

      //campos calculados...
      $this->adicionaColunaCalculada($tabela, translateKey("txt_entities"), "cont_entidades", "select count(*) as cont_entidades from entidade  where entidade.id_projeto=" . projetoAtual() . " and entidade.id_camada=");


      return $tabela;
      } */

    function carregaTipoBanco() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("tipo_banco", "id_tipo_banco", "nm_tipo_banco");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Tipo Banco", "id_tipo_banco", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $this->adicionaColunaNormal($tabela, translateKey("txt_database_type"), "nm_tipo_banco", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaRelacaoDatatype() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("relacao_datatype", "id_relacao_datatype", "nm_relacao_datatype");
        $coluna = $this->adicionaColunaNormal($tabela, "ID relacao_datatype", "id_relacao_datatype", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $tabela->traduzDescricao();
        $tabela->setSortBy('id_relacao_datatype');
        $this->adicionaColunaNormal($tabela, translateKey("txt_urgency"), "nm_relacao_datatype", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaDatatype() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("datatype", "id_datatype", "nm_datatype");
        $coluna = $this->adicionaColunaNormal($tabela, "ID datatype", "id_datatype", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_datatype"), "nm_datatype", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_datatype_desc"), "desc_datatype", false, TIPO_INPUT_TEXTAREA, 250);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, null, "id_primitive_type", true, false, "primitive_type", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $tabela->setPaginaInclusao(getHomeDir() . "datatypes/");

        $tabela->addCondicaoSelect(PRIMITIVE_ENTITY, CONDITION_EQUALS, txt_entities, "id_primitive_type");
        $tabela->addCondicaoSelect(PRIMITIVE_ENTITY, CONDITION_DIFFERENT, txt_primitives, "id_primitive_type");

        return $tabela;
    }

    function carregaDatatypeParam() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("datatype_param", "id_datatype_param", "valor_datatype_param");
        $coluna = $this->adicionaColunaNormal($tabela, "ID datatype", "id_datatype_param", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        //$coluna=$this->adicionaColunaComFK($tabela,translateKey("txt_datatype"),"id_datatype",true,false,"datatype",TIPO_INPUT_RADIO_SELECT);	
        $this->adicionaColunaNormal($tabela, translateKey("txt_value"), "valor_param", false, TIPO_INPUT_TEXTAREA, 20);

        return $tabela;
    }

}

?>