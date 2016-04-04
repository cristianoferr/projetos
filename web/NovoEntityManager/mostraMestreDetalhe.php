<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();


escreveHeader();
?>
<style>



</style>
<hr>
<?
$clContainer = "panel-projeto";
$clD = "";
$clDetRow = " container ";
$clG = "panel-heading";
$clCol = "col-sm-2 col";
$clGI = "";
?>
<div class="">

    <div class="<? echo $clContainer; ?>">
        <div class="<? echo $clG . " " . $clDetRow; ?>">
            <div class="<? echo $clCol . " " . $clGI; ?>">CampoMestre1</div>
            <div class="<? echo $clCol . " " . $clGI; ?>">CampoMestre2</div>
        </div>
        <? for ($c = 0; $c < 10; $c++) { ?>
            <div class="color_alternate <? echo $clDetRow; ?>">
                <div class="col-xs-2 col" >
                    <a href="#" class="btn-xs btn-warning" style="display:inline; ">
                        <span class="glyphicon glyphicon-plus">

                        </span>
                    </a>
                    <a href="#" class="btn-xs btn-warning" style="display:inline; ">
                        <span class="glyphicon glyphicon-plus">

                        </span>
                    </a>
                </div>
                <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo1</div>
                <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo2</div>
                <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo3</div>
                <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo4</div>
                <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo5</div>

            </div>
        <? } ?>
    </div>


    <div class="<? echo $clG . " " . $clDetRow; ?>">
        <div class="<? echo $clCol . " " . $clGI; ?>">CampoMestre1</div>
        <div class="<? echo $clCol . " " . $clGI; ?>">CampoMestre2</div>
    </div>
    <? for ($c = 0; $c < 10; $c++) { ?>
        <div class="color_alternate <? echo $clDetRow; ?>">
            <div class="col-xs-2 col" >
                <a href="#" class="btn-xs btn-warning" style="display:inline; ">
                    <span class="glyphicon glyphicon-plus">

                    </span>
                </a>
                <a href="#" class="btn-xs btn-warning" style="display:inline; ">
                    <span class="glyphicon glyphicon-plus">

                    </span>
                </a>
            </div>
            <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo1</div>
            <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo2</div>
            <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo3</div>
            <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo4</div>
            <div class="<? echo $clCol . " " . $clD; ?>"><? echo $c; ?>.Campo5</div>

        </div>
    <? } ?>




    <div class="container btn-default ">
        <div class="<? echo " " . $clGI; ?>"><a href="#">New Section</a></div>
    </div>
</div>
<hr>
<?
$painel = new PainelMestreDetalhe();
$painel->defineMaster("widget_section", "id_widget_section", "seq_widget_section");
$painel->defineDetail("widget_coluna", "seq_widget_coluna");

// -- ao mudar o campo mestre, novos campos filhos serão mostrados
$painel->addColunaMestre("id_widget_section");
$painel->addColunaMestre("seq_widget_section");
$painel->addColunaMestre("nm_widget_section");
$painel->addColunaDetalhe("id_widget_coluna");
$painel->addColunaDetalhe("seq_widget_coluna");
$painel->addColunaDetalhe("id_coluna");

$painel->load("id_widget", WIDGET_TESTE_SECTION);
$painel->mostra();
escreveFooter();
?>
