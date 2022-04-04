package com.fedebonel.vista.componentes;

import com.fedebonel.vista.StringsFinales;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Vista de lista dinamica que permite agregar items y eliminarlos
 * introduciendolos en un campo de texto
 */
public class ListaDinamicaCampoTexto extends JPanel {
    /**
     * Campo para agregar items nuevos
     */
    public final JTextField itemNuevoCampo;
    /**
     * Contenedor items ya agregados
     */
    public final JList<String> itemsAgregados;
    /**
     * Contenedor de los datos de los items ya agregados
     */
    public final DefaultListModel<String> datosAgregados;
    /**
     * Boton agregar
     */
    public final JButton botonAgregar;
    /**
     * Boton remover
     */
    public final JButton botonRemover;


    /**
     * Constructor de una lista dinamica de strings con un campo de texto para agregar items
     *
     * @param datosAgregados StringsFinales a agregar a la lista de datos ya agregados
     */
    public ListaDinamicaCampoTexto(LinkedList<String> datosAgregados) {
        super(new BorderLayout());
        itemNuevoCampo = new JTextField(10);
        this.datosAgregados = new DefaultListModel<>();
        itemsAgregados = new JList<>(this.datosAgregados);
        this.datosAgregados.addAll(datosAgregados);
        // Botones
        botonAgregar = new JButton(StringsFinales.AGREGAR);
        botonRemover = new JButton(StringsFinales.REMOVER);
        JPanel contenedorBotones = new JPanel(new BorderLayout());
        contenedorBotones.add(BorderLayout.WEST, botonAgregar);
        contenedorBotones.add(BorderLayout.EAST, botonRemover);

        // Agrega todos los elementos a un solo contenedor
        super.add(BorderLayout.NORTH, itemNuevoCampo);
        super.add(BorderLayout.CENTER, contenedorBotones);
        super.add(BorderLayout.SOUTH, itemsAgregados);

        // Agrega las acciones de los botones
        botonAgregar.addActionListener(e -> {
            if (!itemNuevoCampo.getText().isEmpty() && !this.datosAgregados.contains(itemNuevoCampo.getText()))
                this.datosAgregados.addElement(itemNuevoCampo.getText());
        });
        botonRemover.addActionListener(e -> {
            List<String> itemsARemover = itemsAgregados.getSelectedValuesList();
            for (String itemSeleccionado : itemsARemover)
                this.datosAgregados.removeElement(itemSeleccionado);
        });
    }

    /**
     * Devuelve todos los datos contenidos en la lista
     *
     * @return Todos los datos contenidos en la lista como un linked list
     */
    public LinkedList<String> getDatos(){
        LinkedList<String> datos = new LinkedList<>();
        for (int i = 0; i < datosAgregados.size(); i++) {
            datos.add(datosAgregados.getElementAt(i));
        }
        return datos;
    }

    /**
     * Setea la vista como interactuable o no interactuable
     *
     * @param habilitado Si es true la vista es interactuable, si es falso no lo es
     */
    @Override
    public void setEnabled(boolean habilitado){
        itemNuevoCampo.setEnabled(habilitado);
        itemsAgregados.setEnabled(habilitado);
        botonAgregar.setEnabled(habilitado);
        botonRemover.setEnabled(habilitado);
    }

}
