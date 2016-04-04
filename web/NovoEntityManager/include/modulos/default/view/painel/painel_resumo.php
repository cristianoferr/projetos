<?

class PainelResumo extends PainelHorizontal {

    function registro(IModel $model) {
        if ($this->getCountAtual() < ITENS_RESUMO) {
            PainelHorizontal::registro($model);
        }
    }

    function infoRodape() {
        $min = ITENS_RESUMO;
        $max = $this->getCountAtual();
        if ($min > $max) {
            $min = $max;
        }
        return "$min " . translateKey("txt_of") . " $max";
    }

}

?>