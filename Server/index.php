<?php

if (isset($_POST["action"]) && $_POST["action"] == "insert"){
	$username = $_POST["username"];
	$timestamp = $_POST["timestamp"];
	$data = $_POST["json"];

	if (!file_exists($username)){
		mkdir($username, 0777, true);
	}
	
	chdir($username);

	mkdir('raw', 0777, true);

	chdir('raw');	

	$newFile = fopen($timestamp.".json", "w") or die("Unable to create files!");

	fwrite($newFile, $data);

	fclose($newFile);

	echo "Success";
}

?>
