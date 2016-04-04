<?

//id=entidade
class WidgetVisualizaEntidade extends WidgetEntidade {

    function generate($id) {//coberto
        $entidadeController = getControllerManager()->getControllerForTable("entidade");
        $colunaController = getControllerManager()->getControllerForTable("coluna");
        $modelEntidade = $entidadeController->loadSingle($id);
        $painel = getpainelManager()->getPainel(PAINEL_DIAGRAMA_ER);
        $painel->setEntidadeModel($modelEntidade);
        $painel->setController($colunaController);
        $painel->setSolido(true);
        $painel->setTitulo(translateKey("txt_table_view"));
        $colunaController->setOrderBy("id_relacao_datatype,nm_coluna");
        $colunaController->loadRegistros("and coluna.id_entidade_pai=$id", $painel);

        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_entity");
    }

}

?>