package com.fedebonel.servicios;

import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.respositorios.UsuariosRepositorio;

import java.sql.SQLException;
import java.util.LinkedList;

public class UsuariosServicio implements EntidadServicio<String, Usuario> {

    /**
     * String constante que posee el error cuando el usuario esta registrado
     */
    public static final String ERROR_USUARIO_REGISTRADO = "Usuario ya registrado";
    /**
     * String constante que posee el error cuando el usuario no esta registrado
     */
    public static final String ERROR_USUARIO_NO_REGISTRADO = "Usuario no registrado";

    /**
     * Repositorio de usuarios
     */
    private final UsuariosRepositorio usuariosRepositorio;

    /**
     * Constructor del servicio de usuarios
     *
     * @param usuariosRepositorio Repositorio a utilizar para conectarse a la base de datos
     */
    public UsuariosServicio(UsuariosRepositorio usuariosRepositorio) {
        this.usuariosRepositorio = usuariosRepositorio;
    }

    /**
     * Retorna a todos los usuarios contenidos en la base de datos
     *
     * @return Todos los usuarios contenidos en la base de datos como lista
     * @throws SQLException Si algun error en la conexion y sentencia de SQL es detectado
     */
    @Override
    public LinkedList<Usuario> leerTodo() throws SQLException {
        return (LinkedList<Usuario>) usuariosRepositorio.leerTodo();
    }

    /**
     * Retorna al usuario que contenga ese identificador o nulo si no existe
     *
     * @param id Identificador de la entidad a leer
     * @return Usuario que contenga dicho identificador
     * @throws SQLException Si algun error en la conexion y sentencia de SQL es detectado
     */
    @Override
    public Usuario leerPorID(String id) throws SQLException {
        return usuariosRepositorio.leerPorId(id);
    }

    /**
     * Busca usuarios en la base de datos por nombre
     *
     * @param name Nombre del usuario a buscar
     * @return El usuario que posea ese nombre o null en caso que no exista
     * @throws SQLException Si algun error en la conexion y sentencia de SQL es detectado
     */
    public Usuario searchByName(String name) throws SQLException {
        return usuariosRepositorio.searchByName(name);
    }

    @Override
    public void registrar(Usuario usuario) throws SQLException {
        Usuario foundUser = usuariosRepositorio.leerPorId(usuario.getId());
        if (foundUser != null) throw new IllegalArgumentException(ERROR_USUARIO_REGISTRADO);
        usuariosRepositorio.guardar(usuario);
    }

    @Override
    public void eliminar(Usuario usuario) throws IllegalArgumentException, SQLException {
        Usuario foundUser = usuariosRepositorio.leerPorId(usuario.getId());
        if (foundUser == null) throw new IllegalArgumentException(ERROR_USUARIO_NO_REGISTRADO);
        usuariosRepositorio.eliminarPorId(usuario.getId());
    }

    @Override
    public void actualizar(Usuario usuario, String field, String value) throws IllegalArgumentException, SQLException {
        Usuario foundUser = usuariosRepositorio.leerPorId(usuario.getId());
        if (foundUser == null) throw new IllegalArgumentException(ERROR_USUARIO_NO_REGISTRADO);
        usuariosRepositorio.actualizarPorId(usuario.getId(), field, value);
    }
}
