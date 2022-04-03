package modelo.usuario;

import java.util.LinkedList;

/**
 * Clase que abstrae a los roles de todos los usuarios
 */
public class RolUsuario {
    /**
     * Objetos sobre los que se puede realizar acciones
     */
    public static final String[] OBJETOS = new String[]{"Usuarios", "Municipios", "Convocatorias", "Presentaciones"};
    /**
     * Acciones que se pueden realizar sobre los objetos
     */
    public static final String[] ACCIONES =
            new String[]{
                    // Creacion
                    "Crear",
                    // Modificacion
                    "Modificar",
                        "ModificarRepresentante", "ModificarSupervisor", "ModificarApertura", "ModificarPropio",
                    // Eliminacion
                    "Eliminar",
                        "EliminarVacias", "EliminarPropio",
                    // Lectura
                    "Consultar",
                        "ConsultarRepresentante", "ConsultarSupervisor", "ConsultarAsignado", "ConsultarAbiertas", "ConsultarPropio",
                    // Supervision
                    "Supervisar",
                    // Representacion
                    "Representar"};
    /**
     * Rol administrador
     */
    public final static String ROL_ADMINISTRADOR_NOMBRE = "RolAdministrador";
    /**
     * Contenedor de todos los permisos de los administradores como matriz [objeto][accion]
     */
    public final static String[][] PERMISOS_ADMINISTRADOR = new String[][]{
            // Usuarios (Todas las acciones)
            {OBJETOS[0], ACCIONES[0]},
            {OBJETOS[0], ACCIONES[1]},
            {OBJETOS[0], ACCIONES[6]},
            {OBJETOS[0], ACCIONES[9]},
            // Municipios (Todas las acciones)
            {OBJETOS[1], ACCIONES[0]},
            {OBJETOS[1], ACCIONES[1]},
            {OBJETOS[1], ACCIONES[6]},
            {OBJETOS[1], ACCIONES[9]},
            // Convocatorias (Todas las acciones)
            {OBJETOS[2], ACCIONES[0]},
            {OBJETOS[2], ACCIONES[1]},
            {OBJETOS[2], ACCIONES[6]},
            {OBJETOS[2], ACCIONES[9]},
            // Presentaciones (No puede crear)
            {OBJETOS[3], ACCIONES[1]},
            {OBJETOS[3], ACCIONES[6]},
            {OBJETOS[3], ACCIONES[9]}};
    /**
     * Rol fiscal gral
     */
    public final static String ROL_FISCAL_GRAL_NOMBRE = "RolFiscalGral";
    /**
     * Contenedor de todos los permisos de los fiscales generales como matriz [objeto][accion]
     */
    public final static String[][] PERMISOS_FISCAL_GRAL = new String[][]{
            // Usuarios (Solo consultar supervisores [Fiscales] y representantes [Cuentadantes])
            {OBJETOS[0], ACCIONES[10]},
            {OBJETOS[0], ACCIONES[11]},
            // Municipios (Solo consultar todos y modificar representantes y supervisores)
            {OBJETOS[1], ACCIONES[2]},
            {OBJETOS[1], ACCIONES[3]},
            {OBJETOS[1], ACCIONES[9]},
            // Convocatorias (Todas las acciones pero solo eliminar las vacias)
            {OBJETOS[2], ACCIONES[0]},
            {OBJETOS[2], ACCIONES[1]},
            {OBJETOS[2], ACCIONES[7]},
            {OBJETOS[2], ACCIONES[9]},
            // Presentaciones (Solo consultar todas y modificar estado de apertura)
            {OBJETOS[3], ACCIONES[4]},
            {OBJETOS[3], ACCIONES[9]}};
    /**
     * Rol fiscal
     */
    public final static String ROL_FISCAL_NOMBRE = "RolFiscal";
    /**
     * Contenedor de todos los permisos de los fiscales como matriz [objeto][accion]
     */
    public final static String[][] PERMISOS_FISCAL = new String[][]{
            // Municipios (Solo consultar todos y supervisar)
            {OBJETOS[1], ACCIONES[9]},
            {OBJETOS[1], ACCIONES[15]},
            // Convocatorias (Solo consultar todas)
            {OBJETOS[2], ACCIONES[9]},
            // Presentaciones (Solo consultar asignadas)
            {OBJETOS[3], ACCIONES[12]}};
    /**
     * Rol cuentadante
     */
    public final static String ROL_CUENTADANTE_NOMBRE = "RolCuentadante";
    /**
     * Contenedor de todos los permisos de los cuentadantes como matriz [objeto][accion]
     */
    public final static String[][] PERMISOS_CUENTADANTE = new String[][]{
            // Municipios (Solo consultar el que tenga asignado y representar)
            {OBJETOS[1], ACCIONES[14]},
            {OBJETOS[1], ACCIONES[16]},
            // Convocatorias (Solo consultar abiertas)
            {OBJETOS[2], ACCIONES[13]},
            // Presentaciones (Solo crear, modificar propias, eliminar propias y consultar propias)
            {OBJETOS[3], ACCIONES[0]},
            {OBJETOS[3], ACCIONES[5]},
            {OBJETOS[3], ACCIONES[8]},
            {OBJETOS[3], ACCIONES[14]}};
    /**
     * Conjunto de permisos del rol
     */
    private final LinkedList<Permiso> permisos;
    /**
     * Nombre del rol
     */
    private final String nombreRol;

    /**
     * Constructor del rol de usuario con su nombre correspondiente
     *
     * @param nombreRol Rol del usuario como un string
     */
    public RolUsuario(String nombreRol) {
        String[][] permisosEstablecidos;
        this.nombreRol = nombreRol;
        switch (nombreRol) {
            case (ROL_CUENTADANTE_NOMBRE) -> permisosEstablecidos = PERMISOS_CUENTADANTE;
            case (ROL_FISCAL_NOMBRE) -> permisosEstablecidos = PERMISOS_FISCAL;
            case (ROL_FISCAL_GRAL_NOMBRE) -> permisosEstablecidos = PERMISOS_FISCAL_GRAL;
            case (ROL_ADMINISTRADOR_NOMBRE) -> permisosEstablecidos = PERMISOS_ADMINISTRADOR;
            default -> permisosEstablecidos = new String[][]{};
        }
        permisos = new LinkedList<>();
        for (String[] permiso : permisosEstablecidos) {
            addPermiso(permiso[0], permiso[1]);
        }
    }

    /**
     * Verifica si el rol tiene los permisos necesarios
     *
     * @param objeto Objeto sobre el que se realiza la accion
     * @param accion Accion a realizar sobre el objeto
     * @return true si tiene permiso, false en caso contrario
     */
    public boolean tienePermiso(String objeto, String accion) {
        for (Permiso permiso : permisos) {
            // Se usa index of para que si hay control total solo se ponga Modificar y no haya que poner todos los casos
            if (permiso.getObjeto().equals(objeto) && accion.indexOf(permiso.getAccion()) == 0)
                return true;
        }
        return false;
    }

    /**
     * Devuelve el nombre del rol como string
     *
     * @return El nombre del rol como string
     */
    public String getNombreRol() {
        return nombreRol;
    }

    /**
     * Agrega permisos al rol
     *
     * @param objeto Objeto sobre el que se realiza la accion
     * @param accion Accion a realizar sobre el objeto
     */
    public void addPermiso(String objeto, String accion) {
        permisos.add(new Permiso(objeto, accion));
    }
}
