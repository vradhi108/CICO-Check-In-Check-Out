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
$pass = $_GET['b'];
$sql = "SELECT * FROM user WHERE username = '$usern' AND password = '$pass'";
$result = mysqli_query($conn,$sql);
$check = mysqli_fetch_array($result);
$count = mysqli_num_rows($result);
if($count > 0){
    echo "Login Successful";
}else{
    echo "Login failed";
}
?>