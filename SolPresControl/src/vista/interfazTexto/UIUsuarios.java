package vista.interfazTexto;

import controlador.abstraccionNegocio.Cuentadante;
import controlador.abstraccionNegocio.Fiscal;
import controlador.abstraccionNegocio.Usuario;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a municipios
 */
public class UIUsuarios {

    /**
     * Interfaz principal del gestor de usuarios
     */
    public static int interfazUsuarios(Usuario usuario, Hashtable<String, Usuario> usuarios) {
        int input;
        ArrayList<String> opciones = new ArrayList<>();
        //Imprime los usuarios
        System.out.println(TextosConstantes.USUARIOS_CABECERA);
        System.out.println(TextosConstantes.USUARIOS_GESTOR_CAMPOS);
        for (String usuarioEnSistema : usuarios.keySet()) {
            Usuario usuarioObj = usuarios.get(usuarioEnSistema);
            // Si el usuario es administrador
            if (usuario.getTipo().equals("Administrador")) System.out.println(usuarioObj.toStringAdmin());
            // Si no el usuario es fiscal general imprime solo los fiscales y cuentadantes
            else if (usuarioObj instanceof Cuentadante || usuarioObj instanceof Fiscal)
                System.out.println(usuarios.get(usuarioEnSistema).toString());
        }
        // Restringe la entrada segun el usuario
        opciones.add(TextosConstantes.GESTOR_MODIFICAR);
        if (usuario.getTipo().equals("Administrador")) {
            opciones.add(TextosConstantes.GESTOR_ELIMINAR);
            opciones.add(TextosConstantes.GESTOR_CREAR);
        }
        opciones.add(TextosConstantes.MENU_SALIR);
        input = UIGenerales.interfazMostrarOpcionesPedirEntero(opciones.toArray(new String[0]));
        return input;
    }

    /**
     * Interfaz de modificacion de usuarios
     */
    public static int interfazModificarUsuario(Usuario usuarioModificador, Usuario usuarioAModificar) {
        System.out.println(TextosConstantes.USUARIOS_QUE_ATRIBUTO_MODIFICAR);
        ArrayList<String> opciones = new ArrayList<>();
        // Operaciones para fiscal
        if (usuarioAModificar.getTipo().equals("Fiscal")){
            opciones.add(TextosConstantes.FISCAL_CUENTADANTES);
        }
        // Operaciones para cuentadante
        else if (usuarioAModificar.getTipo().equals("Cuentadante")) {
            opciones.add(TextosConstantes.CUENTADANTE_FISCAL);
        }
        // Solo si modificador es adminsitrador podra cambiar clave
        if (usuarioModificador.getTipo().equals("Administrador")) {
            // Operaciones comunes entre usuarios
            opciones.add(TextosConstantes.USUARIOS_CONTRA);
        }
        opciones.add(TextosConstantes.MENU_SALIR);
        return UIGenerales.interfazMostrarOpcionesPedirEntero(opciones.toArray(new String[0]));
    }

    /**
     * Interfaz de modificacion de clave de usuarios
     */
    public static String interfazModificarContra() {
        while (true) {
            // Pedir nueva clave
            System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
            String nuevaClave = UIGenerales.interfazPedirDatosYEsperar(
                    new String[]{TextosConstantes.USUARIOS_NUEVA_CONTRA})[0];
            if (nuevaClave.isEmpty()) return null;
            // Verificar formato de la clave
            if (!Verificadores.verificarClave(nuevaClave)) {
                System.out.println(TextosConstantes.USUARIO_CLAVE_FORMATO_ERROR);
                continue;
            }
            return nuevaClave;
        }
    }
}
