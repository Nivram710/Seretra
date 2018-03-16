<?php

error_reporting(0);
require "conn.php";

$imei = $_GET["imei"];

if($result = $db->query("SELECT * FROM locations WHERE imei = $imei")) {

	if($result->num_rows) {
		echo "true";
	} else {
		echo "false";
	}
} else {
	die("No IMEI found");
}
?>
