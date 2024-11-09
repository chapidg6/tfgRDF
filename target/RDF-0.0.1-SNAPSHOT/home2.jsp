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
}

.title-home{
  margin-top: 3%;
}

.title-formulario-base{
  text-align: center;
}


.formulario-basedatos{
  display: inline-block;
    width: 40%;
    background-color: #959595;
    margin-top: 0%;
    margin-bottom: 0%;
    margin-left: 30%;
    margin-right: 5%;
    border: none;
	border-radius: 5px;
	background-color:/* #4d88ff*/#6699ff ;
    text-align: center;
    padding: 25px 10px;
}

.boton-formulario-base{
  cursor: pointer;
  margin-bottom: 0;
  margin-top: 2%;
  border: none;
	border-radius: 5px;
  padding: 10px 25px;
  transition: background-color 0.3s ease;	

  }

.boton-formulario-base:hover{
 background-color:#aba8a8;
 }


.title-formulario-json{
  text-align: center;
  margin-top: 3%;
}


.formulario-json{
  display: inline-block;
    width: 40%;
    background-color: #959595;
    margin-top: 0%;
    margin-bottom: 0%;
    margin-left: 30%;
    margin-right: 5%;
    border: none;
	border-radius: 5px;
	background-color:/* #4d88ff*/#6699ff ;
    text-align: center;
    padding: 25px 10px;
}

.boton-formulario-json{
  cursor: pointer;
  margin-bottom: 0;
  margin-top: 2%;
  border: none;
	border-radius: 5px;
  padding: 10px 25px;
  transition: background-color 0.3s ease;	

  }

.boton-formulario-json:hover{
 background-color:#aba8a8;
 }




.title-formulario-csv{
  text-align: center;
   margin-top: 3%;
}


.formulario-csv{
  display: inline-block;
    width: 40%;
    background-color: #959595;
    margin-top: 0%;
    margin-bottom: 0%;
    margin-left: 30%;
    margin-right: 5%;
    border: none;
	border-radius: 5px;
	background-color:/* #4d88ff*/#6699ff ;
    text-align: center;
    padding: 25px 10px;
}

.boton-formulario-csv{
  cursor: pointer;
  margin-bottom: 0;
  margin-top: 2%;
  border: none;
	border-radius: 5px;
  padding: 10px 25px;
  transition: background-color 0.3s ease;	

  }

.boton-formulario-csv:hover{
 background-color:#aba8a8;
 }




  /*#formulario_izquierda {
    float: left;
  }


  #formulario_derecha {
    float: right;
  } */




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
 <h2 class="title-formulario-base">BASE DE DATOS</h2>
   <div id="formulario-basedatos" class="formulario-basedatos">

    <h3> Pulsa el botón para ir a la sección de base de datos</h3>
    
    <form id="formularioDataBase" action="DataBase" method="get" >    
    <button class="boton-formulario-base" type="submit">Ir</button>
    </form>
  </div>
  
  <h2 class="title-formulario-json">JSON</h2>
   <div id="formulario-json" class="formulario-json">

    <h3> Pulsa el botón para ir a la sección de JSON</h3>
    
    <form id="formularioJSON" action="Json" method="get" >    
    <button class="boton-formulario-json" type="submit">Ir</button>
    </form>
  </div>


	
	<h2 class="title-formulario-csv">CSV</h2>
   <div id="formulario-csv" class="formulario-csv">

    <h3> Pulsa el botón para ir a la sección de CSV</h3>
    
    <form id="formularioCSV" action="Csv" method="get" >    
    <button class="boton-formulario-csv" type="submit">Ir</button>
    </form>
  </div>
	<br>
	<br>
	<br>
	<br>
	<br>
	
	  
</div>

<footer>
    <p>&copy; 2024 Copyright: Álvaro Chapinal</p>
</footer>


</body>
</html>