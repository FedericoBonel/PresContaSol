package controlador;

import controlador.abstraccionNegocio.Municipio;
import controlador.abstraccionNegocio.Usuario;
import controlador.herramientas.InicializadoresVariables;
import controlador.modulosFuncionales.MenuPrincipal;

import java.util.Hashtable;

import static controlador.modulosFuncionales.Ingresar.ingresar;
import static vista.TextosConstantes.LOGIN_TEXT_BIENVENIDO;

/**
 * Programa principal del sistema, contiene el metodo main a ejecutar
 */
public class ProgramaMain {
    public static void main(String[] args) {
        // Variables
        Hashtable<String, Usuario> usuarios = new Hashtable<>();
        Hashtable<String, Municipio> municipios = new Hashtable<>();
        Usuario usuario;
        // Inicializa las variables del programa
        InicializadoresVariables.inicializarMunicipios(municipios);
        InicializadoresVariables.inicializarUsuarios(usuarios);
        // Verifica usuario
        System.out.println(LOGIN_TEXT_BIENVENIDO);
        usuario = ingresar(usuarios);
        // Lanza menu principal
        MenuPrincipal.menuPrincipal(usuario, usuarios, municipios);
    }
}
