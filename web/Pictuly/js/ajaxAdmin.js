//Atualização de entidades (ajax)

function mostraInput(cellID){
	
	var x='value'+cellID;
	var txtElement=document.getElementById(x);
	//alert ('x:'+x+' '+txtElement);
	var inputDivElement=document.getElementById('divinput'+cellID);
	var inputElement=document.getElementById('input'+cellID);
	var tdElement=document.getElementById('td'+cellID);
	
	//var display=txtElement.style.display;
	var width=txtElement.offsetWidth+50;
	var height=txtElement.offsetHeight;
	if (height<=30)
	{
		height=30;
	}

	if ((width<120) &&(inputElement.type=='select-one')){
		width=120;
	}


	txtElement.style.display = 'none';
	inputDivElement.style.display='block';
	tdElement.onclick=function(){return false;};

	inputElement.onblur=function(){escondeInput(cellID);return false;};
	inputElement.onkeydown=function(){return inputKeyDown(cellID,inputElement);};
	inputElement.focus();
	inputElement.style.width=width+'px';
	inputElement.style.height=height+'px';
	/*} else {
		txtElement.style.display = 'block';
		inputDivElement.style.display='none';
	}*/
	
	return true;
}

function inputKeyDown(cellID,inputElement)
{
    if (window.event.keyCode == 13){
		inputElement.blur();
        return false;
	}

	//tab
	if (window.event.keyCode == 9){
		inputElement.blur();
		if (window.event.shiftKey)
		{
			mostraInput(cellID-1);
		} else {
			mostraInput(cellID+1);
		}
        return false;
	}
	return true;
}

function escondeInput(cellID){
	var txtElement=document.getElementById('value'+cellID);
	var inputDivElement=document.getElementById('divinput'+cellID);
	var inputElement=document.getElementById('input'+cellID);
	var inputValor=document.getElementById('inputValor'+cellID);
	var tdElement=document.getElementById('td'+cellID);
	
	if (txtElement.textContent!=inputElement.value)
	{
		if (inputElement.type=='select-one')
		{
			txtElement.textContent=inputElement.options[inputElement.selectedIndex].text;
		} else {
			txtElement.textContent=inputElement.value;
		}
		if (inputValor!=null){
			txtElement.style.backgroundColor="green";
			atualizaValorRemoto(cellID,inputElement.value);
		}
	}

	txtElement.style.display = 'block';
	inputDivElement.style.display='none';
	tdElement.onclick=function(){mostraInput(cellID);return false;};
	return true;
}



function atualizaValorRemoto(cellID,novoValor){
	var txtElement=document.getElementById('value'+cellID);
	var inputDivElement=document.getElementById('divinput'+cellID);
	var inputElement=document.getElementById('input'+cellID);
	var tdElement=document.getElementById('td'+cellID);
	var inputValor=document.getElementById('inputValor'+cellID);

	var id_valor=inputValor.value;

$inputValor="inputValor".$cellID;//usado para guardar o valor do registro
	var pars="valorid="+id_valor+"&novoValor="+novoValor;
	//alert(pars);

	xmlhttpPost("ajax/updateAdmin.php",pars,function(){
		if (self.xmlHttpReq.readyState == 4) {
			txtElement.style.backgroundColor="";
		} else {
			txtElement.style.backgroundColor="red";
		}
		return true;});
}


function atualizaGenerico(element,nome_coluna,idRegistro,flagTexto,modulo){
	element.style.backgroundColor="green";
	var params="modulo="+modulo+"&campo="+nome_coluna+"&flagtexto="+flagTexto+"&idreg="+idRegistro+"&novoValor="+element.value;
	//alert(params);
	xmlhttpPost("ajax/updateAdmin.php",params,function(){
		if (self.xmlHttpReq.readyState == 4) {
			element.style.backgroundColor="";
		} else {
			element.style.backgroundColor="red";
		}
		return true;});

}

function atualizaColuna(element,id_sql_coluna,id_coluna,flagTexto){
	element.style.backgroundColor="green";
	
	xmlhttpPost("ajax/updateAdmin.php","campo="+id_sql_coluna+"&flagtexto="+flagTexto+"&id_coluna="+id_coluna+"&novoValor="+element.value,function(){
		if (self.xmlHttpReq.readyState == 4) {
			element.style.backgroundColor="";
		} else {
			element.style.backgroundColor="red";
		}
		return true;});

}
