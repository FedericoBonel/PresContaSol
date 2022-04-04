package com.fedebonel.vista.formularios.modificacion;

import com.fedebonel.vista.StringsFinales;
import com.fedebonel.controlador.controladorobjetos.UsuariosControlador;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.vista.Estilo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formulario para modificar usuarios
 */
public class FormularioModificarUsuario {
    /**
     * Ventana del formulario
     */
    public final JFrame ventana;
    /**
     * Campo de texto de clave
     */
    public JPasswordField claveCampo;
    /**
     * Boton de modificar
     */
    public final JButton modificarBoton;
    /**
     * Usuario a ser modificado en este formulario
     */
    public final Usuario usuarioAModificar;

    /**
     * Controlador que debe gestionar las interacciones de esta vista
     */
    private final UsuariosControlador controlador;

    /**
     * Constructor de un nuevo formulario de modificacion de usuarios
     *
     * @param controlador controlador que debe gestionar las interacciones de este formulario
     * @param usuarioAModificar Usuario a modificar
     */
    public FormularioModificarUsuario(UsuariosControlador controlador, Usuario usuarioAModificar){
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.MODIFICAR + " " + StringsFinales.USUARIO);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_MODIFICAR_USUARIO[0], Estilo.DIMENSIONES_FORM_MODIFICAR_USUARIO[1]);
        ventana.setResizable(false);

        // Etiqueta identificador/nombre de usuario
        JPanel contenedorId = crearComponenteIdentificador(usuarioAModificar);
        // Componentes clave de usuario
        JPanel contenedorClave = crearComponenteClave(usuarioAModificar);

        // Setea los parametros del boton modificar
        modificarBoton = new JButton(StringsFinales.MODIFICAR);
        modificarBoton.setActionCommand(StringsFinales.MODIFICAR);
        modificarBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorClave);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(modificarBoton);
        ventana.add(BorderLayout.CENTER, contenedorTodos);
        ventana.add(BorderLayout.SOUTH, modificarBoton);
        ventana.setLocationRelativeTo(null);
        // Asigna el controlador
        this.controlador = controlador;
        // Asigna el usuario
        this.usuarioAModificar = usuarioAModificar;
    }

    /**
     * Crea el componente del identificador
     *
     * @param usuarioAModificar Usuario que se desea modificar
     * @return Un panel con todos los componentes del identificador
     */
    private JPanel crearComponenteIdentificador(Usuario usuarioAModificar) {
        JPanel contenedorId = new JPanel();
        JLabel idEtiqueta = new JLabel(usuarioAModificar.getId());
        contenedorId.add(idEtiqueta);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente de la clave de usuario
     *
     * @param usuarioAModificar Usuario que se desea modificar
     * @return Un panel con todos los componentes de la clave de usuario
     */
    private JPanel crearComponenteClave(Usuario usuarioAModificar) {
        JPanel contenedorClave = new JPanel(new BorderLayout());
        JLabel claveEtiqueta = new JLabel(StringsFinales.COLUMNAS_USUARIOS[2]);
        claveCampo = new JPasswordField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        claveCampo.setText(usuarioAModificar.getClave());
        contenedorClave.add(BorderLayout.WEST, claveEtiqueta);
        contenedorClave.add(BorderLayout.EAST, claveCampo);
        contenedorClave.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorClave;
    }
}
