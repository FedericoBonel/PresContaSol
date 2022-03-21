package controlador.controladorObjetos;

import modelo.dataAccess.Singleton;
import modelo.municipio.ColeccionMunicipios;
import modelo.municipio.Municipio;
import modelo.usuario.RolUsuario;
import modelo.usuario.Usuario;
import vista.StringsFinales;
import vista.formularios.FormularioCrearMunicipio;
import vista.formularios.FormularioModificarMunicipio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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
    private final Usuario usuarioLogueado;

    /**
     * Formulario de creacion de municipios que este controlador debe gestionar
     */
    private FormularioCrearMunicipio formularioCrearMunicipio;
    /**
     * Formulario de modificacion de municipios que este controlador debe gestionar
     */
    private FormularioModificarMunicipio formularioModificarMunicipio;


    /**
     * Costructor del controlador
     * @param usuarioLogueado Usuario autenticado en el sistema que utilizara el controlador
     */
    public MunicipiosControlador(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Devuelve los municipios que deben ser visibles para el usuario logueado
     *
     * @return Un linked list con todos los municipios que deben ser visibles por el usuario logueado
     */
    public LinkedList<Municipio> getMunicipiosVisibles() {
        ColeccionMunicipios municipios = leerMunicipiosBaseDeDatos();
        // Consultar cualquier municipio
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[9])) {
            return municipios.getMunicipiosLinkedList();
        // Consultar solo el municipio asignado
        } else if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[14])) {
            LinkedList<Municipio> municipiosVisibles = new LinkedList<>();
            Municipio municipioRepresentado = usuarioLogueado.getMunicipioRepresentadoDe(municipios);
            if (municipioRepresentado != null) municipiosVisibles.add(municipioRepresentado);
            return municipiosVisibles;
        } else {
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
        // Crear municipios
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[0])) {
            Municipio nuevoMunicipio = new Municipio(id, nombre, categoria);
            // Verifica si ya esta creado
            leerMunicipiosBaseDeDatos().addMunicipio(nuevoMunicipio);
            // Actualiza base de datos
            agregarMunicipioABaseDeDatos(nuevoMunicipio);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Elimina el municipio
     *
     * @param municipio      Municipio que se desea eliminar como objeto
     * @throws IllegalArgumentException Si el municipio ya no existe en el sistema
     */
    public void eliminarMunicipio(Municipio municipio) {
        // Eliminar cualquier municipio
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[6])) {
            // Verifica que todavia exista
            leerMunicipiosBaseDeDatos().removeMunicipio(municipio, PresentacionesControlador.leerPresentacionesBaseDeDatos());
            // Actualiza base de datos
            eliminarMunicipioDeBaseDeDatos(municipio);
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
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
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2]) &&
            representante.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[16])) {
            // Toma el municipio antiguo del cuentadante y actualizalo
            Municipio municipioAntiguo = municipio.tomaNuevoRepresentante(representante, leerMunicipiosBaseDeDatos());
            // Actualiza base de datos
            if (municipioAntiguo != null)
                // Remueve el supervisor del municipio antiguo en caso de tenerlo
                actualizarMunicipioEnBaseDeDatos(municipioAntiguo, DB_CAMPOS[4], "NULL");
            actualizarMunicipioEnBaseDeDatos(municipio, DB_CAMPOS[4], representante.getId());
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                StringsFinales.ERROR_NO_PERMISOS + " o " + StringsFinales.ERROR_USUARIO_NO_REPRESENTANTE, "", JOptionPane.ERROR_MESSAGE);
            System.out.print(StringsFinales.ERROR_NO_PERMISOS);
            System.out.print(" o ");
            System.out.println(StringsFinales.ERROR_USUARIO_NO_REPRESENTANTE);
        }
    }

    /**
     * Asigna el municipio al fiscal
     *
     * @param municipio Municipio que se desee asignar
     * @param supervisor    Fiscal que se desee asignar
     */
    public void asignarSupervisorAMunicipio(Municipio municipio, Usuario supervisor) {
        // Modificar Supervisor de cualquier municipio y usuario a asignar tiene permiso para supervisar (i.e. Fiscales)
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3]) &&
                supervisor.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[15])) {
            municipio.tomaNuevoSupervisorFiscal(supervisor);
            // Actualiza base de datos
            actualizarMunicipioEnBaseDeDatos(municipio, DB_CAMPOS[3], supervisor.getId());
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS + " o " + StringsFinales.ERROR_USUARIO_NO_SUPERVISOR, "", JOptionPane.ERROR_MESSAGE);
            System.out.print(StringsFinales.ERROR_NO_PERMISOS);
            System.out.print(" o ");
            System.out.println(StringsFinales.ERROR_USUARIO_NO_SUPERVISOR);
        }
    }

    /**
     * Asigna nueva categoria al municipio especificado
     *
     * @param municipio Municipio al que se desea asignar la categoria
     * @param categoria Nueva categoria a asignar
     */
    public void asignarCategoria(Municipio municipio, int categoria) {
        // Modificar cualquier municipio
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[1])) {
            municipio.setCategoria(categoria);
            // Actualiza base de datos
            actualizarMunicipioEnBaseDeDatos(municipio, DB_CAMPOS[2], String.valueOf(categoria));
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Remueve el cuentadante actual del municipio
     *
     * @param municipio Municipio a actualizar
     */
    public void removeRepresentanteDeMunicipio(Municipio municipio) {
        // Modificar representante de cualquier municipio
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[2])) {
            municipio.abandonaRepresentante();
            // Actualiza base de datos
            actualizarMunicipioEnBaseDeDatos(municipio, DB_CAMPOS[4], "NULL");
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Remueve el fiscal actual del municipio
     *
     * @param municipio Municipio a actualizar
     */
    public void removeSupervisorDeMunicipio(Municipio municipio) {
        // Modificar supervisor de cualquier municipio
        if (usuarioLogueado.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[3])) {
            municipio.abandonaSupervisor();
            // Actualiza base de datos
            actualizarMunicipioEnBaseDeDatos(municipio, DB_CAMPOS[3], "NULL");
        } else {
            JOptionPane.showMessageDialog(new JFrame(),
                    StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
            System.out.println(StringsFinales.ERROR_NO_PERMISOS);
        }
    }

    /**
     * Lee los municipios desde la base de datos y los retorna como una coleccion
     *
     * @return Todos los municipios de la base de datos
     */
    public static ColeccionMunicipios leerMunicipiosBaseDeDatos() {
        ColeccionMunicipios municipios = new ColeccionMunicipios();
        try {
            Municipio municipioActual;
            String fiscalId, cuentadanteId;
            Connection baseDatos = Singleton.getConnection();
            Statement stmt = baseDatos.createStatement();
            ResultSet rs = stmt.executeQuery("select * from municipio");
            while (rs.next()) {
                municipioActual = new Municipio(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3));
                fiscalId = rs.getString(4);
                cuentadanteId = rs.getString(5);
                if (fiscalId != null)
                    municipioActual.tomaNuevoSupervisorFiscal(UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(fiscalId));
                if (cuentadanteId != null)
                    municipioActual.tomaNuevoRepresentante(UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(cuentadanteId), municipios);
                municipios.addMunicipio(municipioActual);
            }
        } catch (Exception e) {
            // Si ocurrio algun error muestralo por pantalla
            JOptionPane.showMessageDialog(new JFrame(),
                    Singleton.ERROR_ACCESO_BASE_DATOS + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.out.print(Singleton.ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
        return municipios;
    }

    /**
     * Agrega el municipio a la base de datos
     *
     * @param municipio Municipio a agregar
     */
    private void agregarMunicipioABaseDeDatos(Municipio municipio) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("insert into municipio values (?, ?, ?, NULL, NULL)");
            stmt.setString(1, municipio.getId());
            stmt.setString(2, municipio.getNombre());
            stmt.setInt(3, municipio.getCategoria());
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
     * Elimina el municipio de la base de datos
     *
     * @param municipio municipio a eliminar
     */
    private void eliminarMunicipioDeBaseDeDatos(Municipio municipio) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt = baseDatos.prepareStatement("delete from municipio where " + DB_CAMPOS[0] + " = ?");
            stmt.setString(1, municipio.getId());
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
     * Actualiza el municipio en la base de datos en el campo especificado
     *
     * @param municipio Municipio a actualizar
     * @param campoBaseDatos Campo a actualizar
     * @param nuevoValorString Valor a poner en el campo como string
     */
    private void actualizarMunicipioEnBaseDeDatos(Municipio municipio, String campoBaseDatos, String nuevoValorString) {
        try {
            Connection baseDatos = Singleton.getConnection();
            PreparedStatement stmt;
            if (nuevoValorString.equals("NULL")) {
                stmt = baseDatos.prepareStatement("update municipio set " + campoBaseDatos + " = NULL where " + DB_CAMPOS[0] + " = ?");
                stmt.setString(1, municipio.getId());
            } else {
                stmt = baseDatos.prepareStatement("update municipio set " + campoBaseDatos + " = ? where " + DB_CAMPOS[0] + " = ?");
                stmt.setString(1, nuevoValorString);
                stmt.setString(2, municipio.getId());
            }
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
        formularioModificarMunicipio =
                new FormularioModificarMunicipio(this,
                        municipioAModificar,
                        UsuariosControlador.leerUsuariosBaseDeDatos().getRepresentantesLinkedList(),
                        UsuariosControlador.leerUsuariosBaseDeDatos().getSupervisoresLinkedList());
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
                    // Si ocurrio algun error muestralo por pantalla
                    JOptionPane.showMessageDialog(new JFrame(),
                            StringsFinales.ERROR_REALIZANDO_OPERACION + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                    System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
                    System.out.println(e.getMessage());
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
                        nuevoSupervisor = UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(nuevoSupervisorId);
                    String nuevoRepresentanteId = String.valueOf(formularioModificarMunicipio.representanteCampo.getSelectedItem());
                    Usuario nuevoRepresentante = null;
                    if (!nuevoRepresentanteId.equals(StringsFinales.NINGUNO))
                        nuevoRepresentante = UsuariosControlador.leerUsuariosBaseDeDatos().getUsuario(nuevoRepresentanteId);

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
