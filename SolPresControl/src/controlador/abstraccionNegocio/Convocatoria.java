package controlador.abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;

/**
 * Clase que abstrae a una convocatoria
 */
public class Convocatoria extends Evento {
    // Presentaciones asignadas a esta convocatoria
    private final Hashtable<String, Presentacion> presentaciones;
    // Fecha de cierre de la convocatoria
    private LocalDate fechaCierre;

    //Constructor de convocatoria
    public Convocatoria(String id, LocalDate fechaInicio, LocalDate fechaCierre, Hashtable<String, Boolean> docsReq) throws Exception {
        super(id, fechaInicio, false, docsReq);
        //Si la fecha de cierre es antes que la fecha de inicio tira una excepcion
        if (fechaCierre.isBefore(fechaInicio)) throw new Exception("Fecha incorrecta");
        this.fechaCierre = fechaCierre;
        this.presentaciones = new Hashtable<>();
    }

    // Devuelve la presentacion deseada de la convocatoria
    public Presentacion getPresentacion(Presentacion presentacion) {
        return presentaciones.get(presentacion.getId());
    }

    // Devuelve todas las presentaciones
    public Hashtable<String, Presentacion> getPresentaciones() {
        return presentaciones;
    }

    // Verifica si la presentacion pasada corresponde a una de la convocatoria
    public boolean containsPresentacion(Presentacion presentacion) {
        return presentaciones.containsKey(presentacion.getId());
    }

    // Devuelve la fecha de cierre de la convocatoria
    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    // Asigna nueva fecha de cierre
    public void setFechaCierre(LocalDate fechaCierre) throws Exception {
        //Si la fecha de cierre es antes que la fecha de inicio tira una excepcion
        if (fechaCierre.isBefore(this.getFechaInicio())) throw new Exception("Fecha incorrecta");
        this.fechaCierre = fechaCierre;
    }

    // Asigna la presentacion a esta convocatoria
    protected void addPresentacion(Presentacion presentacion) {
        presentaciones.put(presentacion.getId(), presentacion);
    }

    // Desasigna presentacion del sistema
    protected void removePresentacion(Presentacion presentacion) {
        presentaciones.remove(presentacion.getId());
    }
}
