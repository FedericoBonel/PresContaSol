package controlador.controladorPaneles;

import controlador.controladorObjetos.UsuariosControlador;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.menuPrincipal.UsuariosPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del panel usuarios, funciona como un puente entre el usuario, la vista del panel de usuarios
 * y el controlador de usuarios
 */
public class PanelUsuariosControlador implements ActionListener {
    /**
     * Controlador de usuarios a ser utilizado por el usuario
     */
    private UsuariosControlador usuariosControlador;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private UsuariosPanel panelUsuarios;
    /**
     * Usuario autenticado que utilizara esta vista
     */
    private Usuario usuarioLogueado;

    /**
     * Constructor del controlador del panel de usuarios
     *
     * @param panelUsuarios Vista del panel de usuarios que gestionara este controlador
     * @param usuarioLogueado Usuario que utilizara el controlador
     */
    public PanelUsuariosControlador(UsuariosPanel panelUsuarios, Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        usuariosControlador = new UsuariosControlador(usuarioLogueado);
        setPanelUsuarios(panelUsuarios);
        configurarPanelUsuarios(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de usuarios que este controlador debe manejar
     *
     * @param vista Vista del panel de usuarios a asignar
     */
    private void setPanelUsuarios(UsuariosPanel vista) {
        this.panelUsuarios = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de usuarios en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelUsuarios(Usuario usuarioLogueado) {
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
                    int decision = JOptionPane.showConfirmDialog(new JFrame(),
                            StringsFinales.ESTA_SEGURO,
                            StringsFinales.ELIMINAR + " " + identificador,
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Usuario usuarioAEliminar = UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(identificador);
                        usuariosControlador.eliminarUsuario(usuarioAEliminar);
                        // Actualiza los datos
                        panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
                    }
                }
            }
            // Si el usuario desea modificar a otro usuario
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelUsuarios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            panelUsuarios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Usuario usuarioAModificar = UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(identificador);
                    usuariosControlador.mostrarFormularioModificar(usuarioAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de los usuarios
            case (StringsFinales.ACTUALIZAR) -> panelUsuarios.mostrarUsuarios(usuariosControlador.getUsuariosVisibles());
        }
    }
}
