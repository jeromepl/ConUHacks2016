<?php
    include_once("mySQL_connection.php");

    if(isset($_GET['query']) && strlen($_GET['query']) > 0) {
        
        $in_list = implode(',', explode('_', $_GET['query']));
        
        $answer = $bdd->prepare('SELECT f.name
                                    FROM files f
                                    INNER JOIN tags t ON t.file_id = f.id
									WHERE t IN (:query)
                                    ORDER BY f.time DESC');
		$answer->execute(array('query' => $_SESSION['id'],
								'query1' => $_GET['query'],
								'query2' => $_GET['query']))
								or die(print_r($bdd->errorInfo()));
									
		while($data = $answer->fetch()) {
			
		}
		
		$answer->closeCursor();
        
    }