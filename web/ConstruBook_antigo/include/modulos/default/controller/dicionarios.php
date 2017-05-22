<?

class DicionarioSession {

    //private $arrStrings;
    private $name;

    function DicionarioSession($name) {
        $this->name = $name;
        //$this->arrStrings=array();
    }

    /*
      function setValor($key,$valor){
      //$this->arrStrings[$key]=$valor;
      }
      function getValor($key){

      //return $this->arrStrings[$key];
      } */

    function getValor($key) {
        if (!isset($_SESSION[$this->name . "_$key"])) {
            return false;
        }
        return $_SESSION[$this->name . "_$key"];
    }

    function setValor($key, $valor) {
        $_SESSION[$this->name . "_$key"] = $valor;
    }

}

class Dicionario {

    private $arrStrings;
    private $arrAuxiliar;

    function Dicionario() {
        $this->arrStrings = array();
        $this->arrAuxiliar = array();
    }

    function setValor($key, $valor, $aux = null) {
        $this->arrStrings[$key] = $valor;
        if ($aux) {
            $this->arrAuxiliar[$key] = $aux;
        }
    }

    function getValor($key) {
        return $this->arrStrings[$key];
    }

    function getAuxiliar($key) {
        if (isset($this->arrAuxiliar[$key])) {
            return $this->arrAuxiliar[$key];
        }
    }

    function count() {
        return sizeof($this->arrStrings);
    }

    function getArray() {
        return $this->arrStrings;
    }

}

class InternationalDict extends DicionarioSession {

    private $id_lang;

    function InternationalDict($id_lang) {
        $this->id_lang = $id_lang;
    }

    function adicionaValores() {
        $this->setValor("site_name", "ConstruBook");
        $this->setValor("main_titulo_chamada", "ConstruBook");
        $this->setValor("login_forgot_password", "Esqueci a senha");
        $this->setValor("login_account", "Login");
        $this->setValor("txt_fornecedores", "Fornecedores");
        $this->setValor("txt_buscar_fornecedores", "Procurar Fornecedores");
        $this->setValor("txt_meus_fornecedores", "Meus Fornecedores");
        $this->setValor("txt_produtos", "Produtos");
        $this->setValor("txt_buscar_produtos", "Buscar Produtos");
        $this->setValor("txt_categorias", "Categorias");
        $this->setValor("txt_categoria", "Categoria");
        $this->setValor("txt_navegar_categorias", "Navegar nas Categorias");
        $this->setValor("txt_nm_fornecedor", "Fornecedor");
        $this->setValor("txt_fornecedor", "Fornecedor");
        $this->setValor("txt_of", "de");
        $this->setValor("txt_usuario", "Usuário");
        $this->setValor("txt_atividade", "Atividade");
        $this->setValor("txt_edit", "Editar");
        $this->setValor("txt_create_account", "Criar Conta");
        $this->setValor("txt_escolher_perfil", "Perfil em uso");

        //menu Meu fornecedor
        $this->setValor("txt_menu_fornecedor", "Menu Fornecedor");
        $this->setValor("txt_meus_produtos", "Meus Produtos");
        $this->setValor("txt_tabela_calculo", "Tábela de Cálculo");
        $this->setValor("txt_meus_clientes", "Meus Clientes");
        $this->setValor("txt_solicitacoes_clientes", "Solicitações");
        $this->setValor("txt_configuracao", "Configuração");
        $this->setValor("txt_busca", "Busca");
        $this->setValor("txt_menu_cliente", "Menu Cliente");
        $this->setValor("txt_minhas_solicitacoes", "Minhas Solicitações");

        //menu Usuario
        $this->setValor("txt_my_profile", "Meu Perfil");
        $this->setValor("txt_logout", "Sair");

        //tela fornecedor
        $this->setValor("txt_mural", "Mural");
        $this->setValor("txt_parceiros", "Parceiros");
        $this->setValor("txt_solicitar_acesso", "Solicitar Acesso");
        $this->setValor("txt_solicitar_parceria", "Solicitar Parceria");

        //Produto
        $this->setValor("txt_editar_produto", "Editar Produto");
        $this->setValor("txt_nm_produto", "Produto");
        $this->setValor("txt_vlr_unitario", "Valor Unitário");

        $this->setValor("hint_nm_produto", "Nome do Produto");
        $this->setValor("hint_desc_produto", "Descrição do Produto, tente ser conciso, mostrando as principais qualidades do mesmo.");
        $this->setValor("txt_desc_produto", "Descrição");
        //
        $this->setValor("txt_cancel", "Cancelar");
        $this->setValor("txt_password_placeholder", "Senha");
        $this->setValor("txt_email_placeholder", "Login");

        $this->setValor("register_account", "Criar conta");
        $this->setValor("txt_login", "Login");
    }

    function language() {
        return $this->id_lang;
    }

    function getDefaultLang() {
        return $this->id_lang;
    }

    function translateKey($int_txt) {
        $int_txt = validaTexto($int_txt);
        if (!$this->getValor($int_txt)) {
            $this->adicionaValores();
            if (!$this->getValor($int_txt)) {
                return "[" . $int_txt . "]";
            }
        }
        return $this->getValor($int_txt);
    }

}

class PosicaoDiagrama extends DicionarioSession {

    private $id_diagrama;

    function PosicaoDiagrama($cod_dict, $id_diagrama) {
        DicionarioSession::DicionarioSession($cod_dict);
        $this->id_diagrama = $id_diagrama;
    }

    function getPosX($id_componente) {
        $pos = DicionarioSession::getValor($id_componente . "_x");
        if (!$pos) {
            $this->pegaPosicaoBanco($id_componente);
        }
        $pos = DicionarioSession::getValor($id_componente . "_x");
        return $pos;
    }

    function getPosY($id_componente) {
        $pos = DicionarioSession::getValor($id_componente . "_y");
        if (!$pos) {
            $this->pegaPosicaoBanco($id_componente);
        }
        $pos = DicionarioSession::getValor($id_componente . "_y");
        return $pos;
    }

    function updatePosicao($id_componente, $posx, $posy) {
        $this->setValor($id_componente . "_x", $posx);
        $this->setValor($id_componente . "_y", $posy);
        $controller = getControllerForTable("componente_diagrama");
//        echo "update($this->id_diagrama, $id_componente, $posx, $posy)";
        $controller->updatePosicao($this->id_diagrama, $id_componente, $posx, $posy);
    }

    function pegaPosicaoBanco($id_componente) {
        $controller = getControllerForTable("componente_diagrama");
        $row = $controller->pegaPosicao($this->id_diagrama, $id_componente);

        $this->setValor($id_entidade . "_x", $row['posx']);
        $this->setValor($id_entidade . "_y", $row['posy']);
    }

}

?>