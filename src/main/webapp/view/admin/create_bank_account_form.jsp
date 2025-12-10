<!DOCTYPE html>

<!--
  -- @author ouziri
  -- @version 0.1
-->

<html>
<head>
	<title>Account creation</title>
<!-- 	<meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
		<link rel="stylesheet" href="../../styles/style.css">
</head>

<body>
	<div>
		<!-- Account creation form -->
		
	  <h2>Create account (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h2>
	  <form action="../../CreateBankAccount" method="post">
	    <label>Client email: <input type="text" name="pClientEmail" /></label><br/>
	    <button>Create</button>
	  </form>
	  
	  	<p><a href="../../">Home page</a></p>
  </div>
</body>
</html>
