package main;

import controlador.*;
import vista.formularios.LoginVista;
import vista.menuPrincipal.MenuPrincipalVista;

/**
 * Clase que contiene el metodo main donde se lanza todos los componentes necesarios para la ejecucion del programa
 */
public class ProgramaPrincipal {
    /**
     * Metodo main que se ejecuta cuando se ejecuta el programa
     *
     * @param args Argumentos pasados desde consola como un array de strings
     */
    public static void main (String[] args) {
        try {
            // Crear vistas
            LoginVista loginVista = new LoginVista();
            MenuPrincipalVista menuPrincipalVista = new MenuPrincipalVista();
            // Crear el controlador del menu principal desde donde se gestionan todas las operaciones del usuario
            MenuPrincipalControlador menuPrincipalControlador = new MenuPrincipalControlador(loginVista, menuPrincipalVista);
            loginVista.addControlador(menuPrincipalControlador);
            menuPrincipalVista.addControlador(menuPrincipalControlador);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
