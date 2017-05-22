<?php

class IOUtils {

    /**
     * Sends an email
     * 
     * @param type $emailTo
     * @param type $emailFrom
     * @param type $title
     * @param type $body
     */
    function sendEmailPear($emailTo, $emailFrom, $title, $body) {
        $recipients = $emailTo;

        $headers["From"] = $emailFrom;
        $headers["To"] = $emailTo;
        $headers["Subject"] = $title;


        $params["host"] = "example.com";
        $params["port"] = "25";
        $params["auth"] = true;
        $params["username"] = "user";
        $params["password"] = "password";

// Create the mail object using the Mail::factory method 
        $mail_object = & Mail::factory("smtp", $params);

        $html = "<html>";
        $html.="<head>";
        $html.="<title>$title</title>";
        $html.="</head>";
        $html.="<body>" . $body;
        $html.="</body>";
        $html.="</html>";

        $mail_object->send($recipients, $headers, $html);
    }

    function sendEmail($emailTo, $emailFrom, $title, $body) {

        $html = "<html>";
        $html.="<head>";
        $html.="<title>$title</title>";
        $html.="</head>";
        $html.="<body>" . $body;
        $html.="</body>";
        $html.="</html>";
        $headers = 'MIME-Version: 1.0' . "\r\n";
        $headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
        $headers .= 'From: ' . $emailFrom . "\r\n" .
                'X-Mailer: PHP/' . phpversion();
        mail($emailTo, $title, $html, $headers);
    }

    function generateRandomString($length = 10) {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $randomString = '';
        for ($i = 0; $i < $length; $i++) {
            $randomString .= $characters[rand(0, strlen($characters) - 1)];
        }
        return $randomString;
    }

}

?>
