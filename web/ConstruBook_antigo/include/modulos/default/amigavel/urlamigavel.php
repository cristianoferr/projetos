<?php 
$var = $_SERVER ['REQUEST_URI'];
$var = explode('/',$var);
//$var = explode('-',$var[3]);
/*echo '<h1>Article ID: '.$var[0].'</h1>';
echo '<h1>bosta ID: '.$var[1].'</h1>';
echo '<h1>tonto ID: '.$var[2].'</h1>';*/

	
	if(!Empty($var[7]))
	{
		$cond++;
	}
	elseif(!Empty($var[6]) && $cond == 0)
	{
		$cond++;
	}
	elseif(!Empty($var[5]) && $cond == 0)
	{
		$cond++;
	}
	elseif(!Empty($var[4]) && $cond == 0)
	{
		$cond++;
		$url = '/../../..';
	}
	elseif(!Empty($var[3]) && $cond == 0)
	{
		$cond++;
		$url = '/../..';
	}
	elseif(!Empty($var[2]) && $cond == 0)
	{
		$url = '/..';
		$cond++;
	}
	elseif(!Empty($var[1]) && $cond == 0)
	{
		$url = '..';
		$cond++;
	}
	else
	{
		$url = '.';
		
	}

	
?>