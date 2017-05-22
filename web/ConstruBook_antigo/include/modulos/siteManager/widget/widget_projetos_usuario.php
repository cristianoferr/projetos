<?

/* Shows a button for login on google's */

//id=usuario
class WidgetProjetosUsuario extends WidgetSiteManager {

    function generate($id) {
        $id = validaNumero($id);
        $projetoController = getControllerForTable("projeto");
        $painelProjetos = getPainelManager()->getPainel(PAINEL_PROJETOS_USUARIO);
        $projetoController->loadRegistros("and id_projeto in (select id_projeto from usuario_projeto where id_usuario=$id)", $painelProjetos);
        $painelProjetos->setController($projetoController);
        return $painelProjetos->generate();
    }

    function getTitle() {
        return translateKey("txt_user_projects");
    }

}

?>