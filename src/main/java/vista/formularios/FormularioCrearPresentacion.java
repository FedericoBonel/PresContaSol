package vista.formularios;

import controlador.controladorObjetos.PresentacionesControlador;
import modelo.evento.convocatoria.Convocatoria;
import vista.Estilo;
import vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.LinkedList;

/**
 * Formulario para crear presentaciones
 */
public class FormularioCrearPresentacion {
    /**
     * Ventana del formulario
     */
    public JFrame ventana;
    /**
     * Campo de texto de identificador (Se usan los limites de usuario por cuestiones visuales)
     */
    public JTextField idCampo;
    /**
     * Opciones de convocatoria (Sus identificadores)
     */
    public JComboBox<String> convocatoriaCampo;
    /**
     * Datos de las opciones de documentos requeridos
     */
    private DefaultListModel<String> documentosRequeridosDatos;
    /**
     * Campo que muestra las opciones de los documentos requeridos
     */
    public JList<String> documentosRequeridosCampo;
    /**
     * Boton de crear
     */
    public JButton crearBoton;

    /**
     * Controlador que debe gestionar las interacciones de esta vista
     */
    private PresentacionesControlador controlador;

    /**
     * Constructor de un nuevo formulario de creacion de convocatorias
     *
     * @param controlador Controlador que debe gestionar las interacciones de este formulario
     * @param convocatorias Lista de convocatorias abiertas actualmente
     */
    public FormularioCrearPresentacion(PresentacionesControlador controlador, LinkedList<Convocatoria> convocatorias){
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.CREAR + " " + StringsFinales.PRESENTACION);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_CREAR_PRES[0], Estilo.DIMENSIONES_FORM_CREAR_PRES[1]);
        ventana.setResizable(false);

        // Inicializa el contenedor de los datos de los documentos requeridos
        documentosRequeridosDatos = new DefaultListModel<>();

        // Componentes identificador
        JPanel contenedorId = crearComponenteIdentificador();
        // Componentes convocatoria
        JPanel contenedorConvocatoria = crearComponenteConvocatoria(convocatorias);
        // Componentes documentos requeridos
        JPanel contenedorDocumentosRequeridos = crearComponenteDocumentosRequeridos();

        // Setea los parametros del boton crear
        crearBoton = new JButton(StringsFinales.CREAR);
        crearBoton.setActionCommand(StringsFinales.CREAR);
        crearBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorConvocatoria);
        contenedorTodos.add(contenedorDocumentosRequeridos);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(crearBoton);
        ventana.add(BorderLayout.NORTH, contenedorTodos);
        ventana.add(BorderLayout.SOUTH, crearBoton);
        ventana.setLocationRelativeTo(null);
        // Asigna el controlador
        this.controlador = controlador;
    }

    /**
     * Crea el componente del identificador
     *
     * @return Un panel con todos los compoenentes del identificador
     */
    private JPanel crearComponenteIdentificador() {
        JPanel contenedorId = new JPanel(new BorderLayout());
        JLabel idEtiqueta = new JLabel(StringsFinales.COLUMNAS_PRESENTACIONES[0]);
        idCampo = new JTextField(Estilo.ANCHURA_CAMPO_TEXTO_ESTANDAR);
        contenedorId.add(BorderLayout.WEST, idEtiqueta);
        contenedorId.add(BorderLayout.EAST, idCampo);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente de la convocatoria
     *
     * @param convocatorias Lista de Convocatorias abiertas actualmente
     * @return Un panel con todos los componentes de la convocatoria
     */
    private JPanel crearComponenteConvocatoria(LinkedList<Convocatoria> convocatorias) {
        JPanel contenedorConvocatoria = new JPanel(new BorderLayout());
        JLabel convocatoriaEtiqueta = new JLabel(StringsFinales.CONVOCATORIA);
        String[] opcionesConvocatoria = new String[convocatorias.size()];
        int i = 0;
        for (Convocatoria convocatoria : convocatorias) {
            opcionesConvocatoria[i] = convocatoria.getId();
            i++;
        }
        convocatoriaCampo = new JComboBox<>(opcionesConvocatoria);
        convocatoriaCampo.setSelectedIndex(-1);
        contenedorConvocatoria.add(BorderLayout.WEST, convocatoriaEtiqueta);
        contenedorConvocatoria.add(BorderLayout.EAST, convocatoriaCampo);
        contenedorConvocatoria.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        // Listener para cambiar los documentos a seleccionar con cada convocatoria elegida
        convocatoriaCampo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                LinkedList<String> documentosOpciones = new LinkedList<>();
                for (Convocatoria convocatoria : convocatorias) {
                    if (convocatoria.getId().equals(convocatoriaCampo.getSelectedItem())) {
                        documentosOpciones = convocatoria.getDocumentos().getDocumentosLinkedList();
                        break;
                    }
                }
                documentosRequeridosDatos.removeAllElements();
                documentosRequeridosDatos.addAll(documentosOpciones);
            }
        });
        return contenedorConvocatoria;
    }

    /**
     * Crea el componente de los documentos requeridos
     *
     * @return Un panel con todos los componentes de los documentos requeridos
     */
    private JPanel crearComponenteDocumentosRequeridos() {
        JPanel contenedorDocsRequeridos = new JPanel(new BorderLayout());
        JLabel documentosRequeridosEtiqueta = new JLabel(StringsFinales.COLUMNAS_CONVOCATORIAS[3]);
        documentosRequeridosCampo = new JList<>(documentosRequeridosDatos);
        contenedorDocsRequeridos.add(BorderLayout.WEST, documentosRequeridosEtiqueta);
        contenedorDocsRequeridos.add(BorderLayout.EAST, documentosRequeridosCampo);
        contenedorDocsRequeridos.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorDocsRequeridos;
    }
}
