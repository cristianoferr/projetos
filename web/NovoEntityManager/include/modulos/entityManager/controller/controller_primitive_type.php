<?

class PrimitiveTypeController extends BaseController {

    function getOrderBy() {
        return "id_meta_type,nm_primitive_type";
    }

    function getOrderSelect() {
        return "id_meta_type,nm_primitive_type";
    }

    function getCampoExtraSelect() {
        return "(select nm_meta_type from meta_type m where m.id_meta_type=primitive_type.id_meta_type) as nm_metatype";
    }

    function getGroupBySelect() {
        return "nm_metatype";
    }

    function isTexto($id_primitive_type) {
        $v = $this->getFieldFromModel($id_primitive_type, 'flag_texto');
        if ($v == "T")
            return true;
        return false;
    }

}

?>