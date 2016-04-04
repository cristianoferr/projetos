<?
	session_start();
	unset($_SESSION['id_usuario']); // repeat for all session variables 
    session_unregister(); 
    session_destroy(); 
	header( "Location: http://pictuly.com");

?>