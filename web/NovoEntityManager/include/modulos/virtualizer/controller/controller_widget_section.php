<?

class WidgetSectionController extends BaseController {

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaEscrita("widget", $array["id_widget"], true);

        $sql = "insert into widget_section (id_widget,nm_widget_section,seq_widget_section) values (?,?,?)";
        $id = executaSQL($sql, array($array['id_widget'], $array['nm_widget_section'], $this->countForWidget($array['id_widget'])));

        return $id;
    }

    function excluirRegistro($id) {//coberto
        validaEscrita("widget_section", $id);
        executaSQL("delete from widget_coluna where id_widget_section=?", array($id));
        executaSQL("delete from widget_section where id_widget_section=?", array($id));
    }

    function assignField($idSection, $idColuna) {//coberto
        $contr = getControllerForTable("widget_coluna");
        return $contr->assignField($idSection, $idColuna);
    }

    function createSectionFor($id_widget, $name = "") {//coberto
        return $this->insereRegistro(array("id_widget" => $id_widget, "nm_widget_section" => $name));
    }

    function countForWidget($id_widget) {//coberto
        $row = executaQuerySingleRow("select count(*) as tot from widget_section where id_widget=?", array($id_widget));
        return $row['tot'];
    }

    function getWidget($id_widget_section) {
        return $this->getFieldFromModel($id_widget_section, "id_widget");
    }

    function getProjeto($id) {//coberto
        $key = "wid_sec_proj_$id";
        if ($this->getCacheInfo($key)) {
            return $this->getCacheInfo($key);
        }
        $id = validaNumero($id, "id getProjeto contr_wid_section");
        $row = executaQuerySingleRow("select id_projeto from widget w,widget_section ws where w.id_widget=ws.id_widget and ws.id_widget_section=?", array($id));
        $this->setCacheInfo($key, $row['id_projeto']);
        return $row['id_projeto'];
    }

}

?>