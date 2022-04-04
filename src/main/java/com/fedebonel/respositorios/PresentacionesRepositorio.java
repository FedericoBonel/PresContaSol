package com.fedebonel.respositorios;

import com.fedebonel.modelo.evento.Presentacion;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Interfaz que abstrae a un repositorio de presentacion
 */
public interface PresentacionesRepositorio extends RepositorioCRUD<Presentacion, String>{

    /**
     * Lee los documentos de la presentacion pasada desde la base de datos
     *
     * @param idPresentacion Presentacion de la que se desean obtener los documentos
     * @return LinkedList con todos los documentos de la presentacion
     * @throws SQLException Si ocurre algun error al leer los documentos
     */
    LinkedList<String> readAllDocumentsFromPresentation(String idPresentacion) throws SQLException;

    /**
     * Agrega el documento a la presentacion
     *
     * @param presentacion Presentacion a la que se desea agregar el documento
     * @param documento    Documento a agregar a la presentacion
     */
    void agregarDocPresentacion(Presentacion presentacion, String documento) throws SQLException;

    /**
     * Elimina el documento de la presentacion
     *
     * @param presentacion Presentacion de la que se desea eliminar el documento
     * @param documento    Documento a eliminar de la presentacion
     */
    void removerDocPresentacion(Presentacion presentacion, String documento) throws SQLException;
}
