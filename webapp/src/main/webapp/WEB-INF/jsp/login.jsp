<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<%@ include file="theme/head.jsp" %>
<body class="container">
<%@ include file="theme/header.jsp" %>
<%@ include file="theme/menu.jsp" %>
	<section class="row justify-content-center">
		<div class="col-12">
			<p class="h3">Connexion</p>
		</div>
		<div class="col-12 col-md-8">
			<c:url value="/login" var="loginProcessingUrl"/>
			<form action="${loginProcessingUrl}" method="post" class="form-row form-inline">
		        <c:if test="${param.error != null}">
		            <div class="col-12">
		                <p class="text-warning"><strong>Erreur de connexion</strong></p>
		            </div>
		        </c:if>
		        <c:if test="${param.logout != null}">
		            <div class="col-12">
		                <p><strong>Vous êtes déconnecté</strong></p>
		            </div>
		        </c:if>	        			
				<label for="email" class="col-5">Email</label> 
				<input type="text" class="form-control form-control-sm col-7 col-md-3" 
					name=username id="email" required="required" autofocus="autofocus" />										
				<label for="motpasse" class="col-5">Mot de passe</label>
				<input type="password" class="form-control form-control-sm col-7 col-md-3"
					name="password" id="password" required="required" />									
				<input class="btn btn-primary offset-4 mt-3" type="submit" value="Se connecter">						
			</form>
		</div>
	</section>
<%@ include file="theme/footer.jsp" %>
</body>
</html>