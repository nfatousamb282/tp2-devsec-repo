<%@ taglib uri="jakarta.tags.core"  prefix="c" %>
<!DOCTYPE html>

<!--
  -- @author ouziri
  -- @version 0.1
-->

<html>
<head>
	<title>Account creation</title>
		<link rel="stylesheet" href="../../styles/style.css">
</head>

<body>

	<div class="form-container">
	  <h2>View your account by Id (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h2>
	  <form action="../../viewAccount" method="get">
	    <label>Account number: <input type="number" name="pAccountId" /></label><br/>
	    <input type="text" hidden="true" name="pOwnerEmail" value="${sessionScope.principal.email}" />
	    <input type="text" hidden="true" name="searchBy" value="serachByIdAndOwnerEmail" />
	    <button>View</button>
	  </form>
	</div>
	
	<div class="form-container">
	  <h2>View all your accounts (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h2>
	  <form action="../../viewAccount" method="get">
	    <input type="text" hidden="true" name="pOwnerEmail" value="${sessionScope.principal.email}" />
	    <input type="text" hidden="true" name="searchBy" value="serachByOwnerEmail" />
	    <button>View</button>
	  </form>
	</div>	  
	
	  <div class="home-link">
	  	<p><a href="../../">Home page</a></p>
	  </div>
  
</body>
</html>
