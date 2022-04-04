package com.fedebonel.respositorios;

import com.fedebonel.modelo.evento.Convocatoria;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Interfaz que abstrae a un repositorio de convocatorias
 */
public interface ConvocatoriasRepositorio extends RepositorioCRUD<Convocatoria, String>{

    /**
     * Agrega el documento a la convocatoria
     *
     * @param convocatoria Convocatoria a la que se desea agregar el documento
     * @param documento Documento a agregar a la convocatoria
     */
    void agregarDocConvocatoria(Convocatoria convocatoria, String documento)  throws SQLException;

    /**
     * Lee los documentos de la convocatoria pasada desde la base de datos
     *
     * @param idConvocatoria Convocatoria de la que se desean obtener los documentos
     * @return LinkedList con todos los documentos de la presentacion
     * @throws SQLException Si ocurre algun error en la letura de los documentos
     */
    LinkedList<String> leerDocsConvocatoria(String idConvocatoria) throws SQLException;

    /**
     * Elimina el documento de la convocatoria
     *
     * @param convocatoria Convocatoria de la que se desea eliminar el documento
     * @param documento Documento a eliminar de la convocatoria
     */
    void removerDocConvocatoria(Convocatoria convocatoria, String documento) throws SQLException;
}
