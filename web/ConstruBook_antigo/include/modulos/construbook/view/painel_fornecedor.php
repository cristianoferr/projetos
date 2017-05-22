<?php

class PainelFornecedor extends BasePainel {

    var $fornecedor;

    function __construct() {
        
    }

    function generate() {
            
        $controller = $this->controller;
        $fornecedor = $this->fornecedor;
        $id = $fornecedor->getId();
        $linkRoot=getHomeDir() . "fornecedores/$id";
        $nm = $fornecedor->getDescricao();
        $elRoot = elMaker("div")->setClass("marketing pagina_fornecedor navbar");

        $logoPath = getMediaPath($fornecedor->getValorCampo(FornecedorController::$CAMPO_MEDIA_LOGO));
        if ($logoPath == "") {
            
        } else {
            $elDivLogo = elMaker("div", $elRoot);
            $elLogo = elMaker("img", $elDivLogo)->setClass("logo_fornecedor")->setSrc($logoPath);
        }
        $elDivCorpo = elMaker("div", $elRoot);
        $elUl = elMaker("ul", $elDivCorpo)->setClass("nav navbar-nav mural");
        $elLi = elMaker("li", $elUl);
        $elH2 = elMaker("h2", $elLi)->setClass("nomeFornecedorMural")->setValue($nm);
        $elLi = elMaker("li", $elUl);
        $elA = elMaker("a", $elLi)->setValue(translateKey("txt_mural"))->setHref("$linkRoot/mural");
        if ($controller->usuarioPodeListarProdutosDoFornecedor($id)) {
            $elLi = elMaker("li", $elUl);
            $elA = elMaker("a", $elLi)->setValue(translateKey("txt_produtos"))->setHref("$linkRoot/produtos");
        }
        if ($controller->usuarioPodeListarParceirosDoFornecedor($id)) {
            $elLi = elMaker("li", $elUl);
            $elA = elMaker("a", $elLi)->setValue(translateKey("txt_parceiros"))->setHref("$linkRoot/parceiros");
        }

        $elDivRight = elMaker("div", $elDivCorpo)->setClass("navbar-right");
        $elUl = elMaker("ul", $elDivRight)->setClass("nav navbar-nav mural");
        if (!$controller->usuarioAtualAfiliadoFornecedor($id)) {
            $elLi = elMaker("li", $elUl);
            $elA = elMaker("a", $elLi)->setValue(translateKey("txt_solicitar_acesso"))->setHref("$linkRoot/solicitarAcesso");
        }
        if (!$controller->fornecedorAtualParceiroFornecedor($id)) {
            $elLi = elMaker("li", $elUl);
            $elA = elMaker("a", $elLi)->setValue(translateKey("txt_solicitar_parceria"))->setHref("$linkRoot/solicitarParceria");
        }
        return $elRoot;
    }

    function mostra() {
        $this->generate()->mostra();
    }

    function registro(IModel $model) {
        $this->fornecedor = $model;
    }

    /*
      <div class=" marketing pagina_fornecedor navbar">
      <div>
      <img class="logo_fornecedor" src="logo_empresa.jpg">
      </div>
      <div>
      <ul class="nav navbar-nav mural">
      <li><h2 class="nomeFornecedorMural">[Nome Aqui]</h2></li>
      <li><a href="#">Mural</a></li>
      <li><a href="#">Produtos</a></li>
      <li><a href="#">Parceiros</a></li>
      </ul>
      <div class="navbar-right">
      <ul class="nav navbar-nav mural">
      <li><a href="#">Solicitar Acesso (C)</a></li>
      <li><a href="#">Solicitar Parceria (F)</a></li>
      </ul>
      </div>
      </div>
      </div>
     */
}
