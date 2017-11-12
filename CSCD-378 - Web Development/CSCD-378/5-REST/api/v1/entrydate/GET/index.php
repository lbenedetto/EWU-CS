<?php
include "../../SQL.php";

get("entrydate", $_GET["stock"], "vin", "/api/v1/vin/GET", "ups", "/api/v1/ups/GET");