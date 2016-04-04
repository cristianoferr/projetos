<?php

include("testPainelMestreDetalhe.php");

class TestsDefault extends TestBase {

    function testWidgetLink() {
        $mng = getWidgetManager();

        $widname = "qualquer_coisa";
        $id = 123;
        $urlEsperado = getHomeDir() . "mod_default/widget_viewer?widget=$widname&id=$id&redir=" . webify(getPaginaAtual());
        $link = $mng->generateRedirectLinkForWidget($widname, $id);
        $url = $link->getAtributo("href");
        $this->assertEqual($url, $urlEsperado, "Url retorno ($url) diferente de ($urlEsperado)");
    }

    function testeWidgets() {
        $arrManagers = getWidgetManager()->getManagers();
        $this->assertNotNull($arrManagers, "Widget Managers null");

        foreach ($arrManagers as $manager) {
            $widgets = $manager->getWidgetsArray();
            $this->assertNotNull($widgets, "widgets vazio para " . get_class($manager));
            $this->verificaWidgets($widgets);
        }
    }

    function testController() {
        $controller = getControllerForTable("lingua");

        $expec = "EN";
        $ret = $controller->getFieldFromModel(LINGUA_EN, "nmcurto_lang");
        $this->assertEqual($ret, $expec, "nmcurto retornou dif: $ret");
        $expec = "English";
        $ret = $controller->getFieldFromModel(LINGUA_EN, "nmlongo_lang");
        $this->assertEqual($ret, $expec, "nmlongo retornou dif: $ret");

        $ret = $controller->getDescricao(LINGUA_EN);
        $this->assertEqual($ret, $expec, "getDescricao retornou dif: $ret");

        $modelVazio = $controller->loadEmptyModel();
        $this->assertTrue(get_class($modelVazio) == "BaseModel", "Classe do modelo vazio diferente do esperado: " . get_class($modelVazio));
    }

    function testaOutput() {
        //createButton($link, $id, $formName, $linkClass = null, $linkText = null)
        $url = "link!";
        $id = "id!";
        $formName = "frm!";
        $texto = "texto!";
        $class = "classe!";

        $link = Out::createEditLinkForLink($url, $id, $formName);
        $this->assertNotNull($link, "link nulo");
        $this->assertEqual($link->getAtributo("href"), getHomeDir() . $url . $id . "#" . ANCORA_INICIO, "href edit diferente: '" . $link->getAtributo("href") . "'");

        $link = Out::createNewItemForLink($url, $formName);
        $this->assertNotNull($link, "link nulo");
        $this->assertEqual($link->getAtributo("href"), getHomeDir() . $url . "#" . ANCORA_INICIO, "href new item diferente: '" . $link->getAtributo("href") . "'");

        $link = Out::createDeleteLinkForLink($url, $id, $formName);
        $this->assertNotNull($link, "link nulo");
        $this->assertEqual($link->getAtributo("href"), getHomeDir() . $url . $id . "#" . ANCORA_INICIO, "href delete diferente: '" . $link->getAtributo("href") . "'");


        //testando link
        $link = Out::linkG($url, $texto, $id, $class);
        $this->assertNotNull($link, "link nulo");
        $this->assertEqual($link->getAtributo("href"), getHomeDir() . $url . "#" . ANCORA_INICIO, "href link diferente: '" . $link->getAtributo("href") . "'");
        $this->assertEqual($link->getAtributo("class"), $class, "link classe diferente: '" . $link->getAtributo("class") . "'");
        $this->assertEqual($link->getAtributo("id"), $id, "link id diferente: '" . $link->getAtributo("id") . "'");
        $this->assertEqual($link->getValue(), $texto, "texto link diferente: '" . $link->getValue() . "'");
    }

    function testeEditWidget() {
        $elLink = getWidgetManager()->generateButtonForWidget(WIDGET_EDIT_COLUNA, 1396, true, "glyphicon glyphicon-new-window", "Quick Edit");
        $this->assertNotNull($elLink, "elLink nulo");
        $elLink = getWidgetManager()->generateButtonForWidget(WIDGET_EDIT_COLUNA, 1396, false, "glyphicon glyphicon-new-window", "Quick Edit");
        // $this->assertNotNull($elLink, "elLink nulo");
    }

    function verificaWidgets($widgets) {
        // write(sizeof($widgets));
        foreach ($widgets as $widgetName => $id) {
            $widget = getWidgetManager()->getWidgetFor($widgetName);
            $this->assertNotNull($widget, "widget '$widgetName' não encontrado");
            try {
                $ret = $widget->generate($id);
            } catch (Exception $e) {
                write("Caught exception on '$widgetName': " . $e->getMessage());
            }
            $this->assertNotNull($ret, "retorno do widget '$widgetName' vazio.");
        }
    }

    function testElementMaker() {

        $el = new ElementMaker("a");

        $this->assertTrue($el->checkIfTagIsOpen("a"), "Tag open: a");
        $this->assertTrue($el->checkIfTagIsOpen("div"), "Tag open: div");
        $this->assertTrue($el->checkIfTagIsOpen("span"), "Tag open: span");
        $this->assertTrue($el->checkIfTagIsOpen("p"), "Tag open: p");
        $this->assertFalse($el->checkIfTagIsOpen("br"), "Tag closed: br");
        $this->assertFalse($el->checkIfTagIsOpen("hr"), "Tag closed: hr");
        $this->assertFalse($el->checkIfTagIsOpen("input"), "Tag closed: input");
        $this->assertFalse($el->checkIfTagIsOpen("link"), "Tag closed: link");

        $valExp = "http://google.com";
        $el->setAtributo("href", $valExp);
        $ret = $el->getAtributo("href");
        $this->assertEqual($valExp, $ret, "element maker: retornou atributo errado: '$ret' ao invés de '$valExp'");

        $valExp = "link";
        $el->setValue($valExp);
        $ret = $el->getValue();
        $this->assertEqual($valExp, $ret, "element maker: retornou valor errado: '$ret' ao invés de '$valExp'");

        $this->assertTrue($el->isTagOpen(), "tag é open mas retornou falso");

        $elBR = new ElementMaker("br");
        $valExp = "<br>";
        $ret = $elBR->html();
        $ret = replaceString($ret, PHP_EOL, "");
        $this->assertEqual($valExp, $ret, "element maker: elBR retornou html errado: '$ret' ao invés de '$valExp'");

        $valExp = "<a href='http://google.com'>link</a>";
        $ret = $el->html();
        $ret = replaceString($ret, PHP_EOL, "");
        $this->assertEqual($valExp, $ret, "element maker: el retornou html errado: '$ret' ao invés de '$valExp'");

        $el->setClass("c1");
        $ret = $el->getAtributo("class");
        $valExp = "c1";
        $this->assertEqual($valExp, $ret, "element maker: setClass retornou valor errado: '$ret' ao invés de '$valExp'");

        $el->addClass("c2");
        $ret = $el->getAtributo("class");
        $valExp = "c1 c2";
        $this->assertEqual($valExp, $ret, "element maker: addClass retornou valor errado: '$ret' ao invés de '$valExp'");
    }

    function testaddElement() {
        $elA = elMaker("a");
        $elB = elMaker("b");
        $elC = null;
        $elD = elMaker("d", $elB);
        $elBr = elMaker("br", $elB);
        $elHr = elMaker("hr", $elB);
        $elInput = elMaker("input", $elB);
        $elE = elMaker("e", $elB)->setValue();
        $elA->addElement();
        $elA->addElement($elB);
        $elA->addElement($elC);
        $saida = $elA->html();
        $saida = replaceString($saida, PHP_EOL, "");

        $saidaEsp = "<a><b><d></d><br><hr><input><e></e></b></a>";
        //$saida = replaceString($saida, "<", "");
        //$saidaEsp = replaceString($saidaEsp, "<", "");
        $this->assertEqual($saidaEsp, $saida, "testaddElement: '$saida' != '$saidaEsp'");
    }

    function testInputMaker() {
        $controller = getControllerForTable("entidade");
        $model = $controller->loadSingle(388);
        $this->assertNotNull($model, "Entidade de Teste retornou nulo");


        $input = new InputBusiness($controller);
        $colunas = $controller->getTable()->getColunas();
        foreach ($colunas as $coluna) {



            $input->setPosAtual($c);
            $input->setFlagAjax(false);
            $input->setColuna($coluna);
            $input->setModel($model);
            $input->setFormID($formID);
            $input->setFlagInclusao(false);
            $input->setPlaceholder($coluna->getCaption());

            $input->criaInput();
        }
    }

    function testInputMakerSets() {
        $class = "klaz";
        $href = "href!";
        $id = "id!";
        $name = "name!";
        $onclick = "onclick!";
        $src = "src!";
        $style = "style!";
        $type = "type!";
        $value = "value";
        $el = elMaker("a")->setClass($class)->setHref($href)->setId($id)->setName($name)->setOnclick($onclick)->setSrc($src)->setStyle($style)->setType($type)->setValue($value);
        $this->assertNotNull($el, "testInputMakerSets criou nulo...");
        $this->assertEqual($el->getAtributo("class"), $class, " atributo class diferente");
        $this->assertEqual($el->getAtributo("href"), $href, " atributo href diferente");
        $this->assertEqual($el->getAtributo("id"), $id, " atributo id diferente");
        $this->assertEqual($el->getAtributo("name"), $name, " atributo name diferente");
        $this->assertEqual($el->getAtributo("onclick"), $onclick, " atributo onclick diferente");
        $this->assertEqual($el->getAtributo("src"), $src, " atributo src diferente");
        $this->assertEqual($el->getAtributo("style"), $style, " atributo style diferente");
        $this->assertEqual($el->getAtributo("type"), $type, " atributo type diferente");
        $this->assertEqual($el->getValue(), $value, " atributo value diferente");
    }

    function testColunaAbstract() {
        $table = getTableManager()->getTabelaComNome("lingua");
        $this->assertNotNull($table, "tabela nula");
        $coluna = new Coluna($table);

        $this->assertNotNull($coluna, "coluna nula");
        $c = 0;

        $vlr = "ABC-" . ( ++$c);
        $coluna->setCaption($vlr);
        $this->assertEqual($vlr, $coluna->getCaption(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setDbName($vlr);
        $this->assertEqual($vlr, $coluna->getDbName(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setFkTable($vlr);
        $this->assertEqual($vlr, $coluna->getFkTable(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setDatatype($vlr);
        $this->assertEqual($vlr, $coluna->getDatatype(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setDbName($vlr);
        $this->assertEqual($vlr, $coluna->getDbName(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setCondicao($vlr);
        $this->assertEqual($vlr, $coluna->getCondicao(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setColuna1($vlr);
        $this->assertEqual($vlr, $coluna->getColuna1(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setColuna2($vlr);
        $this->assertEqual($vlr, $coluna->getColuna2(), "$vlr");
        $vlr = "ABC-" . ( ++$c);
        $coluna->setTipoInput($vlr);
        $this->assertEqual($vlr, $coluna->getTipoInput(), "$vlr");

        $this->assertEqual($table, $coluna->getTable(), "table difere");

        $caption = "caption!";
        $dbname = "dbname!";
        $flagRead = false;
        $tipoInput = TIPO_INPUT_INTEIRO;

        $coluna->iniciaColuna($caption, $dbname, $flagReadOnly, $tipoInput);
        $this->assertEqual($caption, $coluna->getCaption(), "$caption");
        $this->assertEqual($dbname, $coluna->getDbName(), "$dbname<>" . $coluna->getDbName());
        $this->assertEqual($flagRead, $coluna->isReadOnly(), "flagRead 1");
        $flagRead = true;
        $coluna->setReadonly($flagRead);
        $this->assertEqual($flagRead, $coluna->isReadOnly(), "flagRead 2");
        $this->assertEqual($tipoInput, $coluna->getTipoInput(), "$tipoInput");

        $this->assertTrue($coluna->isVisible(), "isVisible");
        $coluna->setInvisible();
        $this->assertFalse($coluna->isVisible(), "!isVisible");
        $coluna->setVisible();
        $this->assertTrue($coluna->isVisible(), "isVisible");
        $this->assertTrue($coluna->isNumber(), "isNumber");

        $coluna->setFlagAllowNull(true);
        $this->assertTrue($coluna->isAllowingNull(), "isAllowingNull");
        $coluna->setFlagAllowNull(false);
        $this->assertFalse($coluna->isAllowingNull(), "isAllowingNull");

        $this->assertFalse($coluna->isTotalizado(), "isTotalizado");
        $coluna->totaliza();
        $this->assertTrue($coluna->isTotalizado(), "isAllowingNull");

        $valor = "valor!";
        $coluna->setDetalhesInclusao(true, false, $valor);
        $this->assertTrue($coluna->isVisibleInclusao(), "isVisibleInclusao");
        $this->assertFalse($coluna->isRequired(), "required");
        $this->assertEqual($valor, $coluna->getDefaultValue(), "getDefaultValue");
        $v = "aa!";
        $coluna->setDefaultValue($v);
        $this->assertEqual($v, $coluna->getDefaultValue(), "setDefaultValue: $v<>" . $coluna->getDefaultValue());
    }

    function testValidaValor() {
        $coluna = new Coluna(getTableManager()->getTabelaComNome("lingua"));
        $coluna->setTipoInput(TIPO_INPUT_DATA);

        $dataInvalida = "2013-02-05T35:40";
        $ret = $coluna->validaValor($dataInvalida);
        $this->assertNotEqual($ret, $dataInvalida, "a data continua invalida");

        $dataValida = "2013-02-05T15:40";
        $ret = $coluna->validaValor($dataValida);
        $this->assertEqual($ret, $dataValida, "a data valida foi alterada: $datavalida");
    }

}

?>
