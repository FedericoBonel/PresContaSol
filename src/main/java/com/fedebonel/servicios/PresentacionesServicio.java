package com.fedebonel.servicios;

import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.respositorios.PresentacionesRepositorio;

import java.sql.SQLException;
import java.util.LinkedList;

public class PresentacionesServicio implements EntidadServicio<String, Presentacion> {

    /**
     * String constante que posee el error cuando la presentacion esta registrada
     */
    public static final String ERROR_PRESENTACION_REGISTRADA = "Presentacion ya registrada";
    /**
     * String constante que posee el error cuando la presentacion esta registrada
     */
    public static final String ERROR_PRESENTACION_NO_REGISTRADA = "Presentacion no registrada";

    /**
     * Repositorio de presentaciones a ser utilizado en esta clase
     */
    private final PresentacionesRepositorio presentacionesRepositorio;

    /**
     * Constructor del servicio de presentaciones
     *
     * @param presentacionesRepositorio Repositorio de presentaciones a utilizar
     */
    public PresentacionesServicio(PresentacionesRepositorio presentacionesRepositorio) {
        this.presentacionesRepositorio = presentacionesRepositorio;
    }

    @Override
    public LinkedList<Presentacion> leerTodo() throws SQLException {
        return (LinkedList<Presentacion>) presentacionesRepositorio.leerTodo();
    }

    @Override
    public Presentacion leerPorID(String id) throws SQLException {
        return presentacionesRepositorio.leerPorId(id);
    }

    @Override
    public void registrar(Presentacion presentacion) throws IllegalArgumentException, SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion != null) throw new IllegalArgumentException(ERROR_PRESENTACION_REGISTRADA);
        presentacionesRepositorio.guardar(presentacion);
    }

    @Override
    public void eliminar(Presentacion presentacion) throws IllegalArgumentException, SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        presentacionesRepositorio.eliminarPorId(presentacion.getId());
    }

    @Override
    public void actualizar(Presentacion presentacion, String field, String value) throws IllegalArgumentException, SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        presentacionesRepositorio.actualizarPorId(presentacion.getId(), field, value);
    }

    /**
     * Agrega un documento a la presentacion indicada
     *
     * @param presentacion Presentacion a la que agregar un documento
     * @param document     Documento a agregar
     * @throws SQLException Si algun error en la conexion y sentencia de SQL es detectado
     */
    public void agregarDocumento(Presentacion presentacion, String document) throws SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        presentacionesRepositorio.agregarDocPresentacion(presentacion, document);
    }

    /**
     * Elimina un documento de la presentacion indicada
     *
     * @param presentacion Presentacion de la que eliminar documento
     * @param document     Documento a eliminar
     * @throws SQLException Si algun error en la conexion y sentencia de SQL es detectado
     */
    public void eliminarDocumento(Presentacion presentacion, String document) throws SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        presentacionesRepositorio.removerDocPresentacion(presentacion, document);
    }
}
