package controlador.controladorPaneles;

import controlador.controladorObjetos.MunicipiosControlador;
import controlador.controladorObjetos.PresentacionesControlador;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.menuPrincipal.InformacionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del panel informacion, funciona como un puente entre el usuario, la vista del panel de informacion
 * y el controlador de convocatorias
 */
public class PanelInformacionControlador implements ActionListener {
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private InformacionPanel informacionPanel;
    /**
     * Usuario autenticado que utilizara esta vista
     */
    private Usuario usuarioLogueado;

    /**
     * Constructor del controlador del panel de informacion
     *
     * @param informacionPanel Vista del panel de convocatorias que gestionara este controlador
     * @param usuarioLogueado Usuario que utilizara el controlador
     */
    public PanelInformacionControlador(InformacionPanel informacionPanel, Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        setPanelInformacion(informacionPanel);
        configurarPanelInformacion(usuarioLogueado);
    }

    /**
     * Asigna la vista del panel de informacion que este controlador debe manejar
     *
     * @param vista Vista del panel de convocatorias a asignar
     */
    private void setPanelInformacion(InformacionPanel vista) {
        this.informacionPanel = vista;
        vista.addControlador(this);
    }

    /**
     * Configura el panel de informacion en la vista del menu principal asignada para el usuario logueado
     *
     * @param usuarioLogueado Usuario que utilizara el panel
     */
    private void configurarPanelInformacion(Usuario usuarioLogueado) {
        // Todos pueden ver esto
        informacionPanel
                .mostrarInformacion(MunicipiosControlador.leerMunicipiosBaseDeDatos(),
                        PresentacionesControlador.leerPresentacionesBaseDeDatos());
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
            case (StringsFinales.ACTUALIZAR) ->
                    informacionPanel
                            .mostrarInformacion(MunicipiosControlador.leerMunicipiosBaseDeDatos(),
                                    PresentacionesControlador.leerPresentacionesBaseDeDatos());
        }
    }
}
