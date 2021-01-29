<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<nav class="navbar navbar-expand-lg navbar-light bg-light row">
	<a class="navbar-brand" href="/">Bibliothèques de Bouquinville</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#menu">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="menu">
		<sec:authorize access="!isAuthenticated()">
			<a class="nav-link" href="/inscription">S'inscrire</a>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<a class="nav-link" href="/abonne/listeOuvrages">Liste des ouvrages</a>		
			<a class="nav-link" href="/abonne/listePrets">Vos prêts en cours</a>
			<a class="nav-link" href="/abonne/listeReservations">Vos réservations en cours</a>
			<a class="nav-link ml-auto" href="/logout">Se déconnecter</a>
		</sec:authorize>
	</div>
</nav>