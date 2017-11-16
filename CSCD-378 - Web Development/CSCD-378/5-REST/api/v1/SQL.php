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

try {
	$pdo = new PDO($dsn, $user, $pass, $opt);
} catch (PDOException $e) {
	echo "Could not connect to database\n";
	echo $e;
}

function delete($table) {
	global $pdo;
	$stock = $_GET['stock'];
	$del = $pdo->prepare("DELETE FROM $table WHERE stock = ?");
	$del->execute([$stock]);
	$affectedRows = $del->rowCount();
	echo json_encode(array(
			"stock" => $stock,
			"deleted" => $affectedRows != 0)
	);
}

function get($table) {
	$arg = $_GET["stock"];
	if ($arg != null) {
		getOne($table, $arg);
	}
	$arg = $_GET["regex"];
	if ($arg != null) {
		getMultiple($table, $arg);
	}
	getAll($table);
}

function getOne($table, $stock) {
	global $pdo;
	$stmt = $pdo->prepare("SELECT * FROM $table WHERE stock = ?");
	$stmt->execute([$stock]);
	returnResults($stmt->fetchAll());
}

function getAll($table) {
	global $pdo;
	$stmt = $pdo->prepare("SELECT * FROM $table");
	$stmt->execute();
	returnResults($stmt->fetchAll());
}

function getMultiple($table, $regex) {
	global $pdo;
	$stmt = $pdo->prepare("SELECT * FROM $table WHERE $table REGEXP ?");
	$stmt->execute([$regex]);
	returnResults($stmt->fetchAll());
}

function returnResults($results) {
	if ($results == null) {
		echo "Could not find any entries matching your request";
		http_response_code(404);
		die();
	} else {
		echo json_encode($results, JSON_UNESCAPED_SLASHES);
	}
	die(0);
}

function post($table) {
	global $pdo;
	$stock = $_POST['stock'];
	$data = $_POST['data'];
	$created = false;
	try {
		$created = $pdo->prepare("INSERT INTO $table(`stock`, $table) VALUES (?,?)")
			->execute([$stock, $data]);
	} catch (PDOException $e) {
		echo "Cannot have duplicate stock numbers\n";
		http_response_code(400);
	}
	echo json_encode(array(
			"stock" => $stock,
			"created" => $created)
	);
	die(0);
}

function put($table) {
	global $pdo;
	$stock = $_POST['stock'];
	$data = $_POST['data'];
	$updated = false;
	try {
		$updated = $pdo->prepare("UPDATE $table SET $table=? WHERE stock = ?")
			->execute([$data, $stock]);
	} catch (PDOException $e) {
		http_response_code(400);
	}
	echo json_encode(array(
			"stock" => $stock,
			"updated" => $updated)
	);
	die(0);
}

function options($endpoint) {
	echo "[
  {
	\"/api/v1/$endpoint\": {
	  \"GET\": {
		\"description\": \"Get options in JSON format (what you are currently viewing)\"
	  }
	},
	\"/api/v1/$endpoint/CREATE\": {
	  \"POST\": {
		\"description\": \"Create an entry. This acts as a regular POST request.\",
		\"parameters\": {
		  \"stock\": {
			\"type\": \"string\",
			\"description\": \"The stock number of the data to be added\",
			\"required\": true
		  },
		  \"data\": {
			\"type\": \"string\",
			\"description\": \"The data to be inserted\",
			\"required\": true
		  }
		}
	  }
	},
	\"/api/v1/$endpoint/UPDATE\": {
	  \"POST\": {
		\"description\": \"Update an entry. This acts as a PUT request.\",
		\"parameters\": {
		  \"stock\": {
			\"type\": \"string\",
			\"description\": \"The stock number of the data to be updated\",
			\"required\": true
		  },
		  \"data\": {
			\"type\": \"string\",
			\"description\": \"The data to be updated\",
			\"required\": true
		  }
		}
	  }
	},
	\"/api/v1/$endpoint/GET\": {
	  \"GET\": {
		\"description\": \"Get an entry. Only one of the three parameters may be active at a time\",
		\"parameters\": {
		  \"stock\": {
			\"type\": \"string\",
			\"description\": \"The stock number of the data to be retrieved\",
			\"required\": false
		  },
		  \"regex\": {
			\"type\": \"string\",
			\"description\": \"A regular expression that matches the data you want\",
			\"required\": false
		  },
		  \"all\": {
			\"type\": \"empty\",
			\"description\": \"Returns all data\",
			\"required\": false
		  }
		}
	  }
	},
	\"/api/v1/$endpoint/DELETE\": {
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
  }
]";
	die(0);
}