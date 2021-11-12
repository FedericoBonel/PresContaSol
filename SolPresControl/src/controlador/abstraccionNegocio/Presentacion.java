package controlador.abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;


/**
 * Clase que abstrae a una presentacion de una convocatoria
 */
public class Presentacion extends Evento {
    // Estado de apertura de presentacion por defecto
    private final static boolean ESTADO_DEFECTO_PRES = true;
    // Convocatoria a la que presenta
    private Convocatoria convocatoria;
    // Cuentadante al que pertenece la presentacion
    private Cuentadante cuentadante;

    //Constructor de la presentacion
    public Presentacion(String id, LocalDate fechaInicio, Convocatoria convocatoria, Cuentadante cuentadante, Hashtable<String, Boolean> docsEntregados) {
        super(id, fechaInicio, ESTADO_DEFECTO_PRES, docsEntregados);
        this.convocatoria = convocatoria;
        convocatoria.addPresentacion(this);
        this.cuentadante = cuentadante;
        cuentadante.addPresentacion(this);
    }

    // Remueve el documento adicional pasado de la presentacion
    public boolean removeDocumentoAdicional(String documento) {
        // Si no esta en los documentos requeridos, es adicional y entonces puedes removerlo
        if (!convocatoria.containsDoc(documento)) {
            super.removeDocumento(documento);
            return true;
        }
        //Si no no es documento adicional y no se puede eliminar
        return false;
    }

    // Devuelve si todos los documentos requeridos estan entregados
    public boolean todosRequeridosEntregados() {
        // Itera por todos los documentos requeridos
        for (String documento : convocatoria.getDocumentos().keySet()) {
            // Si el documento es requerido y no esta subido en esta presentacion entonces no esta entregado
            if (convocatoria.getEstadoDocumento(documento) && !super.getEstadoDocumento(documento)) {
                return false;
            }
        }
        // Si no todos estan entregados
        return true;
    }

    // Devuelve a que convocatoria se hace la presentacion
    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    // Asigna una convocatoria diferente a esta presentacion
    protected void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    // Verifica si la convocatoria pasada corresponde a la asignada
    public boolean isConvocatoria(Convocatoria convocatoria) {
        if (this.convocatoria == null) return false;
        return this.convocatoria.getId().equals(convocatoria.getId());
    }

    // Devuelve a que cuentadante corresponde esta presentacion
    public Cuentadante getCuentadante() {
        return cuentadante;
    }

    // Asigna cuentadante a presentacion
    protected void setCuentadante(Cuentadante cuentadante) {
        this.cuentadante = cuentadante;
    }

}
