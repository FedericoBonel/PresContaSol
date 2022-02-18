package controlador.controladorPaneles;

import controlador.controladorObjetos.MunicipiosControlador;
import modelo.municipio.Municipio;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.menuPrincipal.MunicipiosPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del panel municipios, funciona como un puente entre el usuario, la vista del panel de municipios
 * y el controlador de municipios
 */
public class PanelMunicipiosControlador implements ActionListener {
    /**
     * Controlador de municipios a ser utilizado por el usuario
     */
    private MunicipiosControlador municipiosControlador;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private MunicipiosPanel panelMunicipios;
    /**
     * Usuario autenticado que utilizara esta vista
     */
    private Usuario usuarioLogueado;

    /**
     * Constructor del controlador del panel de municipios
     *
     * @param panelMunicipios Vista del panel de municipios que gestionara este controlador
     * @param usuarioLogueado Usuario que utilizara el controlador
     */
    public PanelMunicipiosControlador(MunicipiosPanel panelMunicipios, Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        municipiosControlador = new MunicipiosControlador(usuarioLogueado);
        setPanelMunicipios(panelMunicipios);
        configurarPanelMunicipios(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de municipios que este controlador debe manejar
     *
     * @param vista Vista del panel de municipios a asignar
     */
    private void setPanelMunicipios(MunicipiosPanel vista) {
        this.panelMunicipios = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de municipios en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelMunicipios(Usuario usuarioLogueado) {
        // Carga los municipios
        panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
        // Si no tiene permiso para crear quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[0]))
            panelMunicipios.crearBoton.setVisible(false);
        // Si no tiene el permiso para modificar nada quita el boton
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2]) &&
                !usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3]))
            panelMunicipios.modificarBoton.setVisible(false);
        // Si no tiene permiso para eliminar quita el boton eliminar
        if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[6]))
            panelMunicipios.eliminarBoton.setVisible(false);
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
            // Si el usuario desea crear un nuevo municipio
            case (StringsFinales.CREAR) -> municipiosControlador.mostrarFormularioCrear();
            // Si el usuario desea eliminar un municipio
            case (StringsFinales.ELIMINAR) -> {
                int filaSeleccionada = panelMunicipios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(
                            panelMunicipios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    int decision = JOptionPane.showConfirmDialog(new JFrame(),
                            StringsFinales.ESTA_SEGURO,
                            StringsFinales.ELIMINAR + " " + identificador,
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        Municipio municipioAEliminar = MunicipiosControlador.leerMunicipiosBaseDeDatos().getMunicipio(identificador);
                        municipiosControlador.eliminarMunicipio(municipioAEliminar);
                        // Actualiza los datos
                        panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
                    }
                }
            }
            // Si el usuario desea modificar a un municipio
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelMunicipios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String identificador = String.valueOf(panelMunicipios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                    Municipio municipioAModificar = MunicipiosControlador.leerMunicipiosBaseDeDatos().getMunicipio(identificador);
                    municipiosControlador.mostrarFormularioModificar(municipioAModificar);
                }
            }
            // Si el usuario desea actualizar los datos de los municipios
            case (StringsFinales.ACTUALIZAR) -> panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
        }
    }
}
