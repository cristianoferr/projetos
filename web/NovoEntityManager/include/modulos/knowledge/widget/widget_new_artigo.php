<?

//id=elemento
class WidgetNewArtigo extends WidgetKnowledge {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("artigo");
        $painel = getPainelManager()->getPainel(PAINEL_NOVO_ARTIGO);
        $painel->setController($controller);
        $painel->setModal($this->isModal());
        $token = $_GET['token'];
        $this->setToken($token);
        //$painel->adicionaLinkImportante(getHomeDir()."tasks/".projetoAtual(),txt_back,false);
        $painel->setTitulo($this->getTitle());
        $painel->adicionaInputForm("pagina", $this->getPaginaReferrer());
        $painel->adicionaInputForm("elemento", $id);
        $painel->adicionaLink($this->getPaginaReferrer(), translateKey("txt_back"), false);
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_new_article");
    }

}

?>