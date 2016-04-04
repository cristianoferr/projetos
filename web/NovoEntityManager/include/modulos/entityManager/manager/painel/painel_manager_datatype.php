<?

define("PAINEL_DATATYPES", 'P_DATATYPES');
define("PAINEL_DATATYPES_PARAMS", 'P_DT_PARAMS');
define("PAINEL_DATATYPE", 'P_DATATYPE');
define("PAINEL_FORM_INC_DATATYPE", 'FORM_INC_DATATYPE');

class PainelManagerDatatype {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_FORM_INC_DATATYPE)
            return $this->getFormInclusaoDatatype();
        if ($nome == PAINEL_DATATYPES)
            return $this->getPainelDatatypes();
        if ($nome == PAINEL_DATATYPES_PARAMS)
            return $this->getPainelDatatypeParams();
        if ($nome == PAINEL_DATATYPE)
            return $this->getPainelDatatype();
    }

    function getFormInclusaoDatatype() {
        $table = getTableManager()->getTabelaComNome("datatype");
        $painel = new PainelVertical("pe", $table);
        $painel->addColunaWithDBName("nm_datatype");
        $painel->addColunaWithDBName("id_primitive_type");
        $painel->addColunaWithDBName("desc_datatype");

        $painel->adicionaLinkImportante(getHomeDir() . "datatypes/" . projetoAtual(), translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelDatatypes() {
        $table = getTableManager()->getTabelaComNome("datatype");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("pds", $table);
        $painel->addColunaWithDBName("id_datatype");
        $painel->addColunaWithDBName("nm_datatype");
        $painel->addColunaWithDBName("id_primitive_type");
        $painel->addColunaWithDBName("desc_datatype");
        $painel->modoEdicao();

        $painel->setEditWidget(WIDGET_EDIT_DATATYPE);
        $painel->setEditLink("datatypes/" . projetoAtual() . "/");

        return $painel;
    }

    function getPainelDatatypeParams() {

        $table = getTableManager()->getTabelaComNome("datatype_param");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("pdps", $table);
        $painel->addColunaWithDBName("id_datatype_param");
        $painel->addColunaWithDBName("valor_param");
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelDatatype() {
        $table = getTableManager()->getTabelaComNome("datatype");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelVertical("pd", $table);
        $painel->addColunaWithDBName("id_datatype");
        $painel->addColunaWithDBName("nm_datatype");
        $painel->addColunaWithDBName("id_primitive_type");
        $painel->addColunaWithDBName("desc_datatype");
        //$painel->adicionaAcaoSeguir(getHomeDir()."datatypes/".projetoAtual()."/");
        $painel->modoEdicao();
        return $painel;
    }

}

?>