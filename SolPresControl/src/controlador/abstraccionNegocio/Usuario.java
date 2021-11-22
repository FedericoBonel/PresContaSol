package controlador.abstraccionNegocio;

/**
 * Clase que abstrae a todos los usuarios del sistema
 */
public class Usuario extends Entidad {
    // Tipos de usuarios
    public final static String[] TIPOS_USUARIO = new String[]{"Administrador", "FiscalGeneral", "Fiscal", "Cuentadante"};
    // clave y tipo del usuario
    private String contra, tipo;

    // Constructor de usuario, nombre de usuario es el id en este caso
    public Usuario(String usuario, String contra, String tipo) {
        super(usuario);
        this.contra = contra;
        this.tipo = tipo;
    }

    // Devuelve contrasenia
    public String getContra() {
        return contra;
    }

    // Asigna nueva contrasenia
    public void setContra(String contra) {
        this.contra = contra;
    }

    // Devuelve tipo de usuario
    public String getTipo() {
        return tipo;
    }

    // Asigna nuevo tipo de usuario
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // devuelve los parametros como string a mostrar a los administradores
    public String toStringAdmin() {
        return super.toString() + " | " + contra + " | " + tipo;
    }

    // devuelve los parametros como string a mostrar al resto de usuarios
    @Override
    public String toString() {
        return super.toString() + " | " + "ESCONDIDA" + " | " + tipo;
    }
}
