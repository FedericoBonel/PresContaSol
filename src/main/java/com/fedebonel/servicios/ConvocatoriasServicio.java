package com.fedebonel.servicios;

import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.respositorios.ConvocatoriasRepositorio;

import java.sql.SQLException;
import java.util.LinkedList;

public class ConvocatoriasServicio implements EntidadServicio<String, Convocatoria> {

    /**
     * String constante que posee el error cuando la convocatoria esta registrada
     */
    public static final String ERROR_CONVOCATORIA_REGISTRADA = "Convocatoria ya registrada";
    /**
     * String constante que posee el error cuando la convocatoria no esta registrada
     */
    public static final String ERROR_CONVOCATORIA_NO_REGISTRADA = "Convocatoria no registrada";

    /**
     * Repositorio de convocatorias
     */
    private final ConvocatoriasRepositorio convocatoriasRepositorio;

    /**
     * Constructor del servicio de convocatorias
     *
     * @param convocatoriasRepositorio Repositorio de convocatorias a utililizar en este servicio
     */
    public ConvocatoriasServicio(ConvocatoriasRepositorio convocatoriasRepositorio) {
        this.convocatoriasRepositorio = convocatoriasRepositorio;
    }

    @Override
    public LinkedList<Convocatoria> leerTodo() throws SQLException {
        return (LinkedList<Convocatoria>) convocatoriasRepositorio.leerTodo();
    }

    @Override
    public Convocatoria leerPorID(String id) throws SQLException {
        return convocatoriasRepositorio.leerPorId(id);
    }

    @Override
    public void registrar(Convocatoria convocatoria) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria != null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_REGISTRADA);
        convocatoriasRepositorio.guardar(convocatoria);
    }

    @Override
    public void eliminar(Convocatoria convocatoria) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoriasRepositorio.eliminarPorId(convocatoria.getId());
    }

    @Override
    public void actualizar(Convocatoria convocatoria, String field, String value) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoriasRepositorio.actualizarPorId(convocatoria.getId(), field, value);
    }

    /**
     * Agrega el documento a la convocatoria indicada
     *
     * @param convocatoria Convocatoria a la que se debe agregar el documento
     * @param document     Documento a agregar
     * @throws IllegalArgumentException Si la convocatoria no existe en la base de datos
     * @throws SQLException             Si algun error en la conexion y sentencia de SQL es detectado
     */
    public void agregarDocumento(Convocatoria convocatoria, String document) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoriasRepositorio.agregarDocConvocatoria(convocatoria, document);
    }

    /**
     * Remueve un documento de la convocatoria indicada
     *
     * @param convocatoria Convocatoria de la cual se debe remover un documento
     * @param document     Documento a remover
     * @throws IllegalArgumentException Si la convocatoria no existe
     * @throws SQLException             Si algun error en la conexion y sentencia de SQL es detectado
     */
    public void removerDocumento(Convocatoria convocatoria, String document) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoriasRepositorio.removerDocConvocatoria(convocatoria, document);
    }
}
