var lines = [];
var ctx = null;
var cellHeight = 20;
var zindex = 1;
var dragDropOk;



var _startX = 0;			// mouse starting positions
var _startY = 0;
var _offsetX = 0;			// current element offset
var _offsetY = 0;
var _dragElement;			// needs to be passed from OnMouseDown to OnMouseMove
var _oldZIndex = 0;			// we temporarily increase the z-index during drag
//var _debug = $('debug');	// makes life easier
var idEntidade;
var _scale = 1;


function backingScale(context) {
    if ('devicePixelRatio' in window) {
        if (window.devicePixelRatio > 1) {
            return window.devicePixelRatio;
        }
    }
    return 1;
}

function checkOverlaps() {
    for (i = 0; i < arrElements.length - 1; i++) {
        for (j = i + 1; j < arrElements.length; j++) {
            checkOverlap(arrElements[i], arrElements[j]);
        }
    }
}

function checkOverlap(el1, el2) {
    if (el1 === el2)
        return false;
    var px1 = Math.abs(el1.style.left.replace("px", ""));
    var py1 = Math.abs(el1.style.top.replace("px", ""));

    var w1 = el1.offsetWidth;
    var h1 = el1.offsetHeight;
    var lx1 = px1 + w1;
    var ly1 = py1 + h1;


    var px2 = Math.abs(el2.style.left.replace("px", ""));
    var py2 = Math.abs(el2.style.top.replace("px", ""));

    var w2 = el2.offsetWidth;
    var h2 = el2.offsetHeight;
    var lx2 = px2 + w2;
    var ly2 = py2 + h2;


    var overlap = false;


    if ((px2 < lx1) && (px2 > px1))
        if ((py2 <= ly1) && (py2 >= py1))
            overlap = true;

    if ((px1 < lx2) && (px1 > px2))
        if ((py1 <= ly2) && (py1 >= py2))
            overlap = true;
    if (overlap) {
        /* console.log("overlap!" + el1.id + "--" + el2.id);
         console.log("px1:" + px1 + " w1:" + w1 + " py1:" + py1 + " h1:" + h1);
         console.log("px2:" + px2 + " w2:" + w2 + " py2:" + py2 + " h2:" + h2);*/
        if (px1 < px2) {
            el2.style.left = lx1 + "px";
        } else {
            el1.style.left = lx2 + "px";
        }


    }
}


function calculatePosition(objElement, strOffset)
{
    var iOffset = 0;
    if (objElement.offsetParent)
    {
        do
        {
            iOffset += objElement[strOffset];
            objElement = objElement.offsetParent;
        } while (objElement);
    }
    return iOffset;
}

function linha(xFrom, yFrom, xTo, yTo, color) {
    ctx.beginPath();
    ctx.lineWidth = 1;
    ctx.strokeStyle = color;

    ctx.moveTo(xFrom, yFrom);
    ctx.lineTo(xTo, yTo);
    ctx.stroke();
}

function   preparaLinha(start, end, i) {
    var espaco = 10;
    var extraHeight = 8;
    // var multiConta = 0;
    //  alert("desenhalinha");


    var xIni = (calculatePosition(start, 'offsetLeft') - _minX) * _scale;
    var yIni = (calculatePosition(start, 'offsetTop') - _minY) * _scale;
    var widthIni = start.offsetWidth;
    yIni += extraHeight;

    var xFim = (calculatePosition(end, 'offsetLeft') - _minX) * _scale;
    var yFim = (calculatePosition(end, 'offsetTop') - _minY) * _scale;
    var widthFim = end.offsetWidth;

    yFim += extraHeight;

    var destinoOffset = lines[i].destinoOffset;
    yFim += +destinoOffset;

    var xQuebra = xIni;
    var xPosIni = xIni;
    var xPosFim = xFim;

    var xPosPata = espaco / 2;

    //a direita
    if (xFim > xIni + widthIni) {
        xQuebra += espaco + widthIni;
        xPosIni += widthIni;
    } else {
        xPosPata = -xPosPata;
        xQuebra -= espaco;
        xPosFim += widthFim;
    }

    ctx.beginPath();
    ctx.lineWidth = 1;
    ctx.strokeStyle = lines[i].color;

    ctx.moveTo(xPosIni, yIni);
    ctx.lineTo(xQuebra, yIni);
    ctx.lineTo(xQuebra, yFim);
    ctx.lineTo(xFim, yFim);
    ctx.stroke();


    //pata de galinha
    if (lines[i].relationType == "one2many") {
        ctx.lineWidth = 1;
        ctx.moveTo(xPosIni, yIni - xPosPata);
        ctx.lineTo(xPosIni + xPosPata * 2, yIni);
        ctx.lineTo(xPosIni, yIni + xPosPata);
        ctx.stroke();
    }
    if (lines[i].relationType == "redirect") {
        ctx.moveTo(xPosFim - xPosPata * 2, yFim - xPosPata);
        ctx.lineTo(xPosFim - xPosPata * 2, yFim + xPosPata);
        ctx.lineTo(xPosFim, yFim);
        ctx.lineTo(xPosFim - xPosPata * 2, yFim - xPosPata);
        ctx.stroke();
    }

    if (lines[i].relationType == "extends") {
        ctx.moveTo(xPosFim - xPosPata * 2, yFim - xPosPata);
        ctx.lineTo(xPosFim - xPosPata * 2, yFim + xPosPata);
        ctx.lineTo(xPosFim, yFim);
        ctx.lineTo(xPosFim - xPosPata * 2, yFim - xPosPata);
        ctx.stroke();

        ctx.fillStyle = lines[i].color;
        ctx.font = "14px Arial";
        ctx.fillText("<< extends >>", (xPosIni + xPosFim) / 2, yFim);
    }
}

function draw_loop()
{
    if (!dragDropOk)
        return;

    setTimeout(draw_loop, 1 / 1000);
    if (ctx == null) {
        var el = document.getElementById("background");
        if (el != undefined)
            ctx = el.getContext("2d");
    }
    if (ctx == null) {
        return;
    }
    ctx.fillStyle = "#ffffff";
    // alert("loop");

    //Clear the background
    ctx.fillRect(0, 0, _maxX, _maxY);

    for (var i in lines)
    {
        var start = lines[i].start;
        var end = lines[i].end;

        preparaLinha(start, end, i);


    }

    // console.log(lines);
}

function add_line(inicio, fim, color, conta, tipoRel) {
    inicio = document.getElementById(inicio);
    fim = document.getElementById(fim);
    if ((inicio == null) || (fim == null))
    {
        return;
    }
    lines.push({
        start: inicio,
        end: fim,
        'color': color ? color : "#" + ("000" + (Math.random() * (1 << 24) | 0).toString(16)).substr(-6),
        count: conta,
        destinoOffset: -cellHeight / 2 + Math.random() * cellHeight * flagRandomizeLine,
        relationType: tipoRel

    });
}



function InitDragDrop()
{
    dragDropOk = (document.getElementById("background") != undefined);
//{
//   var ctx = document.getElementById("background").getContext("2d");
//Run the draw_loop for the first time. It will call itself 25 times per second after that    
    draw_loop();

    if (!dragDropOk)
        return;
    document.onmousedown = OnMouseDown;
    document.onmouseup = OnMouseUp;
    document.touchend = OnMouseUp;
    _scale = backingScale(ctx);
}

function OnMouseDown(e)
{

    if (!dragDropOk)
        return true;


    // alert("oi");
    // IE is retarded and doesn't pass the event object
    if (e == null)
        e = window.event;

    // IE uses srcElement, others use target
    var target = e.target != null ? e.target : e.srcElement;

    idEntidade = target.id.replace("cell", "");
    var idTable = target.id.replace("cell", "entity");
    target = document.getElementById(idTable);


    if (!target)
    {
        return true;
    }
    if (target.getAttribute("dragel") == null) {
        return true;
    }

    if (target.id == "background")
    {
        return true;
    }

    zindex++;

//	target=target.value;

    /*_debug.innerHTML = target.className == 'drag' 
     ? 'draggable element clicked' 
     : 'NON-draggable element clicked';*/

    // for IE, left click == 1
    // for Firefox, left click == 0
    //		alert(target.value);
    if ((e.button == 1 && window.event != null ||
            e.button == 0))
            //&& target.className == 'drag'
            {
                // grab the mouse position
                _startX = e.clientX;
                _startY = e.clientY;

                // grab the clicked element's position
                _offsetX = ExtractNumber(target.style.left);
                _offsetY = ExtractNumber(target.style.top);

                // bring the clicked element to the front while it is being dragged
                _oldZIndex = zindex;
                target.style.zIndex = 10000;

                // we need to access the element in OnMouseMove
                _dragElement = target;

                // tell our code to start moving the element with the mouse
                document.onmousemove = OnMouseMove;

                // cancel out any text selections
                document.body.focus();

                // prevent text selection in IE
                document.onselectstart = function() {
                    return  false;
                };
                // prevent IE from trying to drag an image
                target.ondragstart = function() {
                    return  false;
                };

                // prevent text selection (except IE)
                return  false;
            }
}

function ExtractNumber(value)
{
    var n = parseInt(value);

    return n == null || isNaN(n) ? 0 : n;
}

function OnMouseMove(e)
{



    if (e == null)
        var e = window.event;
    var px = (_offsetX + e.clientX - _startX);
    var py = (_offsetY + e.clientY - _startY);

    if (px > _maxX)
        px = _maxX;
    if (py > _maxY)
        py = _maxY;
    if (px < _minX)
        px = _minX;
    if (py < _minY)
        py = _minY;
    //alert(e_startXclientX);
    //log("startX:" + _startX + " _offsetX:" + _offsetX);

    // this is the actual "drag code"
    _dragElement.style.left = px + 'px';
    _dragElement.style.top = py + 'px';

//	_debug.innerHTML = '(' + _dragElement.style.left + ', ' + _dragElement.style.top + ')';	
    return true;
}


function OnMouseUp(e)
{
    if (!dragDropOk)
        return true;
    //alert(_dragElement + " " + e);
    var elEvent = e.srcElement;
    var elDrag = document.getElementById(elEvent.getAttribute("dragel"));
    if (elDrag == null) {
        var elDrag = document.getElementById(elEvent.parentNode.getAttribute("dragel"));
    }

    if (elDrag == null) {
        // alert("drag nulo");
        return true;
    }

    //if (_dragElement !== null)
    {
        elDrag.style.zIndex = _oldZIndex;

        // we're done with these events until the next OnMouseDown
        document.onmousemove = null;
        document.onselectstart = null;
        //log("idProjeto: "+idProjeto+" idEntidade:"+idEntidade+" left:"+_dragElement.style.left+" top:"+_dragElement.style.top);
        atualizaPosicao(elDrag, elDrag.style.left, elDrag.style.top);
        elDrag.ondragstart = null;


        // this is how we know we're not dragging
        elDrag = null;

        //	_debug.innerHTML = 'mouse up';
    }
}



function atualizaPosicao(element, posX, posY) {
    posX = posX.replace("px", "");
    posY = posY.replace("px", "");
    //element.style.backgroundColor = "green";
    var params = "modulo=posicao_entidade&posX=" + posX + "&posY=" + posY + "&flagtexto=F&iddiagrama=" + idDiagrama + "&idComponente=" + element.getAttribute("idcomp") + "&idprojeto=" + idProjeto;
    //alert(params);
    xmlhttpPost("/NovoEntityManager/ajax/updateEntity.php", params, function() {
        if (self.xmlHttpReq.readyState == 4) {
            //element.style.backgroundColor="";
        } else {
            //element.style.backgroundColor="red";
        }
        return true;
    });
}

