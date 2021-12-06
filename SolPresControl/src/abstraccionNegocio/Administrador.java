package abstraccionNegocio;

import java.util.LinkedList;

/**
 * Clase que abstrae a todos los Administradores del sistema
 *
 * @author Bonel Federico
 */
public class Administrador extends FiscalGral {
    /**
     *  Constante que mantiene el tipo de usuario
     */
    private static final String TIPO_ADMINISTRADOR = "Administrador";

    /**
     * Constructor para instanciar Fiscales Generales
     *
     * @param usuario Identificador del usuario
     * @param clave   Clave a asignar para el ingreso al sistema del usuario
     */
    public Administrador(String usuario, String clave) throws IllegalArgumentException {
        super(usuario, clave);
    }

    /**
     * Verifica si la convocatoria puede ser eliminada por el usuario actual
     * @param convocatoria Convocatoria a verificar
     * @return true si el usuario puede eliminarla, falso de otra forma
     */
    @Override
    protected boolean puedeEliminarConvocatoria(Convocatoria convocatoria, ColeccionPresentaciones presentaciones){
        return true;
    }

    /**
     * Devuelve los usuarios que deben ser visibles para los administradores, estos son:
     * Todos los usuarios del sistema
     *
     * @param usuarios Coleccion de todas los usuarios del sistema
     * @return Un linked list con todas los usuarios que corresponden al usuario Administrador
     */
    @Override
    public LinkedList<Usuario> getUsuariosVisibles(ColeccionUsuarios usuarios) {
        return usuarios.getUsuariosLinkedList();
    }

    /**
     * Crea un Cuentadante y lo agrega a la coleccion de Usuarios pasada
     *
     * @param id       Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave    Clave usada por el cuentadante para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @param usuarios Coleccion de todos los usuarios del sistema donde se agregara el nuevo usuario
     * @throws IllegalArgumentException Si algun parametro no cumple con los requisitos de formato o el usuario ya esta registrado
     */
    public void creaCuentadante(String id, String clave, ColeccionUsuarios usuarios) throws IllegalArgumentException {
        Cuentadante nuevoUsuario = new Cuentadante(id, clave);
        usuarios.addUsuario(nuevoUsuario);
    }

    /**
     * Crea un Fiscal y lo agrega a la coleccion de Usuarios pasada
     *
     * @param id       Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave    Clave usada por el cuentadante para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @param usuarios Coleccion de todos los usuarios del sistema donde se agregara el nuevo usuario
     * @throws IllegalArgumentException Si algun parametro no cumple con los requisitos de formato o el usuario ya esta registrado
     */
    public void creaFiscal(String id, String clave, ColeccionUsuarios usuarios) throws IllegalArgumentException {
        Fiscal nuevoUsuario = new Fiscal(id, clave);
        usuarios.addUsuario(nuevoUsuario);
    }

    /**
     * Crea un Fiscal Gral y lo agrega a la coleccion de Usuarios pasada
     *
     * @param id       Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave    Clave usada por el cuentadante para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @param usuarios Coleccion de todos los usuarios del sistema donde se agregara el nuevo usuario
     * @throws IllegalArgumentException Si algun parametro no cumple con los requisitos de formato o el usuario ya esta registrado
     */
    public void creaFiscalGral(String id, String clave, ColeccionUsuarios usuarios) throws IllegalArgumentException {
        FiscalGral nuevoUsuario = new FiscalGral(id, clave);
        usuarios.addUsuario(nuevoUsuario);
    }

    /**
     * Crea un Administrador y lo agrega a la coleccion de Usuarios pasada
     *
     * @param id       Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave    Clave usada por el cuentadante para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @param usuarios Coleccion de todos los usuarios del sistema donde se agregara el nuevo usuario
     * @throws IllegalArgumentException Si algun parametro no cumple con los requisitos de formato o el usuario ya esta registrado
     */
    public void creaAdministrador(String id, String clave, ColeccionUsuarios usuarios) throws IllegalArgumentException {
        Administrador nuevoUsuario = new Administrador(id, clave);
        usuarios.addUsuario(nuevoUsuario);
    }

    /**
     * Elimina el usuario especificado de la coleccion de usuarios
     *
     * @param usuario        Usuario a eliminar del sistema
     * @param usuarios       Coleccion de todos los usuarios del sistema
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @throws IllegalArgumentException Si el usuario no se halla registrado en el sistema
     */
    public void eliminaUsuario(Usuario usuario, ColeccionUsuarios usuarios, ColeccionPresentaciones presentaciones, ColeccionMunicipios municipios)
            throws IllegalArgumentException {
        usuarios.removeUsuario(usuario, presentaciones, municipios);
    }


    /**
     * Actualiza la clave del usuario por una nueva
     *
     * @param usuario    Usuario a actualizar
     * @param nuevaClave Clave a asignar: Puede tener desde 4 caracteres hasta 8
     * @throws IllegalArgumentException Si la clave no cumple el formato requerido
     */
    public void actualizaClaveDe(Usuario usuario, String nuevaClave) throws IllegalArgumentException {
        usuario.setClave(nuevaClave);
    }

    /**
     * Verifica si la presentacion puede ser eliminada por el Administrador
     *
     * @param presentacion Presentacion a verificar
     * @return true, el Administrador puede eliminar todas las presentaciones
     */
    @Override
    protected boolean puedeEliminarPresentacion(Presentacion presentacion) {
        return true;
    }

    /**
     * Verifica si el Administrador puede entregar el documento en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     * @return true, el administrador puede entregar cualquier documento en cualquier presentacion
     */
    @Override
    protected boolean puedeEntregarDocumentoA(Presentacion presentacion, String documento) {
        return true;
    }

    /**
     * Verifica si el Administrador puede retirar el documento de la presentacion
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     * @return true, el Administrador puede retirar documentos en cualquier presentacion
     */
    @Override
    protected boolean puedeRetirarDocumentoDe(Presentacion presentacion, String documento) {
        return true;
    }

    /**
     * Crea un municipio y lo agrega a la coleccion de municipios pasado
     *
     * @param id         Identificador alfanumerico unico de municipio: Puede tener desde 1 caracter hasta 30 caracteres
     * @param categoria  Categoria del municipio
     * @param municipios Coleccion de todos los municipios donde se agregara el nuevo municipio
     * @throws IllegalArgumentException Si alguno de los parametros es invalido o el municipio se halla registrado
     */
    public void creaMunicipio(String id, int categoria, ColeccionMunicipios municipios) throws IllegalArgumentException {
        Municipio nuevoMunicipio = new Municipio(id, categoria);
        municipios.addMunicipio(nuevoMunicipio);
    }

    /**
     * Elimina el municipio especificado de la coleccion de municipios
     *
     * @param municipio      Municipio que se desea eliminar como objeto
     * @param municipios     Coleccion de todos los municipios desde donde se eliminara el municipio
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @throws IllegalArgumentException si el municipio no se halla registrado en el sistema
     */
    public void eliminaMunicipio(Municipio municipio, ColeccionMunicipios municipios,
                                 ColeccionPresentaciones presentaciones, ColeccionUsuarios usuarios) throws IllegalArgumentException {
        municipios.removeMunicipio(municipio, presentaciones, usuarios);
    }

    /**
     * Asigna nueva categoria al municipio especificado
     *
     * @param municipio Municipio al que se desea asignar la categoria
     * @param categoria Nueva categoria a asignar
     */
    public void asignaCategoria(Municipio municipio, int categoria) throws IllegalArgumentException {
        municipio.setCategoria(categoria);
    }

    @Override
    public String toStringConClave() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + super.getClave() + '\'' +
                ", tipo='" + TIPO_ADMINISTRADOR + '\'' +
                "} ";
    }

    @Override
    public String toString() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + "ESCONDIDA" + '\'' +
                ", tipo='" + TIPO_ADMINISTRADOR + '\'' +
                "} ";
    }
}
