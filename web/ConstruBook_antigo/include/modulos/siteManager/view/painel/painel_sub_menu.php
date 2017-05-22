<?

//TODO: substituir referencias a "projeto" por referencias genéricas
class SubSideMenuPainel extends BasePainel {

    private $linkItem;
    //private $painelFilho;
    private $codPai;

    function setLinkItem($linkItem) {
        $this->linkItem = $linkItem;
    }

    function setCodPai($valorID) {
        $this->codPai = $valorID;
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();
        $idColuna = $colunas[0];
        $descColuna = $colunas[1];

        $valorID = $model->getValor($idColuna);
        $valorDesc = $model->getValor($descColuna);
        $elLi = elMaker("li", $this->elRoot);
        //echo $valorID."!!!!!!-".$valorDesc."<br>";
        $elLi->addElement(Out::linkG($this->linkItem . $this->codPai . "/" . $valorID, $valorDesc));
    }

}

?>