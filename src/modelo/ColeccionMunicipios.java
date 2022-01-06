package modelo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Estructura de datos que contiene a todos los municipios del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionMunicipios {

    /**
     * String constante que pose el error cuando el municipio esta registrado
     */
    public static final String ERROR_MUNICIPIO_REGISTRADO = "Municipio ya registrado";
    /**
     * String constante que pose el error cuando el municipio no esta registrado
     */
    public static final String ERROR_MUNICIPIO_NO_REGISTRADO = "Municipio no registrado";

    /**
     *  LinkedList que contiene a todos los municipios
     */
    private final LinkedList<Municipio> municipios;

    /**
     * Constructor que inicializa la coleccion de municipios
     */
    public ColeccionMunicipios(){
        municipios = new LinkedList<>();
    }

    /**
     * Devuelve el objeto municipio del identificador
     * @param identificador Identificador del municipio deseado
     * @return Objeto municipio que deseamos obtener, sera nulo si no existe
     * @throws IllegalArgumentException Si el municipio no estaba registrado
     */
    public Municipio getMunicipio(String identificador) {
        Municipio municipioExistente = buscarMunicipio(identificador);
        if (municipioExistente == null) throw new IllegalArgumentException(ERROR_MUNICIPIO_NO_REGISTRADO);
        return municipioExistente;
    }

    /**
     * Agrega el municipio a la coleccion
     * @param municipio Objeto municipio que deseamos agregar a la coleccion
     * @throws IllegalArgumentException si el id del municipio corresponde a uno ya registrado
     */
    public void addMunicipio(Municipio municipio) {
        Municipio municipioExistente = buscarMunicipio(municipio.getId());
        if (municipioExistente != null) throw new IllegalArgumentException(ERROR_MUNICIPIO_REGISTRADO);
        municipios.add(municipio);
    }

    /**
     * Elimina un municipio del sistema
     * @param municipio Municipio a eliminar
     * @param presentaciones Coleccion de presentaciones del sistema
     * @throws IllegalArgumentException si el id pasado no corresponde a un municipio registrado
     */
    public void removeMunicipio(Municipio municipio, ColeccionPresentaciones presentaciones) {
        Municipio municipioExistente = buscarMunicipio(municipio.getId());
        if (municipioExistente == null) throw new IllegalArgumentException(ERROR_MUNICIPIO_NO_REGISTRADO);
        municipio.abandonaSupervisor();
        municipio.eliminaSusPresentacionesDe(presentaciones);
        municipio.abandonaRepresentante();
        municipios.remove(municipio);
    }

    /**
     * Devuelve como un LinkedList todos los municipios
     * @return LinkedList con todos los municipios del sistema
     */
    public LinkedList<Municipio> getMunicipiosLinkedList() {
        return new LinkedList<>(municipios);
    }

    /**
     * Metodo para conseguir todos los municipios del sistema como un string
     * @return String con TODOS los municipios del sistema con TODOS sus parametros
     */
    @Override
    public String toString() {
        StringBuilder munucipiosString = new StringBuilder();
        for (Municipio municipio : municipios) {
            munucipiosString.append(municipio.toString()).append("\n");
        }
        return munucipiosString.toString();
    }

    /**
     * Metodo que busca municipios en la coleccion por el identificador pasado
     *
     * @param identificador identificador del municipio a buscar
     * @return Municipio con ese identificador en caso de existir, null en caso contrario
     */
    private Municipio buscarMunicipio(String identificador){
        for (Municipio municipio : municipios) {
            if (municipio.getId().equals(identificador.toLowerCase())) return municipio;
        }
        return null;
    }
}
