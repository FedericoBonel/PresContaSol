package com.fedebonel;

import com.fedebonel.controlador.MenuPrincipalControlador;
import com.fedebonel.controlador.controladorobjetos.ConvocatoriasControlador;
import com.fedebonel.controlador.controladorobjetos.MunicipiosControlador;
import com.fedebonel.controlador.controladorobjetos.PresentacionesControlador;
import com.fedebonel.controlador.controladorobjetos.UsuariosControlador;
import com.fedebonel.controlador.controladorpaneles.*;
import com.fedebonel.respositorios.ConvocatoriasRepositorio;
import com.fedebonel.respositorios.MunicipiosRepositorio;
import com.fedebonel.respositorios.PresentacionesRepositorio;
import com.fedebonel.respositorios.UsuariosRepositorio;
import com.fedebonel.respositorios.mysql.ConvocatoriasRepositorioMySQL;
import com.fedebonel.respositorios.mysql.MunicipiosRepositorioMySQL;
import com.fedebonel.respositorios.mysql.PresentacionesRepositorioMySQL;
import com.fedebonel.respositorios.mysql.UsuariosRepositorioMySQL;
import com.fedebonel.servicios.ConvocatoriasServicio;
import com.fedebonel.servicios.MunicipiosServicio;
import com.fedebonel.servicios.PresentacionesServicio;
import com.fedebonel.servicios.UsuariosServicio;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.ventanasemergentes.LoginVista;
import com.fedebonel.vista.menuprincipal.MenuPrincipalVista;

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
        inicializarContexto();
    }

    /**
     * Inicializacion del sistema
     */
    private static void inicializarContexto() {
        try {
            // Crear vistas --------------------------------------------------------------------------------------------
            LoginVista loginVista = new LoginVista();
            MenuPrincipalVista menuPrincipalVista = new MenuPrincipalVista();
            // Crear repositorios --------------------------------------------------------------------------------------
            UsuariosRepositorio usuariosRepositorio = new UsuariosRepositorioMySQL();
            MunicipiosRepositorio municipiosRepositorio = new MunicipiosRepositorioMySQL(usuariosRepositorio);
            ConvocatoriasRepositorio convocatoriasRepositorio = new ConvocatoriasRepositorioMySQL();
            PresentacionesRepositorio presentacionesRepositorio =
                    new PresentacionesRepositorioMySQL(
                            convocatoriasRepositorio,
                            usuariosRepositorio,
                            municipiosRepositorio);
            // Servicios -----------------------------------------------------------------------------------------------
            UsuariosServicio usuariosServicio = new UsuariosServicio(usuariosRepositorio);
            MunicipiosServicio municipiosServicio = new MunicipiosServicio(municipiosRepositorio);
            PresentacionesServicio presentacionesServicio = new PresentacionesServicio(presentacionesRepositorio);
            ConvocatoriasServicio convocatoriasServicio = new ConvocatoriasServicio(convocatoriasRepositorio);
            // Controladores  Generales --------------------------------------------------------------------------------
            UsuariosControlador usuariosControlador =
                    new UsuariosControlador(usuariosServicio);
            MunicipiosControlador municipiosControlador =
                    new MunicipiosControlador(municipiosServicio, usuariosServicio);
            PresentacionesControlador presentacionesControlador =
                    new PresentacionesControlador(presentacionesServicio, convocatoriasServicio, municipiosServicio);
            ConvocatoriasControlador convocatoriasControlador =
                    new ConvocatoriasControlador(convocatoriasServicio, presentacionesServicio);
            // Controladores de paneles --------------------------------------------------------------------------------
            PanelUsuariosControlador panelUsuariosControlador =
                    new PanelUsuariosControlador(usuariosControlador, usuariosServicio);
            PanelMunicipiosControlador panelMunicipiosControlador =
                    new PanelMunicipiosControlador(municipiosServicio, municipiosControlador);
            PanelPresentacionesControlador panelPresentacionesControlador =
                    new PanelPresentacionesControlador(presentacionesServicio,
                            presentacionesControlador);
            PanelConvocatoriasControlador panelConvocatoriasControlador =
                    new PanelConvocatoriasControlador(convocatoriasControlador, convocatoriasServicio, presentacionesServicio);
            PanelInformacionControlador panelInformacionControlador =
                    new PanelInformacionControlador(municipiosServicio, presentacionesServicio);
            // Controlador del menu principal --------------------------------------------------------------------------
            MenuPrincipalControlador menuPrincipalControlador =
                    new MenuPrincipalControlador(
                            loginVista,
                            menuPrincipalVista,
                            usuariosServicio,
                            panelUsuariosControlador,
                            panelMunicipiosControlador,
                            panelPresentacionesControlador,
                            panelConvocatoriasControlador,
                            panelInformacionControlador);
            loginVista.addControlador(menuPrincipalControlador);
            menuPrincipalVista.addControlador(menuPrincipalControlador);
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorEnOperacion(e);
            System.out.println(e.getMessage());
        }
    }

}
