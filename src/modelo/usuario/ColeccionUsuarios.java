package modelo.usuario;

import modelo.municipio.ColeccionMunicipios;
import modelo.evento.presentacion.ColeccionPresentaciones;

import java.util.LinkedList;

/**
 * Estructura de datos que contiene a todos los usuarios del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionUsuarios {

    /**
     * String constante que posee el error cuando el usuario esta registrado
     */
    public static final String ERROR_USUARIO_REGISTRADO = "Usuario ya registrado";
    /**
     * String constante que posee el error cuando el usuario no esta registrado
     */
    public static final String ERROR_USUARIO_NO_REGISTRADO = "Usuario no registrado";

    /**
     *  LinkedList que contiene a todos los usuarios
     */
    private final LinkedList<Usuario> usuarios;

    /**
     * Constructor que inicializa la coleccion de usuarios
     */
    public ColeccionUsuarios() {
        usuarios = new LinkedList<>();
    }

    /**
     * Devuelve el objeto usuario del identificador
     * @param identificador Identificador del usuario deseado
     * @return Objeto Usuario que deseamos obtener, sera nulo si no existe
     * @throws IllegalArgumentException Si el usuario no esta registrado
     */
    public Usuario getUsuario(String identificador) {
        Usuario usuarioExistente = buscarUsuario(identificador);
        if (usuarioExistente == null) throw new IllegalArgumentException(ERROR_USUARIO_NO_REGISTRADO);
        return usuarioExistente;
    }

    /**
     * Agrega un el usuario a la coleccion
     * @param usuario Objeto Usuario que deseamos agregar a la coleccion
     * @throws IllegalArgumentException si el id del usuario corresponde a uno ya registrado
     */
    public void addUsuario(Usuario usuario) {
        Usuario usuarioExistente = buscarUsuario(usuario.getId());
        if (usuarioExistente != null) throw new IllegalArgumentException(ERROR_USUARIO_REGISTRADO);
        usuarios.add(usuario);
    }

    /**
     * Elimina un usuario del sistema
     * @param usuario Usuario a eliminar
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @param municipios Coleccion de todos los municipios del sistema
     * @throws IllegalArgumentException si el id pasado no corresponde a un usuario registrado
     */
    public void removeUsuario(Usuario usuario, ColeccionPresentaciones presentaciones, ColeccionMunicipios municipios) {
        Usuario usuarioExistente = buscarUsuario(usuario.getId());
        if (usuarioExistente == null) throw new IllegalArgumentException(ERROR_USUARIO_NO_REGISTRADO);
        // Verifica casos especiales (Puede haber tenido roles diferentes en el pasado)
        usuario.abandonaSusMunicipiosEn(municipios);
        usuario.eliminaSusPresentacionesDe(presentaciones);
        usuarios.remove(usuario);
    }

    /**
     * Devuelve como un LinkedList todos los usuarios del sistema
     * @return LinkedList con todos los usuarios del sistema
     */
    public LinkedList<Usuario> getUsuariosLinkedList() {
        return new LinkedList<>(usuarios);
    }

    /**
     * Devuelve como un LinkedList todos los usuarios que tengan el permiso de supervisar del sistema
     * @return LinkedList con todos los usuarios del sistema
     */
    public LinkedList<Usuario> getSupervisoresLinkedList() {
        LinkedList<Usuario> resultado = new LinkedList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[15])){
                resultado.add(usuario);
            }
        }
        return resultado;
    }

    /**
     * Devuelve como un LinkedList todos los usuarios que tengan el permiso de representar del sistema
     * @return LinkedList con todos los usuarios del sistema
     */
    public LinkedList<Usuario> getRepresentantesLinkedList() {
        LinkedList<Usuario> resultado = new LinkedList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.rolUsuario.tienePermiso(RolUsuario.OBJETOS[1], RolUsuario.ACCIONES[16])){
                resultado.add(usuario);
            }
        }
        return resultado;
    }



    /**
     *  Metodo para conseguir todos los usuarios del sistema como un string
     * @return String con TODOS los usuarios del sistema con TODOS sus parametros
     */
    @Override
    public String toString() {
        StringBuilder usuariosString = new StringBuilder();
        for (Usuario usuario : usuarios) {
            usuariosString.append(usuario.toStringConClave()).append("\n");
        }
        return usuariosString.toString();
    }

    /**
     * Metodo que busca usuarios en la coleccion por el identificador pasado
     *
     * @param identificador identificador del usuario a buscar
     * @return Usuario con ese identificador en caso de existir, null en caso contrario
     */
    private Usuario buscarUsuario(String identificador){
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(identificador.toLowerCase())) return usuario;
        }
        return null;
    }
}
