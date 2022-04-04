package controlador.controladorobjetos;

import modelo.municipio.Municipio;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import servicios.MunicipiosServicio;
import servicios.UsuariosServicio;
import vista.StringsFinales;
import vista.errores.ErrorVistaGenerador;
import vista.formularios.creacion.FormularioCrearMunicipio;
import vista.formularios.modificacion.FormularioModificarMunicipio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Clase contenedora de todos los metodos que gestionan municipios
 */
public class MunicipiosControlador implements ActionListener {

    /**
     * Nombres de los campos en la base de datos
     */
    private static final String[] DB_CAMPOS = new String[]{"identificador", "nombre", "categoria", "supervisor", "representante"};

    /**
     * Usuario logueado que esta utilizando el sistema
     */
    private Usuario usuarioLogueado;

    /**
     * Formulario de creacion de municipios que este controlador debe gestionar
     */
    private FormularioCrearMunicipio formularioCrearMunicipio;
    /**
     * Formulario de modificacion de municipios que este controlador debe gestionar
     */
    private FormularioModificarMunicipio formularioModificarMunicipio;
    /**
     * Servicio de municipios
     */
    private final MunicipiosServicio municipiosServicio;
    /**
     * Servicio de usuarios
     */
    private final UsuariosServicio usuariosServicio;


    /**
     * Costructor del controlador
     * @param municipiosServicio Servicio de municipios
     * @param usuariosServicio Servicio de usuarios
     */
    public MunicipiosControlador(MunicipiosServicio municipiosServicio,
                                 UsuariosServicio usuariosServicio) {
        this.municipiosServicio = municipiosServicio;
        this.usuariosServicio = usuariosServicio;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Devuelve los municipios que deben ser visibles para el usuario logueado
     *
     * @return Un linked list con todos los municipios que deben ser visibles por el usuario logueado
     */
    public LinkedList<Municipio> getMunicipiosVisibles() {
        try {
            LinkedList<Municipio> municipios = municipiosServicio.leerTodo();
            // Consultar cualquier municipio
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[9])) {
                return municipios;
                // Consultar solo el municipio asignado
            } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[14])) {
                LinkedList<Municipio> municipiosVisibles = new LinkedList<>();
                Municipio municipioRepresentado = usuarioLogueado.getMunicipioRepresentadoDe(municipios);
                if (municipioRepresentado != null) municipiosVisibles.add(municipioRepresentado);
                return municipiosVisibles;
            } else {
                return new LinkedList<>();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
            return new LinkedList<>();
        }
    }

    /**
     * Crea un municipio
     * @param id         Identificador alfanumerico unico de municipio: Puede tener desde 1 caracter hasta 30 caracteres
     * @param nombre     Nombre del municipio a crear
     * @param categoria  Categoria del municipio
     * @throws IllegalArgumentException Si alguno de los parametros es invalido o el municipio ya existe
     */
    public void crearMunicipio(String id, String nombre, int categoria) throws IllegalArgumentException {
        try {
            // Crear municipios
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[0])) {
                Municipio nuevoMunicipio = new Municipio(id, nombre, categoria);
                municipiosServicio.registrar(nuevoMunicipio);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Elimina el municipio
     *
     * @param municipio      Municipio que se desea eliminar como objeto
     * @throws IllegalArgumentException Si el municipio ya no existe en el sistema
     */
    public void eliminarMunicipio(Municipio municipio) {
        try {
            // Eliminar cualquier municipio
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[6])) {
                municipiosServicio.eliminar(municipio);
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Asigna el cuentadante pasado como representante del municipio en las presentaciones realizadas para el
     *
     * @param municipio     Municipio a actualizar
     * @param representante Cuentadante a asignar
     */
    public void asignarRepresentanteAMunicipio(Municipio municipio, Usuario representante) {
        // Modificar Representante de cualquier municipio y usuario a asignar tiene permiso para representar (i.e. Cuentadantes)
        try {
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2]) &&
                    representante.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[16])) {
                // Toma el municipio antiguo del cuentadante y actualizalo
                Municipio municipioAntiguo = municipio.tomaNuevoRepresentante(representante, municipiosServicio.leerTodo());
                if (municipioAntiguo != null) municipiosServicio.actualizar(municipioAntiguo, DB_CAMPOS[4], "NULL");
                municipiosServicio.actualizar(municipio, DB_CAMPOS[4], representante.getId());
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Asigna el municipio al fiscal
     *
     * @param municipio Municipio que se desee asignar
     * @param supervisor    Fiscal que se desee asignar
     */
    public void asignarSupervisorAMunicipio(Municipio municipio, Usuario supervisor) {
        try {
            // Modificar Supervisor de cualquier municipio y usuario a asignar tiene permiso para supervisar (i.e. Fiscales)
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3]) &&
                    supervisor.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[15])) {
                municipio.tomaNuevoSupervisorFiscal(supervisor);
                // Actualiza base de datos
                municipiosServicio.actualizar(municipio, DB_CAMPOS[3], supervisor.getId());
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Asigna nueva categoria al municipio especificado
     *
     * @param municipio Municipio al que se desea asignar la categoria
     * @param categoria Nueva categoria a asignar
     */
    public void asignarCategoria(Municipio municipio, int categoria) {
        try {
            // Modificar cualquier municipio
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[1])) {
                municipio.setCategoria(categoria);
                // Actualiza base de datos
                municipiosServicio.actualizar(municipio, DB_CAMPOS[2], String.valueOf(categoria));
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Remueve el cuentadante actual del municipio
     *
     * @param municipio Municipio a actualizar
     */
    public void removeRepresentanteDeMunicipio(Municipio municipio) {
        try {
            // Modificar representante de cualquier municipio
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2])) {
                municipio.abandonaRepresentante();
                // Actualiza base de datos
                municipiosServicio.actualizar(municipio, DB_CAMPOS[4], "NULL");
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Remueve el fiscal actual del municipio
     *
     * @param municipio Municipio a actualizar
     */
    public void removeSupervisorDeMunicipio(Municipio municipio) {
        try {
            // Modificar supervisor de cualquier municipio
            if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3])) {
                municipio.abandonaSupervisor();
                // Actualiza base de datos
                municipiosServicio.actualizar(municipio, DB_CAMPOS[3], "NULL");
            } else {
                ErrorVistaGenerador.mostrarErrorNoPermisos();
            }
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Crea y muestra el formulario para pedir los datos de un nuevo municipio
     */
    public void mostrarFormularioCrear(){
        formularioCrearMunicipio = new FormularioCrearMunicipio(this);
        formularioCrearMunicipio.ventana.setVisible(true);
    }

    /**
     * Crea y muestra el formulario para modificar los datos de un municipio existente
     * @param municipioAModificar Municipio que se desea modificar en el formulario
     */
    public void mostrarFormularioModificar(Municipio municipioAModificar){
        try {
            LinkedList<Usuario> representantes = new LinkedList<>();
            LinkedList<Usuario> supervisores = new LinkedList<>();
            usuariosServicio.leerTodo().forEach(usuario -> {
                if (usuario.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[16]))
                    representantes.add(usuario);
                else if (usuario.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[15]))
                    supervisores.add(usuario);
            });
            formularioModificarMunicipio =
                    new FormularioModificarMunicipio(this, municipioAModificar, representantes, supervisores);
            // Si no puede modificar categoria deshabilita el boton
            if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[1]))
                formularioModificarMunicipio.categoriaCampo.setEnabled(false);
            // Si no puede modificar representante deshabilita el boton
            if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2]))
                formularioModificarMunicipio.representanteCampo.setEnabled(false);
            // Si no puede modificar supervisor deshabilita el boton
            if (!usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3]))
                formularioModificarMunicipio.supervisorCampo.setEnabled(false);
            formularioModificarMunicipio.ventana.setVisible(true);
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }


    /**
     * Metodo que lee las interacciones de los usuarios sobre las vistas gestionadas por este controlador
     * @param event Evento generado por la interaccion del usuario
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String accion = event.getActionCommand();
        switch (accion) {
            // En caso que se desee crear un municipio
            case (StringsFinales.CREAR) -> {
                try {
                    // Toma los datos
                    String identificador = formularioCrearMunicipio.idCampo.getText();
                    String nombre = formularioCrearMunicipio.nombreCampo.getText();
                    int categoria = (Integer) formularioCrearMunicipio.categoriaCampo.getSelectedItem();
                    // Intenta crear el municipio
                    crearMunicipio(identificador, nombre, categoria);
                    // Cierra la ventana
                    formularioCrearMunicipio.ventana.dispose();
                } catch (IllegalArgumentException e) {
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
                }
            }

            // En caso que se desee modificar un municipio
            case (StringsFinales.MODIFICAR) -> {
                try {
                    // Toma el municipio a modificar
                    Municipio municipioAModificar = formularioModificarMunicipio.municipioAModificar;
                    // Toma los valores a modificar del formulario
                    int nuevaCategoria = (Integer) formularioModificarMunicipio.categoriaCampo.getSelectedItem();
                    String nuevoSupervisorId = String.valueOf(formularioModificarMunicipio.supervisorCampo.getSelectedItem());
                    Usuario nuevoSupervisor = null;
                    if (!nuevoSupervisorId.equals(StringsFinales.NINGUNO))
                        nuevoSupervisor = usuariosServicio.leerPorID(nuevoSupervisorId);
                    String nuevoRepresentanteId = String.valueOf(formularioModificarMunicipio.representanteCampo.getSelectedItem());
                    Usuario nuevoRepresentante = null;
                    if (!nuevoRepresentanteId.equals(StringsFinales.NINGUNO))
                        nuevoRepresentante = usuariosServicio.leerPorID(nuevoRepresentanteId);

                    // Verifica si se desea modificar categoria
                    if (nuevaCategoria != municipioAModificar.getCategoria())
                        asignarCategoria(municipioAModificar, nuevaCategoria);
                    // Verifica si se desea modificar supervisor
                    if (nuevoSupervisor != null && !municipioAModificar.isFiscal(nuevoSupervisor))
                        asignarSupervisorAMunicipio(municipioAModificar, nuevoSupervisor);
                    else if (nuevoSupervisor == null && municipioAModificar.getFiscal() != null)
                        removeSupervisorDeMunicipio(municipioAModificar);
                    // Verifica si se desea modificar representante
                    if (nuevoRepresentante != null && !municipioAModificar.isCuentadante(nuevoRepresentante))
                        asignarRepresentanteAMunicipio(municipioAModificar, nuevoRepresentante);
                    else if (nuevoRepresentante == null && municipioAModificar.getCuentadante() != null)
                        removeRepresentanteDeMunicipio(municipioAModificar);

                    // Cierra la ventana
                    formularioModificarMunicipio.ventana.dispose();
                } catch (Exception e) {
                    ErrorVistaGenerador.mostrarErrorEnOperacion(e);
                }
            }
        }
    }
}
