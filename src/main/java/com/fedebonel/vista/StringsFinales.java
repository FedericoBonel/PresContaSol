package com.fedebonel.vista;

/**
 * Clase que contiene los strings constantes utilizados en las vistas
 */
public class StringsFinales {

    /**
     * Nombre de la aplicacion
     */
    public final static String NOMBRE_SOFTWARE = "SolPresControl";


    // Strings principales
    /**
     * String "Autenticacion de usuario"
     */
    public final static String AUTENTICACION_USUARIO = "Autenticacion de usuario";
    /**
     * String "Usuario"
     */
    public final static String USUARIO = "Usuario";
    /**
     * String "Usuarios"
     */
    public final static String USUARIOS = "Usuarios";
    /**
     * String "Presentacion"
     */
    public static final String PRESENTACION = "Presentacion";
    /**
     * String "Presentaciones"
     */
    public static final String PRESENTACIONES = "Presentaciones";
    /**
     * String "Municipio"
     */
    public static final String MUNICIPIO = "Municipio";
    /**
     * String "Municipios"
     */
    public static final String MUNICIPIOS = "Municipios";
    /**
     * String "Convocatoria"
     */
    public static final String CONVOCATORIA = "Convocatoria";
    /**
     * String "Convocatorias"
     */
    public static final String CONVOCATORIAS = "Convocatorias";
    /**
     * String "Informacion del sistema"
     */
    public static final String INFORMACION = "Informacion del sistema";
    /**
     * String "Crear"
     */
    public final static String CREAR = "Crear";

    // Verbos
    /**
     * String "Modificar"
     */
    public final static String MODIFICAR = "Modificar";
    /**
     * String "Eliminar"
     */
    public final static String ELIMINAR = "Eliminar";
    /**
     * String "Ingresar"
     */
    public final static String INGRESAR = "Ingresar";
    /**
     * String "Salir"
     */
    public final static String SALIR = "Salir del sistema";
    /**
     * String "Actualizar datos"
     */
    public final static String ACTUALIZAR = "Actualizar datos";
    /**
     * String "Agregar documento adicional"
     */
    public final static String DOCS_ADICIONALES = "Documentos adicionales";
    /**
     * String "Entregado"
     */
    public final static String ENTREGADO = "Entregado";
    /**
     * String "remover"
     */
    public final static String REMOVER = "Remover";
    /**
     * String "agregar"
     */
    public final static String AGREGAR = "Agregar";
    /**
     * String "Clave"
     */
    public final static String CLAVE = "Clave";

    // Sustantivos
    /**
     * String "¿Esta seguro?"
     */
    public final static String ESTA_SEGURO = "¿Esta seguro?";

    // Preguntas
    /**
     * String "Ninguno"
     */
    public final static String NINGUNO = "Ninguno";

    // Miscelaneo
    /**
     * String "Total de presentaciones realizadas a convocatorias abiertas"
     */
    public final static String TOTAL_PRESEN_CONVO_ABIERTAS = "Total de presentaciones realizadas a convocatorias abiertas";
    /**
     * String "Total de presentaciones realizadas a convocatorias cerradas"
     */
    public final static String TOTAL_PRES_CONVO_CERRADAS = "Total de presentaciones realizadas a convocatorias cerradas";
    /**
     * Array de atributos a mostrar en la vista de informacion
     */
    public final static String[] COLUMNAS_MUNICIPIO_INFORMACION =
            new String[]{"Identificador del Municipio", "Nombre", "N° Presentaciones Realizadas", "N° Documentos Presentados"};

    // Campos a mostrar en las tablas de cada panel de objetos
    /**
     * Array de atributos a mostrar en la vista de convocatorias
     */
    public final static String[] COLUMNAS_CONVOCATORIAS =
            new String[]{"Identificador", "Fecha de Apertura", "Fecha de Cierre", "Documentos Requeridos", "Descripcion", "Abierta", "N° Presentaciones Recibidas"};
    /**
     * Array de atributos a mostrar en la vista de usuarios
     */
    public final static String[] COLUMNAS_USUARIOS = new String[]{"Nombre de Usuario", "Nombre", "Clave", "Rol"};
    /**
     * Array de atributos a mostrar en la vista de usuarios
     */
    public final static String[] COLUMNAS_PRESENTACIONES =
            new String[]{"Identificador", "Fecha de Creacion", "Autor", "Documentos Presentados", "Municipio", "Entregada", "Convocatoria"};
    /**
     * Array de atributos a mostrar en la vista de usuarios
     */
    public final static String[] COLUMNAS_MUNICIPIO =
            new String[]{"Identificador", "Nombre", "Categoria", "Supervisor", "Representante"};
    /**
     * String constante que contiene el error a mostrar por usuario o clave incorrecto
     */
    public final static String ERROR_USUARIO_CLAVE = "Usuario o clave incorrecta";

    // Errores
    /**
     * String constante que posee el mensaje a mostrar cuando hay un error por intentar asignar como supervisor a un
     * usuario que no tiene el permiso
     */
    public static final String ERROR_USUARIO_NO_SUPERVISOR = "El usuario a asignar no posee el rol requerido para supervisar municipios";
    /**
     * String constante que posee el mensaje a mostrar cuando hay un error por intentar asignar como representante a un
     * usuario que no tiene el permiso
     */
    public static final String ERROR_USUARIO_NO_REPRESENTANTE = "El usuario a asignar no posee el rol requerido para representar municipios";
    /**
     * String constante que posee el error cuando no se poseen los permisos
     */
    public static final String ERROR_NO_PERMISOS = "No se poseen los permisos para la operacion";
    /**
     * String constante que posee el mensaje a mostrar cuando hay un error
     */
    public static final String ERROR_REALIZANDO_OPERACION = "Error al realizar operacion: ";
    /**
     * String constante que posee el error a mostrar cuando la convocatoria esta cerrada
     */
    public static final String ERROR_CONVOCATORIA_CERRADA = "convocatoria cerrada";
    /**
     * String constante que posee el error a mostrar cuando faltan documentos a entregar
     */
    public static final String ERROR_DOCUMENTOS_REQUERIDOS = "faltan documentos requeridos a entregar";
    /**
     * String constate que posee el error a mostrar cuando la fecha se ingresa en un formato incorrecto
     */
    public static final String ERROR_FECHA_FORMATO_INCORRECTO = "Fechas deben ir formato 'AAAA-MM-DD' sin comillas";
    // Constructor privado para evitar instanciacion
    private StringsFinales() {
    }


}
