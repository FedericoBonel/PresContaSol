package abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Clase que abstrae a todos los Cuentadantes del sistema
 *
 * @author Bonel Federico
 */
public class Cuentadante extends Usuario {
    /**
     * Constante que mantiene el tipo de usuario
     */
    private static final String TIPO_CUENTADANTE = "Cuentadante";
    /**
     * Municipio al que representa
     */
    private Municipio municipio;

    /**
     * Constructor para instanciar Cuentadantes
     *
     * @param usuario Identificador del usuario: debe tener desde 1 hasta 10 caracteres
     * @param clave   Clave a asignar para el ingreso al sistema del usuario: debe tener desde 4 hasta 8 caracteres
     */
    public Cuentadante(String usuario, String clave) throws IllegalArgumentException {
        super(usuario, clave);
    }

    /**
     * Devuelve las presentaciones que deben ser visibles para el cuentadante, estas son:
     * Todas las presentaciones realizadas por el
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @return Un linked list con todas las presentaciones que corresponden al usuario cuentadante
     */
    @Override
    public LinkedList<Presentacion> getPresentacionesVisibles(ColeccionPresentaciones presentaciones) {
        LinkedList<Presentacion> presentacionesRealizadas = new LinkedList<>();
        Presentacion presentacionActual;
        for (String presentacionId : presentaciones.getHashtable().keySet()) {
            presentacionActual = presentaciones.getPresentacion(presentacionId);
            if (presentacionActual.isAutor(this)) presentacionesRealizadas.add(presentacionActual);
        }
        return presentacionesRealizadas;
    }

    /**
     * Crea una presentacion y la agrega a la coleccion de presentaciones pasada
     *
     * @param id             Identificador alfanumerico unico de presentacion: Puede tener desde 1 caracter hasta 100 caracteres
     * @param convocatoria   Convocatoria a la cual se presenta: No puede estar cerrada
     * @param docsEntregados Documentos entregados en la presentacion para la convocatoria
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @throws IllegalArgumentException Si la presentacion ya esta registrada, o si algun parametro es invalido
     * @throws IllegalCallerException   Si el cuentadante no tiene ningun municipio asignado
     */
    public void creaPresentacion(String id, Convocatoria convocatoria, Hashtable<String, Boolean> docsEntregados,
                                 ColeccionPresentaciones presentaciones) throws IllegalArgumentException, IllegalCallerException {
        // Verifica que el municipio del que contiene informacion la presentacion esta asignado
        if (this.municipio == null) throw new IllegalCallerException("Municipio no asignado");
        // Cuando el usuario crea una presentacion siempre se establece como abierta por defecto
        Presentacion nuevaPresentacion = new Presentacion(id, LocalDate.now(), true, convocatoria,
                this, this.municipio, docsEntregados);
        presentaciones.addPresentacion(nuevaPresentacion);
    }

    /**
     * Verifica si la presentacion puede ser eliminada por el cuentadante
     *
     * @param presentacion Presentacion a verificar
     * @return true si el cuentadante es el propietario de la presentacion, false de otra forma
     */
    @Override
    protected boolean puedeEliminarPresentacion(Presentacion presentacion) {
        return presentacion.isAutor(this);
    }

    /**
     * Cierra la presentacion especificada para entregarla de manera final al Fiscal
     *
     * @param presentacion Presentacion que se desea cerrar/entregar
     * @throws IllegalCallerException Si la presentacion no pertenece al Cuentadante, o si es invalida
     */
    public void entregaPresentacion(Presentacion presentacion) throws IllegalCallerException {
        if (!presentacion.isAutor(this))
            throw new IllegalCallerException("Presentacion no fue hecha por este cuentadante");
        presentacion.seEntrega();
    }

    /**
     * Verifica si el cuentadante puede entregar el documento en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     * @return true si la presentacion es suya, false de otra forma
     */
    @Override
    protected boolean puedeEntregarDocumentoA(Presentacion presentacion, String documento) {
        return presentacion.isAutor(this);
    }

    /**
     * Verifica si el cuentadante puede retirar el documento de la presentacion
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     * @return true si la presentacion es suya, false de otra forma
     */
    @Override
    protected boolean puedeRetirarDocumentoDe(Presentacion presentacion, String documento) {
        return presentacion.isAutor(this);
    }

    // Devuelve el municipio asignado
    public Municipio getMunicipio() {
        return municipio;
    }

    /**
     * Verifica si el municipio pasado es representado por el cuentadante
     *
     * @param municipio municipio a verificar
     * @return true si el municipio pasado es el mismo, false de otra forma
     */
    public boolean isMunicipio(Municipio municipio) {
        if (this.municipio == null) return false;
        return this.municipio.getId().equals(municipio.getId());
    }


    /**
     * Devuelve el municipio que debe ser visible para los cuentadantes, este solo es:
     * el municipio que representa
     *
     * @param municipios Coleccion de todos los municipios del sistema
     * @return Un linked list con el municipio que corresponde al usuario cuentadante
     */
    @Override
    public LinkedList<Municipio> getMunicipiosVisibles(ColeccionMunicipios municipios) {
        LinkedList<Municipio> municipiosVisibles = new LinkedList<>();
        municipiosVisibles.add(municipio);
        return municipiosVisibles;
    }

    /**
     * Asigna el municipio pasado al cuentadante
     *
     * @param municipioNuevo Municipio a asignar
     * @param usuarios       Coleccion de todos los usuarios del sistema (donde se halla el cuentadante a reemplazar del municipio)
     */
    protected void tomaResponsabilidadDeMunicipio(Municipio municipioNuevo, ColeccionUsuarios usuarios) {
        Cuentadante cuentadanteAntiguo;
        // Encuentra el cuentadante antiguo del municipio (si lo tiene) y desasignalo
        for (String usuarioId : usuarios.getHashtable().keySet()) {
            if (usuarios.getUsuario(usuarioId) instanceof Cuentadante) {
                cuentadanteAntiguo = (Cuentadante) usuarios.getUsuario(usuarioId);
                if (cuentadanteAntiguo.isMunicipio(municipioNuevo)) {
                    cuentadanteAntiguo.abandonaMunicipio();
                    break;
                }
            }
        }
        // Asignalo a este municipio
        municipio = municipioNuevo;
    }

    /**
     * Remueve el municipio que actualmente se halla asignado al cuentadante
     */
    protected void abandonaMunicipio() {
        municipio = null;
    }

    /**
     * Remueve todas las presentaciones del cuentadante de la coleccion de presentaciones especificada
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     */
    protected void eliminaSusPresentacionesDe(ColeccionPresentaciones presentaciones) {
        // Toma las presentaciones realizadas por este cuentadante
        LinkedList<Presentacion> presentacionesPropias = getPresentacionesVisibles(presentaciones);
        // Por cada presentacion del cuentadante
        for (Presentacion presentacionARemover : presentacionesPropias) {
            // Remuevela de la coleccion
            presentaciones.removePresentacion(presentacionARemover);
        }
    }

    /**
     * Implementacion para conseguir las convocatorias que deben ser visibles para el cuentadante
     *
     * @param convocatorias coleccion de todas las convocatorias del sistema
     * @return Un linked list con todas las convocatorias que corresponden al cuentadante
     */
    @Override
    public LinkedList<Convocatoria> getConvocatoriasVisibles(ColeccionConvocatorias convocatorias) {
        return convocatorias.getConvocatoriasAbiertasLinkedList();
    }

    public String toStringConClave() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + super.getClave() + '\'' +
                ", tipo='" + TIPO_CUENTADANTE + '\'' +
                ", municipio='" + municipio + '\'' +
                "} ";
    }

    /**
     * Devuelve los parametros de la instancia como string sin las presentaciones ni el municipio
     *
     * @return Un string con los parametros relevantes de la instancia
     */
    @Override
    public String toString() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + "ESCONDIDA" + '\'' +
                ", tipo='" + TIPO_CUENTADANTE + '\'' +
                ", municipio='" + municipio + '\'' +
                "} ";
    }
}
