<?

function urlRewriteCallback($match) {
	$extra = '';
	if ($match[3]) {
		$params = explode('&', $match[3]);
		if ($params[0] == '') array_shift($params);
		foreach ($params as $param) {
			$paramEx = explode('=', $param);
			$extra .= $paramEx[0].'/'.$paramEx[1].'/';
		}
	}
	return BASE_URL.$match[1].'/'.$extra;
} 


$pageContents = ob_get_contents();
ob_end_clean();
echo preg_replace_callback($urlPatterns, 'urlRewriteCallback', $pageContents); 

?>