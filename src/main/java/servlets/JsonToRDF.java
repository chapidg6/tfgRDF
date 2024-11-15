package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import rdf.ResourceFieldNotFoundException;
import rdf.Servicio;

/**
 * Servlet implementation class JsonToRDF
 */
@WebServlet(name = "JsonToRDF", urlPatterns = { "/JsonToRDF" })
@MultipartConfig
public class JsonToRDF extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public JsonToRDF() {
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
		// TODO Auto-generated method stub
		try {
			// Obtener la ruta temporal del archivo JSON y otros par�metros de la solicitud
			HttpSession session = request.getSession();
			String tempJsonPath = (String) session.getAttribute("tempJsonPath");
			String nombreArchivo = request.getParameter("nombreArchivo");
			String serializacion = request.getParameter("serializacion");
			String campoSujeto = request.getParameter("campoSujeto");

			// Transformar el JSON a RDF utilizando un servicio
			Servicio servicio = new Servicio();
			String rdfContent = servicio.jsonListToRDFTransformer(tempJsonPath, serializacion, campoSujeto);

			// Configurar los encabezados de la respuesta para la descarga del archivo
			response.setContentType("text/plain");
			//response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + ".txt\"");

			// Escribir el contenido RDF en la respuesta
			try (PrintWriter out = response.getWriter()) {
				out.write(rdfContent);
				out.flush();
			}
			
			session.setAttribute("rdfContent", rdfContent);
			session.setAttribute("serializacion", serializacion);
			
		} catch (ResourceFieldNotFoundException e) {
			// Manejar la excepci�n si un campo de recurso no es encontrado
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			HttpSession session = request.getSession();
			session.setAttribute("errorMessage", e.getMessage());
			
			try (PrintWriter out = response.getWriter()) {
				out.write("ERROR: " + e.getMessage());
				out.flush();
			}
		} catch (Exception e) {
			// Manejar otras excepciones internas del servidor
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try (PrintWriter out = response.getWriter()) {
				out.write("ERROR: " + e.getMessage());
				out.flush();
			}
		}
	}
}