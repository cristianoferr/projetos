<?
$htmlText = new HtmlText();

function escreveCabecalho() {
    $GLOBALS['htmlText']->escreveCabecalho();
}

function escreveHeader($flagEscreveSideMenu = true) {
    $GLOBALS['htmlText']->escreveHeader($flagEscreveSideMenu);
}

function escreveFooter() {
    $GLOBALS['htmlText']->escreveFooter();
}

class HtmlText {

    function escreveCabecalho() {
        $elRoot = criaEl("");
        $elDoctype = criaEl("!DOCTYPE")->ativaFlag("html")->mostra();
        echo "<html>";
        $elHead = criaEl("head", $elRoot);
        $elMeta = criaEl("meta", $elHead)->setName("viewport")->setAtributo("content", "width=device-width, initial-scale=1.0");
        $elMeta = criaEl("meta", $elHead)->setAtributo("http-equiv", "Content-Type")->setAtributo("content", "text/html; charset=utf-8");
        //$elMeta = criaEl("meta", $elHead)->setAtributo("http-equiv", "Content-Script-Type")->setAtributo("content", "text/javascript");
        //$elMeta = criaEl("meta", $elHead)->setAtributo("http-equiv", "Content-Style-Type")->setAtributo("content", "text/css");
        $elLink = criaEl("link", $elHead)->setAtributo("rel", "shortcut icon")->setHref(getHomeDir() . "images/favicon.png");


        //echo "<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->";
        //echo "<!--[if lt IE 9]>";
        //echo "<script src=\"../../assets/js/html5shiv.js\"></script>";
        //echo "<script src=\"../../assets/js/respond.min.js\"></script>";
        //echo "<![endif]-->";
        getFileManager()->printModulosCSS($elHead);
        getFileManager()->printModulosJS($elHead);

        $elTitle = criaEl("title", $elHead)->setValue($this->getTitle());
        $elHead->mostra();
        echo "<body>";
        $this->escreveMetricasAfterBody();
    }

    function getTitle() {
        $controllerPagina = getControllerForTable("pagina");
        return translateKey("site_name");
    }

    function escreveMetricasFooter() {
        //$elScriptFeedback = ElementMaker::criaScript("startToggle(); BAROMETER.load('xThSCGyydFKKRPknr58hw');");
       // $elScriptFeedback->mostra();

        if (!isExternalUser()) {
            return;
        }
       // $this->escreveAnalysticsFooter();
    }

    function escreveAnalysticsFooter() {
        $script = "googleAnalytics();";
        $script .="document.write(unescape(\"%3Cscript%20src='\"+";
        $script .="(document.location.protocol=='https:'?";
        $script .="\"https://clicktalecdn.sslcs.cdngc.net/www07/ptc/00281527-483c-4f0e-b744-c270c8440311.js\":";
        $script .="\"http://cdn.clicktale.net/www07/ptc/00281527-483c-4f0e-b744-c270c8440311.js\")+\"'%20type='text/javascript'%3E%3C/script%3E\"));";
        $elScript = ElementMaker::criaScript($script);
        $elScript->mostra();
    }

    function escreveMetricasAfterBody() {
        if (!isExternalUser()) {
            return;
        }
        //  $this->escreveFlurry();
        $this->escreveClicktaleBody();
    }

    function escreveClicktaleBody() {
        $elScript = ElementMaker::criaScript("var WRInitTime = (new Date()).getTime();");
        $elScript->mostra();
    }

    function escreveFlurry() {
        $controllerPagina = getControllerForTable("pagina");
        elMaker("script")->setSrc("https://cdn.flurry.com/js/flurry.js")->setType("text/javascript")->mostra();


        $script = "FlurryAgent.startSession(\"VNXW8Q43NB22P8R87TWX\");";
        $script.= "FlurryAgent.setUserId('" . usuarioAtual() . "');";
        $script.= " FlurryAgent.setAppVersion('" . VERSAO_ATUAL . "');";
        $script.= "var page = {}?";
        $script.= "env['page'] = '" . $controllerPagina->getActualPage() . "';";
        $script.= "FlurryAgent.logEvent('page', page);";
        ElementMaker::criaScript($script)->mostra();
    }

    function geraMenuTopo() {
        $menu = new MenuMaker();
        $topMenu = $menu->geraMenuTopo();

        return $topMenu;
    }

    function escreveHeader($flagEscreveSideMenu = true) {
        $this->escreveCabecalho();

        $topMenu = $this->geraMenuTopo();
        $elHeader = criaEl("header");
        $elDivMenu = criaEl("div", $elHeader);
        $topMenu->mostra($elDivMenu);

        if (($flagEscreveSideMenu) && (usuarioAtual() != "")) {
            $this->escreveMenuLateral($elHeader);
            
        }

        $elHeader->mostra();
        ?>
        <section class="container">
            <a id="<? echo ANCORA_INICIO; ?>" class="invisivel"></a>
            <?
        }

        function escreveMenuLateral(ElementMaker $elRoot) {
            if (isGuest()) {
                return;
            }
            //$_SESSION["flagEdicao"]=true;
            $painelManager = getPainelManager();
            $controllerManager = getControllerManager();
            $projetoController = $controllerManager->getControllerForTable("projeto");

            $entidadeController = $controllerManager->getControllerForTable("entidade");
            $memberController = $controllerManager->getControllerForTable("usuario");

            $painel = $painelManager->getPainel(PAINEL_SIDE_MENU);
            $painel->setController($projetoController);
            $projetoController->loadRegistros("and id_projeto in (select id_projeto from usuario_projeto where id_usuario=" . usuarioAtual() . ")", $painel);
            
            $entidadeController->carregaTodos();
            $painel->setControllerFilho($entidadeController);
            
           // $elRoot->addElement($painel->generate());
            
            $entidadeController->carregaNormal();

            $estado = $memberController->getEstadoInterface("menu_projects", "false");
            if ($estado == "false") {
                $elRoot->setScript("alternaSideMenu();");
            }
        }

        function linksRodape(ElementMaker $elRoot) {
            $controller = getControllerForTable("artigo");
            $elRoot->addElement($controller->linkIfOk(5)); //About Us
            $elRoot->addElement($controller->linkIfOk(6)); //Sobre Nós
        }

        function escreveFooter() {
            if (getIDPaginaAtual() != PAGINA_KNOWLEDGE_BASE) {
                getWidgetManager()->showWidget(WIDGET_LISTA_ARTIGOS_PAGINA, getIdPaginaAtual());
            }
            echo "</section>";
            $elFooter = elMaker("footer");
            $elDiv = elMaker("div", $elFooter)->setClass("fundo caixa centro")->setId("bottomMenu");
            $this->linksRodape($elDiv);
            $elFooter->mostra();

            $this->escreveMetricasFooter();
            $this->showPageStats();
            echo "</body> </html>";
        }

        function showPageStats() {
            if (!isAdmin()) {
                return;
            }
            echo "<br><br><br>Queries: " . $GLOBALS["contaQueries"];
            echo "<br>Tables: " . $GLOBALS["contaTables"];
            echo "<br>Includes: " . getFileManager()->countIncludes();

            $c = 0;
            foreach ($_SESSION as $key => $value) {
                $c++;
            }
            echo "<br>Session Vars: " . $c;
            $finish = microtime();
            $finish = explode(' ', $finish);
            $finish = $finish[1] + $finish[0];
            $total_time = round(($finish - $GLOBALS["startTime"]), 4);
            echo '<br>Page generated in ' . $total_time . ' seconds.';
        }

    }
    ?>