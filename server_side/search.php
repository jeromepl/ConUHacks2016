<?php
    include_once("mySQL_connection.php");

    if(isset($_GET['query']) && strlen($_GET['query']) > 0) {
        
        //$in_list = implode(',', explode('_', $_GET['query']));
        
        $query = implode('|', explode('_', $_GET['query']));
        
        $req = $bdd->prepare("SELECT f.name
                                    FROM files f
                                    INNER JOIN tags t ON t.file_id = f.id
                                    WHERE t.tag REGEXP :query
                                    ORDER BY f.time DESC");
        
		$req->execute(array('query' => $query)) or die(print_r($bdd->errorInfo()));
        
        $results = array();
		while($data = $req->fetch()) {
			array_push($results, $data['name']);
		}
		
		$req->closeCursor();
        
        echo(implode(';', $results));
        
    }