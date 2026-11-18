<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Lote de Café</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty lot ? 'Atualizar' : 'Adicionar'} Lote</h3>
		
		<form action="${pageContext.request.contextPath}/lot/${action}" method="POST">
			<input type="hidden" value="${lot.id}" name="lotId">
			
			<div class="row">
				<!-- input text -->
				<div class="form-group col-md-6">
					<label for="name">Nome do lote</label>
					<input type="text" class="form-control" id="name" name="name" 
						   autofocus="autofocus" placeholder="Ex.: Talhão da Nascente" maxlength="100"
						   required 
						   oninvalid="this.setCustomValidity('Por favor, informe o nome do lote.')"
						   oninput="setCustomValidity('')"
						   value="<c:out value='${lot.name}' />" />
				</div>
				
				<!-- select -->
				<div class="form-group col-md-6">
					<label for="variety">Variedade</label>
					<select id="variety" class="form-control" name="variety" required
							oninvalid="this.setCustomValidity('Por favor, selecione a variedade.')"
							oninput="setCustomValidity('')">
						<option value="" disabled ${not empty lot ? '' : 'selected'}>Selecione a variedade</option>
						<c:forTokens var="v" items="Catuaí,Mundo Novo,Bourbon,Arara,Acaiá,Topázio" delims=",">
							<option value="${v}" ${lot.variety == v ? 'selected' : ''}>${v}</option>
						</c:forTokens>
					</select>
				</div>
			</div>
			
			<div class="row">
				<!-- input number -->
				<div class="form-group col-md-3">
					<label for="area">Área (hectares)</label>
					<input type="number" class="form-control" id="area" name="area" 
						   min="0.01" step="0.01" placeholder="Ex.: 4.50" required
						   oninvalid="this.setCustomValidity('Por favor, informe uma área maior que zero.')"
						   oninput="setCustomValidity('')"
						   value="${lot.area > 0 ? lot.area : ''}" />
				</div>
				
				<!-- input date -->
				<div class="form-group col-md-3">
					<label for="plantingDate">Data de plantio</label>
					<input type="date" class="form-control" id="plantingDate" name="plantingDate" required
						   oninvalid="this.setCustomValidity('Por favor, informe a data de plantio.')"
						   oninput="setCustomValidity('')"
						   value="${lot.plantingDate}" />
				</div>
				
				<!-- select com chave estrangeira (users) -->
				<div class="form-group col-md-4">
					<label for="user">Proprietário</label>
					<select id="user" class="form-control" name="user" required
							oninvalid="this.setCustomValidity('Por favor, informe o proprietário.')"
							oninput="setCustomValidity('')">
						<option value="" disabled ${not empty lot ? '' : 'selected'}>Selecione um usuário</option>
						<c:forEach var="user" items="${users}">
							<option value="${user.id}" ${lot.user.id == user.id ? 'selected' : ''}>
								<c:out value="${user.name}" />
							</option>
						</c:forEach>
					</select>
				</div>
				
				<!-- checkbox -->
				<div class="form-group col-md-2">
					<label>Irrigação</label>
					<div class="checkbox">
						<label for="irrigated">
							<input type="checkbox" id="irrigated" name="irrigated" ${lot.irrigated ? 'checked' : ''} />
							Lote irrigado
						</label>
					</div>
				</div>
			</div>
			
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/lots" class="btn btn-default">Cancelar</a>
					<button type="submit" class="btn btn-primary">${not empty lot ? 'Atualizar' : 'Cadastrar'} Lote</button>
				</div>
			</div>
		</form>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
