<?php

/*
 * Essa classe é responsável por mostrar detalhes na tela principal
 */

class MainPageAssist {

    private $arrSubColumn;
    private $arrFeaturette;

    function __construct() {//coberto
        $this->arrSubColumn = array();
        $this->arrFeaturette = array();
        $this->iniciaFeaturette();
    }

 

    function registerNowLink() {//coberto
        if (!isGuest()) {
            return;
        }
        /*if (isAbtestAtivo(ABTEST_ROTULO_CRIAR_CONTA, 1)) {
            $elSaida = Out::linkG("login/new", translateKey("txt_create_account") . "  &raquo;", null, "btn btn-info btn-lg");
        } else {
            $elSaida = Out::linkG("login/new", translateKey("txt_sign_up_now") . "  &raquo;", null, "btn btn-info btn-lg");
        }*/
        return $elSaida;
    }

    function criaJumboTron() {//coberto
        $elDivJumbo = elMaker("div")->setClass("jumbotron");
        $eltitulo = elMaker("h1", $elDivJumbo)->setValue(translateKey("main_titulo_chamada"));
        $elSubtitulo = elMaker("p", $elDivJumbo)->setValue(translateKey("main_titulo_sub_texto"));

        $elP = elMaker("p", $elDivJumbo);

        $elP->addElement($linkMore);
        $link = $this->registerNowLink();
        $elP->addElement($link);
        //$elP->addElement($this->sampleProjectLink());

        return $elDivJumbo;
    }

    function listSubColumns($qtd, ElementMaker $elRoot) {
        $arr = $this->arrSubColumn;
        $max = sizeof($arr);
        if ($qtd > $max) {
            $qtd = $max;
        }
        $used = "";

        //  write("$max $qtd");
        for ($c = 0; $c < $qtd; $c++) {
            $r = rand(0, $max - 1);
            $u = "|$r|";
            if (constainsSubstring($used, $u)) {
                $c--;
            } else {
                $used.=$u;
                $this->listSubColumn($r, $elRoot);
            }
        }
    }

    function listSubColumn($pos, ElementMaker $elRoot) {
        $elDiv = elMaker("div", $elRoot)->setClass("col-lg-4");

        $texto = $this->arrSubColumn[$pos];
        if ($texto->getUrlImagem()) {
            $elImg = elMaker("img", $elImg)->setAtributo("src", "getHomeDir() . $texto->getUrlImagem()");
        }
        $elH2 = elMaker("h2", $elDiv)->setValue($texto->getTitulo());
        $elP = elMaker("p", $elDiv)->setValue($texto->getTexto());

        //$link = $texto->geturl();
        //if (!$link) {
        $link = "nyi/" . $texto->getUrl();
        // }
        $elPLink = elMaker("p", $elDiv);
        $elLink = elMaker("a", $elPLink)->setClass("btn btn-default")->setHref(getHomeDir() . $link)->setValue(translateKey("txt_view_details") . " &raquo;");
    }
    



    function iniciaFeaturette() {
        $arr = $this->arrFeaturette;
        if (getLingua() == LINGUA_EN) {
            $this->addTexto($arr, $titulo, $texto);
        }
        if (getLingua() == LINGUA_PT) {
            $this->addTexto($arr, $titulo, $texto);
        }
    }

    function addTexto(&$arr, $titulo, $texto, $url = null, $urlImagem = null) {//coberto
        $t = new Texto($titulo, $texto, $url, $urlImagem);
        array_push($arr, $t);
    }

}

class Texto {

    private $titulo, $texto, $urlImagem, $url;

    function __construct($titulo, $texto, $url = null, $urlImagem = null) {//coberto
        $this->titulo = $titulo;
        $this->texto = $texto;
        $this->urlImagem = $urlImagem;
        $this->url = $url;
    }

    function getUrlImagem() {//coberto
        return $this->urlImagem;
    }

    function getTitulo() {//coberto
        return $this->titulo;
    }

    function getTexto() {//coberto
        return $this->texto;
    }

    function getUrl() {//coberto
        return $this->url;
    }

}

?>
