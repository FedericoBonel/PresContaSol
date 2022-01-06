package controlador;

import modelo.*;
import vista.StringsFinales;
import vista.formularios.FormularioCrearPresentacion;
import vista.formularios.FormularioModificarPresentacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Clase contenedora de todos los metodos que gestionan presentaciones
 */
public class PresentacionesControlador implements ActionListener {

    /**
     * Nombres de los campos en la base de datos
     */
    private static final String[] DB_CAMPOS = new String[]{"identificador", "fecha_creacion", "apertura", "convocatoria", "autor", "municipio"};

    /**
     * Usuario logueado que esta utilizando el sistema
     */
    private final Usuario usuarioLogueado;

    /**
     * Formulario de creacion de presentaciones que este controlador debe gestionar
     */
    private FormularioCrearPresentacion formularioCrearPresentacion;
    /**
     * Formulario de modificacion de presentaciones que este controlador debe gestionar
     */
    private FormularioModificarPresentacion formularioModificarPresentacion;

    /**
     * Costructor del controlador
     *
     * @param usuarioLogueado Usuario autenticado en el sistema que utilizara el controlador
     */
    public PresentacionesControlador(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Devuelve las presentaciones que deben ser visibles para el usuario logueado
     *
     * @return Un linked list con todas las presentaciones que deben ser visibles por el usuario logueado
     */
    public LinkedList<Presentacion> getPresentacionesVisibles() {
        ColeccionPresentaciones presentaciones = leerPresentacionesBaseDeDatos();
        // Rol Administrador y rol fiscal general
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[9])) {
            return presentaciones.getPresentacionesLinkedList();
            // Rol Cuentadante
        } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[14])) {
            LinkedList<Presentacion> presentacionesRealizadas = new LinkedList<>();
            for (Presentacion presentacion : presentaciones.getPresentacionesLinkedList()) {
                if (presentacion.isAutor(usuarioLogueado)) presentacionesRealizadas.add(presentacion);
            }
            return presentacionesRealizadas;
            // Rol Fiscal
        } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[12])) {
            LinkedList<Presentacion> presentacionesVisibles = new LinkedList<>();
            for (Presentacion presentacion : presentaciones.getPresentacionesLinkedList()) {
                if (presentacion.getMunicipio().isFiscal(usuarioLogueado))
                    presentacionesVisibles.add(presentacion);
            }
            return presentacionesVisibles;
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * Crea una presentacion
     *
     * @param id             Identificador alfanumerico unico de presentacion: Puede tener desde 1 caracter hasta 100 caracteres
     * @param convocatoria   Convocatoria a la cual se presenta: No puede estar cerrada
     * @param docsEntregados Documentos entregados en la presentacion para la convocatoria
     * @throws IllegalArgumentException Si alguno de los parametros es erroneo o la presentacion esta registrada en el sistema
     */
    public void crearPresentacion(String id, Convocatoria convocatoria, LinkedList<String> docsEntregados)
            throws IllegalArgumentException {
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[0]) &&
                convocatoria.isAbierto()) {
            // Cuando el usuario crea una presentacion siempre se establece como abierta por defecto
            Presentacion nuevaPresentacion =
                    new Presentacion(id,
                            LocalDate.now(), true, convocatoria,
                            usuarioLogueado,
                            usuarioLogueado.getMunicipioRepresentadoDe(MunicipiosControlador.leerMunicipiosBaseDeDatos()),
                            docsEntregados);
            // Verifica si ya existe
            leerPresentacionesBaseDeDatos().addPresentacion(nuevaPresentacion);
            // Actualiza base de datos
            agregarPresentacionABaseDeDatos(nuevaPresentacion);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS + " o " + StringsFinales.ERROR_CONVOCATORIA_CERRADA, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Elimina la presentacion
     *
     * @param presentacion Presentacion que se desea eliminar
     * @throws IllegalArgumentException Si la presentacion ya no esta registrada en el sistema
     */
    public void eliminarPresentacion(Presentacion presentacion) {
        // Rol Administrador
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[6]) ||
                // Rol Cuentadante
                (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[8]) &&
                        presentacion.isAbierto() && presentacion.isAutor(usuarioLogueado))) {
            // Verifica que todavia existe
            leerPresentacionesBaseDeDatos().removePresentacion(presentacion);
            // Actualiza base de datos
            eliminarPresentacionDeBaseDeDatos(presentacion);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Retira la presentacion entregada (la abre)
     *
     * @param presentacion Presentacion que se desea abrir
     */
    public void retirarPresentacion(Presentacion presentacion) {
        // Rol Administrador y fiscal general
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4])) {
            presentacion.setAbierto(true);
            actualizarPresentacionEnBaseDeDatos(presentacion, DB_CAMPOS[2], String.valueOf(1));
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Cierra la presentacion especificada para entregarla de manera final al Fiscal
     *
     * @param presentacion Presentacion que se desea cerrar/entregar
     */
    public void entregarPresentacion(Presentacion presentacion) throws IllegalCallerException {
        // Rol Cuentadante
        if ((usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                presentacion.isAutor(usuarioLogueado) && presentacion.getConvocatoria().isAbierto() &&
                presentacion.todosDocsRequeridosEntregados()) ||
                usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4])) {
            presentacion.setAbierto(false);
            actualizarPresentacionEnBaseDeDatos(presentacion, DB_CAMPOS[2], String.valueOf(0));
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS + ", "
                            + StringsFinales.ERROR_CONVOCATORIA_CERRADA + " o "
                            + StringsFinales.ERROR_DOCUMENTOS_REQUERIDOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Establece el documento pasado como entregado en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     */
    public void entregarDocumentoA(Presentacion presentacion, String documento) {
        // Rol Administrador
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[1]) ||
                // Rol cuentadante
                (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                        presentacion.isAutor(usuarioLogueado) && presentacion.isAbierto())) {
            try {
                presentacion.addDocumento(documento);
                agregarDocPresEnBaseDeDatos(presentacion, documento);
            } catch (IllegalArgumentException e) {
                System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Retira el documento de la presentacion (i.e. se marca como no entregado)
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     */
    public void retirarDocumentoDe(Presentacion presentacion, String documento) throws IllegalCallerException {
        // Rol Administrador
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[1]) ||
                // Rol cuentadante
                (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                        presentacion.isAutor(usuarioLogueado) && presentacion.isAbierto())) {
            try {
                presentacion.removeDocumento(documento);
                eliminarDocPresEnBaseDeDatos(presentacion, documento);
            } catch (IllegalArgumentException e) {
                System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Lee las presentaciones desde la base de datos y las asigna a la coleccion de presentaciones del sistema
     *
     * @return Coleccion con todas las presentaciones de la base de datos
     */
    public static ColeccionPresentaciones leerPresentacionesBaseDeDatos() {
        ColeccionPresentaciones presentaciones = new ColeccionPresentaciones();
        try {
            Presentacion presActual;
            LinkedList<String> documentos;
            Connection baseDatos = Singleton.getConnection();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Statement stmt = baseDatos.createStatement();
            ResultSet rs = stmt.executeQuery("select * from presentacion");
            while (rs.next()) {
                documentos = leerDocumentosPresentacionesBaseDatos(rs.getString(1));
                presActual = new Presentacion(rs.getString(1),
                        LocalDate.parse(formatter.format(rs.getTimestamp(2))),
                        rs.getBoolean(3),
                        ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoria(rs.getString(4)),
                        UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(rs.getString(5)),
                        MunicipiosControlador.leerMunicipiosBaseDeDatos().getMunicipio(rs.getString(6)),
                        documentos);
                presentaciones.addPresentacion(presActual);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
        return presentaciones;
    }

    /**
     * Lee los documentos de la presentacion pasada desde la base de datos
     *
     * @param idPresentacion Presentacion de la que se desean obtener los documentos
     * @return LinkedList con todos los documentos de la presentacion
     * @throws SQLException Si ocurre algun error al leer los documentos
     */
    private static LinkedList<String> leerDocumentosPresentacionesBaseDatos(String idPresentacion) throws SQLException {
        ResultSet documentosRs;
        LinkedList<String> documentos = new LinkedList<>();
        PreparedStatement tablaDocumentos =
                Singleton.getConnection().prepareStatement("select * from docmnt_prsntcion where presentacion = ?");
        tablaDocumentos.setString(1, idPresentacion);
        documentosRs = tablaDocumentos.executeQuery();
        while (documentosRs.next()) {
            documentos.add(documentosRs.getString(1));
        }
        return documentos;
    }


    /**
     * Agrega la presentacion a la base de datos
     *
     * @param presentacion Presentacion a agregar
     */
    private void agregarPresentacionABaseDeDatos(Presentacion presentacion) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("insert into presentacion values (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, presentacion.getId());
            stmt.setDate(2, Date.valueOf(presentacion.getFechaInicio()));
            stmt.setBoolean(3, presentacion.isAbierto());
            stmt.setString(4, presentacion.getConvocatoria().getId());
            stmt.setString(5, presentacion.getAutor().getId());
            stmt.setString(6, presentacion.getMunicipio().getId());
            stmt.executeUpdate();
            for (String documento : presentacion.getDocumentos().getDocumentosLinkedList())
                agregarDocPresEnBaseDeDatos(presentacion, documento);
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Elimina la presentacion de la base de datos
     *
     * @param presentacion Presentacion a eliminar
     */
    private void eliminarPresentacionDeBaseDeDatos(Presentacion presentacion) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("delete from presentacion where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, presentacion.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Actualiza la presentacion en la base de datos en el campo especificado
     *
     * @param presentacion     Convocatoria a actualizar
     * @param campoBaseDatos   Campo a actualizar
     * @param nuevoValorString Valor a poner en el campo como string
     */
    private void actualizarPresentacionEnBaseDeDatos(Presentacion presentacion, String campoBaseDatos, String nuevoValorString) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            stmt = baseDatos.prepareStatement("update presentacion set " + campoBaseDatos + " = ? where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, nuevoValorString);
            stmt.setString(2, presentacion.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Agrega el documento a la presentacion
     *
     * @param presentacion Presentacion a la que se desea agregar el documento
     * @param documento    Documento a agregar a la presentacion
     */
    private void agregarDocPresEnBaseDeDatos(Presentacion presentacion, String documento) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            stmt = baseDatos.prepareStatement("insert into docmnt_prsntcion values (?, ?)");
            stmt.setString(1, documento);
            stmt.setString(2, presentacion.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Elimina el documento de la presentacion
     *
     * @param presentacion Presentacion de la que se desea eliminar el documento
     * @param documento    Documento a eliminar de la presentacion
     */
    private void eliminarDocPresEnBaseDeDatos(Presentacion presentacion, String documento) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            stmt = baseDatos.prepareStatement("delete from docmnt_prsntcion where presentacion = ? and nombre = ?");
            stmt.setString(1, presentacion.getId());
            stmt.setString(2, documento);
            stmt.executeUpdate();
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Crea y muestra el formulario para pedir los datos de una nueva presentacion
     */
    public void mostrarFormularioCrear() {
        formularioCrearPresentacion = new FormularioCrearPresentacion(this,
                ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoriasAbiertasLinkedList());
        formularioCrearPresentacion.ventana.setVisible(true);
    }

    /**
     * Crea, configura y muestra el formulario para pedir los datos a modificar de una presentacion
     *
     * @param presentacionAModificar Presentacion que se desea modificar en el formulario
     */
    public void mostrarFormularioModificar(Presentacion presentacionAModificar) {
        formularioModificarPresentacion = new FormularioModificarPresentacion(this, presentacionAModificar);
        // Si no tiene permisos para agregar documentos deshabilita los campos
        if (!(usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[1]) ||
                (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                        presentacionAModificar.isAbierto()))) {
            formularioModificarPresentacion.documentosRequeridosCampo.setEnabled(false);
            formularioModificarPresentacion.documentosAdicionalesCampo.setEnabled(false);
        }
        // Si no tiene permisos para entregar o no entregar la presentacion deshabilita el campo
        if (!(usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4]) ||
                (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                        presentacionAModificar.isAbierto())))
            formularioModificarPresentacion.campoEstadoEntrega.setEnabled(false);
        formularioModificarPresentacion.ventana.setVisible(true);
    }

    /**
     * Metodo que lee las interacciones de los usuarios sobre las vistas gestionadas por este controlador
     *
     * @param event Evento generado por la interaccion del usuario
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String accion = event.getActionCommand();
        switch (accion) {
            // Si el usuario desea crear una nueva presentacion
            case (StringsFinales.CREAR) -> {
                try {
                    // Toma los datos
                    String identificador = formularioCrearPresentacion.idCampo.getText();
                    String convocatoriaId = String.valueOf(formularioCrearPresentacion.convocatoriaCampo.getSelectedItem());
                    LinkedList<String> documentos = new LinkedList<>(formularioCrearPresentacion.documentosRequeridosCampo.getSelectedValuesList());
                    // Intenta crear la presentacion
                    crearPresentacion(identificador,
                            ConvocatoriasControlador.leerConvocatoriasBaseDeDatos().getConvocatoria(convocatoriaId),
                            documentos);
                    // Cerrar formulario
                    formularioCrearPresentacion.ventana.dispose();
                } catch (IllegalArgumentException e) {
                    // Si ocurrio algun error muestralo por pantalla
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                    System.out.println(e.getMessage());
                }
            }

            // Si el usuario desea modificar presentacion existente
            case (StringsFinales.MODIFICAR) -> {
                // Toma la presentacion a modificar
                Presentacion presentacionAModificar = formularioModificarPresentacion.presentacionAModificar;
                // Toma los valores a modificar del formulario
                HashSet<String> documentosReqNuevos = new HashSet<>(formularioModificarPresentacion.documentosRequeridosCampo.getSelectedValuesList());
                HashSet<String> docsAdNuevos = new HashSet<>(formularioModificarPresentacion.documentosAdicionalesCampo.getDatos());
                boolean nuevoAbierto = !formularioModificarPresentacion.campoEstadoEntrega.isSelected();

                // Verifica si se desea modificar los documentos requeridos
                for (String documento : presentacionAModificar.getDocumentos().getDocumentosLinkedList())
                    if (!documentosReqNuevos.contains(documento) &&
                            presentacionAModificar.getConvocatoria().getDocumentos().containsDocumento(documento))
                        retirarDocumentoDe(presentacionAModificar, documento);
                for (String documento : documentosReqNuevos)
                    if (!presentacionAModificar.containsDocumento(documento))
                        entregarDocumentoA(presentacionAModificar, documento);
                // Verifica si se quiere agregar un documento adicional
                for (String documento : presentacionAModificar.getDocumentos().getDocumentosLinkedList())
                    if (!docsAdNuevos.contains(documento) &&
                            !presentacionAModificar.getConvocatoria().getDocumentos().containsDocumento(documento))
                        retirarDocumentoDe(presentacionAModificar, documento);
                for (String documento : docsAdNuevos)
                    if (!presentacionAModificar.containsDocumento(documento))
                        entregarDocumentoA(presentacionAModificar, documento);
                // Verifica si se desea cambiar el estado
                if (nuevoAbierto != presentacionAModificar.isAbierto()) {
                    if (nuevoAbierto) retirarPresentacion(presentacionAModificar);
                    else entregarPresentacion(presentacionAModificar);
                }

                // Cierra el formulario
                formularioModificarPresentacion.ventana.dispose();
            }
        }
    }
}
