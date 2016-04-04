<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Entity Manager</title>
	</head>
	<body>
<?
$v="é:  %C3%A9";

echo "v: ".$v."<br>";
echo "utf8_urldecode: ".utf8_urldecode($v)."<br>";
echo "urldecode: ".urldecode($v)."<br>";


function utf8_urldecode($str) {
    $str = preg_replace("/%u([0-9a-f]{3,4})/i","&#x\\1;",urldecode($str));
    return html_entity_decode($str,null,'UTF-8');;
  }
?>
</body></html>