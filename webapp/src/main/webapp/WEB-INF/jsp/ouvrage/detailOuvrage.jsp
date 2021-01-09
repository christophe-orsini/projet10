<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<%@ include file="../theme/head.jsp" %>
<body class="container">
<%@ include file="../theme/header.jsp" %>
<%@ include file="../theme/menu.jsp" %>
	<section class="row justify-content-center">
		<div class="col-12">
			<p class="h3">Détail d'ouvrage N° <c:out value="${ouvrage.id}" />
		</div>
		<div class="col-12">
			<div class="row col-12">
				<p>Titre : ${ouvrage.titre} ${ouvrage.anneeEdition}</p>
			</div>
			<div class="row col-12">
				<p>Résumé : ${ouvrage.resume}</p> 
			</div>
			<div class="row col-12">
				<p class="col-md-4 p-0">Auteur : ${ouvrage.auteur}<p>
				<p class="col-7 col-md-4 p-0">Exemplaires disponibles : ${ouvrage.nbreExemplaire}<p>				
			</div>			
		</div>
		<div class="col-12">
			<a class="btn btn-primary" href="/abonne/listeOuvrages" role="button">Retour</a>
		</div>
	</section>
<%@ include file="../theme/footer.jsp" %>
</body>
</html>