<?php
	session_start();
	include('header.php');
?>

<font size="-2"><br></br></font>			
<table align="center" width="100%" cellspacing="0" cellpadding="0">
	<tr align="center" valign="top">
		<td align="center" width="50%">
			<form method="post" action="login_form.php">
				Login with existing account:<br></br>
				<table>
					<tr>
						<td>ID:</td>
						<td><input type="text" name="username"></input></td>
					</tr>
					
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password"></input></td>
					</tr>
					
					<input type="hidden" name="login" value="manual"></input>
					<tr><td></td><td><input type="submit" value="LOGIN"></input></td></tr>
					
				</table>
			</form>
		</td>

		<td align="center" width="50%">
			<form method="post" action="login_form.php">
				Create new account and login:<br></br>
				<table>
					<tr>
						<td>ID:</td>
						<td><input type="text" name="username"></input></td>
					</tr>
					
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password"></input></td>
					</tr>
					
					<input type="hidden" name="login" value="newaccount"></input>
					<tr><td></td><td><input type="submit" value="LOGIN"></input></td></tr>
					
				</table>
			</form>
		</td>
	</tr>
</table>

<?php include('footer.php'); ?>