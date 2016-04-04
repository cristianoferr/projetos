<?

class ComponentDiagramController extends BaseController {

    function insereRegistro($array) {
        
    }

    function filtrosExtras() {
        return " and componente_diagrama.id_diagrama=d.id_diagrama and d.id_tipo_diagrama=td.id_tipo_diagrama";
    }

    function tabelasExtras() {
        return ",diagrama d,tipo_diagrama td";
    }

    function verificaComponentes($id_diagrama, $idTipoDiagrama, $flagShowAll) {
        if ($flagShowAll) {
            $tipoController = getControllerForTable("tipo_diagrama");
            $modelTipoDiagrama = $tipoController->loadSingle($idTipoDiagrama);
            $componentRaiz = $this->loadComponentes($id_diagrama, $modelTipoDiagrama);
            $array = $componentRaiz->getArrayUseableObjects();

            //verifico se os componentes estão todos no diagrama...
            $arrComponentes = $this->atualizaComponentes($id_diagrama, $array);
            if ($arrComponentes) {
                // printArray($arrComponentes);
                //se houve novo componente então verifico a posição de acordo com o tipo...
                //   $tipoController->geraPosicao($id_diagrama, $arrComponentes, $modelTipoDiagrama);
            }
        }
    }

    //pega os componentes informados pelo usuario e insere os novos e remove  quem não estiver na lista
    function atualizaComponentes($id_diagrama, Array $arrComponentes) {
        $this->desativaComponentes($id_diagrama);

        // writeAdmin("atualizaComponentes $id_diagrama");
        $arrAtual = $this->loadArrayComponentesDiagrama($id_diagrama);
        //writeAdmin("Array atual:");
        // printArray($arrAtual);
        //writeAdmin("Array Componentes:");
        //printArray($arrComponentes);
        $arrComponentes = ComponentLoader::geraDiferencaComponentes($arrComponentes, $arrAtual);
        $flagNew = false;
        foreach ($arrComponentes as $comp) {
            $pk = ComponentLoader::getPKDoTipo($comp->getTipo());
            // writeAdmin("idComp:" . $comp->getId() . " selected? " . $comp->isSelected() . " pk:$pk selecionavel?" . $comp->isSelecionavel());
            //  if (($comp->getId()) && ($comp->getId() != TIPO_COMPONENTE_TITULO)) {
            if ($comp->isSelecionavel()) {
                $pk = ComponentLoader::getPKDoTipo($comp->getTipo());
                if ($comp->isSelected()) {
                    //componente já existe, só deixo ativo
                    executaSQL("update componente_diagrama set flag_inativo='F' where id_diagrama=? and $pk=?", array($id_diagrama, $comp->getId()));
                } else {
                    $flagNew = true;
                    executaSQL("insert into componente_diagrama ($pk,id_diagrama,flag_inativo,posx,posy) values (?,?,'F',?,?)", array($comp->getId(), $id_diagrama, rand(MIN_WIDTH, 500), rand(MIN_HEIGHT, 500)));
                }
            }
        }
        if (flagNew)
            return $arrComponentes;
        return false;
    }

    function desativaComponentes($id_diagrama) {
        executaSQL("update componente_diagrama set flag_inativo='T' where id_diagrama=?", array($id_diagrama));
    }

    function loadAtivosDiagrama($id_diagrama) {
        $this->loadRegistros("and diagrama.id_diagrama=$id_diagrama and flag_inativo='F'");
    }

    function updatePosicao($id_diagrama, $id_componente, $posx, $posy) {
        executaSQL("update componente_diagrama set posx=?,posy=? where id_componente_diagrama=? and id_diagrama=?", array($posx, $posy, $id_componente, $id_diagrama));
    }

    function pegaPosicao($id_diagrama, $id_componente) {

        $row = executaQuerySingleRow("select * from componente_diagrama where id_componente_diagrama=? and id_diagrama=?", array($id_componente, $id_diagrama));
        return $row;
    }

    //retorna os componentes que estão em uso pelo diagrama informado
    function loadArrayComponentesDiagrama($id_diagrama) {
        $this->loadRegistros("and componente_diagrama.id_diagrama=$id_diagrama");
        $arr = array();
        while ($model = $this->next()) {
            array_push($arr, $model);
        }

        return $arr;
    }

    function loadComponentes($idDiagrama, BaseModel $modelTipoDiagrama) {
        $component = ComponentLoader::loadComponenteProjeto(projetoAtual(), $idDiagrama, $modelTipoDiagrama);

        return $component;
    }

}

?>