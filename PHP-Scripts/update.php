<?php

error_reporting(0);
require "conn.php";

$imei = $_GET["imei"];
$longtitude = $_GET["longtitude"];
$latitude = $_GET["latitude"];

if($update = $db->query("UPDATE locations SET longtitude = '$longtitude', latitude = '$latitude', time = CURRENT_TIMESTAMP WHERE imei = $imei")) {
	echo "Success";
} else {
	die("Error" . $db->error);
}
?>
