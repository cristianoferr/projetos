<?

class GerenciaAba {

    private $abaInicial;
    private $linksAba;
    private $identificadorVisual;
    private $colunasAba; //colunasAba é um array de array,

    function GerenciaAba() {
        $this->linksAba = array();
        $this->colunasAba = new Dicionario();
    }

    function setIdentificadorVisual($v) {
        $this->identificadorVisual = $v;
    }

    function adicionaAba($id, $texto, $visible = true) {
        $link = new LinkView($texto, $id, $visible, null);
        array_push($this->linksAba, $link);
    }

    function adicionaColunaNaAba($coluna, $idAba) {//coberto
        $aba = $this->colunasAba->getValor($idAba);
        //write("adicionaColunaNaAba: $idAba " . $coluna->getDBName() . " isset(ABA)? " . isset($aba));
        if (!isset($aba)) {
            $aba = array();
        }
        array_push($aba, $coluna);

        $this->colunasAba->setValor($idAba, $aba);
    }

    function getColunasDaAba($idAba) {
        $aba = $this->colunasAba->getValor($idAba);
        return $aba;
    }

    function getAbas() {//coberto
        return $this->linksAba;
    }

    /**
     * Método principal
     * 
     * @param type $model
     */
    function escreveColunasEmAbas(IModel $model, ElementMaker $elRoot) {
        $tot = $this->escreveColunasAba(ABA_PRINCIPAL, null, $model, 0, $elRoot);

        // write("escreveColunasEmAbas " . sizeof($this->linksAba));
        $abas = $this->getAbas();
        for ($c = 0; $c < sizeOf($abas); $c++) {
            $aba = $abas[$c];
            //write("aba:$aba");
            $tot+=$this->escreveColunasAba($aba->getUrl(), $aba->getText(), $model, $tot, $elRoot);
        }
    }

    function escreveColunasAba($idAba_, $nmAba, IModel $model, $qtdItens, ElementMaker $elRoot) {
        //  $idAba = $this->getFormName() . "_" . $idAba_;
        $colunas = $this->getColunasDaAba($idAba_);

        if (isset($nmAba)) {
            $elRoot = $this->escreveTituloAba($idAba_, $nmAba, $elRoot);
        }
        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            $this->escreveColuna($coluna, $qtdItens + $c, $model, $elRoot);
        }
        return $c;
    }

    //chamado no cabecalho para desenhar os botoes das abas
    function drawAbas($formName, ElementMaker $elRoot = null) {
        writeDebug("drawAbas($formName)");
        $links = $this->linksAba;

        for ($c = 0; $c < sizeOf($links); $c++) {
            $this->abaInicial = $links[0];
            $aba = $links[$c];
            //  $id = $aba->getUrl() . "_" . $identificador;
            // $formID = $formName . "_" . $aba->getUrl();

            $aba->drawAba($formName, $this->identificadorVisual, $elRoot);
        }
    }

    //chamado no rodape para inicializar as abas, mostrando apenas a atual
    function inicializaAba($formName) {
        writeDebug("inicializaAba($formName)");
        if (!isset($this->abaInicial)) {
            return;
        }
        $formID = $this->getAbaId($this->abaInicial);
        $this->geraArrayComponentesDasAbas($formID);
    }

    function geraArrayComponentesDasAbas($formName) {
        echo "<script>";

        $arrayAba = $this->linksAba;
        for ($c = 0; $c < sizeOf($arrayAba); $c++) {
            $aba = $arrayAba[$c];
            $formID = $this->getAbaId($aba);
            $this->geraArrayDaAba($aba, $formName, $formID);
        }

        echo "</script>";
    }

    function pegaConfigAbas() {
        if (!isset($this->identificadorVisual)) {
            return;
        }
        echo "<script>";

        $arrayAba = $this->linksAba;
        for ($c = 0; $c < sizeOf($arrayAba); $c++) {
            $aba = $arrayAba[$c];
            $formID = $this->getAbaId($aba);
            $this->pegaConfigAba($aba, $this->getFormName(), $formID);
        }

        echo "</script>";
    }

    function getAbaId($aba) {
        return $this->getFormName() . "_" . $aba->getUrl();
    }

    function pegaConfigAba($aba, $formName, $abaID) {
        if (!$aba->getFlagImportante()) {
            $default = "false";
        }
        if ($default == "false")
        //echo "mudaAba('".$abaID."',arr_aba_$abaID,'".$this->identificadorVisual."',".$default.");\n";
            echo "mudaAba('" . $abaID . "',arr_aba_$abaID,'" . $this->identificadorVisual . "');\n";
    }

    //javascript
    function geraArrayDaAba($aba, $formName, $abaID) {
        //$abaID=$formName."_".$aba->getUrl();


        $arrayColunasAba = $this->colunasAba->getValor($abaID);
        writeDebug("geraArrayDaAba($formName), abaID=$abaID tamanho:" . sizeOf($arrayColunasAba));
        ?>var arr_aba_<? echo $abaID; ?>=null;
        <?
        if (sizeOf($arrayColunasAba) == 0) {
            return;
        }
        ?>arr_aba_<? echo $abaID; ?> = [<?
        $rows = "#";
        for ($c = 0; $c < sizeOf($arrayColunasAba); $c++) {
            $coluna = $arrayColunasAba[$c];
            $rows.=",\"" . $formName . "_" . $coluna->getDBName() . "\"\n";
        }
        $rows = str_replace("#,", "", $rows);

        echo $rows;
        ?>];
        <?
    }

    function getEstadoAba($aba, $formName, $default) {
        $formId = $this->getAbaId($aba);
        $id = $this->identificadorVisual . "_" . $formId;
        return getControllerManager()->getControllerForTable("usuario")->getEstadoInterface($id, $default);
    }

}
?>