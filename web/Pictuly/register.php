<?php
	include 'include/include.php';
if (isset($_POST['email'])){
	$email=$_POST['email'];
	$password=$_POST['password'];
	$name=$_POST['name'];
	$repeat_password=$_POST['repeat_password'];

	if ($repeat_password==$password){
		$controller=getUserController();
		$erro=$controller->registraUsuario($name,$email,$password);
		if (!$erro){
			redirect("account.php");
		}
	} else {
		$erro='passwordMismatch';
	}
}

	
	escreveHeader(false);


?>
<section class="container">
	<form class="form1" action="register.php" method=post>

				<div class="formtitle titulo"><?echo translateKey("register_account");?></div>

				<?if ($erro=='emailExists'){?>
				<div class="importante subTitulo"><?echo translateKey("register_email_exists");?></div>
				<?}?>
				<?if ($erro=='passwordMismatch'){?>
				<div class="importante subTitulo"><?echo translateKey("register_password_mismatch");?></div>
				<?}?>

				<div class="input nobottomborder">
					<div class="inputtext"><?echo translateKey("register_name");?>: </div>
					<div class="inputcontent">

						<input type="text" name="name" required value="<?echo $name;?>"/>
					</div>
				</div>


				<div class="input">
					<div class="inputtext"><?echo translateKey("login_email");?>: </div>
					<div class="inputcontent">
						<input type="email" name="email" required value="<?echo $email;?>" />
					</div>
				</div>

				<div class="input nobottomborder">
					<div class="inputtext"><?echo translateKey("login_password");?>: </div>
					<div class="inputcontent">

						<input type="password" name="password" required/>
					</div>
				</div>

				<div class="input nobottomborder">
					<div class="inputtext"><?echo translateKey("login_repeat_password");?>: </div>
					<div class="inputcontent">

						<input type="password" name="repeat_password" required/>
					</div>
				</div>
				<div class="buttons">

					<input class="orangebutton" type="submit" value="<?echo translateKey("register_confirm");?>" />

				</div>

			</form>

</section>

<?
	escreveFooter();
?>