<?php

    $con = mysqli_connect("localhost","id3260213_mymega","mymega","id3260213_megaaudi");

	$vid = $_POST["id"];
	$votp = $_POST["otp"];

	$res = 0;

	$sql = "select * from otp_student;";

	$result = mysqli_query($con,$sql);

	$response = array();

	while($row = mysqli_fetch_array($result)){

   		if($row[0]==$vid && $row[1]==$votp)
  		{
            $res=1;
		}
	}
	array_push($response,array("otpresponse"=>$res));
	echo json_encode(array("server_response"=>$response));
	mysqli_close($con);


?>
