<?php
$imei = $_GET["imei"];
$longitude = $_GET["longtitude"];
$latitude = $_GET["latitude"];
$altitude = $_GET["altitude"];
$time = round(microtime(true) * 1000);

# TODO: Error handling
echo exec("python Seretra/Prediction/append.py"
  . " " . $imei
  . " " . $time
  . " " . $longitude
  . " " . $latitude
  . " " . $altitude
  . " 2>&1 &", $out);

if(file_exists('.danger/' . $imei)) {
  readfile('.danger/' . $imei);
  unlink('.danger/' . $imei);
} else {
  echo "-1";
}

?>
