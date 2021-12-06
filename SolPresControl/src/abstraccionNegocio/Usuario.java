package abstraccionNegocio;

import java.util.LinkedList;

/**
 * Clase que abstrae a todos los usuarios del sistema
 */
public abstract class Usuario extends Entidad {
    /**
     * Limites en formato de clave (en cantidad de caracteres)
     */
    private final int LIMITE_CLAVE_INFERIOR = 4;
    private final int LIMITE_CLAVE_SUPERIOR = 8;
    /**
     * Limites en formato de identificador (en cantidad de caracteres)
     */
    private final int LIMITE_IDENTIFICADOR_INFERIOR = 1;
    private final int LIMITE_IDENTIFICADOR_SUPERIOR = 10;
    /**
     * Clave y tipo del usuario
     */
    private String clave;

    /**
     * Constructor de usuario, nombre de usuario es el id en este caso
     *
     * @param usuario Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave   Clave usada por el usuario para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @throws IllegalArgumentException Si algun parametro no cumple con los requisitos de formato
     */
    public Usuario(String usuario, String clave) throws IllegalArgumentException {
        super(usuario);
        setClave(clave);
    }

    /**
     * Devuelve la clave del usuario
     *
     * @return La clave del usuario sobre el que se llame
     */
    protected String getClave() {
        return clave;
    }

    /**
     * Asigna una nueva clave al usuario
     *
     * @param clave nueva clave a asignar
     * @throws IllegalArgumentException si la clave no cumple el formato requerido
     */
    protected void setClave(String clave) throws IllegalArgumentException {
        if (!cumpleFormatoClave(clave)) throw new IllegalArgumentException("Clave no valida");
        this.clave = clave;
    }

    /**
     * Verifica si el formato de clave se cumple en el String pasado
     *
     * @param clave clave a verificar
     * @return true si el formato es cumplido, false de otra forma
     */
    private boolean cumpleFormatoClave(String clave) {
        return LIMITE_CLAVE_INFERIOR <= clave.length() && clave.length() <= LIMITE_CLAVE_SUPERIOR;
    }

    /**
     * Verifica si la clave pasada como string se corresponde con la del usuario
     *
     * @param clave Clave a verificar
     * @return Verdadero si se corresponden, falso en caso contrario
     */
    public boolean certificaClave(String clave) {
        return clave.equals(this.clave);
    }

    /**
     * Devuelve las convocatorias que deben ser visibles para el usuario de todas las que se encuentran en la coleccion pasada
     *
     * @param convocatorias coleccion de todas las convocatorias del sistema
     * @return Un linked list con todas las convocatorias que corresponden al usuario sobre el que se llame
     */
    public LinkedList<Convocatoria> getConvocatoriasVisibles(ColeccionConvocatorias convocatorias) {
        return convocatorias.getConvocatoriasLinkedList();
    }

    /**
     * Devuelve las presentaciones que deben ser visibles para el usuario para el usuario de todas las que se encuentran en la coleccion
     *
     * @param presentaciones Todas las presentaciones del sistema
     * @return Un linked list con todas las presentaciones que corresponden al usuario
     */
    public abstract LinkedList<Presentacion> getPresentacionesVisibles(ColeccionPresentaciones presentaciones);

    /**
     * Elimina la presentacion de la coleccion de presentaciones pasada
     *
     * @param presentacion   Presentacion que se desea eliminar
     * @param presentaciones Todas las presentaciones del sistema
     * @throws IllegalCallerException   si el usuario no puede realizar esta llamada
     * @throws IllegalArgumentException si el id no corresponde a una presentacion registrada
     */
    public void eliminaPresentacion(Presentacion presentacion, ColeccionPresentaciones presentaciones)
            throws IllegalCallerException, IllegalArgumentException {
        if (!puedeEliminarPresentacion(presentacion))
            throw new IllegalCallerException("Operacion invalida para usuario");
        presentaciones.removePresentacion(presentacion);
    }

    /**
     * Verifica si la presentacion puede ser eliminada por el usuario
     *
     * @param presentacion Presentacion a verificar
     * @return true si el usuario puede eliminar la presentacion, false de otra forma
     */
    protected abstract boolean puedeEliminarPresentacion(Presentacion presentacion);

    /**
     * Establece el documento pasado como entregado en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     * @throws IllegalCallerException si el usuario no puede realizar esta llamada
     */
    public void entregaDocumentoA(Presentacion presentacion, String documento) throws IllegalCallerException {
        if (!puedeEntregarDocumentoA(presentacion, documento))
            throw new IllegalCallerException("Operacion invalida para usuario");
        presentacion.marcaElDocumentoComoVerdadero(documento);
    }

    /**
     * Verifica si el usuario puede entregar el documento en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     * @return true si el usuario puede entregarlo, false de otra forma
     */
    protected abstract boolean puedeEntregarDocumentoA(Presentacion presentacion, String documento);

    /**
     * Retira el documento de la presentacion (i.e. se marca como no entregado)
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     * @throws IllegalCallerException si el usuario no puede realizar esta llamada
     */
    public void retiraDocumentoDe(Presentacion presentacion, String documento) throws IllegalCallerException {
        if (!puedeRetirarDocumentoDe(presentacion, documento))
            throw new IllegalCallerException("Operacion invalida para usuario");
        // Sacalo si es adicional o marcalo como no entregado si esta en la lista de opciones
        presentacion.marcaDocumentoComoFalso(documento);
    }

    /**
     * Verifica si el usuario puede retirar el documento de la presentacion
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     * @return true si el usuario puede retirarlo, false de otra forma
     */
    protected abstract boolean puedeRetirarDocumentoDe(Presentacion presentacion, String documento);

    /**
     * Devuelve los municipios que deben ser visibles para el usuario de todos las que se encuentran en la coleccion
     *
     * @param municipios Todas los municipios del sistema
     * @return Un linked list con todos los municipios que corresponden al usuario
     */
    public abstract LinkedList<Municipio> getMunicipiosVisibles(ColeccionMunicipios municipios);

    /**
     * Implementacion del verificador de formato que debe cumplirse para Identificador
     *
     * @param identificador identificador a verificar
     * @return true si el formato es cumplido, false de otra forma
     */
    @Override
    protected boolean cumpleFormatoId(String identificador) {
        return LIMITE_IDENTIFICADOR_INFERIOR <= identificador.length() && identificador.length() <= LIMITE_IDENTIFICADOR_SUPERIOR;
    }

    /**
     * Devuelve los parametros como string a mostrar a los administradores
     *
     * @return Un string con todos los parametros del objeto
     */
    public String toStringConClave() {
        return "{" +
                "nombre de usuario:" + super.toString() +
                ", clave='" + clave + '\'' +
                "} ";
    }

    /**
     * Devuelve los parametros como string a mostrar a los usuarios
     *
     * @return El identificador del usuario
     */
    @Override
    public String toString() {
        return "{" +
                "nombre de usuario:" + super.toString() +
                ", clave='" + "ESCONDIDA" + '\'' +
                "} ";
    }
}
