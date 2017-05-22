<?
	class GeraSQLStatic{
		function getSQLColunas($pkID,$colunas){
			$sql="#";

			for ($c=0; $c<sizeOf($colunas);$c++){	
				$coluna=$colunas[$c];
				$sql.=", ".$coluna->getSelectSQL($pkID);
			}
			$sql=str_replace("#,","",$sql);
			if ($sql=="#"){die("Collumns undefined(#).");}
			return $sql;
		}

		function getFromSQL($colunas){
			$sql="";
			for ($c=0; $c<sizeOf($colunas);$c++){	
				$coluna=$colunas[$c];
				$sql.=$coluna->getFromSQL($sql);
			}
			return $sql;
		}

		function getWhereSQL($colunas){
			$sql="";
			for ($c=0; $c<sizeOf($colunas);$c++){	
				$coluna=$colunas[$c];
				$sql.=$coluna->getWhereSQL();
			}
			return $sql;
		}

}


?>