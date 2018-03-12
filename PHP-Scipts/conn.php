<?php
$servername = "localhost";
$user = "seretra";
$password = "aNxv6mZ1eT5OgBwf";

$db = new mysqli($servername, $user, $password, "seretra");

if ($db->connect_errno) {
        echo $db->errno;
        die("Connection failed.");
}

?>
