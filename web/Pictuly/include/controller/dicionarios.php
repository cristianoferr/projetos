<?
	class Dicionario{
		//var $arrStrings;
		var $name;

		function Dicionario($name){
			$this->name=$name;
			//$this->arrStrings=array();
		}
/*
		function setValor($key,$valor){
			//$this->arrStrings[$key]=$valor;
		}
		function getValor($key){

			//return $this->arrStrings[$key];
		}*/

		function getValor($key){
			return $_SESSION[$this->name."_$key"];
		}

		function setValor($key,$valor){
			$_SESSION[$this->name."_$key"]=$valor;
		}

	}

	class InternationalDict extends Dicionario {
		var $id_lang;
	

		function InternationalDict(){
			$this->Dicionario("INTER");
		}

		function language(){
			return $this->id_lang;
		}

		function setDefaultLang($id_lang){
			$this->id_lang=$id_lang;
		}

		

		function translateKey($int_txt){
			$int_txt=validaTexto($int_txt);
			if (!$this->getValor($int_txt)){
				$row=executaQuerySingleRow("select s.* from texto_lang tl,string_internacional s where s.id_texto_lang=tl.id_texto_lang and chave_texto_lang='$int_txt'  and s.id_lingua=".$this->language());
				if ($row){
					$txt=$row['valor_texto_lang'];
					$this->setValor($int_txt,$txt);
					return $txt;
				} else {
					echo "$int_txt não encontrado na base.";
					//die();
				}
			}
			return $this->getValor($int_txt);
		}
	}

?>