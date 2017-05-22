<?

class ABTestVariacaoController extends BaseController {

    function getTotalVariacoes($abtest) {
        return countQuery("select * from abtest_variacao where id_abtest=?", array($abtest));
    }

    function insereRegistro($array) {
        validaAdmin();

        // printArray($array);
        $cod = $this->getTotalVariacoes($array['id_abtest']) + 1;


        $sql = "insert into abtest_variacao (cod_abtest_variacao,nm_abtest_variacao,id_abtest,count_abtest_variacao) values ";
        $sql.="(" . $cod . ",:nm_abtest_variacao,:id_abtest,0)";
        $id = executaSQL($sql, $array);

        return $id;
    }

    function excluirRegistro($id) {
        validaAdmin();
        executaSQL("delete from abtest_variacao where id_abtest_variacao=?", array($id));
    }

    /*


      function countForProjeto($id_projeto){
      $id_projeto=validaNumero($id_projeto,"id_projeto countForProjeto EntidadeController");
      $id="count_diagrama_".projetoAtual();
      if ($this->getCacheInfo($id))return $this->getCacheInfo($id);


      $rs=executaQuery("select count(*) as tot from diagrama where id_projeto=?",array(projetoAtual()));
      if ($row = $rs->fetch()){
      $this->setCacheInfo($id,$row['tot']);
      return $this->getCacheInfo($id);
      }
      }

      function filtrosExtras(){
      return " and diagrama.id_projeto=".projetoAtual();
      }

      function getProjeto($id){
      $id=validaNumero($id,"id getProjeto DiagramController");
      $row=executaQuerySingleRow("select id_projeto from diagrama c where c.id_diagrama=$id");
      return $row['id_projeto'];
      }
     */
}

?>