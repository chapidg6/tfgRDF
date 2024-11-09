<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>TRANSFORMADOR DE CONJUNTOS DE DATOS EN RECURSOS RDF</title>

<link rel="icon" type="image/png" href="img/logo-uc3m.png"> 
<style>



html, body {
  width:100%;
	height:100%;
	background-color: #dbffff;
	margin:0;

}


.topnav {
  overflow: hidden;
  background-color: #80abfd;
  text-align: center;

}


.topnav a {
  float: left;
  display: block;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  transition: background-color 0.3s ease;	

 
}


.topnav a:hover {
  background-color: #3366ff;
}


.container {
    
  padding: 10px;
  height: 100%;
  text-align: center;
}

.title-home{
  margin-top: 3%;
}


.logo-casa-container{
	position: relative;
}

.logo-casa {
	position: absolute;
	left: 93%;
}

.title-error {
	
	margin-top: 3%;
}


.error-box {
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 20px;
            background-color: #f9f9f9; /* Color de fondo rojo claro para indicar un error */
            color: #721c24; 
            margin: 30px auto;
            max-width: 500px;
        }
        
.button-transformar {
	padding: 10px;
	font-size: 16px;
	border: none;
	border-radius: 5px;
	background-color: #4e7eff;
	color: white;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.button-transformar:hover {
	background-color: #3366ff;
}


 footer {
  background-color: #80abfd;
  color: white;
	text-align: center;
	width: 100%;
	padding: 0,8% 0 ;
	position: fixed;
	bottom: 0;
	left: 0;
}


</style>
</head>
<body>

<div class="topnav">
  <a href="index.jsp">  <img class="logo-uc3m" src="img/logo-uc3m.png" width="70" height="65" alt="logo uc3m"></a>
  <h2 class="title-home">TRANSFORMADOR DE CONJUNTOS DE DATOS EN RECURSOS RDF</h2>
 
</div>

<div class="container">
 		<div class="logo-casa-container">
		<a href="home.jsp" ><img class="logo-casa" src="img/logo-casa.png"
			width="50" height="45" alt="Logo Casa"></a>
		</div>
	<h2 class="title-error">ERROR</h2>
 		<%
    // Ruta del archivo de entrada
   	
			HttpSession sessionError = request.getSession();
		    String ruta = (String) sessionError.getAttribute("ruta");
 		%>
 
        <div class="error-box">
       Error de E/S: <%= ruta %> (Archivo o ruta incorrecta) 
   </div>
	          <button class="button-transformar" onclick="manejarError()">Volver</button>
	<br>
	<br>
	<br>
	<br>
	<br>
	
	  
</div>

<footer>
    <p>&copy; 2024 Copyright: Álvaro Chapinal</p>
</footer>

<script>
	
	
function manejarError() {
    window.location.href = "home.jsp";
}
	
</script>
</body>
</html>