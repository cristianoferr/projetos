<?

define("PAINEL_PARAM_FUNC", 'P_PAR_FUNC');
define("PAINEL_FINC_PAR_FUNC", 'P_FINC_PAR_FUNC');
define("PAINEL_PAR_FUNC", 'P_PAR_FUNC');

class PainelManagerParametroFuncao {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_PARAM_FUNC) {
            return $this->getPainelParametrosFuncao();
        }
        if ($nome == PAINEL_FINC_PAR_FUNC) {
            return $this->getFormInclusaoParametroFuncao();
        }
        if ($nome == PAINEL_PAR_FUNC) {
            return $this->getPainelParametroFuncao();
        }
    }

    function getPainelParametrosFuncao() {
        $table = getTableManager()->getTabelaComNome("parametro_funcao");
        $painel = new PainelHorizontal("ppf", $table);
        $painel->addColunaWithDBName("id_parametro_funcao");
        $painel->addColunaWithDBName("nm_parametro_funcao");
        $painel->addColunaWithDBName("var_parametro_funcao");
        $painel->addColunaWithDBName("id_datatype_param");
        $painel->addColunaWithDBName("id_entidade_param");
        //$painel->addColunaWithDBName("tipo_de_dado");

        $painel->modoEdicao();
        $painel->ativaAjax();
        $painel->adicionaLink(getHomeDir() . "parameter/new/" . projetoAtual() . "/" . entidadeAtual() . "/" . funcaoAtual(), translateKey("txt_new_parameter"), false);

        $painel->adicionaAcaoEditar(getHomeDir() . "parameter/" . projetoAtual() . "/" . entidadeAtual() . "/" . funcaoAtual() . "/");
        $painel->adicionaAcaoRemover(getHomeDir() . "parameter/delete/" . projetoAtual() . "/" . entidadeAtual() . "/" . funcaoAtual() . "/");

        $painel->setTitulo(translateKey("txt_parameters"));
        return $painel;
    }

    function getFormInclusaoParametroFuncao() {
        $table = getTableManager()->getTabelaComNome("parametro_funcao");
        $painel = new PainelVertical("fipf", $table);

        $painel->adicionaAba(ABA_COLUNA_RETORNO, translateKey("txt_function_return"), false, true);

        $painel->addColunaWithDBName("nm_parametro_funcao");
        $painel->addColunaWithDBName("var_parametro_funcao");
        $painel->addColunaWithDBName("id_datatype_param", ABA_COLUNA_RETORNO);
        $painel->addColunaWithDBName("id_entidade_param", ABA_COLUNA_RETORNO);
        //$painel->addColunaWithDBName("tipo_de_dado");
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelParametroFuncao() {
        $table = getTableManager()->getTabelaComNome("parametro_funcao");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelVertical("ppf", $table);
        $painel->addColunaWithDBName("id_parametro_funcao");
        //$painel->addColunaWithDBName("seq_coluna");
        $painel->addColunaWithDBName("id_funcao");
        $painel->addColunaWithDBName("nm_parametro_funcao");
        $painel->addColunaWithDBName("var_parametro_funcao");
        $painel->addColunaWithDBName("tipo_de_dado");

        $painel->adicionaAcaoEditar(getHomeDir() . "parameter/" . projetoAtual() . "/" . entidadeAtual() . "/" . funcaoAtual() . "/");
        $painel->adicionaAcaoRemover(getHomeDir() . "parameter/delete/" . projetoAtual() . "/" . entidadeAtual() . "/" . funcaoAtual() . "/");


        $painel->modoEdicao();
        return $painel;
    }

}

?>