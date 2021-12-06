package abstraccionNegocio;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Clase que abstrae a los eventos del sistema (i.e. convocatoria y presentacion)
 *
 * @author Bonel Federico
 */
public abstract class Evento extends Entidad {
    /**
     * Limites en formato de identificador (en cantidad de caracteres)
     */
    private final int LIMITE_IDENTIFICADOR_INFERIOR = 1;
    private final int LIMITE_IDENTIFICADOR_SUPERIOR = 100;
    /**
     * Opciones de todos los documentos base (No adicionales) del sistema
     */
    public static final String[] DOCUMENTOS_OPCIONES = {"Balance de Sumas y Saldos", "Libro Diario", "Libro Mayor",
            "Registro de Ingreso de Caja", "Registro de Movimientos de Bancos"};
    /**
     * Documentos requeridos o entregados (Clave) para este evento con su estado: de requerido o entregado (Valor)
     */
    private Hashtable<String, Boolean> documentos;
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
     * @param documentos  Documentos requeridos o entregados del evento como un Hashtable (Documento : Es requerido o Entregado)
     * @throws IllegalArgumentException si alguno de los parametros no cumple con los requisitos del formato
     */
    public Evento(String id, LocalDate fechaInicio, boolean isAbierto, Hashtable<String, Boolean> documentos) throws IllegalArgumentException {
        super(id);
        setFechaInicio(fechaInicio);
        setDocumentos(documentos);
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
    protected void setFechaInicio(LocalDate nuevaFechaInicio) throws IllegalArgumentException {
        if (!cumpleFormatoFechaInicio(nuevaFechaInicio)) throw new IllegalArgumentException("Fecha Inicio no valida");
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
     * @return Un Hashtable (String documento, Boolean estado de requerido / entregado) con todos los documentos del evento
     */
    protected Hashtable<String, Boolean> getDocumentos() {
        return documentos;
    }

    /**
     * Asigna cada uno de los documentos del hashtable pasado a los del evento
     *
     * @param documentos Documentos como hashtable (Clave: Nombre Documento, Valor booleano: estado de Requerido o Entregado) a asignar
     * @throws IllegalArgumentException si los documentos son invalidos para el evento en cuestion
     */
    private void setDocumentos(Hashtable<String, Boolean> documentos) throws IllegalArgumentException {
        // Si alguno es invalido lanza una excepcion
        for (String documentoId : documentos.keySet()) {
            if (!validaDocumento(documentoId)) throw new IllegalArgumentException("Documentos invalidos");
        }
        this.documentos = documentos;
    }

    /**
     * Devuelve el valor booleano asignado al documento dentro del Hashtable de documentos
     *
     * @param documento Documento a verificar su estado
     * @return Estado de requerimiento/entrega del documento
     */
    protected Boolean getDocumento(String documento) {
        return documentos.get(documento);
    }

    /**
     * Actualiza el hashtable de documentos con un nuevo valor de un documento ya existente
     * o agrega un nuevo documento con un nuevo estado de requerido o entregado
     *
     * @param documento Documento a agregar
     * @throws IllegalArgumentException Si el documento es invalido para el evento en cuestion
     */
    protected void marcaElDocumentoComoVerdadero(String documento) throws IllegalArgumentException {
        if (!validaDocumento(documento)) throw new IllegalArgumentException("Documento invalido");
        documentos.put(documento, true);
    }

    /**
     * Marca un documento como no requerido o entregado del evento, si es un documento adicional, lo elimina
     *
     * @param documento Documento a remover
     * @throws IllegalArgumentException Si el documento es invalido para el evento en cuestion
     */
    protected void marcaDocumentoComoFalso(String documento) throws IllegalArgumentException {
        if (!validaDocumento(documento)) throw new IllegalArgumentException("Documento invalido");
        // Si es adicional, remuevelo
        if (!(Arrays.binarySearch(DOCUMENTOS_OPCIONES, documento) >= 0)) documentos.remove(documento);
        // Si esta en la lista de opciones marcalo como no entregado
        documentos.put(documento, false);
    }

    /**
     * Verifica que el documento pasado sea valido para el evento en cuestion
     *
     * @param documento Nombre del documento a verificar
     * @return Si documento es valido o no como corresponda
     */
    protected abstract boolean validaDocumento(String documento);

    /**
     * Transforma los documentos requeridos o entregados del evento en una string "bonita" y la devuelve
     *
     * @return String organizada con todos los documentos requeridos o entregados solamente
     */
    public String documentosVerdaderosToString() {
        StringBuilder output = new StringBuilder();
        for (String llave : documentos.keySet()) {
            if (documentos.get(llave)) output.append("/").append(llave);
        }
        return output.toString();
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
    protected void setAbierto(boolean isAbierto) {
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
