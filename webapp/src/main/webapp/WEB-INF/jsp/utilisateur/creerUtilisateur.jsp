<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<%@ include file="../theme/head.jsp" %>
<body class="container">
<%@ include file="../theme/header.jsp" %>
<%@ include file="../theme/menu.jsp" %>
	<section class="row  justify-content-center">	
		<div class="col-12">
			<p class="h3">Création d'un utilisateur</p>
		</div>
		<div class="col-12 col-md-6">
			<form:form action="/enregistrerUtilisateur" method="post" class="form-row form-inline" 
				modelAttribute="utilisateur">			
				<form:label path="email" class="col-12 col-sm-5">Email</form:label> 
				<form:input  path="email" class="form-control col-12 col-sm-7" />
				<form:errors path="email" cssClass="error text-danger col-12"/>
				<form:label path="password" class="col-12 col-sm-5">Mot de passe</form:label>
				<form:password path="password" class="form-control col-12 col-sm-4" />
				<form:errors path="password" cssClass="error text-danger col-12"/>	
				<form:label path="nom" class="col-12 col-sm-5">Nom</form:label>
				<form:input path="nom" class="form-control col-12 col-sm-7" />	
				<form:label path="prenom" class="col-12 col-sm-5">Prénom</form:label>
				<form:input path="prenom" class="form-control col-12 col-sm-7" />		
				<input class="btn btn-primary col-6 offset-3" type="submit" value="Enregistrer">
			</form:form>
		</div>
	</section>
<%@ include file="../theme/footer.jsp" %>
</body>
</html>