package controlador.controladorPaneles;

import controlador.controladorObjetos.PresentacionesControlador;
import modelo.evento.presentacion.Presentacion;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.menuPrincipal.PresentacionesPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del panel presentaciones, funciona como un puente entre el usuario, la vista del panel de presentaciones
 * y el controlador de presentaciones
 */
public class PanelPresentacionesControlador implements ActionListener {
    /**
     * Controlador de presentaciones a ser utilizado por el usuario
     */
    private PresentacionesControlador presentacionesControlador;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private PresentacionesPanel panelPresentaciones;
    /**
     * Usuario autenticado que utilizara esta vista
     */
    private Usuario usuarioLogueado;

    /**
     * Constructor del controlador del panel de presentaciones
     *
     * @param panelPresentaciones Vista del panel de presentaciones que gestionara este controlador
     * @param usuarioLogueado Usuario que utilizara el controlador
     */
    public PanelPresentacionesControlador(PresentacionesPanel panelPresentaciones, Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        presentacionesControlador = new PresentacionesControlador(usuarioLogueado);
        setPanelPresentaciones(panelPresentaciones);
        configurarPanelMunicipios(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de presentaciones que este controlador debe manejar
     *
     * @param vista Vista del panel de presentaciones a asignar
     */
    private void setPanelPresentaciones(PresentacionesPanel vista) {
        this.panelPresentaciones = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de presentaciones en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelMunicipios(Usuario usuarioLogueado) {
        // Carga las presentaciones
        panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[0]))
            panelPresentaciones.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4]) &&
                !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]))
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
                    int decision =
                            JOptionPane.showConfirmDialog(new JFrame(),
                                    StringsFinales.ESTA_SEGURO,
                                    StringsFinales.ELIMINAR + " " + identificador,
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Presentacion presentacionAEliminar = PresentacionesControlador.leerPresentacionesBaseDeDatos().getPresentacion(identificador);
                        presentacionesControlador.eliminarPresentacion(presentacionAEliminar);
                        // Actualiza los datos
                        panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
                    }
                }
            }
            // Si el usuario desea modificar a una presentacion
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelPresentaciones.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(panelPresentaciones.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Presentacion presentacionAModificar = PresentacionesControlador.leerPresentacionesBaseDeDatos().getPresentacion(identificador);
                    presentacionesControlador.mostrarFormularioModificar(presentacionAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de las presentaciones
            case (StringsFinales.ACTUALIZAR) -> panelPresentaciones.mostrarPresentaciones(presentacionesControlador.getPresentacionesVisibles());
        }
    }
}
