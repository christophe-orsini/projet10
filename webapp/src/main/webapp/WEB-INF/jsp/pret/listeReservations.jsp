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
			<p class="h3">Liste de vos réservations en cours</p>
		</div>
		<div class="col-12 table-responsive">
			<table class="table table-sm table-striped table-bordered">
				<thead>
					<tr>
						<th>Titre</th>
						<th class="text-center">Réservé le</th>
						<th class="text-center">Informations</th>
						<th class="text-center">Disponibilité</th>
						<th></th>
					</tr>
				</thead>	
				<tbody>
					<jsp:useBean id="now" class="java.util.Date"/>
					<c:forEach items="${reservations}" var="reservation" varStatus="status">
					<tr>
						<td>${reservation.ouvrage.titre}</td>
						<td class="text-center"><fmt:formatDate type="DATE" pattern="dd/MM/yyyy HH:mm" value="${reservation.dateHeureReservation}" /></td>
						<c:if test="${reservation.dateHeureExpiration == null}">
							<td class="text-center">N° ${reservation.rang} dans le liste d'attente</td>
							<td class="text-center">Prochaine : <fmt:formatDate type="DATE" pattern="dd/MM/yyyy" value="${reservation.dateDisponible}" /></td>
						</c:if>
						<c:if test="${reservation.dateHeureExpiration != null}">
							<td class="text-center">Disponible</td>
							<td class="text-center">Jusqu'au : <fmt:formatDate type="DATE" pattern="dd/MM/yyyy HH:mm" value="${reservation.dateHeureExpiration}" /></td>
						</c:if>
						<td class="text-center">
							<c:if test="${reservation.dateHeureExpiration != null && reservation.dateHeureExpiration ge now}">
								<a class="btn btn-primary ml-2" href="/abonne/retirerReservation/${reservation.id}" role="button">Retirer</a>
							</c:if>
							<a class="btn btn-primary ml-2" href="/abonne/annulerReservation/${reservation.id}" role="button">Annuler</a>
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