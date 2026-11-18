<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Login</title>
</head>
<body class="login-page">
	<div class="container">
		<div class="login-box panel panel-default">
			<div class="panel-heading text-center">
				<img height="40px" width="40px" alt="CRUD Manager" 
					 src="${pageContext.request.contextPath}/images/logo.png">
				<h3>CRUD Manager</h3>
			</div>
			
			<div class="panel-body">
				<c:if test="${not empty message}">
					<div class="alert ${alertType eq 1 ? 'alert-success' : 'alert-danger'}" 
						 style="position: static; width: auto;">
						<c:out value="${message}" />
					</div>
				</c:if>
				
				<form action="${pageContext.request.contextPath}/login" method="POST">
					<div class="form-group">
						<label for="email">E-mail</label>
						<input type="email" class="form-control" id="email" name="email" 
							   placeholder="seu@email.com" autofocus="autofocus" required
							   value="<c:out value='${email}' />">
					</div>
					
					<div class="form-group">
						<label for="password">Senha</label>
						<input type="password" class="form-control" id="password" name="password" 
							   placeholder="Sua senha" required>
					</div>
					
					<button type="submit" class="btn btn-primary btn-block">
						<span class="glyphicon glyphicon-log-in"></span>&nbsp;Entrar
					</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
