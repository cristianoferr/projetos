<?

class WidgetColunaController extends BaseController {

    function assignField($idSection, $idColuna) {//coberto
        return $this->insereRegistro(array("id_coluna" => $idColuna, "id_widget_section" => $idSection));
    }

    function addColunaNaSection($id_widget_section) {
        $arr = array("id_widget_section" => $id_widget_section);
        $this->insereRegistro($arr);
    }

    function excluirRegistro($id) {//coberto
        validaEscrita("widget_coluna", $id);
        executaSQL("delete from widget_coluna where id_widget_coluna=?", array($id));
    }

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaEscrita("widget_section", $array["id_widget_section"], true);

        if ($this->isColunaInSection($array['id_widget_section'], $array['id_coluna'])) {
            return false;
        }
        $sql = "insert into widget_coluna (id_coluna,id_widget_section,seq_widget_coluna) values (?,?,?)";
        $id = executaSQL($sql, array($array['id_coluna'], $array['id_widget_section'], $this->countForSection($array['id_widget_section'])));
        return $id;
    }

    function isColunaInSection($idWidgetSection, $idColuna) {
        return existsQuery("select * from widget_coluna where id_coluna=? and id_widget_section=? ", array($idColuna, $idWidgetSection));
    }

    function countForSection($id_widget_section) {
        $row = executaQuerySingleRow("select count(*) as tot from widget_coluna where id_widget_section=?", array($id_widget_section));
        return $row['tot'];
    }

    function getProjeto($id) {//coberto
        $key = "wid_col_proj_$id";
        // write("getprojeto...");
        if ($this->getCacheInfo($key)) {
            return $this->getCacheInfo($key);
        }
        $id = validaNumero($id, "id getProjeto contr_wid_coluna");
        // write("poscache... '$id' ");
        $row = executaQuerySingleRow("select id_projeto from widget w,widget_section ws,widget_coluna wc where wc.id_widget_section=ws.id_widget_section and w.id_widget=ws.id_widget and wc.id_widget_coluna=$id");
        //printArray($row);
        // write("result:" . $row['id_projeto'] . " class:" . get_class($row));
        $this->setCacheInfo($key, $row['id_projeto']);
        return $row['id_projeto'];
    }

}

?>