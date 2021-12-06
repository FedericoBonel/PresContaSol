package abstraccionNegocio;

import java.util.LinkedList;

/**
 * Clase que abstrae a un municipio del sistema
 *
 * @author Bonel Federico
 */
public class Municipio extends Entidad {
    /**
     * Limites en formato de identificador (en cantidad de caracteres)
     */
    private final int LIMITE_IDENTIFICADOR_INFERIOR = 1;
    private final int LIMITE_IDENTIFICADOR_SUPERIOR = 30;
    /**
     * Categoria del municipio
     */
    private int categoria;
    /**
     * Supervisor del municipio
     */
    private Fiscal fiscal;

    /**
     * Constructor de municipio
     *
     * @param identificador Identificador alfanumerico unico de municipio: Puede tener desde 1 caracter hasta 30 caracteres
     * @param categoria     Categoria del municipio: Es un numero entero
     * @throws IllegalArgumentException si alguno de los parametros es invalido y no cumple con los requisitos de formato
     */
    public Municipio(String identificador, int categoria) throws IllegalArgumentException {
        super(identificador);
        setCategoria(categoria);
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
    protected void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * Devuelve el fiscal asignado a este municipio
     *
     * @return Objeto Fiscal que supervisa al municipio
     */
    public Fiscal getFiscal() {
        return fiscal;
    }

    /**
     * Verifica si el fiscal pasado se corresponde con el fiscal del municipio sobre el que se llame
     *
     * @param fiscal Objeto Fiscal a verificar con el asignado
     * @return Verdadero si es el mismo, falso en caso contrario
     */
    public boolean isFiscal(Fiscal fiscal) {
        return fiscal.getId().equals(this.fiscal.getId());
    }

    /**
     * Asigna el fiscal al municipio
     *
     * @param nuevoFiscal Fiscal que se desea asignar
     */
    protected void tomaNuevoSupervisorFiscal(Fiscal nuevoFiscal) {
        fiscal = nuevoFiscal;
    }

    /**
     * Remueve el fiscal asignado actualmente al municipio
     */
    protected void removeFiscal() {
        fiscal = null;
    }

    /**
     * Verifica si el municipio tiene asignado un fiscal o no
     *
     * @return true si tiene supervisor asignado, false de otra forma
     */
    public boolean tieneSupervisor() {
        return fiscal != null;
    }

    /**
     * Devuelve todas las presentaciones que se realizaron para este municipio
     *
     * @return Un LinkedList con todas las presentaciones realizadas por este municipio
     */
    public LinkedList<Presentacion> getSusPresentacionesDe(ColeccionPresentaciones presentaciones) {
        LinkedList<Presentacion> presentacionesRealizadas = new LinkedList<>();
        Presentacion presentacionActual;
        // Itera por todas las presentaciones agregando solo las que se realizaron para este municipio
        for (String presentacionId : presentaciones.getHashtable().keySet()) {
            presentacionActual = presentaciones.getPresentacion(presentacionId);
            if (presentacionActual.isMunicipio(this)) presentacionesRealizadas.add(presentacionActual);
        }
        return presentacionesRealizadas;
    }

    /**
     * Remueve todas las presentaciones del municipio de la coleccion pasada
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     */
    protected void eliminaSusPresentacionesDe(ColeccionPresentaciones presentaciones) {
        // Toma las presentaciones realizadas para este municipio
        LinkedList<Presentacion> presentacionesPropias = getSusPresentacionesDe(presentaciones);
        // Por cada presentacion del municipio
        for (Presentacion presentacionARemover : presentacionesPropias) {
            // Remuevela de la coleccion
            presentaciones.removePresentacion(presentacionARemover);
        }
    }

    /**
     * Quita la relacion del cuentadante representante de este municipio a este municipio
     */
    protected void abandonaSuRepresentanteEn(ColeccionUsuarios usuarios) {
        // Encuentra el cuentadante que represente a este municipio y desasignalo
        for (String usuarioId : usuarios.getHashtable().keySet()) {
            if (usuarios.getUsuario(usuarioId) instanceof Cuentadante cuentadanteActual) {
                if (cuentadanteActual.isMunicipio(this)) {
                    cuentadanteActual.abandonaMunicipio();
                    break;
                }
            }
        }
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
                "} ";
    }
}
