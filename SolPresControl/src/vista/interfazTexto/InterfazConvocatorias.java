package vista.interfazTexto;

import controlador.abstraccionNegocio.Convocatoria;
import controlador.abstraccionNegocio.Usuario;
import vista.TextosConstantes;

import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a convocatorias
 */

public class InterfazConvocatorias {
    /**
     * Interfaz del gestor de convocatorias, imprime las convocatorias, toma input, lo valida y lo devuelve
     */
    public static int convocatoriasInterfaz(Usuario usuario, Hashtable<String, Convocatoria> convocatorias){
        System.out.println(TextosConstantes.CONVOCATORIAS_CABECERA);
        System.out.println(TextosConstantes.CONVOCATORIAS_GESTOR_CAMPOS);

        for (String identificador : convocatorias.keySet()) {
            // Si el usuario es cuentadante solo imprime las convocatorias abiertas
            if (usuario.getTipo().equals("Cuentadante") && convocatorias.get(identificador).getEstado()) {
                InterfazInformacion.imprimirEvento(convocatorias.get(identificador));
            } else if (!usuario.getTipo().equals("Cuentadante")) {
                //Si no imprime todas
                InterfazInformacion.imprimirEvento(convocatorias.get(identificador));
            }
        }
        //Si es un usuario administrador o fiscal general puede realizar operaciones
        if (usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("FiscalGeneral")) {
            int input;
            // Pide la entrada
            input = InterfacesGenerales.interfazImprimirTodoYPedirEntero(new String[]{TextosConstantes.GESTOR_MODIFICAR,
                    TextosConstantes.GESTOR_ELIMINAR,
                    TextosConstantes.GESTOR_CREAR,
                    TextosConstantes.MENU_TEXT_SALIR});
            return input;

        }
        // Si no es un usuario con permisos devuelvelo al menu anterior
        return 0;
    }

    /**
     * Interfaz de modificacion de convocatorias, muestra los atributos a modificar, toma una opcion y la devuelve
     */
    public static int modificarConvocatoriasInterfaz(){
        int input;
        //Pide el input y devuelvelo
        System.out.println(TextosConstantes.CONVOCATORIAS_MODIFICAR_CABECERA);
        input = InterfacesGenerales.interfazImprimirTodoYPedirEntero(new String[]{TextosConstantes.CONVOCATORIAS_MODIFICAR_APERTURA,
        TextosConstantes.CONVOCATORIAS_MODIFICAR_CIERRE,
        TextosConstantes.CONVOCATORIAS_MODIFICAR_DOCUMENTOS,
        TextosConstantes.CONVOCATORIAS_MODIFICAR_APERTURA_ESTADO,
        TextosConstantes.MENU_TEXT_SALIR});
        return input;

    }
}
