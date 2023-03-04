<?php
	session_start();
	
	require_once('db_cred.php');

        function create_sessid() {
		for(;;) {
			$sessid = (mt_rand(10000000,99999999));
			$res = mysqli_query($conn, 'select count(*) as cnt from sessions where sessid='.$sessid);
			$row = mysqli_fetch_array($res, MYSQLI_ASSOC);
			if($row["cnt"] == 0) break;
		}
        	return($sessid);
        }
        

	$login_result = '';
	if ($_POST["login"] == 'manual') {
		// kind of sanitizing
		$username  = substr(preg_replace("/[^A-Za-z0-9 _]/", "", $_POST["username"]), 0, 14);
		$password  = substr(preg_replace("/[^A-Za-z0-9 _]/", "", $_POST["password"]), 0, 14);

		// no password entered, return to login page
		if($password == "" || $username=="") {
			header('Location: CRS-top.jsp');
			exit();
		}

		$res = mysqli_query($conn, 'select count(*) as cnt from users where userid="'.$username.'" and passwd="'.$password.'"');
		$row = mysqli_fetch_array($res, MYSQLI_ASSOC);
		$authc = false;
		if($row["cnt"] == 1) $authc = true;
		
		if($authc == false) {
			$login_result = 'Login failed. Your login/password combination is wrong.';
			$login_result .= '<br><a href="CRS-top.jsp">back</a>';
		} else {
			// login was successful, now delete old sessions and create a new one
			mysqli_query($conn, 'delete from sessions where lower(userid) = lower("'.$username.'")');
			$ip = $_SERVER["REMOTE_ADDR"];    // get the ip number of the user
			$port = $_SERVER["REMOTE_PORT"];  // get the port of the user
			
			// setup session
			$sessid = create_sessid();
			$res = mysqli_query($conn, 'insert into sessions (userid,ip,port,sessid,lastlogin) values(lower("'.$username.'"),"'.$ip.'","'.$port.'","'.$sessid.'",now())');
			if(!$res) $login_result = 'Login successful. Session creation failed.<br><a href="login.php">Back to menu</a>';
			else $login_result = 'Login successful.<br><a href="startsession.php?sessid='.$sessid.'.">Enter lobbies</a>';
		}

        } else if ($_POST["login"] == 'newaccount') {
		// kind of sanitizing
		$username  = substr(preg_replace("/[^A-Za-z0-9 _]/", "", $_POST["username"]), 0, 14);
		$password  = substr(preg_replace("/[^A-Za-z0-9 _]/", "", $_POST["password"]), 0, 14);

		// no password entered, return to login page
		if($password == "" || $username=="") {
			header('Location: CRS-top.jsp');
			exit();
		}

		// add new user
	        $res2 = mysqli_query($conn, 'insert into users (userid, passwd) values("'.$username.'","'.$password.'")');
		if($res2) {
			$ip = $_SERVER["REMOTE_ADDR"];    // get the ip number of the user
			$port = $_SERVER["REMOTE_PORT"];  // get the port of the user
			
			// setup session
			$sessid = create_sessid();
			$res = mysqli_query($conn, 'insert into sessions (userid,ip,port,sessid,lastlogin) values(lower("'.$username.'"),"'.$ip.'","'.$port.'","'.$sessid.'",now())');
			if(!$res) $login_result = 'Login successful. Session creation failed.<br><a href="login.php">Back to menu</a>';
			else $login_result = 'Login successful.<br><a href="startsession.php?sessid='.$sessid.'.">Enter lobbies</a>';
                }
		else {
		        $login_result = 'Login failed. User already exists.<br><a href="CRS-top.jsp">back</a>';
                }
                
	} else {
		$login_result = 'Login failed.<br><a href="CRS-top.jsp">back</a>';
	}
	
	include('header.php');
?>

<br></br>
<br></br>

<?php echo $login_result; ?>

<br></br>
<br></br>

<?php include('footer.php'); ?>
