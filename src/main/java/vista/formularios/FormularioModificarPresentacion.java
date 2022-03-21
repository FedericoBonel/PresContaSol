package vista.formularios;

import controlador.controladorObjetos.PresentacionesControlador;
import modelo.evento.presentacion.Presentacion;
import vista.Estilo;
import vista.StringsFinales;
import vista.componentes.ListaDinamicaCampoTexto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

/**
 * Formulario para modificar presentaciones
 */
public class FormularioModificarPresentacion {
    /**
     * Ventana del formulario
     */
    public JFrame ventana;
    /**
     * Opciones documentos requeridos
     */
    public JList<String> documentosRequeridosCampo;
    /**
     * Campo de texto de documentos adicionales
     */
    public ListaDinamicaCampoTexto documentosAdicionalesCampo;
    /**
     * Campo de entregar activado significa entregado, no activado significa no entregado
     */
    public JCheckBox campoEstadoEntrega;
    /**
     * Boton de modificar
     */
    public JButton modificarBoton;
    /**
     * Presentacion a modificar
     */
    public Presentacion presentacionAModificar;

    /**
     * Controlador que debe gestionar las interacciones de esta vista
     */
    private final PresentacionesControlador controlador;

    /**
     * Constructor de un nuevo formulario de creacion de convocatorias
     *
     * @param controlador            Controlador que debe gestionar las interacciones de este formulario
     * @param presentacionAModificar Presentacion que se desea modificar en este formulario
     */
    public FormularioModificarPresentacion(PresentacionesControlador controlador, Presentacion presentacionAModificar) {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.MODIFICAR + " " + StringsFinales.PRESENTACION);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_MODIFICAR_PRES[0], Estilo.DIMENSIONES_FORM_MODIFICAR_PRES[1]);
        ventana.setResizable(false);
        modificarBoton = new JButton(StringsFinales.MODIFICAR);

        // Componentes identificador
        JPanel contenedorId = crearComponenteIdentificador(presentacionAModificar);
        // Componentes convocatoria
        JPanel contenedorConvocatoria = crearComponenteConvocatoria(presentacionAModificar);
        // Componentes documentos requeridos
        JPanel contenedorDocumentosRequeridos = crearComponenteDocumentosRequeridos(presentacionAModificar);
        // Componente documentos adicionales
        JPanel contenedorDocsAdicionales = crearComponenteDocumentosAdicionales(presentacionAModificar);
        // Componentes estado entrega
        JPanel contenedorEstadoEntrega = crearComponenteEstadoEntrega(presentacionAModificar);

        // Setea los parametros del boton crear
        modificarBoton.setActionCommand(StringsFinales.MODIFICAR);
        modificarBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorConvocatoria);
        contenedorTodos.add(contenedorDocumentosRequeridos);
        contenedorTodos.add(contenedorDocsAdicionales);
        contenedorTodos.add(contenedorEstadoEntrega);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(modificarBoton);
        ventana.add(BorderLayout.NORTH, contenedorTodos);
        ventana.add(BorderLayout.SOUTH, modificarBoton);
        ventana.setLocationRelativeTo(null);
        // Asigna el controlador
        this.controlador = controlador;
        this.presentacionAModificar = presentacionAModificar;
    }

    /**
     * Crea el componente del identificador
     *
     * @param presentacionAModificar Presentacion que se desea modificar
     * @return Un panel con todos los compoenentes del identificador
     */
    private JPanel crearComponenteIdentificador(Presentacion presentacionAModificar) {
        JPanel contenedorId = new JPanel();
        JLabel idEtiqueta = new JLabel(presentacionAModificar.getId());
        contenedorId.add(idEtiqueta);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente de la convocatoria
     *
     * @param presentacionAModificar Presentacion que se desea modificar
     * @return Un panel con todos los componentes de la convocatoria
     */
    private JPanel crearComponenteConvocatoria(Presentacion presentacionAModificar) {
        JPanel contenedorConvocatoria = new JPanel(new BorderLayout());
        JLabel convocatoriaEtiqueta = new JLabel(StringsFinales.CONVOCATORIA);
        JLabel convocatoria = new JLabel(presentacionAModificar.getConvocatoria().getId());
        contenedorConvocatoria.add(BorderLayout.WEST, convocatoriaEtiqueta);
        contenedorConvocatoria.add(BorderLayout.EAST, convocatoria);
        contenedorConvocatoria.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorConvocatoria;
    }

    /**
     * Crea el componente de los documentos requeridos
     *
     * @param presentacionAModificar Presentacion que se desea modificar
     * @return Un panel con todos los componentes de los documentos requeridos
     */
    private JPanel crearComponenteDocumentosRequeridos(Presentacion presentacionAModificar) {
        JPanel contenedorDocsRequeridos = new JPanel(new BorderLayout());
        JLabel documentosEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[3]);
        String[] documentosRequeridos =
                presentacionAModificar.getConvocatoria().getDocumentos().getDocumentosLinkedList().toArray(new String[0]);
        Arrays.sort(documentosRequeridos);
        documentosRequeridosCampo = new JList<>(documentosRequeridos);
        int[] indicesSeleccionados = new int[presentacionAModificar.getDocumentosRequeridosEntregados().size()];
        int i = 0;
        for (String documento : presentacionAModificar.getDocumentosRequeridosEntregados()) {
            indicesSeleccionados[i] = Arrays.binarySearch(documentosRequeridos, documento);
            i++;
        }
        documentosRequeridosCampo.setSelectedIndices(indicesSeleccionados);
        contenedorDocsRequeridos.add(BorderLayout.WEST, documentosEtiqueta);
        contenedorDocsRequeridos.add(BorderLayout.EAST, documentosRequeridosCampo);
        contenedorDocsRequeridos.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDocsRequeridos;
    }

    /**
     * Crea el componente de los documentos adicionales
     *
     * @param presentacionAModificar Presentacion que se desea modificar
     * @return Un panel con todos los componentes de los documentos adicionales
     */
    private JPanel crearComponenteDocumentosAdicionales(Presentacion presentacionAModificar) {
        JPanel contenedorDocsAdicionales = new JPanel(new BorderLayout());
        JLabel documentosAdicionalesEtiqueta = new JLabel(StringsFinales.DOCS_ADICIONALES);
        documentosAdicionalesCampo = new ListaDinamicaCampoTexto(presentacionAModificar.getDocumentosAdicionalesEntregados());
        contenedorDocsAdicionales.add(BorderLayout.WEST, documentosAdicionalesEtiqueta);
        contenedorDocsAdicionales.add(BorderLayout.EAST, documentosAdicionalesCampo);
        contenedorDocsAdicionales.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDocsAdicionales;
    }

    /**
     * Crea el componente del estado de entrega
     *
     * @param presentacionAModificar Presentacion que se desea modificar
     * @return Un panel con todos los componentes del estado de entrega
     */
    private JPanel crearComponenteEstadoEntrega(Presentacion presentacionAModificar) {
        JPanel contenedorEstadoEntrega = new JPanel(new BorderLayout());
        JLabel estadoEntregaEtiqueta = new JLabel(StringsFinales.ENTREGADO);
        campoEstadoEntrega = new JCheckBox();
        campoEstadoEntrega.setSelected(!presentacionAModificar.isAbierto());
        contenedorEstadoEntrega.add(BorderLayout.WEST, estadoEntregaEtiqueta);
        contenedorEstadoEntrega.add(BorderLayout.EAST, campoEstadoEntrega);
        contenedorEstadoEntrega.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorEstadoEntrega;
    }
}
