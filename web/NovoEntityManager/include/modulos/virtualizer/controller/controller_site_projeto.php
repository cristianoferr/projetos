<?

class SiteProjetoController extends BaseController {

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaEscrita("projeto", $array["projeto"], true);
        $model = $this->loadSingle($array["projeto"]);
        if ($model) {
            return;
        }
        $sql = "insert into site_projeto (id_projeto,label_projeto) values (?,?)";
        $id = executaSQL($sql, array($array['projeto'], $array['label_projeto']));

        return $id;
    }

    function getDescricao($id) {//coberto
        $desc = parent::getDescricao($id);
        if ($desc) {
            return $desc;
        }

        $key = "desc_site_" . $id;
        if ($this->getCacheInfo($key)) {
            return $this->getCacheInfo($key);
        }

        $controller = getControllerForTable("projeto");
        $desc = $controller->getDescricao($id);
        $this->insereRegistro(array("projeto" => $id, "label_projeto" => $desc));
        $this->setCacheInfo($key, $desc);
        return $desc;
    }

}

?>