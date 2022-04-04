package com.fedebonel.modelo.usuario;

/**
 * Clase que abstrae a los permisos de los usuarios
 */
public class Permiso {
    /**
     * Objeto sobre el que se realizan las acciones como string
     */
    private final String objeto;
    /**
     * Accion a realizar sobre objetos como string
     */
    private final String accion;

    /**
     * Constructor de permiso
     * @param objeto Objeto sobre el que se realizan las acciones como string
     * @param accion Accion a realizar sobre objetos como string
     */
    public Permiso(String objeto, String accion) {
        this.objeto = objeto;
        this.accion = accion;
    }

    /**
     * Devuelve el objeto sobre el que se realiza la accion
     * @return Objeto sobre el que se realice la accion como string
     */
    public String getObjeto() {
        return objeto;
    }

    /**
     * Devuelve la accion que se realiza sobre el objeto
     * @return Accion que se realiza sobre el objeto como string
     */
    public String getAccion() {
        return accion;
    }
}
