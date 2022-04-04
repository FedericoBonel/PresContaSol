package com.fedebonel.modelo.evento;

import com.fedebonel.modelo.Entidad;

import java.time.LocalDate;
import java.util.LinkedList;

/**
 * Clase que abstrae a los eventos del sistema (i.e. convocatoria y presentacion)
 *
 * @author Bonel Federico
 */
public abstract class Evento extends Entidad {

    /**
     * Strings final que posee el error cuando la fecha inicial es invalida
     */
    public static final String ERROR_FECHA_INVALIDA = "La fecha inicial ingresada es invalida";

    /**
     * Limite inferior de la longitud del identificador
     */
    private final static int LIMITE_IDENTIFICADOR_INFERIOR = 1;
    /**
     * Limite superior de la longitud del identificador
     */
    private final static int LIMITE_IDENTIFICADOR_SUPERIOR = 100;
    /**
     * Documentos requeridos o entregados
     */
    private final ColeccionDocumentos documentos;
    /**
     * Fecha de inicio del evento, sea esta la fecha de creacion (Presentaciones) o apertura (Convocatorias)
     */
    private LocalDate fechaInicio;
    /**
     * Estado del evento, puede estar -> abierto:true, cerrado:false
     */
    private boolean abierto;

    /**
     * Constructor de evento
     *
     * @param id          Identificador alfanumerico unico de municipio: Puede tener desde 1 caracter hasta 100 caracteres
     * @param fechaInicio Fecha de apertura o creacion como objeto LocalDate
     * @param isAbierto   Estado de apertura del evento
     * @param documentos  Documentos requeridos o entregados del evento como un LinkedList
     * @throws IllegalArgumentException si alguno de los parametros no cumple con los requisitos del formato
     */
    public Evento(String id, LocalDate fechaInicio, boolean isAbierto, LinkedList<String> documentos) throws IllegalArgumentException {
        super(id);
        setFechaInicio(fechaInicio);
        this.documentos = incializaDocumentos(documentos);
        this.abierto = isAbierto;
    }

    /**
     * Devuelve la fecha de apertura o creacion
     *
     * @return LocalDate que representa la fecha de Apertura o Creacion
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Asigna nueva fecha de inicio
     *
     * @param nuevaFechaInicio Fecha de inicio como LocalDate a asignar
     * @throws IllegalArgumentException si la fecha de inicio es invalida
     */
    public void setFechaInicio(LocalDate nuevaFechaInicio) throws IllegalArgumentException {
        if (!cumpleFormatoFechaInicio(nuevaFechaInicio)) throw new IllegalArgumentException(ERROR_FECHA_INVALIDA);
        if (nuevaFechaInicio.isAfter(LocalDate.now())) setAbierto(false);
        fechaInicio = nuevaFechaInicio;
    }

    /**
     * Verifica que la fecha de inicio sea valida
     *
     * @param fechaInicio Fecha de inicio como Objeto LocalDate a verificar
     * @return Si fechaInicio pasada es valida o no
     */
    protected abstract boolean cumpleFormatoFechaInicio(LocalDate fechaInicio);

    /**
     * Devuelve los documentos requeridos/entregados para el evento
     *
     * @return Una coleccion de documentos con todos los documentos del evento
     */
    public ColeccionDocumentos getDocumentos() {
        return documentos;
    }

    /**
     * Inicializa la coleccion de documentos que tendra todos los documentos del evento
     *
     * @param documentos Documentos requeridos o entregados como un LinkedList
     * @return Coleccion con todos los documentos requeridos o entregados
     * @throws IllegalArgumentException si los documentos son invalidos para el evento en cuestion
     */
    protected abstract ColeccionDocumentos incializaDocumentos(LinkedList<String> documentos) throws IllegalArgumentException;

    /**
     * Agrega un nuevo documento con un nuevo estado de requerido o entregado
     *
     * @param documento Documento a agregar
     * @throws IllegalArgumentException Si el documento es invalido para el evento en cuestion o ya esta agregado
     */
    public void addDocumento(String documento) throws IllegalArgumentException {
        documentos.addDocumento(documento);
    }

    /**
     * Remueve el documento de la lista de documentos del evento
     *
     * @param documento Documento a remover
     * @throws IllegalArgumentException Si el documento no esta agregado
     */
    public void removeDocumento(String documento) throws IllegalArgumentException {
        documentos.removeDocumento(documento);
    }

    /**
     * Verifica si el documento esta contenido en la coleccion de documentos
     *
     * @param documento Documento a verificar
     * @return verdadero si es contenido, falso en caso contrario
     */
    public Boolean containsDocumento(String documento) {
        return documentos.containsDocumento(documento);
    }

    /**
     * Devuelve el estado que indica si el evento sobre el que se llama esta abierto o no
     *
     * @return verdadero si esta abierto, falso en caso contrario
     */
    public boolean isAbierto() {
        return abierto;
    }

    /**
     * Asigna un nuevo estado de apertura
     *
     * @param isAbierto nuevo estado de apertura: abierto = true, cerrado = falso
     */
    public void setAbierto(boolean isAbierto) {
        this.abierto = isAbierto;
    }

    /**
     * Implementacion del formato que debe cumplirse
     *
     * @param identificador identificador a verificar
     * @return true si el formato es cumplido, false de otra forma
     */
    @Override
    protected boolean cumpleFormatoId(String identificador) {
        return LIMITE_IDENTIFICADOR_INFERIOR <= identificador.length() && identificador.length() <= LIMITE_IDENTIFICADOR_SUPERIOR;
    }

    /**
     * Devuelve todos los atributos de la instancia como string
     *
     * @return Todos los atributos de la instancia como un string
     */
    @Override
    public String toString() {
        return "{" +
                "identificador:" + super.toString() +
                ", documentos=" + documentos +
                ", fechaInicio=" + fechaInicio +
                ", abierto=" + abierto +
                "} ";
    }
}
