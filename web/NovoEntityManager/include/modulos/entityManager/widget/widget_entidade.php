<?

define("WIDGET_EDIT_ENTIDADE", 'WIDGET_EDIT_ENTIDADE');
define("WIDGET_EDIT_COLUNA", 'WIDGET_EDIT_COLUNA');
define("WIDGET_EDIT_FUNCAO", 'WIDGET_EDIT_FUNCAO');
define("WIDGET_EDIT_DATATYPE", 'WIDGET_EDIT_DATATYPE');
define("WIDGET_EDIT_VIRTUAL_REGISTRO", 'WIDGET_EDIT_VIRTUAL_REGISTRO');
define("WIDGET_MEMBROS_PROJETO", 'WIDGET_MEMBROS_PROJETO');
define("WIDGET_ENTIDADES_PROJETO", 'WIDGET_ENTIDADES_PROJETO');
define("WIDGET_ENTIDADES", 'WIDGET_ENTIDADES');
define("WIDGET_COLUNAS_ENTIDADE", 'WIDGET_COLUNAS_ENTIDADE');
define("WIDGET_FUNCOES_ENTIDADE", 'WIDGET_FUNCOES_ENTIDADE');
define("WIDGET_VISUALIZA_ENTIDADE", 'WIDGET_VISUALIZA_ENTIDADE');

class WidgetEntidade extends WidgetBase {

    function getWidgetsArray() {//coberto
        return array(WIDGET_EDIT_ENTIDADE => ENTIDADE_TESTE, WIDGET_EDIT_COLUNA => COLUNA_TESTE, WIDGET_EDIT_FUNCAO => FUNCAO_TESTE, WIDGET_MEMBROS_PROJETO => PROJETO_TESTE,
            WIDGET_ENTIDADES_PROJETO => PROJETO_TESTE, WIDGET_VISUALIZA_ENTIDADE => ENTIDADE_TESTE, WIDGET_ENTIDADES => PROJETO_TESTE, WIDGET_COLUNAS_ENTIDADE => ENTIDADE_TESTE, WIDGET_FUNCOES_ENTIDADE => ENTIDADE_TESTE);
        //,WIDGET_EDIT_DATATYPE=>,WIDGET_EDIT_VIRTUAL_REGISTRO=>
    }

    function getWidgetFor($nome) {//coberto
        if ($nome == WIDGET_VISUALIZA_ENTIDADE) {
            return new WidgetVisualizaEntidade();
        }
        if ($nome == WIDGET_EDIT_ENTIDADE) {
            return new WidgetEditEntidade();
        }

        if ($nome == WIDGET_EDIT_COLUNA) {
            return new WidgetEditColuna();
        }
        if ($nome == WIDGET_EDIT_FUNCAO) {
            return new WidgetEditFuncao();
        }
        if ($nome == WIDGET_EDIT_DATATYPE) {
            return new WidgetEditDatatype();
        }

        if ($nome == WIDGET_EDIT_VIRTUAL_REGISTRO) {
            return new WidgetEditVirtualRegistro();
        }
        if ($nome == WIDGET_MEMBROS_PROJETO) {
            return new WidgetMembrosProjeto();
        }
        if ($nome == WIDGET_ENTIDADES_PROJETO) {
            return new WidgetEntidadesProjeto();
        }
        if ($nome == WIDGET_ENTIDADES) {
            return new WidgetEntidades();
        }
        if ($nome == WIDGET_COLUNAS_ENTIDADE) {
            return new WidgetColunasEntidade();
        }
        if ($nome == WIDGET_FUNCOES_ENTIDADE) {
            return new WidgetFuncoesEntidade();
        }
    }

    function getVars() {
        $vars = parent::getVars();
        return $vars . "&modulo=" . MODULO_TAREFAS;
    }

}

?>