package controlador.herramientas;

import controlador.abstraccionNegocio.*;
import controlador.modulosFuncionales.gestores.GestorUsuarios;
import vista.TextosConstantes;
import vista.interfazTexto.UIGenerales;

import java.util.Hashtable;

/**
 * Clase que contiene operaciones que son generales a varios objetivos
 */
public class OperacionesGenerales {

    /**
     * Metodo que pide y modifica los documentos del usuario
     */

    public static void modificarDocumentos(Usuario usuario, Evento evento) {
        // Imprime el menu y lanza el modulo que corresponda
        System.out.println(TextosConstantes.MODIFICAR_DOCUMENTOS_CABECERA);
        int input = UIGenerales.interfazMostrarOpcionesPedirEntero(new String[]{TextosConstantes.AGREGAR_OPCION,
                TextosConstantes.BORRAR_OPCION,
                TextosConstantes.MENU_SALIR});
        switch (input) {
            case 1 -> agregarDocumento(evento.getDocumentos(), usuario);
            case 2 -> eliminarDocumento(evento, usuario);
        }
    }

    /**
     * Metodo para agregar documentos nuevos al hashtable pasado segun el usuario, los pide, los verifica y los asigna
     */
    public static void agregarDocumento(Hashtable<String, Boolean> documentos, Usuario usuario) {
        String documentoNuevo;
        //Pide documentos hasta que el usuario ingrese el string vacio, y verifica el usuario que lo vaya a hacer
        while (true) {
            documentoNuevo = UIGenerales.pedirDocumento(documentos);
            if (documentoNuevo.isEmpty()) break;
            if (usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("FiscalGeneral")) {
                //Si el documento no se encuentra en la lista de opciones es invalido para estos tipos de usuario
                if (!Verificadores.arrayContieneString(documentoNuevo, Evento.DOCUMENTOS_OPCIONES)) {
                    System.out.println("\n" + TextosConstantes.DOCUMENTO_NO_EN_LISTA);
                    continue;
                }
            }
            //En caso contrario el usuario es cuentadante y puede agregar documentos adicionales a su presentacion
            documentos.put(documentoNuevo, true);
            System.out.println(TextosConstantes.EXITO_OPERACION);
            System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
        }
    }

    /**
     * Metodo para eliminar documentos de un evento, los pide, los verifica y los elimina
     */
    public static void eliminarDocumento(Evento evento, Usuario usuario) {
        String documento;
        // Pide los documentos y verifica el usuario que realizara la operacion
        while (true) {
            documento = UIGenerales.pedirDocumento(evento.getDocumentos());
            //Verifica si el usuario es cuentadante
            if (usuario instanceof Cuentadante) {
                Presentacion presentacion = (Presentacion) evento;
                //Si el documento no es adicional no se puede remover
                if (!presentacion.removeDocumentoAdicional(documento)) {
                    System.out.println("\n" + TextosConstantes.DOCUMENTO_NO_ADICIONAL);
                    continue;
                }
            // En caso contrario el usuario es Administrador o FiscalGeneral
            } else {
                // Pueden deshabilitar documentos SOLO de la lista de opciones
                if (!Verificadores.arrayContieneString(documento, Evento.DOCUMENTOS_OPCIONES)) {
                    System.out.println("\n" + TextosConstantes.DOCUMENTO_NO_EN_LISTA);
                    continue;
                } else {
                    // Si esta en la lista de opciones deshabilitalo, ya no es requerido
                    evento.addDocumento(documento, false);
                }
            }
            System.out.println(TextosConstantes.EXITO_OPERACION);
            break;
        }
    }

    /**
     * Metodo que cambia el estado de apertura del evento segun corresponda
     */
    public static void modificarApertura(Usuario usuario, Evento evento) {
        // Si el usuario es Administrador o Fiscal gral complementa el estado del evento
        if (usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("FiscalGeneral")) {
            evento.setAbierto(!evento.isAbierto());
            System.out.println(TextosConstantes.EXITO_OPERACION);
            return;
        }
        // En caso contrario, verifica si el evento esta abierto o no
        if (evento.isAbierto()) {
            // Verificar si todos los documentos requeridos son entregados
            Presentacion presentacion = (Presentacion) evento;
            if (!presentacion.todosRequeridosEntregados()) {
                // Si no lo son verificar que usuario confirma operacion
                System.out.println(TextosConstantes.FALTAN_DOCUMENTOS);
                if (!UIGenerales.interfazSiONo(TextosConstantes.CONFIRMAR_OPERACION)) return;
            }
            evento.setAbierto(false);
            System.out.println(TextosConstantes.EXITO_OPERACION);
        } else {
            System.out.println(TextosConstantes.PRESENTACION_CERRADA);
        }
    }

    /**
     * Metodo que toma un fiscal o municipio y asigna un nuevo cuentadante a ella
     * Pide identficador del cuentadante, lo valida y lo asigna
     */
    public static void actualizarNuevoCuentadante(Entidad entidad, Hashtable<String, Usuario> usuarios) {
        while (true) {
            // Pide el identificador de cuentadante y validalo
            Usuario usuario = GestorUsuarios.pedirIdUsuario(usuarios);
            if (usuario == null) return;
            if (!(usuario instanceof Cuentadante cuentadante)) {
                System.out.println(TextosConstantes.IDENTIFICADOR_NO_CUENTADANTE);
                continue;
            }
            // Si la entidad es fiscal, entonces asignalo con su metodo
            if (entidad instanceof Fiscal) {
                OperacionesRelaciones.actualizaFiscalACuenta((Fiscal) entidad, cuentadante);
                // Si no, es un municipio
            } else {
                OperacionesRelaciones.actualizaMuniACuenta(cuentadante, (Municipio) entidad);
            }
            break;
        }
    }

}
