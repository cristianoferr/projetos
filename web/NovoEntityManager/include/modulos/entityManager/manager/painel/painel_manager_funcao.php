<?

define("PAINEL_FUNC_ENT", 'P_FUNC_ENT');
define("PAINEL_FINC_FUNC", 'P_FINC_FUNC');
define("PAINEL_FUNCAO", 'P_FUNC');

class PainelManagerFuncao {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_FUNC_ENT) {
            return $this->getPainelFuncoesEntidade();
        }
        if ($nome == PAINEL_FINC_FUNC) {
            return $this->getFormInclusaoFuncao();
        }
        if ($nome == PAINEL_FUNCAO) {
            return $this->getPainelFuncao();
        }
    }

    function getPainelFuncoesEntidade() {
        $table = getTableManager()->getTabelaComNome("funcao");
        $painel = new PainelHorizontal("pfe", $table);
        $painel->addColunaWithDBName("id_funcao");
        $painel->addColunaWithDBName("nm_funcao");
        //$painel->addColunaWithDBName("tipo_de_dado");
        $painel->addColunaWithDBName("cont_params");
        $painel->modoEdicao();
        $painel->ativaAjax();
        $id_projeto = projetoAtual();
        $id_entidade = entidadeAtual();
        $painel->adicionaLink(getHomeDir() . "function/new/$id_projeto/$id_entidade", translateKey("txt_new_function"), false);


        $painel->setEditWidget(WIDGET_EDIT_FUNCAO);
        $painel->setEditLink("function/" . projetoAtual() . "/" . entidadeAtual() . "/");
        $painel->setDeleteLink("function/delete/" . projetoAtual() . "/" . entidadeAtual() . "/");


        $painel->setTitulo(translateKey("txt_functions"));
        return $painel;
    }

    function getFormInclusaoFuncao() {
        $table = getTableManager()->getTabelaComNome("funcao");
        $painel = new PainelVertical("fif", $table);
        $painel->adicionaAba(ABA_COLUNA_RETORNO, translateKey("txt_function_return"), false, true);

        $painel->addColunaWithDBName("nm_funcao");
        $painel->addColunaWithDBName("id_datatype_retorno", ABA_COLUNA_RETORNO);
        $painel->addColunaWithDBName("id_entidade_retorno", ABA_COLUNA_RETORNO);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelFuncao() {
        $table = getTableManager()->getTabelaComNome("funcao");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelVertical("pf", $table);

        $painel->adicionaAba(ABA_COLUNA_RETORNO, translateKey("txt_function_return"), false, true);


        $painel->addColunaWithDBName("id_funcao");
        //$painel->addColunaWithDBName("seq_coluna");
        $painel->addColunaWithDBName("id_entidade_pai");
        $painel->addColunaWithDBName("nm_funcao");
        $painel->addColunaWithDBName("id_datatype_retorno", ABA_COLUNA_RETORNO);
        $painel->addColunaWithDBName("id_entidade_retorno", ABA_COLUNA_RETORNO);

        $painel->adicionaAcaoEditar(getHomeDir() . "function/" . projetoAtual() . "/" . entidadeAtual() . "/");
        $painel->adicionaAcaoRemover(getHomeDir() . "function/delete/" . projetoAtual() . "/" . entidadeAtual() . "/");

        $painel->modoEdicao();
        return $painel;
    }

}

?>