package controlador;

import modelo.*;
import vista.LoginVista;
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
     * Controlador de usuarios a ser utilizado por el usuario
     */
    private UsuariosControlador usuariosControlador;
    /**
     * Controlador de municipios a ser utilizado por el usuario
     */
    private MunicipiosControlador municipiosControlador;
    /**
     * Controlador de presentaciones a ser utilizado por el usuario
     */
    private PresentacionesControlador presentacionesControlador;
    /**
     * Controlador de convocatoria a ser utilizado por el usuario
     */
    private ConvocatoriasControlador convocatoriasControlador;

    /**
     * Constructor del controlador del menu principal
     *
     * @param loginVista         Vista de autenticacion desde el cual el usuario se autentica
     * @param menuPrincipalVista Vista de menu principal que utilizara el usuario
     */
    public MenuPrincipalControlador(LoginVista loginVista, MenuPrincipalVista menuPrincipalVista) {
        if (loginVista == null) loginVista = new LoginVista();
        if (menuPrincipalVista == null) menuPrincipalVista = new MenuPrincipalVista();
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
        usuariosControlador = new UsuariosControlador(usuarioLogueado);
        convocatoriasControlador = new ConvocatoriasControlador(usuarioLogueado);
        presentacionesControlador = new PresentacionesControlador(usuarioLogueado);
        municipiosControlador = new MunicipiosControlador(usuarioLogueado);
        // Muestra la informacion para todos los usuarios
        menuPrincipalVista.panelInformacion
                .mostrarInformacion(MunicipiosControlador.leerMunicipiosBaseDeDatos(),
                        PresentacionesControlador.leerPresentacionesBaseDeDatos());
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
        // Carga los usuarios
        menuPrincipalVista.panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[0]))
            menuPrincipalVista.panelUsuarios.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[1]))
            menuPrincipalVista.panelUsuarios.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[6]))
            menuPrincipalVista.panelUsuarios.eliminarBoton.setVisible(false);
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
        // Carga los municipios
        menuPrincipalVista.panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[0]))
            menuPrincipalVista.panelMunicipios.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2]) &&
                !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3]))
            menuPrincipalVista.panelMunicipios.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[6]))
            menuPrincipalVista.panelMunicipios.eliminarBoton.setVisible(false);
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
        // Carga las convocatorias
        menuPrincipalVista.panelConvocatorias.mostrarConvocatorias(convocatoriasControlador.getConvocatoriasVisibles(),
                PresentacionesControlador.leerPresentacionesBaseDeDatos());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[0]))
            menuPrincipalVista.panelConvocatorias.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1]))
            menuPrincipalVista.panelConvocatorias.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar ninguna quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[7]))
            menuPrincipalVista.panelConvocatorias.eliminarBoton.setVisible(false);
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
        // Carga las presentaciones
        menuPrincipalVista.panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[0]))
            menuPrincipalVista.panelPresentaciones.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4]) &&
                !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]))
            menuPrincipalVista.panelPresentaciones.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar ninguna quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[8]))
            menuPrincipalVista.panelPresentaciones.eliminarBoton.setVisible(false);
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
                setMenuPrincipalVista(new MenuPrincipalVista());
                menuPrincipalVista.addControlador(this);
                usuarioLogueado = null;
                // Lanza una nueva vista de autenticacion
                setLoginVista(new LoginVista());
                loginVista.addControlador(this);
            }

            // Si el usuario desea actualizar los datos del panel de informacion
            case (StringsFinales.ACTUALIZAR + StringsFinales.INFORMACION) ->
                    menuPrincipalVista.panelInformacion.mostrarInformacion(
                            MunicipiosControlador.leerMunicipiosBaseDeDatos(), PresentacionesControlador.leerPresentacionesBaseDeDatos());

            // Si el usuario desea crear otro usuario
            case (StringsFinales.CREAR + StringsFinales.USUARIO) -> usuariosControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar otro usuario
            case (StringsFinales.ELIMINAR + StringsFinales.USUARIO) -> {
                int filaSeleccionada = menuPrincipalVista.panelUsuarios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            menuPrincipalVista.panelUsuarios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    int decision = JOptionPane.showConfirmDialog(new JFrame(),
                            StringsFinales.ESTA_SEGURO,
                            StringsFinales.ELIMINAR + " " + identificador,
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Usuario usuarioAEliminar = UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(identificador);
                        usuariosControlador.eliminarUsuario(usuarioAEliminar);
                        // Actualiza los datos
                        menuPrincipalVista.panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
                    }
                }
            }
            // Si el usuario desea modificar a otro usuario
            case (StringsFinales.MODIFICAR + StringsFinales.USUARIO) -> {
                int filaSeleccionada = menuPrincipalVista.panelUsuarios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            menuPrincipalVista.panelUsuarios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Usuario usuarioAModificar = UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(identificador);
                    usuariosControlador.mostrarFormularioModificar(usuarioAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de los usuarios
            case (StringsFinales.ACTUALIZAR + StringsFinales.USUARIO) -> menuPrincipalVista.panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());

            // Si el usuario desea crear un nuevo municipio
            case (StringsFinales.CREAR + StringsFinales.MUNICIPIO) -> municipiosControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar un municipio
            case (StringsFinales.ELIMINAR + StringsFinales.MUNICIPIO) -> {
                int filaSeleccionada = menuPrincipalVista.panelMunicipios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            menuPrincipalVista.panelMunicipios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    int decision = JOptionPane.showConfirmDialog(new JFrame(),
                            StringsFinales.ESTA_SEGURO,
                            StringsFinales.ELIMINAR + " " + identificador,
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Municipio municipioAEliminar = MunicipiosControlador.leerMunicipiosBaseDeDatos().getMunicipio(identificador);
                        municipiosControlador.eliminarMunicipio(municipioAEliminar);
                        // Actualiza los datos
                        menuPrincipalVista.panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
                    }
                }
            }
            // Si el usuario desea modificar a un municipio
            case (StringsFinales.MODIFICAR + StringsFinales.MUNICIPIO) -> {
                int filaSeleccionada = menuPrincipalVista.panelMunicipios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(menuPrincipalVista.panelMunicipios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Municipio municipioAModificar = MunicipiosControlador.leerMunicipiosBaseDeDatos().getMunicipio(identificador);
                    municipiosControlador.mostrarFormularioModificar(municipioAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de los municipios
            case (StringsFinales.ACTUALIZAR + StringsFinales.MUNICIPIO) -> menuPrincipalVista.panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());

            // Si el usuario desea crear una nueva convocatoria
            case (StringsFinales.CREAR + StringsFinales.CONVOCATORIA) -> convocatoriasControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar una convocatoria
            case (StringsFinales.ELIMINAR + StringsFinales.CONVOCATORIA) -> {
                int filaSeleccionada = menuPrincipalVista.panelConvocatorias.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(menuPrincipalVista.panelConvocatorias.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    int decision =
                            JOptionPane.showConfirmDialog(new JFrame(),
                                    StringsFinales.ESTA_SEGURO,
                                    StringsFinales.ELIMINAR + " " + identificador,
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Convocatoria convocatoriaAEliminar = ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoria(identificador);
                        convocatoriasControlador.eliminarConvocatoria(convocatoriaAEliminar);
                        // Actualiza los datos
                        menuPrincipalVista.panelConvocatorias.mostrarConvocatorias(convocatoriasControlador.getConvocatoriasVisibles(),
                                PresentacionesControlador.leerPresentacionesBaseDeDatos());
                    }
                }
            }
            // Si el usuario desea modificar a una convocatoria
            case (StringsFinales.MODIFICAR + StringsFinales.CONVOCATORIA) -> {
                int filaSeleccionada = menuPrincipalVista.panelConvocatorias.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(menuPrincipalVista.panelConvocatorias.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Convocatoria convocatoriaAModificar = ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoria(identificador);
                    convocatoriasControlador.mostrarFormularioModificar(convocatoriaAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de las convocatorias
            case (StringsFinales.ACTUALIZAR + StringsFinales.CONVOCATORIA) -> menuPrincipalVista.panelConvocatorias.mostrarConvocatorias(
                    convocatoriasControlador.getConvocatoriasVisibles(), PresentacionesControlador.leerPresentacionesBaseDeDatos());

            // Si el usuario desea crear una nueva presentacion
            case (StringsFinales.CREAR + StringsFinales.PRESENTACION) -> presentacionesControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar una presentacion
            case (StringsFinales.ELIMINAR + StringsFinales.PRESENTACION) -> {
                int filaSeleccionada = menuPrincipalVista.panelPresentaciones.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(menuPrincipalVista.panelPresentaciones.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    int decision =
                            JOptionPane.showConfirmDialog(new JFrame(),
                                    StringsFinales.ESTA_SEGURO,
                                    StringsFinales.ELIMINAR + " " + identificador,
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Presentacion presentacionAEliminar = PresentacionesControlador.leerPresentacionesBaseDeDatos().getPresentacion(identificador);
                        presentacionesControlador.eliminarPresentacion(presentacionAEliminar);
                        // Actualiza los datos
                        menuPrincipalVista.panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
                    }
                }
            }
            // Si el usuario desea modificar a una presentacion
            case (StringsFinales.MODIFICAR + StringsFinales.PRESENTACION) -> {
                int filaSeleccionada = menuPrincipalVista.panelPresentaciones.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(menuPrincipalVista.panelPresentaciones.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Presentacion presentacionAModificar = PresentacionesControlador.leerPresentacionesBaseDeDatos().getPresentacion(identificador);
                    presentacionesControlador.mostrarFormularioModificar(presentacionAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de las presentaciones
            case (StringsFinales.ACTUALIZAR + StringsFinales.PRESENTACION) ->
                    menuPrincipalVista.panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
        }
    }
}