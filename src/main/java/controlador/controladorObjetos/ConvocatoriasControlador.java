package controlador.controladorObjetos;

import modelo.dataAccess.Singleton;
import modelo.evento.convocatoria.ColeccionConvocatorias;
import modelo.evento.convocatoria.Convocatoria;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.formularios.FormularioCrearConvocatoria;
import vista.formularios.FormularioModificarConvocatoria;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Clase contenedora de todos los metodos que gestionan convocatorias
 */
public class ConvocatoriasControlador implements ActionListener {

    /**
     * Nombres de los campos en la base de datos
     */
    private static final String[] DB_CAMPOS = new String[]{"identificador", "fecha_apertura", "fecha_cierre", "descripcion"};
    /**
     * Usuario logueado que esta utilizando el sistema
     */
    private final Usuario usuarioLogueado;
    /**
     * Formulario de creacion de municipios que este controlador debe gestionar
     */
    private FormularioCrearConvocatoria formularioCrearConvocatoria;
    /**
     * Formulario de modificacion de municipios que este controlador debe gestionar
     */
    private FormularioModificarConvocatoria formularioModificarConvocatoria;

    /**
     * Costructor del controlador
     * @param usuarioLogueado Usuario autenticado en el sistema que utilizara el controlador
     */
    public ConvocatoriasControlador(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }


    /**
     * Devuelve las convocatorias que deben ser visibles para el usuario logueado de todas las registradas
     *
     * @return Un linked list con todas las convocatorias que deben ser visibles por el usuario logueado
     */
    public LinkedList<Convocatoria> getConvocatoriasVisibles() {
        ColeccionConvocatorias convocatorias = leerConvocatoriasBaseDeDatos();
        // Todas las convocatorias
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[9])) {
            return convocatorias.getConvocatoriasLinkedList();
        // Solo las convocatorias abiertas
        } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[13])) {
            return convocatorias.getConvocatoriasAbiertasLinkedList();
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * Crea una convocatoria
     *
     * @param id            Identificador alfanumerico unico de convocatoria: Puede tener desde 1 caracter hasta 100 caracteres
     * @param fechaInicio   Fecha de apertura planeada como objeto LocalDate
     * @param fechaCierre   Fecha de cierre de la convocatoria como objeto LocalDate: No puede ser antes de fechaInicio
     * @param docsReq       Documentos requeridos de la convocatoria para las presentaciones: Solo pueden ser los establecidos
     *                      en Convocatoria.DOCUMENTOS_OPCIONES
     * @param descripcion   Descripcion de la convocatoria: Debe tener como maximo 2000 caracteres
     * @throws IllegalArgumentException Si alguno de los parametros es invalido o la convocatoria ya existe en el sistema
     */
    public void crearConvocatoria(String id, LocalDate fechaInicio, LocalDate fechaCierre, LinkedList<String> docsReq,
                                  String descripcion) throws IllegalArgumentException {
        // Crear convocatorias
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[0])) {
            Convocatoria nuevaConvocatoria = new Convocatoria(id, fechaInicio, fechaCierre, docsReq, descripcion);
            // Verifica si ya esta agregado
            leerConvocatoriasBaseDeDatos().addConvocatoria(nuevaConvocatoria);
            // Actualiza base de datos
            agregarConvocatoriaABaseDeDatos(nuevaConvocatoria);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Elimina la convocatoria
     *
     * @param convocatoria   Convocatoria que se desea eliminar como objeto
     * @throws IllegalArgumentException Si la convocatoria ya no existe en el sistema
     */
    public void eliminarConvocatoria(Convocatoria convocatoria) throws IllegalArgumentException {
        // Eliminar cualquier convocatoria
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2],  RolUsuario.ACCIONES[6]) ||
                // Eliminar solo las convocatorias que no tienen presentaciones
                (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2],  RolUsuario.ACCIONES[7]) &&
                        convocatoria.getSusPresentacionesDe(PresentacionesControlador.leerPresentacionesBaseDeDatos()).isEmpty())) {
            // Verifica que todavia existe
            leerConvocatoriasBaseDeDatos().removeConvocatoria(convocatoria, PresentacionesControlador.leerPresentacionesBaseDeDatos());
            // Actualiza base de datos
            eliminarConvocatoriaDeBaseDeDatos(convocatoria);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Agrega un nuevo documento requerido a la convocatoria
     *
     * @param convocatoria Convocatoria a la cual se desea agregar el documento
     * @param documento    Documento a establecer como requerido: debe estar en la lista de opciones
     */
    public void requerirDocumentoEn(Convocatoria convocatoria, String documento) {
        // Modificar cualquier convocatoria
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2],  RolUsuario.ACCIONES[1]) &&
                convocatoria.isAbierto()) {
            try {
                convocatoria.addDocumento(documento);
                // Actualiza base de datos
                agregarDocConvoEnBaseDeDatos(convocatoria, documento);
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
     * Quita el documento requerido de la convocatoria
     *
     * @param convocatoria Convocatoria de la cual se desea eliminar el documento
     * @param documento    Documento a establecer como no requerido: debe estar en la lista de opciones
     */
    public void noRequerirDocumentoEn(Convocatoria convocatoria, String documento) {
        // Modificar cualquier convocatoria
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1]) &&
                convocatoria.isAbierto()) {
            try {
                convocatoria.removeDocumento(documento);
                // Actualiza base de datos
                eliminarDocConvoEnBaseDeDatos(convocatoria, documento);
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
     * Establece la fecha pasada como parametro como fecha de cierre en la convocatoria
     *
     * @param convocatoria Convocatoria en la cual se desea establecer fecha de cierre
     * @param fechaCierre  Fecha de cierre como Objeto Local date a establecer
     * @throws IllegalArgumentException Si la fecha es invalida
     */
    public void asignaFechaCierreDe(Convocatoria convocatoria, LocalDate fechaCierre) throws IllegalArgumentException {
        // Modificar cualquier convocatoria
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1])) {
            // Verifica fecha de cierre valida
            convocatoria.setFechaCierre(fechaCierre);
            // Actualiza base de datos
            actualizarConvocatoriaEnBaseDeDatos(convocatoria, DB_CAMPOS[2], Date.valueOf(fechaCierre).toString());
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Establece la fecha pasada como parametro como fecha de apertura en la convocatoria
     *
     * @param convocatoria Convocatoria en la cual se desea establecer fecha de apertura
     * @param fechaApertura  Fecha de apertura como Objeto Local date a establecer
     * @throws IllegalArgumentException Si la fecha es invalida
     */
    public void asignaFechaAperturaDe(Convocatoria convocatoria, LocalDate fechaApertura) throws IllegalArgumentException {
        // Modificar cualquier convocatoria
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1])) {
            // Verifica fecha de apertura valida
            convocatoria.setFechaInicio(fechaApertura);
            // Actualiza base de datos
            actualizarConvocatoriaEnBaseDeDatos(convocatoria, DB_CAMPOS[1], Date.valueOf(fechaApertura).toString());
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Asigna la descripcion a la convocatoria pasada
     *
     * @param convocatoria Convocatoria en la cual se desea establecer una nueva descripcion
     * @param descripcion  Descripcion a establecer como String
     * @throws IllegalArgumentException Si la descripcion es invalida
     */
    public void asignaDescripcionDe(Convocatoria convocatoria, String descripcion) throws IllegalArgumentException {
        // Modificar cualquier convocatoria
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1])) {
            // Verifica que la descripcion sea valida
            convocatoria.setDescripcion(descripcion);
            // Actualiza la base de datos
            actualizarConvocatoriaEnBaseDeDatos(convocatoria, DB_CAMPOS[3], descripcion);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Lee las presentaciones desde la base de datos y las asigna a la coleccion de presentaciones del sistema
     *
     * @return Una coleccion con todas las convocatorias de la base de datos
     */
    public static ColeccionConvocatorias leerConvocatoriasBaseDeDatos() {
        ColeccionConvocatorias convocatorias = new ColeccionConvocatorias();
        try {
            LinkedList<String> documentos;
            Convocatoria convocActual;
            Connection baseDatos = Singleton.getConnection();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Statement stmt = baseDatos.createStatement();
            ResultSet rs = stmt.executeQuery("select * from convocatoria");
            while (rs.next()) {
                documentos = leerDocumentosConvocatoriasBaseDatos(rs.getString(1));
                convocActual = new Convocatoria(rs.getString(1),
                        LocalDate.parse(formatter.format(rs.getTimestamp(2))),
                        LocalDate.parse(formatter.format(rs.getTimestamp(3))),
                        documentos,
                        rs.getString(4));
                convocatorias.addConvocatoria(convocActual);
            }
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
        return convocatorias;
    }


    /**
     * Lee los documentos de la convocatoria pasada desde la base de datos
     *
     * @param idConvocatoria Convocatoria de la que se desean obtener los documentos
     * @return LinkedList con todos los documentos de la presentacion
     * @throws SQLException Si ocurre algun error en la letura de los documentos
     */
    private static LinkedList<String> leerDocumentosConvocatoriasBaseDatos(String idConvocatoria) throws SQLException {
        ResultSet documentosRs;
        LinkedList<String> documentos = new LinkedList<>();
        PreparedStatement tablaDocumentos =
                Singleton.getConnection().prepareStatement("select * from docmnt_cnvctria where convocatoria = ?");
        tablaDocumentos.setString(1, idConvocatoria);
        documentosRs = tablaDocumentos.executeQuery();
        while (documentosRs.next()) {
            documentos.add(documentosRs.getString(1));
        }
        return documentos;
    }

    /**
     * Agrega la convocatoria a la base de datos
     *
     * @param convocatoria Convocatoria a agregar
     */
    private void agregarConvocatoriaABaseDeDatos(Convocatoria convocatoria) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("insert into convocatoria values (?, ?, ?, ?)");
            stmt.setString(1, convocatoria.getId());
            stmt.setDate(2, Date.valueOf(convocatoria.getFechaInicio()));
            stmt.setDate(3, Date.valueOf(convocatoria.getFechaCierre()));
            stmt.setString(4, convocatoria.getDescripcion());
            stmt.executeUpdate();
            for (String documento : convocatoria.getDocumentos().getDocumentosLinkedList())
                agregarDocConvoEnBaseDeDatos(convocatoria, documento);
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Elimina la convocatoria de la base de datos
     *
     * @param convocatoria Convocatoria a eliminar
     */
    private void eliminarConvocatoriaDeBaseDeDatos(Convocatoria convocatoria) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("delete from convocatoria where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, convocatoria.getId());
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
     * Actualiza la convocatoria en la base de datos en el campo especificado
     *
     * @param convocatoria Convocatoria a actualizar
     * @param campoBaseDatos Campo a actualizar
     * @param nuevoValorString Valor a poner en el campo como string
     */
    private void actualizarConvocatoriaEnBaseDeDatos(Convocatoria convocatoria, String campoBaseDatos, String nuevoValorString) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            stmt = baseDatos.prepareStatement("update convocatoria set " + campoBaseDatos + " = ? where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, nuevoValorString);
            stmt.setString(2, convocatoria.getId());
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
     * Agrega el documento a la convocatoria
     *
     * @param convocatoria Convocatoria a la que se desea agregar el documento
     * @param documento Documento a agregar a la convocatoria
     */
    private void agregarDocConvoEnBaseDeDatos(Convocatoria convocatoria, String documento) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            stmt = baseDatos.prepareStatement("insert into docmnt_cnvctria values (?, ?)");
            stmt.setString(1, documento);
            stmt.setString(2, convocatoria.getId());
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
     * Elimina el documento de la convocatoria
     *
     * @param convocatoria Convocatoria de la que se desea eliminar el documento
     * @param documento Documento a eliminar de la convocatoria
     */
    private void eliminarDocConvoEnBaseDeDatos(Convocatoria convocatoria, String documento) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            stmt = baseDatos.prepareStatement("delete from docmnt_cnvctria where convocatoria = ? and nombre = ?");
            stmt.setString(1, convocatoria.getId());
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
     * Crea y muestra el formulario para pedir los datos de una nueva convocatoria
     */
    public void mostrarFormularioCrear(){
        formularioCrearConvocatoria = new FormularioCrearConvocatoria(this);
        formularioCrearConvocatoria.ventana.setVisible(true);
    }

    /**
     * Crea y muestra el formulario para modificar los datos de una convocatoria existente
     *
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     */
    public void mostrarFormularioModificar(Convocatoria convocatoriaAModificar){
        this.formularioModificarConvocatoria = new FormularioModificarConvocatoria(this, convocatoriaAModificar);
        formularioModificarConvocatoria.ventana.setVisible(true);
    }

    /**
     * Metodo que lee las interacciones de los usuarios sobre las vistas gestionadas por este controlador
     * @param event Evento generado por la interaccion del usuario
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String accion = event.getActionCommand();
        switch (accion) {
            // Si el usuario desea crear una convocatoria
            case (StringsFinales.CREAR) -> {
                try {
                    // Toma los datos
                    String identificador = formularioCrearConvocatoria.idCampo.getText();
                    LocalDate fechaApertura = LocalDate.parse(formularioCrearConvocatoria.fechaAperturaCampo.getText());
                    LocalDate fechaCierre = LocalDate.parse(formularioCrearConvocatoria.fechaCierreCampo.getText());
                    LinkedList<String> documentos = new LinkedList<>(formularioCrearConvocatoria.documentosCampo.getSelectedValuesList());
                    String descripcion = formularioCrearConvocatoria.descripcionArea.getText();
                    // Intenta crear la convocatoria
                    crearConvocatoria(identificador, fechaApertura, fechaCierre, documentos, descripcion);
                    // Cierra la ventana
                    formularioCrearConvocatoria.ventana.dispose();
                } catch (DateTimeParseException e) {
                    // Si la fecha no esta en el formato requerido
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + StringsFinales.ERROR_FECHA_FORMATO_INCORRECTO,
                            "", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    // Si ocurrio algun error muestralo por pantalla
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                    System.out.println(e.getMessage());
                }
            }
            // Si el usuario desea modificar una convocatoria
            case (StringsFinales.MODIFICAR) -> {
                try {
                    // Toma la convocatoria a modificar
                    Convocatoria convocatoriaAModificar = formularioModificarConvocatoria.convocatoriaAModificar;
                    // Toma los valores a modificar del formulario
                    LocalDate nuevaFechaApertura = LocalDate.parse(formularioModificarConvocatoria.fechaAperturaCampo.getText());
                    LocalDate nuevaFechaCierre = LocalDate.parse(formularioModificarConvocatoria.fechaCierreCampo.getText());
                    String nuevaDescripcion = formularioModificarConvocatoria.descripcionArea.getText();
                    HashSet<String> documentosNuevos = new HashSet<>(formularioModificarConvocatoria.documentosCampo.getSelectedValuesList());

                    // Verifica si se desea modificar fecha apertura
                    if (!nuevaFechaApertura.isEqual(convocatoriaAModificar.getFechaInicio()))
                        asignaFechaAperturaDe(convocatoriaAModificar, nuevaFechaApertura);
                    // Verifica si se desea modificar fecha cierre
                    if (!nuevaFechaCierre.isEqual(convocatoriaAModificar.getFechaCierre()))
                        asignaFechaCierreDe(convocatoriaAModificar, nuevaFechaCierre);
                    // Verifica si se desea modificar la descripcion
                    if (!nuevaDescripcion.equals(convocatoriaAModificar.getDescripcion()))
                        asignaDescripcionDe(convocatoriaAModificar, nuevaDescripcion);
                    // Verifica si se desea modificar los documentos
                    for (String documento : convocatoriaAModificar.getDocumentos().getDocumentosLinkedList())
                        if (!documentosNuevos.contains(documento))
                            noRequerirDocumentoEn(convocatoriaAModificar, documento);
                    for (String documento : documentosNuevos)
                        if (!convocatoriaAModificar.containsDocumento(documento))
                            requerirDocumentoEn(convocatoriaAModificar, documento);

                    // Cierra la ventana
                    formularioModificarConvocatoria.ventana.dispose();
                } catch (DateTimeParseException e) {
                    // Si la fecha no esta en el formato requerido
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + StringsFinales.ERROR_FECHA_FORMATO_INCORRECTO,
                            "", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    // Si ocurrio algun error muestralo por pantalla
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
