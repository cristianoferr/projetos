<?

class PrereqTarefaController extends BaseController {

    function geraArrayParaColuna(IColuna $coluna, IModel $model = null, $filtro = null) {

        if ($coluna->getDbName() == "id_tarefa_prereq") {
            $id = $model->getId();
//  $filtro = "and id_tarefa not in (select id_tarefa_prereq from prereq_tarefa p where p.id_tarefa=$id) and id_tarefa<>$id ";
//$filtro = "and id_tarefa<>$id ";
        }
        return parent::geraArrayParaColuna($coluna, $model, $filtro);
    }

    function excluirRegistro($id_prereq) {
        $id_tarefa = validaNumero(tarefaAtual(), "excluiregistro PrereqTarefaController");

        validaEscrita("tarefa", $id_tarefa);
        executaSQL("delete from prereq_tarefa where id_tarefa=? and id_tarefa_prereq=?", array($id_tarefa, $id_prereq));
    }

    function insereRegistro($array) {
//echo "$nm_projeto,$iniciais_projeto,$package_projeto,$id_tipo_banco,$id_metodologia";
        validaEscrita("tarefa", $array['id_tarefa']);
        $array['id_tarefa'] = validaNumero($array['id_tarefa'], "id_tarefa insereRegistro PrereqTarefaController");
        $array['id_tarefa_prereq'] = validaNumero($array['id_tarefa_prereq'], "id_tarefa_prereq insereRegistro PrereqTarefaController");

        $sql = "insert into prereq_tarefa (id_tarefa,id_tarefa_prereq) values ";
        $sql.="(:id_tarefa,:id_tarefa_prereq)";

        executaSQL($sql, $array);
    }

}
?>