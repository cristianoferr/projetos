<?

class FornecedorController extends BaseController {

    public static $CAMPO_MEDIA_LOGO = 'id_media_logo';
    public static $CAMPO_NOME = 'nm_fornecedor';
    public static $CAMPO_USUARIO_ID = 'id_usuario';
    private $permDict;

    function iniciaCorrigeDados() {
        parent::iniciaCorrigeDados();
        $this->permDict = new DicionarioSession("forn");
    }

    function loadPerfisUsuario($idUsuario, $painel = null) {
        return $this->loadRegistros("and id_usuario=$idUsuario", $painel);
    }

    function setPerfilAtual($perfil) {
        $keyC = "perfilAtualC";
        $keyV = "perfilAtualV";
        $this->permDict->setValor($keyV, $perfil);
        $this->permDict->setValor($keyC, true);
    }

    function perfilAtual() {
        $keyC = "perfilAtualC";
        $keyV = "perfilAtualV";
        $val = $this->permDict->getValor($keyC);
        if ($val) {
            return $this->permDict->getValor($keyV);
        }

        $this->permDict->setValor($keyV, PERFIL_CLIENTE);
        $this->permDict->setValor($keyC, true);

        return $this->permDict->getValor($keyV);
    }

    function insereRegistro($array) {

        // printArray($array);
        $sql = "insert into fornecedor (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);

        $varContr = getControllerForTable("abtest_variacao");
        $varContr->insereRegistro(array("nm_abtest_variacao" => "variação 1", "id_abtest" => $id));
        $varContr->insereRegistro(array("nm_abtest_variacao" => "variação 2", "id_abtest" => $id));

        return $id;
    }

    function listaFornecedoresConectados($painel = null) {
        return $this->loadRegistros("and id_fornecedor in (select id_fornecedor from cliente_fornecedor where id_usuario=" . usuarioAtual() . ")", $painel);
    }

    function listaParceirosDoFornecedor($idFornecedor, $painel = null) {
        return $this->loadRegistros("and id_fornecedor=$idFornecedor and id_fornecedor in (select id_fornecedor_to from parceiro where id_fornecedor_from=$idFornecedor)", $painel);
    }

    function getCodigoFornecedorUsuario($idUsuario = null) {
        if ($idUsuario == null) {
            $idUsuario = usuarioAtual();
        }
        $this->loadRegistros("and id_usuario=$idUsuario", null);
        $model = $this->next();
        $id = $model->getValorCampo(FornecedorController::$CAMPO_USUARIO_ID);
        return $id;
    }

    function usuarioEhFornecedor($idFornecedor) {
        $idUsuario = usuarioAtual();
        $idFornecedor = validaNumero($idFornecedor);
        $this->loadRegistros("and id_usuario=$idUsuario and id_fornecedor=$idFornecedor", null);
        $model = $this->next();
        return ($model != null);
    }

    function isUsuarioFornecedor($idUsuario = null) {
        if ($idUsuario == null) {
            $idUsuario = usuarioAtual();
        }
        $tot = $this->countRegistros("and id_usuario=" . $idUsuario . "");
        return $tot > 0;
    }

    function usuarioPodeListarProdutosDoFornecedor($idFornecedor) {
        $keyC = "uplpdfC$idFornecedor";
        $keyV = "uplpdfV$idFornecedor";
        $val = $this->permDict->getValor($keyC);
        if ($val) {
            return $this->permDict->getValor($keyV);
        }

        $this->permDict->setValor($keyC, true);
        $idUsuario = usuarioAtual();
        $tot = $this->countRegistros("and id_fornecedor=$idFornecedor and id_fornecedor in (select id_fornecedor from cliente_fornecedor where id_usuario=" . $idUsuario . ")");
        $this->permDict->setValor($keyV, $tot > 0);
    }

    function fornecedorAtualParceiroFornecedor($idFornecedor) {
        $keyC = "fapfC$idFornecedor";
        $keyV = "fapfV$idFornecedor";
        $val = $this->permDict->getValor($keyC);
        if ($val) {
            return $this->permDict->getValor($keyV);
        }
        $this->permDict->setValor($keyC, true);

        $val = false;
        if (fornecedorAtual() == $idFornecedor) {
            $val = true;
        } else {
            $tot = $this->countRegistros("and id_fornecedor=$idFornecedor and id_fornecedor in (select id_fornecedor_from from parceiro where id_fornecedor_to=" . fornecedorAtual() . ")");
            $val = ($tot > 0);
        }

        $this->permDict->setValor($keyV, $val);

        return $val;
    }

    //usuario está na tabela cliente_fornecedor
    function usuarioAtualAfiliadoFornecedor($idFornecedor) {
        return $this->usuarioPodeListarProdutosDoFornecedor($idFornecedor);
    }

    function usuarioPodeListarParceirosDoFornecedor($idFornecedor) {
        return $this->usuarioPodeListarProdutosDoFornecedor($idFornecedor);
    }

    function fornecedorAtualAfiliadoFornecedor($idFornecedor) {
        $keyC = "faafC$idFornecedor";
        $keyV = "faafV$idFornecedor";
        $val = $this->permDict->getValor($keyC);
        if ($val) {
            return $this->permDict->getValor($keyV);
        }

        $idFornecedorAtual = fornecedorAtual();
    }

}

function validaFornecedor() {
    if (!isFornecedor()) {
        logaAcesso("Tentativa de acesso à pagina de Fornecedor", true);
        die("É necessário ser fornecedor para acessar essa página... ");
    }
}

function isFornecedor() {
    return !(fornecedor()->perfilAtual() == PERFIL_CLIENTE);
}

function fornecedorAtual() {
    /* if (isset($_SESSION["fornecedorAtual"])) {
      return $_SESSION["fornecedorAtual"];
      } */

    $_SESSION["fornecedorAtual"] = fornecedor()->getCodigoFornecedorUsuario();
    return$_SESSION["fornecedorAtual"];
}

function fornecedor() {
    return getControllerForTable("fornecedor");
}

?>