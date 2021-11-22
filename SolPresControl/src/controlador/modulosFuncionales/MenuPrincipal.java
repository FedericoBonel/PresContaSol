package controlador.modulosFuncionales;

import controlador.abstraccionNegocio.Convocatoria;
import controlador.abstraccionNegocio.Municipio;
import controlador.abstraccionNegocio.Presentacion;
import controlador.abstraccionNegocio.Usuario;
import controlador.herramientas.InicializadoresVariables;
import controlador.modulosFuncionales.gestores.GestorConvocatorias;
import controlador.modulosFuncionales.gestores.GestorMunicipios;
import controlador.modulosFuncionales.gestores.GestorPresentaciones;
import controlador.modulosFuncionales.gestores.GestorUsuarios;
import vista.TextosConstantes;
import vista.interfazTexto.UIInformacion;
import vista.interfazTexto.UIMenuPrincipal;

import java.util.Hashtable;

/**
 * Clase que contiene el modulo que gestiona el menu principal
 */

public class MenuPrincipal {
    public static void menuPrincipal(Usuario usuario, Hashtable<String, Usuario> usuarios, Hashtable<String, Municipio> municipios) {
        // Variables
        Hashtable<String, Presentacion> presentaciones = new Hashtable<>();
        Hashtable<String, Convocatoria> convocatorias = new Hashtable<>();
        // Inicializacion
        InicializadoresVariables.inicializarConvocatorias(convocatorias);
        InicializadoresVariables.inicializarPresentaciones(presentaciones, convocatorias, usuarios);
        int input = -1;

        //Pide input y los modulos funcionales requeridos
        while (input != 0) {
            input = UIMenuPrincipal.interfazMenuPrincipal(usuario);
            switch (input) {
                case 1 -> GestorConvocatorias.gestorConvocatorias(usuario, convocatorias, presentaciones);
                case 2 -> GestorPresentaciones.gestorPresentaciones(usuario, convocatorias, presentaciones);
                case 3 -> GestorMunicipios.gestorMunicipios(usuario, municipios, usuarios, presentaciones);
                case 4 -> UIInformacion.interfazInfoGnral(convocatorias, municipios);
                case 5 -> GestorUsuarios.gestorUsuario(usuario, usuarios, presentaciones);
            }
        }
        System.out.println(TextosConstantes.SALIENDO_DEL_SISTEMA);
    }
}
