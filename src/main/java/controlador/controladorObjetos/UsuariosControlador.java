package controlador.controladorObjetos;

import modelo.dataAccess.Singleton;
import modelo.usuario.ColeccionUsuarios;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.formularios.FormularioCrearUsuario;
import vista.formularios.FormularioModificarUsuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Clase contenedora de todos los metodos que gestionan usuarios
 */
public class UsuariosControlador implements ActionListener {

    /**
     * Nombres de los campos en la base de datos
     */
    private static final String[] DB_CAMPOS = new String[]{"identificador", "nombre", "clave", "rol"};

    /**
     * Usuario logueado que esta utilizando el sistema
     */
    private final Usuario usuarioLogueado;

    /**
     * Formulario de creacion de usuarios que este controlador debe gestionar
     */
    private FormularioCrearUsuario formularioCrearUsuario;
    /**
     * Formulario de modificacion de usuarios que este controlador debe gestionar
     */
    private FormularioModificarUsuario formularioModificarUsuario;

    /**
     * Costructor del controlador
     *
     * @param usuarioLogueado Usuario autenticado en el sistema que utilizara el controlador
     */
    public UsuariosControlador(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Autentica al usuario en el sistema con su clave
     *
     * @param nombre Nombre de usuario a verificar
     * @param clave  Clave del usuario a verificar
     * @return true si el usuario es autenticado correctamente, false en caso contrario
     */
    public static boolean autenticarUsuario(String nombre, String clave) {
        try {
            return (leerUsuariosBaseDeDatos().getUsuario(nombre).certificaClave(clave));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Lee todos los usuarios de la base de datos y los devuelve
     *
     * @return Todos los usuarios de la base de datos
     */
    public static ColeccionUsuarios leerUsuariosBaseDeDatos() {
        ColeccionUsuarios resultado = new ColeccionUsuarios();
        try {
            Usuario usuarioActual;
            Connection baseDatos = Singleton.getConnection();
            Statement stmt = baseDatos.createStatement();
            ResultSet rs = stmt.executeQuery("select * from usuario");
            while (rs.next()) {
                usuarioActual = new Usuario(
                        rs.getString(2),
                        rs.getString(1),
                        rs.getString(3),
                        new RolUsuario(rs.getString(4)));
                resultado.addUsuario(usuarioActual);
            }
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
        return resultado;
    }

    /**
     * Devuelve los usuarios que deben ser visibles para el usuario logueado
     *
     * @return Un linked list con todos los usuarios que deben ser visibles por el usuario logueado
     */
    public LinkedList<Usuario> getUsuariosVisibles() {
        ColeccionUsuarios usuarios = leerUsuariosBaseDeDatos();
        // Rol Administrador
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[9])) {
            return usuarios.getUsuariosLinkedList();
            // Resto de roles
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * Crea un usuario
     *
     * @param nombre     Nombre del usuario (p. ej: Federico Bonel): Hasta 50 caracteres
     * @param username   Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave      Clave usada por el usuario para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @param rolUsuario Rol del usuario a crear
     * @throws IllegalArgumentException Si alguno de los parametros es erroneo o si el usuario esta registrado
     */
    public void crearUsuario(String nombre, String username, String clave, RolUsuario rolUsuario) throws IllegalArgumentException {
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[0])) {
            Usuario usuario = new Usuario(nombre, username.replaceAll("\\s+", ""), clave, rolUsuario);
            // Verifica si ya esta creado
            leerUsuariosBaseDeDatos().addUsuario(usuario);
            // Guardar en base de datos
            agregarUsuarioABaseDeDatos(usuario);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Elimina un usuario
     *
     * @param usuario Usuario a eliminar
     * @throws IllegalArgumentException Si el usuario ya no existe en el sistema
     */
    public void eliminarUsuario(Usuario usuario) {
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[6]) &&
                !usuarioLogueado.getId().equals(usuario.getId())) {
            // Verifica si todavia existe en el sistema
            leerUsuariosBaseDeDatos().removeUsuario(usuario, PresentacionesControlador.leerPresentacionesBaseDeDatos(),
                    MunicipiosControlador.leerMunicipiosBaseDeDatos());
            // Eliminalo de la base de datos
            eliminarUsuarioDeBaseDeDatos(usuario);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Crea un usuario y lo agrega a la coleccion
     *
     * @param usuario Usuario a actualizar
     * @param clave   Nueva clave del usuario como string
     * @throws IllegalArgumentException Si la clave es invalida
     */
    public void actualizarClaveDe(Usuario usuario, String clave) throws IllegalArgumentException {
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[1])) {
            usuario.setClave(clave);
            // Actualizalo en la base de datos
            actualizarUsuarioEnBaseDeDatos(usuario, DB_CAMPOS[2], clave);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Agrega el usuario a la base de datos
     *
     * @param usuario Usuario a agregar
     */
    private void agregarUsuarioABaseDeDatos(Usuario usuario) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("insert into usuario values (?, ?, ?, ?)");
            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getClave());
            stmt.setString(4, usuario.rolUsuario.getNombreRol());
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Elimina el usuario de la base de datos
     *
     * @param usuario Usuario a eliminar
     */
    private void eliminarUsuarioDeBaseDeDatos(Usuario usuario) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("delete from usuario where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, usuario.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Actualiza el usuario en la base de datos en el campo especificado
     *
     * @param usuario          Usuario a actualizar
     * @param campoBaseDatos   Campo a actualizar
     * @param nuevoValorString Valor a poner en el campo como string
     */
    private void actualizarUsuarioEnBaseDeDatos(Usuario usuario, String campoBaseDatos, String nuevoValorString) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt =
                    baseDatos.prepareStatement("update usuario set " + campoBaseDatos + " = ? where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, nuevoValorString);
            stmt.setString(2, usuario.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Crea y muestra el formulario para pedir los datos de un nuevo usuario
     */
    public void mostrarFormularioCrear() {
        formularioCrearUsuario = new FormularioCrearUsuario(this);
        formularioCrearUsuario.ventana.setVisible(true);
    }

    /**
     * Crea y muestra el formulario para modificar los datos de un usuario existente
     *
     * @param usuarioAModificar usuario que se desea modificar en el formulario
     */
    public void mostrarFormularioModificar(Usuario usuarioAModificar) {
        formularioModificarUsuario = new FormularioModificarUsuario(this, usuarioAModificar);
        formularioModificarUsuario.ventana.setVisible(true);
    }

    /**
     * Metodo que lee las interacciones de los usuarios sobre las vistas gestionadas por este controlador
     *
     * @param event Evento generado por la interaccion del usuario
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String accion = event.getActionCommand();
        switch (accion) {
            // En caso que se desee crear un usuario
            case (StringsFinales.CREAR) -> {
                try {
                    // Toma los datos
                    String nombreUsuario = formularioCrearUsuario.idCampo.getText();
                    String nombre = formularioCrearUsuario.nombreCampo.getText();
                    String clave = String.valueOf(formularioCrearUsuario.claveCampo.getPassword());
                    String rol = String.valueOf(formularioCrearUsuario.opcionesRol.getSelectedItem());
                    // Intenta crear el usuario
                    crearUsuario(nombre, nombreUsuario, clave, new RolUsuario(rol));
                    // Cierra la ventana
                    formularioCrearUsuario.ventana.dispose();
                } catch (IllegalArgumentException e) {
                    // Si ocurrio algun error muestralo por pantalla
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                    System.out.println(e.getMessage());
                }
            }

            // En caso que se desee modificar un usuario
            case (StringsFinales.MODIFICAR) -> {
                try {
                    // Toma el usuario a modificar
                    Usuario usuarioAModificar = formularioModificarUsuario.usuarioAModificar;
                    // Toma la clave a modificar del formulario
                    String nuevaClave = String.valueOf(formularioModificarUsuario.claveCampo.getPassword());
                    // Verifica si se desea modificar su clave
                    if (!nuevaClave.equals(usuarioAModificar.getClave()))
                        actualizarClaveDe(usuarioAModificar, nuevaClave);
                    // Cierra la ventana
                    formularioModificarUsuario.ventana.dispose();
                } catch (IllegalArgumentException e) {
                    // Si ocurrio algun error muestralo por pantalla
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                    System.out.println(e.getMessage());
                }

            }
        }
    }
}
