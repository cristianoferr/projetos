<?

class ABTestController extends BaseController {

    function insereRegistro($array) {
        validaAdmin();

        // printArray($array);
        $sql = "insert into abtest (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);

        $varContr = getControllerForTable("abtest_variacao");
        $varContr->insereRegistro(array("nm_abtest_variacao" => "variação 1", "id_abtest" => $id));
        $varContr->insereRegistro(array("nm_abtest_variacao" => "variação 2", "id_abtest" => $id));

        return $id;
    }

    function getRedirLink($abtest, $text) {
        $elLink = Out::linkG("redir/$abtest", $text);
        return $elLink;
    }

    function getUrlForAbTest($abtest) {
        return $this->getFieldFromModel($abtest, "url_abtest");
    }

    function abTestPageReached($id_pagina) {
        //writeAdmin("abTestPageReached($id_pagina)");
        $id_pagina = validaNumero($id_pagina, "id_pagina em abTestPageReached");
        if (!isGuest()) {
            $filtroGuest = "and flag_guest_only<>'T'";
        }
        $this->loadRegistros("and id_tipo_abtest in (select id_tipo_abtest from tipo_abtest where id_pagina=$id_pagina $filtroGuest)");
        while ($model = $this->next()) {
            writeDebug("ABTest encontrado para pagina $id_pagina ", $model->getValorCampo("id_abtest") . ":" . $model->getValorCampo("nm_abtest"));
            $abtest = $model->getValorCampo("id_abtest");
            $this->incAbtest($abtest);
        }
    }

    function incAbtest($abtest) {
        $var = $this->getVariacaoAtiva($abtest);
        $sessionId = "abcount_$abtest";
        writeDebug("incAbtest($abtest)");
        if (!isset($_SESSION[$sessionId])) {
            $ok = true;
            if (!isGuest()) {
                $ok = $this->contaUsuarioNaVariacao($abtest, $var);
            }


            //("ok:$ok var: $var abtest:$abtest");
            if ($ok) {
                executaSQL("update abtest_variacao set count_abtest_variacao=count_abtest_variacao+1 where cod_abtest_variacao=? and id_abtest=?", array($var, $abtest));
            }
        }
        $_SESSION[$sessionId] = true;
    }

    function contaUsuarioNaVariacao($abtest, $var) {

        $row = executaQuerySingleRow("select flag_counted,au.id_abtest_variacao from abtest_usuario au,abtest_variacao av where au.id_abtest_variacao=av.id_abtest_variacao and av.id_abtest=? and av.cod_abtest_variacao=? and au.id_usuario=?", array($abtest, $var, usuarioAtual()));
        $flag = $row['flag_counted'];
        writeDebug("contaUsuarioNaVariacao: flag: $flag");
        if ($flag != "T") {
            executaSQL("update abtest_usuario set flag_counted='T' where id_abtest_variacao=? and id_usuario=?", array($row['id_abtest_variacao'], usuarioAtual()));
            return true;
        }
        return false;
    }

    function getVariacaoAtiva($abtest) {
        $sessionId = "abtest_$abtest";
        writeDebug("getVariacaoAtiva($abtest)");

        $val = $_SESSION[$sessionId];

        if (!$val) {
            writeDebug("isAbtestAtivo:não existe abtest($abtest) definido em sessão..");
            $val = $this->getVariacaoUsuarioAtual($abtest);
            writeDebug("isAbtestAtivo:valor encontrado: $val");
            $_SESSION[$sessionId] = $val;
        }
        return $val;
    }

    function isAbtestAtivo($abtest, $codinterno) {
        return $this->getVariacaoAtiva($abtest) == $codinterno;
    }

    function pegaVariacaoDoBanco($abtest) {
        //guest nunca vai gravar em banco...
        if (isGuest()) {
            return;
        }
        $row = executaQuerySingleRow("select cod_abtest_variacao from abtest_variacao av,abtest_usuario au where au.id_abtest_variacao=av.id_abtest_variacao and av.id_abtest=? and au.id_usuario=?", array($abtest, usuarioAtual()));
        if ($row)
            return $row["cod_abtest_variacao"];
    }

    function getVariacaoUsuarioAtual($abtest) {
        writeDebug("getVariacaoUsuarioAtual: pegando numero aleatório...");
        $v = $this->pegaVariacaoDoBanco($abtest);
        if ($v)
            return $v;

        $v = $this->escolheVariacaoAleatoria($abtest);
        if (isGuest()) {
            writeDebug("getVariacaoUsuarioAtual: é guest, não gravo em tabela...");
            return $v;
        }

        //Usuario novo no ABTest
        $id_usuario = usuarioAtual();

        //seto testes ab para 1 e 2 para os usuários pessoais para poder testar funcionalidades...
        if (isAdmin())
            $v = 1;
        if ($id_usuario == USUARIO_PESSOAL)
            $v = 2;

        writeDebug("$abtest, $v, $id_usuario");

        executaSQL("insert into abtest_usuario (id_abtest_variacao,id_usuario) values ((select id_abtest_variacao from abtest_variacao where id_abtest=? and cod_abtest_variacao=?) ,?)", array($abtest, $v, $id_usuario));
    }

    function escolheVariacaoAleatoria($abtest) {
        $controllerVariacao = getControllerForTable("abtest_variacao");
        $totVars = $controllerVariacao->getTotalVariacoes($abtest);
        $rand = rand(1, $totVars);
        writeDebug("escolheVariacaoAleatoria: $totVars variações encontradas, $rand escolhido e retornando");
        return $rand;
    }

}

?>