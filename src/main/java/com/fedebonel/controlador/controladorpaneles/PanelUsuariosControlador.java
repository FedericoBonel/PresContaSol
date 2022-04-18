package com.fedebonel.controlador.controladorpaneles;

import com.fedebonel.controlador.controladorobjetos.UsuariosControlador;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.UsuariosServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.ventanasemergentes.FormularioOpcionesGenerador;
import com.fedebonel.vista.menuprincipal.UsuariosPanel;

import java.awt.event.ActionEvent;

/**
 * Controlador del panel usuarios, funciona como un puente entre el usuario, la vista del panel de usuarios
 * y el controlador de usuarios
 */
public class PanelUsuariosControlador implements PanelControlador<UsuariosPanel> {
    /**
     * Controlador de usuarios a ser utilizado por el usuario
     */
    private final UsuariosControlador usuariosControlador;
    /**
     * Servicio de usuarios
     */
    private final UsuariosServicio usuariosServicio;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private UsuariosPanel panelUsuarios;

    /**
     * Constructor del controlador del panel de usuarios
     *
     * @param usuariosControlador Controlador de usuarios
     * @param usuariosServicio    Servicio de usuarios
     */
    public PanelUsuariosControlador(UsuariosControlador usuariosControlador, UsuariosServicio usuariosServicio) {
        this.usuariosControlador = usuariosControlador;
        this.usuariosServicio = usuariosServicio;
    }

    /**
     * Asigna el usuario logueado que utilizara este panel
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    @Override
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuariosControlador.setUsuarioLogueado(usuarioLogueado);
        configurarPanel(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de usuarios que este controlador debe manejar
     *
     * @param vista Vista del panel de usuarios a asignar
     */
    @Override
    public void setPanel(UsuariosPanel vista) {
        this.panelUsuarios = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de usuarios en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    @Override
    public void configurarPanel(Usuario usuarioLogueado) {
        // Carga los usuarios
        panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[0]))
            panelUsuarios.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[1]))
            panelUsuarios.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[6]))
            panelUsuarios.eliminarBoton.setVisible(false);
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
            // Si el usuario desea crear otro usuario
            case (StringsFinales.CREAR) -> usuariosControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar otro usuario
            case (StringsFinales.ELIMINAR) -> {
                int filaSeleccionada = panelUsuarios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            panelUsuarios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    boolean eliminar = FormularioOpcionesGenerador.mostrarOpcionSiNoEliminar(identificador);
                    if (eliminar) {
                        try {
                            Usuario usuarioAEliminar = usuariosServicio.leerPorID(identificador);
                            usuariosControlador.eliminarUsuario(usuarioAEliminar);
                            // Actualiza los datos
                            panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
                        } catch (Exception e) {
                            ErrorVistaGenerador.mostrarErrorDB(e);
                        }
                    }
                }
            }
            // Si el usuario desea modificar a otro usuario
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelUsuarios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            panelUsuarios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    try {
                        Usuario usuarioAModificar = usuariosServicio.leerPorID(identificador);
                        usuariosControlador.mostrarFormularioModificar(usuarioAModificar);
                    } catch (Exception e) {
                        ErrorVistaGenerador.mostrarErrorDB(e);
                    }
                }
            }
            // Si el usuario desea actualizar los datos de los usuarios
            case (StringsFinales.ACTUALIZAR) -> panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
        }
    }
}
