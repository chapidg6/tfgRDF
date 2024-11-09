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

.title-formulario{
  text-align: center;
}

.formulario {
    display: inline-block;
    width: 40%;
    background-color: #959595;
    margin-top: 0%;
    margin-bottom: 0%;
    margin-left: 30%;
    margin-right: 5%;
    border: none;
	border-radius: 5px;
	background-color:#6699ff ;
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
 <h2 class="title-formulario-base">BASE DE DATOS</h2>
   <div id="formulario-basedatos" class="formulario-basedatos">

    <h3> Pulsa el botón para ir a la sección de base de datos</h3>
    
    <form id="formulario3" action="DataBase" method="get" >    
    <button class="boton-formulario-base" type="submit">Ir</button>
    </form>
  </div>
  
  <h2 class="title-formulario">JSON Y CSV</h2>
  
  <div id="formulario" class="formulario">
    <h3>Selecciona el tipo de documento que quiere transformar:</h3>
        
      <form id="formulario1" action="Home" method="post">
      <label for="tipo-dato">Opciones:</label>
      <select id="tipo-dato" name="tipo-dato">
        <option value="CSV">CSV</option>
        <option value="JSON">JSON</option>
      </select>
      <br><br>
      
      <h3>Selecciona el formato de serializacion en el que lo quieres mostrar:</h3>

      <label for="serializacion">Opciones:</label>
      <select id="serializacion" name="serializacion">
          <option value="Turtle">Turtle</option>
          <option value="RDF/XML">RDF/XML</option>
          <option value="JSON-LD">JSON-LD</option>
          <option value="N-Triples">N-Triples</option>
      </select>
      <br><br>
      <button class="boton-formulario" type="submit">Enviar</button>
      </form>
  </div>



  <button id="boton-mostrar"  class="boton-mostrar" disabled>Mostrar resultados</button>  

</div>

<footer>
    <p>&copy; 2024 Copyright: Álvaro Chapinal</p>
</footer>

<script>
  document.getElementById('formulario').addEventListener('submit', function() {
  // Habilitar el botón después de enviar el formulario
  document.getElementById('boton-mostrar').disabled = false;
});
  
  function irPaginaDATABASE() {
      
      window.location.href = 'database.jsp';
  }

  </script>
</body>
</html>