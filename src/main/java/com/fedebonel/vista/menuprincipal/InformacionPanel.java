package com.fedebonel.vista.menuprincipal;

import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.vista.Estilo;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.componentes.JTableNoEditable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Panel del menu principal que contiene la informacion general del sistema
 */
public class InformacionPanel extends JPanel {
    /**
     * Boton Actualizar
     */
    public final JButton actualizarBoton;
    /**
     * Tabla contenedora de todos los datos de los municipios a mostrar
     */
    public final JTableNoEditable tablaObjetos;
    /**
     * Etiqueta contenedora del total de presentaciones realizadas a convocatorias abiertas
     */
    public JLabel totalPresConvoAbiertasCampo;
    /**
     * Etiqueta contenedora del total de presentaciones realizadas a convocatorias cerradas
     */
    public JLabel totalPresConvoCerradasCampo;
    /**
     * Controlador que maneja las interacciones realizadas por el usuario
     */
    private ActionListener controlador;


    /**
     * Crea un panel para mostrar la informacion del sistema
     */
    public InformacionPanel() {
        super(new BorderLayout());
        // Tabla de municipios
        tablaObjetos = new JTableNoEditable(StringsFinales.COLUMNAS_MUNICIPIO_INFORMACION);
        // Componente presentaciones a convocatorias abiertas
        JPanel contenedorPresAConvoAbiertas = crearComponenteTotalAbiertas();
        // Componente presentaciones a convocatorias cerradas
        JPanel contenedorPresAConvoCerradas = crearComponenteTotalCerradas();
        // Componente presentaciones
        JPanel contenedorTotales = new JPanel();
        BoxLayout plantillaTotales = new BoxLayout(contenedorTotales, BoxLayout.Y_AXIS);
        contenedorTotales.setLayout(plantillaTotales);
        contenedorTotales.add(contenedorPresAConvoAbiertas);
        contenedorTotales.add(contenedorPresAConvoCerradas);
        contenedorTotales.setBorder(BorderFactory.createEtchedBorder());
        // Boton de actualizar
        actualizarBoton = new JButton(StringsFinales.ACTUALIZAR);
        actualizarBoton.setActionCommand(StringsFinales.ACTUALIZAR);
        // Agrega todos los componentes al panel general
        super.add(BorderLayout.NORTH, actualizarBoton);
        super.add(BorderLayout.CENTER, new JScrollPane(tablaObjetos));
        super.add(BorderLayout.SOUTH, contenedorTotales);
    }

    /**
     * Metodo que toma las presentaciones y municipios, extrae la informacion y la presenta en el panel
     *
     * @param municipios     Municipios con la informacion a mostrar
     * @param presentaciones Presentaciones con la informacion a mostrar
     */
    public void mostrarInformacion(LinkedList<Municipio> municipios, LinkedList<Presentacion> presentaciones) {
        // Informacion de municipios
        String[][] tablaAMostrar =
                new String[municipios.size()][StringsFinales.COLUMNAS_MUNICIPIO_INFORMACION.length];
        int i = 0;
        for (Municipio municipio : municipios) {
            tablaAMostrar[i][0] = municipio.getId();
            tablaAMostrar[i][1] = municipio.getNombre();
            tablaAMostrar[i][2] = String.valueOf(municipio.getSusPresentacionesDe(presentaciones).size());
            tablaAMostrar[i][3] = String.valueOf(municipio.getTotalDocumentosPresentados(presentaciones));
            i++;
        }
        tablaObjetos.actualizarCon(tablaAMostrar);
        // Informacion de presentaciones
        int totalPresentacionesAbiertas = 0;
        int totalPresentacionesCerradas = 0;
        for (Presentacion presentacion : presentaciones) {
            if (presentacion.getConvocatoria().isAbierto()) totalPresentacionesAbiertas += 1;
            else totalPresentacionesCerradas += 1;
        }
        totalPresConvoAbiertasCampo.setText(String.valueOf(totalPresentacionesAbiertas));
        totalPresConvoCerradasCampo.setText(String.valueOf(totalPresentacionesCerradas));
    }

    /**
     * Crea el componente del total de presentaciones realizadas a convocatorias abiertas
     *
     * @return Componente de presentaciones a convocatorias abiertas
     */
    private JPanel crearComponenteTotalAbiertas() {
        JPanel totalPresConvoAbiertasContenedor = new JPanel(new BorderLayout());
        JLabel totalPresConvoAbiertasEtiqueta = new JLabel(StringsFinales.TOTAL_PRESEN_CONVO_ABIERTAS);
        totalPresConvoAbiertasCampo = new JLabel();
        totalPresConvoAbiertasContenedor.add(BorderLayout.WEST, totalPresConvoAbiertasEtiqueta);
        totalPresConvoAbiertasContenedor.add(BorderLayout.EAST, totalPresConvoAbiertasCampo);
        totalPresConvoAbiertasContenedor.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return totalPresConvoAbiertasContenedor;
    }

    /**
     * Crea el componente del total de presentaciones realizadas a convocatorias cerradas
     *
     * @return Componente de presentaciones a convocatorias cerradas
     */
    private JPanel crearComponenteTotalCerradas() {
        JPanel totalPresConvoCerradasContenedor = new JPanel(new BorderLayout());
        JLabel totalPresConvoCerradasEtiqueta = new JLabel(StringsFinales.TOTAL_PRES_CONVO_CERRADAS);
        totalPresConvoCerradasCampo = new JLabel();
        totalPresConvoCerradasContenedor.add(BorderLayout.WEST, totalPresConvoCerradasEtiqueta);
        totalPresConvoCerradasContenedor.add(BorderLayout.EAST, totalPresConvoCerradasCampo);
        totalPresConvoCerradasContenedor.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));
        return totalPresConvoCerradasContenedor;
    }

    /**
     * Agrega el controlador que manejara los eventos de interaccion
     *
     * @param controlador Controlador a gestionar los eventos de interaccion de esta vista
     */
    public void addControlador(ActionListener controlador) {
        this.controlador = controlador;
        actualizarBoton.addActionListener(controlador);
    }
}
