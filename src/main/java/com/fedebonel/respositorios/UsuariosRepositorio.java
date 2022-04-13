package com.fedebonel.respositorios;

import com.fedebonel.modelo.usuario.Usuario;

import java.sql.SQLException;

/**
 * Interfaz que abstrae a un repositorio de usuarios
 */
public interface UsuariosRepositorio extends RepositorioCRUD<Usuario, String> {

    /**
     * Retorna El usuario que tenga este nombre asignado
     *
     * @param name Nombre del usuario a buscar
     * @return El usuario con ese nombre
     * @throws SQLException Si algun error en la conexion y sentencia de SQL es detectado
     */
    Usuario searchByName(String name) throws SQLException;
}
