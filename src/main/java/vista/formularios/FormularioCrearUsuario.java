package vista.formularios;

import controlador.controladorObjetos.UsuariosControlador;
import modelo.usuario.RolUsuario;
import vista.Estilo;
import vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formulario para crear usuarios
 */
public class FormularioCrearUsuario {
    /**
     * Ventana del formulario
     */
    public JFrame ventana;
    /**
     * Campo de texto de identificador
     */
    public JTextField idCampo;
    /**
     * Campo de texto de nombre
     */
    public JTextField nombreCampo;
    /**
     * Campo de texto de clave (Se usa la longitud del identificador por cuestiones visuales)
     */
    public JPasswordField claveCampo;
    /**
     * Opciones de rol
     */
    public JComboBox<String> opcionesRol;
    /**
     * Boton de crear
     */
    public JButton crearBoton;

    /**
     * Controlador que debe gestionar las interacciones de esta vista
     */
    private final UsuariosControlador controlador;

    /**
     * Constructor de un nuevo formulario de creacion de usuarios
     *
     * @param controlador controlador que debe gestionar las interacciones de este formulario
     */
    public FormularioCrearUsuario(UsuariosControlador controlador){
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.CREAR + " " + StringsFinales.USUARIO);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_CREAR_USUARIO[0], Estilo.DIMENSIONES_FORM_CREAR_USUARIO[1]);
        ventana.setResizable(false);

        // Componentes identificador/nombre de usuario
        JPanel contenedorId = crearComponenteIdentificador();
        // Componentes nombre
        JPanel contenedorNombre = crearComponenteNombre();
        // Componentes clave de usuario
        JPanel contenedorClave = crearComponenteClave();
        // Componentes roles de usuario
        JPanel contenedorRoles = crearComponenteRol();

        // Setea los parametros del boton crear
        crearBoton = new JButton(StringsFinales.CREAR);
        crearBoton.setActionCommand(StringsFinales.CREAR);
        crearBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorNombre);
        contenedorTodos.add(contenedorClave);
        contenedorTodos.add(contenedorRoles);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(crearBoton);
        ventana.add(BorderLayout.CENTER, contenedorTodos);
        ventana.add(BorderLayout.SOUTH, crearBoton);
        ventana.setLocationRelativeTo(null);
        // Asigna el controlador
        this.controlador = controlador;
    }

    /**
     * Crea el componente del identificador
     *
     * @return Un panel con todos los componentes del identificador
     */
    private JPanel crearComponenteIdentificador() {
        JPanel contenedorId = new JPanel(new BorderLayout());
        JLabel idEtiqueta = new JLabel(StringsFinales.COLUMNAS_USUARIOS[0]);
        idCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorId.add(BorderLayout.WEST, idEtiqueta);
        contenedorId.add(BorderLayout.EAST, idCampo);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente del nombre de usuario
     *
     * @return Un panel con todos los componentes del nombre de usuario
     */
    private JPanel crearComponenteNombre() {
        JPanel contenedorNombre = new JPanel(new BorderLayout());
        JLabel nombreEtiqueta = new JLabel(StringsFinales.COLUMNAS_USUARIOS[1]);
        nombreCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorNombre.add(BorderLayout.WEST, nombreEtiqueta);
        contenedorNombre.add(BorderLayout.EAST, nombreCampo);
        contenedorNombre.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorNombre;
    }

    /**
     * Crea el componente de la clave de usuario
     *
     * @return Un panel con todos los componentes de la clave de usuario
     */
    private JPanel crearComponenteClave() {
        JPanel contenedorClave = new JPanel(new BorderLayout());
        JLabel claveEtiqueta = new JLabel(StringsFinales.COLUMNAS_USUARIOS[2]);
        claveCampo = new JPasswordField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorClave.add(BorderLayout.WEST, claveEtiqueta);
        contenedorClave.add(BorderLayout.EAST, claveCampo);
        contenedorClave.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorClave;
    }

    /**
     * Crea el componente de los roles de usuario
     *
     * @return Un panel con todos los componentes de los roles de usuario
     */
    private JPanel crearComponenteRol() {
        JPanel contenedorRoles = new JPanel(new BorderLayout());
        JLabel rolesEtiqueta = new JLabel(StringsFinales.COLUMNAS_USUARIOS[3]);
        opcionesRol = new JComboBox<>(new String[]{
                        RolUsuario.ROL_ADMINISTRADOR_NOMBRE,
                        RolUsuario.ROL_FISCAL_GRAL_NOMBRE,
                        RolUsuario.ROL_FISCAL_NOMBRE,
                        RolUsuario.ROL_CUENTADANTE_NOMBRE});
        contenedorRoles.add(BorderLayout.WEST, rolesEtiqueta);
        contenedorRoles.add(BorderLayout.EAST, opcionesRol);
        contenedorRoles.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorRoles;
    }
}
