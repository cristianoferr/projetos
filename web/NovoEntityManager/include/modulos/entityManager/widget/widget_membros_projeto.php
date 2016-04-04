<?

//id=projeto
class WidgetMembrosProjeto extends WidgetEntidade {

    function generate($id, $flagFull = true) {//coberto
        $memberController = getControllerManager()->getControllerForTable("usuario_projeto");

        if ($flagFull) {
            $painel = getPainelManager()->getPainel(PAINEL_MEMBROS_PROJETO);
        } else {
            $painel = getPainelManager()->getPainel(PAINEL_MEMBROS_PROJETO_RESUMO);
        }
        $painel->setTitulo(translateKey("txt_members"));
        $painel->setController($memberController);
        $memberController->loadRegistros("", $painel);
        if (checkPerm("projeto", $id, true)) {
            //$painel->adicionaLink(getHomeDir() . "members/new/$id_projeto", translateKey("txt_new_member"), false);
            //$painel->setDeleteLink("members/delete/" . projetoAtual() . "/");
        }
        if (!$flagFull) {
            $painel->adicionaLink(getHomeDir() . "members/" . $id, translateKey("txt_members"), false);
        }

        return $painel->generate();
    }

    function show($id, $flagFull = true) {//coberto
        $this->generate($id, $flagFull)->mostra();
    }

    function getTitle() {//coberto
        return translateKey("txt_members");
    }

}

?>