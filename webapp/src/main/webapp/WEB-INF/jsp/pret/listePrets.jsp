<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<%@ include file="../theme/head.jsp" %>
<body class="container">
<%@ include file="../theme/header.jsp" %>
<%@ include file="../theme/menu.jsp" %>
	<section class="row justify-content-center">	
		<div class="col-12">
			<p class="h3">Liste de vos prêts en cours</p>
		</div>
		<div class="col-12 table-responsive">
			<table class="table table-sm table-striped table-bordered">
				<thead>
					<tr>
						<th>Titre</th>
						<th>Emprunté le </th>
						<th>Date limite de retour</th>
						<th></th>
					</tr>
				</thead>	
				<tbody>
					<jsp:useBean id="now" class="java.util.Date"/>
					<c:forEach items="${prets}" var="pret" varStatus="status">
					<tr>
						<td>${pret.ouvrage.titre}</td>
						<td><fmt:formatDate type="DATE" pattern="dd/MM/yyyy" value="${pret.dateDebut}" /></td>
						<td><fmt:formatDate type="DATE" pattern="dd/MM/yyyy" value="${pret.dateFinPrevu}" /></td>
						<td>
							<c:if test="${pret.prolongationsPossible > 0 && pret.dateFinPrevu >= now}">
								<a class="btn btn-primary ml-2" href="/abonne/prolongerPret/${pret.id}" 
									role="button">Prolonger</a>
							</c:if>
							<c:if test="${pret.dateFinPrevu < now}">En retard</c:if>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>
<%@ include file="../theme/footer.jsp" %>
</body>
</html>