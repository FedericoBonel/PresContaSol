package controlador.abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;


/**
 * Clase que abstrae a una presentacion de una convocatoria
 */
public class Presentacion extends Evento {
    // Estado de apertura de presentacion por defecto
    private final static boolean ABIERTO_DEFECTO = true;
    // Convocatoria a la que presenta
    private Convocatoria convocatoria;
    // Cuentadante al que pertenece la presentacion
    private Cuentadante cuentadante;
    // Municipio al que pertenece la presentacion
    private Municipio municipio;

    //Constructor de la presentacion
    public Presentacion(String id, LocalDate fechaInicio, Convocatoria convocatoria, Cuentadante cuentadante, Municipio municipio,
                        Hashtable<String, Boolean> docsEntregados) {
        super(id, fechaInicio, ABIERTO_DEFECTO, docsEntregados);
        this.convocatoria = convocatoria;
        convocatoria.addPresentacion(this);
        this.cuentadante = cuentadante;
        cuentadante.addPresentacion(this);
        this.municipio = municipio;
        municipio.addPresentacion(this);
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
            if (convocatoria.isRequeOEntrega(documento) && !super.isRequeOEntrega(documento)) {
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

    // Devuelve a que cuentadante corresponde esta presentacion
    public Municipio getMunicipio() {
        return municipio;
    }

    // Asigna cuentadante a presentacion
    protected void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    // Devuelve todos los atributos de la instancia como string
    @Override
    public String toString() {
        return super.toString() +
                " | " + convocatoria.getFechaCierre().toString() +
                " | " + cuentadante.getId() +
                " | " + this.todosRequeridosEntregados();
    }
}
