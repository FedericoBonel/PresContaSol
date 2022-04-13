package com.fedebonel.vista.formularios.creacion;

import com.fedebonel.controlador.controladorobjetos.ConvocatoriasControlador;
import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.vista.Estilo;
import com.fedebonel.vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Formulario para crear convocatorias
 */
public class FormularioCrearConvocatoria {
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
     * Campo de texto de fecha de apertura (Se usan los limites de usuario por cuestiones visuales)
     */
    public JTextField fechaAperturaCampo;
    /**
     * Campo de texto de fecha de cierre (Se usan los limites de usuario por cuestiones visuales)
     */
    public JTextField fechaCierreCampo;
    /**
     * Opciones documentos
     */
    public JList<String> documentosCampo;
    /**
     * Area de texto para la descripcion
     */
    public JTextArea descripcionArea;

    /**
     * Constructor de un nuevo formulario de creacion de convocatorias
     *
     * @param controlador controlador que debe gestionar las interacciones de este formulario
     */
    public FormularioCrearConvocatoria(ConvocatoriasControlador controlador) {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.CREAR + " " + StringsFinales.CONVOCATORIA);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_CREAR_CONVO[0], Estilo.DIMENSIONES_FORM_CREAR_CONVO[1]);
        ventana.setResizable(false);

        // Componentes identificador
        JPanel contenedorId = crearComponenteIdentificador();
        // Componentes fecha apertura
        JPanel contenedorFechaApertura = crearComponenteFechaApertura();
        // Componentes fecha cierre
        JPanel contenedorFechaCierre = crearComponenteFechaCierre();
        // Componentes documentos requeridos
        JPanel contenedorDocumentos = crearComponenteDocsRequeridos();
        // Componentes descripcion
        JPanel contenedorDescripcion = crearComponenteDescripcion();

        // Setea los parametros del boton crear
        crearBoton = new JButton(StringsFinales.CREAR);
        crearBoton.setActionCommand(StringsFinales.CREAR);
        crearBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorFechaApertura);
        contenedorTodos.add(contenedorFechaCierre);
        contenedorTodos.add(contenedorDocumentos);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(crearBoton);
        ventana.add(BorderLayout.NORTH, contenedorTodos);
        ventana.add(BorderLayout.CENTER, contenedorDescripcion);
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
        JLabel idEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[0]);
        idCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorId.add(BorderLayout.WEST, idEtiqueta);
        contenedorId.add(BorderLayout.EAST, idCampo);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente de la fecha de apertura
     *
     * @return Un panel con todos los componentes de la fecha de apertura
     */
    private JPanel crearComponenteFechaApertura() {
        JPanel contenedorFechaApertura = new JPanel(new BorderLayout());
        JLabel fechaAperturaEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[1]);
        fechaAperturaCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        fechaAperturaCampo.setText(LocalDate.now().toString());
        contenedorFechaApertura.add(BorderLayout.WEST, fechaAperturaEtiqueta);
        contenedorFechaApertura.add(BorderLayout.EAST, fechaAperturaCampo);
        contenedorFechaApertura.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorFechaApertura;
    }

    /**
     * Crea el componente de la fecha de cierre
     *
     * @return Un panel con todos los componentes de la fecha de cierre
     */
    private JPanel crearComponenteFechaCierre() {
        JPanel contenedorFechaCierre = new JPanel(new BorderLayout());
        JLabel fechaCierreEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[2]);
        fechaCierreCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        fechaCierreCampo.setText(LocalDate.now().plusDays(1).toString());
        contenedorFechaCierre.add(BorderLayout.WEST, fechaCierreEtiqueta);
        contenedorFechaCierre.add(BorderLayout.EAST, fechaCierreCampo);
        contenedorFechaCierre.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorFechaCierre;
    }

    /**
     * Crea el componente de los documentos requeridos
     *
     * @return Un panel con todos los componentes de los documentos requeridos
     */
    private JPanel crearComponenteDocsRequeridos() {
        JPanel contenedorDocumentos = new JPanel(new BorderLayout());
        JLabel documentosEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[3]);
        documentosCampo = new JList<>(Convocatoria.DOCUMENTOS_OPCIONES);
        contenedorDocumentos.add(BorderLayout.WEST, documentosEtiqueta);
        contenedorDocumentos.add(BorderLayout.EAST, documentosCampo);
        contenedorDocumentos.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDocumentos;
    }

    /**
     * Crea el componente de la descripcion
     *
     * @return Un panel con todos los componentes de la descripcion
     */
    private JPanel crearComponenteDescripcion() {
        JPanel contenedorDescripcion = new JPanel(new BorderLayout());
        JLabel descripcionEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[4]);
        descripcionArea = new JTextArea();
        descripcionArea.setColumns(Estilo.ANCHURA_DESCRIPCION_CAMPO);
        contenedorDescripcion.add(BorderLayout.WEST, descripcionEtiqueta);
        contenedorDescripcion.add(BorderLayout.EAST, new JScrollPane(descripcionArea));
        contenedorDescripcion.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDescripcion;
    }
}
