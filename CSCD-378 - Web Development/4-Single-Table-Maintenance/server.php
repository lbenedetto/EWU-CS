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
	case "POST":
		break;
	case "PUT":
		break;
	case "DELETE";
		break;
}

function get()
{
	global $pdo;
	$stmt = $pdo->prepare("SELECT * FROM `movies`");
	$stmt->execute();
	echo json_encode($stmt->fetchAll());
}

function post($name, $year, $studio, $price, $description)
{
	global $pdo;
	$stmt = $pdo->prepare("INSERT INTO `movies`(`name`, `year`, `studio`, `price`, `description`) VALUES (?,?,?,?,?)");
	$stmt->execute([$name, $year, $studio, $price, $description]);
}

function put($name, $year, $studio, $price, $description, $id)
{
	global $pdo;
	$pdo->prepare("UPDATE ? name = ? AND year = ? AND studio = ? AND price = ? AND description = ? WHERE id = ?")
		->execute([$name, $year, $studio, $price, $description, $id]);
}

function delete($id)
{
	global $pdo;
	$pdo->prepare("DELETE FROM `movies` WHERE id = ?")
		->execute([$id]);
}