package controlador.controladorObjetos;

import modelo.evento.Convocatoria;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import servicios.ConvocatoriasServicio;
import servicios.PresentacionesServicio;
import vista.StringsFinales;
import vista.errores.ErrorVistaGenerador;
import vista.formularios.creacion.FormularioCrearConvocatoria;
import vista.formularios.modificacion.FormularioModificarConvocatoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
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
    private Usuario usuarioLogueado;
    /**
     * Formulario de creacion de municipios que este controlador debe gestionar
     */
    private FormularioCrearConvocatoria formularioCrearConvocatoria;
    /**
     * Formulario de modificacion de municipios que este controlador debe gestionar
     */
    private FormularioModificarConvocatoria formularioModificarConvocatoria;
    /**
     * Servicio de convocatorias
     */
    private final ConvocatoriasServicio convocatoriasServicio;
    /**
     * Servicio de presentaciones
     */
    private final PresentacionesServicio presentacionesServicio;

    /**
     * Costructor del controlador
     * @param convocatoriasServicio Servicio de convocatorias
     * @param presentacionesServicio Servicio de presentaciones
     */
    public ConvocatoriasControlador(ConvocatoriasServicio convocatoriasServicio,
                                    PresentacionesServicio presentacionesServicio) {
        this.convocatoriasServicio = convocatoriasServicio;
        this.presentacionesServicio = presentacionesServicio;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Devuelve las convocatorias que deben ser visibles para el usuario logueado de todas las registradas
     *
     * @return Un linked list con todas las convocatorias que deben ser visibles por el usuario logueado
     */
    public LinkedList<Convocatoria> getConvocatoriasVisibles() {
        try {
            LinkedList<Convocatoria> convocatorias = convocatoriasServicio.leerTodo();
            // Todas las convocatorias
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[9])) {
                return convocatorias;
                // Solo las convocatorias abiertas
            } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[13])) {
                return convocatorias;
            } else {
                return new LinkedList<>();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
        try {
            // Crear convocatorias
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[0])) {
                Convocatoria nuevaConvocatoria = new Convocatoria(id, fechaInicio, fechaCierre, docsReq, descripcion);
                convocatoriasServicio.registrar(nuevaConvocatoria);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Elimina la convocatoria
     *
     * @param convocatoria   Convocatoria que se desea eliminar como objeto
     * @throws IllegalArgumentException Si la convocatoria ya no existe en el sistema
     */
    public void eliminarConvocatoria(Convocatoria convocatoria) throws IllegalArgumentException {
        try {
            // Eliminar cualquier convocatoria
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[6]) ||
                    // Eliminar solo las convocatorias que no tienen presentaciones
                    (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[7]) &&
                            convocatoria.getSusPresentacionesDe(presentacionesServicio.leerTodo()).isEmpty())) {
                convocatoriasServicio.eliminar(convocatoria);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Agrega un nuevo documento requerido a la convocatoria
     *
     * @param convocatoria Convocatoria a la cual se desea agregar el documento
     * @param documento    Documento a establecer como requerido: debe estar en la lista de opciones
     */
    public void requerirDocumentoEn(Convocatoria convocatoria, String documento) {
        try {
            // Modificar cualquier convocatoria
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1]) &&
                    convocatoria.isAbierto()) {
                convocatoria.addDocumento(documento);
                convocatoriasServicio.agregarDocumento(convocatoria, documento);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Quita el documento requerido de la convocatoria
     *
     * @param convocatoria Convocatoria de la cual se desea eliminar el documento
     * @param documento    Documento a establecer como no requerido: debe estar en la lista de opciones
     */
    public void noRequerirDocumentoEn(Convocatoria convocatoria, String documento) {
        try {
            // Modificar cualquier convocatoria
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1]) &&
                    convocatoria.isAbierto()) {
                convocatoria.removeDocumento(documento);
                convocatoriasServicio.removerDocumento(convocatoria, documento);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
        try {
            // Modificar cualquier convocatoria
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1])) {
                // Verifica fecha de cierre valida
                convocatoria.setFechaCierre(fechaCierre);
                // Actualiza base de datos
                convocatoriasServicio.actualizar(convocatoria, DB_CAMPOS[2], Date.valueOf(fechaCierre).toString());
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
        try {
            // Modificar cualquier convocatoria
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1])) {
                // Verifica fecha de apertura valida
                convocatoria.setFechaInicio(fechaApertura);
                // Actualiza base de datos
                convocatoriasServicio.actualizar(convocatoria, DB_CAMPOS[1], Date.valueOf(fechaApertura).toString());
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
        try {
            // Modificar cualquier convocatoria
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[2], RolUsuario.ACCIONES[1])) {
                // Verifica que la descripcion sea valida
                convocatoria.setDescripcion(descripcion);
                // Actualiza la base de datos
                convocatoriasServicio.actualizar(convocatoria, DB_CAMPOS[3], descripcion);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
                    ErrorVistaGenerador.mostrarErrorEnOperacionCustomizado(StringsFinales.ERROR_FECHA_FORMATO_INCORRECTO);
                } catch (IllegalArgumentException e) {
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
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
                    ErrorVistaGenerador.mostrarErrorEnOperacionCustomizado(StringsFinales.ERROR_FECHA_FORMATO_INCORRECTO);
                } catch (IllegalArgumentException e) {
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
                }
            }
        }
    }
}
