<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<jsp:include page="../fragments/adminHeader.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>File Upload Page</title>
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



	<%-- <form action="${pageContext.request.contextPath}/admin/import"
		method="post" enctype="multipart/form-data">
		<p>Drag Excel file here or click in this area.</p>
		<input type="file" name="file" /> <input type="submit" value="Import" />
	</form> --%>

	<%-- <form action="action=" @{/import}" method="POST"
		enctype="multipart/form-data">
		<br /> <br /> Please select a file to upload : <input type="file"
			name="file" value="Browse File" id="file" /> <br /> <br /> Press
		here to upload the file : <input type="submit" value="upload" /> <br />
		<br />

		<h4 style="color: green">${message}</h4>
		<br /> Do you want to save excel data into database ? <a
			href="saveData"><b>Yes</b></a> &nbsp &nbsp <a href="/"><b>No</b></a>
	</form> --%>



	<section class="ftco-section">
		<div class="container">
			<div class="row justify-content-center">
				
			</div>
			<div class="row justify-content-center">
				<div class="col-md-7 col-lg-5">
					<div class="login-wrap p-4 p-md-5">

						<h3 class="text-center mb-4">Upload your Excel File</h3>
						<form action="${pageContext.request.contextPath}/admin/import" method="post" enctype="multipart/form-data">
							<!-- <input type="file" name="file" />  -->
							<!-- <input type="submit" value="Import" /> -->
							
							<div class="form-group">
								<input type="file" name="file" class="form-control rounded-left" >
							</div>


							<div class="form-group">
								<button type="submit"
									class="form-control btn btn-primary rounded submit px-3">Import</button>
							</div>

						</form>
						
						<div>
                                        <h3>${message}</h3>
                                    </div>
						
						<c:if test="${message_model.text!=null}">
							
								
									<div class="alert alert-danger col-12">${message_model.text}</div>
										
									
								
							</c:if>
						
						
										
									
					</div>
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