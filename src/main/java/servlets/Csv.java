package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class Csv
 */
@WebServlet(name = "Csv", urlPatterns = { "/Csv" })
//Para poder recibir archivos desde un formulario
@MultipartConfig
public class Csv extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor por defecto.
	 */
	public Csv() {
		// Constructor auto-generado pendiente de implementaci�n
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// M�todo auto-generado pendiente de implementaci�n
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// M�todo auto-generado pendiente de implementaci�n

		// Obtener el archivo enviado por el cliente
		Part filePart = request.getPart("fileInputCsv");

		// Obtener el nombre del archivo
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

		// Verificar si el archivo es un .csv
		if (fileName.endsWith(".csv")) {
			// Crear un archivo temporal
			Path tempCsv = Files.createTempFile("temp", ".csv");

			// Copiar el contenido del archivo enviado al archivo temporal
			try (InputStream fileContent = filePart.getInputStream()) {
				Files.copy(fileContent, tempCsv, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// Manejar errores de entrada/salida
				System.out.println("Error al copiar el contenido del archivo: " + e.getMessage());
				e.printStackTrace();
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}

			// Lista para almacenar las filas del CSV
			ArrayList<String[]> csv = new ArrayList<>();
			// Variable para almacenar las cabeceras del CSV
			String[] headers = null;

			// Leer el archivo CSV temporal
			try (CSVReader reader = new CSVReader(new FileReader(tempCsv.toString()))) {
				String[] nextLine;
				boolean firstLine = true;

				// Leer cada l�nea del archivo CSV
				while ((nextLine = reader.readNext()) != null) {
					if (firstLine) {
						// La primera l�nea contiene las cabeceras
						headers = nextLine;
						firstLine = false;
					} else {
						// Agregar las siguientes l�neas a la lista
						csv.add(nextLine);
					}
				}

				// Guardar la ruta del archivo CSV temporal en la sesi�n
				HttpSession session = request.getSession();
				session.setAttribute("tempCsvPath", tempCsv.toString());

				// Pasar las cabeceras y los datos del CSV a la solicitud
				request.setAttribute("csvHeaders", headers);
				request.setAttribute("csv", csv);

				// Redirigir a la p�gina JSP para mostrar el contenido del CSV
				request.getRequestDispatcher("csv.jsp").forward(request, response);

			} catch (CsvValidationException e) {
				// Manejar errores de validaci�n del CSV
				System.out.println("Error de validaci�n CSV: " + e.getMessage());
				e.printStackTrace();
				request.getRequestDispatcher("error.jsp").forward(request, response);
			} catch (IOException e) {
				// Manejar errores de entrada/salida
				System.out.println("Error de E/S: " + e.getMessage());
				e.printStackTrace();
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}
		} else {
			// Si el archivo no es un .csv, mostrar un mensaje de error
			System.out.println("Error: Archivo no compatible, no es un archivo .csv.");

			// Guardar el nombre del archivo incompatible en el objeto request y asi
			// acceder a �l en la pagina error
			request.setAttribute("fileName", fileName);

			// Redirigir a la p�gina de error
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}
