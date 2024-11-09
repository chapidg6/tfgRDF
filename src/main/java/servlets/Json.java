package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class Ejemplo
 */
@WebServlet(name = "Json", urlPatterns = { "/Json" })

//Para poder recibir archivos desde un formulario
@MultipartConfig
public class Json extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Json() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Obtener el archivo enviado por el cliente
		Part filePart = request.getPart("fileInputJson");

		// Obtener el nombre del archivo
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

		// Verificar si el archivo es un .json
		if (fileName.endsWith(".json")) {
			// Crear un archivo temporal
			Path tempJson = Files.createTempFile("temp", ".json");

			// Copiar el contenido del archivo enviado al archivo temporal
			try (InputStream fileContent = filePart.getInputStream()) {
				Files.copy(fileContent, tempJson, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// Manejar errores de entrada/salida
				System.out.println("Error al copiar el contenido del archivo: " + e.getMessage());
				e.printStackTrace();
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}

			try {
				// Abrir un BufferedReader para leer el archivo temporal
				BufferedReader br = new BufferedReader(new FileReader(tempJson.toString()));
				// Utilizar un StringBuilder para construir una cadena grande con el contenido
				// del archivo
				StringBuilder sb = new StringBuilder();
				String line;
				// Leer el archivo línea por línea y añadir cada línea al StringBuilder
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();

				// Crear un ObjectMapper de Jackson
				ObjectMapper objectMapper = new ObjectMapper();

				// Convertir el JSON a una lista de objetos utilizando Jackson
				List<Object> json = objectMapper.readValue(sb.toString(), new TypeReference<List<Object>>() {
				});

				// Guardar la ruta del archivo JSON temporal en la sesión
				HttpSession session = request.getSession();
				session.setAttribute("tempJsonPath", tempJson.toString());

				// Almacenar el JSON en el request para ser usado en la página siguiente
				request.setAttribute("json", json);

				// Redirigir a la página 'json.jsp' para mostrar el JSON
				request.getRequestDispatcher("json.jsp").forward(request, response);

			} catch (IOException e) {
				// Manejar la excepción de entrada/salida
				System.out.println("El sistema tuvo un error leyendo el archivo.");
				e.printStackTrace();

				// Redirigir a la página de error
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}
		} else {
			// Si el archivo no es un .json, mostrar un mensaje de error
			System.out.println("Error: Archivo no compatible, no es un archivo .json.");

			// Guardar el nombre del archivo incompatible en el objeto request y asi
			// acceder a él en la pagina error
			request.setAttribute("fileName", fileName);

			// Redirigir a la página de error
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
