<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<%@ include file="theme/head.jsp" %>
<body class="container">
<%@ include file="theme/header.jsp" %>
<%@ include file="theme/menu.jsp" %>
	<section class="row">
		<div class="col-12">
			<p class="h3">Accueil</p>
		</div>	
		<div class="col-12">	
			<h5>Bienvenue sur le site des amis de l'escalade.</h5>
			<p>Vous y touverez toutes les informations concernant les lieux d'escalades en France et à l'étranger<br />
			déposés par nos amis, et bien plus encore ...</p>
			<sec:authorize access="!isAuthenticated()">
				<h5>Vous n'avez pas encore de compte ?<br />
				N'attendez pas, <a href="/inscription">inscrivez-vous !</a></h5>
			</sec:authorize>
		</div>	
	</section>
<%@ include file="theme/footer.jsp" %>
</body>
</html>