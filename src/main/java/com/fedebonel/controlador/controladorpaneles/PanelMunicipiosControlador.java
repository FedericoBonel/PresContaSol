package com.fedebonel.controlador.controladorpaneles;

import com.fedebonel.controlador.controladorobjetos.MunicipiosControlador;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.MunicipiosServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.ventanasemergentes.FormularioOpcionesGenerador;
import com.fedebonel.vista.menuprincipal.MunicipiosPanel;

import java.awt.event.ActionEvent;

/**
 * Controlador del panel municipios, funciona como un puente entre el usuario, la vista del panel de municipios
 * y el controlador de municipios
 */
public class PanelMunicipiosControlador implements PanelControlador<MunicipiosPanel> {
    /**
     * Controlador de municipios a ser utilizado por el usuario
     */
    private final MunicipiosControlador municipiosControlador;
    /**
     * Servicio de municipios
     */
    private final MunicipiosServicio municipiosServicio;
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
     * @param municipiosServicio    Servicio de municipios
     * @param municipiosControlador Controlador de municipios
     */
    public PanelMunicipiosControlador(MunicipiosServicio municipiosServicio,
                                      MunicipiosControlador municipiosControlador) {
        this.municipiosServicio = municipiosServicio;
        this.municipiosControlador = municipiosControlador;
    }

    /**
     * Asigna el usuario que utilizara este controlador
     *
     * @param usuarioLogueado Usuario a utilizar este controlador
     */
    @Override
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        this.municipiosControlador.setUsuarioLogueado(usuarioLogueado);
        configurarPanel(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de municipios que este controlador debe manejar
     *
     * @param vista Vista del panel de municipios a asignar
     */
    @Override
    public void setPanel(MunicipiosPanel vista) {
        this.panelMunicipios = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de municipios en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    @Override
    public void configurarPanel(Usuario usuarioLogueado) {
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
                    boolean eliminar = FormularioOpcionesGenerador.mostrarOpcionSiNoEliminar(identificador);
                    if (eliminar) {
                        try {
                            Municipio municipioAEliminar = municipiosServicio.leerPorID(identificador);
                            municipiosControlador.eliminarMunicipio(municipioAEliminar);
                            // Actualiza los datos
                            panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
                        } catch (Exception e) {
                            ErrorVistaGenerador.mostrarErrorDB(e);
                        }
                    }
                }
            }
            // Si el usuario desea modificar a un municipio
            case (StringsFinales.MODIFICAR) -> {
                int filaSeleccionada = panelMunicipios.tablaObjetos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    try {
                        String identificador = String.valueOf(panelMunicipios.tablaObjetos.getValueAt(filaSeleccionada, 0));
                        Municipio municipioAModificar = municipiosServicio.leerPorID(identificador);
                        municipiosControlador.mostrarFormularioModificar(municipioAModificar);
                    } catch (Exception e) {
                        ErrorVistaGenerador.mostrarErrorDB(e);
                    }
                }
            }
            // Si el usuario desea actualizar los datos de los municipios
            case (StringsFinales.ACTUALIZAR) -> panelMunicipios.mostrarMunicipios(municipiosControlador.getMunicipiosVisibles());
        }
    }
}
