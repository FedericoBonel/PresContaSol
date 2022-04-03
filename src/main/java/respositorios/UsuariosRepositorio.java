package respositorios;

import modelo.usuario.Usuario;

import java.sql.SQLException;

/**
 * Interfaz que abstrae a un repositorio de usuarios
 */
public interface UsuariosRepositorio extends RepositorioCRUD<Usuario, String>{

    Usuario searchByName(String name) throws SQLException;
}
