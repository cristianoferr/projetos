<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_ABTEST);
inicializa();
definePaginaAtual(PAGINA_ABTEST);

$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("abtest");

validaAdmin();

if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_ABTEST);
    $arr = $painel->getArrayWithPostValues();

    $id_abtest = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "abtest/$id_abtest");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($id_entidade);
    redirect(getHomeDir() . "abtest");
}

escreveHeader();
$bread = $painelManager->getBread();
$mostraBread = novoABTest($controller, $painelManager, $bread, $acao);


if ($mostraBread)
    $mostraBread = editaABTest($controller, $painelManager, $bread);



if ($mostraBread) {
    $bread->mostra();
    $painel = $painelManager->getPainel(PAINEL_ABTESTS);
    $painel->setController($controller);
    $painel->setTitulo("Testes AB");
    $controller->loadRegistros("", $painel);
    $painel->adicionaLink(getHomeDir() . "abtest/new", "Novo ABTest", false);
    $painel->mostra();
}

function editaABTest($controller, $painelManager, $bread) {
    $abtest = $_GET['abtest'];
    if ($abtest) {
        $painel = $painelManager->getPainel(PAINEL_ABTEST);
        $model = $controller->loadSingle($abtest, $painel);
        $painel->setTitulo($model->getDescricao());
        $painel->setController($controller);


        $bread->addLink($model->getDescricao(), "abtest/$abtest");
        $bread->mostra();
        $painel->mostra();

        mostraExemplos($abtest);

        mostraVariacaoes($abtest);
        return false;
    }
    return true;
}

function mostraExemplos($abtest) {
    ?><div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title">Exemplo de Uso</h3>
        </div>
        <div class="panel-body">
            <div class="alert alert-success">Visualização condicional:</div>

            if (isAbtestAtivo(<? echo $abtest; ?>,1)){<br>
            $link="redir/<? echo $abtest; ?>";<br>
            } else {<br>
            $link="redir/<? echo $abtest; ?>";<br><br>
            }<br>
            <div class="alert alert-success">Incrementando Contadores:</div>
            incAbtest(<? echo $abtest; ?>);<br>

            <div class="alert alert-success">Incrementando Redirecionadores:</div>
            Link para "redir/$abtest":
            abtest()->getRedirLink($abtest, $text);
        </div>
    </div><?
}

function mostraVariacaoes($abtest) {
    $painel = getPainelManager()->getPainel(PAINEL_VARIACOES_ABTEST);
    $controller = getControllerForTable("abtest_variacao");
    $painel->setController($controller);
    $painel->setTitulo("Testes AB");
    $controller->loadRegistros("and id_abtest=$abtest", $painel);
    $painel->adicionaLink(getHomeDir() . "abtest/variacao/new/$abtest", "Nova variação", false);
    $painel->mostra();
}

function novoABTest($controller, $painelManager, $bread, $acao) {
    if ($acao == ACAO_NOVO) {
        $painel = $painelManager->getPainel(PAINEL_FORM_INC_ABTEST);
        $painel->setTitulo("Novo ABTest");
        $painel->setController($controller);

        $bread->addLink("Novo ABTest", "abtest/new");
        $bread->mostra();
        $painel->mostra();
        return false;
    }
    return true;
}

escreveFooter();
?>