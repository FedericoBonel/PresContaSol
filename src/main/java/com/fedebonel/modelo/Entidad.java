package com.fedebonel.modelo;

/**
 * Clase que abstrae a todas las entidades del sistema
 *
 * @author Bonel Federico
 */
public abstract class Entidad {

    /**
     * Strings que posee el error cuando el identificador es invalido
     */
    public static final String ERROR_IDENTIFICADOR_INVALIDO = "El identificador ingresado es invalido";

    /**
     * Identificador de la entidad
     */
    private String identificador;

    /**
     * Constructor de entidad
     *
     * @param identificador identificador utilizado para identificar la entidad en el sistema
     * @throws IllegalArgumentException Si no se cumple el requisito de formato del identificador
     */
    public Entidad(String identificador) throws IllegalArgumentException {
        setId(identificador);
    }

    /**
     * Retorna identificador de la entidad
     *
     * @return El identificador asignado a esta entidad
     */
    public String getId() {
        return identificador;
    }

    /**
     * Asigna el identificador a esta instancia
     *
     * @param identificador identificador a asignar
     * @throws IllegalArgumentException Si el identificador pasado no cumple con los requerimientos de formato
     */
    private void setId(String identificador) throws IllegalArgumentException {
        if (!cumpleFormatoId(identificador)) throw new IllegalArgumentException(ERROR_IDENTIFICADOR_INVALIDO);
        identificador = identificador.replaceAll("\\s+","");
        this.identificador = identificador;
    }

    /**
     * Verifica si el id pasado cumple el requisito de formato
     *
     * @param identificador identificador a verificar
     * @return Verdadero si el formato es cumplido, falso en caso contrario
     */
    protected abstract boolean cumpleFormatoId(String identificador);

    /**
     * Devuelve el identificador de la entidad
     *
     * @return El string del objeto
     */
    @Override
    public String toString() {
        return identificador;
    }
}
