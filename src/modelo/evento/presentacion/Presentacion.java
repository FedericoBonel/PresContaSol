package modelo.evento.presentacion;

import modelo.evento.ColeccionDocumentos;
import modelo.evento.Evento;
import modelo.evento.convocatoria.Convocatoria;
import modelo.municipio.Municipio;
import modelo.usuario.Usuario;

import java.time.LocalDate;
import java.util.LinkedList;


/**
 * Clase que abstrae a una presentacion de una convocatoria
 *
 * @author Bonel Federico
 */
public class Presentacion extends Evento {

    /**
     * String que posee el error cuando el municipio pasado es nulo
     */
    public final static String ERROR_MUNICIPIO_NULO = "El usuario no posee un municipio asignado";
    /**
     * Convocatoria a la que presenta
     */
    private Convocatoria convocatoria;
    /**
     * Cuentadante autor al que pertenece
     */
    private Usuario autor;
    /**
     * Municipio del que contiene informacion
     */
    private Municipio municipio;

    /**
     * Constructor de la presentacion
     *
     * @param id             Identificador alfanumerico unico de presentacion: Puede tener desde 1 caracter hasta 100 caracteres
     * @param fechaInicio    Fecha de creacion de la presentacion: No puede ser en el futuro
     * @param isAbierto      Estado de apertura de la presentacion: True abierta (i.e. no entregada), False cerrada (i.e. entregada)
     * @param convocatoria   Convocatoria a la cual se presenta: No puede estar cerrada
     * @param autor          Cuentadante autor/propietario de la presentacion
     * @param municipio      Municipio al cual pertenece la convocatoria
     * @param docsEntregados Documentos entregados en la presentacion para la convocatoria
     * @throws IllegalArgumentException Si algun parametro es invalido y no cumple con los requisitos de formato
     */
    public Presentacion(String id, LocalDate fechaInicio, boolean isAbierto, Convocatoria convocatoria, Usuario autor, Municipio municipio,
                        LinkedList<String> docsEntregados) throws IllegalArgumentException {
        super(id.toLowerCase(), fechaInicio, isAbierto, docsEntregados);
        setConvocatoria(convocatoria);
        setAutor(autor);
        setMunicipio(municipio);
    }

    /**
     * Devuelve la convocatoria a la que se realiza la presentacion
     *
     * @return Convocatoria a la que se presenta
     */
    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    /**
     * Asigna una convocatoria a esta presentacion
     *
     * @param convocatoria Objeto de la convocatoria a la que se desea presentar
     */
    private void setConvocatoria(Convocatoria convocatoria) throws IllegalArgumentException {
        this.convocatoria = convocatoria;
    }

    /**
     * Verifica si la convocatoria pasada se corresponda con la convocatoria de la presentacion
     *
     * @param convocatoria Objeto de la convocatoria a verificar
     * @return true si la convocatoria es la misma, falso en caso contrario
     */
    public boolean isConvocatoria(Convocatoria convocatoria) {
        return this.convocatoria.getId().equals(convocatoria.getId());
    }

    /**
     * Devuelve el cuentadante que creo la presentacion
     *
     * @return Cuentadante autor de la instancia
     */
    public Usuario getAutor() {
        return autor;
    }

    /**
     * Asigna el cuentadante de esta presentacion
     *
     * @param autor Objeto del cuentadante que realiza la presentacion
     */
    private void setAutor(Usuario autor) {
        this.autor = autor;
    }

    /**
     * Verifica que el cuentadante pasado se corresponda con el cuentadante autor de la presentacion
     *
     * @param autor Objeto del cuentadante a verificar
     * @return true si se corresponden, falso en caso contrario
     */
    public boolean isAutor(Usuario autor) {
        return this.autor.getId().equals(autor.getId());
    }

    /**
     * Devuelve el municipio del cual, la presentacion, contiene informacion
     *
     * @return Municipio al que el cuentadante estaba representando al momento de realizar la presentacion
     */
    public Municipio getMunicipio() {
        return municipio;
    }

    /**
     * Asigna el municipio del cual, la presentación, contiene información
     *
     * @param municipio Objeto del municipio que realiza la presentacion
     * @throws IllegalArgumentException Si el municipio pasado es nulo
     */
    private void setMunicipio(Municipio municipio) {
        if (municipio == null) throw new IllegalArgumentException(ERROR_MUNICIPIO_NULO);
        this.municipio = municipio;
    }

    /**
     * Verifica que el municipio pasado se corresponda con el municipio de esta presentacion
     *
     * @param municipio Objeto del municipio a verificar
     * @return true si es el mismo, false en caso contrario
     */
    public boolean isMunicipio(Municipio municipio) {
        return this.municipio.getId().equals(municipio.getId());
    }

    /**
     * Inicializa la coleccion de documentos que contendra todos los documentos entregados de la presentacion
     *
     * @param documentos Documentos entregados como un LinkedList de strings
     */
    @Override
    protected ColeccionDocumentos incializaDocumentos(LinkedList<String> documentos) throws IllegalArgumentException {
        return new ColeccionDocumentos(documentos);
    }

    /**
     * Verifica si el documento es entregado en la presentacion
     *
     * @param documento Documento a verificar
     * @return true si es entregado, false en caso contrario
     */
    public Boolean isEntregado(String documento) {
        return super.containsDocumento(documento);
    }

    /**
     *  Devuelve los documentos requeridos de la convocatoria que estan entregados en la presentacion
     *
     * @return Un linked list con todos los documentos requeridos de la convocatoria que son entregados
     */
    public LinkedList<String> getDocumentosRequeridosEntregados(){
        LinkedList<String> documentosReqEntregados = new LinkedList<>();
        for (String documento : super.getDocumentos().getDocumentosLinkedList()) {
            if (convocatoria.containsDocumento(documento)) documentosReqEntregados.add(documento);
        }
        return documentosReqEntregados;
    }

    /**
     *  Devuelve los documentos adicionales que estan entregados en la presentacion
     *
     * @return Un linked list con todos los documentos adicionales que son entregados
     */
    public LinkedList<String> getDocumentosAdicionalesEntregados(){
        LinkedList<String> documentosAdicionalesEntregados = new LinkedList<>();
        for (String documento : super.getDocumentos().getDocumentosLinkedList()) {
            if (!convocatoria.containsDocumento(documento)) documentosAdicionalesEntregados.add(documento);
        }
        return documentosAdicionalesEntregados;
    }

    /**
     * Verifica si todos los documentos requeridos en la convocatoria estan marcados como entregados en la presentacion
     *
     * @return true si todos entregados, false en caso contrario
     */
    public boolean todosDocsRequeridosEntregados() {
        // Itera por todos los documentos requeridos
        for (String documento : convocatoria.getDocumentos().getDocumentosLinkedList()) {
            // Si el documento es requerido y no esta subido en esta presentacion entonces no esta entregado
            if (!isEntregado(documento)) {
                return false;
            }
        }
        // Si no todos estan entregados
        return true;
    }

    /**
     * Remueve todas las relaciones de esta presentacion
     */
    protected void abandonaRelaciones() {
        autor = null;
        municipio = null;
        convocatoria = null;
    }

    /**
     * Devuelve si la fecha de creacion de la presentacion es invalida
     * Si es en el futuro es invalida (Es imposible crear presentaciones en el futuro)
     *
     * @param fechaInicio Objeto LocalDate a verificar
     * @return true si la fechaInicio no es en el futuro
     */
    @Override
    protected boolean cumpleFormatoFechaInicio(LocalDate fechaInicio) {
        return !fechaInicio.isAfter(LocalDate.now());
    }

    /**
     * Devuelve todos los atributos de la instancia como string
     *
     * @return Todos los atributos de la instancia en forma de string
     */
    @Override
    public String toString() {
        return "{" +
                "identificador:" + super.getId() +
                ", autor=" + autor +
                ", municipio=" + municipio +
                ", convocatoria=" + convocatoria +
                ", documentos=" + super.getDocumentos() +
                ", fechaInicio=" + super.getFechaInicio() +
                ", abierto=" + super.isAbierto() +
                "} ";
    }
}
