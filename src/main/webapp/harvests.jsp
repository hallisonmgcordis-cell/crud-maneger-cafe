<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Colheitas</title>
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
				<h3>Colheitas</h3>
			</div>
			
			<div class="col-md-3">
				<a href="${pageContext.request.contextPath}/harvest/form" class="btn btn-danger pull-right h2"><span class="glyphicon glyphicon-plus"></span>&nbsp;Adicionar Colheita</a>
			</div>
		</div>
		
		<hr />
		
		<div id="list" class="row">
			<div class="table-responsive col-md-12">
				<table class="table table-striped table-hover" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<th>Data</th>
							<th>Lote</th>
							<th>Sacas</th>
							<th>Qualidade</th>
							<th>Observações</th>
							<th>Responsável</th>
							<th>Editar</th>
							<th>Excluir</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="harvest" items="${harvests}">
							<tr>
								<td><fmt:formatDate value="${harvest.harvestDate}" pattern="dd/MM/yyyy" /></td>
								<td><c:out value="${harvest.coffeeLot.name}" /></td>
								<td>${harvest.bags}</td>
								<td>${harvest.quality == 'ESPECIAL' ? 'Especial' : (harvest.quality == 'SUPERIOR' ? 'Superior' : 'Tradicional')}</td>
								<td><c:out value="${harvest.notes}" /></td>
								<td><c:out value="${harvest.user.name}" /></td>
								
								<td class="actions">
									<a class="btn btn-info btn-xs" 
									   href="${pageContext.request.contextPath}/harvest/update?harvestId=${harvest.id}">
										<span class="glyphicon glyphicon-edit"></span>
									</a>
								</td>
								
								<td class="actions">
									<a class="btn btn-danger btn-xs modal-remove"
									   harvest-id="${harvest.id}" 
									   harvest-desc="<c:out value='${harvest.coffeeLot.name}' />" data-toggle="modal" 
									   data-target="#delete-modal" href="#"><span 
									   class="glyphicon glyphicon-trash"></span></a>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty harvests}">
							<tr><td colspan="8" class="text-center text-muted">Nenhuma colheita cadastrada.</td></tr>
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
			
			// ao clicar no delete de alguma colheita, envia descrição, id e ação para o modal
			$(".modal-remove").click(function () {
				var desc = $(this).attr('harvest-desc');
				var harvestId = $(this).attr('harvest-id');
				$(".modal-body #hiddenValue").text("a colheita do lote '" + desc + "'");
				$("#id").attr("value", harvestId);
				$("#form").attr("action", "${pageContext.request.contextPath}/harvest/delete");
			})
		});
	</script>
</body>
</html>
