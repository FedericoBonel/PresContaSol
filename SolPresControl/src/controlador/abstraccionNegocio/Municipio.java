package controlador.abstraccionNegocio;

import java.util.Hashtable;

/**
 * Clase que abstrae a un municipio
 */
public class Municipio extends Entidad {
    // Categoria del municipio
    private int categoria;
    // Objeto de su cuentadante
    private Cuentadante cuentadante;
    // Presentaciones del municipio
    private Hashtable<String, Presentacion> presentaciones;

    public Municipio(String id, int categoria) {
        super(id);
        this.categoria = categoria;
        presentaciones = new Hashtable<>();
    }

    // Devuelve la categoria del Municipio
    public int getCategoria() {
        return categoria;
    }

    // Asigna la categoria del Municipio
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    // Devuelve el Cuentadante del Municipio
    public Cuentadante getCuentadante() {
        return cuentadante;
    }

    // Asigna el cuentadante del Municipio
    protected void setCuentadante(Cuentadante cuentadante) {
        this.cuentadante = cuentadante;
    }

    // Verifica si el cuentadante pasado corresponde al cuentadante del Municipio
    public boolean isCuentadante(Cuentadante cuentadante) {
        if (this.cuentadante == null) return false;
        return this.cuentadante.getId().equals(cuentadante.getId());
    }

    // Verifica si el municipio tiene asignado un cuentadante o no
    public boolean hasCuentadante() {
        return cuentadante != null;
    }

    // Devuelve las presentaciones del municipio
    public Hashtable<String, Presentacion> getPresentaciones() {
        return presentaciones;
    }

    // Devuelve la presentacion deseada del municipio, si no la tiene devuelve null
    public Presentacion getPresentacion(String presentacion) {
        return presentaciones.get(presentacion);
    }

    // Verifica si el identificador pasado corresponde a una presentacion del municipio
    public boolean containsPresentacion(Presentacion presentacion) {
        return presentaciones.containsKey(presentacion.getId());
    }

    // Asigna la presentacion al municipio
    protected void addPresentacion(Presentacion presentacion) {
        presentaciones.put(presentacion.getId(), presentacion);
    }

    // Remueve la presentacion del cuentadante
    protected void removePresentacion(Presentacion presentacion) {
        presentaciones.remove(presentacion.getId());
    }

    // Devuelve todos los atributos de la instancia como string
    @Override
    public String toString() {
        String resultado = super.toString() + " | " +categoria;
        if (this.hasCuentadante()) resultado += " | " + cuentadante.getId();
        else resultado += " | " + "null";
        return resultado;
    }
}
