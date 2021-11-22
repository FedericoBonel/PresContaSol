package controlador.modulosFuncionales;

import controlador.abstraccionNegocio.Usuario;
import controlador.herramientas.Verificadores;
import vista.interfazTexto.UIMenuPrincipal;

import java.util.Hashtable;

import static vista.TextosConstantes.LOGIN_TEXTO_USUARIOINCORRECTO;

/**
 * Clase que contiene el modulo de ingreso para los usuarios
 */
public class Ingresar {

    /**
     * Pide usuario y clave y los compara con el hashtable pasado
     */
    public static Usuario ingresar(Hashtable<String, Usuario> usuarios) {
        String[] usuarioContraInput;
        Usuario usuario;
        // Pide usuario y clave hasta que sean correctos
        do {
            usuarioContraInput = UIMenuPrincipal.interfazLogin();
            usuario = Verificadores.ingresarConClave(usuarioContraInput[0], usuarioContraInput[1], usuarios);
            if (usuario == null) System.out.println(LOGIN_TEXTO_USUARIOINCORRECTO);
        } while (usuario == null);
        return usuario;
    }
}
