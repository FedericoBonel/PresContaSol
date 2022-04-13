package com.fedebonel.servicios;

import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.respositorios.MunicipiosRepositorio;

import java.sql.SQLException;
import java.util.LinkedList;

public class MunicipiosServicio implements EntidadServicio<String, Municipio> {

    /**
     * String constante que pose el error cuando el municipio esta registrado
     */
    public static final String ERROR_MUNICIPIO_REGISTRADO = "Municipio ya registrado";
    /**
     * String constante que pose el error cuando el municipio no esta registrado
     */
    public static final String ERROR_MUNICIPIO_NO_REGISTRADO = "Municipio no registrado";

    /**
     * Repositorio de municipios a utilizar en esta clase
     */
    private final MunicipiosRepositorio municipiosRepositorio;

    /**
     * Constructor del servicio de municipios
     *
     * @param municipiosRepositorio Repositorio de municipios a utilizar
     */
    public MunicipiosServicio(MunicipiosRepositorio municipiosRepositorio) {
        this.municipiosRepositorio = municipiosRepositorio;
    }

    @Override
    public LinkedList<Municipio> leerTodo() throws SQLException {
        return (LinkedList<Municipio>) municipiosRepositorio.leerTodo();
    }

    @Override
    public Municipio leerPorID(String id) throws SQLException {
        return municipiosRepositorio.leerPorId(id);
    }

    @Override
    public void registrar(Municipio municipio) throws IllegalArgumentException, SQLException {
        Municipio foundMunicipio = municipiosRepositorio.leerPorId(municipio.getId());
        if (foundMunicipio != null) throw new IllegalArgumentException(ERROR_MUNICIPIO_REGISTRADO);
        municipiosRepositorio.guardar(municipio);
    }

    @Override
    public void eliminar(Municipio municipio) throws IllegalArgumentException, SQLException {
        Municipio foundMunicipio = municipiosRepositorio.leerPorId(municipio.getId());
        if (foundMunicipio == null) throw new IllegalArgumentException(ERROR_MUNICIPIO_NO_REGISTRADO);
        municipiosRepositorio.eliminarPorId(municipio.getId());
    }

    @Override
    public void actualizar(Municipio municipio, String field, String value) throws IllegalArgumentException, SQLException {
        Municipio foundMunicipio = municipiosRepositorio.leerPorId(municipio.getId());
        if (foundMunicipio == null) throw new IllegalArgumentException(ERROR_MUNICIPIO_NO_REGISTRADO);
        municipiosRepositorio.actualizarPorId(municipio.getId(), field, value);
    }
}
