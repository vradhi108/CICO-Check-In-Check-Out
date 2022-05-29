<?php 

$command = escapeshellcmd('python maindetect.py '.$_GET["photo"]);
$output = shell_exec($command);
echo $output;

?>