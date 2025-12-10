<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core"  prefix="c" %>
<!DOCTYPE html>


<!--
  -- @author ouziri
  -- @version 0.1
-->

<head>
	<meta charset="UTF-8">
	<title>View banck accounts</title>
	<!-- 	<meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
		<link rel="stylesheet" href="./styles/style.css">
</head>

<html>
<body>

	<h1> Client accounts (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h1>

	<div>

	<c:choose>
		<c:when test="${accounts == null}">
			<p>Account id <b> ${param.pAccountId} </b> not found</p>
		</c:when>
		<c:when test="${accounts != null}">

		  
		  
		  <table border="1" cellpadding="5">
		    <tr><th>Account ID</th><th>Owner email</th><th>Owner name</th><th>Balance</th><th>Action</th></tr>
		    <c:forEach var="account" items="${accounts}">		    
			      <tr>
			        <!-- NOT ENCODED -> Stored XSS possible -->
			        <td>${account.accountId}</td>
			        <td>${account.owner.email}</td>
			        <td>${account.owner.name}</td>
			        <td>${account.balance}</td>
			        <td><a href="./view/client/do_transaction.jsp?pAccountId=${account.accountId}">Transaction</a></td>
			      </tr>		    
		      </c:forEach>
		  </table>
		  
		</c:when>
	</c:choose>
	
	</div>
	
	 <div class="home-link">
		<p><a href="./">Home page</a></p>
	</div>
</body>
</html>
