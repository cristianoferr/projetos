<?

function validaNumero($var, $desc = null) {
    if ($var == "null")
        return $var;
    if ($var == "")
        return "null";
    if (is_numeric($var))
        return $var;
    if (!ctype_digit($var)) {
        if (!isAdmin()) {
            $desc = "";
        }
        erroFatal(" Invalid number: $var   ($desc)");
    }
    return $var;
}

function validaTexto($var) {
    if ($var == "null")
        return $var;
    $var = str_replace("'", "&#39;", $var);
    $var = strip_tags($var);
    $var = mysql_escape_string($var);
    return $var;
}

function validaEmail($var) {

    return validaTexto($var);
}

?>