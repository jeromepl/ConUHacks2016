<?php
    if(isset($_GET['name']) && strlen($_GET['name']) > 0) {
        $filename= $_GET['name'];
        $fileData=file_get_contents('php://input');
        $fhandle=fopen("files/" . $filename, 'wb');
        fwrite($fhandle, $fileData);
        fclose($fhandle);
        echo("Done uploading");
    }
?>