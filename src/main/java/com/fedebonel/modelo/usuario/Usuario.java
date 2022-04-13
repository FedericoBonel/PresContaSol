package com.fedebonel.modelo.usuario;

import com.fedebonel.modelo.Entidad;
import com.fedebonel.modelo.municipio.Municipio;

import java.util.List;

/**
 * Clase que abstrae a todos los usuarios del sistema
 */
public class Usuario extends Entidad {
    /**
     * String constante que posee el error cuando la clave ingresada es invalida
     */
    public static final String ERROR_CLAVE_INVALIDA
            = "La clave ingresada es invalida, debe tener longitud de caracteres menor o igual a ";
    /**
     * String constante que posee el error cuando el nombre ingresado es invalido
     */
    public static final String ERROR_NOMBRE_INVALIDO = "El nombre ingresado es invalido";
    /**
     * Limite inferior de la clave en su longitud
     */
    public final static int LIMITE_CLAVE_INFERIOR = 4;
    /**
     * Limite superior de la clave en su longitud
     */
    public final static int LIMITE_CLAVE_SUPERIOR = 8;
    /**
     * Limite inferior del identificador en su longitud
     */
    public final static int LIMITE_IDENTIFICADOR_INFERIOR = 1;
    /**
     * Limite superior del identificador en su longitud
     */
    public final static int LIMITE_IDENTIFICADOR_SUPERIOR = 10;
    /**
     * Rol del usuario
     */
    public RolUsuario rolUsuario;
    /**
     * Nombre del usuario
     */
    private String nombre;
    /**
     * Clave y tipo del usuario
     */
    private String clave;

    /**
     * Constructor de usuario, nombre de usuario es el id en este caso
     *
     * @param nombre     Nombre del usuario: Debe ser un string
     * @param usuario    Identificador alfanumerico unico de usuario: Puede tener desde 1 caracter hasta 10 caracteres
     * @param clave      Clave usada por el usuario para ingresar al sistema: Puede tener desde 4 caracteres hasta 8
     * @param rolUsuario rol del usuario a crear
     * @throws IllegalArgumentException Si algun parametro no cumple con los requisitos de formato
     */
    public Usuario(String nombre, String usuario, String clave, RolUsuario rolUsuario) throws IllegalArgumentException {
        super(usuario.toLowerCase());
        setNombre(nombre);
        setClave(clave);
        setRol(rolUsuario);
    }

    /**
     * Devuelve el nombre del usuario
     *
     * @return nombre del usuario como un string
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de este usuario
     *
     * @param nombre Nombre a asignar
     * @throws IllegalArgumentException Si el nombre esta vacio
     */
    private void setNombre(String nombre) {
        if (nombre.length() < 1) throw new IllegalArgumentException(ERROR_NOMBRE_INVALIDO);
        this.nombre = nombre;
    }

    /**
     * Devuelve la clave del usuario
     *
     * @return La clave del usuario sobre el que se llame
     */
    public String getClave() {
        return clave;
    }

    /**
     * Asigna una nueva clave al usuario
     *
     * @param clave nueva clave a asignar
     * @throws IllegalArgumentException si la clave no cumple el formato requerido
     */
    public void setClave(String clave) throws IllegalArgumentException {
        if (!cumpleFormatoClave(clave))
            throw new IllegalArgumentException(ERROR_CLAVE_INVALIDA + LIMITE_CLAVE_SUPERIOR);
        this.clave = clave;
    }

    /**
     * Verifica si el formato de clave se cumple en el String pasado
     *
     * @param clave clave a verificar
     * @return true si el formato es cumplido, false de otra forma
     */
    private boolean cumpleFormatoClave(String clave) {
        return LIMITE_CLAVE_INFERIOR <= clave.length() && clave.length() <= LIMITE_CLAVE_SUPERIOR;
    }

    /**
     * Verifica si la clave pasada como string se corresponde con la del usuario
     *
     * @param clave Clave a verificar
     * @return Verdadero si se corresponden, falso en caso contrario
     */
    public boolean certificaClave(String clave) {
        return clave.equals(this.clave);
    }

    /**
     * Devuelve el municipio correspondiente a este usuario cuentadante
     *
     * @param municipios Coleccion de los municipios del sistema
     * @return municipio al cual representa este usuario, si no tiene ninguno devuelve null
     */
    public Municipio getMunicipioRepresentadoDe(List<Municipio> municipios) {
        for (Municipio municipio : municipios) {
            if (municipio.isCuentadante(this))
                return municipio;
        }
        return null;
    }

    /**
     * Asigna un nuevo rol
     *
     * @param rol Nuevo rol a asignar
     */
    protected void setRol(RolUsuario rol) {
        this.rolUsuario = rol;
    }

    /**
     * Implementacion del verificador de formato que debe cumplirse para Identificador
     *
     * @param identificador identificador a verificar
     * @return true si el formato es cumplido, false de otra forma
     */
    @Override
    protected boolean cumpleFormatoId(String identificador) {
        return LIMITE_IDENTIFICADOR_INFERIOR <= identificador.length() && identificador.length() <= LIMITE_IDENTIFICADOR_SUPERIOR;
    }

    /**
     * Devuelve los parametros como string a mostrar a los administradores
     *
     * @return Un string con todos los parametros del objeto
     */
    public String toStringConClave() {
        return "{" +
                "nombre de usuario:" + super.toString() +
                ", clave='" + clave + '\'' +
                "} ";
    }

    /**
     * Devuelve los parametros como string a mostrar a los usuarios
     *
     * @return El identificador del usuario
     */
    @Override
    public String toString() {
        return "{" +
                "nombre de usuario:" + super.toString() +
                ", clave='" + "ESCONDIDA" + '\'' +
                "} ";
    }
}
