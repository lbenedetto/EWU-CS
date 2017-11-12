<?php
include "../../SQL.php";

get("vin", $_GET["stock"], "entrydate", "/api/v1/entrydate/GET", "ups", "/api/v1/ups/GET");