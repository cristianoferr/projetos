var inputOpen = false;
var flagSafeToChange = true;

function redirect(url) {
    window.location.href = url;
}

function clickTaleFooter() {
    document.write(unescape("%3Cscript%20src='" +
            (document.location.protocol == 'https:' ?
                    "https://clicktalecdn.sslcs.cdngc.net/www07/ptc/00281527-483c-4f0e-b744-c270c8440311.js" :
                    "http://cdn.clicktale.net/www07/ptc/00281527-483c-4f0e-b744-c270c8440311.js") + "'%20type='text/javascript'%3E%3C/script%3E"));

}

function googleAnalytics() {
    (function(i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function() {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
        a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-41837105-4', 'projetize.com');
    ga('require', 'linkid', 'linkid.js');
    ga('send', 'pageview');
}

function checkCondition(input, condicao, valorCondicao, idInput, atributo) {
    var valorAtual = "";
    if (atributo == 'c_optgroup') {
        valorAtual = $(input.options[input.selectedIndex]).closest('optgroup').prop('label');
    }
    // alert(valorAtual + "|" + condicao + "|" + valorCondicao + "|" + idInput);
    var cond = false;
    if (condicao == "E")
        cond = (valorAtual == valorCondicao);
    if (condicao == "G")
        cond = (valorAtual > valorCondicao);
    if (condicao == "EG")
        cond = (valorAtual >= valorCondicao);
    if (condicao == "L")
        cond = (valorAtual < valorCondicao);
    if (condicao == "EL")
        cond = (valorAtual <= valorCondicao);
    if (condicao == "D")
        cond = (valorAtual != valorCondicao);

    if (cond) {
        $('#' + idInput).show();
    } else {
        $('#' + idInput).hide();
    }
}



function startTextAreaResize() {
    // auto adjust the height of
    if ($('div') == null)
        return;
    $('div').on('keyup', 'textarea', function(e) {
        $(this).css('height', 'auto');
        $(this).height(this.scrollHeight);
    });
    $('div').find('textarea').keyup();
}

function startformatCurrency() {

    if ($('.currency') == null)
        return;
    $('.currency').blur(function()
    {
        $('.currency').formatCurrency();
    });
    $('.currency').formatCurrency();

}

function startToggle() {

    startTextAreaResize();

    $(function() {
        startformatCurrency();
    });



    // JS is only used to add the <div>s
    var switches = document.querySelectorAll('input[type="checkbox"].ios-switch');

    for (var i = 0, sw; sw = switches[i++]; ) {
        var div = document.createElement('div');
        var textOn = sw.getAttribute('texton');
        var textOff = sw.getAttribute('textoff');
        div.className = 'ios-switch-div';
        $(div).attr('textOn', textOn);
        $(div).attr('textOff', textOff);
        sw.parentNode.insertBefore(div, sw.nextSibling);
    }
}


function copyFrom(elementID, element) {
    var elFrom = document.getElementById(elementID);
    if (elFrom != null) {
        if (element.value == '') {
            element.value = elFrom.value;
            element.onchange();
        }
    }

}


function mudaAba(idAba, arrColunas, identificador, flagOn) {
    //alert(flagOn);


    if (flagOn == undefined) {
        $(".aba_" + idAba).toggleClass('button-aba-off');
    } else {

        if (flagOn) {
            if ($(".aba_" + idAba).hasClass('button-aba-off')) {
                return;
            }
            $(".aba_" + idAba).addClass('button-aba-off');
        } else {
            if (!$(".aba_" + idAba).hasClass('button-aba-off')) {
                return;
            }
            $(".aba_" + idAba).removeClass('button-aba-off');
        }
    }

    var v = "true";
    if ($(".aba_" + idAba).hasClass('button-aba-off')) {
        v = "false";
    }

    var configID = identificador + "_" + idAba;
    //alert("identificador:"+configID+" v:"+v);
    atualizaVisualUsuario(configID, v);



    if (arrColunas == null)
        return false;
    for (var i = 0; i < arrColunas.length; i++) {
        var coluna = '.class_' + arrColunas[i];

        if (flagOn == undefined) {
            $(coluna).toggleClass('invisivel');
        } else {
            if (flagOn) {
                $(coluna).removeClass('invisivel');
            } else {
                $(coluna).addClass('invisivel');
            }
        }
        //alert ("coluna:"+coluna);
    }

    return false;
}

function encodeUrl(url)
{
    if (url.indexOf("?") > 0)
    {
        encodedParams = "?";
        parts = url.split("?");
        params = parts[1].split("&");
        for (i = 0; i < params.length; i++)
        {
            if (i > 0)
            {
                encodedParams += "&";
            }
            if (params[i].indexOf("=") > 0) //Avoid null values
            {
                p = params[i].split("=");
                encodedParams += (p[0] + "=" + escape(encodeURI(p[1])));
            }
            else
            {
                encodedParams += params[i];
            }
        }
        url = parts[0] + encodedParams;
    }
    return url;
}



function changePosition(element1, element2) {
    var node1 = document.getElementById(element1);
    var node2 = document.getElementById(element2);
    //alert("el2:" + element2);
    node2_copy = node2.cloneNode(true);
    node1.parentNode.insertBefore(node2_copy, node1);
    node2.parentNode.insertBefore(node1, node2);
    node2.parentNode.replaceChild(node2, node2_copy);

    return false;
}

function mudaOrdem(rowID, arr, variacao, table) {
    var cellAtual = document.getElementById("rownum_" + rowID); //celula contendo a posicao atual
    var posAtual = cellAtual.textContent; //texto igual a posicao atual
    var posDestino = parseInt(posAtual) + variacao;

    var idAtual = document.getElementById("colid_" + rowID).textContent;

    for (var i = 0; i < arr.length; i++) {
        var cell = arr[i];
        var cellDestino = document.getElementById("rownum_" + cell);
        var pos = cellDestino.textContent;
        if (posDestino == pos) {
            cellDestino.textContent = posAtual;
            cellAtual.textContent = posDestino;
            var idDestino = document.getElementById("colid_" + cell).textContent;
            if (idAtual != idDestino) {
                changePosition("tr_" + rowID, "tr_" + cell);

                if (table == 'coluna') {
                    //alert('atualizando coluna');
                    atualizaGenericoComValor(posDestino, 'seq_coluna', idAtual, 'F', table);
                    atualizaGenericoComValor(posAtual, 'seq_coluna', idDestino, 'F', table);
                }
                if (table == 'widget_coluna') {
                    //alert('atualizando coluna');
                    atualizaGenericoComValor(posDestino, 'seq_widget_coluna', idAtual, 'F', table);
                    atualizaGenericoComValor(posAtual, 'seq_widget_coluna', idDestino, 'F', table);
                }
                if (table == 'widget_section') {
                    //alert('atualizando coluna');
                    atualizaGenericoComValor(posDestino, 'seq_widget_section', idAtual, 'F', table);
                    atualizaGenericoComValor(posAtual, 'seq_widget_section', idDestino, 'F', table);
                }
            }

        }

    }
    return false;
}

function confirmExclusao() {
    return (confirm("Tem certeza que deseja excluir?"));
}

function confirmAction() {
    return (confirm("Are you sure?"));
}



function alternaVisibilidade(elID) {
    var element = document.getElementById(elID);
    var display = element.style.display;

    if (display == 'block')
    {
        element.style.display = 'none';
    } else {
        element.style.display = 'block';
    }

}


//Atualiza��o de entidades (ajax)

function verificaDependencia(idBase, valorBase, idDependente) {

    var inputBase = document.getElementById('input_' + idBase);
    var inputDependente = document.getElementById(idDependente);
    if (inputDependente != null) {
        //alert ('inputDependente:'+inputBase.value+"=="+valorBase+":"+(inputBase.value==valorBase));
        if (inputBase != undefined) {
            if (inputBase.value != valorBase) {
                inputDependente.style.display = 'none';
            } else {
                inputDependente.style.display = '';
            }
        }
    }
}

function mostraInput(cellID) {
    if ((!flagSafeToChange) && (inputOpen)) {
        return;
    }
    var td = document.getElementById("td_" + cellID);
    var widthTd = -1;


    flagSafeToChange = true;
    var x = 'txtDiv_' + cellID;
    var textDiv = document.getElementById(x);
    if (textDiv == null) {
        return;
    }
    var viewDiv = document.getElementById('viewDiv_' + cellID);
    var inputElement = document.getElementById('input_' + cellID);

    //var display=textDiv.style.display;
    var width = textDiv.offsetWidth + 150;
    var height = textDiv.offsetHeight;
    if (height <= 30)
    {
        height = 30;
    }
    if ((width < 120) && (inputElement.type == 'select-one')) {
        width = 120;
    }
    if (viewDiv != undefined) {
        viewDiv.style.display = 'none';
    }
    if (textDiv != undefined) {
        textDiv.style.display = 'block';
    }

    if (td != null) {
        widthTd = td.offsetWidth;
        td.style.width = (widthTd) + "px";
    }
    createHidingEvents(inputElement, cellID);
    return true;
}

function createHidingEvents(inputElement, cellID) {
    inputOpen = true;

    if (inputElement != undefined) {
        inputElement.onblur = function() {
            inputOpen = false;
            escondeInput(cellID);
            return false;
        };
        inputElement.onmousedown = function() {
            flagSafeToChange = false;
        }
        inputElement.onkeydown = function() {
            flagSafeToChange = false;
            return inputKeyDown(cellID, inputElement);
        };
        inputElement.focus();
    }
}

function inputKeyDown(cellID, inputElement)
{
    if (window.event.keyCode == 13) {
        inputElement.blur();
        return false;
    }

    //tab
    if (window.event.keyCode == 9) {
        inputElement.blur();
        if (window.event.shiftKey)
        {
            mostraInput(cellID - 1);
        } else {
            mostraInput(cellID + 1);
        }
        return false;
    }
    return true;
}



function pegaConteudoInput(cellIDFrom, cellIDTo) {
    var fromEl = cellIDFrom;//document.getElementById(cellIDFrom);
    var toEl = cellIDTo;//document.getElementById(cellIDTo);


    if (fromEl.type == 'select-one') {
        //se o select tiver vazio daria erro
        if (fromEl.options[fromEl.selectedIndex] == undefined) {
            toEl.textContent = "";
            return;
        }
        toEl.textContent = fromEl.options[fromEl.selectedIndex].text;
    } else if (fromEl.type == 'checkbox') {
        if (fromEl.checked) {
            toEl.textContent = "True";
        } else {
            toEl.textContent = "False";
        }

    } else {
        toEl.textContent = fromEl.value;
    }
}

function escondeInput(cellID) {
    var x = 'txtDiv_' + cellID;
    var textDiv = document.getElementById(x);
    var viewDiv = document.getElementById('viewDiv_' + cellID);
    var inputElement = document.getElementById('input_' + cellID);
    var tdElement = document.getElementById('celula_' + cellID);
    var inputValor = document.getElementById('inputValor_' + cellID);

    if (textDiv.textContent != inputElement.value)
    {
        /*if (inputElement.type=='select-one')
         {
         textDiv.textContent=inputElement.options[inputElement.selectedIndex].text;
         } else {
         textDiv.textContent=inputElement.value;
         }*/
        if (inputValor != null) {
            textDiv.style.backgroundColor = "green";
            atualizaValorRemoto(cellID, inputElement.value);
        }
    }
    if (viewDiv != undefined)
        viewDiv.style.display = 'block';
    if (textDiv != undefined)
        textDiv.style.display = 'none';
    pegaConteudoInput(inputElement, viewDiv)

    tdElement.onclick = function() {
        mostraInput(cellID);
        return false;
    };
    return true;
}




function getquerystring() {
    var form = document.forms['f1'];
    var word = form.word.value;
    qstr = 'w=' + escape(word);  // NOTE: no '?' before querystring
    return qstr;
}

function updatepage(str) {
    document.getElementById("result").innerHTML = str;
}



