package vista.menuprincipal;

import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.componentes.JTableNoEditable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Panel de usuarios con botones de crear, eliminar y modificar y una lista de ellos
 */
public class UsuariosPanel extends JPanel {
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
     * Crea un panel para mostrar los usuarios como una tabla con botones
     */
    public UsuariosPanel() {
        super(new BorderLayout());
        // Tabla de usuarios
        tablaObjetos = new JTableNoEditable(StringsFinales.COLUMNAS_USUARIOS);
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
     * Metodo que toma los usuarios pasados y los muestra en la pestania de usuarios
     *
     * @param usuarios Usuarios a mostrar como LinkedList
     */
    public void mostrarUsuarios(LinkedList<Usuario> usuarios) {
        String[][] tablaAMostrar = new String[usuarios.size()][StringsFinales.COLUMNAS_USUARIOS.length];
        int i = 0;
        for (Usuario usuario : usuarios) {
            tablaAMostrar[i][0] = usuario.getId();
            tablaAMostrar[i][1] = usuario.getNombre();
            tablaAMostrar[i][2] = usuario.getClave();
            tablaAMostrar[i][3] = usuario.rolUsuario.getNombreRol();
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
