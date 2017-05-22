		function preparaSubmit(){
			var from=document.getElementById('select_use');
			for (var i = 0; i < from.options.length; i++) { 
	        	from.options[i].selected = true; 
	    	} 
	    	return true;
		}
		function adicionaTodosElementos(select_from,select_to){
			var from=document.getElementById(select_from);
			for (var i = 0; i < from.options.length; i++) { 
	        	from.options[i].selected = true; 
	    	} 
	    	adicionaElementos(select_from,select_to);
	    	for (var i = 0; i < from.options.length; i++) { 
	        	from.options[i].selected = false; 
	    	} 
		}
		function adicionaElementos(select_from,select_to){
			var from=document.getElementById(select_from);
			var to=document.getElementById(select_to);
			//alert (from);
			var arr=getSelectValues(from);
			for (var i=0, iLen=arr.length; i<iLen; i++) {
				var o = arr[i];
				//alert ("o:"+o);

				if (o.getAttribute("selectable")=="T") {


				    var opt = document.createElement('option');
	    			opt.value = o.value;
	    			opt.text=o.text;
	    			opt.setAttribute("id",o.getAttribute("id"));
	    			opt.setAttribute("tipo",o.getAttribute("tipo"));
	    			opt.setAttribute("selectable",o.getAttribute("selectable"));
	    			to.appendChild(opt);
	    			from.removeChild(o);
	    		}
			}


		}
		function getSelectValues(select) {
			var result = [];
			var options = select && select.options;
			var opt;


			for (var i=0, iLen=options.length; i<iLen; i++) {
				opt = options[i];


				if (opt.selected) {
					result.push(opt);//.value +" "+  opt.text+" "+  opt.getAttribute("id"));
				}
			}
			return result;
		}