package abstraccionNegocio;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Estructura de datos que contiene a todos los municipios del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionMunicipios {
    /**
     *  Hashtable que contiene a todos los municipios
     */
    private final Hashtable<String, Municipio> municipios;

    /**
     * Constructor que inicializa la coleccion de municipios
     */
    public ColeccionMunicipios(){
        municipios = new Hashtable<>();
    }

    /**
     * Devuelve el hashtable que contiene todos los municipios
     * @return El hashtable<String, Municipio> con todos los municipios del sistema
     */
    protected Hashtable<String, Municipio> getHashtable(){
        return municipios;
    }

    /**
     * Devuelve el objeto municipio del identificador
     * @param identificador Identificador del municipio deseado
     * @return Objeto municipio que deseamos obtener, sera nulo si no existe
     */
    public Municipio getMunicipio(String identificador) {
        if (!municipios.containsKey(identificador)) throw new IllegalArgumentException("Municipio no registrado");
        return municipios.get(identificador);
    }

    /**
     * Agrega el municipio a la coleccion
     * @throws IllegalArgumentException si el id del municipio corresponde a uno ya registrado
     * @param municipio Objeto municipio que deseamos agregar a la coleccion
     */
    public void addMunicipio(Municipio municipio) {
        if (municipios.containsKey(municipio.getId())) throw new IllegalArgumentException("Municipio ya registrado");
        municipios.put(municipio.getId(), municipio);
    }

    /**
     * Elimina un municipio del sistema
     * @throws IllegalArgumentException si el id pasado no corresponde a un municipio registrado
     * @param municipio Municipio a eliminar
     */
    public void removeMunicipio(Municipio municipio, ColeccionPresentaciones presentaciones, ColeccionUsuarios usuarios) {
        if (!municipios.containsKey(municipio.getId())) throw new IllegalArgumentException("Municipio no registrado");
        municipio.removeFiscal();
        municipio.eliminaSusPresentacionesDe(presentaciones);
        municipio.abandonaSuRepresentanteEn(usuarios);
        municipios.remove(municipio.getId());
    }

    /**
     * Devuelve como un LinkedList todos los municipios
     * @return LinkedList con todos los municipios del sistema
     */
    public LinkedList<Municipio> getMunicipiosLinkedList() {
        LinkedList<Municipio> municipiosLista = new LinkedList<>();
        for (String municipioId : this.municipios.keySet()) {
            municipiosLista.add(this.municipios.get(municipioId));
        }
        return municipiosLista;
    }

    /**
     * Metodo para conseguir todos los municipios del sistema como un string
     * @return String con TODOS los municipios del sistema con TODOS sus parametros
     */
    @Override
    public String toString() {
        StringBuilder usuariosString = new StringBuilder();
        for (String municipioId : municipios.keySet()) {
            Municipio municipio = municipios.get(municipioId);
            usuariosString.append(municipio.toString()).append("\n");
        }
        return usuariosString.toString();
    }

}
