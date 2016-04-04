<?

class FuncaoController extends BaseController {

    function excluirRegistro($id) {

        $id = validaNumero($id, "id excluirRegistro FuncaoController");
        validaEscrita("funcao", $id);

        if (isUserAdminProjeto($this->getProjeto($id))) {
            executaSQL("delete from funcao where id_funcao=$id");
        }
    }

    function insereRegistro($array) {
        validaEscrita("projeto", $array['projeto']);

        unset($array['projeto']);
        //printArray($array);
        $sql = "insert into funcao (id_entidade_pai,nm_funcao,id_entidade_retorno,id_datatype_retorno) values ";
        $sql.="(:entidade,:nm_funcao,:id_entidade_retorno,:id_datatype_retorno)";
        $id_funcao = executaSQL($sql, $array);

        return $id_funcao;
    }

    function getName($id_funcao) {
        return $this->getFieldFromModel($id_funcao, "nm_funcao");
    }

    function getProjeto($id_funcao) {
        $id = "fun_proj_$id_funcao";
        if ($this->getCacheInfo($id))
            return $this->getCacheInfo($id);

        $id_funcao = validaNumero($id_funcao, "id getProjeto contr_funcao");
        $row = executaQuerySingleRow("select id_projeto from entidade e,funcao c where e.id_entidade=c.id_entidade_pai and c.id_funcao=?", array($id_funcao));
        $this->setCacheInfo($id, $row['id_projeto']);
        return $row['id_projeto'];
    }

    function getOrderBy() {
        return "nm_funcao";
    }

}

?>