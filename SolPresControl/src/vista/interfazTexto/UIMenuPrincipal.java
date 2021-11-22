package vista.interfazTexto;

import controlador.abstraccionNegocio.Usuario;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;

import java.util.Scanner;

/**
 * Coleccion de interfaces de texto relacionadas a Menu principal
 */
public class UIMenuPrincipal {
    /**
     * Interfaz login, pide usuario y clave y lo devuelve como array de 2 posiciones
     */
    public static String[] interfazLogin(){
        // pide el input
        String[] usuarioContra = UIGenerales.interfazPedirDatosYEsperar(new String[]{TextosConstantes.LOGIN_TEXTO_INSERTEUSUARIO,
                TextosConstantes.LOGIN_TEXTO_CLAVE});
        //Saca los espacios y devuelvelo
        usuarioContra[0] = usuarioContra[0].strip();
        usuarioContra[1] = usuarioContra[1].strip();
        return usuarioContra;
    }

    /**
     * Interfaz menu principal toma input, lo valida y lo devuelve
     */
    public static int interfazMenuPrincipal(Usuario usuario){
        Scanner in = new Scanner(System.in);
        int input;
        while (true) {
            imprimirMenuPrincipal(usuario);
            // Toma la entrada y verificala
            System.out.println(TextosConstantes.INGRESE_INPUT);
            // Verifica la entrada
            input = Verificadores.verificarEnteroEnRango(in, 0,5);
            // En caso de error de input informa al usuario y vuelve a pedirlo
            if (input == -1){
                System.out.println(TextosConstantes.ERROR_SOLO_ENTEROS);
                continue;
            }
            // Verificacion de entrada y tipo de usuario
            if (input <= 4) return input;
            if (input == 5 && (usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("FiscalGeneral")))
                return input;
            System.out.println(TextosConstantes.ERROR_SOLO_ENTEROS);
        }
    }

    // Imprime menu principal en pantalla
    private static void imprimirMenuPrincipal(Usuario usuario){
        // Imprime el menu
        System.out.println(TextosConstantes.MENU_CABECERA);
        System.out.println(TextosConstantes.MENU_CONVOCATORIAS);
        System.out.println(TextosConstantes.MENU_PRESENTACIONES);
        System.out.println(TextosConstantes.MENU_MUNICIPIOS);
        System.out.println(TextosConstantes.MENU_INFORMACION);
        //Solo para los administradores y fiscales generales
        if (usuario.getTipo().equals("Administrador") ||  usuario.getTipo().equals("FiscalGeneral"))
            System.out.println(TextosConstantes.MENUADMIN_USUARIOS);
        System.out.println(TextosConstantes.MENU_SALIR);
    }
}
