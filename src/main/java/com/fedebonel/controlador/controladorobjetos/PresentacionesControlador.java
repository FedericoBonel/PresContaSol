package com.fedebonel.controlador.controladorobjetos;

import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.formularios.creacion.FormularioCrearPresentacion;
import com.fedebonel.vista.formularios.modificacion.FormularioModificarPresentacion;
import com.fedebonel.servicios.ConvocatoriasServicio;
import com.fedebonel.servicios.MunicipiosServicio;
import com.fedebonel.servicios.PresentacionesServicio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Usuario usuarioLogueado;

    /**
     * Formulario de creacion de presentaciones que este controlador debe gestionar
     */
    private FormularioCrearPresentacion formularioCrearPresentacion;
    /**
     * Formulario de modificacion de presentaciones que este controlador debe gestionar
     */
    private FormularioModificarPresentacion formularioModificarPresentacion;
    /**
     * Servicio de presentaciones
     */
    private final PresentacionesServicio presentacionesServicio;
    /**
     * Servicio de convocatorias
     */
    private final ConvocatoriasServicio convocatoriasServicio;
    /**
     * Servicio de Municipios
     */
    private final MunicipiosServicio municipiosServicio;

    /**
     * Costructor del controlador
     * @param presentacionesServicio Servicio de presentaciones
     * @param convocatoriasServicio Servicio de convocatorias
     * @param municipiosServicio Servicio de municipios
     */
    public PresentacionesControlador(PresentacionesServicio presentacionesServicio,
                                     ConvocatoriasServicio convocatoriasServicio,
                                     MunicipiosServicio municipiosServicio) {
        this.presentacionesServicio = presentacionesServicio;
        this.convocatoriasServicio = convocatoriasServicio;
        this.municipiosServicio = municipiosServicio;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado){
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Devuelve las presentaciones que deben ser visibles para el usuario logueado
     *
     * @return Un linked list con todas las presentaciones que deben ser visibles por el usuario logueado
     */
    public LinkedList<Presentacion> getPresentacionesVisibles() {
        try {
            LinkedList<Presentacion> presentaciones = presentacionesServicio.leerTodo();
            // Rol Administrador y rol fiscal general
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[9])) {
                return presentaciones;
                // Rol Cuentadante
            } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[14])) {
                LinkedList<Presentacion> presentacionesRealizadas = new LinkedList<>();
                for (Presentacion presentacion : presentaciones) {
                    if (presentacion.isAutor(usuarioLogueado)) presentacionesRealizadas.add(presentacion);
                }
                return presentacionesRealizadas;
                // Rol Fiscal
            } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[12])) {
                LinkedList<Presentacion> presentacionesVisibles = new LinkedList<>();
                for (Presentacion presentacion : presentaciones) {
                    if (presentacion.getMunicipio().isFiscal(usuarioLogueado))
                        presentacionesVisibles.add(presentacion);
                }
                return presentacionesVisibles;
            } else {
                return new LinkedList<>();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
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
        try {
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[0]) &&
                    convocatoria.isAbierto()) {
                // Cuando el usuario crea una presentacion siempre se establece como abierta por defecto
                Presentacion nuevaPresentacion =
                        new Presentacion(id,
                                LocalDate.now(), true, convocatoria,
                                usuarioLogueado,
                                usuarioLogueado.getMunicipioRepresentadoDe(municipiosServicio.leerTodo()),
                                docsEntregados);
                presentacionesServicio.registrar(nuevaPresentacion);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Elimina la presentacion
     *
     * @param presentacion Presentacion que se desea eliminar
     * @throws IllegalArgumentException Si la presentacion ya no esta registrada en el sistema
     */
    public void eliminarPresentacion(Presentacion presentacion) {
        try {
            // Rol Administrador
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[6]) ||
                    // Rol Cuentadante
                    (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[8]) &&
                            presentacion.isAbierto() && presentacion.isAutor(usuarioLogueado))) {
                presentacionesServicio.eliminar(presentacion);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Retira la presentacion entregada (la abre)
     *
     * @param presentacion Presentacion que se desea abrir
     */
    public void retirarPresentacion(Presentacion presentacion) {
        try {
            // Rol Administrador y fiscal general
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4])) {
                presentacion.setAbierto(true);
                presentacionesServicio.actualizar(presentacion, DB_CAMPOS[2], String.valueOf(1));
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Cierra la presentacion especificada para entregarla de manera final al Fiscal
     *
     * @param presentacion Presentacion que se desea cerrar/entregar
     */
    public void entregarPresentacion(Presentacion presentacion) throws IllegalCallerException {
        try {
            // Rol Cuentadante
            if ((usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                    presentacion.isAutor(usuarioLogueado) && presentacion.getConvocatoria().isAbierto() &&
                    presentacion.todosDocsRequeridosEntregados()) ||
                    usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[4])) {
                presentacion.setAbierto(false);
                presentacionesServicio.actualizar(presentacion, DB_CAMPOS[2], String.valueOf(0));
            } else {
                ErrorVistaGenerador.mostrarErrorEnOperacionCustomizado(
                        StringsFinales.ERROR_NO_PERMISOS + ", " + StringsFinales.ERROR_CONVOCATORIA_CERRADA + " o "
                                + StringsFinales.ERROR_DOCUMENTOS_REQUERIDOS);
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Establece el documento pasado como entregado en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     */
    public void entregarDocumentoA(Presentacion presentacion, String documento) {
        try {
            // Rol Administrador
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[1]) ||
                    // Rol cuentadante
                    (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                            presentacion.isAutor(usuarioLogueado) && presentacion.isAbierto())) {
                    presentacion.addDocumento(documento);
                    presentacionesServicio.agregarDocumento(presentacion, documento);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Retira el documento de la presentacion (i.e. se marca como no entregado)
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     */
    public void retirarDocumentoDe(Presentacion presentacion, String documento) throws IllegalCallerException {
        try {
            // Rol Administrador
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[1]) ||
                    // Rol cuentadante
                    (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[3], RolUsuario.ACCIONES[5]) &&
                            presentacion.isAutor(usuarioLogueado) && presentacion.isAbierto())) {
                presentacion.removeDocumento(documento);
                presentacionesServicio.eliminarDocumento(presentacion, documento);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Crea y muestra el formulario para pedir los datos de una nueva presentacion
     */
    public void mostrarFormularioCrear() {
        try {
            LinkedList<Convocatoria> convocatoriasAbiertas = new LinkedList<>();
            convocatoriasServicio.leerTodo().forEach(convocatoria -> {
                if (convocatoria.isAbierto()) convocatoriasAbiertas.add(convocatoria);
            });
            formularioCrearPresentacion = new FormularioCrearPresentacion(this, convocatoriasAbiertas);
            formularioCrearPresentacion.ventana.setVisible(true);
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
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
                    crearPresentacion(identificador, convocatoriasServicio.leerPorID(convocatoriaId), documentos);
                    // Cerrar formulario
                    formularioCrearPresentacion.ventana.dispose();
                } catch (Exception e) {
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
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
