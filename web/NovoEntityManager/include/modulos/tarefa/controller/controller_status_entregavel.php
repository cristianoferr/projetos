<?

class StatusEntregavelController extends BaseController {

    function getOrderSelect() {
        return "1";
    }

    function getStatusEntregavel($id_status) {
        return $this->getFieldFromModel($id_status, "nm_status_entregavel");
    }

}

?>