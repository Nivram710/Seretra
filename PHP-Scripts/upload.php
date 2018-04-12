<?php

$debug = false;
$imei = $_GET["imei"];
$longitude = $_GET["longtitude"];
$latitude = $_GET["latitude"];
$time = round(microtime(true) * 1000);

if($debug) {
	echo "IMEI: " . $imei;
	echo "<br>----------------------<br>";
	echo "Longtitude: " . $longitude;
	echo "<br>----------------------<br>";
	echo "Latitude: " . $latitude;
	echo "<br>----------------------<br>";
	echo "Time: " . $time;
	echo "<br>----------------------<br>";

	// TODO: check if values are not empty
	echo "python /home/pi/development/mint/seretra/Prediction/append.py" . " " . $imei . " " . $time . " " . $longitude . " " . $latitude . " 2>&1 &";
}

exec("python /home/pi/development/mint/seretra/Prediction/append.py" . " " . $imei . " " . $time . " " . $longitude . " " . $latitude . " 2>&1 &", $out);
echo "<br>Outputs:<br>";
foreach($out as $line) {
	echo $line . "<br>";
}

?>
