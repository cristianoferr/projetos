<?

class FeatureCheckController extends BaseController {

    function insereRegistro($array) {
        // printArray($array);
        $sql = "insert into feature_check (nm_feature_check,count_feature_check) values ";
        $sql.="(?,1)";
        $id = executaSQL($sql, $array);

        return $id;
    }

    function getIdFeature($nome) {
        $row = executaQuerySingleRow("select id_feature_check from feature_check where nm_feature_check=?", array($nome));
        if ($row) {
            executaSQL("update feature_check set count_feature_check=count_feature_check+1 where id_feature_check=?", array($row['id_feature_check']));
            return $row['id_feature_check'];
        } else {
            return $this->insereRegistro(array($nome));
        }
    }

}

?>