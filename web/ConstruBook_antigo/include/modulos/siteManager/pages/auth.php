<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';

inicializa();

login();

function login() {
    $openid = new LightOpenID(getHomeDir());

    if ($openid->mode) {
        if ($openid->mode == 'cancel') {
            redirect(getHomeDir() . "login");
        } elseif ($openid->validate()) {
            $data = $openid->getAttributes();
            $email = $data['contact/email'];
            $first = $data['namePerson/first'];
            $last = $data['namePerson/last'];

            /* echo "Identity : $openid->identity <br>";
              echo "Email : $email <br>";
              echo "First name : $first <br>";
              echo "Last name : $last"; */
            $controller = getControllerForTable("usuario");
            $controller->loginOrRegister($email, $first, $openid);
        } else {
            redirect(getHomeDir() . "login");
        }
    } else {
        redirect(getHomeDir() . "login");
    }
}

?>