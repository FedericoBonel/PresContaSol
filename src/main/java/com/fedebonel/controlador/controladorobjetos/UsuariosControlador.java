package com.fedebonel.controlador.controladorobjetos;

import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.UsuariosServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.creacion.FormularioCrearUsuario;
import com.fedebonel.vista.formularios.modificacion.FormularioModificarUsuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
     * Servicio de usuarios
     */
    private final UsuariosServicio usuariosServicio;
    /**
     * Usuario logueado que esta utilizando el sistema
     */
    private Usuario usuarioLogueado;
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
     * @param usuariosServicio Servicio de usuarios
     */
    public UsuariosControlador(UsuariosServicio usuariosServicio) {
        this.usuariosServicio = usuariosServicio;
    }

    /**
     * Asigna el usuario que utiliza este controlador
     *
     * @param usuarioLogueado Usuario a asignar
     */
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Devuelve los usuarios que deben ser visibles para el usuario logueado
     *
     * @return Un linked list con todos los usuarios que deben ser visibles por el usuario logueado
     */
    public LinkedList<Usuario> getUsuariosVisibles() {
        try {
            LinkedList<Usuario> usuarios = usuariosServicio.leerTodo();
            // Rol Administrador
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[9])) {
                return usuarios;
                // Resto de roles
            } else {
                return new LinkedList<>();
            }
        } catch (SQLException e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
    public void crearUsuario(String nombre,
                             String username,
                             String clave,
                             RolUsuario rolUsuario) throws IllegalArgumentException {
        try {
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[0])) {
                Usuario usuario = new Usuario(nombre, username.replaceAll("\\s+", ""), clave, rolUsuario);
                usuariosServicio.registrar(usuario);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (SQLException e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Elimina un usuario
     *
     * @param usuario Usuario a eliminar
     * @throws IllegalArgumentException Si el usuario ya no existe en el sistema
     */
    public void eliminarUsuario(Usuario usuario) {
        try {
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[6])
                    && !usuarioLogueado.getId().equals(usuario.getId())) {
                usuariosServicio.eliminar(usuario);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
        try {
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[0], RolUsuario.ACCIONES[1])) {
                usuario.setClave(clave);
                // Actualizalo en la base de datos
                usuariosServicio.actualizar(usuario, DB_CAMPOS[2], clave);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
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
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
                }

            }
        }
    }
}
