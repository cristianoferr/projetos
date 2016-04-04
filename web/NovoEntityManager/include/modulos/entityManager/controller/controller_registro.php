<?

class RegistroController extends BaseController {

    private $colunas;
    private $valorController;
    private $id_entidade;

    function __construct() {
        $tableValor = getTableManager()->getTabelaComNome("valor");
        parent::__construct(getTableManager()->getTabelaComNome("registro"), "txt_null", "txt_null");

        $this->valorController = new ValorController($tableValor, "txt_null", "txt_null");
    }

    function excluirRegistro($id_registro) {

        $id_registro = validaNumero($id_registro, "excluiregistro id_registro");
        validaEscrita("registro", $id_registro);

        if (isUserAdminProjeto($this->getProjeto($id_registro))) {
            executaSQL("delete from valor where id_registro=$id_registro");
            executaSQL("delete from registro where id_registro=$id_registro");
        }
    }

    function getProjeto($id_registro) {
        $id = "reg_proj_$id_coluna";
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }

        $id_registro = validaNumero($id_registro, "id_registro getProjeto contr_registro");
        $row = executaQuerySingleRow("select id_projeto from entidade e,registro c where e.id_entidade=c.id_entidade_pai and c.id_registro=$id_registro");
        $this->setCacheInfo($id, $row['id_projeto']);
        return $row['id_projeto'];
    }

    function getEntidade($id_registro) {//coberto
        $id = "reg_ent_$id_registro";
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }
        $id_registro = validaNumero($id_registro, "id_registro getEntidade contr_coluna");
        $row = executaQuerySingleRow("select id_entidade_pai from registro c where c.id_registro=?", array($id_registro));
        $this->setCacheInfo($id, $row['id_entidade_pai']);
        return $row['id_entidade_pai'];
    }

    function getProjetoValor($id) {
        $idC = "reg_val_proj_$id";
        if ($this->getCacheInfo($idC)) {
            return $this->getCacheInfo($id);
        }

        $id = validaNumero($id, "id getProjetoValor contr_registro");
        $row = executaQuerySingleRow("select id_projeto from entidade e,registro r,valor v where e.id_entidade=r.id_entidade_pai and v.id_registro=r.id_registro and v.id_valor=$id");
        $this->setCacheInfo($idC, $row['id_projeto']);
        return $row['id_projeto'];
    }

    function fillSelectCombobox($id_entidade) {
        $dict = new Dicionario();
        $id_entidade = validaNumero($id_entidade, "id_entidade fillSelectCombobox contr_registro");
        if ($id_entidade == "null") {
            erroFatal("Entity undefined");
        }

        $entidadeController = getControllerForTable("entidade");
        $modelEntidade = $entidadeController->loadSingle($id_entidade);

        //writeAdmin("id_entidade:$id_entidade");
        $id_coluna_pk = $modelEntidade->getValorCampo("id_coluna_pk");
        $id_coluna_desc = $modelEntidade->getValorCampo("id_coluna_desc");

        if (($id_coluna_pk) && ($id_coluna_desc)) {
            $sql = "select * from valor v where id_coluna_pai=$id_coluna_pk or id_coluna_pai=$id_coluna_desc order by id_registro";
            $rsValor = executaQuery($sql);
            //echo "fillSelectCombobox:$sql";

            $id_registro = -1;
            while ($rowValor = $rsValor->fetch()) {
                if ($id_registro != $rowValor['id_registro']) {
                    if ($id_registro != -1) {
                        writeDebug("$id_entidade:: dict->setValor($valorPK,$valorDesc)");
                        //echo "$id_entidade:: dict->setValor($valorPK,$valorDesc)";
                        $dict->setValor($valorPK, $valorDesc);
                    }
                    $id_registro = $rowValor['id_registro'];
                }
                if ($rowValor['id_coluna_pai'] == $id_coluna_pk)
                    $valorPK = $rowValor['valor_coluna'];
                if ($rowValor['id_coluna_pai'] == $id_coluna_desc)
                    $valorDesc = $rowValor['valor_coluna'];
            }
        }
        $dict->setValor($valorPK, $valorDesc);

        return $dict;
    }

    function insereRegistro($id_entidade, $seq = null) {
        $this->id_entidade = $id_entidade;
        if (!isset($seq)) {
            $seq = $this->getNextSequence($id_entidade);
        }

        $this->determinaColunas($id_entidade);


        $registro = new BaseModel($this, $this->getTable());
        $registro->setValorCampo("seq_registro", $seq);
        $registro->setValorCampo("id_entidade_pai", $id_entidade);


        $id_registro = executaSQL("insert into registro (id_entidade_pai,seq_registro) values (?,?)", array($id_entidade, $seq));
        $registro->setId($id_registro);

        for ($c = 0; $c < sizeOf($this->colunas); $c++) {
            $coluna = $this->colunas[$c];
            //echo "insereRegistro c: $c ".$registro->getId();
            $this->valorController->insereColunaNoRegistro($coluna, $registro);
        }
    }

    function getNextSequence($id_entidade) {
        $row = executaQuerySingleRow("select max(seq_registro)+1 as tot from registro where id_entidade_pai=?", array($id_entidade));
        $seq = $row['tot'];
        if ($seq == "")
            $seq = 1;
        return $seq;
    }

    function preencheCelulaVazia($id_registro, $id_coluna) {
        $this->valorController->preencheCelula($id_registro, $id_coluna);
    }

    function determinaColunas($id_entidade) {
        $this->id_entidade = $id_entidade;
        if (isset($this->colunas))
            return $this->colunas;
        $colunas = array();

        $sql = "select * from coluna c";
        $sql.=",relacao_datatype rd,datatype d,primitive_type pt ";
        $sql.=" where c.id_entidade_pai=? ";
        $sql.=" and rd.id_relacao_datatype=c.id_relacao_datatype and d.id_datatype=c.id_datatype and d.id_primitive_type=pt.id_primitive_type";
        $sql.=" order by seq_coluna,id_coluna";
        //echo $sql . "::" . $id_entidade;
        $rsColuna = executaQuery($sql, array($id_entidade));

        while ($rowColuna = $rsColuna->fetch()) {
            $metaColuna = new MetaColuna();
            $metaColuna->setName($rowColuna['nm_coluna']);
            $metaColuna->setId($rowColuna['id_coluna']);
            $metaColuna->setPrimitiveType($rowColuna['id_primitive_type']);
            $metaColuna->setRelacao($rowColuna['id_relacao_datatype']);
            $metaColuna->setEntidadeCombo($rowColuna['id_entidade_combo']);
            $metaColuna->setAcessoColuna($rowColuna['id_acesso_coluna']);
            $metaColuna->setSqlColuna($rowColuna['id_sql_coluna']);

            array_push($colunas, $metaColuna);
        }
        $this->colunas = $colunas;
        return $colunas;
    }

    function contaRegistros($id_entidade) {
        $this->id_entidade = $id_entidade;
        $arrRows = executaQuerySingleRow("select count(*) as tot from registro where id_entidade_pai=?", array($id_entidade));
        return $arrRows['tot'];
    }

    function getRSRelacaoValoresEntidade($id_entidade) {
        $this->id_entidade = $id_entidade;
        return executaQuery($this->getSQLRelacaoValoresEntidade($id_entidade));
    }

    function getSQLRelacaoValoresEntidade($id_entidade) {
        $this->id_entidade = $id_entidade;
        $sql = "select * from valor v,coluna c,registro r,relacao_datatype rd,datatype d,primitive_type pt,sql_coluna sc ";
        $sql.=" where r.id_entidade_pai=$id_entidade";
        $sql.=" and v.id_coluna_pai=c.id_coluna";
        $sql.=" and c.id_sql_coluna=sc.id_sql_coluna";
        $sql.=" and d.id_primitive_type=pt.id_primitive_type and d.id_datatype=c.id_datatype";
        $sql.=" and c.id_relacao_datatype=rd.id_relacao_datatype";
        $sql.=" and v.id_registro=r.id_registro";
        $sql.=" order by r.id_registro,c.seq_coluna";
        return $sql;
    }

    function iniciaMatriz() {
        $id_entidade = $this->id_entidade;
        $totRows = $this->contaRegistros($id_entidade);
        if ($totRows == 0)
            return false;
        $matriz = array($totRows);
        for ($row = 0; $row < $totRows; $row++) {
            $registro = new MetaRegistro();
            $matriz[$row] = $registro;
            $this->preencheColunasVaziasRegistro($registro);
        }
        return $matriz;
    }

    function criaMatriz() {
        $colunas = $this->colunas;
        $id_entidade = $this->id_entidade;

        $colsSize = sizeof($colunas);


        $matriz = $this->iniciaMatriz();
        if (!$matriz)
            return;


        $rsRegistro = $this->getRSRelacaoValoresEntidade($id_entidade);

        $prevRegistro = "";
        $linha = -1;
        $regs = 0;
        while ($rowRegistro = $rsRegistro->fetch()) {

            if ($prevRegistro != $rowRegistro['id_registro']) {
                if ($linha != -1) {
                    $this->verificaVazios($matriz[$linha]);
                }
                $linha++;
                $prevRegistro = $rowRegistro['id_registro'];
                $coluna = 0;
            }

            $registro = $matriz[$linha];
            $registro->setId($rowRegistro['id_registro']);

            $x = $rowRegistro['valor_coluna'];

            $valor = $this->getValorFromColunaWithName($registro, $rowRegistro['nm_coluna']);
            if ($valor) {
                $valor->setId($rowRegistro['id_valor']);
                $valor->setValor($x);
            }

            $regs++;
            $coluna++;
        }
        $this->verificaVazios($matriz[$linha]);

        return $matriz;
    }

    function verificaVazios($registro) {
        $this->valorController->verificaVazios($registro);
    }

    function preencheColunasVaziasRegistro($registro) {
        for ($c = 0; $c < sizeOf($this->colunas); $c++) {
            $coluna = $this->colunas[$c];
            $valor = new MetaValor($coluna);
            $registro->adicionaValor($valor);
        }
    }

    function getValorFromColunaWithName($registro, $nm_coluna) {
        return $registro->getValorFromColunaWithName($nm_coluna);
    }

}

?>