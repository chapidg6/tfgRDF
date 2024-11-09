<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
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

.container {
	padding: 10px;
	height: 100%;
	text-align: center;
}

.title-home {
	margin-top: 3%;
}

.formulario-basedatos {
	display: inline-block;
	width: 40%;
	background-color: #959595;
	margin-top: 0%;
	margin-bottom: 0%;
	border: none;
	border-radius: 5px;
	background-color: /* #4d88ff*/ #6699ff;
	padding: 25px 10px;
}

.boton-formulario-base {
	cursor: pointer;
	margin-bottom: 0;
	margin-top: 2%;
	border: none;
	border-radius: 5px;
	padding: 10px 25px;
	transition: background-color 0.3s ease;
}

.boton-formulario-base:hover {
	background-color: #aba8a8;
}

.title-formulario-json {
	margin-top: 3%;
}

.formulario-json {
	width: 40%;
	border: none;
	border-radius: 5px;
	background-color: #6699ff;
	padding: 25px 10px;
	margin: auto; /* Centra el formulario horizontalmente */
}

.formulario-json input, .formulario-json button {
	display: block;
	margin-left: 36%;
}

.fileInputJson {
	cursor: pointer;
	border: none;
	border-radius: 5px;
	transition: background-color 0.3s ease;
	margin-bottom: 20px;
}

.boton-formulario-json {
	cursor: pointer;
	margin-bottom: 0;
	margin-top: 2%;
	border: none;
	border-radius: 5px;
	padding: 10px 25px;
	transition: background-color 0.3s ease;
}

.boton-formulario-json:hover {
	background-color: #aba8a8;
}

.title-formulario-csv {
	margin-top: 3%;
}

.formulario-csv {
	width: 40%;
	border: none;
	border-radius: 5px;
	background-color: #6699ff;
	padding: 25px 10px;
	margin: auto; /* Centra el formulario horizontalmente */
}

.formulario-csv input, .formulario-csv button {
	display: block;
	margin-left: 36%;
}

.fileInputCsv {
	cursor: pointer;
	border: none;
	border-radius: 5px;
	transition: background-color 0.3s ease;
	margin-bottom: 20px;
}

.boton-formulario-csv {
	cursor: pointer;
	margin-bottom: 0;
	margin-top: 2%;
	border: none;
	border-radius: 5px;
	padding: 10px 25px;
	transition: background-color 0.3s ease;
}

.boton-formulario-csv:hover {
	background-color: #aba8a8;
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
		<h2 class="title-home">TRANSFORMADOR DE CONJUNTOS DE DATOS EN
			RECURSOS RDF</h2>

	</div>

	<div class="container">
		<h2 class="title-formulario-base">BASE DE DATOS</h2>
		<div id="formulario-basedatos" class="formulario-basedatos">

			<h3>Pulsa el botón para ir a la sección de base de datos</h3>

			<form id="formularioDataBase" action="DataBase" method="get">
				<button class="boton-formulario-base" type="submit">Ir</button>
			</form>
		</div>

		<h2 class="title-formulario-json">JSON</h2>
		<div id="formulario-json" class="formulario-json">

			<h3>Selecciona el archivo JSON a transformar</h3>


			<!--  enctype="multipart/form-data" para poder enviar archivos -->
			<form id="formularioJson" action="Json" method="post"
				enctype="multipart/form-data">
				<input type="file" id="fileInputJson" name="fileInputJson"
					class="fileInputJson">
				<button id="subirArchivoJson" class="boton-formulario-json"
					type="submit" disabled>Subir Archivo</button>
			</form>

		</div>



		<h2 class="title-formulario-csv">CSV</h2>
		<div id="formulario-csv" class="formulario-csv">

			<h3>Selecciona el archivo CSV a transformar</h3>

			<!--  enctype="multipart/form-data" para poder enviar archivos -->
			<form id="formularioCSV" action="Csv" method="post"
				enctype="multipart/form-data">
				<input type="file" id="fileInputCsv" name="fileInputCsv"
					class="fileInputCsv">
				<button id="subirArchivoCsv" class="boton-formulario-csv"
					type="submit" disabled>Subir Archivo</button>

			</form>

		</div>


		<br> <br> <br> <br> <br>


	</div>

	<footer>
		<p>&copy; 2024 Copyright: Álvaro Chapinal</p>
	</footer>

	<script>
		// Obtener referencia al elemento de entrada de archivo JSON
		const fileInputJson = document.getElementById('fileInputJson');
		// Obtener referencia al botón de subir archivo JSON
		const subirArchivoJson = document.getElementById('subirArchivoJson');

		// Agregar un event listener para detectar cambios en el campo de entrada de archivo JSON
		fileInputJson.addEventListener('change', function() {
			// Verificar si se ha seleccionado un archivo
			if (fileInputJson.files.length > 0) {
				// Si se ha seleccionado un archivo, habilitar el botón
				subirArchivoJson.removeAttribute('disabled');
			} else {
				// Si no se ha seleccionado un archivo, deshabilitar el botón
				subirArchivoJson.setAttribute('disabled', 'true');
			}
		});

		//Obtener referencia al elemento de entrada de archivo CSV
		const fileInputCsv = document.getElementById('fileInputCsv');
		// Obtener referencia al botón de subir archivo CSV
		const subirArchivoCsv = document.getElementById('subirArchivoCsv');

		// Agregar un event listener para detectar cambios en el campo de entrada de archivo CSV
		fileInputCsv.addEventListener('change', function() {
			// Verificar si se ha seleccionado un archivo
			if (fileInputCsv.files.length > 0) {
				// Si se ha seleccionado un archivo, habilitar el botón
				subirArchivoCsv.removeAttribute('disabled');
			} else {
				// Si no se ha seleccionado un archivo, deshabilitar el botón
				subirArchivoCsv.setAttribute('disabled', 'true');
			}
		});
	</script>
</body>
</html>
