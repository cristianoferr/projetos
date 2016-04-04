<?php

/*
 * Classe responsável pelo desenho de diagramas wbs
 */

class DesenhaDiagramaWbs extends DesenhaDiagrama {

    function rodape() {
        parent::rodape();
    }

    function __construct($idDiagrama) {
        parent::__construct($idDiagrama);
        $this->setRandomizeLine(0);
    }

    function verificaPosicoes($arrComponents) {
        $controller = getControllerForTable("entregavel");
        $controller->loadRegistros("and id_entregavel in (select id_entregavel from componente_diagrama where id_diagrama=" . $this->getIdDiagrama() . ")");


        $arr = $this->attribSourceToView($controller);
        //printArray($arr);
        $arrFinal = $controller->createModelTreeFromArray("id_entregavel_pai", $arr);
//echo sizeof($arr);
        $this->updateComponentePosition($controller, $arrFinal, 6, 2);
    }

    function updateComponentePosition($controller, $arr, $initX, $initY, $nivel = 0) {
        $width = 0;
        $height = 0;

        if ($nivel == 1) {
            $initX = 1;
        }

        foreach ($arr as $model) {
            $width += $model->getWidth();
            $height += $model->getHeight($nivel);
        }
        $left = $initX;
        $top = $initY;

        foreach ($arr as $model) {
            // if ($nivel == 2)
            // write("a) $nivel  Model: $model left:$left top:$top width:" . $model->getWidth() . " height:" . $model->getHeight());
            $model->setViewLeft($left);
            $model->setViewTop($top);
            $this->updateComponentePosition($controller, $model->getChildren(), $left + 1, $top + 1, $nivel + 1);
            if ($nivel <= 1) {
                $left+=$model->getHeight();
            } else {
                $top+=$model->getWidth();
            }
        }
    }

}

?>
