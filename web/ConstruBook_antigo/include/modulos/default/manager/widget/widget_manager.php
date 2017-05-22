<?

class WidgetManager {

    private $arrManagers;

    function __construct() {//coberto
        $this->arrManagers = array();
    }

    function addManager($manager) {//coberto
        array_push($this->arrManagers, $manager);
    }

    function getManagers() {//coberto
        return $this->arrManagers;
    }

    function getWidgetFor($nome) {//coberto
        for ($c = 0; $c < sizeof($this->arrManagers); $c++) {
            $manager = $this->arrManagers[$c];
            $w = $manager->getWidgetFor($nome);
            if ($w) {
                return $w;
            }
        }
        erroFatal("WIDGET não encontrado:" . $nome);
    }

    function showWidget($nome, $id, $arg0 = null) {
        $widget = $this->getWidgetFor($nome);
        if ($widget) {
            return $widget->show($id, $arg0);
        }
    }

    function generateWidget($nome, $id, $arg0 = null) {
        $widget = $this->getWidgetFor($nome);
        if ($widget) {
            return $widget->generate($id, $arg0);
        }
    }

    function getLinkForWidget($nome, $idRegistro, $full = "true") {
        $widget = $this->getWidgetFor($nome);
        $vars = $widget->getVars();
        $token = $this->getNextToken();
        $widget->initToken($token);

        return "widget/?$vars&full=$full&widget=$nome&id=$idRegistro&token=$token";
    }

    //função genérica que cria um botão para um widget
    function generateButtonForWidget($nome, $idRegistro, $flagModal, $buttonClass, $buttonText) {//coberto
        if ($flagModal) {
            $widget = $this->getWidgetFor($nome);
            $vars = $widget->getVars();
            $painel = new PainelModal($nome . "_" . $idRegistro, "", "Loading", translateKey("txt_ok"), $buttonClass);
            $painel->setTitle($widget->getTitle());
            $painel->setUrlConteudo("widget/?$vars&widget=$nome&id=$idRegistro");
            $painel->setHintBotao($buttonText);
            $painel->setIdBotao($nome . "_" . $idRegistro);
            return $painel;
        } else {
            Out::createNewItemForLink($this->getLinkForWidget($nome, $idRegistro), $nome);
        }
    }

    function generateRedirectLinkForWidget($widget, $idRegistro) {//coberto
        return criaEl("a")->setHref(getHomeDir() . "mod_default/widget_viewer?widget=$widget&id=$idRegistro&redir=" . webify(getPaginaAtual()));
    }

    function createLinkButtonForWidget($nome, $idRegistro) {//coberto
        Out::createEditLinkForLink($this->getLinkForWidget($nome, $idRegistro), $idRegistro, $nome);
    }

    function generateEditButtonForWidget($nome, $idRegistro) {//coberto
        return $this->generateButtonForWidget($nome, $idRegistro, true, "glyphicon glyphicon-new-window", translateKey("txt_popup_edit"));
    }

    function createNewButtonForWidgetModal($nome, $idRegistro) {
        return $this->generateButtonForWidget($nome, $idRegistro, true, "glyphicon glyphicon-plus", translateKey("txt_create_new"));
    }

    function createNewButtonForWidget($nome, $idRegistro) {
        return Out::createNewItemForLink($this->getLinkForWidget($nome, $idRegistro), $nome);
    }

    function getNextToken() {
        $v = $_SESSION['widget_token'] + 1;
        $_SESSION['widget_token'] = $v;
        return $v;
    }

}

?>