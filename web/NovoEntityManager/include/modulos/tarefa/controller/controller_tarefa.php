<?

class TarefaController extends BaseController {

    function getOrderBy() {
        return "id_status_tarefa,nm_tarefa";
    }

    function filtrosExtras() {
        return " and tarefa.id_projeto=" . projetoAtual();
    }

    function avancarTarefa($id_tarefa) {
        executaSQL("update tarefa set id_status_tarefa=id_status_tarefa+1 where id_tarefa=?", array($id_tarefa));
    }

    function countForProjeto($id_projeto) {//coberto
        $id_projeto = validaNumero($id_projeto, "id_projeto countForProjeto EntidadeController");
        $id = "count_tarefa_" . projetoAtual();
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }
        $rs = executaQuery("select count(*) as tot from tarefa where id_projeto=?", array($id_projeto));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['tot']);
            return $this->getCacheInfo($id);
        }
    }

    function excluirRegistro($id) {//coberto
        $id = validaNumero($id, "excluiregistro idcol");
        validaEscrita("tarefa", $id);
        executaSQL("delete from tarefa where id_tarefa=?", array($id));
    }

    function insereRegistro($array) {//coberto
        //echo "$nm_projeto,$iniciais_projeto,$package_projeto,$id_tipo_banco,$id_metodologia";
        validaEscrita("projeto", $array['projeto']);

        if ($array['id_entregavel'] == 'null') {
            mostraErro('txt_tarefa_entregavel_null');
        }

        //printArray($array);
        $data = date('Y-m-d\TH:i:s');
        $sql = "insert into tarefa (id_projeto,nm_tarefa,desc_tarefa,id_complexidade,id_urgencia,id_entregavel,id_status_tarefa,dtcriacao_tarefa,hh_tarefa,custo_tarefa) values ";
        $sql.="(:projeto,:nm_tarefa,:desc_tarefa,:id_complexidade,:id_urgencia,:id_entregavel," . STATUS_TAREFA_NOVA_TAREFA . ",'$data',:hh_tarefa,:custo_tarefa)";

        $id_tarefa = executaSQL($sql, $array);

        return $id_tarefa;
    }

    function getProjeto($id_tarefa) {//coberto
        $id_tarefa = validaNumero($id_tarefa, "id_tarefa getProjeto TarefaController");
        $row = executaQuerySingleRow("select id_projeto from tarefa c where c.id_tarefa=$id_tarefa");
        return $row['id_projeto'];
    }

}

?>