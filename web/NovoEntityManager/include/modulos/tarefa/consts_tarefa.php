<?

define("STATUS_TAREFA_NOVA_TAREFA", "2");
define("STATUS_TAREFA_A_FAZER", "2");
define("STATUS_TAREFA_INICIADO", "3");
define("STATUS_TAREFA_EM_ANDAMENTO", "4");
define("STATUS_TAREFA_TESTES", "5");
define("STATUS_TAREFA_CONCLUIDO", "6");
define("STATUS_TAREFA_CANCELADO", "7");
define("STATUS_TAREFA_POSTERGADO", "8");

define("STATUS_ENTREG_PLANEJADO", "1");
define("STATUS_ENTREG_APROVADO", "2");
define("STATUS_ENTREG_DESENVOLVIMENTO", "3");
define("STATUS_ENTREG_TESTES", "4");
define("STATUS_ENTREG_HOMOLOG", "5");
define("STATUS_ENTREG_ENTREGUE", "6");
define("STATUS_ENTREG_CANCELADO", "7");

define("LIMITE_STATUS_ENTREG_ABERTO", STATUS_ENTREG_HOMOLOG);



define("FILTRO_TAREFA", "filtro_tarefa");
define("FILTRO_ENTREGAVEL", "filtro_entreg");

define("MODO_TAREFA", "modo_tarefa");
define("MODO_ENTREG", "modo_entreg");

define("ABA_FILTRO_ENTREGAVEL_SOMENTE", 'D');
define("ABA_FILTRO_ENTREGAVEL_TAREFA", 'DT');
define("ABA_FILTRO_GRADE", 'G');

function tarefaAtual() {
    $id = $_GET["tarefa"];

    if ($id == "") {
        $id = $_POST["tarefa"];
    }
    return $id;
}

function entregavelAtual() {
    $id = $_GET["entregavel"];

    if ($id == "") {
        $id = $_POST["entregavel"];
    }
    return $id;
}

?>