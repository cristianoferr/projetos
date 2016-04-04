<?php

class ComponentLoader {

    function geraDiferencaComponentes(Array $arrComponentes, $arrAtual) {
        for ($c = 0; $c < sizeof($arrComponentes); $c++) {
            $comp = $arrComponentes[$c];
            for ($i = 0; $i < sizeof($arrAtual); $i++) {
                $model = $arrAtual[$i];
                $tipoModel = ComponentLoader::getTipoComponenteModel($model);
                $IDComponenteModel = ComponentLoader::getIDComponenteModel($model);
                //writeAdmin("geraDiferencaComponentes : $IDComponenteModel = ".$comp->getId(),$comp->getTipo()." = $tipoModel");
                if (($IDComponenteModel == $comp->getId()) && ($comp->getTipo() == $tipoModel)) {
                    //writeAdmin("");
                    $comp->seleciona();
                }
            }
        }
        return $arrComponentes;
    }

    function loadComponenteProjeto($idProjeto, $idDiagrama, BaseModel $modelTipoDiagrama) {
        $controller = getControllerManager()->getControllerForTable("projeto");
        $model = $controller->loadSingle($idProjeto);
        $component = new ComponentModel($model->getValorCampo(ComponentLoader::getPKDoTipo(TIPO_COMPONENTE_PROJETO)), $model->getValorCampo('nm_entidade'), $modelTipoDiagrama->getValorCampo("flag_projeto"), TIPO_COMPONENTE_PROJETO, null);

        ComponentLoader::loadComponenteEntidade($idProjeto, $component, $idDiagrama, $modelTipoDiagrama);
        ComponentLoader::loadComponenteEntregavel($idProjeto, $component, $idDiagrama, $modelTipoDiagrama);
        return $component;
    }

    function loadComponenteEntregavel($idProjeto, $componentParent, $idDiagrama, BaseModel $modelTipoDiagrama) {
        if ($modelTipoDiagrama->getValorCampo("flag_entregavel") != "T")
            return;
        $table = "entregavel";
        $controller = getControllerManager()->getControllerForTable($table);
        $controller->loadRegistros("and id_projeto=$idProjeto");
        $c = 0;
        while ($model = $controller->next()) {
            if ($c == 0) {
                $componentParent = new ComponentModel(TIPO_COMPONENTE_TITULO, translateKey("txt_deliverables"), "F", TIPO_COMPONENTE_ENTREGAVEL, $componentParent);
            }
            $component = new ComponentModel($model->getValorCampo("id_$table"), $model->getValorCampo("nm_$table"), $modelTipoDiagrama->getValorCampo("flag_entregavel"), TIPO_COMPONENTE_ENTREGAVEL, $componentParent);
            $c++;
        }
    }

    function loadComponenteEntidade($idProjeto, $componentParent, $idDiagrama, BaseModel $modelTipoDiagrama) {
        $controller = getControllerManager()->getControllerForTable("entidade");
        $controller->loadRegistros("and id_projeto=$idProjeto");
        $c = 0;
        while ($model = $controller->next()) {
            if ($c == 0) {
                $componentParent = new ComponentModel(TIPO_COMPONENTE_TITULO, translateKey("txt_entities"), "F", TIPO_COMPONENTE_ENTIDADE, $componentParent);
            }
            $component = new ComponentModel($model->getValorCampo(ComponentLoader::getPKDoTipo(TIPO_COMPONENTE_ENTIDADE)), $model->getValorCampo("nm_entidade"), $modelTipoDiagrama->getValorCampo("flag_entidade"), TIPO_COMPONENTE_ENTIDADE, $componentParent);
            ComponentLoader::loadComponentePropriedade($model->getValorCampo('id_entidade'), $component, $idDiagrama, $modelTipoDiagrama);
            ComponentLoader::loadComponenteFuncao($model->getValorCampo('id_entidade'), $component, $idDiagrama, $modelTipoDiagrama);
            $c++;
        }
    }

    function getArrayTipoComponente() {
        return array(TIPO_COMPONENTE_PROJETO, TIPO_COMPONENTE_ENTIDADE, TIPO_COMPONENTE_CAMADA, TIPO_COMPONENTE_PROPRIEDADE, TIPO_COMPONENTE_FUNCAO, TIPO_COMPONENTE_ENTREGAVEL);
    }

    function loadComponentePropriedade($idEntidade, $componentParent, $idDiagrama, BaseModel $modelTipoDiagrama) {
        if ($modelTipoDiagrama->getValorCampo("flag_propriedade") != "T")
            return;
        $table = "coluna";
        $controller = getControllerManager()->getControllerForTable($table);
        $controller->loadRegistros("and id_entidade_pai=$idEntidade");
        $c = 0;
        while ($model = $controller->next()) {
            if ($c == 0) {
                $componentParent = new ComponentModel(TIPO_COMPONENTE_TITULO, translateKey("txt_properties"), "F", TIPO_COMPONENTE_PROPRIEDADE, $componentParent);
            }
            $component = new ComponentModel($model->getValorCampo("id_$table"), $model->getValorCampo("nm_$table"), $modelTipoDiagrama->getValorCampo("flag_propriedade"), TIPO_COMPONENTE_PROPRIEDADE, $componentParent);
            $c++;
        }
    }

    function loadComponenteFuncao($idEntidade, $componentParent, $idDiagrama, BaseModel $modelTipoDiagrama) {
        if ($modelTipoDiagrama->getValorCampo("flag_funcao") != "T")
            return;
        $table = "funcao";
        $controller = getControllerManager()->getControllerForTable($table);
        $controller->loadRegistros("and id_entidade_pai=$idEntidade");
        $c = 0;
        while ($model = $controller->next()) {
            if ($c == 0) {
                $component = new ComponentModel(TIPO_COMPONENTE_TITULO, translateKey("txt_functions"), "F", TIPO_COMPONENTE_FUNCAO, $componentParent);
            }
            $component = new ComponentModel($model->getValorCampo("id_$table"), $model->getValorCampo("nm_$table"), $modelTipoDiagrama->getValorCampo("flag_funcao"), TIPO_COMPONENTE_FUNCAO, $componentParent);
            $c++;
        }
    }

    function getTipoComponenteModel(BaseModel $model) {
        $arr = ComponentLoader::getArrayTipoComponente();
        foreach ($arr as $tipo) {
            if ($model->getValorCampo(ComponentLoader::getPKDoTipo($tipo)))
                return $tipo;
        }
    }

    function getIDComponenteModel(BaseModel $model) {
        $arr = ComponentLoader::getArrayTipoComponente();
        foreach ($arr as $tipo) {
            $v = ComponentLoader::getPKDoTipo($tipo);
            if ($model->getValorCampo($v))
                return $model->getValorCampo($v);
        }

        //writeAdmin("Tipo desconhecido",$model->getValorCampo('id_componente_diagrama'));
    }

    function getPKDoTipo($tipo) {
        if ($tipo == TIPO_COMPONENTE_PROJETO)
            return "id_projeto_comp";
        if ($tipo == TIPO_COMPONENTE_ENTIDADE)
            return "id_entidade";
        if ($tipo == TIPO_COMPONENTE_CAMADA)
            return "id_camada";
        if ($tipo == TIPO_COMPONENTE_PROPRIEDADE)
            return "id_coluna";
        if ($tipo == TIPO_COMPONENTE_FUNCAO)
            return "id_funcao";
        if ($tipo == TIPO_COMPONENTE_ENTREGAVEL)
            return "id_entregavel";
    }

}

?>
