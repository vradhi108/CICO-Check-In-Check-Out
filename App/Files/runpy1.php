<?php 

$command = escapeshellcmd('python maintrain.py');
$output = shell_exec($command);
echo $output;

?>