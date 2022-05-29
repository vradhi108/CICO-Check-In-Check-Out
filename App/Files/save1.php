<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "userregistration";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}
$usern = $_GET['a'];
$name = $_GET['b'];
$pass = $_GET['c'];
$lati = $_GET['d'];
$longi = $_GET['e'];
$sql = "INSERT INTO user (username, name, password, latitude, longitude) VALUES ('$usern', '$name', '$pass', '$lati', '$longi')";

if ($conn->query($sql) === TRUE) {
  echo "New record created successfully";
} else {
  echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>