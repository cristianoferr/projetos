<?

class EntregavelController extends BaseController {

    function filtrosExtras() {
        return " and entregavel.id_projeto=" . projetoAtual();
    }

    function getOrderBy() {
        return "id_entregavel_pai=0,nm_entregavel";
    }

    function getOrderSelect() {
        return "id_entregavel,nm_entregavel";
    }

    function getEntregavelSelecionadoExportar($id_projeto) {
        $userController = getControllerForTable("usuario");
        $id = "root_deliv_$id_projeto";
        $id_entregavel = $userController->getEstadoInterface($id, $this->getEntregavelRaiz());
        return $id_entregavel;
    }

    function atualizaEntregavelSelecionadoExportar($id_projeto, $id_entregavel) {
        $userController = getControllerForTable("usuario");
        $id = "root_deliv_$id_projeto";
        $userController->atualizaInterfaceUsuario($id, $id_entregavel);
    }

    function getIdStatusEntregavel($id_Entr) {//coberto
        return $this->getFieldFromModel($id_Entr, "id_status_entregavel");
    }

    function excluirRegistro($id) {//coberto
        $id = validaNumero($id, "excluiregistro idcol");
        validaEscrita("entregavel", $id);
        executaSQL("delete from entregavel where id_entregavel=?", array($id));
    }

    function getEntregavelRaiz() {//coberto
        $this->loadRegistros("and id_entregavel_pai=0");
        if ($model = $this->next()) {
            return $model->getId();
        }
    }

    function getHierarchicalDeliverables($entregavel, $arr = array()) {//coberto
        $sql = "select * from entregavel where id_entregavel=?";
        if (sizeof($arr) == 0) {
            $row = executaQuerySingleRow($sql, array($entregavel));
            if ($row) {
                array_push($arr, $row);
            }
        }


        $sql = "select * from entregavel where id_entregavel_pai=?";
        $rs = executaQuery($sql, array($entregavel));
        while ($row = $rs->fetch()) {
            array_push($arr, $row);
            $id = $row['id_entregavel'];
            $arr = $this->getHierarchicalDeliverables($id, $arr);
        }

        return $arr;
    }

    function insereEntregavelRaiz($id_projeto, $cod_projeto) {

        $arr = array('id_projeto' => $id_projeto,
            'nm_entregavel' => $cod_projeto,
            'desc_entregavel' => translateKey("txt_entregavel_desc_initial_value"),
            'id_entregavel_pai' => 'null',
            'id_status_entregavel' => STATUS_ENTREG_PLANEJADO
        );

        $this->insereRegistro($arr);
    }

    function countForProjeto($id_projeto) {//coberto
        $id_projeto = validaNumero($id_projeto, "id_projeto countForProjeto EntidadeController");
        $id = "count_entregavel_" . projetoAtual();
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }

        $rs = executaQuery("select count(*) as tot from entregavel where id_projeto=?", array($id_projeto));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['tot']);
            return $this->getCacheInfo($id);
        }
    }

    function insereRegistro($array) {//coberto
        //echo "$nm_projeto,$iniciais_projeto,$package_projeto,$id_tipo_banco,$id_metodologia";
        validaEscrita("projeto", $array['id_projeto']);
        $array['nm_entregavel'] = validaTexto($array['nm_entregavel']);
        $array['desc_entregavel'] = validaTexto($array['desc_entregavel']);
        $array['id_entregavel_pai'] = validaNumero($array['id_entregavel_pai'], "id_entregavel_pai insereRegistro EntregavelController");
        $array['id_status_entregavel'] = validaNumero($array['id_status_entregavel'], "id_status_entregavel insereRegistro EntregavelController");

        $sql = "insert into entregavel (id_projeto,nm_entregavel,desc_entregavel,id_entregavel_pai,id_status_entregavel) values ";
        $sql.="(:id_projeto,:nm_entregavel,:desc_entregavel,:id_entregavel_pai,:id_status_entregavel)";

        $id_entidade = executaSQL($sql, $array);

        return $id_entidade;
    }

    function getProjeto($id) {//coberto
        $id = validaNumero($id, "id getProjeto EntregavelController");
        $row = executaQuerySingleRow("select id_projeto from entregavel c where c.id_entregavel=$id");
        return $row['id_projeto'];
    }

    //retorna um combo hierarquico
    function geraArrayComChaveDescricao($filtro = null) {//coberto
        if (isset($this->arrayComboBox)) {
            return $this->arrayComboBox;
        }

        $contr = new EntregavelController($this->getTable());
        $contr->loadRegistros($filtro);
        $arrTree = $contr->createModelTree("id_entregavel_pai");

        //printArray($arrTree);
        $dict = $this->generateDictFromTree($arrTree);

        $this->arrayComboBox = $dict;
        return $dict;
    }

    /**
     * Retorna uma array com os elementos da árvore de modelos passada 
     * 
     * @param type $arr
     * @param type $arrFinal
     * @param type $flagAddLevel
     * @param type $nivel
     */
    function generateDictFromTree($arrTree, Dicionario $dict = null, $flagAddLevel = true, $nivel = "", $espaco = "") {//coberto
        if (!$dict) {
            $dict = new Dicionario();
        }

        $c = 0;
        if ($flagAddLevel) {
            $espaco.="  ";
        }
        foreach ($arrTree as $model) {
            // write("model: $model desc:$campoDesc size:" . sizeof($arrTree));
            $dados = $model->getDados();
            $name = $model->getDescricao();
            if ($flagAddLevel) {
                $n = $nivel . ($c + 1) . ".";
                $name = $espaco . $n . " " . $name;
            }
            $dados['name'] = $name;
            $dict->setValor($model->getId(), $dados);

            $arr = $model->getChildren();
            $this->generateDictFromTree($arr, $dict, $flagAddLevel, $n, $espaco);
            $c++;
        }
        return $dict;
    }

}

?>