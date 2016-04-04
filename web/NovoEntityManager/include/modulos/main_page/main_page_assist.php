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
        $this->iniciaSubColumn();
        $this->iniciaFeaturette();
    }

    function sampleProjectLink() {//coberto
        //$elSaida = Out::linkG("project/135", translateKey("txt_create_account") . "  &raquo;", null, "btn btn-info btn-lg");
        if (isAbtestAtivo(ABTEST_ROTULO_PROJETO, 1)) {
            $elSaida = abtest()->getRedirLink(ABTEST_ROTULO_PROJETO, translateKey("txt_sample_project") . "  &raquo;");
        } else {
            $elSaida = abtest()->getRedirLink(ABTEST_ROTULO_PROJETO, translateKey("txt_sample_project_tour") . "  &raquo;");
        }
        $elSaida->setClass("btn btn-warning btn-lg");
        return $elSaida;
    }

    function registerNowLink() {//coberto
        if (!isGuest()) {
            return;
        }
        if (isAbtestAtivo(ABTEST_ROTULO_CRIAR_CONTA, 1)) {
            $elSaida = Out::linkG("login/new", translateKey("txt_create_account") . "  &raquo;", null, "btn btn-info btn-lg");
        } else {
            $elSaida = Out::linkG("login/new", translateKey("txt_sign_up_now") . "  &raquo;", null, "btn btn-info btn-lg");
        }
        return $elSaida;
    }

    function criaJumboTron() {//coberto
        $elDivJumbo = elMaker("div")->setClass("jumbotron");
        $eltitulo = elMaker("h1", $elDivJumbo)->setValue(translateKey("main_titulo_chamada"));
        $elSubtitulo = elMaker("p", $elDivJumbo)->setValue(translateKey("main_titulo_sub_texto"));

        $elP = elMaker("p", $elDivJumbo);

        $linkMore = Out::linkG("article/13", translateKey("main_learn_more") . "  &raquo;", null, "btn btn-primary btn-lg");
        $elP->addElement($linkMore);
        $link = $this->registerNowLink();
        $elP->addElement($link);
        $elP->addElement($this->sampleProjectLink());

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

    function iniciaSubColumnEN() {//coberto
        $arr = &$this->arrSubColumn;
        $this->addTexto($arr, "Import", "Import your ongoing project's structure using our integrated tool, allowing you to quickly transition to this tool!", "sc1");
        $this->addTexto($arr, "Export", "Export the structure and data using the SQL format, easily done.", "sc2");
        $this->addTexto($arr, "Go Public!", "Do you want to work on a public project? Then open the project so everyone can see what you are doing! (or not, that's up to you)", "sc3");
        $this->addTexto($arr, "Cooperate", "Invite and work in your project with team members, assigning tasks to each member.", "sc4");
        $this->addTexto($arr, "Visualize", "Create and share diagrams of your project, among those you have: WBS charts, entity relationship and more to come (that's a promise)", "sc5");
        $this->addTexto($arr, "Data Entry", "How about inputing data directly into the entity you just created? Yes you can!", "sc6");
        $this->addTexto($arr, "Task control", "Import your ongoing project's structure using our integrated tool, allowing you to quickly transition to this tool!", "sc7");
        $this->addTexto($arr, "Not Just Software", "Although the focus is software, the tools present here can also be used to manage any type project.  ", "sc8");
        return $arr;
    }

    function iniciaSubColumnPT() {//coberto
        $arr = &$this->arrSubColumn;
        $this->addTexto($arr, "Importação", "Importe a estrutura do seu projeto em andamento usando a nossa ferramenta integrada, permitindo que você transicione rapidamente para essa ferramenta", "sc1");
        $this->addTexto($arr, "Export", "Export the structure and data using the SQL format, easily done.", "sc2");
        $this->addTexto($arr, "Go Public!", "Do you want to work on a public project? Then open the project so everyone can see what you are doing! (or not, that's up to you)", "sc3");
        $this->addTexto($arr, "Cooperate", "Invite and work in your project with team members, assigning tasks to each member.", "sc4");
        $this->addTexto($arr, "Visualize", "Create and share diagrams of your project, among those you have: WBS charts, entity relationship and more to come (that's a promise)", "sc5");
        $this->addTexto($arr, "Data Entry", "How about inputing data directly into the entity you just created? Yes you can!", "sc6");
        $this->addTexto($arr, "Task control", "Import your ongoing project's structure using our integrated tool, allowing you to quickly transition to this tool!", "sc7");
        $this->addTexto($arr, "Not Just Software", "Although the focus is software, the tools present here can also be used to manage any type project.  ", "sc8");
        return $arr;
    }

    function iniciaSubColumn() {//coberto
        if (getLingua() == LINGUA_EN) {
            $arr = $this->iniciaSubColumnEN();
        } else if (getLingua() == LINGUA_PT) {
            $arr = $this->iniciaSubColumnPT();
        } else {
            erroFatal("Language " . getLingua() . " not implemented.");
        }
        return $arr;
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
