package com.fedebonel.vista.formularios.ventanasemergentes;

import com.fedebonel.vista.StringsFinales;

import javax.swing.*;

/**
 * Generador de formularios de opciones
 */
public class FormularioOpcionesGenerador {

    // Constructor privado para evitar instanciacion
    private FormularioOpcionesGenerador(){}

    /**
     * Muestra una ventana emergente de eliminacion de algun objeto
     * @param identificador Identificador del objeto a preguntar si se desea borrar
     * @return La opcion seleccionada por el usuario
     */
    public static boolean mostrarOpcionSiNoEliminar(String identificador){
        int seleccion = JOptionPane.showConfirmDialog(new JFrame(),
                StringsFinales.ESTA_SEGURO,
                StringsFinales.ELIMINAR + " " + identificador,
                JOptionPane.YES_NO_CANCEL_OPTION);
        return seleccion == JOptionPane.YES_OPTION;
    }
}
