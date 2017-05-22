<?

/**
 * Widget para editar dados do usuário
 */
//id=usuario
class WidgetEditaUsuario extends WidgetSiteManager {

    function generate($id) {
        $id = validaNumero($id);
        $controller = getControllerForTable("usuario");
        $usuario = $controller->loadSingle($id);
        $painel = getPainelManager()->getPainel(PAINEL_EDICAO_USUARIO);
        $painel->setController($controller);
        $painel->setTitulo($usuario->getValorCampo("nm_usuario"));
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_your_profile");
    }

}

?>