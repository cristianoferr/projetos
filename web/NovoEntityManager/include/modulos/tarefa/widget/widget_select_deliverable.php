<?

//id=projeto
class WidgetSelectDeliverable extends WidgetTarefa {

    private $entregavelSelecionado;

    function generate($var_name, $redir) {

        $id_entregavel = $_GET[$var_name];

        $delivController = getControllerForTable("entregavel");
        if (!$id_entregavel) {
            $id_entregavel = $delivController->getEntregavelSelecionadoExportar(projetoAtual());
        } else {
            $delivController->atualizaEntregavelSelecionadoExportar(projetoAtual(), $id_entregavel);
        }

        $painel = getPainelManager()->getPainel(PAINEL_SELECT_DELIVERABLE);
        $painel->setRedirLink($redir);
        $colunas = $painel->getColunas();
        foreach ($colunas as $coluna) {
            if ($coluna->getDbName() == "id_entregavel_pai") {
                $coluna->setDefaultValue($id_entregavel);
                $coluna->setCaption(" ");
            }
        }

        $painel->setTitulo($this->getTitle());
        $delivController->loadRegistros("");
        $painel->setController($delivController);

        $this->entregavelSelecionado = $id_entregavel;

        return $painel->generate();
    }

    function show($var_name, $redir) {
        $this->generate($var_name, $redir)->mostra();
        return $this->entregavelSelecionado;
    }

    function getTitle() {//coberto
        return translateKey("txt_deliverable_root");
    }

}

?>