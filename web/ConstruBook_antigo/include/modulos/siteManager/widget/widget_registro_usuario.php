<?

/**
 * Widget para editar dados do usuário
 */
class WidgetRegistroUsuario extends WidgetSiteManager {

    function show($id = null) {
        $painel = getPainelManager()->getPainel(PAINEL_FINC_USUARIO);
        $painel->adicionaInputForm("acao", ACAO_INSERIR);
        $painel->setController(getControllerForTable("usuario"));
        $painel->mostra();
        echo "<center><h1>-- " . translateKey("txt_or") . " --</h1></center>";
        getWidgetManager()->showWidget(WIDGET_LOGIN_GOOGLE, null);
    }

    function getTitle() {
        return translateKey("txt_your_profile");
    }

}

?>