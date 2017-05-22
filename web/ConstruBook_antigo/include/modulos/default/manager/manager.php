<?

class MainManager {

    private $painelManager;
    private $internationalDict;
    private $descEntidadeDict; //chave:table_id, valor: desc_field
    private $tableManager;
    private $controllerManager;
    private $widgetManager;
    private $painelManagerKnowledge;

    function MainManager() {
        
    }

    function translateKey($key) {
        return $this->getInternational()->translateKey($key);
    }

    function getDescEntidade() {
        if (!isset($this->descTableDict)) {
            $this->descEntidadeDict = new Dicionario("desc_entidade");
        }
        return $this->descEntidadeDict;
    }

    function getTableManager() {
        if (!isset($this->tableManager)) {
            $this->tableManager = new TableManager();
        }
        return $this->tableManager;
    }

    function setWidgetManager($wm) {
        $this->widgetManager = $wm;
    }

    function getWidgetManager() {
        if (!isset($this->widgetManager)) {
            writeErro("Widget Manager não definido!!");
        }
        return $this->widgetManager;
    }

    function getControllerManager() {
        if (!isset($this->controllerManager)) {
            $this->controllerManager = new ControllerManager($this);
        }
        return $this->controllerManager;
    }

    function getInternational() {
        if (!isset($this->internationalDict)) {
            $lingua = $_SESSION['id_lingua'];
            if (!$lingua) {
                $lingua = LINGUA_EN;
            }
            $this->internationalDict = new InternationalDict($lingua);
        }
        return $this->internationalDict;
    }

    function getDefaultLang() {
        return $this->getInternational()->getDefaultLang();
    }

    function getPainelManager() {
        if (!isset($this->painelManager)) {
            $this->painelManager = new PainelManager($this);
        }
        return $this->painelManager;
    }

    function getPainelManagerKnowledge() {
        if (!$this->painelManagerKnowledge) {
            $this->painelManagerKnowledge = new PainelManagerKnowledge();
        }

        return $this->painelManagerKnowledge;
    }

}

?>