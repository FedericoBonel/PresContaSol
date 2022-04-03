package servicios;

import modelo.evento.Convocatoria;
import respositorios.ConvocatoriasRepositorio;

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

    private final ConvocatoriasRepositorio convocatoriasRepositorio;

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

    public void agregarDocumento(Convocatoria convocatoria, String document) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoriasRepositorio.agregarDocConvocatoria(convocatoria, document);
    }

    public void removerDocumento(Convocatoria convocatoria, String document) throws IllegalArgumentException, SQLException {
        Convocatoria foundConvocatoria = convocatoriasRepositorio.leerPorId(convocatoria.getId());
        if (foundConvocatoria == null) throw new IllegalArgumentException(ERROR_CONVOCATORIA_NO_REGISTRADA);
        convocatoriasRepositorio.removerDocConvocatoria(convocatoria, document);
    }
}
