<?php
include "../SQL.php";

$rq = $_SERVER['REQUEST_METHOD'];
if ($rq == "POST")
	post("ups", $_POST['stock'], $_POST['ups']);
else
	options();