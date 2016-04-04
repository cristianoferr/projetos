<?

class Tarefa_EP {

    private $model;

    function __construct($model) {
        $this->model = $model;
    }

    function escreveTarefa() {
        ?><span class="del_task"><a href="<? echo getHomeDir() . "task/" . projetoAtual() . "/" . $this->model->getValorCampo('id_tarefa'); ?>"><? echo $this->model->getValorCampo('nm_tarefa'); ?></a></span><?
    }

}

class Entregavel_EP {

    private $model;
    private $arrChilden;
    private $arrTarefas;

    function __construct($model) {
        $this->model = $model;
        $this->arrChilden = array();
        $this->arrTarefas = array();
    }

    function setModel($model) {
        $this->model = $model;
    }

    function getModel() {
        return $this->model;
    }

    function adicionaFilho($entregavel) {
        array_push($this->arrChilden, $entregavel);
    }

    function adicionaTarefa($tarefa) {
        array_push($this->arrTarefas, $tarefa);
    }

    //não é otimizada...
    function adicionaEntregavelID($idEntregavelPai, $entregavel) {
        if ($this->model->getValorCampo['id_entregavel'] == $idEntregavel) {
            //$entr=new Entregavel($model);
            array_push($this->arrChilden, $entregavel);
            return true;
        }
        for ($c = 0; $c < sizeof($this->arrChilden); $c++) {
            $child = $this->arrChilden[$c];
            if ($child->adicionaEntregavelID($idEntregavelPai, $entregavel)) {
                return true;
            }
        }
        return false;
    }

    //$nivelPai exemplo: '1.1.', posicao é a posição do filho, se for 1o é 1
    function escreveEntregavel($nivelPai, $posicao) {
        $title = translateKey($this->getModel()->getValorCampo('id_status_entregavel' . SUFIXO_DESC));
        $id = $nivelPai . $posicao;
        $id = replaceString($id, ".", "_");
        ?><div class="deliverable">
            <span class="del_tit"><span id="status_<? echo $id; ?>" data-toggle="tooltip" data-placement="left" title="<? echo $title; ?>" 
                                        class="hint--left caixa del_block status_del_<? echo $this->getModel()->getValorCampo('id_status_entregavel'); ?>"></span>
                <a href="<? echo getHomeDir() . "deliverable/" . projetoAtual() . "/" . $this->model->getValorCampo('id_entregavel'); ?>"><? echo $nivelPai . $posicao . ".  " . $this->getModel()->getValorCampo('nm_entregavel') ?></a></span>
            <script>$('#status_<? echo $id; ?>').tooltip()</script>
            <?
            $this->escreveTarefas();
            for ($c = 0; $c < sizeof($this->arrChilden); $c++) {
                $child = $this->arrChilden[$c];
                $child->escreveEntregavel($nivelPai . $posicao . ".", $c + 1);
            }
            ?>


        </div>	<?
    }

    function escreveTarefas() {
        if (sizeof($this->arrTarefas) > 0) {
            ?><div class="del_tasks"><?
            for ($c = 0; $c < sizeof($this->arrTarefas); $c++) {
                $tarefa = $this->arrTarefas[$c];
                $tarefa->escreveTarefa();
            }
            ?></div><?
        }
    }

}

class EntregavelPainel extends BasePainel {

    private $tarefaController;
    private $entregavelRaiz;
    private $entrInseridos; //string no formato ,<id>,
    private $arrEntregaveis;

    function cabecalho() {
        BasePainel::cabecalho();
        $this->entrInseridos = ",";
        $this->arrEntregaveis = array();
        ?><div class="destaque"><?
        }

        function registro(IModel $model) {
            $entreg = new Entregavel_EP($model);
            if (($model->getValorCampo('id_entregavel_pai') == "") || ($model->getValorCampo('id_entregavel_pai') == "0")) {
                $this->entregavelRaiz = $entreg;
            }
            array_push($this->arrEntregaveis, $entreg);
        }

        function setDetalhesNovoRegistro($link, $texto) {
            $this->textoNovo = $texto;
            $this->linkNovo = $link;
        }

        function setTarefaController($c) {
            $this->tarefaController = $c;
        }

        function encadeia() {
            $this->entregavelRaiz;
            for ($c = 0; $c < sizeof($this->arrEntregaveis); $c++) {
                $entreg = $this->arrEntregaveis[$c];
                //	echo "$c - ".$entreg->getModel()->getValorCampo('id_entregavel')." - ".$entreg->getModel()->getValorCampo('id_entregavel_pai')."<br>";
                $this->adicionaNoPai($entreg, $entreg->getModel()->getValorCampo('id_entregavel_pai'));
            }


            if ($this->tarefaController) {
                while ($model = $this->tarefaController->next()) {
                    $tarefa = new Tarefa_EP($model);
                    $idEntregavel = $model->getValorCampo("id_entregavel");
                    for ($c = 0; $c < sizeof($this->arrEntregaveis); $c++) {
                        $entreg = $this->arrEntregaveis[$c];
                        //	echo "$c - ".$entreg->getModel()->getValorCampo('id_entregavel')." - ".$entreg->getModel()->getValorCampo('id_entregavel_pai')."<br>";
                        if ($entreg->getModel()->getValorCampo('id_entregavel') == $idEntregavel) {
                            $entreg->adicionaTarefa($tarefa);
                        }
                    }
                }
            }
        }

        function adicionaNoPai($entreg, $idPai) {
            if ((!isset($idPai)) || ($idPai == 0)) {
                return true;
            }
            for ($c = 0; $c < sizeof($this->arrEntregaveis); $c++) {
                $entregPai = $this->arrEntregaveis[$c];
                if ($entregPai->getModel()->getValorCampo('id_entregavel') == $idPai) {
                    $entregPai->adicionaFilho($entreg);
                    return true;
                }
            }
        }

        function rodape(ElementMaker $elRoot) {

            $this->encadeia();
            $this->entregavelRaiz->escreveEntregavel('', 1);
            $this->drawLinks();
            ?></div><?
    }

}
?>