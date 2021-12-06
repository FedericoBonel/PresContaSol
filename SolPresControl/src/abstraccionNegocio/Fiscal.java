package abstraccionNegocio;

import java.util.LinkedList;

/**
 * Clase que abstrae a todos los Fiscales del sistema
 *
 * @author Bonel Federico
 */

public class Fiscal extends Usuario {
    /**
     * Constante que mantiene el tipo de usuario para el metodo to String
     */
    private static final String TIPO_FISCAL = "Fiscal";

    /**
     * Constructor de usuario Fiscal
     *
     * @param usuario Identificador del usuario: Debe tener entre 1 y 10 caracteres inclusive
     * @param clave   Clave a asignar para el ingreso al sistema del usuario: Debe tener entre 4 y 8 caracteres inclusive
     * @throws IllegalArgumentException Si alguno de los parametros no cumple con los requerimientos de formato
     */
    public Fiscal(String usuario, String clave) throws IllegalArgumentException {
        super(usuario, clave);
    }

    /**
     * Devuelve las presentaciones que deben ser visibles para los fiscales, estas son:
     * Todas las presentaciones de sus municipios, ignora todas las presentaciones del resto de municipios
     * (Incluso aquellas que en el pasado pudo tener asignadas, ya no lo estan asi que ya no se ven)
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @return Un linked list con todas las presentaciones que son visibles por el fiscal
     */
    @Override
    public LinkedList<Presentacion> getPresentacionesVisibles(ColeccionPresentaciones presentaciones) {
        LinkedList<Presentacion> presentacionesVisibles = new LinkedList<>();
        Presentacion presentacionActual;
        for (String presentacionId : presentaciones.getHashtable().keySet()) {
            presentacionActual = presentaciones.getHashtable().get(presentacionId);
            if (presentacionActual.getMunicipio().isFiscal(this)) presentacionesVisibles.add(presentacionActual);
        }
        return presentacionesVisibles;
    }

    /**
     * Verifica si el fiscal puede eliminar la presentacion
     *
     * @param presentacion Presentacion a verificar
     * @return false, el Fiscal nunca puede eliminar las presentaciones
     */
    @Override
    protected boolean puedeEliminarPresentacion(Presentacion presentacion) {
        return false;
    }

    /**
     * Verifica si el fiscal puede entregar el documento en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     * @return false, el fiscal nunca puede entregar documentos
     */
    @Override
    protected boolean puedeEntregarDocumentoA(Presentacion presentacion, String documento) {
        return false;
    }

    /**
     * Verifica si el Fiscal puede retirar el documento de la presentacion
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     * @return false, el fiscal nunca puede retirar documentos
     */
    @Override
    protected boolean puedeRetirarDocumentoDe(Presentacion presentacion, String documento) {
        return false;
    }


    /**
     * Devuelve los municipios que deben ser visibles para el Fiscal, estos son:
     * Todos los municipios del sistema
     *
     * @param municipios Coleccion de todos los municipios del sistema
     * @return Un linked list con los municipios que corresponden al usuario Fiscal
     */
    @Override
    public LinkedList<Municipio> getMunicipiosVisibles(ColeccionMunicipios municipios) {
        return municipios.getMunicipiosLinkedList();
    }

    /**
     * Quita todas las relaciones a este Fiscal de todos los municipios
     */
    protected void abandonaSusMunicipiosEn(ColeccionMunicipios municipios) {
        Municipio municipioActual;
        // Busca todos los municipios que referencian a este fiscal y remueve la referencia
        for (String municipioId : municipios.getHashtable().keySet()) {
            municipioActual = municipios.getMunicipio(municipioId);
            if (municipioActual.isFiscal(this)) municipioActual.removeFiscal();
        }
    }

    @Override
    public String toStringConClave() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + super.getClave() + '\'' +
                ", tipo='" + TIPO_FISCAL + '\'' +
                "} ";
    }

    // Devuelve como string los atributos de la instancia
    @Override
    public String toString() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + "ESCONDIDA" + '\'' +
                ", tipo='" + TIPO_FISCAL + '\'' +
                "} ";
    }


}
