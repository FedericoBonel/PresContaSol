package com.fedebonel.respositorios;

import java.sql.SQLException;
import java.util.List;

/**
 * Repositorio de acceso CRUD
 */
public interface RepositorioCRUD<T, ID> {
    /**
     * Guarda la entidad en el repositorio
     *
     * @param entidad Entidad a guardar
     * @throws SQLException Si no se consiguio conectarse
     */
    void guardar(T entidad) throws SQLException;

    /**
     * Devuelve todas las entidades contenidas en el repositorio
     *
     * @return Todas las entidades como una lista
     * @throws SQLException Si no consiguio conectarse
     */
    List<T> leerTodo() throws SQLException;

    /**
     * Devuelve la entidad que contenga esa ID, si no un valor nulo
     *
     * @param id Identificador de la entidad
     * @return Entidad encontrada, null si no encontro ninguna
     * @throws SQLException Si no consiguio conectarse
     */
    T leerPorId(ID id) throws SQLException;

    /**
     * Elimina la entidad que tenga esa id del repositorio, si no no hace nada
     *
     * @param id Identificador de la entidad
     * @throws SQLException Si no consiguio conectarse
     */
    void eliminarPorId(ID id) throws SQLException;

    /**
     * Actualiza la entidad en el respositorio que tenga esa id, en ese campo, con ese valor
     *
     * @param id    Identificador de la entidad
     * @param campo Campo a actualizar
     * @param valor Valor a poner
     * @throws SQLException Si no consiguio conectarse
     */
    void actualizarPorId(ID id, String campo, String valor) throws SQLException;
}
