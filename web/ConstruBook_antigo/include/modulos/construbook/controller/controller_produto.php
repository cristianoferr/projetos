<?

class ProdutoController extends BaseController {

    function insereRegistro($array) {

        // printArray($array);
        $sql = "insert into produto (nm_abtest,url_abtest,desc_abtest,id_tipo_abtest) values ";
        $sql.="(:nm_abtest,:url_abtest,:desc_abtest,:id_tipo_abtest)";
        $id = executaSQL($sql, $array);


        return $id;
    }

    function listaProdutosDoFornecedor($id,$painel=null){
        return $this->loadRegistros("and produto.id_fornecedor=$id", $painel);
    }
    
    function getRandomPicture($idProduto){
        $controller=  getControllerForTable("produto_media");
        $controller->setDefaultOrderBy("rand()");
        $controller->loadRegistros("and id_produto=$idProduto");
        return $controller->next();
    }
}


?>