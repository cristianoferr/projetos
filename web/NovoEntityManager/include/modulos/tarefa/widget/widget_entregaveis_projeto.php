<?

//id=projeto
class WidgetEntregaveisProjeto extends WidgetTarefa {

    private $flagResumo;

    function __construct($flagResumo) {
        parent::__construct();
        $this->flagResumo = $flagResumo;
    }

    function getFiltroFor($filtro) {
        if ($filtro == "open") {
            $filtroSQL = " and id_status_entregavel<=" . LIMITE_STATUS_ENTREG_ABERTO;
        }
        if ($filtro == "closed") {
            $filtroSQL = " and id_status_entregavel>" . LIMITE_STATUS_ENTREG_ABERTO;
        }
        if ($filtro == "canceled") {
            $filtroSQL = " and id_status_entregavel=" . STATUS_ENTREG_CANCELADO;
        }
        if ($filtro == "notstarted") {
            $filtroSQL = " and id_status_entregavel<=" . STATUS_ENTREG_APROVADO;
        }
        if ($filtro == "started") {
            $filtroSQL = " and id_status_entregavel<=" . LIMITE_STATUS_ENTREG_ABERTO . " and id_status_entregavel>=" . STATUS_ENTREG_DESENVOLVIMENTO;
        }
        return $filtroSQL;
    }

    function generate($id) {
        if ($this->flagResumo) {
            $painel = $this->showResumo($id);
        } else {
            $painel = $this->showCompleto($id);
        }

        return $painel->generate();
    }

    function showCompleto($id_projeto) {
        $filtro = $_GET['filtro'];
        $userController = getControllerForTable("usuario");
        $controller = getControllerForTable("entregavel");

        if ($filtro) {
            $userController->atualizaInterfaceUsuario(FILTRO_ENTREGAVEL, $filtro);
        } else {
            $filtro = $userController->getEstadoInterface(FILTRO_ENTREGAVEL, "all");
        }
        $filtroSQL = $this->getFiltroFor($filtro);

        $painel = getPainelManager()->getPainel(PAINEL_ENTREGAVEIS);
        $painel->setController($controller);
        $controller->loadRegistros("and entregavel.id_projeto=$id_projeto $filtroSQL and id_entregavel_pai>0", $painel);
        if (checaEscrita("projeto", $id_projeto)) {
            $painel->adicionaLink(getHomeDir() . "deliverables/new/$id_projeto", translateKey("txt_new_deliverable"), false);
        }

        $painel->adicionaLinkImportante(getHomeDir() . "project/$id_projeto", translateKey("txt_back"), false);
        return $painel;
    }

    function showResumo($id_projeto) {
        $painel = getPainelManager()->getPainel(PAINEL_ENTR_PROJ);
        $painel->setTitulo(translateKey("txt_deliverables"));
        $entregavelController = getControllerForTable("entregavel");
        $painel->setController($entregavelController);
        $entregavelController->loadRegistros(" and id_entregavel_pai>0", $painel);
        if (checkPerm("projeto", $id_projeto, true)) {
            $painel->adicionaLink(getHomeDir() . "deliverables/new/$id_projeto", translateKey("txt_new_deliverable"), false);
        }
        $painel->adicionaLink(getHomeDir() . "deliverables/$id_projeto/G", translateKey("txt_go_to_deliverables"), false);
        return $painel;
    }

    function getTitle() {
        return translateKey("txt_deliverables");
    }

}

?>