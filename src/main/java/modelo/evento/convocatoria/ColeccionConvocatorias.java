package modelo.evento.convocatoria;

import modelo.evento.presentacion.ColeccionPresentaciones;

import java.util.*;

/**
 * Estructura de datos que contiene a todas las convocatorias del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionConvocatorias {

    /**
     * String constante que posee el error cuando la convocatoria esta registrada
     */
    public static final String ERROR_CONVOCATORIA_REGISTRADA = "Convocatoria ya registrada";
    /**
     * String constante que posee el error cuando la convocatoria no esta registrada
     */
    public static final String ERROR_CONVOCATORIA_NO_REGISTRADA = "Convocatoria no registrada";

    /**
     * LinkedList que contiene a todas las convocatorias
     */
    private final LinkedList<Convocatoria> convocatorias;

    /**
     * Constructor que inicializa la coleccion de convocatorias
     */
    public ColeccionConvocatorias() {
        convocatorias = new LinkedList<>();
    }

    /**
     * Devuelve el objeto convocatoria del identificador
     *
     * @param identificador Identificador de la convocatoria deseada
     * @return Objeto convocatoria que deseamos obtener, sera nulo si no existe
     */
    public Convocatoria getConvocatoria(String identificador) {
        Convocatoria convocatoriaExistente = buscarConvocatoria(identificador);
        if (convocatoriaExistente == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        return convocatoriaExistente;
    }

    /**
     * Agrega la Convocatoria a la coleccion
     *
     * @param convocatoria Objeto convocatoria que deseamos agregar a la coleccion
     * @throws IllegalArgumentException si el id de la convocatoria corresponde a una ya registrada
     */
    public void addConvocatoria(Convocatoria convocatoria) throws IllegalArgumentException {
        Convocatoria convocatoriaExistente = buscarConvocatoria(convocatoria.getId());
        if (convocatoriaExistente != null)
            throw new IllegalArgumentException(ERROR_CONVOCATORIA_REGISTRADA);
        convocatorias.add(convocatoria);
    }

    /**
     * Elimina una convocatoria del sistema
     *
     * @param convocatoria Objeto de la convocatoria a eliminar
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @throws IllegalArgumentException si el id pasado no corresponde a una convocatoria registrada
     */
    public void removeConvocatoria(Convocatoria convocatoria, ColeccionPresentaciones presentaciones) throws IllegalArgumentException {
        Convocatoria convocatoriaExistente = buscarConvocatoria(convocatoria.getId());
        if (convocatoriaExistente == null)
            throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoria.eliminaSusPresentacionesDe(presentaciones);
        convocatorias.remove(convocatoria);
    }

    /**
     * Devuelve como un LinkedList todas las convocatorias
     *
     * @return Un LinkedList con todas las convocatorias
     */
    public LinkedList<Convocatoria> getConvocatoriasLinkedList() {
        return new LinkedList<>(convocatorias);
    }

    /**
     * Devuelve como un LinkedList solo las convocatorias abiertas
     *
     * @return Un LinkedList con todas las convocatorias abiertas
     */
    public LinkedList<Convocatoria> getConvocatoriasAbiertasLinkedList() {
        LinkedList<Convocatoria> convosAbiertas = new LinkedList<>();
        for (Convocatoria convocatoria : convocatorias) {
            if (convocatoria.isAbierto()) convosAbiertas.add(convocatoria);
        }
        return convosAbiertas;
    }

    /**
     * Metodo para conseguir todas las convocatorias del sistema como un string
     *
     * @return String con TODAS las convocatorias del sistema con TODOS sus parametros
     */
    @Override
    public String toString() {
        StringBuilder usuariosString = new StringBuilder();
        for (Convocatoria convocatoria : convocatorias) {
            usuariosString.append(convocatoria.toString()).append("\n");
        }
        return usuariosString.toString();
    }

    /**
     * Metodo que busca convocatorias en la coleccion por identificador
     *
     * @param identificador identificador de la convocatoria a buscar
     * @return Convocatoria con ese identificador en caso de existir, null en caso contrario
     */
    private Convocatoria buscarConvocatoria(String identificador){
        for (Convocatoria convocatoria : convocatorias) {
            if (convocatoria.getId().equals(identificador.toLowerCase())) return convocatoria;
        }
        return null;
    }

}
