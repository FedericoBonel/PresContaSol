package controlador;

import controlador.controladorObjetos.UsuariosControlador;
import controlador.controladorPaneles.*;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.formularios.LoginVista;
import vista.StringsFinales;
import vista.menuPrincipal.MenuPrincipalVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del menu principal, funciona como un puente entre el usuario, la vista del menu principal
 * y los controladores y vistas de cada objeto
 */
public class MenuPrincipalControlador implements ActionListener {

    /**
     * Vista de autenticacion que gestiona este controlador
     */
    private LoginVista loginVista;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private MenuPrincipalVista menuPrincipalVista;
    /**
     * Usuario autenticado que utilizara esta vista
     */
    private Usuario usuarioLogueado;
    /**
     * Controlador del panel de usuarios
     */
    private PanelUsuariosControlador panelUsuariosControlador;
    /**
     * Controlador del panel de municipios
     */
    private PanelMunicipiosControlador panelMunicipiosControlador;
    /**
     * Controlador del panel de presentaciones
     */
    private PanelPresentacionesControlador panelPresentacionesControlador;
    /**
     * Controlador del panel de convocatorias
     */
    private PanelConvocatoriasControlador panelConvocatoriasControlador;
    /**
     * Controlador del panel de informacion
     */
    private PanelInformacionControlador panelInformacionControlador;


    /**
     * Constructor del controlador del menu principal
     *
     * @param loginVista         Vista de autenticacion desde el cual el usuario se autentica
     * @param menuPrincipalVista Vista de menu principal que utilizara el usuario
     */
    public MenuPrincipalControlador(LoginVista loginVista, MenuPrincipalVista menuPrincipalVista) {
        setLoginVista(loginVista);
        setMenuPrincipalVista(menuPrincipalVista);
    }

    /**
     * Asigna la vista de autenticacion que este controlador debe manejar
     *
     * @param vista Vista de autenticacion a asignar
     */
    public void setLoginVista(LoginVista vista) {
        this.loginVista = vista;
    }

    /**
     * Asigna la vista de menu principal que este controlador debe manejar
     *
     * @param vista Vista de menuprincipal a asignar
     */
    public void setMenuPrincipalVista(MenuPrincipalVista vista) {
        this.menuPrincipalVista = vista;
    }

    /**
     * Inicializa el menu principal con todas sus vistas y variables para el usuario logueado en cuestion
     *
     * @param usuarioLogueado usuario logueado que utilizara el sistema
     */
    private void inicializarMenuPrincipal(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        loginVista.ventana.dispose();
        // Inicializa los controladores a ser utilizados por el usuario
        panelUsuariosControlador = new PanelUsuariosControlador(menuPrincipalVista.panelUsuarios, usuarioLogueado);
        panelMunicipiosControlador = new PanelMunicipiosControlador(menuPrincipalVista.panelMunicipios, usuarioLogueado);
        panelPresentacionesControlador = new PanelPresentacionesControlador(menuPrincipalVista.panelPresentaciones, usuarioLogueado);
        panelConvocatoriasControlador = new PanelConvocatoriasControlador(menuPrincipalVista.panelConvocatorias, usuarioLogueado);
        panelInformacionControlador = new PanelInformacionControlador(menuPrincipalVista.panelInformacion, usuarioLogueado);
        // Configura los paneles
        configurarPanelUsuarios(usuarioLogueado);
        configurarPanelMunicipios(usuarioLogueado);
        configurarPanelConvocatorias(usuarioLogueado);
        configurarPanelPresentaciones(usuarioLogueado);
        // Haz el menu visible
        menuPrincipalVista.ventana.setVisible(true);
    }

    /**
     * Configura el panel de usuarios en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelUsuarios(Usuario usuarioLogueado) {
        // Si no tiene permiso para ver usuarios no muestres nada
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[9])) {
            menuPrincipalVista.tabs.setEnabledAt(MenuPrincipalVista.INDICE_PANEL_USUARIOS, false);
        }
    }

    /**
     * Configura el panel de municipios en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelMunicipios(Usuario usuarioLogueado) {
        // Si no tiene permiso para ver municipios no muestres nada
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[14])) {
            menuPrincipalVista.tabs.setEnabledAt(MenuPrincipalVista.INDICE_PANEL_MUNICIPIOS, false);
        }
    }

    /**
     * Configura el panel de convocatorias en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelConvocatorias(Usuario usuarioLogueado) {
        // Si no tiene permiso para ver convocatorias no muestres nada
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[13])) {
            menuPrincipalVista.tabs.setEnabledAt(MenuPrincipalVista.INDICE_PANEL_CONVOCATORIAS, false);
        }
    }

    /**
     * Configura el panel de presentaciones en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelPresentaciones(Usuario usuarioLogueado) {
        // Si no tiene permiso para ver presentaciones no muestres nada
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[12]) &&
                !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[14])) {
            menuPrincipalVista.tabs.setEnabledAt(MenuPrincipalVista.INDICE_PANEL_PRESENTACIONES, false);
        }
    }

    /**
     * Lee las interacciones del usuario sobre las vistas que este controlador gestiona
     *
     * @param evento Evento generado por una interaccion del usuario
     */
    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        switch (accion) {
            // Si el usuario desea autenticarse
            case (StringsFinales.INGRESAR) -> {
                String nombreUsuario = loginVista.usuarioCampo.getText();
                String clave = String.valueOf(loginVista.claveCampo.getPassword());
                if (UsuariosControlador.autenticarUsuario(nombreUsuario, clave)) {
                    inicializarMenuPrincipal(UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(nombreUsuario));
                } else {
                    // En caso de que no sea informacion valida muestra mensaje de error
                    JOptionPane.showMessageDialog(new JFrame(), StringsFinales.ERROR_USUARIO_CLAVE, "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_USUARIO_CLAVE);
                }
            }
            // Si el usuario desea salir del sistema
            case (StringsFinales.SALIR) -> {
                // Cierra el menu principal
                menuPrincipalVista.ventana.dispose();
                // Reinicialo para controlar los permisos del siguiente usuario
                menuPrincipalVista.reiniciarVista();
                menuPrincipalVista.addControlador(this);
                usuarioLogueado = null;
                // Lanza una nueva vista de autenticacion
                loginVista.reiniciarVista();
                loginVista.addControlador(this);
            }

        }
    }
}