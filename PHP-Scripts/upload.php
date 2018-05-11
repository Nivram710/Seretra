<?php

$debug = false;
$imei = $_GET["imei"];
$longitude = $_GET["longtitude"];
$latitude = $_GET["latitude"];
$time = round(microtime(true) * 1000);

# TODO: Error handling
exec("python /home/pi/development/mint/seretra/Prediction/append.py"
  . " " . $imei
  . " " . $time
  . " " . $longitude
  . " " . $latitude
  . " 2>&1 &", $out);

foreach($out as $line) {
	echo $line . "<br>";
}

?>
