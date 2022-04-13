package com.fedebonel.vista.formularios.creacion;

import com.fedebonel.controlador.controladorobjetos.MunicipiosControlador;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.vista.Estilo;
import com.fedebonel.vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formulario para crear municipios
 */
public class FormularioCrearMunicipio {
    /**
     * Ventana del formulario
     */
    public final JFrame ventana;
    /**
     * Boton de crear
     */
    public final JButton crearBoton;
    /**
     * Campo de texto de identificador (Se usan los limites de usuario por cuestiones visuales)
     */
    public JTextField idCampo;
    /**
     * Campo de texto de nombre (Se usan los limites de usuario por cuestiones visuales)
     */
    public JTextField nombreCampo;
    /**
     * Opciones para la categoria
     */
    public JComboBox<Integer> categoriaCampo;

    /**
     * Constructor de un nuevo formulario de creacion de usuarios
     *
     * @param controlador controlador que debe gestionar las interacciones de este formulario
     */
    public FormularioCrearMunicipio(MunicipiosControlador controlador) {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.CREAR + " " + StringsFinales.MUNICIPIO);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_CREAR_MUNI[0], Estilo.DIMENSIONES_FORM_CREAR_MUNI[1]);
        ventana.setResizable(false);

        // Componentes identificador
        JPanel contenedorId = crearComponenteIdentificador();
        // Componentes nombre
        JPanel contenedorNombre = crearComponenteNombre();
        // Componentes categoria
        JPanel contenedorCategoria = crearComponenteCategoria();

        // Setea los parametros del boton crear
        crearBoton = new JButton(StringsFinales.CREAR);
        crearBoton.setActionCommand(StringsFinales.CREAR);
        crearBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorNombre);
        contenedorTodos.add(contenedorCategoria);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(crearBoton);
        ventana.add(BorderLayout.CENTER, contenedorTodos);
        ventana.add(BorderLayout.SOUTH, crearBoton);
        ventana.setLocationRelativeTo(null);
    }

    /**
     * Crea el componente del identificador
     *
     * @return Un panel con todos los componentes del identificador
     */
    private JPanel crearComponenteIdentificador() {
        JPanel contenedorId = new JPanel(new BorderLayout());
        JLabel idEtiqueta = new JLabel(StringsFinales.COLUMNAS_MUNICIPIO[0]);
        idCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorId.add(BorderLayout.WEST, idEtiqueta);
        contenedorId.add(BorderLayout.EAST, idCampo);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente del nombre
     *
     * @return Un panel con todos los componentes del nombre
     */
    private JPanel crearComponenteNombre() {
        JPanel contenedorNombre = new JPanel(new BorderLayout());
        JLabel nombreEtiqueta = new JLabel(StringsFinales.COLUMNAS_MUNICIPIO[1]);
        nombreCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorNombre.add(BorderLayout.WEST, nombreEtiqueta);
        contenedorNombre.add(BorderLayout.EAST, nombreCampo);
        contenedorNombre.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorNombre;
    }

    /**
     * Crea el componente de la categoria
     *
     * @return Un panel con todos los componentes de la categoria
     */
    private JPanel crearComponenteCategoria() {
        JPanel contenedorCategoria = new JPanel(new BorderLayout());
        JLabel categoriaEtiqueta = new JLabel(StringsFinales.COLUMNAS_MUNICIPIO[2]);
        Integer[] opcionesCategoria = new Integer[Municipio.LIMITE_CATEGORIA_SUPERIOR];
        for (int i = 0; i < Municipio.LIMITE_CATEGORIA_SUPERIOR; i++) opcionesCategoria[i] = i;
        categoriaCampo = new JComboBox<>(opcionesCategoria);
        contenedorCategoria.add(BorderLayout.WEST, categoriaEtiqueta);
        contenedorCategoria.add(BorderLayout.EAST, categoriaCampo);
        contenedorCategoria.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorCategoria;
    }
}
