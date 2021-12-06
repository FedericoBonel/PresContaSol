package abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;


/**
 * Clase que abstrae a una presentacion de una convocatoria
 *
 * @author Bonel Federico
 */
public class Presentacion extends Evento {
    /**
     * Convocatoria a la que presenta
     */
    private Convocatoria convocatoria;
    /**
     * Cuentadante autor al que pertenece
     */
    private Cuentadante autor;
    /**
     * Municipio del que contiene informacion
     */
    private Municipio municipio;

    /**
     * Constructor de la presentacion
     *
     * @param id             Identificador alfanumerico unico de presentacion: Puede tener desde 1 caracter hasta 100 caracteres
     * @param fechaInicio    Fecha de creacion de la presentacion: No puede ser en el futuro
     * @param convocatoria   Convocatoria a la cual se presenta: No puede estar cerrada
     * @param autor          Cuentadante autor/propietario de la presentacion
     * @param municipio      Municipio al cual pertenece la convocatoria
     * @param docsEntregados Documentos entregados en la presentacion para la convocatoria
     * @throws IllegalArgumentException Si algun parametro es invalido y no cumple con los requisitos de formato
     */
    public Presentacion(String id, LocalDate fechaInicio, boolean isAbierto, Convocatoria convocatoria, Cuentadante autor, Municipio municipio,
                        Hashtable<String, Boolean> docsEntregados) throws IllegalArgumentException {
        super(id, fechaInicio, isAbierto, docsEntregados);
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
     * @throws IllegalArgumentException Si la convocatoria esta cerrada
     */
    private void setConvocatoria(Convocatoria convocatoria) throws IllegalArgumentException {
        if (!convocatoria.isAbierto()) throw new IllegalArgumentException("Convocatoria cerrada");
        this.convocatoria = convocatoria;
    }

    /**
     * Remueve la convocatoria de la presentacion
     */
    private void abandonaConvocatoria() {
        convocatoria = null;
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
    public Cuentadante getAutor() {
        return autor;
    }

    /**
     * Asigna el cuentadante de esta presentacion
     *
     * @param autor Objeto del cuentadante que realiza la presentacion
     */
    private void setAutor(Cuentadante autor) {
        this.autor = autor;
    }

    /**
     * Remueve el autor de la presentacion
     */
    private void abandonaAutor() {
        autor = null;
    }

    /**
     * Verifica que el cuentadante pasado se corresponda con el cuentadante autor de la presentacion
     *
     * @param autor Objeto del cuentadante a verificar
     * @return true si se corresponden, falso en caso contrario
     */
    public boolean isAutor(Cuentadante autor) {
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
     */
    private void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    /**
     * Remueve el municipio de la presentacion
     */
    private void abandonaMunicipio() {
        municipio = null;
    }

    /**
     * Verifica que el municipio pasado se corresponda con el municipio de esta presentacion
     *
     * @param municipio Objeto del municipio a verificar
     */
    public boolean isMunicipio(Municipio municipio) {
        return this.municipio.getId().equals(municipio.getId());
    }

    /**
     * Verifica que el documento entregado pasado sea valido para las presentaciones
     * En este caso cualquier documento es valido porque se permiten documentos adicionales
     *
     * @param documento Nombre del documento a verificar
     * @return verdadero siempre
     */
    @Override
    protected boolean validaDocumento(String documento) {
        return true;
    }

    /**
     * Verifica si el documento es entregado en la presentacion
     *
     * @param documento Documento a verificar
     * @return true si es entregado, false en caso contrario
     */
    public Boolean isEntregado(String documento) {
        if (!super.getDocumentos().containsKey(documento)) return false;
        return super.getDocumento(documento);
    }

    /**
     * Verifica si todos los documentos requeridos en la convocatoria estan marcados como entregados en la presentacion
     *
     * @return true si todos entregados, false en caso contrario
     */
    public boolean todosDocsRequeridosEntregados() {
        // Itera por todos los documentos requeridos
        for (String documento : convocatoria.getDocumentos().keySet()) {
            // Si el documento es requerido y no esta subido en esta presentacion entonces no esta entregado
            if (convocatoria.isRequerido(documento) && !isEntregado(documento)) {
                return false;
            }
        }
        // Si no todos estan entregados
        return true;
    }

    /**
     * Cierra la presentacion marcandola como entregada (cerrada)
     *
     * @throws IllegalCallerException Si faltan documentos requeridos o si la convocatoria ya esta cerrada
     */
    protected void seEntrega() throws IllegalCallerException {
        if (!todosDocsRequeridosEntregados() || !convocatoria.isAbierto())
            throw new IllegalCallerException("Faltan documentos o Convocatoria ya esta cerrada");
        super.setAbierto(false);
    }

    /**
     * Remueve todas las relaciones de esta presentacion
     */
    protected void abandonaRelaciones() {
        abandonaAutor();
        abandonaMunicipio();
        abandonaConvocatoria();
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
                ", documentos=" + super.documentosVerdaderosToString() +
                ", fechaInicio=" + super.getFechaInicio() +
                ", abierto=" + super.isAbierto() +
                "} ";
    }
}
