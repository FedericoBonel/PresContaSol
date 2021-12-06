package abstraccionNegocio;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Estructura de datos que contiene a todos las presentaciones del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionPresentaciones {
    /**
     *  Hashtable que contiene a todas las presentaciones
     */
    private final Hashtable<String, Presentacion> presentaciones;

    /**
     * Constructor que inicializa la coleccion de presentaciones
     */
    public ColeccionPresentaciones(){
        presentaciones = new Hashtable<>();
    }

    /**
     * Devuelve el hashtable que contiene todas las presentaciones
     * @return El hashtable<String, Presentacion> con todos las presentaciones del sistema
     */
    protected Hashtable<String, Presentacion> getHashtable(){
        return presentaciones;
    }

    /**
     * Devuelve el objeto presentacion del identificador
     * @param identificador Identificador de la presentacion deseada
     * @return Objeto presentacion que deseamos obtener, sera nulo si no existe
     */
    public Presentacion getPresentacion(String identificador) {
        if (!presentaciones.containsKey(identificador)) throw new IllegalArgumentException("Presentacion no registrada");
        return presentaciones.get(identificador);
    }

    /**
     * Agrega la presentacion a la coleccion
     * @throws IllegalArgumentException si el id de la presentacion corresponde a una ya registrada
     * @param presentacion Objeto Presentacion que deseamos agregar a la coleccion
     */
    public void addPresentacion(Presentacion presentacion) {
        if (presentaciones.containsKey(presentacion.getId())) {
            throw new IllegalArgumentException("Presentacion ya registrada");
        }
        presentaciones.put(presentacion.getId(), presentacion);
    }

    /**
     * Elimina una presentacion del sistema
     * @throws IllegalArgumentException si el id pasado no corresponde a una presentacion registrada
     * @param presentacion Presentacion a eliminar
     */
    public void removePresentacion(Presentacion presentacion) {
        if (!presentaciones.containsKey(presentacion.getId())) throw new IllegalArgumentException("Presentacion no registrada");
        // Abandona todas sus relaciones
        presentacion.abandonaRelaciones();
        presentaciones.remove(presentacion.getId());
    }

    /**
     * Devuelve como un LinkedList todas las presentaciones
     * @return LinkedList con todas las presentaciones del sistema
     */
    public LinkedList<Presentacion> getPresentacionesLinkedList() {
        LinkedList<Presentacion> presentacionesLista = new LinkedList<>();
        for (String presentacionId : this.presentaciones.keySet()) {
            presentacionesLista.add(this.presentaciones.get(presentacionId));
        }
        return presentacionesLista;
    }

    /**
     *  Metodo para conseguir todas las presentaciones del sistema como un string
     * @return String con todos las presentaciones del sistema con todos sus parametros
     */
    @Override
    public String toString() {
        StringBuilder presenString = new StringBuilder();
        for (String presentacionId : presentaciones.keySet()){
            presenString.append(presentaciones.get(presentacionId).toString()).append("\n");
        }
        return presenString.toString();
    }
}
