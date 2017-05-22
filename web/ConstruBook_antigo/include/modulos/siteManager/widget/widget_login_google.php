<?
/* Shows a button for login on google's */

class WidgetLoginGoogle extends WidgetSiteManager {

    function show($id) {
        $openid = new LightOpenID(getHomeDir());

        $openid->identity = 'https://www.google.com/accounts/o8/id';
        $openid->required = array(
            'namePerson/first',
            'namePerson/last',
            'contact/email',
        );
        $openid->returnUrl = getHomeDir() . 'auth';
        //echo "aa" . $openid->authUrl();
        if (AMBIENTE != AMBIENTE_PRODUCAO) {
            $authUrl = "http://local";
        } else {
            $authUrl = $openid->authUrl();
        }
        ?><a href = "<?php echo $authUrl; ?>"><img style="width:20%" src="<? echo getHomeDir() . "images/sign-in-with-google.png" ?>"></a><?
//
    }

    function getTitle() {
        return translateKey("txt_property");
    }

}
?>