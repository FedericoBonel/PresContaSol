package respositorios.mySQL;

import modelo.dataAccess.ConexionDB;
import modelo.evento.Convocatoria;
import respositorios.ConvocatoriasRepositorio;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Repositorio de convocatorias en MySQL
 */
public class ConvocatoriasRepositorioMySQL implements ConvocatoriasRepositorio {

    @Override
    public void guardar(Convocatoria entidad) throws SQLException {
        Connection baseDatos = ConexionDB.getConnection();
        PreparedStatement stmt = baseDatos.prepareStatement("insert into convocatoria values (?, ?, ?, ?)");
        stmt.setString(1, entidad.getId());
        stmt.setDate(2, Date.valueOf(entidad.getFechaInicio()));
        stmt.setDate(3, Date.valueOf(entidad.getFechaCierre()));
        stmt.setString(4, entidad.getDescripcion());
        stmt.executeUpdate();
        for (String documento : entidad.getDocumentos().getDocumentosLinkedList())
            agregarDocConvocatoria(entidad, documento);
    }

    @Override
    public List<Convocatoria> leerTodo() throws SQLException {
        LinkedList<Convocatoria> result = new LinkedList<>();
        LinkedList<String> documentos;
        Convocatoria convocActual;
        Connection conn = ConexionDB.getConnection();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from convocatoria");
        while (rs.next()) {
            documentos = leerDocsConvocatoria(rs.getString(1));
            convocActual = new Convocatoria(rs.getString(1),
                    LocalDate.parse(formatter.format(rs.getTimestamp(2))),
                    LocalDate.parse(formatter.format(rs.getTimestamp(3))),
                    documentos,
                    rs.getString(4));
            result.add(convocActual);
        }
        return result;
    }

    @Override
    public Convocatoria leerPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from convocatoria where identificador=?");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;
        LinkedList<String> documentos = leerDocsConvocatoria(rs.getString(1));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return new Convocatoria(rs.getString(1),
                LocalDate.parse(formatter.format(rs.getTimestamp(2))),
                LocalDate.parse(formatter.format(rs.getTimestamp(3))),
                documentos,
                rs.getString(4));
    }

    @Override
    public void eliminarPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt = conn.prepareStatement("delete from convocatoria where identificador = ?");
        stmt.setString(1, id);
        stmt.executeUpdate();
    }

    @Override
    public void actualizarPorId(String id, String campo, String valor) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt;
        stmt = conn.prepareStatement("update convocatoria set " + campo + " = ? where identificador = ?");
        stmt.setString(1, valor);
        stmt.setString(2, id);
        stmt.executeUpdate();
    }

    @Override
    public LinkedList<String> leerDocsConvocatoria(String idConvocatoria) throws SQLException {
        ResultSet documentosRs;
        LinkedList<String> documentos = new LinkedList<>();
        PreparedStatement tablaDocumentos =
                ConexionDB.getConnection().prepareStatement("select * from docmnt_cnvctria where convocatoria = ?");
        tablaDocumentos.setString(1, idConvocatoria);
        documentosRs = tablaDocumentos.executeQuery();
        while (documentosRs.next()) {
            documentos.add(documentosRs.getString(1));
        }
        return documentos;
    }

    @Override
    public void removerDocConvocatoria(Convocatoria convocatoria, String documento) throws SQLException {
        Connection baseDatos = ConexionDB.getConnection();
        PreparedStatement stmt;
        stmt = baseDatos.prepareStatement("delete from docmnt_cnvctria where convocatoria = ? and nombre = ?");
        stmt.setString(1, convocatoria.getId());
        stmt.setString(2, documento);
        stmt.executeUpdate();
    }

    @Override
    public void agregarDocConvocatoria(Convocatoria convocatoria, String documento) throws SQLException {
        Connection baseDatos = ConexionDB.getConnection();
        PreparedStatement stmt;
        stmt = baseDatos.prepareStatement("insert into docmnt_cnvctria values (?, ?)");
        stmt.setString(1, documento);
        stmt.setString(2, convocatoria.getId());
        stmt.executeUpdate();
    }
}
