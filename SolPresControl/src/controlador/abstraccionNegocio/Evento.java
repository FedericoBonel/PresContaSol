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
    // Estado del evento, puede estar -> abierto:true, cerrado:false
    private boolean estado;

    //Constructor del evento
    public Evento(String id, LocalDate fechaInicio, boolean estado, Hashtable<String, Boolean> documentos) {
        super(id);
        this.fechaInicio = fechaInicio;
        this.estado = estado;
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

    // Devuelve el estado de apertura
    public boolean getEstado() {
        return estado;
    }

    // Asigna un nuevo estado de apertura
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Devuelve los documentos para el evento
    public Hashtable<String, Boolean> getDocumentos() {
        return documentos;
    }

    // Devuelve estado de documento requerido o entregado
    public Boolean getEstadoDocumento(String docReq) {
        return documentos.get(docReq);
    }

    // Agrega un nuevo documento requerido o entregado
    public void addDocumento(String nuevoDocReq, Boolean estado) {
        documentos.put(nuevoDocReq, estado);
    }

    // Remueve un documento de los requeridos o entregados
    public void removeDocumento(String documento) {
        documentos.remove(documento);
    }

    // Verifica si el documento pasado corresponde a un documento requerido o entregado
    public boolean containsDoc(String documento) {
        return documentos.containsKey(documento);
    }

}
