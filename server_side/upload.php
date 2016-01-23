<?php
    include_once("mySQL_connection.php");

    if(isset($_GET['name']) && strlen($_GET['name']) > 0) {
        
        //name contains the extension
        
        //Upload the file to the "files" folder
        $filename= $_GET['name'];
        $fileData=file_get_contents('php://input');
        $fhandle=fopen("files/" . $filename, 'wb');
        fwrite($fhandle, $fileData);
        fclose($fhandle);
        echo("Done uploading");
        
        //Add the file to the database
        $req = $bdd->prepare('INSERT INTO files(id, name, time)
								VALUES(\'\', :name, NOW())');
        $req->execute(array('name' => $_GET['name']));
        echo $bdd->lastInsertId();
    }
?>