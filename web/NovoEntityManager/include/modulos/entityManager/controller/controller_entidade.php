<?

class EntidadeController extends BaseController {

    private $flagtodos;

    function carregaTodos() {//coberto
        $this->flagtodos = true;
    }

    function carregaNormal() {//coberto
        $this->flagtodos = false;
    }

    function corrigeDados() {//coberto
        $rs = executaQuery("select * from entidade where id_coluna_pk is null or id_coluna_pk=0");
        while ($row = $rs->fetch()) {
            $id_entidade = $row["id_entidade"];
            $rsColuna = executaQuery("select * from coluna where id_entidade_pai=? and id_relacao_datatype=1", array($id_entidade));
            $c = 0;
            while ($rowColuna = $rsColuna->fetch()) {
                $idColuna = $rowColuna['id_coluna'];
                $c++;
            }
            if ($c == 1) {
                executaSQL("update entidade set id_coluna_pk=? where id_entidade=?", array($idColuna, $id_entidade));
            }
        }

        executaSQL("update entidade set id_entidade_extends=null where id_entidade_extends=id_entidade");
    }

    function getPKForEntidade($id_entidade) {//coberto
        $v = $this->flagtodos;
        $this->flagtodos = true;
        $model = $this->loadSingle($id_entidade);
        $this->flagtodos = $v;
        $id_coluna_pk = $model->getValorCampo("id_coluna_pk");
        if ($id_coluna_pk) {
            return $id_coluna_pk;
        }

        $pk = getControllerForTable("coluna")->calcPKForEntidade($id_entidade);
        if ($pk) {
            $this->atualizaValor($id_entidade, "id_coluna_pk", $pk);
        }
        return $pk;
    }

    //o filtro serve para não mostrar entidades de fora do projeto atual...
    function filtrosExtras() {
        if ($this->flagtodos) {
            return;
        }
        if (!projetoAtual()) {
            return;
        }
        return " and id_projeto=" . projetoAtual();
    }

    //TODO: Implementar id_entregavel_raiz
    function consultaEntidadesProjeto($id_projeto, $id_entregavel_raiz) {//coberto
        $id_projeto = validaNumero($id_projeto, "id_projeto consultaEntidadesProjeto EntidadeController");
        $filtro = $this->getFiltroEntregavel($id_entregavel_raiz);
        $rs = executaQuery("select e.*,p.nm_projeto from entidade e,projeto p where e.id_projeto=p.id_projeto $filtro and p.id_projeto=? order by nm_entidade", array($id_projeto));
        return $rs;
    }

    function getFiltroEntregavel($id_entregavel_raiz) {
        $arrEntregavel = getControllerForTable("entregavel")->getHierarchicalDeliverables($id_entregavel_raiz);
        $filtro = "(";
        foreach ($arrEntregavel as $rowEntr) {
            $filtro.="," . $rowEntr['id_entregavel'];
        }
        $filtro = replaceString($filtro, "(,", "(") . ")";
        $filtro = "and id_entregavel in $filtro";
        if ($filtro == "and id_entregavel in ()") {
            $filtro = "";
        }
        return $filtro;
    }

    function getProjeto($id_entidade) {//coberto
        return $this->getFieldFromModel($id_entidade, "id_projeto");
    }

    function getEntidadeID($nm_entidade) {//coberto
        $nm_entidade = validaTexto($nm_entidade, "nm_entidade getEntidadeID EntidadeController");
        $id = "id_entidade_$nm_entidade" . "_" . projetoAtual();
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }

        $rs = executaQuery("select id_entidade from entidade where nm_entidade=? and id_projeto=?", array($nm_entidade, projetoAtual()));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['id_entidade']);
            return $this->getCacheInfo($id);
        }
    }

    function countForProjeto($id_projeto) {//coberto
        $id_projeto = validaNumero($id_projeto, "id_projeto countForProjeto EntidadeController");
        $id = "count_entidade_" . projetoAtual();
        if ($this->getCacheInfo($id))
            return $this->getCacheInfo($id);


        $rs = executaQuery("select count(*) as tot from entidade where id_projeto=?", array($id_projeto));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['tot']);
            return $this->getCacheInfo($id);
        }
    }

    function insereRegistro($array) {//coberto
        validaEscrita("projeto", $array['projeto']);


        $row = executaQuerySingleRow("select id_entidade from entidade where id_projeto=? and nm_entidade=?", array($array['projeto'], $array['nm_entidade']));
        if ($row) {
            return $row['id_entidade'];
        }

        $sql = "insert into entidade (id_projeto,nm_entidade,dbname_entidade,classname_entidade,
			package_entidade,id_camada,id_entregavel,flag_classe,flag_banco) values ";
        $sql.="(:projeto,:nm_entidade,:nm_entidade,:nm_entidade,
			:package_entidade,:id_camada,:id_entregavel,'F','T')";
        //echo $sql;
        //printArray($array);
        $id_entidade = executaSQL($sql, $array);

        return $id_entidade;
    }

    function excluirRegistro($id_entidade) {//coberto
        $id_entidade = validaNumero($id_entidade, "id_entidade excluirRegistro EntidadeController");
        validaEscrita("entidade", $id_entidade);
        if ($this->usuarioPodeExcluir($id_entidade)) {
            executaSQL("delete from coluna where id_entidade_pai=?", array($id_entidade));
            executaSQL("delete from entidade where id_entidade=?", array($id_entidade));
        }
    }

    function excluirRegistrosProjeto($id_projeto) {
        validaEscrita("projeto", $id_projeto);
        writeDebug("excluirRegistrosProjeto: validando $id_projeto");
        $id_projeto = validaNumero($id_projeto);
        executaSQL("delete from entidade where id_projeto=?", array($id_projeto));
    }

    function usuarioPodeExcluir($id_registro) {//coberto
        $id_registro = validaNumero($id_registro, "id_registro usuarioPodeExcluir EntidadeController");
        $row = executaQuerySingleRow("select up.id_projeto from usuario_projeto up,entidade e,papel p where up.id_usuario=? and up.id_projeto=e.id_projeto and e.id_entidade=? and p.id_papel=up.id_papel and p.flag_editor='T'", array(usuarioAtual(), $id_registro));
        if ($row) {
            return true;
        }
        return false;
    }

    function usuarioPodeAcessar($id_registro) {
        $id_registro = validaNumero($id_registro, "id_registro usuarioPodeAcessar EntidadeController");
        return existsQuery("select * from usuario_projeto up,entidade e where up.id_usuario=? and up.id_projeto=e.id_projeto and e.id_entidade=?", array(usuarioAtual(), $id_registro));
    }

}

?>