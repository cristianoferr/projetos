<?

//Vertical é ideal para quando se tem só um registro
class PainelVertical extends FormPainel {
    
}

class PainelVerticalTable extends BasePainel {

    function cabecalho() {
        BasePainel::cabecalho();
        echo "<div class='table-responsive'>";
        echo "<table class='data fundo caixa panelsizetable table-bordered table-hover '>";

        $this->escreveTitulo();
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();
        writeDebug("PainelVertical.registro, contem " . sizeOf($colunas) . " colunas.");


        $this->escreveColunasAba(ABA_PRINCIPAL, null, $model);

        $abas = $this->getAbas();
        for ($c = 0; $c < sizeOf($abas); $c++) {
            $aba = $abas[$c];
            $this->escreveColunasAba($aba->getUrl(), $aba->getText(), $model);
        }
    }

    function escreveColunasAba($idAba, $nmAba, $model) {
        $idAba = $this->getFormName() . "_" . $idAba;
        $colunas = $this->getColunasDaAba($idAba);
        if (isset($nmAba)) {
            ?><tr class="aba-vertical"><td colspan=2><? echo $nmAba; ?></td></tr><?
        }
        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            $this->escreveColuna($coluna, $model);
        }
    }

    function escreveColuna($coluna, $model) {
        writeDebug("PainelVertical.registro: " . $coluna->getDbName . " pode? " . $this->podeMostrarColuna($coluna));
        if ($this->podeMostrarColuna($coluna)) {
            $uniqueID = $this->getFormName();
            ?>
            <tr id="tr_<? echo $uniqueID . "_" . $coluna->getDbName(); ?>">
                <th scope="row" class="specalt"><? echo $coluna->getCaption() ?></th>
                    <? $this->escreveCelula($coluna, $model, $uniqueID, true); ?>
            </tr>
            <?
        }
    }

    function rodape(ElementMaker $elRoot) {

        BasePainel::rodape();
        ?></table></div><?
    }

}
?>