package vista;

import java.awt.*;

/**
 * Clase que contiene los elementos visuales de "Estilo" usados en las vistas (Dimensiones, espaciados, etc.)
 */
public class Estilo {

    // Dimensiones

    /**
     * Dimensiones de la ventana del menu principal en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_MENU_PRINCIPAL = new int[]{800, 400};
    /**
     * Dimensiones del formulario para crear convocatoriaas en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_CREAR_CONVO = new int[]{400, 500};
    /**
     * Dimensiones del formulario para modificar convocatorias en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_MODIFICAR_CONVO = new int[]{400, 500};
    /**
     * Dimensiones del formulario para crear municipios en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_CREAR_MUNI = new int[]{400, 160};
    /**
     * Dimensiones del formulario para modificar municipios en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_MODIFICAR_MUNI = new int[]{400, 210};
    /**
     * Dimensiones del formulario para crear presentaciones en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_CREAR_PRES = new int[]{400, 250};
    /**
     * Dimensiones del formulario para modificar presentaciones en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_MODIFICAR_PRES = new int[]{400, 600};
    /**
     * Dimensiones del formulario para crear usuarios en pixeles [anchura, altura]
     */
    public final static int[] DIMENSIONES_FORM_CREAR_USUARIO = new int[]{400, 200};
    /**
     * Dimensiones del formulario en pixeles [anchura, altura]
     */
    public static final int[] DIMENSIONES_FORM_MODIFICAR_USUARIO = new int[]{400, 130};
    /**
     * Anchura estandar en columnas de los campos de texto
     */
    public final static int ANCHURA_CAMPO_TEXTO_ESTANDAR = 10;
    /**
     * Anchura del campo de descripcion en los formularios de convocatorias en numero de columnas
     */
    public final static int ANCHURA_DESCRIPCION_CAMPO = 25;


    // Espaciados

    /**
     * Espaciado estandar utilizado en los componentes de cada panel (5,5,5,5)
     */
    public static Insets ESPACIADO_ESTANDAR = new Insets(5,5,5,5);
    /**
     * Espaciado utilizado en vista login (20,20,20,20)
     */
    public static Insets ESPACIADO_TODO_LOGIN = new Insets(20,20,20,20);
    /**
     * Espaciado utilizado en nombre y clave de vista login
     */
    public static Insets ESPACIADO_NOMBRE_CLAVE_LOGIN = new Insets(5,0,5,0);
    /**
     * Espaciado utilizado en barra de menu del menu principal
     */
    public static Insets ESPACIADO_BARRA_MENU = new Insets(0,0,5,0);

}
