package rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.DC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import entity.Persona;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class Servicio {

	// Método para crear URIs a partir de strings
	public static String createURI(String name) {
		// Se puede ajustar este formato según las necesidades
		return "http://example.org/" + name.replaceAll(" ", "_");
	}

	  public String jsonListToRDFTransformer(String jsonContentPath, String serializacion, String resourceFieldName) throws ResourceFieldNotFoundException {
	        // Crea un modelo RDF
	        Model model = ModelFactory.createDefaultModel();
	        String source = "Json";

	        // Transformar JSON a RDF
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode jsonNode = objectMapper.readTree(new File(jsonContentPath));

	            // Iterar sobre la lista de objetos en el JSON
	            for (JsonNode objectNode : jsonNode) {
	                // Determinar el campo de recurso y su valor
	                JsonNode resourceFieldNode = null;
	                String resourceId = null; // Inicializar variable
	                String actualResourceFieldName = resourceFieldName; // Inicializar variable

	                if (resourceFieldName == null) {
	                    // Si no se especifica un campo de recurso, usar el primer campo del objeto JSON
	                    Map.Entry<String, JsonNode> firstField = objectNode.fields().next();
	                    resourceFieldNode = firstField.getValue();
	                    resourceId = resourceFieldNode.asText();
	                    actualResourceFieldName = firstField.getKey();
	                } else {
	                    // Si se especifica un campo de recurso, buscarlo en el objeto JSON
	                    resourceFieldNode = objectNode.get(resourceFieldName);
	                    if (resourceFieldNode == null) {
	                        // Opción para que el campo coincida aunque haya diferencias de minúsculas/mayúsculas
	                        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
	                        while (fields.hasNext()) {
	                            Map.Entry<String, JsonNode> field = fields.next();
	                            if (field.getKey().equalsIgnoreCase(resourceFieldName)) { // Comparación sin distinguir mayúsculas y minúsculas
	                                resourceFieldNode = field.getValue();
	                                actualResourceFieldName = field.getKey();
	                                break;
	                            }
	                        }

	                        if (resourceFieldNode == null) {
	                            throw new ResourceFieldNotFoundException("El nombre del campo de recurso '" + resourceFieldName + "' no existe en el JSON.");
	                        }
	                    } else {
	                        actualResourceFieldName = resourceFieldName;
	                    }

	                    // Crear un recurso RDF con el valor del campo especificado como ID
	                    if (resourceFieldNode.isObject()) {
	                        resourceId = createResourceIdFromObject(resourceFieldNode);
	                    } else {
	                        resourceId = resourceFieldNode.asText();
	                    }
	                }

	                Resource resource = model.createResource(createURI(resourceId));

	                // Iterar sobre los campos del objeto JSON
	                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = objectNode.fields();

	                while (fieldsIterator.hasNext()) {
	                    Map.Entry<String, JsonNode> field = fieldsIterator.next();
	                    String fieldName = field.getKey();
	                    JsonNode fieldValue = field.getValue();

	                    // Omitir el campo de recurso ya que ya lo hemos utilizado como ID del recurso
	                    if (fieldName.equalsIgnoreCase(actualResourceFieldName)) {
	                        continue;
	                    }

	                    // Crear una propiedad RDF como URI y agregarla al recurso
	                    Property property = model.createProperty(createURI(fieldName));

	                    // Agregar la propiedad al recurso según el tipo de dato
	                    addPropertyToResource(resource, property, fieldValue, resourceId + "_" + fieldName);
	                }
	            }

	            // Agregar metadatos al modelo RDF
	            addMetadata(model, source);

	            // Serializar el modelo RDF al formato especificado
	            return serializeModel(model, serializacion);
	        } catch (IOException e) {
	            throw new RuntimeException("Error al procesar el archivo JSON", e);
	        }
	    }
	  

	  // Crear un ID de recurso desde un objeto JSON
	  private String createResourceIdFromObject(JsonNode objectNode) {
	        StringBuilder resourceId = new StringBuilder();
	        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = objectNode.fields();
	        while (fieldsIterator.hasNext()) {
	            Map.Entry<String, JsonNode> field = fieldsIterator.next();
	            resourceId.append(field.getValue().asText().replaceAll(" ", "_")).append("_");
	        }
	        // Eliminar el último guion bajo
	        if (resourceId.length() > 0) {
	            resourceId.setLength(resourceId.length() - 1);
	        }
	        return resourceId.toString();
	    }
	  
	  
	private static void addPropertyToResource(Resource resource, Property property, JsonNode fieldValue, String parentId) {
		if (fieldValue.isTextual()) {
	        // Si el valor del campo JSON es textual, se agrega como una cadena al recurso
	        resource.addProperty(property, fieldValue.asText());
	    } else if (fieldValue.isNumber()) {
	        // Si el valor del campo JSON es numérico, se convierte a cadena y se agrega al recurso
	        resource.addProperty(property, String.valueOf(fieldValue.numberValue()));
	    } else if (fieldValue.isBoolean()) {
	        // Si el valor del campo JSON es booleano, se convierte a cadena y se agrega al recurso
	        resource.addProperty(property, String.valueOf(fieldValue.asBoolean()));
	    } else if (fieldValue.isArray()) {
	        // Si el valor del campo JSON es un array, se itera sobre sus elementos
	        int index = 0;
	        for (JsonNode arrayElement : fieldValue) {
	            // Se genera un ID único para cada elemento del array
	            String arrayResourceId = parentId + "_" + index;
	            // Se crea un recurso para representar el elemento del array
	            Resource arrayResource = resource.getModel().createResource(createURI(arrayResourceId));
	            // Se agrega el recurso como valor de la propiedad al recurso principal
	            resource.addProperty(property, arrayResource);
	            // Se llama recursivamente a la función para procesar los elementos del array
	            addPropertyToResource(arrayResource, property, arrayElement, arrayResourceId);
	            index++;
	        }
	    } else if (fieldValue.isObject()) {
	        // Si el valor del campo JSON es un objeto, se crea un recurso para representarlo
	        Resource objectResource = resource.getModel().createResource(createURI(parentId));
	        // Se agrega el recurso como valor de la propiedad al recurso principal
	        resource.addProperty(property, objectResource);
	        // Se itera sobre los campos del objeto y se procesan recursivamente
	        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = fieldValue.fields();
	        while (fieldsIterator.hasNext()) {
	            Map.Entry<String, JsonNode> field = fieldsIterator.next();
	            // Se crea una propiedad para cada campo del objeto
	            Property propiedadAnidada = resource.getModel().createProperty(createURI(field.getKey()));
	            // Se llama recursivamente a la función para procesar los valores de los campos del objeto
	            addPropertyToResource(objectResource, propiedadAnidada, field.getValue(), parentId + "_" + field.getKey());
	        }
	    }
	}
	
	
	public String csvToRDFTransformer(String csvContentPath, String serializacion, String resourceFieldName) throws ResourceFieldNotFoundException {
	    // Crear un modelo RDF vacío
	    Model model = ModelFactory.createDefaultModel();
	    String source = "Csv";

	    try (CSVReader reader = new CSVReader(new FileReader(csvContentPath))) {
	        // Leer la cabecera del CSV
	        String[] headers = reader.readNext();

	        // Determinar el índice del campo de recurso por defecto inicialmente
	        int resourceFieldIndex = -1;

	        // Si no se especifica un campo de recurso, usar el primer campo
	        if (resourceFieldName == null) {
	            resourceFieldIndex = 0;
	        } 
	        
	        //Si se especifica un campo de recurso, se busca el índice del campo de recurso especificado
	        else {
	           
	            for (int i = 0; i < headers.length; i++) {
	                if (headers[i].equalsIgnoreCase(resourceFieldName)) {
	                    resourceFieldIndex = i;
	                    break;
	                }
	            }
	            // Lanzar una excepción si el campo de recurso no existe
	            if (resourceFieldIndex == -1) {
	                throw new ResourceFieldNotFoundException("El nombre del campo de recurso " + resourceFieldName + " no existe en el archivo CSV.");
	            }
	        }

	        // Leer cada fila del CSV y crear recursos RDF
	        String[] values;
	        while ((values = reader.readNext()) != null) {
	            // Verificar que el índice del campo de recurso esté dentro de los límites
	            if (resourceFieldIndex < 0 || resourceFieldIndex >= values.length) {
	                throw new IllegalArgumentException("Índice del campo de recurso fuera de rango.");
	            }

	            // Omitir la fila si el campo sujeto está vacío
	            if (values[resourceFieldIndex].isEmpty()) {
	                continue;
	            }

	            // Crear un recurso RDF para el valor del campo de recurso
	            Resource resource = model.createResource(createURI(values[resourceFieldIndex]));
	            for (int i = 0; i < headers.length; i++) {
	                // Agregar propiedades al recurso para cada campo no vacío, excepto el campo de recurso
	                if (i != resourceFieldIndex && i < values.length && !values[i].isEmpty()) {
	                    Property property = model.createProperty(createURI(headers[i]));
	                    resource.addProperty(property, values[i]);
	                }
	            }
	        }

	        // Agregar metadatos al modelo RDF
	        addMetadata(model, source);

	        // Serializar el modelo RDF al formato especificado
	        return serializeModel(model, serializacion);

	    } catch (IOException | CsvValidationException e) {
	        // Manejar errores de entrada/salida y validación CSV
	        throw new RuntimeException("Error al procesar el archivo CSV", e);
	    }
	}
	
	
	public String dataBaseToRDFTransformer(String serializacion) {
	    // Crea un modelo RDF
	    Model model = ModelFactory.createDefaultModel();

	    // Fuente de los datos RDF
	    String source = "DataBase";

	    // Propiedades y recursos RDF
	    Property name = VCARD.NAME; // Propiedad para el nombre
	    Property age = model.createProperty("http://example.org/Age"); // Propiedad para la edad

	    EntityManagerFactory emf = null;
	    EntityManager em = null;

	    try {
	        emf = Persistence.createEntityManagerFactory("RDF");
	        em = emf.createEntityManager();

	        // Realizar una consulta para obtener una lista de personas
	        TypedQuery<Persona> query = em.createQuery("SELECT p FROM Persona p", Persona.class);
	        List<Persona> personas = query.getResultList();

	        for (Persona persona : personas) {
	            // Crear un recurso RDF para cada persona usando su ID como URI
	            Resource personaResource = model.createResource("http://example.org/" + persona.getId());

	            // Agregar propiedades al recurso RDF
	            personaResource.addProperty(name, persona.getNombre()); // Añade el nombre
	            personaResource.addProperty(age, String.valueOf(persona.getEdad())); // Añade la edad
	        }

	        // Agregar metadatos al modelo RDF
	        addMetadata(model, source);

	        // Serializar el modelo RDF al formato especificado
	        return serializeModel(model, serializacion);

	    } catch (Exception e) {
	        // Capturar y manejar cualquier excepción que ocurra durante la ejecución del bloque try
	        e.printStackTrace();
	        return null; // Devolver null en caso de error
	        
	    } finally {
	        // Cerrar el EntityManager y el EntityManagerFactory en el bloque finally
	        // para garantizar que se ejecute incluso si ocurre una excepción.

	        if (em != null) {
	            em.close();
	        }
	        if (emf != null) {
	            emf.close();
	        }
	    }
	}


	private void addMetadata(Model model, String source) {
		  // Genera un URI único para el grafo RDF basado en la marca de tiempo actual
	    String uriGrafo = "http://www.example.com/resource/grafoRDF/" + System.currentTimeMillis();
	    
	    // Crea un recurso RDF para representar la declaración del sujeto del grafo
	    Resource sujetoDeclaracion = model.createResource(uriGrafo);

	    // Obtiene la fecha actual y la formatea en el formato deseado
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String fechaCreacion = sdf.format(new Date());

	    // Crea un recurso RDF para representar los metadatos del grafo
	    Resource metadatosGrafo = model.createResource("http://www.example.com/resource/metadata");
	    metadatosGrafo.addProperty(DC.source, source); // Agrega la propiedad DC.source con el valor de la fuente
	    metadatosGrafo.addProperty(DC.date, fechaCreacion); // Agrega la propiedad DC.date con la fecha de creación

	    // Crea una declaración RDF que relaciona el sujeto del grafo con los metadatos del grafo
	    model.add(model.createStatement(sujetoDeclaracion, DC.description, metadatosGrafo));
	}
	
	
	
	
	private String serializeModel(Model model, String serializacion) {
		
	    StringWriter out = new StringWriter();
	    
	    switch (serializacion.toLowerCase()) {
	        case "turtle":
	            model.write(out, "TURTLE");
	            break;
	        case "rdf/xml":
	            model.write(out, "RDF/XML");
	            break;
	        case "json-ld":
	            model.write(out, "JSON-LD");
	            break;
	        case "n-triples":
	            model.write(out, "N-TRIPLES");
	            break;
	        default:
	            throw new IllegalArgumentException("Formato de serialización no soportado: " + serializacion);
	    }
	    return out.toString();
	}
	 
	
	/*
	public static String getWindowsDirectory() {
		// Obtener el directorio del escritorio según la configuración de OneDrive en
		// Windows
		String userHome = System.getProperty("user.home");
		String oneDriveDesktop = userHome + "\\OneDrive\\";
		String standardDesktop = userHome + "\\";

		// Comprobar si la carpeta de OneDrive existe
		Path oneDriveDesktopPath = Paths.get(oneDriveDesktop);
		if (Files.exists(oneDriveDesktopPath)) {
			// La carpeta de OneDrive existe, se devuelve su ruta
			return oneDriveDesktop;
		} else {
			// La carpeta de OneDrive no existe, se utiliza la ruta de escritorio estándar
			return standardDesktop;
		}
	}

	static void printTurtle(Model model, String nombreArchivo, String rutaDocumentoRDF) {

		String directorio = null;
		String os = System.getProperty("os.name").toLowerCase();
		// Sistema operativo Windows
		if (os.contains("win")) {
			String directorioWin = getWindowsDirectory();
			directorio = directorioWin + rutaDocumentoRDF + "\\";
			// Sitema operativo macOS
		} else if (os.contains("mac")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
			// Sistema operativo Linux
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
		} else {
			// Sistema operativo desconocido
			System.out.println("No se puede determinar el directorio para este sistema operativo.");

		}

		// Nombre del archivo y ruta completa
		String nombreDocumentoRDF = nombreArchivo + ".txt";
		String rutaCompleta = directorio + nombreDocumentoRDF;

		// Nombre del archivo en el que se sobreescribe y se usa para mostrar en la web
		// solo los datos que se acaban de transformar
		// y ruta completa de este
		String nombreDocumentoRDFTemp = nombreArchivo + "Temp.txt";
		String rutaCompletaTemp = directorio + nombreDocumentoRDFTemp;

		// Crea un StringWriter para capturar la salida del model y poder transformarlo
		// despues a un String para poder escribirlo en un documento
		StringWriter stringWriter = new StringWriter();

		// Verifica si el archivo de destino está vacío
		boolean archivoVacio = new File(rutaCompleta).length() == 0;

		try {

			// Se crea un objeto BufferedWriter para escribir en el documento
			// posteriormentos los datos transformados en RDF
			// Para no sobreescribir e indicar que el contenido se añade al final del
			// documento se indica true como segundo parametro de la clase FileWriter
			BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta, true));

			// Escritor que sobreescribe
			BufferedWriter writerTemp = new BufferedWriter(new FileWriter(rutaCompletaTemp));

			// Escribe el modelo RDF en formato TURTLE en el StringWriter
			model.write(stringWriter, "TURTLE");

			// Obtiene el modelo RDF como una cadena de texto
			String modeloTurtle = stringWriter.toString();

			// Imprime el modelo RDF obtenido por pantalla
			System.out.println("Contenido en formato TURTLE:");
			System.out.println(modeloTurtle);

			writerTemp.write(modeloTurtle);
			writerTemp.close();

			if (!archivoVacio) {
				// Si el archivo no está vacío, añade saltos de línea antes de escribir
				writer.newLine();
				writer.newLine();
				writer.newLine();
				writer.newLine();
			}

			// Escribe el modelo RDF obtenido como cadena de texto en el documento
			writer.write(modeloTurtle);
			writer.close();

			// Notifica al usuario de que el modelo se ha escrito en el documento
			System.out.println("El resultado del grafo se ha guardado en el documento " + nombreDocumentoRDF + " en "
					+ directorio);

			System.out.println();

		} catch (IOException e) {
			System.out.println("Ocurrió un error al escribir en el archivo.");
			e.printStackTrace();
		}
	}

	static void printRDFXML(Model model, String nombreArchivo, String rutaDocumentoRDF) {

		String directorio = null;
		String os = System.getProperty("os.name").toLowerCase();
		// Sistema operativo Windows
		if (os.contains("win")) {
			String directorioWin = getWindowsDirectory();
			directorio = directorioWin + rutaDocumentoRDF + "\\";
			// Sitema operativo macOS
		} else if (os.contains("mac")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
			// Sistema operativo Linux
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
		} else {
			// Sistema operativo desconocido
			System.out.println("No se puede determinar el directorio para este sistema operativo.");

		}

		// Nombre del archivo y ruta completa
		String nombreDocumentoRDF = nombreArchivo + ".txt";
		String rutaCompleta = directorio + nombreDocumentoRDF;

		// Nombre del archivo en el que se sobreescribe y se usa para mostrar en la web
		// solo los datos que se acaban de transformar
		// y ruta completa de este
		String nombreDocumentoRDFTemp = nombreArchivo + "Temp.txt";
		String rutaCompletaTemp = directorio + nombreDocumentoRDFTemp;

		// Crea un StringWriter para capturar la salida del model y poder transformarlo
		// despues a un String para poder escribirlo en un documento
		StringWriter stringWriter = new StringWriter();

		// Verifica si el archivo de destino está vacío
		boolean archivoVacio = new File(rutaCompleta).length() == 0;

		try {

			// Se crea un objeto BufferedWriter para escribir en el documento
			// posteriormentos los datos transformados en RDF
			// Paea no sobreescribir e indicar que el contenido se añade al final del
			// documento
			// Se indica true como segundo parametro de la clase FileWriter
			BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta, true));

			// Escritor que sobreescribe
			BufferedWriter writerTemp = new BufferedWriter(new FileWriter(rutaCompletaTemp));

			// Escribe el modelo RDF en formato RDF/XML en el StringWriter
			model.write(stringWriter, "RDF/XML");

			// Obtiene el modelo RDF como una cadena de texto
			String modeloRDFXML = stringWriter.toString();

			// Imprime el modelo RDF obtenido por pantalla
			System.out.println("Contenido en formato RDF/XML:");
			System.out.println(modeloRDFXML);

			if (!archivoVacio) {
				// Si el archivo no está vacío, añade saltos de línea antes de escribir
				writer.newLine();
				writer.newLine();
				writer.newLine();
				writer.newLine();
			}

			// Escribe el modelo RDF obtenido como cadena de texto en el documento
			writer.write(modeloRDFXML);
			writer.close();

			writerTemp.write(modeloRDFXML);
			writerTemp.close();

			// Notifica al usuario de que se ha el modelo escrito en el documento
			System.out.println("El resultado del grafo se ha guardado en el documento " + nombreDocumentoRDF + " en "
					+ rutaDocumentoRDF);

			System.out.println();

		} catch (IOException e) {
			System.out.println("Ocurrió un error al escribir en el archivo.");
			e.printStackTrace();
		}
	}

	static void printJSONLD(Model model, String nombreArchivo, String rutaDocumentoRDF) {

		String directorio = null;
		String os = System.getProperty("os.name").toLowerCase();
		// Sistema operativo Windows
		if (os.contains("win")) {
			String directorioWin = getWindowsDirectory();
			directorio = directorioWin + rutaDocumentoRDF + "\\";
			// Sitema operativo macOS
		} else if (os.contains("mac")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
			// Sistema operativo Linux
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
		} else {
			// Sistema operativo desconocido
			System.out.println("No se puede determinar el directorio para este sistema operativo.");

		}

		// Nombre del archivo y ruta completa
		String nombreDocumentoRDF = nombreArchivo + ".txt";
		String rutaCompleta = directorio + nombreDocumentoRDF;

		// Nombre del archivo en el que se sobreescribe y se usa para mostrar en la web
		// solo los datos que se acaban de transformar
		// y ruta completa de este
		String nombreDocumentoRDFTemp = nombreArchivo + "Temp.txt";
		String rutaCompletaTemp = directorio + nombreDocumentoRDFTemp;

		// Crea un StringWriter para capturar la salida del model y poder transformarlo
		// despues a un String para poder escribirlo en un documento
		StringWriter stringWriter = new StringWriter();

		// Verifica si el archivo de destino está vacío
		boolean archivoVacio = new File(rutaCompleta).length() == 0;

		try {

			// Se crea un objeto BufferedWriter para escribir en el documento
			// posteriormentos los datos transformados en RDF
			// Paea no sobreescribir e indicar que el contenido se añade al final del
			// documento
			// Se indica true como segundo parametro de la clase FileWriter
			BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta, true));

			// Escritor que sobreescribe
			BufferedWriter writerTemp = new BufferedWriter(new FileWriter(rutaCompletaTemp));

			// Escribe el modelo RDF en formato JSON-LD en el StringWriter
			model.write(stringWriter, "JSON-LD");

			// Obtiene el modelo RDF como una cadena de texto
			String modeloJSONLD = stringWriter.toString();

			// Imprime el modelo RDF obtenido por pantalla
			System.out.println("Contenido en formato JSON-LD:");
			System.out.println(modeloJSONLD);

			if (!archivoVacio) {
				// Si el archivo no está vacío, añade saltos de línea antes de escribir
				writer.newLine();
				writer.newLine();
				writer.newLine();
				writer.newLine();
			}
			// Escribe el modelo RDF obtenido como cadena de texto en el documento
			writer.write(modeloJSONLD);
			writer.close();

			writerTemp.write(modeloJSONLD);
			writerTemp.close();

			// Notifica al usuario de que se ha el modelo escrito en el documento
			System.out.println("El resultado del grafo se ha guardado en el documento " + nombreDocumentoRDF + " en "
					+ rutaDocumentoRDF);

			System.out.println();

		} catch (IOException e) {
			System.out.println("Ocurrió un error al escribir en el archivo.");
			e.printStackTrace();
		}
	}

	static void printNTriples(Model model, String nombreArchivo, String rutaDocumentoRDF) {

		String directorio = null;
		String os = System.getProperty("os.name").toLowerCase();
		// Sistema operativo Windows
		if (os.contains("win")) {
			String directorioWin = getWindowsDirectory();
			directorio = directorioWin + rutaDocumentoRDF + "\\";
			// Sitema operativo macOS
		} else if (os.contains("mac")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
			// Sistema operativo Linux
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			directorio = System.getProperty("user.home") + "/" + rutaDocumentoRDF;
		} else {
			// Sistema operativo desconocido
			System.out.println("No se puede determinar el directorio para este sistema operativo.");

		}

		// Nombre del archivo y ruta completa
		String nombreDocumentoRDF = nombreArchivo + ".txt";
		String rutaCompleta = directorio + nombreDocumentoRDF;

		// Nombre del archivo en el que se sobreescribe y se usa para mostrar en la web
		// solo los datos que se acaban de transformar
		// y ruta completa de este
		String nombreDocumentoRDFTemp = nombreArchivo + "Temp.txt";
		String rutaCompletaTemp = directorio + nombreDocumentoRDFTemp;

		// Crea un StringWriter para capturar la salida del model y poder transformarlo
		// despues a un String para poder escribirlo en un documento
		StringWriter stringWriter = new StringWriter();

		// Verifica si el archivo de destino está vacío
		boolean archivoVacio = new File(rutaCompleta).length() == 0;

		try {
			// Se crea un objeto BufferedWriter para escribir en el documento
			// posteriormentos los datos transformados en RDF
			// Paea no sobreescribir e indicar que el contenido se añade al final del
			// documento
			// Se indica true como segundo parametro de la clase FileWriter
			BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta, true));

			// Escritor que sobreescribe
			BufferedWriter writerTemp = new BufferedWriter(new FileWriter(rutaCompletaTemp));

			// Escribe el modelo RDF en formato N-Triples en el StringWriter
			model.write(stringWriter, "N-Triples");

			// Obtiene el modelo RDF como una cadena de texto
			String modeloNTriples = stringWriter.toString();

			// Imprime el modelo RDF obtenido por pantalla
			System.out.println("Contenido en formato N-Triples:");
			System.out.println(modeloNTriples);

			if (!archivoVacio) {
				// Si el archivo no está vacío, añade saltos de línea antes de escribir
				writer.newLine();
				writer.newLine();
				writer.newLine();
				writer.newLine();
			}

			// Escribe el modelo RDF obtenido como cadena de texto en el documento
			writer.write(modeloNTriples);
			writer.close();

			writerTemp.write(modeloNTriples);
			writerTemp.close();

			// Notifica al usuario de que se ha el modelo escrito en el documento
			System.out.println("El resultado del grafo se ha guardado en el documento " + nombreDocumentoRDF + " en "
					+ rutaDocumentoRDF);

			System.out.println();

		} catch (IOException e) {
			System.out.println("Ocurrió un error al escribir en el archivo.");
			e.printStackTrace();
		}
	}
*/
}
