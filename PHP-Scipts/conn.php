<?php
$servername = "localhost";
$user = "username";
$password = "geheimes Passwort";

$db = new mysqli($servername, $user, $password, "seretra");

if ($db->connect_errno) {
        echo $db->errno;
        die("Connection failed.");
}

?>
