package abstraccionNegocio;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Estructura de datos que contiene a todos los usuarios del sistema
 *
 * @author Bonel Federico
 */
public class ColeccionUsuarios {
    // Hashtable que contiene a todos los usuarios
    private final Hashtable<String, Usuario> usuarios;

    /**
     * Constructor que inicializa la coleccion de usuarios
     */
    public ColeccionUsuarios(){
        usuarios = new Hashtable<>();
    }

    /**
     * Devuelve el hashtable que contiene todos los usuarios
     * @return El hashtable<String, Usuario> con todos los usuarios del sistema
     */
    protected Hashtable<String, Usuario> getHashtable(){
        return usuarios;
    }

    /**
     * Devuelve el objeto usuario del identificador
     * @param identificador Identificador del usuario deseado
     * @return Objeto Usuario que deseamos obtener, sera nulo si no existe
     */
    public Usuario getUsuario(String identificador) {
        if (!usuarios.containsKey(identificador)) throw new IllegalArgumentException("Usuario no registrado");
        return usuarios.get(identificador);
    }

    /**
     * Agrega un el usuario a la coleccion
     * @throws IllegalArgumentException si el id del usuario corresponde a uno ya registrado
     * @param usuario Objeto Usuario que deseamos agregar a la coleccion
     */
    public void addUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getId())) throw new IllegalArgumentException("Usuario ya registrado");
        usuarios.put(usuario.getId(), usuario);
    }

    /**
     * Elimina un usuario del sistema
     * @throws IllegalArgumentException si el id pasado no corresponde a un usuario registrado
     * @param usuario Usuario a eliminar
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     */
    public void removeUsuario(Usuario usuario, ColeccionPresentaciones presentaciones, ColeccionMunicipios municipios) {
        if (!usuarios.containsKey(usuario.getId())) throw new IllegalArgumentException("Usuario no registrado");
        // Verifica casos especiales
        if (usuario instanceof Fiscal fiscalEliminar) fiscalEliminar.abandonaSusMunicipiosEn(municipios);
        else if (usuario instanceof Cuentadante cuentaEliminar) cuentaEliminar.eliminaSusPresentacionesDe(presentaciones);
        usuarios.remove(usuario.getId());
    }

    /**
     * Devuelve como un LinkedList todos los usuarios del sistema
     * @return LinkedList con todos los usuarios del sistema
     */
    public LinkedList<Usuario> getUsuariosLinkedList() {
        LinkedList<Usuario> usuariosLista = new LinkedList<>();
        for (String presentacionId : this.usuarios.keySet()) {
            usuariosLista.add(this.usuarios.get(presentacionId));
        }
        return usuariosLista;
    }

    /**
     *  Metodo para conseguir todos los usuarios del sistema como un string
     * @return String con TODOS los usuarios del sistema con TODOS sus parametros
     */
    @Override
    public String toString() {
        StringBuilder usuariosString = new StringBuilder();
        for (String usuarioId : usuarios.keySet()) {
            Usuario usuario = usuarios.get(usuarioId);
            usuariosString.append(usuario.toStringConClave()).append("\n");
        }
        return usuariosString.toString();
    }

}
