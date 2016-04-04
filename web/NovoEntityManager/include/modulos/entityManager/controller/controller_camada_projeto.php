<?
class CamadaProjetoController extends BaseController{

		function filtrosExtras(){
			return " and camada_projeto.id_projeto=".projetoAtual();
		}

		function carregaCamadasNoProjeto($id_projeto,$id_metodologia){
			executaSQL("delete from camada_projeto where id_projeto=?",array($id_projeto));
			$rs=executaQuery("select * from camada_metodologia cm,camada c where c.id_camada=cm.id_camada and cm.id_metodologia=?",array($id_metodologia));
			while ($row= $rs->fetch()){
				$id_camada=$row['id_camada'];
				$cor_camada=$row['cor_camada'];
				executaSQL("insert into camada_projeto (id_camada,id_projeto,id_linguagem,cor_camada_projeto) values (????)",array($id_camada,$id_projeto,LINGUAGEM_PADRAO,$cor_camada));
			}
		}
	}
?>