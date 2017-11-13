<?php
include "../SQL.php";

$rq = $_SERVER['REQUEST_METHOD'];
if ($rq == "POST")
	post("vin", $_POST['stock'], $_POST['vin']);
else
	options();