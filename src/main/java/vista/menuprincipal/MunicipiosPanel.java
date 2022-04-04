package vista.menuprincipal;

import modelo.municipio.Municipio;
import vista.StringsFinales;
import vista.componentes.JTableNoEditable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Panel de municipios con botones de crear, eliminar y modificar y una lista de ellos
 */
public class MunicipiosPanel extends JPanel {

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
     * Crea un panel para mostrar los municipios como una tabla con botones
     */
    public MunicipiosPanel() {
        super(new BorderLayout());
        // Tabla de municipios
        tablaObjetos = new JTableNoEditable(StringsFinales.COLUMNAS_MUNICIPIO);
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
     * Metodo que toma los municipios pasados y los muestra en la pestania de municipios
     *
     * @param municipios Municipios a mostrar como LinkedList
     */
    public void mostrarMunicipios(LinkedList<Municipio> municipios) {
        String[][] tablaAMostrar = new String[municipios.size()][StringsFinales.COLUMNAS_MUNICIPIO.length];
        int i = 0;
        for (Municipio municipio : municipios) {
            tablaAMostrar[i][0] = municipio.getId();
            tablaAMostrar[i][1] = municipio.getNombre();
            tablaAMostrar[i][2] = String.valueOf(municipio.getCategoria());
            // Verifica si tiene supervisor
            if (municipio.getFiscal() != null) tablaAMostrar[i][3] = municipio.getFiscal().getId();
            else tablaAMostrar[i][3] = null;
            // Verifica si tiene representante
            if (municipio.getCuentadante() != null) tablaAMostrar[i][4] = municipio.getCuentadante().getId();
            else tablaAMostrar[i][4] = null;
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
