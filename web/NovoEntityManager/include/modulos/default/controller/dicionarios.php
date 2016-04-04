<?

class DicionarioSession {

    //private $arrStrings;
    private $name;

    function DicionarioSession($name) {
        $this->name = $name;
        //$this->arrStrings=array();
    }

    /*
      function setValor($key,$valor){
      //$this->arrStrings[$key]=$valor;
      }
      function getValor($key){

      //return $this->arrStrings[$key];
      } */

    function getValor($key) {
        if (!isset($_SESSION[$this->name . "_$key"]))
            return false;
        return $_SESSION[$this->name . "_$key"];
    }

    function setValor($key, $valor) {
        $_SESSION[$this->name . "_$key"] = $valor;
    }

}

class Dicionario {

    private $arrStrings;
    private $arrAuxiliar;

    function Dicionario() {
        $this->arrStrings = array();
        $this->arrAuxiliar = array();
    }

    function setValor($key, $valor, $aux = null) {
        $this->arrStrings[$key] = $valor;
        if ($aux) {
            $this->arrAuxiliar[$key] = $aux;
        }
    }

    function getValor($key) {
        return $this->arrStrings[$key];
    }

    function getAuxiliar($key) {
        if (isset($this->arrAuxiliar[$key])) {
            return $this->arrAuxiliar[$key];
        }
    }

    function count() {
        return sizeof($this->arrStrings);
    }

    function getArray() {
        return $this->arrStrings;
    }

}

class InternationalDict extends DicionarioSession {

    private $id_lang;

    function InternationalDict($id_lang) {
        $this->id_lang = $id_lang;
        $this->DicionarioSession("INTER_$id_lang");
    }

    function language() {
        return $this->id_lang;
    }

    function getDefaultLang() {
        return $this->id_lang;
    }

    function translateKey($int_txt) {
        $int_txt = validaTexto($int_txt);
        if (!$this->getValor($int_txt)) {
            $lang = $this->language();
            //$lang = LINGUA_IPSUM;
            $l = $lang;
            if ($lang == LINGUA_IPSUM)
                $l = 1;
            $row = executaQuerySingleRow("select nm_valor_lingua from valor_lingua v where v.nm_chave_lingua=? and v.id_lingua=?", array($int_txt, $l));
            if ($row) {
                if ($lang == LINGUA_IPSUM) {
                    $txt = generateLoremIpsum(strlen($row['nm_valor_lingua']));
                } else {
                    $txt = $row['nm_valor_lingua'];
                }
                $txt = replaceString($txt, "\n", "");
                $txt = replaceString($txt, "\r", "");
                $txt = replaceString($txt, "\\", "");
                $this->setValor($int_txt, $txt);
                return $txt;
            } else {
                //echo "$int_txt não encontrado na base.<br>";
                if (isAdmin()) {
                    echo "insert into chave_lingua values (\"$int_txt\");insert into valor_lingua(id_lingua,nm_chave_lingua,nm_valor_lingua) values (1,\"$int_txt\",\"ENG\");  insert into valor_lingua(id_lingua,nm_chave_lingua,nm_valor_lingua) values (2,\"$int_txt\",\"PT\");<br>";
                    erroFatal("");
                }
                //die();
            }
        }
        return $this->getValor($int_txt);
    }

}

class PosicaoDiagrama extends DicionarioSession {

    private $id_diagrama;

    function PosicaoDiagrama($cod_dict, $id_diagrama) {
        DicionarioSession::DicionarioSession($cod_dict);
        $this->id_diagrama = $id_diagrama;
    }

    function getPosX($id_componente) {
        $pos = DicionarioSession::getValor($id_componente . "_x");
        if (!$pos) {
            $this->pegaPosicaoBanco($id_componente);
        }
        $pos = DicionarioSession::getValor($id_componente . "_x");
        return $pos;
    }

    function getPosY($id_componente) {
        $pos = DicionarioSession::getValor($id_componente . "_y");
        if (!$pos) {
            $this->pegaPosicaoBanco($id_componente);
        }
        $pos = DicionarioSession::getValor($id_componente . "_y");
        return $pos;
    }

    function updatePosicao($id_componente, $posx, $posy) {
        $this->setValor($id_componente . "_x", $posx);
        $this->setValor($id_componente . "_y", $posy);
        $controller = getControllerForTable("componente_diagrama");
//        echo "update($this->id_diagrama, $id_componente, $posx, $posy)";
        $controller->updatePosicao($this->id_diagrama, $id_componente, $posx, $posy);
    }

    function pegaPosicaoBanco($id_componente) {
        $controller = getControllerForTable("componente_diagrama");
        $row = $controller->pegaPosicao($this->id_diagrama, $id_componente);

        $this->setValor($id_entidade . "_x", $row['posx']);
        $this->setValor($id_entidade . "_y", $row['posy']);
    }

}

?>