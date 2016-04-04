<?

function escreveCabecalho($flagRefresh=true){
	if (isAdmin()){
		$flagRefresh=false;
	}?>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="description" content="<?echo translateKey("meta_description");?>">
		<meta name="keywords" content="<?
			if (isNSFW()) {
				echo translateKey("meta_tags_nsfw");
			} else {
				echo translateKey("meta_tags_sfw");
			}
			?>" />
		<meta property="og:url" content="<?echo getHomeLink()?>/>
		<meta http-equiv="content-language" content="en-us">
		<?if ($flagRefresh){?>
			<meta http-equiv="refresh" content="120">
		<?}?>
		<link rel="stylesheet" href="css/css_unico.php" type="text/css">
		<script src="js/jquery.js" type="text/javascript"></script>
		<script src="js/scripts.js" type="text/javascript"></script>
		<script src="js/rating.js" type="text/javascript"></script>
		
		
		<title><?echo translateKey("site_title");?> - <?echo translateKey("site_sub_title");
		if (isNSFW()) {echo " (NSFW)";}?></title>
	</head>
	<body>
	<?
}

function escreveHeader($flagRefresh=true){
	escreveCabecalho($flagRefresh);
	?>


		<header class="container">
			<div class="cabecalho">
				<h1><a href="<?echo getHomeLink()?>"><?echo translateKey("site_title");?></a></h1>
				<h2><?echo translateKey("site_subtitle");?></h2>
				
				<nav class="menu topo">
					<ul>
						<li><a href="#">
							<?if (isAdmin()){
								?><li><a rel="nofollow" href="admin.php">Administração</a></li><?
							}
							if (!usuarioAtual()){
								?><li><a rel="nofollow" href="register.php"><?echo translateKey("menu_create_account");?></a></li>
								<li><a rel="nofollow" href="login.php"><?echo translateKey("menu_login");?></a></li><?
							} else {
								?><li><a rel="nofollow" href="account.php"><?echo $_SESSION["email_usuario"];?></a></li>
								<li><a rel="nofollow" href="logout.php"><?echo translateKey("menu_logout");?></a></li><?
							}
							?>
					</ul>
				</nav>
				<nav class="menu menuprincipal">
					<ul>
						<li><a href="#" class="hint--bottom" data-hint="<?echo translateKey("desc_popularStuff");?>"><?echo translateKey("txt_popularStuff");?></a></li>
						<li><a href="#"><?echo translateKey("txt_newStuff");?></a></li>
					</ul>
				</nav>
				
				<section class="busca">
					<div id="dark">
						<form method="get" action="search.php" id="search" required>
							<input name="q" type="text" size="40" placeholder="<?echo translateKey("txt_search");?>" />
						</form>
					</div>
				</section>
			</div>
		</header>
		


	<?
}


	function escreveFooter(){
		?>
		
		<footer class="">
			<div class="cabecalho container" style="height:20px">
				<nav class="menu">
				<ul>
					<li><a href="#">About Us</a></li>
					<li><a href="#">Contact</a></li>
					<li><a href="#"></a></li>
				</ul>
				</nav>

				<label class="labelToggle hint--left" id=labelToggle data-hint="...">
					<input onclick="clickToggle();" id="nsfwToggle" type="checkbox" class="ios-switch" <?if (!isNSFW()) echo "checked"?> />
				</label>
				<?if (isNSFW()){
					?><a id="linkNSFW" href="http://pictuly.com">Show Safe for Work images</a><?
				} else {
					?><a id="linkNSFW" href="http://nsfw.pictuly.com">Show NSFW (not safe for work) images</a><?
				}?>
				
			</div>
		</footer>
		<?if (isAdmin()){
			echo "Queries: ".$GLOBALS["contaQueries"];
			?><p class="togleEdicao">
				<?
				$flagEdicao=verificaFlagEdicao();
				if ($flagEdicao==true){
					$texto="Desabilita Debug";
					$param="false";
				} else {
					$texto="Habilita Debug";
					$param="true";
				}

				$link=getPaginaAtual();
				$link=str_replace("&togleEdicao=true", "", $link);
				$link=str_replace("&togleEdicao=false", "", $link);

				?>
				<a href=<? echo $link."&togleEdicao=$param"?>><?echo $texto?></a><br>
			</p><?
		}?>

		<script>

				startToggle();
				alteraDescricaoToggle();
		</script>

		<script>
		  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

		  <?if (isNSFW()){?>
			  ga('create', 'UA-41837105-2', 'pictuly.com');
		  <?}else{?>
		  	ga('create', 'UA-41837105-1', 'pictuly.com');
		  <?}?>
		  ga('send', 'pageview');
		</script>
	</body>
</html>
<?
	}


?>