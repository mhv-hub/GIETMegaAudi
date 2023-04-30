<?php

    $con = mysqli_connect("localhost","id3260213_mymega","mymega","id3260213_megaaudi");

	$vid = $_POST["id"];
	$vseatno = $_POST["seatno"];

	$sql = "insert into book_status values('$vid','$vseatno');";
	
	mysqli_query($con,$sql);
	
	$sql = "update seat_status set status_seat = 1 where seat_no = '"+$vseatno+"'";
	
	mysqli_query($con,$sql);
	
	mysqli_close($con);
?>