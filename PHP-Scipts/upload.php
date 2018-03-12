<?php

error_reporting(0);
require "conn.php";

$imei = $_GET["imei"];
$longtitude = $_GET["longtitude"];
$latitude = $_GET["latitude"];

if ($inert = $db->query("INSERT INTO locations (imei, longtitude, latitude, time) VALUES ('$imei', '$longtitude', '$latitude', CURRENT_TIMESTAMP)")) {
	echo "Success";
} else {
	die("Error: " . $db->error);
}

?>
