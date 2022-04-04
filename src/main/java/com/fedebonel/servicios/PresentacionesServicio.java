package com.fedebonel.servicios;

import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.respositorios.PresentacionesRepositorio;

import java.sql.SQLException;
import java.util.LinkedList;

public class PresentacionesServicio implements EntidadServicio<String, Presentacion>{

    /**
     * String constante que posee el error cuando la presentacion esta registrada
     */
    public static final String ERROR_PRESENTACION_REGISTRADA = "Presentacion ya registrada";
    /**
     * String constante que posee el error cuando la presentacion esta registrada
     */
    public static final String ERROR_PRESENTACION_NO_REGISTRADA = "Presentacion no registrada";

    private final PresentacionesRepositorio presentacionesRepositorio;

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

    public void agregarDocumento(Presentacion presentacion, String document) throws SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        presentacionesRepositorio.agregarDocPresentacion(presentacion, document);
    }

    public void eliminarDocumento(Presentacion presentacion, String document) throws SQLException {
        Presentacion foundPresentacion = presentacionesRepositorio.leerPorId(presentacion.getId());
        if (foundPresentacion == null) throw new IllegalArgumentException(ERROR_PRESENTACION_NO_REGISTRADA);
        presentacionesRepositorio.removerDocPresentacion(presentacion, document);
    }
}
