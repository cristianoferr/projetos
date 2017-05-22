<?

class ControllerManager_ABTest implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "abtest") {
            return $this->getABTestController();
        }
        if ($tableName == "tipo_abtest") {
            return $this->getTipoABTestController();
        }
        if ($tableName == "feature_check") {
            return $this->getFeatureCheckController();
        }
        if ($tableName == "abtest_variacao") {
            return $this->getABTestVariacaoController();
        }
    }

    function getABTestController() {
        $table = getTableManager()->getTabelaComNome("abtest");
        $controller = new ABTestController($table, "abtests", "abtest", new LinkView("Novo ABTest", "abtest/new"));
        return $controller;
    }

    function getFeatureCheckController() {
        $table = getTableManager()->getTabelaComNome("feature_check");
        $controller = new FeatureCheckController($table, "feature_checks", "feature_check");
        return $controller;
    }

    function getABTestVariacaoController() {
        $table = getTableManager()->getTabelaComNome("abtest_variacao");
        $controller = new ABTestVariacaoController($table);
        return $controller;
    }

    function getTipoABTestController() {
        $table = getTableManager()->getTabelaComNome("tipo_abtest");
        $controller = new TipoABTestController($table);
        return $controller;
    }

}

?>