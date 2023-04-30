<?php

$con = mysqli_connect("localhost","....................",".............","................");

$vroll = $_POST["roll"];

$sql = "select * from faculty_details;";

$result = mysqli_query($con,$sql);

$response = array();

while($row = mysqli_fetch_array($result)){
    
    if($row[0]==$vroll)
    {
        array_push($response,array("name"=>$row[1]));
        break;
    }
}

echo json_encode(array("server_response"=>$response));

mysqli_close($con);


?>