package vista.interfazTexto;

import controlador.abstraccionNegocio.Usuario;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a municipios
 */
public class InterfazUsuarios {

    /**
     * Interfaz principal del gestor de usuarios
     */
    public static int usuariosInterfaz(Usuario usuario, Hashtable<String, Usuario> usuarios) {
        int input;
        ArrayList<String> opciones = new ArrayList<>();
        //Imprime los usuarios
        System.out.println(TextosConstantes.USUARIOS_CABECERA);
        System.out.println(TextosConstantes.USUARIOS_GESTOR_CAMPOS);
        for (String usuarioEnSistema : usuarios.keySet()) InterfazInformacion.imprimirUsuario(usuarios.get(usuarioEnSistema));
        // Restringe la entrada segun el usuario
        opciones.add(TextosConstantes.GESTOR_MODIFICAR);
        if (usuario.getTipo().equals("Administrador")) {
            opciones.add(TextosConstantes.GESTOR_ELIMINAR);
            opciones.add(TextosConstantes.GESTOR_CREAR);
        }
        opciones.add(TextosConstantes.MENU_TEXT_SALIR);
        input = InterfacesGenerales.interfazImprimirTodoYPedirEntero(opciones.toArray(new String[0]));
        return input;
    }

    /**
     * Interfaz de modificacion de usuarios
     */
    public static int modificarUsuarioInterfaz(Usuario usuarioModificador, Usuario usuarioAModificar) {
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
        opciones.add(TextosConstantes.MENU_TEXT_SALIR);
        return InterfacesGenerales.interfazImprimirTodoYPedirEntero(opciones.toArray(new String[0]));
    }

    /**
     * Interfaz de modificacion de clave de usuarios
     */
    public static String modificarContraInterfaz() {
        while (true) {
            // Pedir nueva clave
            System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
            String nuevaClave = InterfacesGenerales.interfazPedirDatosYEsperar(
                    new String[]{TextosConstantes.USUARIOS_NUEVA_CONTRA})[0];
            if (nuevaClave.isEmpty()) return null;
            // Verificar la clave
            if (!Verificadores.verificarFormatoClave(nuevaClave)) {
                System.out.println(TextosConstantes.USUARIO_CLAVE_FORMATO_ERROR);
                continue;
            }
            return nuevaClave;
        }
    }
}
