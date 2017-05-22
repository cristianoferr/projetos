function alternaSideMenuProjeto(id_projeto) {
    //alert ("id_projeto:"+id_projeto);
    var elemento = "#conteudo_projeto" + id_projeto;
    var elementoLink = "#root_projeto" + id_projeto;
    //var elMenu=document.getElementById(elemento);
    //alert(elemento);
    if ($(elemento) == null)
        return;
    $(elemento).toggle();
    if ($(elemento).is(":visible")) {
        $(elementoLink).addClass('menu_expandido');
        $(elementoLink).removeClass('menu_contraido');
        atualizaVisualUsuario("menu_projeto" + id_projeto, 'true');

    } else {
        $(elementoLink).addClass('menu_contraido');
        $(elementoLink).removeClass('menu_expandido');
        atualizaVisualUsuario("menu_projeto" + id_projeto, 'false');
    }

}

function alternaSideMenu() {
    //alert($("#raiz_entidades"));
    $(".conteudoMenu").toggle();
    if ($(".conteudoMenu").is(":visible")) {
        $("#raiz_entidades").addClass('menu_expandido');
        $("#raiz_entidades").removeClass('menu_contraido');
        atualizaVisualUsuario("menu_projects", 'true');
    } else {
        $("#raiz_entidades").addClass('menu_contraido');
        $("#raiz_entidades").removeClass('menu_expandido');
        atualizaVisualUsuario("menu_projects", 'false');
    }

}


function defineOrderby(campo, formulario, isAsc) {
    atualizaVisualUsuario(formulario, campo);
    atualizaVisualUsuario(formulario + "isAsc", isAsc);
    setTimeout("location.reload(true);", 200);
}

// Copyright 2006-2007 javascript-array.com

var timeout = 500;
var closetimer = 0;
var ddmenuitem = 0;


function abreMenuConjunto(classe) {
    mcancelclosetime();
    $(classe).style.removeClass("invisivel");

}


// open hidden layer
function mopen(id)
{
    // cancel close timer
    mcancelclosetime();
//	log('mopen:'+id);

    // close old layer
    var novoItem = document.getElementById(id);
    if (novoItem == ddmenuitem)
    {
        return;
    }

    if (ddmenuitem)
        ddmenuitem.style.visibility = 'hidden';


    // get new layer and show it
    ddmenuitem = document.getElementById(id);
    ddmenuitem.style.visibility = 'visible';

}
// close showed layer
function mclose()
{
    //log('mclose()');
    if (ddmenuitem)
        ddmenuitem.style.visibility = 'hidden';
}

// go close timer
function mclosetime()
{
//	log('mclosetime()');
    closetimer = window.setTimeout(mclose, timeout);
}

// cancel close timer
function mcancelclosetime()
{
//	log('mcancelclosetime');
    if (closetimer)
    {
        //	log('tem closetimer');
        window.clearTimeout(closetimer);
        closetimer = null;
    }

}



// close layer when click-out
//document.onclick = mclose; 