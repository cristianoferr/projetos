function search_ajax_way(displayId, queryId, fieldNameID) {
    $("#" + displayId).show();
    var search_this = $("#" + queryId).val();
    $.post("/ajax/searchUsers.php?fieldname=" + fieldNameID, {searchit: search_this}, function(data) {
        $("#" + displayId).html(data);
    })
}

function search_unique(displayId, queryId, fieldNameID, modulo, dbfield, flagNovo) {
    $("#" + displayId).show();
    var search_this = $("#" + queryId).val();
    //alert('/ajax/validaUnico.php?modulo='+modulo+'&dbfield='+dbfield+'&novo='+flagNovo+'&fieldname='+fieldNameID);
    $.post('/ajax/validaUnico.php?modulo=' + modulo + '&dbfield=' + dbfield + '&novo=' + flagNovo + '&fieldname=' + fieldNameID, {searchit: search_this}, function(data) {
        $("#" + displayId).html(data);
    })
}


function atualizaValorRemoto(cellID, novoValor) {
    var textDiv = document.getElementById('value' + cellID);
    var viewDiv = document.getElementById('divinput' + cellID);
    var inputElement = document.getElementById('input' + cellID);
    var tdElement = document.getElementById('td' + cellID);
    var inputValor = document.getElementById('inputValor' + cellID);

    var id_valor = inputValor.value;

    $inputValor = "inputValor".$cellID;//usado para guardar o valor do registro
    var pars = "valorid=" + id_valor + "&novoValor=" + novoValor;
    //alert(pars);

    xmlhttpPost("/ajax/updateEntity.php", pars, function() {
        if (self.xmlHttpReq.readyState == 4) {
            textDiv.style.backgroundColor = "";
        } else {
            textDiv.style.backgroundColor = "red";
        }
        return true;
    });
}

function atualizaGenerico(element, nome_coluna, idRegistro, flagTexto, modulo) {
    return atualizaGenericoComValor(element.value, nome_coluna, idRegistro, flagTexto, modulo);
}

function atualizaGenericoComValor(valor, nome_coluna, idRegistro, flagTexto, modulo) {
    var data = {
        modulo: modulo,
        campo: nome_coluna,
        flagtexto: flagTexto,
        idreg: idRegistro,
        novoValor: valor
    };
    $.ajax({
        url: '/NovoEntityManager/ajax/updateEntity.php',
        data: data,
        type: 'post',
        success: function(data) {
        }
    });
   
    return true;
    //});
}

function atualizaVisualUsuario(identificador, valor) {
    xmlhttpPost("/ajax/atualizaVisual.php", "identificador=" + identificador + "&valor=" + valor, function() {
        return true;
    });
}

function atualizaColuna(element, id_sql_coluna, id_coluna, flagTexto) {
    element.style.backgroundColor = "green";

    xmlhttpPost("/NovoEntityManager/ajax/updateEntity.php", "campo=" + id_sql_coluna + "&flagtexto=" + flagTexto + "&id_coluna=" + id_coluna + "&novoValor=" + element.value, function() {
        if (self.xmlHttpReq.readyState == 4) {
            element.style.backgroundColor = "";
        } else {
            element.style.backgroundColor = "red";
        }
        return true;
    });

}

function xmlhttpPost(strURL, param, funct) {
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
    param = encodeURI(param);
    self.xmlHttpReq.send(param);
}
