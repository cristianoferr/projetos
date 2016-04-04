<?php
	include 'include/include.php';
if (isset($_POST['email'])){
	$email=$_POST['email'];
	$password=$_POST['password'];
	$controller=getUserController();
	$controller->validaUsuario($email,$password);
}

	
	escreveHeader(false);


?>
<section class="container">
	<form class="form1" action="login.php" method=post>

				<div class="formtitle titulo"><?echo translateKey("login_account");?></div>

				<?if ($_GET['erro']=='invalidPasswd'){?>
				<div class="importante subTitulo"><?echo translateKey("login_invalid_password");?></div>
				<?}?>
				<div class="input">
					<div class="inputtext"><?echo translateKey("login_email");?>: </div>
					<div class="inputcontent">
						<input type="email" name="email" required value="<?echo $_GET['email'];?>" />
					</div>
				</div>

				<div class="input nobottomborder">
					<div class="inputtext"><?echo translateKey("login_password");?>: </div>
					<div class="inputcontent">

						<input type="password" name="password" required/>
						<br/><a href="forgotPassword.php"><?echo translateKey("login_forgot_password");?></a>

					</div>
				</div>
				<div class="buttons">

					<input class="orangebutton" type="submit" value="<?echo translateKey("login_submit_login");?>" />
					<input class="greybutton" type="submit" value="<?echo translateKey("login_cancel");?>" />

				</div>

			</form>

</section>

<?
	escreveFooter();
?>