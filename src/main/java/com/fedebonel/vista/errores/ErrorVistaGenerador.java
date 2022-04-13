package com.fedebonel.vista.errores;

import com.fedebonel.modelo.accesodatos.ConexionDB;
import com.fedebonel.vista.StringsFinales;

import javax.swing.*;

/**
 * Generador de errores
 */
public final class ErrorVistaGenerador {

    /**
     * Constructor privado para evitar instanciacion de la clase
     */
    private ErrorVistaGenerador() {
    }

    /**
     * Muestra una ventana de error en pantalla con contexto de base de datos
     *
     * @param error Error a mostrar en la ventana
     */
    public static void mostrarErrorDB(Exception error) {
        JOptionPane.showMessageDialog(new JFrame(),
                ConexionDB.ERROR_ACCESO_BASE_DATOS + error.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        System.out.print(ConexionDB.ERROR_ACCESO_BASE_DATOS);
        System.out.println(error.getMessage());
    }

    /**
     * Muestra una ventana de error en pantalla con contexto de Operacion
     *
     * @param error Error a mostrar en la ventana
     */
    public static void mostrarErrorEnOperacion(Exception error) {
        JOptionPane.showMessageDialog(new JFrame(),
                StringsFinales.ERROR_REALIZANDO_OPERACION + error.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        System.out.println(StringsFinales.ERROR_REALIZANDO_OPERACION);
        System.out.println(error.getMessage());
    }

    /**
     * Muestra un error en pantalla con contexto de operacion y un string customizado
     *
     * @param mensajeError Mensaje customizado a mostrar
     */
    public static void mostrarErrorEnOperacionCustomizado(String mensajeError) {
        // Si la fecha no esta en el formato requerido
        JOptionPane.showMessageDialog(new JFrame(),
                StringsFinales.ERROR_REALIZANDO_OPERACION + mensajeError, "", JOptionPane.ERROR_MESSAGE);
        System.out.println(mensajeError);
    }

    /**
     * Muestra un mensaje de error en contexto de no permisos
     */
    public static void mostrarErrorNoPermisos() {
        JOptionPane.showMessageDialog(new JFrame(),
                StringsFinales.ERROR_NO_PERMISOS, "", JOptionPane.ERROR_MESSAGE);
        System.out.println(StringsFinales.ERROR_NO_PERMISOS);
    }

    /**
     * Muestra un error por llaves incorrectas en autenticacion
     */
    public static void mostrarErrorAutenticacion() {
        JOptionPane.showMessageDialog(new JFrame(), StringsFinales.ERROR_USUARIO_CLAVE, "", JOptionPane.ERROR_MESSAGE);
        System.out.println(StringsFinales.ERROR_USUARIO_CLAVE);
    }
}
