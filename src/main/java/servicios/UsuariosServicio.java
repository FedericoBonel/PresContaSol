package servicios;

import modelo.usuario.Usuario;
import respositorios.UsuariosRepositorio;

import java.sql.SQLException;
import java.util.LinkedList;

public class UsuariosServicio implements EntidadServicio<String, Usuario>{

    /**
     * String constante que posee el error cuando el usuario esta registrado
     */
    public static final String ERROR_USUARIO_REGISTRADO = "Usuario ya registrado";
    /**
     * String constante que posee el error cuando el usuario no esta registrado
     */
    public static final String ERROR_USUARIO_NO_REGISTRADO = "Usuario no registrado";

    private final UsuariosRepositorio usuariosRepositorio;

    public UsuariosServicio(UsuariosRepositorio usuariosRepositorio) {
        this.usuariosRepositorio = usuariosRepositorio;
    }

    @Override
    public LinkedList<Usuario> leerTodo() throws SQLException {
        return (LinkedList<Usuario>) usuariosRepositorio.leerTodo();
    }

    @Override
    public Usuario leerPorID(String id) throws SQLException {
        return usuariosRepositorio.leerPorId(id);
    }

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
