<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Lotes de Café</title>
</head>
<body>
	<%@include file="modal.html"%>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<div id="alert" style="${not empty message ? 'display: block;' : 'display: none;'}" class="alert alert-dismissable ${alertType eq 1 ? 'alert-success' : 'alert-danger'}">
		  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		  <c:out value="${message}" />
		</div>
		
		<div id="top" class="row">
			<div class="col-md-9">
				<h3>Lotes de Café</h3>
			</div>
			
			<div class="col-md-3">
				<a href="${pageContext.request.contextPath}/lot/form" class="btn btn-danger pull-right h2"><span class="glyphicon glyphicon-plus"></span>&nbsp;Adicionar Lote</a>
			</div>
		</div>
		
		<hr />
		
		<div id="list" class="row">
			<div class="table-responsive col-md-12">
				<table class="table table-striped table-hover" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Variedade</th>
							<th>Área (ha)</th>
							<th>Plantio</th>
							<th>Irrigado</th>
							<th>Proprietário</th>
							<th>Editar</th>
							<th>Excluir</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="lot" items="${lots}">
							<tr>
								<td><c:out value="${lot.name}" /></td>
								<td><c:out value="${lot.variety}" /></td>
								<td><fmt:formatNumber value="${lot.area}" minFractionDigits="2" maxFractionDigits="2" /></td>
								<td><fmt:formatDate value="${lot.plantingDate}" pattern="dd/MM/yyyy" /></td>
								<td>${lot.irrigated ? 'Sim' : 'Não'}</td>
								<td><c:out value="${lot.user.name}" /></td>
								
								<td class="actions">
									<a class="btn btn-info btn-xs" 
									   href="${pageContext.request.contextPath}/lot/update?lotId=${lot.id}">
										<span class="glyphicon glyphicon-edit"></span>
									</a>
								</td>
								
								<td class="actions">
									<a class="btn btn-danger btn-xs modal-remove"
									   lot-id="${lot.id}" 
									   lot-name="<c:out value='${lot.name}' />" data-toggle="modal" 
									   data-target="#delete-modal" href="#"><span 
									   class="glyphicon glyphicon-trash"></span></a>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty lots}">
							<tr><td colspan="8" class="text-center text-muted">Nenhum lote cadastrado.</td></tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
		
		<div id="bottom" class="row">
			<div class="col-md-12">
				<%@include file="pagination.jsp"%>
			</div>
		</div>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// fecha o alert após 3 segundos
			setTimeout(function() {
				$("#alert").slideUp(500);
			}, 3000);
			
			// ao clicar no delete de algum lote, envia nome, id e ação para o modal
			$(".modal-remove").click(function () {
				var lotName = $(this).attr('lot-name');
				var lotId = $(this).attr('lot-id');
				$(".modal-body #hiddenValue").text("o lote '" + lotName + "'");
				$("#id").attr("value", lotId);
				$("#form").attr("action", "${pageContext.request.contextPath}/lot/delete");
			})
		});
	</script>
</body>
</html>
