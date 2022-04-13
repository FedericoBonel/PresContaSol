package com.fedebonel.controlador.controladorpaneles;

import com.fedebonel.controlador.controladorobjetos.ConvocatoriasControlador;
import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.ConvocatoriasServicio;
import com.fedebonel.servicios.PresentacionesServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.ventanasemergentes.FormularioOpcionesGenerador;
import com.fedebonel.vista.menuprincipal.ConvocatoriasPanel;

import java.awt.event.ActionEvent;

/**
 * Controlador del panel convocatorias, funciona como un puente entre el usuario, la vista del panel de convocatorias
 * y el controlador de convocatorias
 */
public class PanelConvocatoriasControlador implements PanelControlador<ConvocatoriasPanel> {
    /**
     * Controlador de convocatorias a ser utilizado por el usuario
     */
    private final ConvocatoriasControlador convocatoriasControlador;
    /**
     * Servicio de convocatorias
     */
    private final ConvocatoriasServicio convocatoriasServicio;
    /**
     * Servicio de presentaciones
     */
    private final PresentacionesServicio presentacionesServicio;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private ConvocatoriasPanel panelConvocatorias;

    /**
     * Constructor del controlador del panel de convocatorias
     *
     * @param convocatoriasControlador controlador de convocatorias
     * @param convocatoriasServicio    Servicio de convocatorias
     * @param presentacionesServicio   servicio de presentaciones
     */
    public PanelConvocatoriasControlador(ConvocatoriasControlador convocatoriasControlador,
                                         ConvocatoriasServicio convocatoriasServicio,
                                         PresentacionesServicio presentacionesServicio) {
        this.convocatoriasControlador = convocatoriasControlador;
        this.convocatoriasServicio = convocatoriasServicio;
        this.presentacionesServicio = presentacionesServicio;
    }

    /**
     * Asigna el usuario que utilizara este panel
     *
     * @param usuarioLogueado Usuario a utilizar este panel
     */
    @Override
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.convocatoriasControlador.setUsuarioLogueado(usuarioLogueado);
        configurarPanel(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de convocatorias que este controlador debe manejar
     *
     * @param vista Vista del panel de convocatorias a asignar
     */
    @Override
    public void setPanel(ConvocatoriasPanel vista) {
        this.panelConvocatorias = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de presentaciones en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    @Override
    public void configurarPanel(Usuario usuarioLogueado) {
        try {
            // Carga las convocatorias
            panelConvocatorias.mostrarConvocatorias(convocatoriasControlador.getConvocatoriasVisibles(),
                    presentacionesServicio.leerTodo());
            // Si no tiene permiso para crear quita el boton
            if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[0]))
                panelConvocatorias.crearBoton.setVisible(false);
            // Si no tiene el permiso para modificar nada quita el boton
            if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1]))
                panelConvocatorias.modificarBoton.setVisible(false);
            // Si no tiene permiso para eliminar ninguna quita el boton eliminar
            if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[7]))
                panelConvocatorias.eliminarBoton.setVisible(false);
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
            // Si el usuario desea crear una nueva convocatoria
            case (StringsFinales.CREAR) -> convocatoriasControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar una convocatoria
            case (StringsFinales.ELIMINAR) -> {
                int filaSeleccionada = panelConvocatorias.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(panelConvocatorias.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    boolean eliminar = FormularioOpcionesGenerador.mostrarOpcionSiNoEliminar(identificador);
                    if (eliminar) {
                        try {
                            Convocatoria convocatoriaAEliminar = convocatoriasServicio.leerPorID(identificador);
                            convocatoriasControlador.eliminarConvocatoria(convocatoriaAEliminar);
                            // Actualiza los datos
                            panelConvocatorias.mostrarConvocatorias(convocatoriasControlador.getConvocatoriasVisibles(),
                                    presentacionesServicio.leerTodo());
                        } catch (Exception e) {
                            ErrorVistaGenerador.mostrarErrorDB(e);
                        }
                    }
                }
            }
            // Si el usuario desea modificar a una convocatoria
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelConvocatorias.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    try {
                        String identificador = String.valueOf(panelConvocatorias.tablaObjetos.getValueAt(filaSeleccionada, 0));
                        Convocatoria convocatoriaAModificar = convocatoriasServicio.leerPorID(identificador);
                        convocatoriasControlador.mostrarFormularioModificar(convocatoriaAModificar);
                    } catch (Exception e) {
                        ErrorVistaGenerador.mostrarErrorDB(e);
                    }
                }
            }
            // Si el usuario desea actualizar los datos de las convocatorias
            case (StringsFinales.ACTUALIZAR) -> {
                try {
                    panelConvocatorias.mostrarConvocatorias(
                            convocatoriasControlador.getConvocatoriasVisibles(), presentacionesServicio.leerTodo());
                } catch (Exception e) {
                    ErrorVistaGenerador.mostrarErrorDB(e);
                }
            }
        }
    }
}
