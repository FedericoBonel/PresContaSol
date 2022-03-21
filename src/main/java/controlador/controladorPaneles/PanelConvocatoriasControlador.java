package controlador.controladorPaneles;

import controlador.controladorObjetos.ConvocatoriasControlador;
import controlador.controladorObjetos.PresentacionesControlador;
import modelo.evento.convocatoria.Convocatoria;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.menuPrincipal.ConvocatoriasPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del panel convocatorias, funciona como un puente entre el usuario, la vista del panel de convocatorias
 * y el controlador de convocatorias
 */
public class PanelConvocatoriasControlador implements ActionListener {
    /**
     * Controlador de convocatorias a ser utilizado por el usuario
     */
    private ConvocatoriasControlador convocatoriasControlador;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private ConvocatoriasPanel panelConvocatorias;
    /**
     * Usuario autenticado que utilizara esta vista
     */
    private Usuario usuarioLogueado;

    /**
     * Constructor del controlador del panel de convocatorias
     *
     * @param panelConvocatorias Vista del panel de convocatorias que gestionara este controlador
     * @param usuarioLogueado Usuario que utilizara el controlador
     */
    public PanelConvocatoriasControlador(ConvocatoriasPanel panelConvocatorias, Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        convocatoriasControlador = new ConvocatoriasControlador(usuarioLogueado);
        setPanelConvocatorias(panelConvocatorias);
        configurarPanelConvocatorias(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de convocatorias que este controlador debe manejar
     *
     * @param vista Vista del panel de convocatorias a asignar
     */
    private void setPanelConvocatorias(ConvocatoriasPanel vista) {
        this.panelConvocatorias = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de presentaciones en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelConvocatorias(Usuario usuarioLogueado) {
        // Carga las convocatorias
        panelConvocatorias.mostrarConvocatorias(convocatoriasControlador.getConvocatoriasVisibles(),
                PresentacionesControlador.leerPresentacionesBaseDeDatos());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[0]))
            panelConvocatorias.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1]))
            panelConvocatorias.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar ninguna quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[7]))
            panelConvocatorias.eliminarBoton.setVisible(false);
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
                    int decision =
                            JOptionPane.showConfirmDialog(new JFrame(),
                                    StringsFinales.ESTA_SEGURO,
                                    StringsFinales.ELIMINAR + " " + identificador,
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Convocatoria convocatoriaAEliminar = ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoria(identificador);
                        convocatoriasControlador.eliminarConvocatoria(convocatoriaAEliminar);
                        // Actualiza los datos
                        panelConvocatorias.mostrarConvocatorias(convocatoriasControlador.getConvocatoriasVisibles(),
                                PresentacionesControlador.leerPresentacionesBaseDeDatos());
                    }
                }
            }
            // Si el usuario desea modificar a una convocatoria
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelConvocatorias.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(panelConvocatorias.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Convocatoria convocatoriaAModificar = ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoria(identificador);
                    convocatoriasControlador.mostrarFormularioModificar(convocatoriaAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de las convocatorias
            case (StringsFinales.ACTUALIZAR) -> panelConvocatorias.mostrarConvocatorias(
                    convocatoriasControlador.getConvocatoriasVisibles(), PresentacionesControlador.leerPresentacionesBaseDeDatos());
        }
    }
}
