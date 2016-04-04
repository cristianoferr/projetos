<?

/*
  Classe que gerencia os paineis da aplicação.  Um painel é um quadro que permite a visualização e edição dos dados
 */

class PainelManager {

    private $manager;

    function PainelManager($manager) {
        $this->manager = $manager;
    }

    function getTableManager() {
        return $this->manager->getTableManager();
    }

    function getPainel($name) {

        $tm = getFileManager()->carregaPainelComNome($name);
        if ($tm) {
            $GLOBALS["contaPaineis"] = $GLOBALS["contaPaineis"] + 1;
            return $tm;
        }
        erroFatal("Painel $name desconhecido!");
    }

    function getBread() {
        if ($GLOBALS['bread']) {
            return $GLOBALS['bread'];
        }
        $b = new Breadcrumb();
        $b->addLink(translateKey("main_titulo_chamada"), "");
      /*  $b->addLink(translateKey("txt_my_projects"), "projects/" . projetoAtual());
        if (projetoAtual() != "") {
            $id_projeto = projetoAtual();
            $projController = getControllerManager()->getControllerForTable('projeto');
            $nm_projeto = $projController->getDescricao($id_projeto);
            $b->addLink($nm_projeto, "project/" . $id_projeto);
        }

        if (entidadeAtual() != "") {
            $id_entidade = entidadeAtual();
            $entController = getControllerManager()->getControllerForTable('entidade');
            $nm = $entController->getDescricao($id_entidade);
            $b->addLink($nm, "entity/" . $id_projeto . "/$id_entidade");
        }

        if (getIDPaginaAtual() == PAGINA_ABTEST) {
            $b->addLink("Testes AB", "abtest");
        }
*/
        $GLOBALS['bread'] = $b;

        return $b;
    }

}

?>