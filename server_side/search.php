<?php
    include_once("mySQL_connection.php");

    if(isset($_GET['query']) && strlen($_GET['query']) > 0) {
        
        //$in_list = implode(',', explode('_', $_GET['query']));
        
        $words = explode('_', $_GET['query']);
        for($i = 0; $i < count($words); $i++) {
            $words[$i] = '"' . $words[$i] . '"';
        }
        $query = implode(',', $words);
        
        $req = $bdd->prepare("SELECT DISTINCT f.name
                                    FROM files f
                                    INNER JOIN tags t ON t.file_id = f.id
                                    WHERE f.name IN(" . $query . ") OR t.tag IN(" . $query . ")
                                    ORDER BY f.time DESC");
        
		$req->execute() or die(print_r($bdd->errorInfo())); //array('query1' => $query, 'query2' => $query)
        
        $results = array();
		while($data = $req->fetch()) {
			array_push($results, $data['name']);
		}
		
		$req->closeCursor();
        
        echo(implode(';', $results));
        
    }