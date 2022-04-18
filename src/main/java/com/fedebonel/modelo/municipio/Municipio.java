package com.fedebonel.modelo.municipio;

import com.fedebonel.modelo.Entidad;
import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.modelo.usuario.Usuario;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase que abstrae a un municipio del sistema
 *
 * @author Bonel Federico
 */
public class Municipio extends Entidad {
    /**
     * String constante que posee el error cuando el nombre ingresado es invalido
     */
    public static final String ERROR_NOMBRE_INVALIDO = "El nombre ingresado es invalido";
    /**
     * Limite inferior de longitud del identificador (en cantidad de caracteres)
     */
    public final static int LIMITE_IDENTIFICADOR_INFERIOR = 1;
    /**
     * Limite superior de longitud del identificador (en cantidad de caracteres)
     */
    public final static int LIMITE_IDENTIFICADOR_SUPERIOR = 30;
    /**
     * Limite superior de categoria (en valor numerico)
     */
    public final static int LIMITE_CATEGORIA_SUPERIOR = 100;
    /**
     * Nombre municipio
     */
    private String nombre;
    /**
     * Categoria del municipio
     */
    private int categoria;
    /**
     * Supervisor del municipio
     */
    private Usuario fiscal;
    /**
     * Representante del municipio
     */
    private Usuario cuentadante;

    /**
     * Constructor de municipio
     *
     * @param identificador Identificador alfanumerico unico de municipio: Puede tener desde 1 caracter hasta 30 caracteres
     * @param nombre        Nombre del municipio: Es una cadena de caracteres
     * @param categoria     Categoria del municipio: Es un numero entero
     * @throws IllegalArgumentException si alguno de los parametros es invalido y no cumple con los requisitos de formato
     */
    public Municipio(String identificador, String nombre, int categoria) throws IllegalArgumentException {
        super(identificador.toLowerCase());
        setNombre(nombre);
        setCategoria(categoria);
    }

    /**
     * Devuelve el nombre del municipio
     *
     * @return Nombre del municipio como un string
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de este municipio
     *
     * @param nombre Nombre a asignar
     * @throws IllegalArgumentException Si el nombre esta vacio
     */
    private void setNombre(String nombre) {
        if (nombre.length() < 1) throw new IllegalArgumentException(ERROR_NOMBRE_INVALIDO);
        this.nombre = nombre;
    }

    /**
     * Devuelve la categoria del Municipio
     *
     * @return Categoria del municipio en forma de entero
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * Asigna la categoria del Municipio
     *
     * @param categoria Categoria del municipio en forma de entero
     */
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * Devuelve el fiscal asignado a este municipio
     *
     * @return Objeto Fiscal que supervisa al municipio
     */
    public Usuario getFiscal() {
        return fiscal;
    }

    /**
     * Verifica si el fiscal pasado se corresponde con el fiscal del municipio sobre el que se llame
     *
     * @param fiscal Objeto Fiscal a verificar con el asignado
     * @return Verdadero si es el mismo, falso en caso contrario
     */
    public boolean isFiscal(Usuario fiscal) {
        if (this.fiscal == null) return false;
        return fiscal.getId().equals(this.fiscal.getId());
    }

    /**
     * Asigna el fiscal al municipio
     *
     * @param nuevoFiscal Fiscal que se desea asignar
     */
    public void tomaNuevoSupervisorFiscal(Usuario nuevoFiscal) {
        fiscal = nuevoFiscal;
    }

    /**
     * Remueve el fiscal asignado actualmente al municipio
     */
    public void abandonaSupervisor() {
        fiscal = null;
    }

    /**
     * Devuelve todas las presentaciones que se realizaron para este municipio
     *
     * @param presentaciones Coleccion de presentaciones del sistema
     * @return Un LinkedList con todas las presentaciones realizadas por este municipio
     */
    public LinkedList<Presentacion> getSusPresentacionesDe(LinkedList<Presentacion> presentaciones) {
        LinkedList<Presentacion> presentacionesRealizadas = new LinkedList<>();
        // Itera por todas las presentaciones agregando solo las que se realizaron para este municipio
        for (Presentacion presentacion : presentaciones) {
            if (presentacion.isMunicipio(this)) presentacionesRealizadas.add(presentacion);
        }
        return presentacionesRealizadas;
    }

    /**
     * Obtiene el numero total de documentos presentados por este municipio en todas sus presentaciones
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @return El total de todos los documentos presentados de este municipio en todas las presentaciones
     */
    public int getTotalDocumentosPresentados(LinkedList<Presentacion> presentaciones) {
        int totalDocumentosPresentados = 0;
        LinkedList<Presentacion> presentacionesRealizadas = getSusPresentacionesDe(presentaciones);
        for (Presentacion presentacion : presentacionesRealizadas)
            totalDocumentosPresentados += presentacion.getDocumentos().getDocumentosLinkedList().size();
        return totalDocumentosPresentados;
    }

    /**
     * Devuelve el cuentadante asignado a este municipio
     *
     * @return Objeto Cuentadante que representa al municipio
     */
    public Usuario getCuentadante() {
        return cuentadante;
    }

    /**
     * Verifica si el cuentadante pasado se corresponde con el cuentadante del municipio sobre el que se llame
     *
     * @param cuentadante Objeto Cuentadante a verificar con el asignado
     * @return Verdadero si es el mismo, falso en caso contrario
     */
    public boolean isCuentadante(Usuario cuentadante) {
        if (this.cuentadante == null) return false;
        return cuentadante.getId().equals(this.cuentadante.getId());
    }

    /**
     * Asigna un nuevo representante a este municipio
     *
     * @param municipios  municipios del sistema
     * @param cuentadante nuevo representante a asignar
     * @return Municipio antiguo del cuentadante si tenia alguno asignado, null en otro caso
     */
    public Municipio tomaNuevoRepresentante(Usuario cuentadante, List<Municipio> municipios) {
        Municipio municipioAntiguo = cuentadante.getMunicipioRepresentadoDe(municipios);
        if (municipioAntiguo != null) municipioAntiguo.abandonaRepresentante();
        // Asigna el nuevo representante
        this.cuentadante = cuentadante;
        return municipioAntiguo;
    }

    /**
     * Quita la relacion del cuentadante representante de este municipio a este municipio
     */
    public void abandonaRepresentante() {
        cuentadante = null;
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
     * @return Un String con todos los atributos de la instancia
     */
    @Override
    public String toString() {
        return "{" +
                "identificador:" + super.getId() +
                ", categoria=" + categoria +
                ", fiscal=" + fiscal +
                ", cuentadante=" + cuentadante +
                "} ";
    }
}
