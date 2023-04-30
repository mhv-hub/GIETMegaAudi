<?php

$con = mysqli_connect("localhost","id3260213_mymega","mymega","id3260213_megaaudi");

$sql = "select * from slot_details;";

$result = mysqli_query($con,$sql);

$response = array();

while($row = mysqli_fetch_array($result)){

    array_push($response,array("slotid"=>$row[0],"slottime"=>$row[1]));
}

echo json_encode(array("server_response"=>$response));

mysqli_close($con);


?>