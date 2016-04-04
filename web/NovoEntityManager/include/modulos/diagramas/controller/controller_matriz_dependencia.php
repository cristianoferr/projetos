<?

class MatrizDependenciaController extends BaseController {

    private $matriz;

    function calculaOrdemInsert($id_projeto, $id_entregavel_raiz) {
        $matriz = $this->criaMatrizDependencia($id_projeto, $id_entregavel_raiz);
        $indices = array();

        $contaInicial = 0;
        $contaFinal = 1;
        $iter = 0;

        while ($contaInicial != $contaFinal) {
            $contaInicial = sizeof($indices);
            $indices = $this->iteraMatrizDependencia($matriz, $indices);
            $matriz = $this->limpaIndiceMatriz($matriz, $indices);
            $contaFinal = sizeof($indices);

            $conta = sizeof($indices);

            $iter++;
        }

        $conta = sizeof($indices);
        $colsSize = sizeof($matriz);
        if ($conta != $colsSize) {
            echo "<h2>ERROR! $conta $colsSize Circular dependence detected!</h2>";
            $vet = $this->relacaoEntidadesComDependencia($matriz);

            for ($row = 0; $row < sizeof($vet); $row++) {
                echo "<a href='" . getHomeDir() . "entity/$id_projeto/" . $vet[$row][0] . "'>" . $vet[$row][1] . "</a><br>";
            }
        }

        return $indices;
    }

    function limpaIndiceMatriz($matriz, $indices) {
        $colsSize = sizeof($matriz);
        $tIndices = sizeof($indices);
        for ($row = 0; $row < $colsSize; $row++) {
            $vetor = $matriz[$row];
            for ($col = 0; $col < $colsSize; $col++) {
                for ($cIndices = 0; $cIndices < $tIndices; $cIndices++) {
                    if ($vetor[$col][0] == $indices[$cIndices]) {
                        $vetor[$col][2] = "";
                    }
                }
            }
            $matriz[$row] = $vetor;
        }

        return $matriz;
    }

    function iteraMatrizDependencia($matriz, $indices) {
        $colsSize = sizeof($matriz);
        $count = sizeof($indices);
        for ($row = 0; $row < $colsSize; $row++) {
            $vetor = $matriz[$row];
            $id_entidade_row = $vetor[$row][0];
            $soma = $this->somaLinha($vetor);
            if (($soma == 0) && (!$this->entidadeIn($id_entidade_row, $indices))) {
                $indices[$count] = $id_entidade_row;
                $count++;
            }
        }
        return $indices;
    }

    function entidadeIn($id_entidade, $indices) {
        $colsSize = sizeof($indices);
        for ($row = 0; $row < $colsSize; $row++) {
            if ($indices[$row] == $id_entidade) {
                return true;
            }
        }
        return false;
    }

    function somaLinha($vetor) {
        $colsSize = sizeof($vetor);
        $soma = 0;
        for ($row = 0; $row < $colsSize; $row++) {
            $soma+=$vetor[$row][2];
        }
        return $soma;
    }

    function criaVetorEntidades($id_projeto, $id_entregavel_raiz) {
        $id = "mat_vet_ent_$id_projeto" . "_" . $id_entregavel_raiz;
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }
        $entidadeController = getControllerManager()->getControllerForTable("entidade");
        $rs = $entidadeController->consultaEntidadesProjeto($id_projeto, $id_entregavel_raiz);
        $count = 0;
        $vetor = array();
        while ($rowEntidade = $rs->fetch()) {
            $id_entidade = $rowEntidade['id_entidade'];
            $nm_entidade = $rowEntidade['nm_entidade'];
            $vetor[$count] = array();
            $vetor[$count][0] = $id_entidade;
            $vetor[$count][1] = $nm_entidade;
            $vetor[$count][2] = "";
            $count++;
        }
        $this->setCacheInfo($id, $vetor);
        return $vetor;
    }

    function getMatriz() {
        return $this->matriz;
    }

    function criaMatrizDependencia($id_projeto, $id_entregavel_raiz) {//coberto
        $vetor = $this->criaVetorEntidades($id_projeto, $id_entregavel_raiz);
        $colsSize = sizeof($vetor);
        //echo "Tamanho vetor  size:$colsSize<br>";

        $this->matriz = array();
        $count = 0;
        for ($row = 0; $row < $colsSize; $row++) {
            $this->matriz[$row] = $vetor;
            $id_entidade = $vetor[$row][0];
            $this->criaMatrizDependencia_Coluna($colsSize, $row, $id_entidade);
            $count++;
        }
        return $this->matriz;
    }

    function criaMatrizDependencia_Coluna($colsSize, $row, $id_entidade) {//coberto
        $vetorDependencia = $this->getVetorDependenciaEntidade($id_entidade);
        $depSize = sizeof($vetorDependencia);
        for ($col = 0; $col < $colsSize; $col++) {
            $colVet = $this->matriz[$row];
            for ($depCount = 0; $depCount < $depSize; $depCount++) {
                //echo "verificando:  $vetorDependencia[$depCount] = ".$colVet[$col][0]."<br>";
                if ($vetorDependencia[$depCount][0] == $colVet[$col][0]) {
                    $colVet[$col][2] = $vetorDependencia[$depCount][1];
                }
            }
            $this->matriz[$row] = $colVet;
        }
    }

    //retorna um vetor com as entidades que essa entidade precisa
    function getVetorDependenciaEntidade($id_entidade) {//coberto
        $id = "mat_dep_vet_$id_entidade";
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }
        $vet = array();
        $this->preencheDependenciasFK($id_entidade, $vet);
        $this->preencheDependenciasExtends($id_entidade, $vet);

        $this->setCacheInfo($id, $vet);
        return $vet;
    }

    function preencheDependenciasFK($id_entidade, &$vet) {//coberto
        $rsEntidade = executaQuery("select * from coluna c where id_entidade_pai=? and id_entidade_combo is not null", array($id_entidade));
        $count = 0;
        while ($rowEntidade = $rsEntidade->fetch()) {
            $entidadePK = $rowEntidade['id_entidade_combo'];
            if (($entidadePK > 0) && ($entidadePK != $id_entidade)) {
                $vet[$count] = array();
                $vet[$count][0] = $rowEntidade['id_entidade_combo'];
                $vet[$count][1] = DEP_FK . " ";
                $count++;
            }
        }
    }

    function preencheDependenciasExtends($id_entidade, &$vet) {//coberto
        $row = executaQuerySingleRow("select id_entidade_extends from entidade where id_entidade=? and id_entidade_extends is not null", array($id_entidade));
        if ($row) {
            $v = array();
            array_push($v, $row['id_entidade_extends']);
            array_push($v, DEP_EXTENDS . " ");
            array_push($vet, $v);
        }
    }

}

?>