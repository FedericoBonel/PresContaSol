package com.fedebonel.vista.formularios.modificacion;

import com.fedebonel.controlador.controladorobjetos.ConvocatoriasControlador;
import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.vista.Estilo;
import com.fedebonel.vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

/**
 * Formulario para modificar Convocatorias
 */
public class FormularioModificarConvocatoria {
    /**
     * Ventana del formulario
     */
    public final JFrame ventana;
    /**
     * Boton de modificar
     */
    public final JButton modificarBoton;
    /**
     * Convocatoria a ser modificada
     */
    public final Convocatoria convocatoriaAModificar;
    /**
     * Controlador que debe gestionar las interacciones de esta vista
     */
    private final ConvocatoriasControlador controlador;
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
     * Constructor de un nuevo formulario de modificacion de convocatorias
     *
     * @param controlador            controlador que debe gestionar las interacciones de este formulario
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     */
    public FormularioModificarConvocatoria(ConvocatoriasControlador controlador, Convocatoria convocatoriaAModificar) {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.MODIFICAR + " " + StringsFinales.CONVOCATORIA);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_MODIFICAR_CONVO[0], Estilo.DIMENSIONES_FORM_MODIFICAR_CONVO[1]);
        ventana.setResizable(false);

        // Componente identificador
        JPanel contenedorId = crearComponenteIdentificador(convocatoriaAModificar);
        // Componente fecha apertura
        JPanel contenedorFechaApertura = crearComponenteFechaApertura(convocatoriaAModificar);
        // Componente fecha cierre
        JPanel contenedorFechaCierre = crearComponenteFechaCierre(convocatoriaAModificar);
        // Componente documentos requeridos
        JPanel contenedorDocumentos = crearComponenteDocsRequeridos(convocatoriaAModificar);
        // Componente descripcion
        JPanel contenedorDescripcion = crearComponenteDescripcion(convocatoriaAModificar);

        // Setea los parametros del boton modificar
        modificarBoton = new JButton(StringsFinales.MODIFICAR);
        modificarBoton.setActionCommand(StringsFinales.MODIFICAR);
        modificarBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorFechaApertura);
        contenedorTodos.add(contenedorFechaCierre);
        contenedorTodos.add(contenedorDocumentos);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(modificarBoton);
        ventana.add(BorderLayout.NORTH, contenedorTodos);
        ventana.add(BorderLayout.CENTER, contenedorDescripcion);
        ventana.add(BorderLayout.SOUTH, modificarBoton);
        ventana.setLocationRelativeTo(null);
        // Asigna el controlador
        this.controlador = controlador;
        this.convocatoriaAModificar = convocatoriaAModificar;
    }

    /**
     * Crea el componente del identificador
     *
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     * @return Un panel con todos los componentes del identificador
     */
    private JPanel crearComponenteIdentificador(Convocatoria convocatoriaAModificar) {
        JPanel contenedorId = new JPanel();
        JLabel idEtiqueta = new JLabel(convocatoriaAModificar.getId());
        contenedorId.add(idEtiqueta);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente de la fecha de apertura
     *
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     * @return Un panel con todos los componentes de la fecha de apertura
     */
    private JPanel crearComponenteFechaApertura(Convocatoria convocatoriaAModificar) {
        JPanel contenedorFechaApertura = new JPanel(new BorderLayout());
        JLabel fechaAperturaEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[1]);
        fechaAperturaCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        fechaAperturaCampo.setText(convocatoriaAModificar.getFechaInicio().toString());
        contenedorFechaApertura.add(BorderLayout.WEST, fechaAperturaEtiqueta);
        contenedorFechaApertura.add(BorderLayout.EAST, fechaAperturaCampo);
        contenedorFechaApertura.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorFechaApertura;
    }

    /**
     * Crea el componente de la fecha de cierre
     *
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     * @return Un panel con todos los componentes de la fecha de cierre
     */
    private JPanel crearComponenteFechaCierre(Convocatoria convocatoriaAModificar) {
        JPanel contenedorFechaCierre = new JPanel(new BorderLayout());
        JLabel fechaCierreEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[2]);
        fechaCierreCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        fechaCierreCampo.setText(convocatoriaAModificar.getFechaCierre().toString());
        contenedorFechaCierre.add(BorderLayout.WEST, fechaCierreEtiqueta);
        contenedorFechaCierre.add(BorderLayout.EAST, fechaCierreCampo);
        contenedorFechaCierre.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorFechaCierre;
    }

    /**
     * Crea el componente de los documentos requeridos
     *
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     * @return Un panel con todos los componentes de los documentos requeridos
     */
    private JPanel crearComponenteDocsRequeridos(Convocatoria convocatoriaAModificar) {
        JPanel contenedorDocumentos = new JPanel(new BorderLayout());
        JLabel documentosEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[3]);
        documentosCampo = new JList<>(Convocatoria.DOCUMENTOS_OPCIONES);
        int[] indicesSeleccionados = new int[convocatoriaAModificar.getDocumentos().getDocumentosLinkedList().size()];
        int i = 0;
        for (String documento : convocatoriaAModificar.getDocumentos().getDocumentosLinkedList()) {
            indicesSeleccionados[i] = Arrays.binarySearch(Convocatoria.DOCUMENTOS_OPCIONES, documento);
            i++;
        }
        documentosCampo.setSelectedIndices(indicesSeleccionados);
        contenedorDocumentos.add(BorderLayout.WEST, documentosEtiqueta);
        contenedorDocumentos.add(BorderLayout.EAST, documentosCampo);
        contenedorDocumentos.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDocumentos;
    }

    /**
     * Crea el componente de la descripcion
     *
     * @param convocatoriaAModificar Convocatoria que se desea modificar
     * @return Un panel con todos los componentes de la descripcion
     */
    private JPanel crearComponenteDescripcion(Convocatoria convocatoriaAModificar) {
        JPanel contenedorDescripcion = new JPanel(new BorderLayout());
        JLabel descripcionEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[4]);
        descripcionArea = new JTextArea();
        descripcionArea.setColumns(Estilo.ANCHURA_DESCRIPCION_CAMPO);
        descripcionArea.setText(convocatoriaAModificar.getDescripcion());
        contenedorDescripcion.add(BorderLayout.WEST, descripcionEtiqueta);
        contenedorDescripcion.add(BorderLayout.EAST, new JScrollPane(descripcionArea));
        contenedorDescripcion.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDescripcion;
    }
}
