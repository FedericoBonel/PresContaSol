package respositorios.mySQL;


import modelo.dataAccess.ConexionDB;
import modelo.evento.Presentacion;
import respositorios.ConvocatoriasRepositorio;
import respositorios.MunicipiosRepositorio;
import respositorios.PresentacionesRepositorio;
import respositorios.UsuariosRepositorio;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Repositorio de presentaciones en MySQL
 */
public class PresentacionesRepositorioMySQL implements PresentacionesRepositorio {
    private final ConvocatoriasRepositorio convocatoriasRepositorio;
    private final UsuariosRepositorio usuariosRepositorio;
    private final MunicipiosRepositorio municipiosRepositorio;

    public PresentacionesRepositorioMySQL(ConvocatoriasRepositorio convocatoriasRepositorio,
                                          UsuariosRepositorio usuariosRepositorio,
                                          MunicipiosRepositorio municipiosRepositorio) {
        this.convocatoriasRepositorio = convocatoriasRepositorio;
        this.usuariosRepositorio = usuariosRepositorio;
        this.municipiosRepositorio = municipiosRepositorio;
    }

    @Override
    public void guardar(Presentacion entidad) throws SQLException {
        Connection baseDatos = ConexionDB.getConnection();
        PreparedStatement stmt = baseDatos.prepareStatement("insert into presentacion values (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, entidad.getId());
        stmt.setDate(2, Date.valueOf(entidad.getFechaInicio()));
        stmt.setBoolean(3, entidad.isAbierto());
        stmt.setString(4, entidad.getConvocatoria().getId());
        stmt.setString(5, entidad.getAutor().getId());
        stmt.setString(6, entidad.getMunicipio().getId());
        stmt.executeUpdate();
        for (String documento : entidad.getDocumentos().getDocumentosLinkedList())
            agregarDocPresentacion(entidad, documento);
    }

    @Override
    public List<Presentacion> leerTodo() throws SQLException {
        List<Presentacion> result = new LinkedList<>();
        Presentacion presActual;
        LinkedList<String> documentos;
        Connection conn = ConexionDB.getConnection();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from presentacion");
        while (rs.next()) {
            documentos = readAllDocumentsFromPresentation(rs.getString(1));
            presActual = new Presentacion(rs.getString(1),
                    LocalDate.parse(formatter.format(rs.getTimestamp(2))),
                    rs.getBoolean(3),
                    convocatoriasRepositorio.leerPorId(rs.getString(4)),
                    usuariosRepositorio.leerPorId(rs.getString(5)),
                    municipiosRepositorio.leerPorId(rs.getString(6)),
                    documentos);
            result.add(presActual);
        }
        return result;
    }

    @Override
    public Presentacion leerPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement stmt = conn.prepareStatement("select * from presentacion where identificador=?");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;
        LinkedList<String> documentos = readAllDocumentsFromPresentation(rs.getString(1));
        return new Presentacion(rs.getString(1),
                LocalDate.parse(formatter.format(rs.getTimestamp(2))),
                rs.getBoolean(3),
                convocatoriasRepositorio.leerPorId(rs.getString(4)),
                usuariosRepositorio.leerPorId(rs.getString(5)),
                municipiosRepositorio.leerPorId(rs.getString(6)),
                documentos);
    }

    @Override
    public void eliminarPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt = conn.prepareStatement("delete from presentacion where identificador = ?");
        stmt.setString(1, id);
        stmt.executeUpdate();
    }

    @Override
    public void actualizarPorId(String id, String campo, String valor) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt;
        stmt = conn.prepareStatement("update presentacion set " + campo + " = ? where identificador = ?");
        stmt.setString(1, valor);
        stmt.setString(2, id);
        stmt.executeUpdate();
    }

    /**
     * Lee los documentos de la presentacion pasada desde la base de datos
     *
     * @param idPresentacion Presentacion de la que se desean obtener los documentos
     * @return LinkedList con todos los documentos de la presentacion
     * @throws SQLException Si ocurre algun error al leer los documentos
     */
    public LinkedList<String> readAllDocumentsFromPresentation(String idPresentacion) throws SQLException {
        ResultSet documentosRs;
        LinkedList<String> documentos = new LinkedList<>();
        PreparedStatement tablaDocumentos =
                ConexionDB.getConnection().prepareStatement("select * from docmnt_prsntcion where presentacion = ?");
        tablaDocumentos.setString(1, idPresentacion);
        documentosRs = tablaDocumentos.executeQuery();
        while (documentosRs.next()) {
            documentos.add(documentosRs.getString(1));
        }
        return documentos;
    }

    @Override
    public void agregarDocPresentacion(Presentacion presentacion, String documento) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt;
        stmt = conn.prepareStatement("insert into docmnt_prsntcion values (?, ?)");
        stmt.setString(1, documento);
        stmt.setString(2, presentacion.getId());
        stmt.executeUpdate();
    }

    @Override
    public void removerDocPresentacion(Presentacion presentacion, String documento) throws SQLException {
        Connection baseDatos = ConexionDB.getConnection();
        PreparedStatement stmt;
        stmt = baseDatos.prepareStatement("delete from docmnt_prsntcion where presentacion = ? and nombre = ?");
        stmt.setString(1, presentacion.getId());
        stmt.setString(2, documento);
        stmt.executeUpdate();
    }
}
