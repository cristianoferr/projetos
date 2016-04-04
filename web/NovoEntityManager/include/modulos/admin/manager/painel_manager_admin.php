<?

define("PAINEL_PRIMITIVE_TYPES_ADMIN", 'P_PT_ADMIN');
define("PAINEL_PRIMITIVE_TYPE_ADMIN", 'P_PTS_ADMIN');
define("PAINEL_META_TYPES_ADMIN", 'P_MTS_ADMIN');
define("PAINEL_META_TYPE_ADMIN", 'P_PT_ADMIN');
define("PAINEL_PRIMITIVETYPEPARAM_ADMIN", 'P_PTP_ADMIN');
define("PAINEL_USER_ADMIN", 'P_USER_ADMIN');

class PainelManagerAdmin {

    function getPainelComNome($nome) {
        
        if ($nome == PAINEL_PRIMITIVE_TYPES_ADMIN)
            return $this->getPainelPrimitiveTypeAdmin();
        if ($nome == PAINEL_PRIMITIVE_TYPE_ADMIN)
            return $this->getPainelPrimitiveTypeAdminSingle();
        if ($nome == PAINEL_META_TYPES_ADMIN)
            return $this->getPainelMetaTypeAdmin();
        if ($nome == PAINEL_META_TYPE_ADMIN)
            return $this->getPainelMetaTypeAdminSingle();
        if ($nome == PAINEL_PRIMITIVETYPEPARAM_ADMIN)
            return $this->getPainelPrimitiveTypeParamAdmin();
        if ($nome == PAINEL_USER_ADMIN)
            return $this->getPainelUsuarioAdmin();
    }

    function getPainelPrimitiveTypeAdmin() {
        $table = getTableManager()->getTabelaComNome("primitive_type");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("ppta", $table);
        $painel->addColunaWithDBName("id_primitive_type");
        $painel->addColunaWithDBName("nm_primitive_type");
        $painel->addColunaWithDBName("form_primitive_type");
        $painel->addColunaWithDBName("id_meta_type");
        $painel->adicionaAcaoSeguir(getHomeDir() . "admin/primitive_type.php?id=");
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelPrimitiveTypeAdminSingle() {
        $table = getTableManager()->getTabelaComNome("primitive_type");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelVertical("pptas", $table);
        $painel->addColunaWithDBName("id_primitive_type");
        $painel->addColunaWithDBName("nm_primitive_type");
        $painel->addColunaWithDBName("form_primitive_type");
        $painel->addColunaWithDBName("id_meta_type");
        $painel->adicionaAcaoSeguir(getHomeDir() . "admin/primitive_type.php?id=");
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelMetaTypeAdmin() {
        $table = getTableManager()->getTabelaComNome("meta_type");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("pmta", $table);
        $painel->addColunaWithDBName("id_meta_type");
        $painel->addColunaWithDBName("nm_meta_type");
        $painel->addColunaWithDBName("css_meta_type");
        $painel->adicionaAcaoSeguir(getHomeDir() . "admin/meta_type.php?id=");
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelMetaTypeAdminSingle() {
        $table = getTableManager()->getTabelaComNome("meta_type");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelVertical("pmtas", $table);
        $painel->addColunaWithDBName("id_meta_type");
        $painel->addColunaWithDBName("nm_meta_type");
        $painel->addColunaWithDBName("css_meta_type");
        $painel->adicionaAcaoSeguir(getHomeDir() . "admin/meta_type.php?id=");
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelPrimitiveTypeParamAdmin() {
        $table = getTableManager()->getTabelaComNome("primitive_type_param");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("ppta", $table);
        $painel->addColunaWithDBName("id_primitive_type_param");
        $painel->addColunaWithDBName("id_primitive_type");
        $painel->addColunaWithDBName("nm_primitive_type_param");
        $painel->addColunaWithDBName("default_primitive_type_param");
        $painel->addColunaWithDBName("seq_primitive_type_param");
        $painel->adicionaAcaoSeguir(getHomeDir() . "admin/primitive_type.php?id=" . $_GET['id'] . "&param=");

        $painel->modoEdicao();
        return $painel;
    }

    function getPainelUsuarioAdmin() {
        $table = getTableManager()->getTabelaComNome("usuario");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("pua", $table);
        $painel->addColunaWithDBName("nm_usuario");
        $painel->addColunaWithDBName("email_usuario");
        $painel->addColunaWithDBName("id_lingua");
        $painel->addColunaWithDBName("dtcadastro_usuario");
        $painel->addColunaWithDBName("ultimo_acesso_usuario");
        $painel->addColunaWithDBName("conta_acesso_usuario");

        $painel->adicionaAcaoRemover(getHomeDir() . "admin/usuarios.php?acao=delete&usuario=");
        $painel->modoEdicao();
        return $painel;
    }

}

?>