<?

//Model representa 1 registro
class BaseModel extends AbstractModel implements IModel {

    private $arrChildren; //used for hierarchical tree... each child is a model.
    private $viewComponent; //used to define who this model represents visually
    private $_width, $_height;

    function BaseModel($controller, $table) {
        $this->controller = $controller;
        $this->table = $table;
    }

    function setViewComponent($v) {
        $this->viewComponent = $v;
    }

    function getViewComponent() {
        return $this->viewComponent;
    }

    /**
     * width retorna a quantidade maxima de colunas do nó e seus filhos...
     * Se um nó tem 2 filhos, então o width é 2, 
     * se cada filho tiver 2 filhos então o width de cada nós é 2 e dando um total de 4
     */
    function getWidth() {//coberto
        $arrChild = $this->getChildren();
        $size = sizeof($arrChild);
        $ret = 1;
        $max = $size;
        foreach ($arrChild as $child) {
            $v = $child->getWidth();
            if ($v > $max) {
                $max = $v;
            }
        }
        $ret+=$max;
        $this->_width = $ret;
        return $this->_width;
    }

    /**
     * Retorna a quantidade de níveis que o nó possui
     * @return int
     */
    function getHeight() {//coberto
        $arrChild = $this->getChildren();
        $ret = 1;
        $max = 0;
        foreach ($arrChild as $child) {
            $v = $child->getHeight();
            if ($v > $max) {
                $max = $v;
            }
        }
        $ret+=$max;
        $this->_height = $ret;
        return $ret;
    }

    function setViewLeft($left) {
        $this->viewComponent->setAbsoluteLeft($left);
    }

    function setViewTop($v) {
        $this->viewComponent->setAbsoluteTop($v);
    }

    function getChildren() {//coberto
        if (!$this->arrChildren) {
            $this->arrChildren = array();
        }
        return $this->arrChildren;
    }

    function addChild(IModel $child) {//coberto
        $arr = $this->getChildren();
        foreach ($arr as $c) {
            if ($c->getId() == $child->getId()) {
                return;
            }
        }
        array_push($this->arrChildren, $child);
    }

    function addChildToTree($child, $idFather) {
        if ($idFather == $this->getId()) {
            $this->addChild($child);
            return true;
        }

        $arr = $this->getChildren();
        $size = sizeof($arr);
        for ($c = 0; $c < $size; $c++) {
            $son = $arr[$c];
            if ($son->addChildToTree($child, $idFather)) {
                return true;
            }
        }
        return false;
    }

    function getValor(IColuna $coluna) {
        if ($this->isFlagInclusao()) {
            return $coluna->getDefaultValue();
        }
        if (!$coluna) {
            erroFatal("column undefined.");
        }
        $valor = $this->getValorCampo($coluna->getDbName());
        return $valor;
    }

}

?>