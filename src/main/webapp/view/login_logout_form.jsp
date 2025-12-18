<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core"  prefix="c" %>

<!DOCTYPE html>

<!--
-- @author ouziri
-- @version 0.1
-->

<html>

<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<link rel="stylesheet" href="../styles/style.css">
</head>

<body>

<c:if test="${sessionScope.principal == null}">
	<div class="div-login">
		<h2>Login (not yet connected)</h2>
		<form action="../auth" method="post" onsubmit="verifTaillePwd(event)">
			<label>Email: <input type="text" id="idUserEmail" name="pUserEmail" required /></label><br/>
			<label>Password: <input type="password" id="idUserPwd" name="pUserPwd" required/></label><br/>
			<span id="pwd-error-msg" class="error-message"></span> <br/>
			<input type="hidden" name="action" value="login"/>
			<button>Login</button>
		</form>

		<c:if test="${not empty param.message}">
			<div class="div-message ${param.messageType eq 'success' ? 'success-message' : 'error-message'}">
					${param.message}
			</div>
		</c:if>
	</div>
</c:if>

<c:if test="${sessionScope.principal != null}">
	<div class="div-logout">
		<h2>Logout confirmation (connected as: ${sessionScope.principal.name})</h2>
		<form action="../auth" method="post">
			<input type="hidden" name="action" value="logout"/>
			<button>Logout</button>
		</form>

	</div>
</c:if>

<div class="home-link">
	<a href="../"> Home page</a>
</div>

<script>
	function verifTaillePwd(event) {
		var pwd = document.getElementById("idUserPwd").value;
		var errorDiv = document.getElementById("pwd-error-msg");
		if (pwd.length <= 2) {
			errorDiv.textContent = "Password must be longer than 2 characters";
			event.preventDefault();
		} else {
			errorDiv.textContent = "";
		}
	}
</script>


</body>
</html>
