<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>

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

.title-list {
	margin-top: 3%;
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

.jsonList {
	list-style-position: inside;
	padding-left: 0;
	font-size: 20px;
	color: #333;
	border: 1px solid #ccc;
	border-radius: 5px;
	background-color: #f9f9f9;
	max-width: 400px;
	margin-left: auto;
	margin-right: auto;
	padding: 10px;
}

.popup {
	display: none;
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.5);
	z-index: 9999;
}

.popup-contenido {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background-color: white;
	padding: 20px;
	border-radius: 5px;
}

#serializacion {
	margin-bottom: 5%;
}

.buttonCerrarPopupTransformar {
	background-color: #007bff;
	color: white;
	border: none;
	padding: 10px 20px;
	cursor: pointer;
	border-radius: 5px;
}

.buttonCerrarPopupTransformar:hover {
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

		<h2 class="title-list">JSON</h2>

		<button id="mostrarPopupTransformar" class="button-transformar">Transformar
			a RDF</button>


		<ul class="jsonList">
			<% 
            List<Object> jsons = (List<Object>) request.getAttribute("json");
            for (Object json : jsons) {
                out.println("<li>" + json.toString() + "</li>");
            }
            %>
		</ul>


		<form id="rdfForm" action="JsonToRDF" method="post"
			target="hidden_iframe">
			<div id="formPopUp" class="popup">
				<div class="popup-contenido">

					<h2>Transformar a RDF</h2>

					<label for="nombreArchivo">Nombre del archivo:</label> <input
						type="text" id="nombreArchivo" name="nombreArchivo"
						placeholder="Escribe el nombre del archivo .txt" required>
					<br>
					<br> <label for="serializacion">Formato de
						serialización:</label> <select id="serializacion" name="serializacion">
						<option value="Turtle">Turtle</option>
						<option value="RDF/XML">RDF/XML</option>
						<option value="JSON-LD">JSON-LD</option>
						<option value="N-Triples">N-Triples</option>
					</select> <br> <label for="campoSujeto"> Campo sujeto por
						defecto</label> <input type="checkbox" id="check"
						onchange="checkCampoSujeto()" checked> <br>
					<br> <input type="text" id="campoSujeto" name="campoSujeto"
						placeholder="Campo para ser sujeto" disabled> <br>
					<br>
					<button class="buttonCerrarPopupTransformar" type="submit"
						id="cerrarPopupTransformar">Confirmar</button>
				</div>
			</div>
		</form>

		<iframe id="hidden_iframe" name="hidden_iframe" style="display: none;"></iframe>

		<br> <br> <br> <br> <br>

	</div>

	<footer>
		<p>&copy; 2024 Copyright: Álvaro Chapinal</p>
	</footer>

	<script>
    function checkCampoSujeto() {
        var checkbox = document.getElementById("check");
        var campoSujeto = document.getElementById("campoSujeto");

        if (checkbox.checked) {
            campoSujeto.required = false;
            campoSujeto.disabled = true;
            campoSujeto.value = ""; // Limpia el valor del campo
            
        } else {
            campoSujeto.required = true;
            campoSujeto.disabled = false;
          
        }
    }

    function validarFormulario() {
        var nombreArchivo = document.getElementById("nombreArchivo").value;
        var campoSujeto = document.getElementById("campoSujeto");

        if (nombreArchivo.trim() === "") {
            alert("Por favor, rellene todos los campos.");
            return false;
        }

        if (campoSujeto.required && campoSujeto.value.trim() === "") {
            alert("Por favor, rellene el campo sujeto.");
            return false;
        } 

        return true;
    }

    function limpiarFormulario() {
        document.getElementById("nombreArchivo").value = "";
        document.getElementById("serializacion").selectedIndex = 0;
        document.getElementById("check").checked = true;
        checkCampoSujeto();
    }

    document.addEventListener("DOMContentLoaded", function() {
        checkCampoSujeto();

        document.getElementById("cerrarPopupTransformar").addEventListener("click", function(event) {
            if (!validarFormulario()) {
                event.preventDefault();
            } else {
                document.getElementById("formPopUp").style.display = "none";
            }
        });

        document.getElementById("mostrarPopupTransformar").addEventListener("click", function() {
            limpiarFormulario();
            document.getElementById("formPopUp").style.display = "block";
        });
    });

    document.getElementById('hidden_iframe').addEventListener('load', function() {
        const iframe = document.getElementById('hidden_iframe');
        const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
        console.log("Verificando flujo de control...");
        
        if (iframeDoc.body && iframeDoc.body.innerText.trim()) {
            const responseText = iframeDoc.body.innerText.trim();
            console.log("Response received:", responseText);
            
            if (responseText.includes('ERROR')) {
                 window.location.href = "error.jsp"
            } 
            
            else {
            	 
            	 
            const blob = new Blob([responseText], { type: 'text/plain' });
            const form = document.getElementById('rdfForm');
            const formData = new FormData(form);
            const nombreArchivo = formData.get('nombreArchivo');
            const extension = ".txt";
            const nombreArchivoFinal= nombreArchivo+extension;
            console.log('Nombre del archivo:', nombreArchivo); 
            
            const a = document.createElement('a');
            a.href = URL.createObjectURL(blob);
            a.download = nombreArchivoFinal;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(a.href);
            
            window.location.href = "resultados.jsp"; // Redirigir a home.jsp si no hay errores
            } 
        } 
    });
    </script>
</body>
</html>
