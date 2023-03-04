<?php
    $serv = 'localhost';
    $datb = 'bioserver';
    $user = 'bioserver';
    $pass = 'xxxxxxxxxxxxxxxx';
    
    $conn = mysql_connect($serv, $user, $pass)
        or die ("connection error");

    mysql_select_db($datb, $conn)
        or die("database failure");
?>