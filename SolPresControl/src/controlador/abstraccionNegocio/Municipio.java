package controlador.abstraccionNegocio;

/**
 * Clase que abstrae a un municipio
 */
public class Municipio extends Entidad {
    // Categoria del municipio
    private int categoria;
    // Objeto de su cuentadante
    private Cuentadante cuentadante;

    public Municipio(String id, int categoria) {
        super(id);
        this.categoria = categoria;
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
}
