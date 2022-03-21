package modelo.evento.presentacion;

import java.util.LinkedList;

/**
 * Estructura de datos que contiene a todos las presentaciones del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionPresentaciones {

    /**
     * String constante que posee el error cuando la presentacion esta registrada
     */
    public static final String ERROR_PRESENTACION_REGISTRADA = "Presentacion ya registrada";
    /**
     * String constante que posee el error cuando la presentacion esta registrada
     */
    public static final String ERROR_PRESENTACION_NO_REGISTRADA = "Presentacion no registrada";

    /**
     *  LinkedList que contiene a todas las presentaciones
     */
    private final LinkedList<Presentacion> presentaciones;

    /**
     * Constructor que inicializa la coleccion de presentaciones
     */
    public ColeccionPresentaciones(){
        presentaciones = new LinkedList<>();
    }

    /**
     * Devuelve el objeto presentacion del identificador
     * @param identificador Identificador de la presentacion deseada
     * @return Objeto presentacion que deseamos obtener, sera nulo si no existe
     * @throws IllegalArgumentException Si la presentacion no esta registrada
     */
    public Presentacion getPresentacion(String identificador) {
        Presentacion presentacionExistente = buscaPresentacion(identificador);
        if (presentacionExistente == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        return presentacionExistente;
    }

    /**
     * Agrega la presentacion a la coleccion
     * @param presentacion Objeto Presentacion que deseamos agregar a la coleccion
     * @throws IllegalArgumentException si el id de la presentacion corresponde a una ya registrada
     */
    public void addPresentacion(Presentacion presentacion) {
        Presentacion presentacionExistente = buscaPresentacion(presentacion.getId());
        if (presentacionExistente != null) {
            throw new IllegalArgumentException(ERROR_PRESENTACION_REGISTRADA);
        }
        presentaciones.add(presentacion);
    }

    /**
     * Elimina una presentacion del sistema
     * @param presentacion Presentacion a eliminar
     * @throws IllegalArgumentException si el id pasado no corresponde a una presentacion registrada
     */
    public void removePresentacion(Presentacion presentacion) {
        Presentacion presentacionExistente = buscaPresentacion(presentacion.getId());
        if (presentacionExistente == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        // Abandona todas sus relaciones
        presentacion.abandonaRelaciones();
        presentaciones.remove(presentacion);
    }

    /**
     * Devuelve como un LinkedList todas las presentaciones
     * @return LinkedList con todas las presentaciones del sistema
     */
    public LinkedList<Presentacion> getPresentacionesLinkedList() {
        return new LinkedList<>(presentaciones);
    }

    /**
     *  Metodo para conseguir todas las presentaciones del sistema como un string
     * @return String con todos las presentaciones del sistema con todos sus parametros
     */
    @Override
    public String toString() {
        StringBuilder presenString = new StringBuilder();
        for (Presentacion presentacion : presentaciones){
            presenString.append(presentacion.toString()).append("\n");
        }
        return presenString.toString();
    }

    /**
     * Metodo que busca presentaciones en la coleccion por identificador
     *
     * @param identificador identificador de la presentacion a buscar
     * @return Presentacion con ese identificador en caso de existir, null en caso contrario
     */
    private Presentacion buscaPresentacion(String identificador){
        for (Presentacion presentacion : presentaciones) {
            if (presentacion.getId().equals(identificador.toLowerCase())) return presentacion;
        }
        return null;
    }
}
