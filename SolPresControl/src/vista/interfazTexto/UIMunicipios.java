package vista.interfazTexto;

import controlador.abstraccionNegocio.*;
import vista.TextosConstantes;

import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a municipios
 */
public class UIMunicipios {

    /**
     * Interfaz del gestor de Municipios, imprime municipios segun usuario, toma opcion, la valida y la devuelve
     */
    public static int interfazMunicipios(Usuario usuario, Hashtable<String, Municipio> municipios) {
        System.out.println(TextosConstantes.MUNICIPIOS_GESTOR_CABECERA);
        System.out.println(TextosConstantes.MUNICIPIOS_GESTOR_CAMPOS);
        //Si es un usuario cuentadante solo imprime su municipio
        if (usuario instanceof Cuentadante cuentadante) {
            // Asegurate que tenga municipio
            if (cuentadante.hasMunicipio()) System.out.println(cuentadante.getMunicipio().toString());
        // De lo contrario imprime todos los municipios
        } else {
            for (String identificador : municipios.keySet())
                System.out.println(municipios.get(identificador).toString());
            // Solo si el usuario es administrador imprime las opciones de gestion
            if (usuario.getTipo().equals("Administrador")) {
                int input;
                // Pide la entrada
                input = UIGenerales.interfazMostrarOpcionesPedirEntero(new String[]{TextosConstantes.GESTOR_MODIFICAR,
                        TextosConstantes.GESTOR_ELIMINAR,
                        TextosConstantes.GESTOR_CREAR,
                        TextosConstantes.MENU_SALIR});
                return input;
            }
        }
        // Si no es un usuario con permisos devuelvelo al menu anterior
        return 0;
    }

    /**
     * Interfaz del modificador de municipios, imprime las opciones, toma una, la valida y la devuelve
     */
    public static int modificarMunicipioInterfaz() {
        System.out.println(TextosConstantes.MUNICIPIOS_MODIFICAR_CABECERA);
        return UIGenerales.interfazMostrarOpcionesPedirEntero(new String[]{TextosConstantes.MUNICIPIOS_MODIFICAR_CATEGORIA,
                TextosConstantes.MUNICIPIOS_MODIFICAR_CUENTADANTE,
                TextosConstantes.MENU_SALIR});
    }

}
