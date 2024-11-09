package rdf;


public class ResourceFieldNotFoundException extends Exception {
    // Constructor que acepta un mensaje de error
    public ResourceFieldNotFoundException(String message) {
        super(message); // Llama al constructor de la superclase (Exception) con el mensaje
    }
}