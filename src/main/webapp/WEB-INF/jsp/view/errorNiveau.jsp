<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<!doctype html>
<html lang="en">
<head>
<title>Error page</title>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<link
	href="https://fonts.googleapis.com/css?family=Lato:300,400,700&display=swap"
	rel="stylesheet">

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/style.css">

</head>

<body>
<body>



	<section class="ftco-section">
		<div class="container">
			<div class="row justify-content-center"></div>
			<div class="row justify-content-center">
				<div class="col-md-7 col-lg-5">
					







						<div class="alert alert-danger col-12">Ces Ids de Niveau n'exist pas dans la base de données !!</div>
						<table class="table">
							<thead>
								<tr>
									<th scope="col">ID ETUDIANT</th>
									<th scope="col">CNE</th>
									<th scope="col">NOM</th>
									<th scope="col">PRENOM</th>
									<th scope="col">ID NIVEAU ACTUEL</th>
									<th scope="col">Type</th>

								</tr>
							</thead>

							<c:forEach items="${inscriptionsList}" var="p">
								<tr>

									<td><c:out value="${p.idEtudiant}" /></td>
									<td><c:out value="${p.cne}" /></td>
									<td><c:out value="${p.nom}" /></td>
									<td><c:out value="${p.prenom} " /></td>
									<td><c:out value="${p.idNiveau} " /></td>
									<td><c:out value="${p.type} " /></td>


								</tr>

							</c:forEach>

						</table>







					
				</div>
			</div>
		</div>
	</section>

	<script
		src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/popper.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>


</body>

</body>
</html>