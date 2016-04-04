	<?
function escreveSelectBooleanoGenerico($form_id,$selected_PK,$vlr_pk_tabela_update,$tabela_update,$nm_campo_update){
	?>
		<select id=<?echo $form_id?> name=<?echo $form_id?> onchange="<?echo "atualizaGenerico(this,'$nm_campo_update',".$vlr_pk_tabela_update.",'T','$tabela_update');"?>">
			<option value=null></option>
			<option value='T' <? if ($selected_PK=='T'){echo "selected";}?> >True</option>
			<option value='F' <? if ($selected_PK=='F'){echo "selected";}?> >False</option>
		</select>
	<?}


	function escreveSelectGenerico($tabela,$pk_tabela,$desc_tabela,$selected_PK,$orderby,$vlr_pk_tabela_update,$tabela_update,$nm_campo_update){
	?>
		<select id=<?echo $pk_tabela?> name=<?echo $pk_tabela?> onchange="<?echo "atualizaGenerico(this,'$nm_campo_update',".$vlr_pk_tabela_update.",'F','$tabela_update');"?>">
		<option value=null></option>
			<?
			$rsSelect=executaQuery("select $pk_tabela,$desc_tabela$extrafield from $tabela $where order by $orderby");
			$projAtual="a";
			while ($rowSelect = mysql_fetch_array($rsSelect)){
				?><option value=<?echo $rowSelect[$pk_tabela];?> <? if ($selected_PK==$rowSelect[$pk_tabela]){echo "selected";}?> ><?echo $rowSelect[$desc_tabela];?></option>
			<?}?>
			</select>
	<?}

	function escreveSelectGenericoSimples($tabela_select,$tabela_update,$selected_value,$orderby,$vlr_pk_tabela_update){
		escreveSelectGenerico($tabela_select,"id_$tabela_select","nm_$tabela_select",$selected_value,$orderby,$vlr_pk_tabela_update,$tabela_update,"id_$tabela_select");
	}

	function escreveCelulaGenericaAjax($nm_campo,$valor_pk,$value,$tabela,$charFlagTexto){

		$cellID="$nm_campo$valor_pk";
		$inputID="input".$cellID;

		$valueID="value".$cellID;
		$divInputID="divinput".$cellID;
		$inputValor="inputValor".$cellID;//usado para guardar o valor do registro
		$tdID="td".$cellID;

		?><td id='<?echo $tdID;?>' onclick="mostraInput('<?echo "$cellID";?>');">
			<div style="display:inline;"  id='<?echo $valueID;?>'><?echo $value;?></div>
			<div style="display:none;" id='<?echo $divInputID;?>'>
				<textarea name=<?echo $inputID;?> id=<?echo $inputID;?> onchange="<?echo "atualizaGenerico(this,'$nm_campo',".$valor_pk.",'$charFlagTexto','$tabela');"?>"><?echo $value;?></textarea>
			</div>
		</td>
	<?}
	

	?>