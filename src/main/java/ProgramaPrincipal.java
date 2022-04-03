import controlador.MenuPrincipalControlador;
import controlador.controladorObjetos.ConvocatoriasControlador;
import controlador.controladorObjetos.MunicipiosControlador;
import controlador.controladorObjetos.PresentacionesControlador;
import controlador.controladorObjetos.UsuariosControlador;
import controlador.controladorPaneles.*;
import respositorios.ConvocatoriasRepositorio;
import respositorios.MunicipiosRepositorio;
import respositorios.PresentacionesRepositorio;
import respositorios.UsuariosRepositorio;
import respositorios.mySQL.ConvocatoriasRepositorioMySQL;
import respositorios.mySQL.MunicipiosRepositorioMySQL;
import respositorios.mySQL.PresentacionesRepositorioMySQL;
import respositorios.mySQL.UsuariosRepositorioMySQL;
import servicios.ConvocatoriasServicio;
import servicios.MunicipiosServicio;
import servicios.PresentacionesServicio;
import servicios.UsuariosServicio;
import vista.errores.ErrorVistaGenerador;
import vista.formularios.ventanasEmergentes.LoginVista;
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
