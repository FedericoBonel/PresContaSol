package modelo.evento.convocatoria;

import modelo.evento.ColeccionDocumentos;
import modelo.evento.presentacion.ColeccionPresentaciones;
import modelo.evento.Evento;
import modelo.evento.presentacion.Presentacion;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Clase que abstrae a una convocatoria
 *
 * @author Bonel Federico
 */
public class Convocatoria extends Evento {

    /**
     * String constante que posee el error cuando la fecha es invalida
     */
    public static final String ERROR_FECHA_INVALIDA = "La fecha de cierre ingresada es invalida, debe ser despues de la de apertura";
    /**
     * String constante que posee el error cuando la descripcion es invalida
     */
    public static final String ERROR_DESCRIPCION_INVALIDA =
            "La descripcion ingresada es invalida, debe tener una longitud en caracteres de menos de ";

    /**
     * Opciones de todos los documentos base (No adicionales) del sistema
     */
    public static final String[] DOCUMENTOS_OPCIONES = {"Balance de Sumas y Saldos", "Libro Diario", "Libro Mayor",
            "Registro de Ingreso de Caja", "Registro de Movimientos de Bancos"};
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
     *                    en DOCUMENTOS_OPCIONES
     * @param descripcion Descripcion de la convocatoria: Puede tener hasta 2000 caracteres
     * @throws IllegalArgumentException si alguno de los parametros no cumple con los requisitos de formato
     */
    public Convocatoria(String id, LocalDate fechaInicio, LocalDate fechaCierre, LinkedList<String> docsReq, String descripcion)
            throws IllegalArgumentException {
        super(id.toLowerCase(), fechaInicio, (fechaInicio.isBefore(LocalDate.now().plusDays(1)) && fechaCierre.isAfter(LocalDate.now())), docsReq);
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
    public void setFechaCierre(LocalDate fechaCierre) throws IllegalArgumentException {
        //Si la fecha de cierre es antes que la fecha de inicio tira una excepcion
        if (fechaCierre.isBefore(super.getFechaInicio())) throw new IllegalArgumentException(ERROR_FECHA_INVALIDA);
        if (fechaCierre.isBefore(LocalDate.now())) setAbierto(false);
        this.fechaCierre = fechaCierre;
    }

    /**
     * Inicializa la coleccion de documentos que tendra todos los documentos requeridos de la convocatoria
     *
     * @param documentos Documentos requeridos como un LinkedList
     * @throws IllegalArgumentException si los documentos no son contenidos en la lista de opciones
     */
    @Override
    protected ColeccionDocumentos incializaDocumentos(LinkedList<String> documentos) throws IllegalArgumentException {
        LinkedList<String> opciones = new LinkedList<>(Arrays.stream(DOCUMENTOS_OPCIONES).toList());
        return new ColeccionDocumentos(documentos, opciones);
    }

    /**
     * Indica si el documento especificado es requerido en la convocatoria
     *
     * @param documento Documento a verificar su estado
     * @return Estado de requerimiento del documento
     */
    public Boolean isRequerido(String documento) {
        return super.containsDocumento(documento);
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
    public void setDescripcion(String descripcion) throws IllegalArgumentException {
        if (descripcion.length() > LIMITE_DESCRIPCION) throw new IllegalArgumentException(ERROR_DESCRIPCION_INVALIDA + LIMITE_DESCRIPCION);
        this.descripcion = descripcion;
    }

    /**
     * Devuelve todas las presentaciones que se realizaron para esta convocatoria de la colección especificada
     * @param presentaciones Coleccion de presentaciones del sistema
     * @return un LinkedList con todas las presentaciones realizadas para esta convocatoria
     */
    public LinkedList<Presentacion> getSusPresentacionesDe(ColeccionPresentaciones presentaciones) {
        LinkedList<Presentacion> presentacionesDeConvocatoria = new LinkedList<>();
        for (Presentacion presentacion : presentaciones.getPresentacionesLinkedList()) {
            if (presentacion.isConvocatoria(this)) presentacionesDeConvocatoria.add(presentacion);
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
                ", documentos=" + super.getDocumentos() +
                ", fechaInicio=" + super.getFechaInicio() +
                ", fechaCierre=" + fechaCierre +
                ", descripcion=" + descripcion +
                ", abierto=" + super.isAbierto() +
                "} ";
    }
}
