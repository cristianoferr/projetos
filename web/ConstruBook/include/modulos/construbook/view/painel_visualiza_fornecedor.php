<?php

class PainelListaFornecedor extends BasePainel {

    function cabecalho() {
        $this->elRoot=criaEl("div")->setClass("lista_mural");
        //write("cabecalho");
    }
    function registro(IModel $model) {
        //write("registro");
        $divMural=criaEl("div",$this->elRoot)->setClass("row linhaFornecedor");
        $div=criaEl("div",$divMural)->setClass("col-md-3");
        $link=criaEl("a",$div)->setClass("")->setHref(getHomeDir()."fornecedores/".$model->getId())->setValue($model->getDescricao());
        $divDesc=criaEl("div",$divMural)->setClass("col-lg-6")->setValue($model->getValorCampo("texto_fornecedor"));
    }
}
