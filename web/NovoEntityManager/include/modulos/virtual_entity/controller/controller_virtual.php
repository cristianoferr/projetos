<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of controller_virtual
 *
 * @author CMM4
 */
class VirtualController extends ControllerAbstract implements IController {

    private $entityId; //Id da entidade que está sendo virtualizada

    function __construct(ITable $table) {
        $this->table = $table;
        $this->limpaRegistros();
    }

    function atualizaValor($idreg, $campo, $novoValor) {
        if (!$this->isValueWritable($idreg, $campo)) {
            return;
        }
        $controllerColuna = getControllerForTable("coluna");
        $controllerRegistro = getControllerForTable("registro");
        $id_entidade = $controllerRegistro->getEntidade($idreg);
        $id_coluna = $controllerColuna->getIdColunaComNome($campo, $id_entidade);

        $controllerValor = getControllerForTable("valor");

        $controllerValor->atualizaCelula($idreg, $id_coluna, $novoValor);
        write("$campo,$id_entidade,$idreg, $id_coluna, $novoValor");
    }

    function excluirRegistro($id) {
        
    }

    function getNewItem() {
        
    }

    /*     * ********** implementar melhor esses metodos */

    function checkIfUserCanWrite(IModel $model) {
        return true;
    }

    function isValueWritable($idreg, $campo) {
        return true;
    }

    /*     * ********** */

    function geraArrayParaColuna(IColuna $coluna, IModel $model = null, $filtro = null) {
        //write ($coluna->getDbName()."  ".$coluna->getTable()->getTableName());
    }

    function initFromEntity($entityId) {//coberto
        $this->entityId = $entityId;
        $this->fillColumns();
        return $this;
    }

    function loadAllColumnsIntoPanel(IPainel $painel) {
        $this->table->loadAllColumnsIntoPanel($painel);
    }

    function fillColumns() {//coberto
        $rs = executaQuery("select * from coluna where id_entidade_pai=? order by id_relacao_datatype,seq_coluna,id_coluna", array($this->entityId));

        while ($row = $rs->fetch()) {
            $this->iniciaColuna($row);
        }
    }

    function iniciaColuna($row) {//coberto
        $datatypeController = getControllerForTable("datatype");
        $coluna = new ColunaVirtualModel($this->table, $row["id_coluna"]);
        $tipoDado = $datatypeController->getTipoDadoFrom($row["id_datatype"]);
        //write ("col:".$row["nm_coluna"]." ".$row["id_entidade_combo"]." tipodado:$tipoDado ".TIPO_INPUT_SELECT_FK);
        if ($row["id_entidade_combo"]>0){
          //  $tipoDado = TIPO_INPUT_SELECT_FK;
        }
        if ($tipoDado == TIPO_INPUT_SELECT_FK) {
            $coluna->iniciaColunaComFK($row["nm_coluna"], $row["dbname_coluna"], false, $row["id_entidade_combo"], $tipoDado);
        } else {
            $coluna->iniciaColuna($row["nm_coluna"], $row["dbname_coluna"], false, $tipoDado);
        }
        $this->table->addColuna($coluna);
    }

    function loadSingle($id_registro, $painel = null) {//coberto
        $this->loadRegistros();
        return $this->getModelByPk($id_registro);
    }

    function loadRegistros($filtro = null, $painel = null) {//coberto
        $this->limpaRegistros();
        $rsRegistro = executaQuery("select * from registro r, valor v,coluna c where v.id_coluna_pai=c.id_coluna and v.id_registro=r.id_registro and r.id_entidade_pai=? order by r.id_registro,id_valor", array($this->entityId));
        $id_registro_atual = false;
        $registro = array();
        while ($row = $rsRegistro->fetch()) {
            $id_registro = $row["id_registro"];
            writeDebug("id_registro: $id_registro  id_registro_atual:$id_registro_atual");
            if ($id_registro != $id_registro_atual) {
                $this->preparaRegistro($registro, $id_registro_atual);
                $id_registro_atual = $id_registro;
                $registro = array();
            }
            $registro[$row["dbname_coluna"]] = $row["valor_coluna"];
        }
        $this->preparaRegistro($registro, $id_registro);
    }

    function preparaRegistro($registro, $id_registro_atual) {//coberto
        $model = $this->inicializaRegistro($registro, $id_registro_atual);
        $this->adicionaRegistro($model);
    }

    function inicializaRegistro($row, $id = null) {//coberto
        if (!$id) {
            return;
        }
        $model = $this->loadEmptyModel();
        $model->carregaDados($id, $row);
        return $model;
    }

    function loadEmptyModel() {//coberto
        $model = new VirtualModel($this->table);
        return $model;
    }

}

?>