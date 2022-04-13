package com.fedebonel.modelo.evento;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Estructura de datos que contiene a todos los documentos de cada evento.
 * Si contiene opciones, solo aquellos documentos contenidos en estos seran agregables
 * de no contener opciones cualquier documento es agregable a la coleccion
 *
 * @author Bonel Federico
 */
public class ColeccionDocumentos {

    /**
     * String constante que contiene el error cuando un documento es invalido (i.g. no esta contenido en la coleccion)
     */
    public static final String ERROR_DOCUMENTO_INVALIDO = "El documento seleccionado es invalido";

    /**
     * Documentos que pueden ser agregados a esta coleccion, de ser vacio, cualquier documento puede ser agregado
     */
    private HashSet<String> opcionesDocumentos;

    /**
     * Lista de documentos que son contenidos en esta estructura de datos
     */
    private HashSet<String> documentos;

    /**
     * Constructor de la coleccion de documentos con restricciones a solo los detallados en las opciones
     *
     * @param documentos         Documentos a agregar a esta coleccion
     * @param opcionesDocumentos Lista de todos los posibles documentos que se pueden agregar a esta coleccion (Los
     *                           demas no son aceptados)
     * @throws IllegalArgumentException Si la lista de documentos a agregar tiene strings no contenidos en la lista
     *                                  de las opciones de documentos
     */
    public ColeccionDocumentos(LinkedList<String> documentos, LinkedList<String> opcionesDocumentos) {
        setOpciones(opcionesDocumentos);
        setDocumentos(documentos);
    }

    /**
     * Constructor de la coleccion de documentos sin ninguna restriccion
     *
     * @param documentos Documentos a agregar a esta coleccion pueden ser cualquiera
     */
    public ColeccionDocumentos(LinkedList<String> documentos) {
        setDocumentos(documentos);
    }

    /**
     * Asigna la lista de los documentos que pueden ser agregados a esta coleccion
     *
     * @param opcionesDocumentos Los unicos documentos que pueden ser agregados como un LinkedList de Strings
     */
    private void setOpciones(LinkedList<String> opcionesDocumentos) {
        this.opcionesDocumentos = new HashSet<>(opcionesDocumentos);
    }

    /**
     * Agrega un nuevo documento a la coleccion
     *
     * @param documento Documento a agregar como un string
     * @throws IllegalArgumentException Si la coleccion posee una lista de opciones y el documento a asignar no esta
     *                                  contenido en ella o si el documento ya esta agregado a la coleccion
     */
    protected void addDocumento(String documento) {
        if ((opcionesDocumentos != null && !opcionesDocumentos.contains(documento))
                || documentos.contains(documento)) throw new IllegalArgumentException(ERROR_DOCUMENTO_INVALIDO);
        documentos.add(documento);
    }

    /**
     * Remueve el documento especificado de la coleccion
     *
     * @param documento Documento a remover como un string
     * @throws IllegalArgumentException Si el documento especificado no se encuentra contenido en la coleccion
     */
    protected void removeDocumento(String documento) {
        if (!documentos.contains(documento)) throw new IllegalArgumentException(ERROR_DOCUMENTO_INVALIDO);
        documentos.remove(documento);
    }

    /**
     * Verifica si el documento esta contenido en la coleccion
     *
     * @param documento Documento a verificar como un string
     * @return verdadero si el documento es contenido en la coleccion, falso en caso contrario
     */
    public boolean containsDocumento(String documento) {
        return documentos.contains(documento);
    }

    /**
     * Devuelve una copia de los documentos contenidos en esta coleccion para iterar por ellos
     *
     * @return Todos los documentos contenidos en la coleccion como un LinkedList
     */
    public LinkedList<String> getDocumentosLinkedList() {
        return new LinkedList<>(documentos);
    }

    /**
     * Asigna los documentos pasados al atributo de documentos
     *
     * @param documentos Documentos a asignar como un LinkedList de Strings
     * @throws IllegalArgumentException Si la coleccion posee una lista de opciones y los documentos a asignar no
     *                                  estan contenidos en ella
     */
    private void setDocumentos(LinkedList<String> documentos) {
        if (opcionesDocumentos != null && !opcionesDocumentos.containsAll(documentos))
            throw new IllegalArgumentException(ERROR_DOCUMENTO_INVALIDO);
        this.documentos = new HashSet<>(documentos);
    }

    /**
     * Devuelve todos los documentos contenidos como una string
     *
     * @return Todos los documentos como un string
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (String llave : documentos) {
            output.append(" | ").append(llave);
        }
        return output.toString();
    }
}
