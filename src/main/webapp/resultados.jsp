<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.jena.rdf.model.ModelFactory"%>
<%@ page import="org.apache.jena.rdf.model.Model"%>
<%@ page import="org.apache.jena.rdf.model.RDFNode"%>
<%@ page import="org.apache.jena.query.*"%>
<%@ page import="org.apache.jena.riot.RiotException"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.StringReader"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>TRANSFORMADOR DE CONJUNTOS DE DATOS EN RECURSOS RDF</title>
<link rel="icon" type="image/png" href="img/logo-uc3m.png">

<style>
html, body {
	width: 100%;
	height: 100%;
	background-color: #dbffff;
	margin: 0;
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

.title-page {
	margin-top: 3%;
}

.container {
	padding: 10px;
	height: 100%;
	text-align: center;
}

.logo-casa-container {
	position: relative;
}

.logo-casa {
	position: absolute;
	left: 93%;
}

.title-result {
	margin-top: 3%;
}

.result {
	color: #333;
	border: 1px solid #ccc;
	border-radius: 5px;
	background-color: #f9f9f9;
	max-width: 500px;
	margin-left: 29%;
	margin-top: 5px;
	padding: 5px;
}

.button-transformar {
	margin: -10px;
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
	padding: 0, 8% 0;
	position: fixed;
	bottom: 0;
	left: 0;
}
</style>
</head>
<body>

	<div class="topnav">
		<a href="index.jsp"> <img class="logo-uc3m"
			src="img/logo-uc3m.png" width="70" height="65" alt="logo uc3m"></a>
		<h2 class="title-page">TRANSFORMADOR DE CONJUNTOS DE DATOS EN
			RECURSOS RDF</h2>

	</div>

	<div class="container">

		<div class="logo-casa-container">
			<a href="home.jsp"><img class="logo-casa" src="img/logo-casa.png"
				width="50" height="45" alt="Logo Casa"></a>
		</div>

		<h2 class="title-result">DATOS RDFICADOS</h2>

		<%
    // Ruta al archivo RDF
   	
	//String serializacion = request.getParameter("serializacion");
	//String nombreArchivo = request.getParameter("nombreArchivo");
	
	/*
	String ruta = request.getParameter("ruta");
	  String directorio = null;
      String os = System.getProperty("os.name").toLowerCase();
      // Sistema operativo Windows
       if (os.contains("win")) {
           directorio = System.getProperty("user.home") + "\\OneDrive\\"+ ruta + "\\";
      // Sitema operativo macOS
       } else if (os.contains("mac")) {
           directorio = System.getProperty("user.home") + "/"+ ruta;
      // Sistema operativo Linux
       } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
           directorio = System.getProperty("user.home") + "/"+ ruta;
       } else {
           // Sistema operativo desconocido
           System.out.println("No se puede determinar el directorio para este sistema operativo.");
           
       }
			

	// Nombre del archivo en el que se sobreescribe y se usa para mostrar en la web
	// solo los datos que se acaban de transformar
	// y ruta completa de este
	String nombreDocumentoRDFTemp = nombreArchivo + "Temp.txt";
	String rutaCompletaTemp = directorio + nombreDocumentoRDFTemp;
	*/
	
	HttpSession sessionEntrada = request.getSession();
	String rdfContent = (String) sessionEntrada.getAttribute("rdfContent");
	String serializacion = (String) sessionEntrada.getAttribute("serializacion");

	if (serializacion.equalsIgnoreCase("Turtle")) {
	    try {
	        // Crear un modelo RDF
	        Model model = ModelFactory.createDefaultModel();
	        // Leer el contenido RDF
	        StringReader stringReader = new StringReader(rdfContent);
	        model.read(stringReader, null, "TURTLE");

	        // Definir y ejecutar la consulta SPARQL
	        String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
	        Query query = QueryFactory.create(queryString);
	        QueryExecution qexec = QueryExecutionFactory.create(query, model);
	        ResultSet results = qexec.execSelect();

	        // Iterar sobre los resultados de la consulta
	        while (results.hasNext()) {
	            QuerySolution soln = results.nextSolution();
	            RDFNode s = soln.get("s");
	            RDFNode p = soln.get("p");
	            RDFNode o = soln.get("o");
	            %>
	            <div class="result">
	                Sujeto: <%= s %><br>
	                Predicado: <%= p %><br>
	                Objeto: <%= o %><br>
	            </div>
	            <%
	        }
	        // Cerrar recursos
	        qexec.close();
	        stringReader.close();
	    } catch (RiotException e) {
	        // Manejo de errores específicos de Jena
	        request.setAttribute("errorResultados", "Error al procesar el RDF en formato Turtle.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    } catch (Exception e) {
	        // Manejo de errores generales
	        request.setAttribute("errorResultados", "Error general al procesar el RDF.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    }
	} else if (serializacion.equalsIgnoreCase("RDF/XML")) {
	    try {
	        // Crear un modelo RDF
	        Model model = ModelFactory.createDefaultModel();
	        // Leer el contenido RDF
	        StringReader stringReader = new StringReader(rdfContent);
	        model.read(stringReader, null, "RDF/XML");

	        // Definir y ejecutar la consulta SPARQL
	        String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
	        Query query = QueryFactory.create(queryString);
	        QueryExecution qexec = QueryExecutionFactory.create(query, model);
	        ResultSet results = qexec.execSelect();

	        // Iterar sobre los resultados de la consulta
	        while (results.hasNext()) {
	            QuerySolution soln = results.nextSolution();
	            RDFNode s = soln.get("s");
	            RDFNode p = soln.get("p");
	            RDFNode o = soln.get("o");
	            %>
	            <div class="result">
	                Sujeto: <%= s %><br>
	                Predicado: <%= p %><br>
	                Objeto: <%= o %><br>
	            </div>
	            <%
	        }
	        // Cerrar recursos
	        qexec.close();
	        stringReader.close();
	    } catch (RiotException e) {
	        // Manejo de errores específicos de Jena
	        request.setAttribute("errorResultados", "Error al procesar el RDF en formato RDF/XML.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    } catch (Exception e) {
	        // Manejo de errores generales
	        request.setAttribute("errorResultados", "Error general al procesar el RDF.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    }
	} else if (serializacion.equalsIgnoreCase("JSON-LD")) {
	    try {
	        // Crear un modelo RDF
	        Model model = ModelFactory.createDefaultModel();
	        // Leer el contenido RDF
	        StringReader stringReader = new StringReader(rdfContent);
	        model.read(stringReader, null, "JSON-LD");

	        // Definir y ejecutar la consulta SPARQL
	        String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
	        Query query = QueryFactory.create(queryString);
	        QueryExecution qexec = QueryExecutionFactory.create(query, model);
	        ResultSet results = qexec.execSelect();

	        // Iterar sobre los resultados de la consulta
	        while (results.hasNext()) {
	            QuerySolution soln = results.nextSolution();
	            RDFNode s = soln.get("s");
	            RDFNode p = soln.get("p");
	            RDFNode o = soln.get("o");
	            %>
	            <div class="result">
	                Sujeto: <%= s %><br>
	                Predicado: <%= p %><br>
	                Objeto: <%= o %><br> 
	            </div>
	            <%
	        }
	        // Cerrar recursos
	        qexec.close();
	        stringReader.close();
	    } catch (RiotException e) {
	        // Manejo de errores específicos de Jena
	        request.setAttribute("errorResultados", "Error al procesar el RDF en formato JSON-LD.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    } catch (Exception e) {
	        // Manejo de errores generales
	        request.setAttribute("errorResultados", "Error general al procesar el RDF.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    }
	} else if (serializacion.equalsIgnoreCase("N-Triples")) {
	    try {
	        // Crear un modelo RDF
	        Model model = ModelFactory.createDefaultModel();
	        // Leer el contenido RDF
	        StringReader stringReader = new StringReader(rdfContent);
	        model.read(stringReader, null, "N-Triples");

	        // Definir y ejecutar la consulta SPARQL
	        String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
	        Query query = QueryFactory.create(queryString);
	        QueryExecution qexec = QueryExecutionFactory.create(query, model);
	        ResultSet results = qexec.execSelect();

	        // Iterar sobre los resultados de la consulta
	        while (results.hasNext()) {
	            QuerySolution soln = results.nextSolution();
	            RDFNode s = soln.get("s");
	            RDFNode p = soln.get("p");
	            RDFNode o = soln.get("o");
	            %>
	            <div class="result">
	                Sujeto: <%= s %><br>
	                Predicado: <%= p %><br>
	                Objeto: <%= o %><br>
	            </div>
	            <%
	        }
	        // Cerrar recursos
	        qexec.close();
	        stringReader.close();
	    } catch (RiotException e) {
	        // Manejo de errores específicos de Jena
	        request.setAttribute("errorResultados", "Error al procesar el RDF en formato N-Triples.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    } catch (Exception e) {
	        // Manejo de errores generales
	        request.setAttribute("errorResultados", "Error general al procesar el RDF.");
	        request.getRequestDispatcher("error.jsp").forward(request, response);
	    }
	}

	// Eliminar atributos de sesión
	sessionEntrada.removeAttribute("rdfContent");
	%>
		 <br> <br> <br> <br> <br>
		 
	<footer>
	    <p>&copy; 2024 Copyright: Álvaro Chapinal</p>
	</footer>


	<script>

    </script>
</body>
</html>