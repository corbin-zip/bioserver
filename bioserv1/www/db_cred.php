<?php
    $serv = 'localhost';
    $datb = 'bioserver';
    $user = 'bioserver';
    $pass = 'xxxxxxxxxxxxxxxx';
    
    $conn = mysqli_connect($serv, $user, $pass, $datb)
        or die ("connection error");

?>
