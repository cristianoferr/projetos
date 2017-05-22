<?

class ViewBase extends GerenciaLinks {

    private $titulo;
    protected $controller;
    private $linkTitulo;
    private $flagModal; //se for modal não é mostrado o rodapé
    private $editWidget; //uma string com o nome do widget em uso
    private $newWidget; //uma string com o nome do widget em uso
    private $editLink;
    private $deleteLink;
    private $widgetId;
    private $inputCreator;

    function __construct() {
        parent::__construct();
    }

    function getInputCreator() {
        return $this->inputCreator;
    }

    function setModal($v) {
        $this->flagModal = $v;
    }

    function isModal() {
        return $this->flagModal;
    }

    function setEditWidget($nome) {
        $this->editWidget = $nome;
    }

    function getEditWidget(IModel $model = null) {
        if ($model) {
            if (!$this->checaUsuarioPodeEditar($model)) {
                return false;
            }
        }
        return $this->editWidget;
    }

    /**
     * Usado para pegar o link... não deve ser usado para ver se há permissão
     * @return type
     */
    function getEditWidgetLink() {
        return $this->editWidget;
    }

    function checaUsuarioPodeEditar(IModel $model) {
        $controller = $this->getController();
        //$table = $controller->getTable();
        //return checaEscrita($table->getTableName(), $model->getId());
        return $controller->checkIfUserCanWrite($model);
    }

    function setNewWidget($nome) {
        $this->newWidget = $nome;
    }

    function getNewWidget() {
        return $this->newWidget;
    }

    function setHorizontal() {
        $this->inputCreator->setHorizontal();
    }

    //edit links devem ser no formato "deliverables/<projeto/"
    function setDeleteLink($nome) {
        $this->deleteLink = $nome;
    }

    function getDeleteLink(IModel $model = null) {
        if ($model) {
            if (!$this->checaUsuarioPodeEditar($model)) {
                writeDebug("não pode gravar: $model");
                return false;
            }
        }
        return $this->deleteLink;
    }

    function setEditLink($nome) {
        $this->editLink = $nome;
    }

    function setWidgetId($id) {
        $this->widgetId = $id;
    }

    function getWidgetId() {
        return $this->widgetId;
    }

    function getEditLink(IModel $model = null) {
        return $this->editLink;
    }

    function generateEditWidget($id) {
        return getWidgetManager()->generateEditButtonForWidget($this->getEditWidgetLink(), $id);
    }

    function showEditWidget($id) {
        $this->generateEditWidget($id)->mostra();
    }

    function showNewWidget(ElementMaker $elRoot = null) {
        if (!$this->newWidget) {
            return;
        }
        $widget = getWidgetManager()->createNewButtonForWidgetModal($this->newWidget, $this->getWidgetId());
        if ($elRoot) {
            $elRoot->addElement($widget->generate());
        } else {
            $widget->mostra();
        }
    }

    function generateEditLink($link, $id, $formName) {
        return Out::createEditLinkForLink($link, $id, $formName);
    }

    function generateDeleteLink($link, $id, $formName) {
        return Out::createDeleteLinkForLink($link, $id, $formName);
    }

    function cabecalho() {
        
    }

    function rodape() {
        
    }

    function escreveTitulo(ElementMaker $elRoot = null) {
        if ($this->isModal()) {
            return;
        }
        if (!$this->getTitulo()) {
            return;
        }

        $elTitulo = elMaker("caption", $elRoot);
        if ($this->getLinkTitulo()) {
            $elLink = elMaker("a", $elTitulo)->setHref($this->getLinkTitulo());
        } else {
            $elLink = elMaker("span", $elTitulo);
        }
        $elLink->setValue($this->getTitulo());
        if (!$elRoot) {
            $elTitulo->mostra();
        }
        return $elTitulo;
    }

    function setTitulo($v) {
        $this->titulo = $v;
    }

    function setTituloNovoItem() {
        $this->titulo = $this->getController()->getNewItem()->getText();
    }

    function setTituloPlural() {
        $this->titulo = $this->getController()->getPlural();
    }

    function getTitulo() {
        return $this->titulo;
    }

    function setLinkTitulo($v) {
        $this->linkTitulo = $v;
    }

    function getLinkTitulo() {
        return $this->linkTitulo;
    }

    function setController(IController $controller) {
        if (!isset($controller)) {
            die("Controller não definido no painel. " . $this->getFormName());
        }

        $this->inputCreator = new InputBusiness($controller);
        $this->controller = $controller;
    }

    function getController() {
        return $this->controller;
    }

}

?>