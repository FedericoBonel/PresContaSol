package vista.menuPrincipal;

import controlador.MenuPrincipalControlador;
import vista.Estilo;
import vista.StringsFinales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Vista del menu principal del sistema, contiene toda la informacion y componentes con los que el usuario puede interactuar
 */
public class MenuPrincipalVista {
    /**
     * Indice de panel del panel de informacion en pestanias
     */
    public static final int INDICE_PANEL_INFORMACION = 0;
    /**
     * Indice de panel de municipios en pestanias
     */
    public static final int INDICE_PANEL_MUNICIPIOS = 1;
    /**
     * Indice de panel de convocatorias en pestanias
     */
    public static final int INDICE_PANEL_CONVOCATORIAS = 2;
    /**
     * Indice de panel de presentaciones en pestanias
     */
    public static final int INDICE_PANEL_PRESENTACIONES = 3;
    /**
     * Indice de panel de usuarios en pestanias
     */
    public static final int INDICE_PANEL_USUARIOS = 4;
    /**
     * Ventana del menu principal
     */
    public JFrame ventana;
    /**
     * Boton de menu opciones salir
     */
    public JMenuItem salirBoton;
    /**
     * Componente de pestanias
     */
    public JTabbedPane tabs;
    /**
     * Panel de informacion general del sistema
     */
    public InformacionPanel panelInformacion;
    /**
     * Panel de usuarios
     */
    public UsuariosPanel panelUsuarios;
    /**
     * Panel de municipios
     */
    public MunicipiosPanel panelMunicipios;
    /**
     * Panel de convocatorias
     */
    public ConvocatoriasPanel panelConvocatorias;
    /**
     * Panel de Presentaciones
     */
    public PresentacionesPanel panelPresentaciones;
    /**
     * Controlador que gestiona a esta vista
     */
    private MenuPrincipalControlador controlador;

    /**
     * Constructor del menu principal, crea un ventana con todos los componentes necesarios para que el usuario
     * pueda utilizar el sistema
     */
    public MenuPrincipalVista() {
        // Setea los parametros basicos de la ventana
        ventana = new JFrame(StringsFinales.NOMBRE_SOFTWARE);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.setSize(Estilo.DIMENSIONES_MENU_PRINCIPAL[0], Estilo.DIMENSIONES_MENU_PRINCIPAL[1]);
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setLocationRelativeTo(null);

        // Configura la barra de menu
        JMenuBar barraMenu = crearBarraMenu();

        // Configura las pestanias
        tabs = new JTabbedPane();
        panelInformacion = new InformacionPanel();
        tabs.addTab(StringsFinales.INFORMACION, panelInformacion);
        panelMunicipios = new MunicipiosPanel();
        tabs.addTab(StringsFinales.MUNICIPIOS, panelMunicipios);
        panelConvocatorias = new ConvocatoriasPanel();
        tabs.addTab(StringsFinales.CONVOCATORIAS, panelConvocatorias);
        panelPresentaciones = new PresentacionesPanel();
        tabs.addTab(StringsFinales.PRESENTACIONES, panelPresentaciones);
        panelUsuarios = new UsuariosPanel();
        tabs.addTab(StringsFinales.USUARIOS, panelUsuarios);
        tabs.setBorder(new EmptyBorder(Estilo.ESPACIADO_ESTANDAR));

        ventana.setJMenuBar(barraMenu);
        ventana.add(BorderLayout.CENTER, tabs);
        ventana.setVisible(false);
    }

    /**
     * Crea el componente de la barra de menu
     *
     * @return Componente con la barra de menu
     */
    private JMenuBar crearBarraMenu(){
        JMenuBar barraMenu = new JMenuBar();
        JMenu opciones = new JMenu(StringsFinales.USUARIO);
        salirBoton = new JMenuItem(StringsFinales.SALIR);
        salirBoton.setActionCommand(StringsFinales.SALIR);
        opciones.add(salirBoton);
        opciones.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        barraMenu.add(Box.createHorizontalGlue());
        barraMenu.add(opciones);
        barraMenu.setBorder(new EmptyBorder(Estilo.ESPACIADO_BARRA_MENU));
        return barraMenu;
    }

    /**
     * Asigna el controlador que gestiona las interacciones de esta vista
     *
     * @param menuPrincipalControlador Controlador a ser asignado
     */
    public void addControlador(MenuPrincipalControlador menuPrincipalControlador) {
        this.controlador = menuPrincipalControlador;
        panelInformacion.addControlador(menuPrincipalControlador);
        salirBoton.addActionListener(menuPrincipalControlador);
    }
}
