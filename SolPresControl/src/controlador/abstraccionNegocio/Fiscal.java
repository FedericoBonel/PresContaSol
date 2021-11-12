package controlador.abstraccionNegocio;

import java.util.Hashtable;

/**
 * Clase que abstrae a todos los Fiscales del sistema
 */

public class Fiscal extends Usuario {
    // Constante que mantiene el tipo de usuario
    private static final String TIPO_FISCAL = "Fiscal";
    // Todos los cuentadantes asignados al fiscal <identificador, objeto>
    private final Hashtable<String, Cuentadante> cuentadantes;

    // Constructor de Fiscal
    public Fiscal(String usuario, String contra) {
        super(usuario, contra, TIPO_FISCAL);
        cuentadantes = new Hashtable<>();
    }

    // Devuelve los cuentadantes del fiscal
    public Hashtable<String, Cuentadante> getCuentadantes() {
        return cuentadantes;
    }

    // Verifica si el cuentadante pasado corresponde a un Cuentadante del Fiscal
    public boolean containsCuentadante(Cuentadante cuentadante) {
        return cuentadantes.containsKey(cuentadante.getId());
    }

    // Asigna el cuentadante a este fiscal
    protected void addCuentadante(Cuentadante cuentadante) {
        cuentadantes.put(cuentadante.getId(), cuentadante);
    }

    // Desasigna cuentadante de este fiscal
    protected void removeCuentadante(Cuentadante cuentadante) {
        cuentadantes.remove(cuentadante.getId());
    }

}
