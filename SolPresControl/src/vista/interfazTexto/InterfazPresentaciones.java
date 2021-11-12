package vista.interfazTexto;

import controlador.abstraccionNegocio.Cuentadante;
import controlador.abstraccionNegocio.Fiscal;
import controlador.abstraccionNegocio.Presentacion;
import controlador.abstraccionNegocio.Usuario;
import vista.TextosConstantes;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a presentaciones
 */
public class InterfazPresentaciones {

    /**
     * Interfaz principal del gestor de presentaciones
     */
    public static int presentacionesInterfaz(Usuario usuario, Hashtable<String, Presentacion> presentaciones){
        //Imprime las cabeceras
        System.out.println(TextosConstantes.PRESENTACIONES_CABECERA);
        System.out.println(TextosConstantes.PRESENTACIONES_GESTOR_CAMPOS);
        // Muestra el menu acordemente al usuario y devuelve su input
        return presenInterfazUsuario(usuario, presentaciones);
    }

    /**
     * Interfaz principal de presentaciones segun el tipo de usuario
     */
    public static int presenInterfazUsuario(Usuario usuario, Hashtable<String, Presentacion> presentaciones){
        // Si es el fiscal solo puede visualizar las presentaciones de sus cuentadantes
        if (usuario instanceof Fiscal fiscal) {
            presenInterfazFiscal(fiscal, presentaciones);
            return 0;
        }
        // Todos los usuarios podran modificar
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add(TextosConstantes.GESTOR_MODIFICAR);

        // Los administradores y fiscales podran visualizar TODAS las presentaciones
        if (usuario.getTipo().equals("FiscalGeneral") || usuario.getTipo().equals("Administrador")) {
            for (String identificador : presentaciones.keySet())
                InterfazInformacion.imprimirEvento(presentaciones.get(identificador));
            // Los administradores pueden eliminar presentaciones
            if (usuario.getTipo().equals("Administrador")) opciones.add(TextosConstantes.GESTOR_ELIMINAR);
        }

        //Si es cuentadante entonces solo imprime sus presentaciones
        if (usuario instanceof Cuentadante cuentadante){
            for (String identificador : cuentadante.getPresentaciones().keySet())
                InterfazInformacion.imprimirEvento(presentaciones.get(identificador));
            // Los cuentadantes pueden eliminar como crear presentaciones
            opciones.add(TextosConstantes.GESTOR_ELIMINAR);
            opciones.add(TextosConstantes.GESTOR_CREAR);
        }

        opciones.add(TextosConstantes.MENU_TEXT_SALIR);
        return InterfacesGenerales.interfazImprimirTodoYPedirEntero(opciones.toArray(new String[0]));
    }

    /**
     * Interfaz del gestor de presentaciones para fiscales
     */
    public static void presenInterfazFiscal(Fiscal fiscal, Hashtable<String, Presentacion> presentaciones) {
        // Por cada cuentadante que tenga asignado imprime cada una de sus presentaciones
        Hashtable<String, Cuentadante> cuentadantesFiscal = fiscal.getCuentadantes();
        for (String identificadorCuentadante : cuentadantesFiscal.keySet()) {
            Hashtable<String, Presentacion> presentacionesCuenta = cuentadantesFiscal.get(identificadorCuentadante).getPresentaciones();
            for (String presentacion : presentacionesCuenta.keySet())
                // Imprimela en pantalla
                InterfazInformacion.imprimirEvento(presentaciones.get(presentacion));
        }
    }

    /**
     * Interfaz de modificacion de presentaciones, muestra los atributos a modificar, toma una opcion y la devuelve
     */
    public static int modificarPresentacionInterfaz(Usuario usuario){
        ArrayList<String> opciones = new ArrayList<>();
        //Pide el input acordemente al usuario y devuelvelo
        System.out.println(TextosConstantes.PRESENTACIONES_MODIFICAR_CABECERA);
        // Todos pueden modificar el estado
        opciones.add(TextosConstantes.PRESENTACIONES_MODIFICAR_APERTURA_ESTADO);
        // Si es un cuentadante o administrador muestra todas las opciones de modificacion
        if (usuario.getTipo().equals("Cuentadante") || usuario.getTipo().equals("Administrador")) {
            opciones.add(TextosConstantes.PRESENTACIONES_MODIFICAR_DOCUMENTOS);
        }
        opciones.add(TextosConstantes.MENU_TEXT_SALIR);
        return InterfacesGenerales.interfazImprimirTodoYPedirEntero(opciones.toArray(new String[0]));
    }
}
