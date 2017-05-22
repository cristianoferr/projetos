<?

define("WIDGET_LOGIN_GOOGLE", 'WID_LOGIN_GOOGLE');
define("WIDGET_PROJETOS_USUARIO", 'WID_PROJETOS_USUARIO');
define("WIDGET_EDIT_USUARIO", 'WID_EDIT_USUARIO');
define("WIDGET_REGISTRO_USUARIO", 'WID_REGISTRO_USUARIO');

class WidgetSiteManager extends WidgetBase {

    function getWidgetsArray() {
        return array(WIDGET_PROJETOS_USUARIO => USUARIO_PESSOAL, WIDGET_EDIT_USUARIO => USUARIO_PESSOAL);
    }

    function getWidgetFor($nome) {
        if ($nome == WIDGET_LOGIN_GOOGLE) {
            return new WidgetLoginGoogle();
        }
        if ($nome == WIDGET_PROJETOS_USUARIO) {
            return new WidgetProjetosUsuario();
        }
        if ($nome == WIDGET_EDIT_USUARIO) {
            return new WidgetEditaUsuario();
        }
        if ($nome == WIDGET_REGISTRO_USUARIO) {
            return new WidgetRegistroUsuario();
        }
    }

    function getVars() {
        $vars = parent::getVars();
        return $vars . "&modulo=" . MODULO_SITE_MANAGER;
    }

}

?>