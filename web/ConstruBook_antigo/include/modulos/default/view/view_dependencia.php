<?
//classe onde armazeno quem depende de quem... por exemplo, só vou mostrar table_name se o campo flag_banco for "T"
class DependenciaDado{
	private $colunaBase;
	private $valorBase;
	private $colunaDependente;
	private $flagCampoForm;

	function DependenciaDado($colunaBase,$valorBase,$colunaDependente,$flagCampoForm){
		$this->colunaBase=$colunaBase;
		$this->valorBase=$valorBase;
		$this->colunaDependente=$colunaDependente;
		$this->flagCampoForm=$flagCampoForm;
	}

	function getColunaBase(){
		return $this->colunaBase;
	}

	function isCampoForm(){
		return $this->flagCampoForm;
	}

	function getValorBase(){
		return $this->valorBase;
	}

	function getColunaDependente(){
		return $this->colunaDependente;
	}
}
?>