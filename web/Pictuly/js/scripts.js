function startToggle(){

	// JS is only used to add the <div>s
	var switches = document.querySelectorAll('input[type="checkbox"].ios-switch');

	for (var i=0, sw; sw = switches[i++]; ) {
		var div = document.createElement('div');
		div.className = 'switch';
		sw.parentNode.insertBefore(div, sw.nextSibling);
	}
	
	$("#linkNSFW").hide();
}

function confirmExclusao() {  
   return (confirm("Tem certeza que deseja excluir?")) ;
}  

function redirectTo(pagina){
	self.location=pagina;
}

function xmlhttpPost(strURL,param,funct) {
    var xmlHttpReq = false;
    var self = this;
    // Mozilla/Safari
    if (window.XMLHttpRequest) {
        self.xmlHttpReq = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
        self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
    }


    self.xmlHttpReq.open('POST', strURL, true);
    self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    self.xmlHttpReq.onreadystatechange = funct;
    self.xmlHttpReq.send(param);
}

function alteraDescricaoToggle(){
	var label = document.getElementById('labelToggle');
	label.setAttribute("data-hint",descricaoToggle());
}

function clickToggle(){
	alteraDescricaoToggle();
	var toggle = document.getElementById('nsfwToggle');
	var t="T";
	if (toggle.checked){
		t="F"
	}
	if (t=="T"){
		redirectTo("http://nsfw.pictuly.com");
	} else {
		redirectTo("http://pictuly.com");
	}
	
	return true;
}

function descricaoToggle(){
	var toggle = document.getElementById('nsfwToggle');
	if (!toggle.checked){
		return "Not Safe For Work (click to change)";
	} else{
		return "Safe for work (click to change)";
	}
}

