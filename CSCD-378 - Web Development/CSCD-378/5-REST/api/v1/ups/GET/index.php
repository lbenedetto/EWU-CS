<?php
include "../../SQL.php";

get("ups", $_GET["stock"], "vin", "/api/v1/vin/GET", "entrydate", "/api/v1/entrydate/GET");