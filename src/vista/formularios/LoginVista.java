package vista.formularios;

import controlador.MenuPrincipalControlador;
import vista.Estilo;
import vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Vista de autenticacion
 */
public class LoginVista {
    /**
     * Dimensiones de la ventana en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES = new int[]{400, 170};
    /**
     * Ventana de autenticacion
     */
    public JFrame ventana;
    /**
     * Campo de texto del usuario
     */
    public JTextField usuarioCampo;
    /**
     * Campo de texto de la clave (Se usa la longitud del identificador por cuestiones visuales)
     */
    public JPasswordField claveCampo;
    /**
     * Boton de ingreso
     */
    public JButton botonIngreso;
    /**
     * Controlador que manejara los eventos de esta vista
     */
    private MenuPrincipalControlador controlador;

    /**
     * Constructor de la vista de autenticacion, crea una ventana con todos los componentes necesarios para autenticar usuarios
     */
    public LoginVista() {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.AUTENTICACION_USUARIO);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(DIMENSIONES[0], DIMENSIONES[1]);
        ventana.setLocationRelativeTo(null);

        // Crea el contenedor del nombre de usuario
        JPanel contenedorUsuario = crearComponenteNombreUsuario();
        // Agrega los componentes de clave de usuario
        JPanel contenedorClave = crearComponenteClaveUsuario();
        // Agrega los componentes de clave y nombre de usuario a un solo componente
        JPanel contenedorNombreClave = new JPanel();
        BoxLayout plantillaNombreClave = new BoxLayout(contenedorNombreClave, BoxLayout.Y_AXIS);
        contenedorNombreClave.setLayout(plantillaNombreClave);
        contenedorNombreClave.add(contenedorUsuario);
        contenedorNombreClave.add(contenedorClave);

        // Setea los parametros del boton de ingreso
        botonIngreso = new JButton(StringsFinales.INGRESAR);
        botonIngreso.setActionCommand(StringsFinales.INGRESAR);

        // Agrega todos los componentes a un contenedor
        JPanel contenedorTodos = new JPanel(new BorderLayout());
        contenedorTodos.add(BorderLayout.CENTER, contenedorNombreClave);
        contenedorTodos.add(BorderLayout.SOUTH, botonIngreso);
        contenedorTodos.setBorder(new EmptyBorder(Estilo.ESPACIADO_TODO_LOGIN));

        // Agrega todos los contenendores a la ventana
        ventana.add(contenedorTodos);
        ventana.getRootPane().setDefaultButton(botonIngreso);
        ventana.setVisible(true);
    }

    /**
     * Crea el componente del nombre de usuario
     *
     * @return Componente del nombre de usuario
     */
    private JPanel crearComponenteNombreUsuario(){
        JPanel contenedorUsuario = new JPanel();
        contenedorUsuario.setLayout(new BorderLayout());
        JLabel etiquetaUsuario = new JLabel(StringsFinales.USUARIO);
        usuarioCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorUsuario.add(BorderLayout.WEST, etiquetaUsuario);
        contenedorUsuario.add(BorderLayout.EAST, usuarioCampo);
        contenedorUsuario.setBorder(new EmptyBorder(Estilo.ESPACIADO_NOMBRE_CLAVE_LOGIN));
        return contenedorUsuario;
    }

    /**
     * Crea el componente de clave de usuario
     *
     * @return Componente de clave de usuario
     */
    private JPanel crearComponenteClaveUsuario(){
        JPanel contenedorClave = new JPanel();
        contenedorClave.setLayout(new BorderLayout());
        JLabel etiquetaClave = new JLabel(StringsFinales.CLAVE);
        claveCampo = new JPasswordField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorClave.add(BorderLayout.WEST, etiquetaClave);
        contenedorClave.add(BorderLayout.EAST, claveCampo);
        contenedorClave.setBorder(new EmptyBorder(Estilo.ESPACIADO_NOMBRE_CLAVE_LOGIN));
        return contenedorClave;
    }

    /**
     * Metodo para agregar controlador que maneje los eventos de interaccion
     *
     * @param controlador Controlador que gestionara las interacciones sobre esta vista
     */
    public void addControlador(MenuPrincipalControlador controlador) {
        this.controlador = controlador;
        botonIngreso.addActionListener(controlador);
    }
}
