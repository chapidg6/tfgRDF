package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import rdf.Servicio;
import entity.Persona;

import java.util.List;

/**
 * Servlet implementation class DataBase
 */
@WebServlet(name = "DataBase", urlPatterns = { "/DataBase" })
public class DataBase extends jakarta.servlet.http.HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DataBase() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// Crear el EntityManagerFactory utilizando la unidad de persistencia "RDF"
			emf = Persistence.createEntityManagerFactory("RDF");
			// Crear un EntityManager para interactuar con la base de datos
			em = emf.createEntityManager();

			// Crear una consulta para obtener todas las instancias de la entidad Persona
			TypedQuery<Persona> query = em.createQuery("SELECT p FROM Persona p", Persona.class);
			// Ejecutar la consulta y obtener la lista de resultados
			List<Persona> personas = query.getResultList();

			// Establecer la lista de personas como un atributo de la solicitud
			request.setAttribute("personas", personas);
			// Redirigir a la página JSP que mostrará los datos obtenidos de la base de
			// datos
			request.getRequestDispatcher("database.jsp").forward(request, response);

		} catch (Exception e) {
			// Manejar cualquier excepción lanzada durante el proceso
			throw new ServletException("Error al procesar la solicitud", e);

		} finally {
			// Cerrar el EntityManager si no es nulo
			if (em != null) {
				em.close();
			}
			// Cerrar el EntityManagerFactory si no es nulo
			if (emf != null) {
				emf.close();
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {
			// Obtener los parámetros del formulario
			HttpSession session = request.getSession();
			String nombreArchivo = request.getParameter("nombreArchivo");
			String serializacion = request.getParameter("serializacion");

			// Transformar los datos de la base de datos a RDF utilizando un servicio
			Servicio servicio = new Servicio();
			String rdfContent = servicio.dataBaseToRDFTransformer(serializacion);

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