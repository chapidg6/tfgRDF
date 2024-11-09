<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="rdf.Servicio" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
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
}

.title-home{
    margin-top: 3%;
}




.formulario {
    display: inline-block;
    width: 40%;
    background-color: #959595;
    margin-top: 2%;
    margin-bottom: 0%;
    margin-left: 30%;
    margin-right: 5%;
    border: none;
	  border-radius: 5px;
	  background-color: #80abfd;
    text-align: center;
    padding: 25px 10px;

  }



  /*#formulario_izquierda {
    float: left;
  }


  #formulario_derecha {
    float: right;
  }*/

.boton-formulario{
  cursor: pointer;
  margin-bottom: 0;
  margin-top: 2%;
  border: none;
	border-radius: 5px;
  padding: 10px 15px;
  transition: background-color 0.3s ease;	

  }

.boton-formulario:hover{
 background-color:#aba8a8;
 }

.boton-mostrar{
  margin-top: 1%;
  margin-left: 45%;
  cursor: pointer;
  border: none;
	border-radius: 5px;
  padding: 15px 15px;
  background-color: #d7d5d5;
  transition: background-color 0.3s ease;	
}

.boton-mostrar:hover{
 background-color:#aba8a8;
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
<h1>Esta es la pagina prueba</h1>
<h3>Esta es la variable tipo de dato: <%=request.getSession().getAttribute("nameSession") %></h3>
<h3>Esta es la variable seria: ${name1}</h3>
<h3>Esta es la variable seria: ${name2}</h3>
</div>

<footer>
    <p>&copy; 2024 Copyright: Álvaro Chapinal</p>
</footer>

</body>
</html>