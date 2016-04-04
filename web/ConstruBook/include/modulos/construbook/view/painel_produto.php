<?php

class PainelListaProduto extends BasePainel {

    function cabecalho() {
        $this->elRoot = criaEl("div")->setClass("");
        //write("cabecalho");
    }

    function registro(IModel $model) {
        //write("registro");
        $divProduto = criaEl("div", $this->elRoot)->setClass("mural row listaProduto");

        $idMedia = $this->controller->getRandomPicture($model->getId());
        $imgPath = getMediaPath($idMedia);
        //if ($imgPath != "") {
            $divImg = criaEl("div", $divProduto)->setClass("col-xs-3 thumb_produto");
            $img= criaEl("img", $divImg)->setClass("thumb_produto")->setSrc($val);
       // }
        $divCorpoProduto= criaEl("div", $divProduto)->setClass("corpoProduto");
        $divNome= criaEl("div", $divCorpoProduto)->setClass("col-md-8");
        $h2Nome= criaEl("h2", $divNome)->setClass("nomeProduto");
        $linkNome= criaEl("a", $h2Nome)->setHref(getHomedir()."fornecedores/".$model->getValorCampo("id_fornecedor")."/produto/".$model->getId())->setValue($model->getDescricao());
        
        $divDesc= criaEl("div", $divCorpoProduto)->setClass("descricaoProduto col-md-8")->setValue($model->getValorCampo("desc_produto"));
        $divFornecedor= criaEl("div", $divCorpoProduto)->setClass("col-md-2 nomeFornecedor");
        $h3Fornecedor= criaEl("h3", $divFornecedor);
        $linkFornecedor= criaEl("a", $h3Fornecedor)->setHref(getHomedir()."fornecedores/".$model->getId())->setValue($model->getValorCampo("nm_fornecedor"));
        
        //TODO: calcular baseado na tabela de calculo
        $divProduto= criaEl("div", $divCorpoProduto)->setClass("col-md-2 precoProduto")->setvalue(convertcash($model->getValorCampo("vlr_unitario"),"R$ "));
    }

}

/*
<div class="mural">
                <div class="row listaProduto ">
                    <div class="col-xs-3 thumb_produto"><img class="thumb_produto" src="logo_empresa.jpg"></div>
                    <div class="corpoProduto ">--$divCorpoProduto
                        <div class="col-md-2">--$divNome
                            <h2 class="nomeProduto">--$h2Nome
                            <a href="produto/1">[Produto]</a>--$linkNome
                             </h2>
                        </div>
                        <div class="descricaoProduto">Descrição Curta1 Descrição Curta1 Descrição Curta1 Descrição 
                            Curta1 Descrição Curta1 Descrição Curta1 Descrição Curta1 Descrição Curta1 </div>--$divDesc
                        <div class="col-md-2 nomeFornecedor">--$divFornecedor
                            <h3 class="">
                                <a href="fornecedor/1">[Fornecedor]</a>--$linkFornecedor
                            </h3></div>
                        <div class="col-md-2 precoProduto">R$ 999,99</div>
                    </div>
                </div>
            </div>
 *  */