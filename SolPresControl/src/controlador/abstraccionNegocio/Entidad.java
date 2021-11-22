package controlador.abstraccionNegocio;

/**
 * Clase que abstrae a todas las entidades del sistema
 */
public abstract class Entidad {
    //Identificador de la entidad
    private final String id;

    //Constructor basico de entidad
    public Entidad(String id) {
        this.id = id;
    }

    //Retorna identificador de la entidad
    public String getId() {
        return id;
    }

    // Devuelve los parametros como string
    @Override
    public String toString() {
        return id;
    }
}
