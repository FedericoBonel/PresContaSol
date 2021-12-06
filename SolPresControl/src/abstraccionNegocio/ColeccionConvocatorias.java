package abstraccionNegocio;

import java.util.*;

/**
 * Estructura de datos que contiene a todas las convocatorias del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionConvocatorias {
    /**
     * Hashtable que contiene a todas las convocatorias
     */
    private final Hashtable<String, Convocatoria> convocatorias;

    /**
     * Constructor que inicializa la coleccion de convocatorias
     */
    public ColeccionConvocatorias() {
        convocatorias = new Hashtable<>();
    }

    /**
     * Devuelve el hashtable que contiene todas las convocatorias
     *
     * @return El hashtable<String, Convocatoria> con todas las convocatorias del sistema
     */
    protected Hashtable<String, Convocatoria> getHashtable() {
        return convocatorias;
    }

    /**
     * Devuelve el objeto convocatoria del identificador
     *
     * @param identificador Identificador de la convocatoria deseada
     * @return Objeto convocatoria que deseamos obtener, sera nulo si no existe
     */
    public Convocatoria getConvocatoria(String identificador) {
        if (!convocatorias.containsKey(identificador)) throw new IllegalArgumentException("Convocatoria no registrada");
        return convocatorias.get(identificador);
    }

    /**
     * Agrega la Convocatoria a la coleccion
     *
     * @param convocatoria Objeto convocatoria que deseamos agregar a la coleccion
     * @throws IllegalArgumentException si el id de la convocatoria corresponde a una ya registrada
     */
    public void addConvocatoria(Convocatoria convocatoria) throws IllegalArgumentException {
        if (convocatorias.containsKey(convocatoria.getId()))
            throw new IllegalArgumentException("Convocatoria ya registrada");
        convocatorias.put(convocatoria.getId(), convocatoria);
    }

    /**
     * Elimina una convocatoria del sistema
     *
     * @param convocatoria Objeto de la convocatoria a eliminar
     * @throws IllegalArgumentException si el id pasado no corresponde a una convocatoria registrada
     */
    public void removeConvocatoria(Convocatoria convocatoria, ColeccionPresentaciones presentaciones) throws IllegalArgumentException {
        if (!convocatorias.containsKey(convocatoria.getId()))
            throw new IllegalArgumentException("Convocatoria no registrada");
        convocatoria.eliminaSusPresentacionesDe(presentaciones);
        convocatorias.remove(convocatoria.getId());
    }

    /**
     * Devuelve como un LinkedList todas las convocatorias
     *
     * @return Un LinkedList<Convocatoria> con todas las convocatorias
     */
    public LinkedList<Convocatoria> getConvocatoriasLinkedList() {
        LinkedList<Convocatoria> convosLista = new LinkedList<>();
        Convocatoria convocatoria;
        for (String convocatoriaId : this.convocatorias.keySet()) {
            convocatoria = this.convocatorias.get(convocatoriaId);
            convosLista.add(convocatoria);
        }
        return convosLista;
    }

    /**
     * Devuelve como un LinkedList solo las convocatorias abiertas
     *
     * @return Un LinkedList<Convocatoria> con todas las convocatorias abiertas
     */
    public LinkedList<Convocatoria> getConvocatoriasAbiertasLinkedList() {
        LinkedList<Convocatoria> convosAbiertas = new LinkedList<>();
        Convocatoria convocatoria;
        for (String convocatoriaId : convocatorias.keySet()) {
            convocatoria = convocatorias.get(convocatoriaId);
            if (convocatoria.isAbierto()) convosAbiertas.add(convocatoria);
        }
        return convosAbiertas;
    }

    /**
     * Devuelve un Array con las convocatorias en orden por estado
     *
     * @return Un Convocatoria[] con todas las convocatorias ordenadas por estado
     */
    public Convocatoria[] getConvocatoriasPorEstadoArray() {
        // Ordenalas por estado de apertura
        Comparator<Convocatoria> porEstado = Comparator.comparing(Convocatoria::isAbierto).reversed();
        Convocatoria[] convocatoriasPorEstado = convocatorias.values().toArray(new Convocatoria[0]);
        Arrays.sort(convocatoriasPorEstado, porEstado);
        return convocatoriasPorEstado;
    }

    /**
     * Metodo para conseguir todas las convocatorias del sistema como un string
     *
     * @return String con TODAS las convocatorias del sistema con TODOS sus parametros
     */
    @Override
    public String toString() {
        StringBuilder usuariosString = new StringBuilder();
        for (String convoctoriaId : convocatorias.keySet()) {
            Convocatoria convocatoria = convocatorias.get(convoctoriaId);
            usuariosString.append(convocatoria.toString()).append("\n");
        }
        return usuariosString.toString();
    }

}
