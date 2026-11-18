<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Colheita</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty harvest ? 'Atualizar' : 'Adicionar'} Colheita</h3>
		
		<form action="${pageContext.request.contextPath}/harvest/${action}" method="POST">
			<input type="hidden" value="${harvest.id}" name="harvestId">
			
			<div class="row">
				<!-- select com chave estrangeira (coffee_lots) -->
				<div class="form-group col-md-4">
					<label for="lot">Lote</label>
					<select id="lot" class="form-control" name="lot" required autofocus="autofocus"
							oninvalid="this.setCustomValidity('Por favor, selecione o lote.')"
							oninput="setCustomValidity('')">
						<option value="" disabled ${not empty harvest ? '' : 'selected'}>Selecione um lote</option>
						<c:forEach var="lot" items="${lots}">
							<option value="${lot.id}" ${harvest.coffeeLot.id == lot.id ? 'selected' : ''}>
								<c:out value="${lot.name}" /> (<c:out value="${lot.variety}" />)
							</option>
						</c:forEach>
					</select>
				</div>
				
				<!-- input date -->
				<div class="form-group col-md-4">
					<label for="harvestDate">Data da colheita</label>
					<input type="date" class="form-control" id="harvestDate" name="harvestDate" required
						   oninvalid="this.setCustomValidity('Por favor, informe a data da colheita.')"
						   oninput="setCustomValidity('')"
						   value="${harvest.harvestDate}" />
				</div>
				
				<!-- input number -->
				<div class="form-group col-md-4">
					<label for="bags">Sacas (60 kg)</label>
					<input type="number" class="form-control" id="bags" name="bags" 
						   min="1" step="1" placeholder="Ex.: 120" required
						   oninvalid="this.setCustomValidity('Por favor, informe a quantidade de sacas.')"
						   oninput="setCustomValidity('')"
						   value="${harvest.bags > 0 ? harvest.bags : ''}" />
				</div>
			</div>
			
			<div class="row">
				<!-- radio -->
				<div class="form-group col-md-4">
					<label>Qualidade</label>
					<div>
						<label class="radio-inline">
							<input type="radio" name="quality" value="ESPECIAL" required
								   ${harvest.quality == 'ESPECIAL' ? 'checked' : ''} /> Especial
						</label>
						<label class="radio-inline">
							<input type="radio" name="quality" value="SUPERIOR"
								   ${harvest.quality == 'SUPERIOR' ? 'checked' : ''} /> Superior
						</label>
						<label class="radio-inline">
							<input type="radio" name="quality" value="TRADICIONAL"
								   ${harvest.quality == 'TRADICIONAL' ? 'checked' : ''} /> Tradicional
						</label>
					</div>
				</div>
				
				<!-- select com chave estrangeira (users) -->
				<div class="form-group col-md-4">
					<label for="user">Responsável</label>
					<select id="user" class="form-control" name="user" required
							oninvalid="this.setCustomValidity('Por favor, informe o responsável.')"
							oninput="setCustomValidity('')">
						<option value="" disabled ${not empty harvest ? '' : 'selected'}>Selecione um usuário</option>
						<c:forEach var="user" items="${users}">
							<option value="${user.id}" ${harvest.user.id == user.id ? 'selected' : ''}>
								<c:out value="${user.name}" />
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="row">
				<!-- textarea -->
				<div class="form-group col-md-12">
					<label for="notes">Observações</label>
					<textarea class="form-control" id="notes" name="notes" rows="3" 
							  placeholder="Secagem, clima, detalhes da colheita..."><c:out value="${harvest.notes}" /></textarea>
				</div>
			</div>
			
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/harvests" class="btn btn-default">Cancelar</a>
					<button type="submit" class="btn btn-primary">${not empty harvest ? 'Atualizar' : 'Cadastrar'} Colheita</button>
				</div>
			</div>
		</form>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
