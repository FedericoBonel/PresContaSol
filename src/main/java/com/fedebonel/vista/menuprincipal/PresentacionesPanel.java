package com.fedebonel.vista.menuprincipal;

import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.componentes.JTableNoEditable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Panel de presentaciones con botones de crear, eliminar y modificar y una lista de ellas
 */
public class PresentacionesPanel extends JPanel {
    /**
     * Boton crear
     */
    public final JButton crearBoton;
    /**
     * Boton Modificar
     */
    public final JButton modificarBoton;
    /**
     * Boton Eliminar
     */
    public final JButton eliminarBoton;
    /**
     * Boton Actualizar
     */
    public final JButton actualizarBoton;
    /**
     * Tabla contenedora de todos los datos
     */
    public final JTableNoEditable tablaObjetos;
    /**
     * Controlador que maneja las interacciones realizadas por el usuario
     */
    private ActionListener controlador;

    /**
     * Crea un panel para mostrar las presentaciones como una tabla con botones
     */
    public PresentacionesPanel() {
        super(new BorderLayout());
        // Tabla de presentaciones
        tablaObjetos = new JTableNoEditable(StringsFinales.COLUMNAS_PRESENTACIONES);
        // Contenedor de los botones de operacion
        JPanel operaciones = new JPanel(new FlowLayout());
        JPanel botones = new JPanel(new BorderLayout());
        // Boton de crear
        crearBoton = new JButton(StringsFinales.CREAR);
        crearBoton.setActionCommand(StringsFinales.CREAR);
        operaciones.add(crearBoton);
        // Boton de modificar
        modificarBoton = new JButton(StringsFinales.MODIFICAR);
        modificarBoton.setActionCommand(StringsFinales.MODIFICAR);
        operaciones.add(modificarBoton);
        // Boton de eliminar
        eliminarBoton = new JButton(StringsFinales.ELIMINAR);
        eliminarBoton.setActionCommand(StringsFinales.ELIMINAR);
        operaciones.add(eliminarBoton);
        // Boton de actualizar
        actualizarBoton = new JButton(StringsFinales.ACTUALIZAR);
        actualizarBoton.setActionCommand(StringsFinales.ACTUALIZAR);

        botones.add(BorderLayout.WEST, actualizarBoton);
        botones.add(BorderLayout.EAST, operaciones);
        super.add(BorderLayout.NORTH, botones);
        super.add(BorderLayout.CENTER, new JScrollPane(tablaObjetos));
    }

    /**
     * Metodo que toma las presentaciones pasadas y los muestra en la pestania de presentaciones
     *
     * @param presentaciones Presentaciones a mostrar como linked list
     */
    public void mostrarPresentaciones(LinkedList<Presentacion> presentaciones) {
        String[][] tablaAMostrar = new String[presentaciones.size()][StringsFinales.COLUMNAS_PRESENTACIONES.length];
        int i = 0;
        for (Presentacion presentacion : presentaciones) {
            tablaAMostrar[i][0] = presentacion.getId();
            tablaAMostrar[i][1] = presentacion.getFechaInicio().toString();
            tablaAMostrar[i][2] = presentacion.getAutor().getId();
            tablaAMostrar[i][3] = presentacion.getDocumentos().toString();
            tablaAMostrar[i][4] = presentacion.getMunicipio().getId();
            tablaAMostrar[i][5] = String.valueOf(!presentacion.isAbierto());
            tablaAMostrar[i][6] = presentacion.getConvocatoria().getId();
            i++;
        }
        tablaObjetos.actualizarCon(tablaAMostrar);
    }

    /**
     * Agrega el controlador que manejara los eventos de interaccion
     *
     * @param controlador Controlador a gestionar los eventos de interaccion de esta vista
     */
    public void addControlador(ActionListener controlador) {
        this.controlador = controlador;
        crearBoton.addActionListener(controlador);
        modificarBoton.addActionListener(controlador);
        eliminarBoton.addActionListener(controlador);
        actualizarBoton.addActionListener(controlador);
    }
}
