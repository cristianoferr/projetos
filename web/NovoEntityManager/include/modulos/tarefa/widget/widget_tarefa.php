<?

define("WIDGET_EDIT_TAREFA", 'EDIT_TAREFA');
define("WIDGET_LISTA_TAREFAS", 'WIDGET_LISTA_TAREFAS');
define("WIDGET_OPEN_TASKS", 'OPEN_TASKS');
define("WIDGET_EDIT_ENTREGAVEL", 'EDIT_ENTREGAVEL');
define("WIDGET_ENTREGAVEIS_PROJETO", 'TSK_DELIV_PROJ');
define("WIDGET_ENTREGAVEIS", 'TSK_DELIVs');
define("WIDGET_NEW_PREREQ_TAREFA", 'NEW_PREREQ_TAREFA');
define("WIDGET_PREREQS_TAREFA", 'PREREQS_TAREFA');
define("WIDGET_SELECT_DELIVERABLE", 'SELECT_DELIVERABLE');

class WidgetTarefa extends WidgetBase {

    function getWidgetsArray() {
        return array(WIDGET_LISTA_TAREFAS => PROJETO_TESTE, WIDGET_EDIT_TAREFA => TAREFA_TESTE, WIDGET_OPEN_TASKS => PROJETO_TESTE, WIDGET_EDIT_ENTREGAVEL => ENTREGAVEL_TESTE,
            WIDGET_ENTREGAVEIS_PROJETO => PROJETO_TESTE, WIDGET_ENTREGAVEIS => PROJETO_TESTE, WIDGET_NEW_PREREQ_TAREFA => TAREFA_TESTE, WIDGET_PREREQS_TAREFA => TAREFA_TESTE);
    }

    function getWidgetFor($nome) {

        if ($nome == WIDGET_SELECT_DELIVERABLE) {
            return new WidgetSelectDeliverable();
        }
        if ($nome == WIDGET_LISTA_TAREFAS) {
            return new WidgetListaTarefas();
        }
        if ($nome == WIDGET_EDIT_TAREFA) {
            return new WidgetEditTarefa();
        }

        if ($nome == WIDGET_NEW_PREREQ_TAREFA) {
            return new WidgetNewPrereqTarefa();
        }

        if ($nome == WIDGET_PREREQS_TAREFA) {
            return new WidgetPrereqsTarefa();
        }

        if ($nome == WIDGET_EDIT_ENTREGAVEL) {
            return new WidgetEditEntregavel();
        }
        if ($nome == WIDGET_OPEN_TASKS) {
            return new WidgetOpenTasks();
        }
        if ($nome == WIDGET_ENTREGAVEIS_PROJETO) {
            return new WidgetEntregaveisProjeto(true);
        }
        if ($nome == WIDGET_ENTREGAVEIS) {
            return new WidgetEntregaveisProjeto(false);
        }
    }

    function getVars() {
        $vars = parent::getVars();
        return $vars . "&modulo=" . MODULO_TAREFAS;
    }

}

?>