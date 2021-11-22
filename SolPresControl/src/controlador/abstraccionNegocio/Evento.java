package controlador.abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;

/**
 * Clase que abstrae a un evento de una convocatoria
 */
public abstract class Evento extends Entidad {
    // Opciones de documentos
    public static final String[] DOCUMENTOS_OPCIONES = {"Balance de Sumas y Saldos", "Libro Diario", "Libro Mayor",
            "Registro de Ingreso de Caja", "Registro de Movimientos de Bancos"};
    // Documentos requeridos o entregados para este evento con su estado: de requerido o entregado
    private final Hashtable<String, Boolean> documentos;
    // Primera fecha, sea esta la fecha de creacion o apertura
    private LocalDate fechaInicio;
    // Estado del evento, puede estar -> abierto:true, no abierto:false
    private boolean abierto;

    //Constructor del evento
    public Evento(String id, LocalDate fechaInicio, boolean isAbierto, Hashtable<String, Boolean> documentos) {
        super(id);
        this.fechaInicio = fechaInicio;
        this.abierto = isAbierto;
        this.documentos = documentos;
    }

    // Devuelve la fecha de inicio
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    // Asigna la fecha de inicio
    public void setFechaInicio(LocalDate nuevaFechaInicio) {
        fechaInicio = nuevaFechaInicio;
    }

    // Devuelve si esta abierto o no
    public boolean isAbierto() {
        return abierto;
    }

    // Asigna un nuevo estado de apertura
    public void setAbierto(boolean estado) {
        this.abierto = estado;
    }

    // Devuelve los documentos para el evento
    public Hashtable<String, Boolean> getDocumentos() {
        return documentos;
    }

    // Devuelve estado de documento requerido o entregado
    public Boolean isRequeOEntrega(String documento) {
        return documentos.get(documento);
    }

    // Agrega un nuevo documento requerido o entregado
    public void addDocumento(String nuevoDoc, Boolean requeOEntrega) {
        documentos.put(nuevoDoc, requeOEntrega);
    }

    // Remueve un documento de los requeridos o entregados
    public void removeDocumento(String documento) {
        documentos.remove(documento);
    }

    // Verifica si el documento pasado corresponde a un documento requerido o entregado
    public boolean containsDoc(String documento) {
        return documentos.containsKey(documento);
    }

    // Devuelve todos los atributos de la instancia como string
    @Override
    public String toString() {
        return super.toString() + " | " + abierto + " | " + this.documentosVerdaderosAString() + " | " + fechaInicio.toString();
    }

    // Transforma los documentos verdaderos del evento en una string "bonita" y la devuelve
    private String documentosVerdaderosAString() {
        StringBuilder output = new StringBuilder();
        for (String llave : documentos.keySet()) {
            if (documentos.get(llave)) output.append("/").append(llave);
        }
        return output.toString();
    }
}
