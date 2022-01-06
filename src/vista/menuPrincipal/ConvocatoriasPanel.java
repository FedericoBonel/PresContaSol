package vista.menuPrincipal;

import controlador.MenuPrincipalControlador;
import modelo.ColeccionPresentaciones;
import modelo.Convocatoria;
import vista.StringsFinales;
import vista.componentes.JTableNoEditable;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Panel de convocatorias con botones de crear, eliminar y modificar y una lista de ellas
 */
public class ConvocatoriasPanel extends JPanel {
    /**
     * Boton crear
     */
    public JButton crearBoton;
    /**
     * Boton Modificar
     */
    public JButton modificarBoton;
    /**
     * Boton Eliminar
     */
    public JButton eliminarBoton;
    /**
     * Boton Actualizar
     */
    public JButton actualizarBoton;
    /**
     * Tabla contenedora de todos los datos
     */
    public JTableNoEditable tablaObjetos;
    /**
     * Controlador que maneja las interacciones realizadas por el usuario
     */
    private MenuPrincipalControlador controlador;

    /**
     * Crea un panel para mostrar las convocatorias como una tabla con botones
     */
    public ConvocatoriasPanel() {
        super(new BorderLayout());
        // Tabla de convocatorias
        tablaObjetos = new JTableNoEditable(StringsFinales.COLUMNAS_CONVOCATORIAS);
        // Contenedor de los botones de operacion
        JPanel operaciones = new JPanel(new FlowLayout());
        JPanel botones = new JPanel(new BorderLayout());
        // Boton de crear
        crearBoton = new JButton(StringsFinales.CREAR);
        crearBoton.setActionCommand(StringsFinales.CREAR + StringsFinales.CONVOCATORIA);
        operaciones.add(crearBoton);
        // Boton de modificar
        modificarBoton = new JButton(StringsFinales.MODIFICAR);
        modificarBoton.setActionCommand(StringsFinales.MODIFICAR + StringsFinales.CONVOCATORIA);
        operaciones.add(modificarBoton);
        // Boton de eliminar
        eliminarBoton = new JButton(StringsFinales.ELIMINAR);
        eliminarBoton.setActionCommand(StringsFinales.ELIMINAR + StringsFinales.CONVOCATORIA);
        operaciones.add(eliminarBoton);
        // Boton de actualizar
        actualizarBoton = new JButton(StringsFinales.ACTUALIZAR);
        actualizarBoton.setActionCommand(StringsFinales.ACTUALIZAR + StringsFinales.CONVOCATORIA);
        botones.add(BorderLayout.WEST, actualizarBoton);
        botones.add(BorderLayout.EAST, operaciones);
        super.add(BorderLayout.NORTH, botones);
        super.add(BorderLayout.CENTER, new JScrollPane(tablaObjetos));
    }

    /**
     * Metodo que toma las convocatorias pasadas y los muestra en la pestania de convocatorias
     * @param convocatorias Convocatorias a mostrar como linked list
     * @param presentaciones Coleccion de presentaciones del sistema
     */
    public void mostrarConvocatorias(LinkedList<Convocatoria> convocatorias, ColeccionPresentaciones presentaciones){
        String[][] tablaAMostrar = new String[convocatorias.size()][StringsFinales.COLUMNAS_CONVOCATORIAS.length];
        int i = 0;
        for (Convocatoria convocatoria : convocatorias) {
            tablaAMostrar[i][0] = convocatoria.getId();
            tablaAMostrar[i][1] = convocatoria.getFechaInicio().toString();
            tablaAMostrar[i][2] = convocatoria.getFechaCierre().toString();
            tablaAMostrar[i][3] = convocatoria.getDocumentos().toString();
            tablaAMostrar[i][4] = convocatoria.getDescripcion();
            tablaAMostrar[i][5] = String.valueOf(convocatoria.isAbierto());
            tablaAMostrar[i][6] = String.valueOf(convocatoria.getSusPresentacionesDe(presentaciones).size());
            i++;
        }
        tablaObjetos.actualizarCon(tablaAMostrar);
    }

    /**
     * Agrega el controlador que manejara los eventos de interaccion
     *
     * @param controlador Controlador a gestionar los eventos de interaccion de esta vista
     */
    public void addControlador(MenuPrincipalControlador controlador) {
        this.controlador = controlador;
        crearBoton.addActionListener(controlador);
        modificarBoton.addActionListener(controlador);
        eliminarBoton.addActionListener(controlador);
        actualizarBoton.addActionListener(controlador);
    }
}
