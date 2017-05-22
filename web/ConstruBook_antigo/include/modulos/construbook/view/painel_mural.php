<?php

class PainelListaMural extends BasePainel {
    
    function cabecalho() {
        $this->elRoot=criaEl("div")->setClass("lista_mural");
        //write("cabecalho");
    }
    function registro(IModel $model) {
        //write("registro");
        $divMural=criaEl("div",$this->elRoot)->setClass("mural mural_tipo".$model->getValorCampo("id_tipo_mural"))->setValue($model->getDescricao());
    }
    
}
