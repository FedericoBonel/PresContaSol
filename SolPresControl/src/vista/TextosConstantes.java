package vista;

/**
 * Contenedor de todos los textos del sistema
 */
public class TextosConstantes {

    /**
     * Strings comunes
     */
    public final static String ERROR_ID_NO_ENCONTRADO = "Identificador no encontrado en el sistema";
    public final static String ERROR_FECHA = "Por favor ingrese una fecha valida (en el futuro y en numeros enteros positivos)";
    public final static String ERROR_OPERACION_INVALIDA = "OPERACION INVALIDA";
    public static final String INGRESE_VACIO_TERMINAR = "---- Si desea terminar la operacion presione Intro/Enter sin escribir nada ----";
    public final static String INGRESE_NUEVA_FECHA_YEAR = "Ingrese el año de nueva fecha: ";
    public final static String INGRESE_NUEVA_FECHA_MONTH = "Ingrese el mes de nueva fecha: ";
    public final static String INGRESE_NUEVA_FECHA_DAY = "Ingrese el dia de nueva fecha: ";
    public final static String CONFIRMAR_OPERACION = "Esta seguro que desea continuar?";
    public final static String EXITO_OPERACION = "Exito en operacion!!";
    public final static String INFORMACION_CABECERA = "----------- Informacion General del Sistema -----------";
    public final static String ERROR_FORMATO_O_YA_EXISTE = "\nEl identificador ya existe o es invalido";
    public final static String SALIENDO_DEL_SISTEMA = "Saliendo del sistema...";

    /**
     * Operaciones basicas
     */
    public final static String GESTOR_MODIFICAR = "1. Modificar";
    public final static String GESTOR_ELIMINAR = "2. Eliminar";
    public final static String GESTOR_CREAR = "3. Crear";
    public final static String SI = "1. Si";
    public final static String NO = "0. No";

    /**
     * Pantalla Login
     */
    public final static String LOGIN_TEXT_BIENVENIDO = "++++++ Bienvenido! ++++++";
    public final static String LOGIN_TEXTO_INSERTEUSUARIO = "Inserte su usuario: ";
    public final static String LOGIN_TEXTO_CLAVE = "Inserte su clave: ";
    public final static String LOGIN_TEXTO_USUARIOINCORRECTO = "---Usuario incorrecto! Intente nuevamente---";

    /**
     * Menu principal
     */
    public final static String MENU_CABECERA = "----------- Menu Principal -----------";
    public final static String MENU_INFORMACION = "4. Informacion";
    public final static String MENU_MUNICIPIOS = "3. Municipios";
    public final static String MENU_PRESENTACIONES = "2. Presentaciones";
    public final static String MENU_CONVOCATORIAS = "1. Convocatorias";
    public final static String MENU_SALIR = "0. SALIR";
    public final static String MENUADMIN_USUARIOS = "5. Usuario";
    public final static String ERROR_SOLO_ENTEROS = "Solo puedes ingresar numeros enteros positivos.";
    public final static String INGRESE_INPUT = "Ingrese su opcion en forma de enteros (1, 2, 3, etc.)";

    /**
     * Identificadores de Eventos
     */
    public final static String EVENTOS_ERROR_FORMATO = "Por favor ingrese solo identificadores menores a 100 caracteres";

    /**
     * Gestor de convocatorias
     */
    public final static String CONVOCATORIAS_CABECERA = "----------- Convocatorias -----------";
    public final static String CONVOCATORIAS_GESTOR_CAMPOS = "Identificador   |   Estado Apertura  |   Documentos Requeridos   |" +
            "   Fecha de apertura  |   Fecha de cierre   ";
    public final static String INGRESE_IDENTIFICADOR_CONVOCATORIA = "Ingrese el identificador de la convocatoria: ";
    public final static String CONVOCATORIAS_MODIFICAR_CABECERA = "----------- Modificar Convocatoria -----------";
    public final static String CONVOCATORIAS_MODIFICAR_APERTURA_ESTADO = "4. Estado de apertura";
    public final static String CONVOCATORIAS_MODIFICAR_DOCUMENTOS = "3. Documentos requeridos";
    public final static String CONVOCATORIAS_MODIFICAR_CIERRE = "2. Fecha de cierre";
    public final static String CONVOCATORIAS_MODIFICAR_APERTURA = "1. Fecha de apertura";

    public final static String CONVOCATORIA_ERROR_FECHA_APERTURA = "Por favor ingrese una fecha valida (Despues de la actual y antes de la de cierre)";
    public static final String CONVOCATORIA_ERROR_FECHA_CIERRE = "Por favor ingrese una fecha valida (Despues de la de apertura)";
    public final static String CONVOCATORIA_FECHA_APERTURA = "----------- Fecha de Apertura -----------";
    public final static String CONVOCATORIA_FECHA_CIERRE = "----------- Fecha de Cierre -----------";

    /**
     * Gestor de Presentaciones
     */
    public final static String PRESENTACIONES_CABECERA = "----------- Presentaciones -----------";
    public final static String PRESENTACIONES_GESTOR_CAMPOS = "Identificador   |   Estado Apertura  |   Documentos entregados   |" +
            "   Fecha creada   |   Fecha limite  |   Usuario Presentador   |   Todos requeridos entregados";
    public final static String INGRESE_IDENTIFICADOR_PRESENTACIONES = "Ingrese el identificador de la presentacion: ";
    public final static String ERROR_CONVOCATORIA_CERRADA = "ERROR: La convocatoria seleccionada ya se encuentra cerrada";
    public final static String PRESENTACION_CERRADA = "OPERACION INVALIDA, La presentacion ya se ha entregado!";
    public final static String PRESENTACIONES_MODIFICAR_CABECERA = "----------- Modificar Presentacion -----------";
    public final static String PRESENTACIONES_MODIFICAR_APERTURA_ESTADO = "1. Estado de apertura";
    public final static String PRESENTACIONES_MODIFICAR_DOCUMENTOS = "2. Documentos entregados";
    public final static String PRESENTACIONES = "Presentaciones: -----------";
    public final static String PRESENTACIONES_CONVO_ABIERTAS =
            "Total de presentaciones a convocatorias abiertas por todos los usuarios: ";
    public final static String PRESENTACIONES_CONVO_CERRADAS =
            "Total de presentaciones a convocatorias cerradas por todos los usuarios: ";

    /**
     * Gestor de Municipios
     */
    public final static String MUNICIPIOS_GESTOR_CABECERA = "----------- Municipios -----------";
    public final static String MUNICIPIOS_GESTOR_CAMPOS = "Identificador   |   Categoria  |   Cuentadante Asignado   |";
    public final static String INGRESE_IDENTIFICADOR_MUNICIPIO = "Ingrese el identificador del municipio: ";
    public final static String MUNICIPIOS_MODIFICAR_CABECERA = "----------- Modificar Municipio -----------";
    public final static String MUNICIPIOS_MODIFICAR_CATEGORIA = "1. Categoria";
    public final static String MUNICIPIOS_MODIFICAR_CUENTADANTE = "2. Cuentadante Asignado";
    public final static String MUNICIPIOS_ACTUAL_CATEGORIA = "Categoria actual -> ";
    public final static String MUNICIPIOS_PEDIR_CATEGORIA = "Ingrese nueva categoria: ";


    /**
     * Agregar o borrar menu
     */
    public final static String MODIFICAR_DOCUMENTOS_CABECERA = "----------- Modificar Documentos -----------";
    public final static String AGREGAR_OPCION = "1. Agregar";
    public final static String BORRAR_OPCION = "2. Borrar";


    /**
     * Documentos
     */
    public final static String DOCUMENTOS = "Documentos: -----------";
    public final static String DOCUMENTOS_REQUERIDOS = "----------- Documentos Requeridos: -----------";
    public final static String FALTAN_DOCUMENTOS = "Hay documentos requeridos no entregados.";
    public final static String DOCUMENTO_NO_EN_LISTA = "El documento no se haya en la lista de opciones";
    public final static String DOCUMENTO_NO_ADICIONAL = "El documento no es adicional";
    public final static String PEDIR_DOCUMENTO = "Ingrese el nombre del documento: ";

    /**
     * Usuarios
     */
    public final static String USUARIOS_CABECERA = "----------- Usuarios -----------";
    public final static String USUARIOS_GESTOR_CAMPOS = "Nombre de usuario   |   Clave   |   Tipo   |   Cuentadantes Asignados   " +
            "|   Fiscal Asignado   ";
    public final static String USUARIOS_ERROR_FORMATO = "Por favor ingrese solo identificadores menores a 10 caracteres";
    public final static String USUARIOS_ERROR_TIPO_FORMATO =
            "Por favor ingrese solo tipos de usuario validos: (Administrador, Cuentadante, Fiscal, Fiscal General)";
    public final static String INGRESE_IDENTIFICADOR_USUARIO = "Ingrese el identificador del usuario: ";
    public final static String IDENTIFICADOR_NO_CUENTADANTE = "El identificador no pertenece a un cuentadante!";
    public final static String USUARIOS_QUE_ATRIBUTO_MODIFICAR = "Ingrese el atributo a modificar: ";
    public final static String USUARIOS_CONTRA = "2. Clave";
    public final static String FISCAL_CUENTADANTES = "1. Cuentadantes";
    public final static String CUENTADANTE_FISCAL = "1. Fiscal";
    public final static String USUARIOS_NUEVA_CONTRA = "Ingrese la nueva clave a asignar: ";
    public final static String USUARIOS_INGRESE_TIPO = "Ingrese el tipo del usuario: ";
    public final static String IDENTIFICADOR_NO_FISCAL = "El identificador no pertenece a un fiscal!";
    public final static String USUARIO_CLAVE_FORMATO_ERROR = "Por favor ingrese solo claves entre 4 y 8 caracteres";

}
