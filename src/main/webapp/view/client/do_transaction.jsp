<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>

<!--
  -- @author ouziri
  -- @version 0.1
-->

<html>
<head>	
	<title>Transaction - Vulnerable</title>
	<meta accept-charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../styles/style.css">
</head>


<body>
	<div>
		<h2>Perform a transacti (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name})</h2>
		<form action="../../transaction" method="post">
			<label>Account Id: </label>
			<input type="text" name="pAccountId" value="${param.pAccountId != null ? param.pAccountId : ''}" ${param.pAccountId != null ? 'readonly' : ''} > 
			<br/>
			<label>Amount (value or arithmetic expression): </label>
			<input type="text" name="pAmountExpression"> 
			<br/>	
			<button>Execute</button>
		</form>
	</div>
	
	<!-- View transaction confirmation -->
	<div hidden=${TransactionOK == false and  TransactionKO == false}">
		<c:choose>
			<c:when test="${TransactionOK == true}">
				<p style="color: green;">Transaction done for account : ${pAccountId}</p>
			</c:when>
			<c:when test="${TransactionOK == false}">
				<p style="color: red;">Transaction error for account : ${pAccountId}</p>
			</c:when>
		</c:choose>
	</div>
	
	<div>
		<a href="../../.">Home page</a>
	</div>

</body>
</html>
