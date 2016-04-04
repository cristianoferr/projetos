<?

class ElementoController extends BaseController {

    function insereRegistro($array) {


        $sql = "insert into elemento (id_projeto,id_entidade,id_tarefa,id_entregavel,id_coluna,id_funcao,id_datatype,id_artigo,id_pagina) values ";
        $sql.="(:id_projeto,:id_entidade,:id_tarefa,:id_entregavel,:id_coluna,:id_funcao,:id_datatype,:id_artigo,:id_pagina)";
        $id = executaSQL($sql, $array);

        return $id;
    }

    function excluirElementoPagina($id_pagina) {//coberto
        validaAdmin();
        executaSQL("delete from elemento where id_pagina=?", array($id_pagina));
    }

    function validaPermissao($id_elemento, $id_usuario, $flagEdit) {
        $model = $this->loadSingle($id_elemento);

        if (!$model) {
            writeAdmin("Erro em validaPermissao do elemento: $id_elemento", $model);
            return false;
        }

        $table = "funcao";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "entregavel";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "coluna";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "tarefa";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "pagina";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "entidade";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }
        //$table = "artigo";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "projeto";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        $table = "datatype";
        if ($model->getValorCampo("id_$table") > 0) {
            return checkPerm($table, $model->getValorCampo("id_$table"), $flagEdit);
        }

        //echo "$id_elemento aa:" . $model->getValorCampo("id_$table");
        return false;
    }

    function idForPage($id_pagina) {

        $id_pagina = validaNumero($id_pagina, "id_pagina ElementoController");
        if ($id_pagina == "null") {
            erroFatal("idForPage($id_pagina)");
        }
        $idCache = "el_page_$id_pagina";
        if ($this->getCacheInfo($idCache))
            return $this->getCacheInfo($idCache);

        $this->loadRegistros("and id_pagina=$id_pagina");
        if ($model = $this->next()) {
            $id = $model->getValorCampo("id_elemento");
            $this->setCacheInfo($idCache, $id);
            return $id;
        }

        $id = $this->insereElementoPagina($id_pagina);
        $this->setCacheInfo($idCache, $id);

        return $id;
    }

    function insereElementoPagina($id_pagina) {

        $id_pagina = validaNumero($id_pagina, "id_pagina ElementoController");
        $arr = $this->getEmptyArray();
        $arr['id_pagina'] = $id_pagina;
        return $this->insereRegistro($arr);
    }

    function getEmptyArray() {
        $arr = array("id_projeto" => "null",
            "id_entidade" => "null",
            "id_tarefa" => "null",
            "id_entregavel" => "null",
            "id_coluna" => "null",
            "id_funcao" => "null",
            "id_datatype" => "null",
            "id_artigo" => "null",
            "id_pagina" => "null");
        return $arr;
    }

}

?>