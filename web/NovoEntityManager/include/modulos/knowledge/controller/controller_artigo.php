<?

class ArtigoController extends BaseController {

    function insereRegistro($array, $html) {//coberto
        //echo "pagina: ".$array['pagina'];
        if (!checkPerm("elemento", $array['elemento'], true)) {
            writeErro("User cant write at element: " . $array['elemento']);
            return;
        }
        //printArray($array);

        $sql = "insert into artigo (id_elemento,id_lingua,title_artigo,texto_artigo) values (?,?,?,?)";
        $id = executaSQL($sql, array($array['elemento'], $array['id_lingua'], $array['title_artigo'], $html));

        return $id;
    }

    function linkIfOk($id_artigo, $lingua = null, $pref = null) {//coberto
        if (!$lingua) {
            $lingua = getLingua();
        }

        $id_exists = $pref . "cache_isok_$id_artigo";
        $id_text = $pref . "text_$id_artigo";
        $v = $this->getCacheInfo($id_exists);
        if ($v == "T") {
            $title = $this->getCacheInfo($id_text);
            //return true;
        } else if ($v == "F") {
            return;
        } else {
            //  write("id_artigo:$id_artigo lingua:$lingua");
            $model = $this->loadSingle($id_artigo);
            if ($model->getValorCampo("id_lingua") == $lingua) {
                $this->setCacheInfo($id_exists, "T");
                $title = $model->getValorCampo("title_artigo");
                $this->setCacheInfo($id_text, $title);
            } else {
                $this->setCacheInfo($id_exists, "F");
                return;
            }
        }

        return Out::linkG("article/$id_artigo", $title);
    }

    function excluirRegistro($id) {//coberto
        $id = validaNumero($id);
        validaEscrita("artigo", $id, true);
        executaSQL("delete from artigo where id_artigo=?", array($id));
    }

    function validaPermissao($id, $id_usuario, $flagEdit) {//coberto
        $model = $this->loadSingle($id);
        $id_elemento = $model->getValorCampo("id_elemento");
        return getControllerForTable("elemento")->validaPermissao($id_elemento, $id_usuario, $flagEdit);
    }

}

?>