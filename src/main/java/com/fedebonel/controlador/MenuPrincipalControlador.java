package com.fedebonel.controlador;

import com.fedebonel.controlador.controladorpaneles.PanelControlador;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.UsuariosServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.ventanasemergentes.LoginVista;
import com.fedebonel.vista.menuprincipal.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Controlador del menu principal, funciona como un puente entre el usuario, la vista del menu principal
 * y los controladores y vistas de cada objeto
 */
public class MenuPrincipalControlador implements ActionListener {

    /**
     * Servicio de usuarios
     */
    private final UsuariosServicio usuariosServicio;
    /**
     * Controlador del panel de usuarios
     */
    private final PanelControlador<UsuariosPanel> panelUsuariosControlador;
    /**
     * Controlador del panel de municipios
     */
    private final PanelControlador<MunicipiosPanel> panelMunicipiosControlador;
    /**
     * Controlador del panel de presentaciones
     */
    private final PanelControlador<PresentacionesPanel> panelPresentacionesControlador;
    /**
     * Controlador del panel de convocatorias
     */
    private final PanelControlador<ConvocatoriasPanel> panelConvocatoriasControlador;
    /**
     * Controlador del panel de informacion
     */
    private final PanelControlador<InformacionPanel> panelInformacionControlador;
    /**
     * Vista de autenticacion que gestiona este controlador
     */
    private LoginVista loginVista;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private MenuPrincipalVista menuPrincipalVista;

    /**
     * Constructor del controlador del menu principal
     *
     * @param loginVista                     Vista de autenticacion desde el cual el usuario se autentica
     * @param menuPrincipalVista             Vista de menu principal que utilizara el usuario
     * @param usuariosServicio               Servicio de usuarios
     * @param panelConvocatoriasControlador  Controlador del panel de convocatorias a utilizar
     * @param panelInformacionControlador    Controlador del panel de informacion a utilizar
     * @param panelMunicipiosControlador     Controlador del panel de municipios a utilizar
     * @param panelPresentacionesControlador Controlador del panel de presentaciones a utilizar
     * @param panelUsuariosControlador       Controlador del panel de usuarios a utilizar
     */
    public MenuPrincipalControlador(LoginVista loginVista,
                                    MenuPrincipalVista menuPrincipalVista,
                                    UsuariosServicio usuariosServicio,
                                    PanelControlador<UsuariosPanel> panelUsuariosControlador,
                                    PanelControlador<MunicipiosPanel> panelMunicipiosControlador,
                                    PanelControlador<PresentacionesPanel> panelPresentacionesControlador,
                                    PanelControlador<ConvocatoriasPanel> panelConvocatoriasControlador,
                                    PanelControlador<InformacionPanel> panelInformacionControlador) {
        this.usuariosServicio = usuariosServicio;
        this.panelUsuariosControlador = panelUsuariosControlador;
        this.panelMunicipiosControlador = panelMunicipiosControlador;
        this.panelPresentacionesControlador = panelPresentacionesControlador;
        this.panelConvocatoriasControlador = panelConvocatoriasControlador;
        this.panelInformacionControlador = panelInformacionControlador;
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
        loginVista.ventana.dispose();
        // Inicializa los controladores a ser utilizados por el usuario
        panelUsuariosControlador.setPanel(menuPrincipalVista.panelUsuarios);
        panelUsuariosControlador.setUsuarioLogueado(usuarioLogueado);

        panelMunicipiosControlador.setPanel(menuPrincipalVista.panelMunicipios);
        panelMunicipiosControlador.setUsuarioLogueado(usuarioLogueado);

        panelPresentacionesControlador.setPanel(menuPrincipalVista.panelPresentaciones);
        panelPresentacionesControlador.setUsuarioLogueado(usuarioLogueado);

        panelConvocatoriasControlador.setPanel(menuPrincipalVista.panelConvocatorias);
        panelConvocatoriasControlador.setUsuarioLogueado(usuarioLogueado);

        panelInformacionControlador.setPanel(menuPrincipalVista.panelInformacion);
        panelInformacionControlador.setUsuarioLogueado(usuarioLogueado);
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
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[12])
                && !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[14])) {
            menuPrincipalVista.tabs.setEnabledAt(MenuPrincipalVista.INDICE_PANEL_PRESENTACIONES, false);
        }
    }

    /**
     * Autentica al usuario en el sistema con su clave
     *
     * @param nombre Nombre de usuario a verificar
     * @param clave  Clave del usuario a verificar
     * @return true si el usuario es autenticado correctamente, false en caso contrario
     */
    private boolean autenticarUsuario(String nombre, String clave) throws SQLException {
        Usuario usuarioAutenticar = usuariosServicio.leerPorID(nombre);
        if (usuarioAutenticar == null) return false;
        return (usuarioAutenticar.certificaClave(clave));
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
                try {
                    if (autenticarUsuario(nombreUsuario, clave)) {
                        inicializarMenuPrincipal(usuariosServicio.leerPorID(nombreUsuario));
                    } else {
                        ErrorVistaGenerador.mostrarErrorAutenticacion();
                    }
                } catch (Exception e) {
                    ErrorVistaGenerador.mostrarErrorDB(e);
                }
            }
            // Si el usuario desea salir del sistema
            case (StringsFinales.SALIR) -> {
                // Cierra el menu principal
                menuPrincipalVista.ventana.dispose();
                // Reinicialo para controlar los permisos del siguiente usuario
                menuPrincipalVista.reiniciarVista();
                menuPrincipalVista.addControlador(this);
                // Lanza una nueva vista de autenticacion
                loginVista.reiniciarVista();
                loginVista.addControlador(this);
            }

        }
    }
}
