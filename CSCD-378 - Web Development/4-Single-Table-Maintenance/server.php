<?php
$host = "localhost";
$db = "id2995325_webdev";
$user = "id2995325_larsbenedetto";
$pass = file_get_contents('pw');;
$charset = "utf8mb4";

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$opt = [
	PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
	PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
	PDO::ATTR_EMULATE_PREPARES => false,
];
$pdo = new PDO($dsn, $user, $pass, $opt);

$rq = $_SERVER['REQUEST_METHOD'];
switch ($rq) {
	case "GET":
		get();
		break;
	case "PUT":
	case "POST":
		post();
		break;
	case "DELETE";
		delete();
		break;
}

function get()
{
	global $pdo;
	$stmt = $pdo->prepare("SELECT * FROM `movies`");
	$stmt->execute();
	echo json_encode($stmt->fetchAll());
}

function post()
{
	global $pdo;
	$name = $_POST['name'];
	$year = $_POST['year'];
	$studio = $_POST['studio'];
	$price = $_POST['price'];
	$description = $_POST['description'];
	$id = $_POST['id'];
	$stmt = $pdo->prepare("SELECT 1 FROM `movies` WHERE id = ?;");
	$stmt->execute([$id]);
	$foundID = $stmt->rowCount() > 0;
	if ($foundID) {
		$pdo->prepare("UPDATE `movies` SET `name`=?,`year`=?,`studio`=?,`price`=?,`description`=? WHERE id = 2")
			->execute([$name, $year, $studio, $price, $description, $id]);
	} else {
		$pdo->prepare("INSERT INTO `movies`(`name`, `year`, `studio`, `price`, `description`) VALUES (?,?,?,?,?)")
			->execute([$name, $year, $studio, $price, $description]);
	}
}

function delete()
{
	global $pdo;
	$id = $_POST["id"];
	$pdo->prepare("DELETE FROM `movies` WHERE id = ?")
		->execute([$id]);
}