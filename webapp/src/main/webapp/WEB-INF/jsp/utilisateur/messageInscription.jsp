<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<%@ include file="../theme/head.jsp" %>
<body class="container">
<%@ include file="../theme/header.jsp" %>
	<section class="row">
		<div class="col-12">
			<p class="h3">Félicitation !</p>
		</div>	
		<div class="col-12">
			<p>Votre inscription est validée</p>
			<p>Votre identifiant est ${login}</p>
		</div>	
		<div class="col-12">
			<a class="btn btn-primary" href="/login">Se connecter</a>
		</div>		
	</section>
</body>
</html>