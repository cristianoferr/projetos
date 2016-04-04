<?

class SecurityManager {

    private $permDict;
    private $arrSecurityManager;

    function __construct() {
        $this->arrSecurityManager = array();
    }

    function addSecurityManager(SecurityCheck $pm) {
        array_push($this->arrSecurityManager, $pm);
    }

    function getPermDict() {
        if (!$this->permDict) {
            $this->permDict = new DicionarioSession("perms");
        }
        return $this->permDict;
    }

    function checkPermCache($modulo, $id_modulo, $flagEdit) {
        if ((isGuest()) && ($flagEdit)) {
            return "F";
        }
        $key = $modulo . "_" . $id_modulo . "_" . $flagEdit;
        $v = $this->getPermDict()->getValor($key);
        if ($v == "T") {
            return "T";
        }
    }

    function setPermCache($modulo, $id_modulo, $flagEdit, $result) {
        $key = $modulo . "_" . $id_modulo . "_" . $flagEdit;
        $v = $result ? "T" : "F";
        $this->getPermDict()->setValor($key, $v);
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        //write("modulo: $modulo $id_modulo $flagEdit");
        $cache = $this->checkPermCache($modulo, $id_modulo, $flagEdit);
        if ($cache) {
            return ($cache == "T") ? true : false;
        }

        foreach ($this->arrSecurityManager as $secMan) {
            if ($secMan->controlsModule($modulo)) {
                $result = $secMan->checkPerm($modulo, $id_modulo, $flagEdit);
                $this->setPermCache($modulo, $id_modulo, $flagEdit, $result);
                return $result;
            }
        }
        return false;
    }

    function setPerm($modulo, $id_modulo, $flagEdit, $perm) {
        if ($perm) {
            $perm = "T";
        } else {
            $perm = "F";
        }
        $this->getPermDict()->setValor($modulo . "_" . $id_modulo . "_" . $flagEdit, $perm);
    }

}

class FileManager extends SecurityManager {

    private $arrModulos;
    private $arrModulosUso;
    private $arrPainelManager;

    function __construct() {
        parent::__construct();
        $this->arrModulos = array();
        $this->arrModulosUso = array();
        $this->arrPainelManager = array();
    }

    function isModuloCarregado($moduloName) {
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            if ($modulo->getName() == $moduloName) {
                return true;
            }
        }
        return false;
    }

    function addPainelManager($pm) {
        array_push($this->arrPainelManager, $pm);
    }

    function loadUniqueJS($js, $ident) {
        if (!$this->isFileLoaded($ident)) {
            echo "<script type='text/javascript' src='$js'></script>";
        }
    }

    function isFileLoaded($ident) {
        if ($GLOBALS["file_$ident"]) {
            return true;
        } else {
            $GLOBALS["file_$ident"] = true;
            return false;
        }
    }

    function countIncludes() {
        $arr = $this->getPHP();
        $c = sizeof($arr);
        return $c;
    }

    function carregaPainelComNome($painel) {
        for ($c = 0; $c < sizeOf($this->arrPainelManager); $c++) {
            $pm = $this->arrPainelManager[$c];
            $p = $pm->getPainelComNome($painel);
            if ($p)
                return $p;
        }
    }

    function adicionaModulo($moduloName) {
        $modulo = $this->getModulo($moduloName);
        if ($this->isModuloCarregado($moduloName))
            return;
        array_push($this->arrModulosUso, $modulo);
    }

    function loadModulos() {
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            if ($modulo->getName() == $moduloName)
                return true;
        }
    }

    function linkCSS($cssFile, ElementMaker $elRoot) {
        criaEl("link", $elRoot)->setType("text/css")->setAtributo("rel", "stylesheet")->setHref(getHomeDir() . $cssFile);
    }

    function linkJS($jsFile, ElementMaker $elRoot) {
        criaEl("script", $elRoot)->setType("text/javascript")->setSrc(getHomeDir() . $jsFile)->setValue();
    }

    function getModulosCarregados() {
        return $this->arrModulosUso;
    }

    function getPrefixoJS() {
        return $this->prefJS;
    }

    function getPrefixoCSS() {
        //echo "pref:".$this->prefCSS;
        return $this->prefCSS;
    }

    function getSufixoCSS() {
        return $this->sufCSS;
    }

    function getSufixoJS() {
        return $this->sufJS;
    }

    function getModulo($moduloName) {
        $modulo = $this->arrModulos[$moduloName];
        if (!$modulo) {
            $modulo = new Modulo($moduloName);
            //$this->arrModulos[$moduloName => $modulo];
            $new_array = array($moduloName => $modulo);
            $this->arrModulos = array_merge($this->arrModulos, $new_array);
        }

        return $modulo;
    }

    function addPHP($file, $moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->addPHP($moduloName . "/" . $file);
    }

    function addCSS($file, $moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->addCSS($file);
    }

    function addJS($file, $moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->addJS($file);
    }

    function printModulosCSS(ElementMaker $elRoot) {
        if (USE_MINIFIED) {
            $this->linkCSS("css/minified.css", $elRoot);
        } else {
            for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
                $modulo = $this->arrModulosUso[$c];
                $modulo->printCSS($this, $elRoot);
            }
        }
    }

    function printModulosJS(ElementMaker $elRoot) {
        if (USE_MINIFIED) {
            $this->linkJS("js/minified.js", $elRoot);
        } else {
            for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
                $modulo = $this->arrModulosUso[$c];
                $modulo->printJS($this, $elRoot);
            }
        }
    }

    function printModulosPHP() {
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            $modulo->printPHP($this);
        }
    }

    function printCSS($moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->printCSS($this);
    }

    function printJS($moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->printJS($this);
    }

    function printPHP($moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->printPHP($this);
    }

    function getPHP() {
        $ret = array();
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            $files = $modulo->getPHP();
            for ($i = 0; $i < sizeOf($files); $i++) {
                $file = $files[$i];
                array_push($ret, $file);
            }
        }

        return $ret;
    }

    function getControllerForTable($table) {
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            $controller = $modulo->getControllerForTable($table);
            if ($controller) {
                return $controller;
            }
        }
    }

    function setControllerManager($c, $moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->setControllerManager($c);
    }

    function setTableManager($tm, $moduloName) {
        $modulo = $this->getModulo($moduloName);
        $modulo->setTableManager($tm);
    }

    function getTableManagerForModulo() {
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            $controller = $modulo->getTableManagerForTable($table);
            if ($controller) {
                return $controller;
            }
        }
    }

    function getTableWithName($tableName) {
        for ($c = 0; $c < sizeOf($this->arrModulosUso); $c++) {
            $modulo = $this->arrModulosUso[$c];
            $table = $modulo->hasTableName($tableName);
            if ($table)
                return $table;
        }
        writeAdmin("Não encontrado: $tableName");
    }

}

class Modulo {

    private $arrCSS;
    private $arrJS;
    private $arrPHP;
    private $controllerManager;
    private $tableManager;
    private $name;

    function __construct($name) {
        $this->name = $name;
        $this->arrCSS = array();
        $this->arrJS = array();
        $this->arrPHP = array();
    }

    function getName() {
        return $this->name;
    }

    function setTableManager($tm) {
        $this->tableManager = $tm;
    }

    function hasTableName($tableName) {
        if (!$this->tableManager)
            return;
        return $this->tableManager->carregaTabelaComNome($tableName);
    }

    function setControllerManager($c) {
        $this->controllerManager = $c;
    }

    function getControllerForTable($table) {
        if (!$this->controllerManager) {
            return;
        }
        return $this->controllerManager->getControllerForTable($table);
    }

    function addJS($file) {
        array_push($this->arrJS, $file);
    }

    function addPHP($file) {
        array_push($this->arrPHP, $file);
    }

    function addCSS($file) {
        array_push($this->arrCSS, $file);
    }

    function getPHP() {
        return $this->arrPHP;
    }

    function printPHP($fm) {
        for ($c = 0; $c < sizeOf($this->arrPHP); $c++) {
            $link = $this->arrPHP[$c];
            include($page);
        }
    }

    function printCSS(FileManager $fm, ElementMaker $elRoot) {
        for ($c = 0; $c < sizeOf($this->arrCSS); $c++) {
            $link = $this->arrCSS[$c];
            $fm->linkCSS($link, $elRoot);
        }
    }

    //echo $fm->getPrefixoCSS() . $link . $fm->getSufixoCSS();
    // $fileManager->defineCSS("<link type=\"text/css\" rel=\"stylesheet\" href=\"" . getHomeDir(), "\" />\n");
    // $fileManager->defineJS("<script type=\"text/javascript\" src=\"" . getHomeDir(), "\"></script>");

    function printJS(FileManager $fm, ElementMaker $elRoot) {
        for ($c = 0; $c < sizeOf($this->arrJS); $c++) {
            $link = $this->arrJS[$c];
            $fm->linkJS($link, $elRoot);


            //echo $fm->getPrefixoJS() . $link . $fm->getSufixoJS();
        }
    }

}

?>