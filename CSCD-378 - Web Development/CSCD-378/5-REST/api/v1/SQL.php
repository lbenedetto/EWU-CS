<?php
#PHPSTORM
#/etc/php/7.0/cgi/php.ini
#/usr/lib/php/20151012/xdebug.so
$host = "127.0.0.1";
$db = "id2995325_webdev";
$user = "id2995325_lbenedetto";
$pass = file_get_contents(__DIR__ . '/pw.txt');
$charset = "utf8mb4";

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$opt = [
	PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
	PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
	PDO::ATTR_EMULATE_PREPARES => false,
];
$pdo = new PDO($dsn, $user, $pass, $opt);

function delete($table, $stock)
{
	global $pdo;
	$del = $pdo->prepare("DELETE FROM $table WHERE stock = ?");
	$del->execute([$stock]);
	$affectedRows = $del->rowCount();
	echo json_encode(array(
			"stock" => $stock,
			"deleted" => $affectedRows != 0)
	);
}

function get($table, $stock, $endpointName1, $endpoint1, $endpointName2, $endpoint2)
{
	$vin = internal_get($table, $stock);
	if ($vin == null) {
		http_response_code(404);
		die();
	} else {
		echo json_encode(array(
			"stock" => $stock,
			$table => $vin[0][$table],
			$endpointName1 => $endpoint1,
			$endpointName2 => $endpoint2),
			JSON_UNESCAPED_SLASHES
		);
	}
}

function internal_get($table, $stock)
{
	global $pdo;
	$stmt = $pdo->prepare("SELECT * FROM $table WHERE stock = ?");
	$stmt->execute([$stock]);

	$vin = $stmt->fetchAll();
	return $vin;
}

function post($table, $stock, $data)
{
	global $pdo;
	$updated = false;
	$created = false;

	$vin = internal_get($table, $stock);
	try {
		if ($vin == null) {
			$created = $pdo->prepare("INSERT INTO $table(`stock`, $table) VALUES (?,?)")
				->execute([$stock, $data]);
		} else {
			$updated = $pdo->prepare("UPDATE $table SET $table=? WHERE stock = ?")
				->execute([$data, $stock]);
		}
	} catch (PDOException $e) {
		http_response_code(400);
		echo "Vehicle VIN must be added before any other data";
		die();
	}
	echo json_encode(array(
			"stock" => $stock,
			"created" => $created,
			"updated" => $updated)
	);
}

function options()
{
	echo "{
		    \"/api/v1/{endpoint}\": {
		      \"POST\": {
		        \"description\": \"Update or create an entry. This acts as PUT and POST.\",
		        \"parameters\": {
		          \"stock\": {
		            \"type\": \"string\",
		            \"description\": \"The stock number of the data to be retrieved\",
		            \"required\": true
  		          },
  		          \"{endpoint}\": {
		            \"type\": \"string\",
		            \"description\": \"The data to be updated or inserted\",
		            \"required\": true
		          }
		        }
		      }
		    },
		    \"/api/v1/{endpoint}/GET\": {
		      \"GET\": {
		        \"description\": \"Get an entry\",
		        \"parameters\": {
		          \"stock\": {
		            \"type\": \"string\",
		            \"description\": \"The stock number of the data to be retrieved\",
		            \"required\": true
		          }
		        }
		      }
		    },
		    \"/api/v1/{endpoint}/DELETE\": {
		      \"GET\": {
		        \"description\": \"Delete an entry\",
		        \"parameters\": {
		          \"stock\": {
		            \"type\": \"string\",
		            \"description\": \"The stock number of the data to be deleted\",
		            \"required\": true
		          }
		        }
		      }
		    }
		  }";
}