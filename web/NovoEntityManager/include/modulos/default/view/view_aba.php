<?

class ViewAba {

    private $linksAba;
    private $identificadorVisual;
    private $visaoUnica;
    private $param;
    private $defaultValue;

    function ViewAba($identificadorVisual) {
        $this->identificadorVisual = $identificadorVisual;
        $this->linksAba = array();
        $this->param = "aba";
        $this->defaultValue = "default";
    }

    function setParam($param) {
        $this->param = $param;
    }

    function setDefaultValue($param) {
        $this->defaultValue = $param;
    }

    function setIdentificadorVisual($v) {
        $this->identificadorVisual = $v;
    }

    function ativaVisaoUnica($linkBase) {
        $this->visaoUnica = $linkBase;
    }

    function desenhaAbas() {
        for ($c = 0; $c < sizeOf($this->linksAba); $c++) {
            $aba = $this->linksAba[$c];
            $url = $aba->getUrl();
            $onclick = "onclick=\"return mudaAba('pg" . $aba->getUrl() . "',arrPG_" . $aba->getUrl() . ",'" . $this->identificadorVisual . "');\"";
            if ($this->visaoUnica) {
                $url = $this->visaoUnica . $url;
                $onclick = "";
            } else {
                $url = "#$url";
            }
            echo "<a href='$url' $onclick class='button-aba aba_pg" . $aba->getUrl() . "'>" . $aba->getText() . "</a>";
        }
    }

    function adicionaAba($id, $texto, $visible = true) {
        //echo "adicionaAba: $texto,$id,$visible,$visible";
        $link = new LinkView($texto, $id, $visible, $visible);
        array_push($this->linksAba, $link);
    }

    function iniciaAba($abaID) {
        ?><div class="class_aba class_aba_<? echo $abaID ?>">
            <a name=<? echo $abaID ?>>
                <?
            }

            function fimAba() {
                ?></div><?
        }

        function start() {
            echo "<script>";
            for ($c = 0; $c < sizeOf($this->linksAba); $c++) {
                $aba = $this->linksAba[$c];
                ?>var arrPG_<? echo $aba->getUrl(); ?> = ['<? echo "aba_" . $aba->getUrl(); ?>'];<?
            $default = "false";
            if ($aba->getFlagImportante()) {
                $default = "true";
            }

            $default = $this->getEstadoAba($aba, $default);
            // echo "c:$c default: $default ! ";
            if ($default == "false") {
                echo "mudaAba('pg" . $aba->getUrl() . "',arrPG_" . $aba->getUrl() . ",'" . $this->identificadorVisual . "');";
            }
        }

        echo "</script>";
    }

    function getAbaAtual() {
        if ($_GET[$this->param])
            return $_GET[$this->param];
        if ($GLOBALS[$this->param])
            return $GLOBALS[$this->param];
        return $this->defaultValue;
    }

    function getEstadoAba($aba, $default) {
        //echo "visao:".$this->visaoUnica;
        if ($this->visaoUnica) {
            $abaAtual = $this->getAbaAtual();
            // echo "url:" . $aba->getUrl();
            $cond = ((($abaAtual == $this->defaultValue) && ($aba->getFlagImportante())) || ($aba->getUrl() == $abaAtual));
            //echo "cond: '$cond':" . ($aba->getUrl() == $abaAtual) . "!!";
            if (!$cond) {
                return "true";
            } else {
                return "false";
            }
        }
        $id = $this->identificadorVisual . "_pg" . $aba->getUrl();
        return getControllerManager()->getControllerForTable("usuario")->getEstadoInterface($id, $default);
    }

}
?>