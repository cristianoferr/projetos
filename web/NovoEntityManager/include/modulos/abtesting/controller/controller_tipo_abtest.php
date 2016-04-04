<?

class TipoABTestController extends BaseController {

    function insereRegistro($array) {
        validaAdmin();

        // printArray($array);

        $sql = "insert into tipo_abtest (nm_tipo_abtest,id_pagina,flag_guest_only) values ";
        $sql.="(:nm_tipo_abtest,:id_pagina,:flag_guest_only)";
        $id = executaSQL($sql, $array);

        return $id;
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