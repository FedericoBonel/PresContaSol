package com.fedebonel.vista.formularios.modificacion;

import com.fedebonel.controlador.controladorobjetos.MunicipiosControlador;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.vista.Estilo;
import com.fedebonel.vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

/**
 * Formulario para modificar municipios
 */
public class FormularioModificarMunicipio {
    /**
     * Ventana del formulario
     */
    public final JFrame ventana;
    /**
     * Boton de modificar
     */
    public final JButton modificarBoton;
    /**
     * Municipio a ser modificado en este formulario
     */
    public final Municipio municipioAModificar;
    /**
     * Opciones para la categoria
     */
    public JComboBox<Integer> categoriaCampo;
    /**
     * Opciones para los representantes (Sus identificadores)
     */
    public JComboBox<String> representanteCampo;
    /**
     * Opciones para los supervisores (Sus identificadores)
     */
    public JComboBox<String> supervisorCampo;

    /**
     * Constructor de un nuevo formulario de modificacion de municipios
     *
     * @param controlador         controlador que debe gestionar las interacciones de este formulario
     * @param municipioAModificar Municipio a modificar
     * @param representantes      Representantes disponibles para el municipio
     * @param supervisores        Supervisores disponibles para el municipio
     */
    public FormularioModificarMunicipio(MunicipiosControlador controlador,
                                        Municipio municipioAModificar,
                                        LinkedList<Usuario> representantes,
                                        LinkedList<Usuario> supervisores) {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.MODIFICAR + " " + StringsFinales.MUNICIPIO);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_FORM_MODIFICAR_MUNI[0], Estilo.DIMENSIONES_FORM_MODIFICAR_MUNI[1]);
        ventana.setResizable(false);

        // Etiqueta identificador
        JPanel contenedorId = crearComponenteIdentificador(municipioAModificar);
        // Componentes categoria
        JPanel contenedorCategoria = crearComponenteCategoria(municipioAModificar);
        // Componentes supervisor
        JPanel contenedorSupervisor = crearComponenteSupervisor(municipioAModificar, supervisores);
        // Componentes representante
        JPanel contenedorRepresentante = crearComponenteRepresentante(municipioAModificar, representantes);

        // Setea los parametros del boton aceptar
        modificarBoton = new JButton(StringsFinales.MODIFICAR);
        modificarBoton.setActionCommand(StringsFinales.MODIFICAR);
        modificarBoton.addActionListener(controlador);

        // Contenedor de todos los componentes
        JPanel contenedorTodos = new JPanel();
        BoxLayout plantillaTodos = new BoxLayout(contenedorTodos, BoxLayout.Y_AXIS);
        contenedorTodos.setLayout(plantillaTodos);
        contenedorTodos.add(contenedorId);
        contenedorTodos.add(contenedorCategoria);
        contenedorTodos.add(contenedorSupervisor);
        contenedorTodos.add(contenedorRepresentante);
        // Agrega los componentes a la ventana
        ventana.getRootPane().setDefaultButton(modificarBoton);
        ventana.add(BorderLayout.CENTER, contenedorTodos);
        ventana.add(BorderLayout.SOUTH, modificarBoton);
        ventana.setLocationRelativeTo(null);
        // Asigna el usuario
        this.municipioAModificar = municipioAModificar;
    }


    /**
     * Crea el componente del identificador
     *
     * @param municipioAModificar Municipio que se desea modificar
     * @return Un panel con todos los componentes del identificador
     */
    private JPanel crearComponenteIdentificador(Municipio municipioAModificar) {
        JPanel contenedorId = new JPanel();
        JLabel idEtiqueta = new JLabel(municipioAModificar.getId());
        contenedorId.add(idEtiqueta);
        contenedorId.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorId;
    }

    /**
     * Crea el componente de la categoria
     *
     * @param municipioAModificar Municipio que se desea modificar
     * @return Un panel con todos los componentes de la categoria
     */
    private JPanel crearComponenteCategoria(Municipio municipioAModificar) {
        JPanel contenedorCategoria = new JPanel(new BorderLayout());
        JLabel categoriaEtiqueta = new JLabel(StringsFinales.COLUMNAS_MUNICIPIO[2]);
        Integer[] opcionesCategoria = new Integer[Municipio.LIMITE_CATEGORIA_SUPERIOR];
        for (int i = 0; i < Municipio.LIMITE_CATEGORIA_SUPERIOR; i++) opcionesCategoria[i] = i;
        categoriaCampo = new JComboBox<>(opcionesCategoria);
        categoriaCampo.setSelectedItem(municipioAModificar.getCategoria());
        contenedorCategoria.add(BorderLayout.WEST, categoriaEtiqueta);
        contenedorCategoria.add(BorderLayout.EAST, categoriaCampo);
        contenedorCategoria.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorCategoria;
    }

    /**
     * Crea el componente para el supervisor
     *
     * @param municipioAModificar Municipio que se desea modificar
     * @param supervisores        Lista de supervisores disponibles
     * @return El panel con la informacion correspondiente al supervisor del municipio a modificar
     */
    private JPanel crearComponenteSupervisor(Municipio municipioAModificar, LinkedList<Usuario> supervisores) {
        JPanel contenedorSupervisor = new JPanel(new BorderLayout());
        JLabel supervisorEtiqueta = new JLabel(StringsFinales.COLUMNAS_MUNICIPIO[3]);
        String[] opcionesSupervisor = new String[supervisores.size() + 1];
        opcionesSupervisor[0] = StringsFinales.NINGUNO;
        int i = 1;
        for (Usuario usuario : supervisores) {
            opcionesSupervisor[i] = usuario.getId();
            i++;
        }
        supervisorCampo = new JComboBox<>(opcionesSupervisor);
        if (municipioAModificar.getFiscal() != null)
            supervisorCampo.setSelectedItem(municipioAModificar.getFiscal().getId());
        else supervisorCampo.setSelectedItem(StringsFinales.NINGUNO);
        contenedorSupervisor.add(BorderLayout.WEST, supervisorEtiqueta);
        contenedorSupervisor.add(BorderLayout.EAST, supervisorCampo);
        contenedorSupervisor.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorSupervisor;
    }

    /**
     * Crea el componente para el representante
     *
     * @param municipioAModificar Municipio que se desea modificar
     * @param representantes      Lista de representantes disponibles
     * @return Un panel con la informacion del representante del municipio a modificar
     */
    private JPanel crearComponenteRepresentante(Municipio municipioAModificar, LinkedList<Usuario> representantes) {
        JPanel contenedorRepresentante = new JPanel(new BorderLayout());
        JLabel representanteEtiqueta = new JLabel(StringsFinales.COLUMNAS_MUNICIPIO[4]);
        String[] opcionesRepresentante = new String[representantes.size() + 1];
        opcionesRepresentante[0] = StringsFinales.NINGUNO;
        int i = 1;
        for (Usuario usuario : representantes) {
            opcionesRepresentante[i] = usuario.getId();
            i++;
        }
        representanteCampo = new JComboBox<>(opcionesRepresentante);
        if (municipioAModificar.getCuentadante() != null)
            representanteCampo.setSelectedItem(municipioAModificar.getCuentadante().getId());
        else representanteCampo.setSelectedItem(StringsFinales.NINGUNO);
        contenedorRepresentante.add(BorderLayout.WEST, representanteEtiqueta);
        contenedorRepresentante.add(BorderLayout.EAST, representanteCampo);
        contenedorRepresentante.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return contenedorRepresentante;
    }
}
