<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core"  prefix="c" %>
<%-- <%@ taglib prefix="fn" uri="jakarta.tags.functions" %> --%>

<!DOCTYPE html>

<!--
  -- @author ouziri
  -- @version 0.1
-->

<html>
<head>	
	<title>Bank Demo - Vulnerable</title>
	<meta accept-charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./styles/style.css">
</head>

<body>
  <h1> Bank Application (connected as: ${sessionScope.principal == null ? "none" : sessionScope.principal.name}) </h1>
  

  
  <ul>
<%--    you are: ${cookie.ClientRole.value} --%>
   	<c:if test="${cookie.ClientRole.value eq 'ADMIN'}">
    	<li><a href="./view/admin/create_bank_account_form.jsp">Create a bank account</a></li>
    	<li><a href="./view/admin/view_accounts_form.jsp">View account(s)</a></li>
  	</c:if>
  	<c:if test="${cookie.ClientRole.value eq 'CLIENT'}">
  		<li><a href="./view/client/view_accounts_form.jsp">View account(s)</a></li>
  		<li><a href="./view/client/do_transaction.jsp">Perform a transaction</a></li>  
  	</c:if>
  	
    <li><a href="./view/login_logout_form.jsp"> ${sessionScope.principal == null ? 'Login' : 'Logout' } </a></li>
    	
  </ul>
  
  <c:if test="${not empty param.message}">
	     <div class="div-message ${param.messageType eq 'success' ? 'success-message' : 'error-message'}">
	        	${param.message}            	           
	     </div>
  </c:if>
  
</body>
</html>
