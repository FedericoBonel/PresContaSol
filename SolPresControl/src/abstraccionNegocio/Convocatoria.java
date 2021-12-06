package abstraccionNegocio;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Clase que abstrae a una convocatoria
 *
 * @author Bonel Federico
 */
public class Convocatoria extends Evento {
    /**
     * Limite de longitud en caracteres de la descripcion
     */
    private static final int LIMITE_DESCRIPCION = 2000;
    /**
     * Fecha de cierre de la convocatoria
     */
    private LocalDate fechaCierre;
    /**
     * Descripcion de la convocatoria
     */
    private String descripcion;

    /**
     * Constructor de convocatoria
     *
     * @param id          Identificador alfanumerico unico de convocatoria: Puede tener desde 1 caracter hasta 100 caracteres
     * @param fechaInicio Fecha de apertura planeada como objeto LocalDate
     * @param fechaCierre Fecha de cierre de la convocatoria como objeto LocalDate: No puede ser antes de fechaInicio
     * @param docsReq     Documentos requeridos de la convocatoria para las presentaciones: Solo pueden ser los establecidos
     *                    en Evento.DOCUMENTOS_OPCIONES
     * @throws IllegalArgumentException si alguno de los parametros no cumple con los requisitos de formato
     */
    public Convocatoria(String id, LocalDate fechaInicio, LocalDate fechaCierre, Hashtable<String, Boolean> docsReq, String descripcion)
            throws IllegalArgumentException {
        super(id, fechaInicio, (fechaInicio.isBefore(LocalDate.now()) && fechaCierre.isAfter(LocalDate.now())), docsReq);
        setFechaCierre(fechaCierre);
        setDescripcion(descripcion);
    }

    /**
     * Devuelve siempre true para las convocatorias
     * Si son en el pasado ya estan abiertas, si son el futuro estan planeadas
     *
     * @param fechaInicio Objeto LocalDate a verificar
     * @return Verdadero si la fecha de cierre es despues de la fecha de inicio, falso en caso contrario
     */
    @Override
    protected boolean cumpleFormatoFechaInicio(LocalDate fechaInicio) {
        return fechaCierre == null || !fechaCierre.isBefore(fechaInicio);
    }

    /**
     * Devuelve la fecha de cierre de la convocatoria
     *
     * @return Fecha de cierre como un objeto Local date
     */
    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    /**
     * Asigna la fecha de cierre especificada a la convocatoria
     *
     * @param fechaCierre nueva fecha de cierre como objeto LocalDate
     * @throws IllegalArgumentException Si la fecha de cierre es antes de la fecha de inicio
     */
    protected void setFechaCierre(LocalDate fechaCierre) throws IllegalArgumentException {
        //Si la fecha de cierre es antes que la fecha de inicio tira una excepcion
        if (fechaCierre.isBefore(super.getFechaInicio())) throw new IllegalArgumentException("Fecha incorrecta");
        this.fechaCierre = fechaCierre;
    }

    /**
     * Verifica que el documento requerido pasado sea valido para las convocatorias
     *
     * @param documento Nombre del documento a verificar
     * @return Verdadero si el documento esta en la lista de opciones, falso en caso contrario
     */
    @Override
    protected boolean validaDocumento(String documento) {
        return (Arrays.binarySearch(DOCUMENTOS_OPCIONES, documento) >= 0);
    }

    /**
     * Indica si el documento especificado es requerido en la convocatoria
     *
     * @param documento Documento a verificar su estado
     * @return Estado de requerimiento del documento
     */
    public Boolean isRequerido(String documento) {
        if (!super.getDocumentos().containsKey(documento)) return false;
        return super.getDocumento(documento);
    }

    /**
     * Devuelve la descripcion de la convocatoria
     *
     * @return Descripcion como un String
     */
    public String getDescripcion() {
        return descripcion;
    }


    /**
     * Asigna la descripcion especificada a la convocatoria
     *
     * @param descripcion Descripcion a asignar como String
     * @throws IllegalArgumentException Si la descripcion es mayor al limite en longitud = 2000 caracteres
     */
    protected void setDescripcion(String descripcion) throws IllegalArgumentException {
        if (descripcion.length() > LIMITE_DESCRIPCION)
            throw new IllegalArgumentException("La descripcion excede el limite de caracteres");
        this.descripcion = descripcion;
    }

    /**
     * Devuelve todas las presentaciones que se realizaron para esta convocatoria de la colección especificada
     *
     * @return un LinkedList con todas las presentaciones realizadas para esta convocatoria
     */
    protected LinkedList<Presentacion> getSusPresentacionesDe(ColeccionPresentaciones presentaciones) {
        LinkedList<Presentacion> presentacionesDeConvocatoria = new LinkedList<>();
        Presentacion presentacionActual;
        for (String presentacionId : presentaciones.getHashtable().keySet()) {
            presentacionActual = presentaciones.getPresentacion(presentacionId);
            if (presentacionActual.isConvocatoria(this)) presentacionesDeConvocatoria.add(presentacionActual);
        }
        return presentacionesDeConvocatoria;
    }

    /**
     * Remueve todas las presentaciones de la convocatoria de la coleccion pasada
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     */
    protected void eliminaSusPresentacionesDe(ColeccionPresentaciones presentaciones) {
        // Toma las presentaciones realizadas para esta convocatoria
        LinkedList<Presentacion> presentacionesPropias = getSusPresentacionesDe(presentaciones);
        // Por cada presentacion de la convocatoria
        for (Presentacion presentacionARemover : presentacionesPropias) {
            // Remuevela de la coleccion
            presentaciones.removePresentacion(presentacionARemover);
        }
    }

    /**
     * Devuelve todos los atributos de la instancia como string
     *
     * @return Todos los atributos como un string
     */
    @Override
    public String toString() {
        return "{" +
                "identificador:" + super.getId() +
                ", documentos=" + super.documentosVerdaderosToString() +
                ", fechaInicio=" + super.getFechaInicio() +
                ", fechaCierre=" + fechaCierre +
                ", descripcion=" + descripcion +
                ", abierto=" + super.isAbierto() +
                "} ";
    }
}
