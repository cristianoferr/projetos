<?

class MuralController extends BaseController {
    
    function insereRegistro($array) {

        // printArray($array);
        $sql = "insert into mural (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);

        return $id;
    }

    
}


function mural() {
    return getControllerForTable("mural");
}