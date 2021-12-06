package logInUsuario;

import abstraccionNegocio.ColeccionUsuarios;
import abstraccionNegocio.Usuario;

/**
 * Clase que contiene el modulo de ingreso para los usuarios
 *
 * @author Bonel Federico
 */
public class Ingresar {

    /**
     * Valida el usuario en el sistema con su identificador y clave
     *
     * @param usuario  Identificador del usuario que desea ingresar como string
     * @param clave    Clave del usuario como string
     * @param usuarios Coleccion de todos los usuarios del sistema
     * @throws IllegalArgumentException Si la informacion del usuario es incorrecta
     */
    public static Usuario ingresarConClave(String usuario, String clave, ColeccionUsuarios usuarios) throws IllegalArgumentException {
        //Verifica que el usuario corresponda con la contraseña
        Usuario usuarioAVerificar = usuarios.getUsuario(usuario);
        if (!usuarioAVerificar.certificaClave(clave))
            throw new IllegalArgumentException("Informacion incorrecta");
        return usuarioAVerificar;
    }
}
