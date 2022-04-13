package com.fedebonel.controlador.controladorpaneles;

import com.fedebonel.modelo.usuario.Usuario;

import java.awt.event.ActionListener;

/**
 * Interface que abstrae la implementacion de controlador de paneles de objetos tipo T
 */
public interface PanelControlador<T> extends ActionListener {

    /**
     * Asigna el usuario que utilizara este controlador
     *
     * @param usuarioLogueado Usuario que utiliza el controlador
     */
    void setUsuarioLogueado(Usuario usuarioLogueado);

    /**
     * Asigna el panel que este controlador escuchara por eventos
     *
     * @param panel Panel a escuchar
     */
    void setPanel(T panel);

    /**
     * Configura el panel asignado para el usuario en cuestion
     *
     * @param usuarioLogueado Usuario qeu utilizara el panel
     */
    void configurarPanel(Usuario usuarioLogueado);
}
