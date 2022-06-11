<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />

<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">

			<jsp:include page="../fragments/menu.jsp" />

		</div>
	</nav>






	<div>
		<h3>List of Students</h3>
	</div>

	<div>



		</div><p style="text-align:right"><a href="${pageContext.request.contextPath}/admin/exportInscriptions"><img
			src="${pageContext.request.contextPath}/resources/img/excel.png" width="30">Export Excel</p></a>
			
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

			<c:forEach items="${inscriptionList}" var="p">
				<tr>

					<td><c:out value="${p.idEtudiant}" /></td>
					<td><c:out value="${p.cne}" /></td>
					<td><c:out value="${p.nom}"  /></td>
					<td><c:out value="${p.prenom} " /></td>
			        <td><c:out value="${p.idNiveau} " /></td>
			        <td><c:out value="${p.type} " /></td>
	

				</tr>

			</c:forEach>

		</table>
	
	<jsp:include page="../fragments/adminfooter.jsp" />