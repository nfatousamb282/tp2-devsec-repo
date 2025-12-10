<%@ taglib uri="jakarta.tags.core"  prefix="c" %>
<!DOCTYPE html>

<!--
  -- @author ouziri
  -- @version 0.1
-->

<html>
<head>
	<title>Account</title>
	<link rel="stylesheet" href="../../styles/style.css">
</head>

<body>

	<div>
		<!-- form for ADMIN role -->
		
<%-- 	<c:if test="${sessionScope.principal.roles }"> --%>
	  <h2>View account By Id (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h2>
	  <form action="../../viewAccount" method="get">
	    <label>Account number: <input type="text" name="pAccountId" /></label><br/>
	    <input type="text" hidden="true" name="searchBy" value="serachById" />
	    <button>View</button>
	  </form>
<%-- 	</c:if> --%>
	  
	  <h2>View accounts by owner (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h2>
	  <form action="../../viewAccount" method="get">
	    <label>Owner name : <input type="text" name="pOwnerEmail" /></label><br/>
	    <input type="text" hidden="true" name="searchBy" value="serachByOwnerEmail" />
	    <button>View</button>
	  </form>
	  
	  	<p><a href="../../">Home page</a></p>
	  	
	  	
  </div>
</body>
</html>
