package com.fedebonel.controlador.controladorpaneles;

import com.fedebonel.controlador.controladorobjetos.PresentacionesControlador;
import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.PresentacionesServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.ventanasemergentes.FormularioOpcionesGenerador;
import com.fedebonel.vista.menuprincipal.PresentacionesPanel;

import java.awt.event.ActionEvent;

/**
 * Controlador del panel presentaciones, funciona como un puente entre el usuario, la vista del panel de presentaciones
 * y el controlador de presentaciones
 */
public class PanelPresentacionesControlador implements PanelControlador<PresentacionesPanel> {
    /**
     * Controlador de presentaciones a ser utilizado por el usuario
     */
    private final PresentacionesControlador presentacionesControlador;
    /**
     * Servicio de presentaciones
     */
    private final PresentacionesServicio presentacionesServicio;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private PresentacionesPanel panelPresentaciones;

    /**
     * Constructor del controlador del panel de presentaciones
     *
     * @param presentacionesServicio    Servicio de presentaciones
     * @param presentacionesControlador controlador de presentaciones
     */
    public PanelPresentacionesControlador(PresentacionesServicio presentacionesServicio,
                                          PresentacionesControlador presentacionesControlador) {
        this.presentacionesServicio = presentacionesServicio;
        this.presentacionesControlador = presentacionesControlador;

    }

    /**
     * Asigna el usuario que utilizara este controlador
     *
     * @param usuarioLogueado Usuario a utilizar el controlador
     */
    @Override
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.presentacionesControlador.setUsuarioLogueado(usuarioLogueado);
        configurarPanel(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de presentaciones que este controlador debe manejar
     *
     * @param vista Vista del panel de presentaciones a asignar
     */
    @Override
    public void setPanel(PresentacionesPanel vista) {
        this.panelPresentaciones = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de presentaciones en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    @Override
    public void configurarPanel(Usuario usuarioLogueado) {
        // Carga las presentaciones
        panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[0]))
            panelPresentaciones.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4])
                && !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]))
            panelPresentaciones.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar ninguna quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[8]))
            panelPresentaciones.eliminarBoton.setVisible(false);
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
            // Si el usuario desea crear una nueva presentacion
            case (StringsFinales.CREAR) -> presentacionesControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar una presentacion
            case (StringsFinales.ELIMINAR) -> {
                int filaSeleccionada = panelPresentaciones.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(panelPresentaciones.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    boolean eliminar = FormularioOpcionesGenerador.mostrarOpcionSiNoEliminar(identificador);
                    if (eliminar) {
                        try {
                            Presentacion presentacionAEliminar = presentacionesServicio.leerPorID(identificador);
                            presentacionesControlador.eliminarPresentacion(presentacionAEliminar);
                            // Actualiza los datos
                            panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
                        } catch (Exception e) {
                            ErrorVistaGenerador.mostrarErrorDB(e);
                        }
                    }
                }
            }
            // Si el usuario desea modificar a una presentacion
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelPresentaciones.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    try {
                        String identificador = String.valueOf(panelPresentaciones.tablaObjetos.getValueAt(filaSeleccionada, 0));
                        Presentacion presentacionAModificar = presentacionesServicio.leerPorID(identificador);
                        presentacionesControlador.mostrarFormularioModificar(presentacionAModificar);
                    } catch (Exception e) {
                        ErrorVistaGenerador.mostrarErrorDB(e);
                    }
                }
            }
            // Si el usuario desea actualizar los datos de las presentaciones
            case (StringsFinales.ACTUALIZAR) ->
                    panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
        }
    }
}
