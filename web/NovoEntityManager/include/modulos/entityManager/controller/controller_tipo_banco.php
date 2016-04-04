<?

class TipoBancoController extends BaseController {

    function isAutoIncrementavel($id_tipo_banco) {
        $id = "isainc_$id_tipo_banco";
        $ret = $this->getCacheInfo($id);
        if (!$ret) {

            $model = $this->loadSingle($id_tipo_banco);
            $ret = $model->getValorCampo("flag_autoincrement");
            $this->setCacheInfo($id, $ret);
        }

        if ($ret == "T")
            return true;
        return false;
    }

}

?>