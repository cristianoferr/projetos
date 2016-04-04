<?php
	include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
	inicializa();

	$term = strip_tags(substr($_POST['searchit'],0, 100));
	$fieldname=strip_tags(substr($_GET['fieldname'],0, 100));
	$term = mysql_escape_string($term); 
	if(!$term==""){
		$controller=getControllerManager()->getControllerForTable("usuario");
		$saida=$controller->buscaUsuarios($term);
		//echo $term;
		if ($saida){
			$arr=$saida->getArray();
			
			$count=0;
			
			$nomes="";
			$ids="";
			foreach ($arr as $chave => $row) {
				$id_usuario=$row['id_usuario'];
				$nm_usuario=$row['nm_usuario'];
				$email_usuario=$row['email_usuario'];
				$email_usuario=strstr($email_usuario, '@', true)."@..."; 
				$nome="$nm_usuario - $email_usuario";

				//echo "<a href='#' class='tagAdd' id=\"add_$id_usuario\" onclick='return adicionaUsuario($count,this);'>$nome</a><br>".PHP_EOL;
				if ($count>0){
					$nomes.=",";
					$ids.=",";
				}
				$nomes.="\"$nome\"";
				$ids.="\"$id_usuario\"";
				$count=$count+1;
				
			}

			?><script>	


			var ids=[<?echo $ids;?>];
			var nomes=[<?echo $nomes;?>];

			listaUsuarios();

			function isIdSelecionada(idUser){
				for (j=0;j<idsSelecionados.length;j++){
			  		if (idsSelecionados[j]==idUser) return true;
			  	}
			  return false;
			}

			function listaUsuarios(){
				var data='';
				for (i=0;i<ids.length;i++){
					var idUser=ids[i];
					
				  	if (!isIdSelecionada(idUser)){
						var usuario=nomes[i];
						
						data=data+'<a href="#" class="tagAdd" onclick="return adicionaUsuario('+i+',this);">'+usuario+'</a><br> ';
				  		
				  	}
				}
				var divResult = document.getElementById('display_users');
				$(divResult).html(data);
			}
			
			 function adicionaUsuario(index,element){
			 	  idUser=ids[index];
			 	  usuario=nomes[index];
				  $("#add_"+idUser).hide();
				  
				  if (!isIdSelecionada(idUser)) {
				  	idsSelecionados.push(idUser);
				  	nomesSelecionados.push(usuario);
				  }
				  
				 atualizaNomes();
				  
				 return false;
			}

			function atualizaNomes(){
				var data='';

				for (i=0;i<idsSelecionados.length;i++){
					var usuario=nomesSelecionados[i];
					var idUser=idsSelecionados[i];
					data=data+'<a href="#" class="tagRemove" onclick="return removeUsuario('+idUser+',this);">'+usuario+'</a><br> ';
				}
				var hidden = document.getElementById('input_<?echo $fieldname;?>');
				hidden.value=idsSelecionados;
				$('#selected_<?echo $fieldname;?>').show();
				 $('#selected_<?echo $fieldname;?>').html(data);
				 listaUsuarios();
			}

			function removeUsuario(idUser,element){
				  for (i=0;i<idsSelecionados.length;i++){
				  	if (idsSelecionados[i]==idUser){
				  		//alert ("i:"+i+" idsSelecionados[i]:"+idsSelecionados[i]+" idUser:"+idUser);
				  		idsSelecionados.splice(i, 1);
				  		nomesSelecionados.splice(i, 1);
				  	}
				  }
				//$(element).remove();
				atualizaNomes();
			}
			 </script>
			 <?
		}

		//echo $saida;
	}
?>