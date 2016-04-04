<?

class WidgetController extends BaseController {

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaEscrita("entidade", $array["id_entidade"], true);

        $sql = "insert into widget (id_entidade,id_painel,id_projeto,nm_widget,title_widget) values (?,?,?,?,?)";
        $id = executaSQL($sql, array($array['id_entidade'], $array['id_painel'], $array['id_projeto'], $array['nm_widget'], $array['title_widget']));

        $controllerSection = getControllerForTable("widget_section");
        $controllerSection->createSectionFor($id);
        return $id;
    }

    function createSection($id_widget, $name) {//coberto
        $controllerSection = getControllerForTable("widget_section");
        return $controllerSection->createSectionFor($id_widget, $name);
    }

    function createPainel($id_widget) {
        $controller = $this;
        if (!$id_widget) {
            erroFatal("Empty widget defined.");
        }
        $controllerPainel = getControllerForTable("painel");
        $modelWidget = $controller->loadSingle($id_widget);
        $sectionController = getControllerForTable("widget_section");
        $sectionController->setDefaultOrderBy("seq_widget_section");
        $sectionController->loadRegistros("and id_widget=$id_widget");
        $virtualController = getControllerForTable("virtual");
        $virtualController->initFromEntity($modelWidget->getValorCampo("id_entidade"));

        $painel = $controllerPainel->createPanelObjectForID($modelWidget->getValorCampo("id_painel"));
        $painel->setTitulo($modelWidget->getValorCampo("title_widget"));
        $painel->setController($virtualController);

        $this->preencheColunasPainel($painel, $sectionController);

        return $painel;
    }

    function preencheColunasPainel(IPainel $painel, IController $sectionController) {
        $colunaController = getControllerForTable("widget_coluna");
        $colController = getControllerForTable("coluna");

        while ($modelSection = $sectionController->next()) {
            $painel->adicionaAba($modelSection->getId(), $modelSection->getDescricao());

            $colunaController->loadRegistros("and id_widget_section=" . $modelSection->getId());
            while ($modelColuna = $colunaController->next()) {
                $name = $colController->getFieldFromModel($modelColuna->getValorCampo("id_coluna"), "dbname_coluna");
                $painel->addColunaWithDBName($name, $modelSection->getId());
            }
        }
    }

    function excluirRegistro($id) {//coberto
        $id = validaNumero($id);
        validaEscrita("widget", $id);
        executaSQL("delete from widget_section where id_widget=?", array($id));
        executaSQL("delete from widget where id_widget=?", array($id));
    }

    function getPainel($id_widget) {//coberto
        return $this->getFieldFromModel($id_widget, "id_painel");
    }

    function getProjeto($id_entidade) {//coberto
        return $this->getFieldFromModel($id_entidade, "id_projeto");
    }

}

?>