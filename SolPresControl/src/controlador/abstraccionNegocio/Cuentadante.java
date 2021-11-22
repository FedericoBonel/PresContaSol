package controlador.abstraccionNegocio;

import java.util.Hashtable;

/**
 * Clase que abstrae a todos los Cuentadantes del sistema
 */
public class Cuentadante extends Usuario {
    // Constante que mantiene el tipo de usuario
    private static final String TIPO_CUENTADANTE = "Cuentadante";
    // Presentaciones entregadas por el cuentadante <identificador, objeto>
    private final Hashtable<String, Presentacion> presentaciones;
    // Fiscal supervisor del cuentadante
    protected Fiscal fiscal;
    // Municipio del cuentadante
    private Municipio municipio;

    //Constructor de cuentadante
    public Cuentadante(String usuario, String contra) {
        super(usuario, contra, TIPO_CUENTADANTE);
        presentaciones = new Hashtable<>();
    }


    // Devuelve las presentaciones del Cuentadante
    public Hashtable<String, Presentacion> getPresentaciones() {
        return presentaciones;
    }

    // Devuelve la presentacion deseada del Cuentadante, si no la tiene devuelve null
    public Presentacion getPresentacion(String presentacion) {
        return presentaciones.get(presentacion);
    }

    // Verifica si el identificador pasado corresponde a una presentacion del cuentadante
    public boolean containsPresentacion(Presentacion presentacion) {
        return presentaciones.containsKey(presentacion.getId());
    }

    // Asigna la presentacion al cuentadante
    protected void addPresentacion(Presentacion presentacion) {
        presentaciones.put(presentacion.getId(), presentacion);
    }

    // Remueve la presentacion del cuentadante
    protected void removePresentacion(Presentacion presentacion) {
        presentaciones.remove(presentacion.getId());
    }


    // Devuelve el municipio asignado
    public Municipio getMunicipio() {
        return municipio;
    }

    // Asigna el municipio al cuentadante
    protected void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    // Devuelve verdadero si el cuentadante tiene municipio
    public boolean hasMunicipio() {
        return municipio != null;
    }

    // Verifica si el municipio pasado corresponde al municipio del cuentadante
    public boolean isMunicipio(Municipio municipio) {
        if (this.municipio == null) return false;
        return this.municipio.getId().equals(municipio.getId());
    }

    // Devuelve el fiscal asignado
    public Fiscal getFiscal() {
        return fiscal;
    }

    // Asigna fiscal al cuentadante
    protected void setFiscal(Fiscal fiscal) {
        this.fiscal = fiscal;
    }

    // Verifica si el fiscal pasado corresponde al fiscal del cuentadante
    public boolean isFiscal(Fiscal fiscal) {
        if (this.fiscal == null) return false;
        return this.fiscal.getId().equals(fiscal.getId());
    }

    // Verifica si el cuentadante tiene asignado un fiscal o no
    public boolean hasFiscal() {
        return fiscal != null;
    }

    // Devuelve los parametros de la instancia como string sin las presentaciones ni el municipio
    @Override
    public String toString() {
        String resultado = super.toString() + " | " + "null";
        if(this.hasFiscal()) resultado += " | " + fiscal.getId();
        else resultado += " | " + "null";
        return resultado;
    }

    public String toStringAdmin() {
        String resultado = super.toStringAdmin() + " | " + "null";
        if(this.hasFiscal()) resultado += " | " +  fiscal.getId();
        else resultado += " | " +  "null";
        return resultado;
    }
}
