<?

class MetaColuna {

    private $name, $id, $primitive_type, $relacao_datatype, $id_entidade_combo, $acesso_coluna, $sql_coluna;
    private $comboDict; //valores que aparecem quando é coluna fk (entidade combo).

    function MetaColuna() {
        
    }

    function getValorInicial($registro) {
        $valor = "";
        if ($this->getPrimitiveType() == PRIMITIVE_BOOLEAN) {
            $valor = "F";
        }
        if ($this->getPrimitiveType() == PRIMITIVE_INT) {
            if ($this->getRelacao() == RELACAO_DATATYPE_PK) {
                $valor = $registro->getValorCampo("seq_registro");
            }
        }

        return $valor;
    }

    function formataValor($v) {
        if ($this->getPrimitiveType() == PRIMITIVE_BOOLEAN) {
            if ($v == "T")
                return txt_true;
            return txt_false;
        }
        if ($this->getEntidadeCombo()) {
            return $this->comboDict->getValor($v);
        }

        return $v;
    }

    function getTipoInput() {

        if ($this->getPrimitiveType() == PRIMITIVE_BOOLEAN) {
            return TIPO_INPUT_BOOLEAN;
        }

        if ($this->getPrimitiveType() == PRIMITIVE_INT) {
            if ($this->getEntidadeCombo()) {
                return TIPO_INPUT_SELECT_FK;
            }

            return TIPO_INPUT_INTEIRO;
        }

        return TIPO_INPUT_TEXTO_CURTO;
    }

    function carregaValoresCombo() {
        $controller = getControllerManager()->getRegistroController();
        $this->comboDict = $controller->fillSelectCombobox($this->id_entidade_combo);

        /*
          usar isso para mostrar, $a=array
          foreach ($a as $k => $v) {
          echo "\$a[$k] => $v.\n";
          } */
    }

    //getters e setters

    function getComboDictionary() {
        return $this->comboDict;
    }

    function setAcessoColuna($v) {
        $this->acesso_coluna = $v;
    }

    function getAcessoColuna() {
        return $this->acesso_coluna;
    }

    function setSqlColuna($v) {
        $this->sql_coluna = $v;
    }

    function getSqlColuna() {
        return $this->sql_coluna;
    }

    function setName($v) {
        $this->name = $v;
    }

    function getName() {
        return $this->name;
    }

    function setId($v) {
        $this->id = $v;
    }

    function getId() {
        return $this->id;
    }

    function setEntidadeCombo($v) {
        if ($v == "")
            unset($v);
        if ($v == "0")
            unset($v);

        $this->id_entidade_combo = $v;
        writeDebug("meta_coluna_model setEntidadeCombo:$v");
        if (isset($v)) {
            $this->carregaValoresCombo();
        }
    }

    function getEntidadeCombo() {
        return $this->id_entidade_combo;
    }

    function setPrimitiveType($v) {
        $this->primitive_type = $v;
    }

    function getPrimitiveType() {
        return $this->primitive_type;
    }

    function setRelacao($v) {
        $this->relacao_datatype = $v;
    }

    function getRelacao() {
        return $this->relacao_datatype;
    }

}

?>