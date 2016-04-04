<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();

$acao = acaoAtual();


//escreveHeader();
//login();
$openid = new LightOpenID(getHomeDir());

$openid->identity = 'https://www.google.com/accounts/o8/id';
$openid->required = array(
    'namePerson/first',
    'namePerson/last',
    'contact/email',
);
$openid->returnUrl = getHomeDir() . 'login.php';
?><a href = "<?php echo $openid->authUrl() ?>">Login with Google</a><?

//escreveFooter();

function login() {
    $openid = new LightOpenID("my-domain.com");

    if ($openid->mode) {
        if ($openid->mode == 'cancel') {
            echo "User has canceled authentication !";
        } elseif ($openid->validate()) {
            $data = $openid->getAttributes();
            $email = $data['contact/email'];
            $first = $data['namePerson/first'];
            echo "Identity : $openid->identity <br>";
            echo "Email : $email <br>";
            echo "First name : $first";
        } else {
            echo "The user has not logged in";
        }
    } else {
        echo "Go to index page to log in.";
    }
}

?>