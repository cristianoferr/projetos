<?

class ProjetoController extends BaseController {

    function filtrosExtras() {

        return " and projeto.flag_excluido is null";
        // return " and projeto.flag_excluido is null and projeto.id_projeto=up.id_projeto and up.id_usuario=" . usuarioAtual();
    }

    function tabelasExtras() {
        // return ",usuario_projeto up";
    }

    function corrigeDados() {

        executaSQL("delete from usuario_projeto WHERE id_projeto not in (select id_projeto from projeto)");
    }

    function getProjectCod($id_registro) {
        return $this->getFieldFromModel($id_registro, "cod_projeto");
    }

    function countProjetosUsuario($id_usuario) {
        $id_usuario = validaNumero($id_usuario, "id_usuario countProjetosUsuario EntidadeController");
        $id = "count_projetos_usuario_" . $id_usuario;
        if ($this->getCacheInfo($id))
            return $this->getCacheInfo($id);


        $rs = executaQuery("select count(*) as tot from usuario_projeto u,projeto p where u.id_projeto=p.id_projeto and p.flag_excluido is null and u.id_usuario=?", array($id_usuario));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['tot']);
            return $this->getCacheInfo($id);
        }
    }

    function consultaProjetosUsuario($id_usuario) {
        $id_usuario = validaNumero($id_usuario, "id_usuario consultaProjetosUsuario ProjetoController");

        $rs = executaQuery("select p.* from projeto p,usuario_projeto up where up.id_projeto=p.id_projeto and up.id_usuario=? order by dtultima_acao desc,nm_projeto", array($id_usuario));
        return $rs;
    }

    function insereRegistro($array) {
        //echo "$nm_projeto,$iniciais_projeto,$package_projeto,$id_tipo_banco,$id_metodologia";
        $array['nm_projeto'] = validaTexto($array['nm_projeto']);
        $array['cod_projeto'] = validaTexto($array['cod_projeto']);
        $array['iniciais_projeto'] = validaTexto($array['iniciais_projeto']);
        $array['package_projeto'] = validaTexto($array['package_projeto']);
        $array['id_tipo_banco'] = validaNumero($array['id_tipo_banco'], "id_tipo_banco insereRegistro ProjetoController");
        $array['id_visibilidade_projeto'] = validaNumero($array['id_visibilidade_projeto'], "id_visibilidade_projeto insereRegistro ProjetoController");
        $array['id_metodologia'] = validaNumero($array['id_metodologia'], "id_metodologia insereRegistro ProjetoController");

        if ($this->checkIfValueExists("cod_projeto", $array['cod_projeto'])) {
            //die("cod_projeto:".$array['cod_projeto']);
            showError(ERROR_COD_PROJETO_EXISTE);
            return;
        }
        $sql = "insert into projeto (cod_projeto,nm_projeto,iniciais_projeto,package_projeto,id_tipo_banco,id_metodologia,dtultima_acao,id_visibilidade_projeto) values ";
        $sql.="(:cod_projeto,:nm_projeto,:iniciais_projeto,:package_projeto,:id_tipo_banco,:id_metodologia,now(),:id_visibilidade_projeto)";
        //echo $sql;
        $id_projeto = executaSQL($sql, $array);
        writeDebug("linkando projeto '$id_projeto' ao usuario '" . usuarioAtual() . "'");
        //writeAdmin("linkando projeto");
        $this->linkarProjetoUsuario($id_projeto, usuarioAtual(), PAPEL_PROJETO_ADMIN, false);


        //writeAdmin("insereEntregavelRaiz");
        $entregavelController = getControllerManager()->getControllerForTable("entregavel");
        $entregavelController->insereEntregavelRaiz($id_projeto, $array['cod_projeto']);

        $datatypeController = getControllerManager()->getControllerForTable("datatype");

        //writeAdmin("insereDatatypesDefault");
        $datatypeController->insereDatatypesDefault($id_projeto);


        return $id_projeto;
    }

    function isUserAdminProjeto($id_registro) {
        $id_registro = validaNumero($id_registro, "id_registro isUserAdminProjeto ProjetoController");
        return existsQuery("select * from usuario_projeto where id_usuario=? and id_projeto=? and id_papel=?", array(usuarioAtual(), $id_registro, PAPEL_PROJETO_ADMIN));
    }

    function isProjetoPublico($id_project) {
        $id_registro = validaNumero($id_registro, "id_projeto isProjetoPublico ProjetoController");
        return existsQuery("select * from projeto where (id_visibilidade_projeto>1 and id_projeto=?) ", array($id_project));
    }

    function isUserNoProjeto($id_registro) {
        $id_registro = validaNumero($id_registro, "id_projeto isUserNoProjeto ProjetoController");
        return existsQuery("select * from usuario_projeto where (id_usuario=? and id_projeto=?) ", array(usuarioAtual(), $id_registro));
    }

    function excluirRegistro($id_projeto) {
        $id_projeto = validaNumero($id_projeto, "id_projeto excluirRegistro ProjetoController");
        if (!checkPerm("projeto", $id_projeto, true))
            return false;
        executaSQL("update projeto set flag_excluido='T' where id_projeto=$id_projeto");
        return true;
    }

    function excluirFisicamente($id_projeto) {
        $id_projeto = validaNumero($id_projeto, "id_projeto excluirRegistro ProjetoController");
        //writeDebug("Projeto validado");
        validaEscrita("projeto", $id_projeto);
        executaSQL("delete from usuario_projeto where id_projeto=$id_projeto");
        getControllerManager()->getEntidadeController()->excluirRegistrosProjeto($id_projeto);
        executaSQL("delete from tarefa where id_projeto=$id_projeto");
        executaSQL("delete from entregavel where id_projeto=$id_projeto");
        executaSQL("delete from datatype_param where id_datatype in (select id_datatype from datatype where id_projeto=$id_projeto)");
        executaSQL("delete from datatype where id_projeto=$id_projeto");
        executaSQL("delete from projeto_projeto where id_projeto_pai=$id_projeto or id_projeto_filho=$id_projeto");
        executaSQL("delete from projeto where id_projeto=$id_projeto");
    }

    function usuarioPodeExcluir($id_registro) {
        return $this->isUserAdminProjeto($id_registro);
    }

    function linkarProjetoUsuario($id_projeto, $id_usuario, $id_papel, $flagValida) {
        $id_projeto = validaNumero($id_projeto, "id_projeto linkarProjetoUsuario ProjetoController");
        $id_papel = $id_papel + 1;
        $id_papel = $id_papel - 1;
        if ($flagValida)
            validaEscrita("projeto", $id_projeto);
        writeDebug("linkarProjetoUsuario: validando id_usuario ($id_usuario)...");
        $id_usuario = validaNumero($id_usuario);
        writeDebug("linkarProjetoUsuario: validando id_papel ($id_papel)...");
        $id_papel = validaNumero($id_papel);

        executaSQL("insert into usuario_projeto (id_projeto,id_usuario,id_papel) values ($id_projeto,$id_usuario,$id_papel)");
    }

    function removeProjetoUsuario($id_projeto, $id_usuario) {
        $id_projeto = validaNumero($id_projeto, "id_projeto removeProjetoUsuario ProjetoController");
        $id_usuario = validaNumero($id_usuario, "id_usuario removeProjetoUsuario ProjetoController");
        executaSQL("delete from  usuario_projeto where id_projeto=$id_projeto and id_usuario=$id_usuario");
    }

    function mudaPapelProjetoUsuario($id_projeto, $id_usuario, $id_papel) {
        $id_projeto = validaNumero($id_projeto, "id_projeto mudaPapelProjetoUsuario ProjetoController");
        $id_usuario = validaNumero($id_usuario, "id_usuario mudaPapelProjetoUsuario ProjetoController");
        $id_papel = validaNumero($id_papel, "id_papel mudaPapelProjetoUsuario ProjetoController");
        validaEscrita("projeto", $id_projeto);
        executaSQL("update usuario_projeto  set id_papel=$id_papel where id_projeto=$id_projeto and id_usuario=$id_usuario");
    }

}

?>