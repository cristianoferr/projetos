<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
adicionaModuloCarga(MODULO_DIAGRAMAS);
adicionaModuloCarga(MODULO_TAREFAS);
write("update...");
inicializa();
//atualiza coluna com novo valor
if ($_POST["id_coluna"] != "") {
    $id_coluna = $_POST["id_coluna"];
    $campo = $_POST["campo"];
    $novoValor = utf8_urldecode($_POST["novoValor"]);
    $flagtexto = $_POST["flagtexto"];
    if ($flagtexto == "T") {
        $novoValor = "'$novoValor'";
    }
    executaSQL("update coluna set $campo=$novoValor where id_coluna=$id_coluna");
}


if ($_POST["modulo"] != "") {
    $idreg = $_POST["idreg"];
    $modulo = $_POST["modulo"];
    $campo = $_POST["campo"];

    $novoValor = $_POST["novoValor"]; //urldecode($_POST["novoValor"]);
    $flagtexto = $_POST["flagtexto"];
    if ($flagtexto == "T") {
        //	$novoValor="'$novoValor'";
    }
    $chave = "";

    if ($modulo == "posicao_entidade") {
        $idcomponente = $_POST["idComponente"];
        $id_projeto = $_POST["idprojeto"];
        $id_diagrama = $_POST["iddiagrama"];
        $posX = $_POST["posX"];
        $posY = $_POST["posY"];
        if ($posX < MIN_WIDTH)
            $posX = MIN_WIDTH;
        if ($posY < MIN_HEIGHT)
            $posY = MIN_HEIGHT;
        if ($posX > MAX_WIDTH)
            $posX = MAX_WIDTH;
        if ($posY > MAX_HEIGHT)
            $posY = MAX_HEIGHT;
        validaEscrita("projeto", $id_projeto);
        //addLog("pos_diag_$id_diagrama,$id_diagrama... $id_entidade,$posX,$posY");
        $posicaoDict = new PosicaoDiagrama("pos_diag_" . $id_diagrama, $id_diagrama);
        $posicaoDict->updatePosicao($idcomponente, $posX, $posY);
        echo "ok pos $idcomponente $id_projeto $id_diagrama: x:$posX y:$posY";
        die();
    }


    $controller = getControllerForTable($modulo);
    write("idReg:$idreg campo:$campo novoValor:$novoValor");
    $controller->atualizaValor($idreg, $campo, $novoValor);

    /*
      addLog("antes validaEscrita($modulo,$idreg)");
      validaEscrita($modulo,$idreg);
      $sql="update $modulo set $campo=? where $chave=?";
      addLog("(".usuarioAtual().")".$sql." valor: $novoValor idreg:$idreg");
      //echo $sql;
      executaSQL($sql,array($novoValor,$idreg)); */

    if ($modulo == "coluna") {
        $controller->verificaDados();
    }
    echo "ok";
}


//atualiza valor da celula
if ($_POST["valorid"] != "") {
    $valorid = $_POST["valorid"];
    $novoValor = urldecode($_POST["novoValor"]);
    executaSQL("update valor set valor_coluna='$novoValor' where id_valor=$valorid");
}

function utf8_urldecode($str) {
    $str = preg_replace("/%u([0-9a-f]{3,4})/i", "&#x\\1;", urldecode($str));
    return html_entity_decode($str, null, 'UTF-8');
    ;
}

?>