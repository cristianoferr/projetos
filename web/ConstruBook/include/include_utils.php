<?

//strtolower

function verficaCondicao($valor1, $condicao, $valor2) {
    if ($condicao == CONDITION_EQUALS)
        return $valor1 == $valor2;
    if ($condicao == CONDITION_GREATER)
        return $valor1 > $valor2;
    if ($condicao == CONDITION_EQUAL_GREATER)
        return $valor1 >= $valor2;
    if ($condicao == CONDITION_LOWER)
        return $valor1 < $valor2;
    if ($condicao == CONDITION_EQUAL_LOWER)
        return $valor1 <= $valor2;
    if ($condicao == CONDITION_DIFFERENT)
        return $valor1 != $valor2;

    die("Condition $condicao not found.");
}

function limitaValor($valor, $min, $max, $casas = null) {
    if ($valor > $max) {
        $valor = $max;
    }
    if ($valor < $min) {
        $valor = $min;
    }
    if ($casas) {
        while (strlen($valor) < $casas) {
            $valor = "0" . $valor;
        }
    }
    return $valor;
}

function getUrl($url) {
    $ch = curl_init();
    $timeout = 5;
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, $timeout);
    $data = curl_exec($ch);
    curl_close($ch);
    return $data;
}

function generateLoremIpsum($tamanho) {
    $saida = "";
    $arrIpsum = explode(" ", "sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium totam rem aperiam eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt neque porro quisquam est qui dolorem ipsum quia dolor sit amet consectetur adipisci velit sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem ut enim ad minima veniam quis nostrum exercitationem ullam corporis suscipit laboriosam nisi ut aliquid ex ea commodi consequatur quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur vel illum qui dolorem eum fugiat quo voluptas nulla pariatur");
    $size = sizeof($arrIpsum);
    while (strlen($saida) < $tamanho) {
        $rand = rand(0, $size);
        $saida.=$arrIpsum[$rand] . " ";
        //  write($rand . " " . $arrIpsum[$rand]);
    }
    //echo "generateLoremIpsum: $saida";

    return $saida;
}

function erroFatal($erro) {
    echo "<br>Erro Fatal: $erro";
    array_walk(debug_backtrace(), create_function('$a,$b', 'print "<br /><b>". basename( $a[\'file\'] ). "</b> &nbsp; <font color=\"red\">{$a[\'line\']}</font> &nbsp; <font color=\"green\">{$a[\'function\']} ()</font> &nbsp; -- ". dirname( $a[\'file\'] ). "/";'));

    //var_dump(debug_backtrace());
    die();
}

function constainsSubstring($str, $substr) {
    return (strpos($str, $substr) !== FALSE);
}

function replaceString($str, $substr, $strto) {
    return str_replace($substr, $strto, $str);
}

function addLine($text, $line) {
    $text.=$line . PHP_EOL;
    return $text;
}

function multiexplode($delimiters, $string) {

    $ready = str_replace($delimiters, $delimiters[0], $string);
    $launch = explode($delimiters[0], $ready);
    $arr = array();
    for ($c = 0; $c < sizeof($launch); $c++) {
        $a = $launch[$c];
        if (trim($a)) {
            array_push($arr, trim($a));
        }
    }
    return $arr;
    //ex: $exploded = multiexplode(array(",",".","|",":"),$text);
}

function formatNumber($n, $n_decimals) {
    return ((floor($n) == round($n, $n_decimals)) ? number_format($n) : number_format($n, $n_decimals));
}

function convertcash($num, $currency) {
    return $currency.number_format($num,2,",",".");
}

function getPaginaAtual() {
    $isHTTPS = (isset($_SERVER["HTTPS"]) && $_SERVER["HTTPS"] == "on");
    $port = (isset($_SERVER["SERVER_PORT"]) && ((!$isHTTPS && $_SERVER["SERVER_PORT"] != "80") || ($isHTTPS && $_SERVER["SERVER_PORT"] != "443")));
    $port = ($port) ? ':' . $_SERVER["SERVER_PORT"] : '';
    $url = ($isHTTPS ? 'https://' : 'http://') . $_SERVER["SERVER_NAME"] . $port . $_SERVER["REQUEST_URI"];

    if (strpos($url, '?') == FALSE) {
        $url = $url . "?";
    }

    return $url;
}

function getRealIpAddr() {
    if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
        //check ip from share internet
        $ip = $_SERVER['HTTP_CLIENT_IP'];
    } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
        //to check ip is pass from proxy
        $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
    } else {
        $ip = $_SERVER['REMOTE_ADDR'];
    }
    return $ip;
}

function getGeoLocation() {
    $xml = simplexml_load_file('http://ipinfodb.com/ip_query.php?ip=' . $_SERVER['REMOTE_ADDR']);
    var_dump($xml);
}

function redirect($url) {
    //echo $url;
    header("Location: $url" . "#" . ANCORA_INICIO);
    die();
}

function getLingua() {
    $language = LINGUA_EN;
    if ($_SESSION["lingua_atual"] != '') {
        return $_SESSION["lingua_atual"];
    }
    if (AMBIENTE == "server") {
        if (isset($_SERVER['HTTP_CLIENT_IP'])) {
            $real_ip_adress = $_SERVER['HTTP_CLIENT_IP'];
        } else if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
            $real_ip_adress = $_SERVER['HTTP_X_FORWARDED_FOR'];
        } else {
            $real_ip_adress = $_SERVER['REMOTE_ADDR'];
        }
        $iptolocation = 'http://api.hostip.info/country.php?ip=' . $real_ip_adress;
        $creatorlocation = file_get_contents($iptolocation);

        if ($creatorlocation == "BR") {
            $language = LINGUA_PT;
        }
        if ($creatorlocation == "PT") {
            $language = LINGUA_PT;
        }
    }

    $_SESSION["lingua_atual"] = $language;

    return $language;
}

function inicioDebug() {
    $_SESSION["flagEdicao"] = "true";
    //error_reporting(E_WARNING);
    error_reporting(E_ALL);
    ini_set('display_errors', '1');
}

function fimDebug() {
    unset($_SESSION["flagEdicao"]);
    //error_reporting(E_WARNING);
    error_reporting(E_ERROR);
}

function verificaFlagDebug() {
    if (!isset($_SESSION["flagEdicao"])){
    return false;}
    $flagEdicao = $_SESSION["flagEdicao"];
    //echo "flagEdicao(session): $flagEdicao<br>";
    if ($flagEdicao == ""){
    $flagEdicao = false;}
    //echo "flagEdicao1: $flagEdicao<br>";
    //echo "flagEdicao2: $flagEdicao<br>";

    return $flagEdicao;
}

function webify($s) {
    $s = replaceString($s, "?", "$");
    $s = replaceString($s, "&", "{");
    return $s;
}

function unWebify($s) {
    $s = replaceString($s, "$", "?");
    $s = replaceString($s, "{", "&");
    return $s;
}

function get_caller_info() {
    $c = '';
    $file = '';
    $func = '';
    $class = '';
    $trace = debug_backtrace();
    if (isset($trace[2])) {
        $file = $trace[1]['file'];
        $func = $trace[2]['function'];
        if ((substr($func, 0, 7) == 'include') || (substr($func, 0, 7) == 'require')) {
            $func = '';
        }
    } else if (isset($trace[1])) {
        $file = $trace[1]['file'];
        $func = '';
    }
    if (isset($trace[3]['class'])) {
        $class = $trace[3]['class'];
        $func = $trace[3]['function'];
        $file = $trace[2]['file'];
    } else if (isset($trace[2]['class'])) {
        $class = $trace[2]['class'];
        $func = $trace[2]['function'];
        $file = $trace[1]['file'];
    }
    if ($file != '')
        $file = basename($file);
    $c = $file . ": ";
    //   $c .= ($class != '') ? ":" . $class . "->" : "";
    //  $c .= ($func != '') ? $func . "(): " : "";
    return($c);
}

?>