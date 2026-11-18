<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%-- 
	Paginação reutilizável. Os controllers informam via request:
	currentPage, totalPages e pageUrl (ex.: "/users") - veja ControllerUtil.paginate
--%>
<c:set var="firstPage" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
<c:set var="lastPage" value="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" />
<c:set var="pageBase" value="${pageContext.request.contextPath}${pageUrl}?page=" />

<ul class="pagination">
	<c:choose>
		<c:when test="${currentPage > 1}">
			<li><a href="${pageBase}${currentPage - 1}" rel="prev">&lt; Anterior</a></li>
		</c:when>
		<c:otherwise>
			<li class="disabled"><a>&lt; Anterior</a></li>
		</c:otherwise>
	</c:choose>
	
	<c:forEach begin="${firstPage}" end="${lastPage}" var="p">
		<li class="${p == currentPage ? 'active' : ''}"><a href="${pageBase}${p}">${p}</a></li>
	</c:forEach>
	
	<c:choose>
		<c:when test="${currentPage < totalPages}">
			<li class="next"><a href="${pageBase}${currentPage + 1}" rel="next">Próximo &gt;</a></li>
		</c:when>
		<c:otherwise>
			<li class="disabled"><a>Próximo &gt;</a></li>
		</c:otherwise>
	</c:choose>
</ul>
