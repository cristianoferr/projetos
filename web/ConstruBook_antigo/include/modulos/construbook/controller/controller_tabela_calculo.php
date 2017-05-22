<?

class TabelaCalculoController extends BaseController {

    function insereRegistro($array) {

        // printArray($array);
        $sql = "insert into tabela_calculo (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);

        return $id;
    }

}


?>