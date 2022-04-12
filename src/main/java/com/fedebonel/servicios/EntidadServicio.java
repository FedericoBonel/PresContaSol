package com.fedebonel.servicios;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Servicio de una entidad
 * <p>
 * I = Tipo de dato usado para el identificador
 * T = Tipo de dato usado para las entidades guardadas
 */
public interface EntidadServicio<I, T> {

    /**
     * Lee todas las entidades de la persistencia
     *
     * @return Una lista con todos los elementos
     * @throws SQLException Si hubo algun error en el acceso a base de datos
     */
    LinkedList<T> leerTodo() throws SQLException;

    /**
     * Lee la entidad que tenga ese id de la persistencia
     *
     * @param id Identificador de la entidad a leer
     * @return La entidad que posea ese identificador
     * @throws SQLException Si hubo algun error en el acceso a base de datos
     */
    T leerPorID(I id) throws SQLException;

    /**
     * Registra la entidad en la base de datos
     *
     * @param entity Entidad a registrar
     * @throws IllegalArgumentException Si la entidad ya existe en el sistema
     * @throws SQLException             Si hubo algun error en el acceso a base de datos
     */
    void registrar(T entity) throws IllegalArgumentException, SQLException;

    /**
     * Elimina la entidad de la base de datos
     *
     * @param entity Entidad a eliminar
     * @throws IllegalArgumentException Si la entidad no existe
     * @throws SQLException             Si hubo algun error en el acceso a base de datos
     */
    void eliminar(T entity) throws IllegalArgumentException, SQLException;

    /**
     * Actualiza la entidad en la base de datos en el campo requerido con el valor pasado
     *
     * @param entity Entidad a actualizar
     * @param field  Campo a actualizar de la entidad en la base de datos
     * @param value  Nuevo valor a asignar
     * @throws IllegalArgumentException Si la entidad no existe
     * @throws SQLException             Si hubo algun error en el acceso a base de datos
     */
    void actualizar(T entity, String field, String value) throws IllegalArgumentException, SQLException;
}
