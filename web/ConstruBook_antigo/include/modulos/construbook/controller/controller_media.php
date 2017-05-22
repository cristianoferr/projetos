<?

class MediaController extends BaseController {
    
    function insereRegistro($array) {

        // printArray($array);
        $sql = "insert into fornecedor (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);

        return $id;
    }

   

    function getMediaPath($idMedia) {
        if ($idMedia==null){
            return "";
        }
        return "media/$idMedia";
    }
    
}


class ProdutoMediaController extends BaseController {
    
    function insereRegistro($array) {

        // printArray($array);
        $sql = "insert into fornecedor (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);

        return $id;
    }
   
}


function getMediaPath($idMedia) {
    return media()->getMediaPath($idMedia);
}

function media() {
    return getControllerForTable("media");
}