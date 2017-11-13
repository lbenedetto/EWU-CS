<?php
include "../SQL.php";

$rq = $_SERVER['REQUEST_METHOD'];
if ($rq == "POST")
	post("entrydate", $_POST['stock'], $_POST['entrydate']);
else
	options();