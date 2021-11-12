package controlador.herramientas;

import controlador.abstraccionNegocio.*;
import controlador.modulosFuncionales.gestores.GestorUsuario;
import vista.TextosConstantes;
import vista.interfazTexto.InterfacesGenerales;

import java.time.LocalDate;
import java.util.Hashtable;

/**
 * Clase que contiene operaciones que son generales a varios objetivos
 */
public class OperacionesGenerales {

    /**
     * Metodo que pide y modifica los documentos del usuario
     */

    public static void modificarDocumentos(Usuario usuario, Evento evento) {
        int input = -1;
        while (input != 0) {
            // Imprime el menu y lanza el modulo que corresponda
            System.out.println(TextosConstantes.MODIFICAR_DOCUMENTOS_CABECERA);
            input = InterfacesGenerales.agregarOBorrar();
            switch (input) {
                case 1 -> agregarDocumento(evento.getDocumentos(), usuario);
                case 2 -> eliminarDocumento(evento, usuario);
            }
            input = 0;
        }
    }

    /**
     * Metodo para agregar documentos nuevos al hashtable pasado segun el usuario, los pide, los verifica y los asigna
     */
    public static void agregarDocumento(Hashtable<String, Boolean> documentos, Usuario usuario) {
        String documentoNuevo;
        //Pide documentos hasta que el usuario ingrese el string vacio, y verifica el usuario que lo vaya a hacer
        while (true) {
            documentoNuevo = InterfacesGenerales.pedirDocumento(documentos);
            if (documentoNuevo.isEmpty()) break;
            if (usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("FiscalGeneral")) {
                //Si el documento no se encuentra en la lista de opciones es invalido para estos tipos de usuario
                if (!Verificadores.arrayContainsString(documentoNuevo, Evento.DOCUMENTOS_OPCIONES)) {
                    System.out.println("\n" + TextosConstantes.DOCUMENTO_NO_EN_LISTA);
                    continue;
                }
            }
            //En caso contrario el usuario es cuentadante y puede agregar documentos adicionales a su presentacion
            //Agrega el documento
            documentos.put(documentoNuevo, true);
            //Operacion llega a su final correctamente
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
            documento = InterfacesGenerales.pedirDocumento(evento.getDocumentos());
            //Verifica si el usuario es cuentadante
            if (usuario instanceof Cuentadante) {
                Presentacion pres = (Presentacion) evento;
                //Si el documento no es adicional no se pudo remover
                if (!pres.removeDocumentoAdicional(documento)) {
                    System.out.println("\n" + TextosConstantes.DOCUMENTO_NO_ADICIONAL);
                    continue;
                }
                // En caso contrario el usuario es Administrador o FiscalGeneral
                // Pueden deshabilitar documentos de la lista de opciones
            } else {
                if (!Verificadores.arrayContainsString(documento, Evento.DOCUMENTOS_OPCIONES)) {
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
     * Metodo que cambia el estado del evento segun corresponda
     */
    public static void modificarEstado(Usuario usuario, Evento evento) {
        // Si el usuario es Administrador o Fiscal gral complementa el estado del evento
        if (usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("FiscalGeneral")) {
            evento.setEstado(!evento.getEstado());
            System.out.println(TextosConstantes.EXITO_OPERACION);
            return;
        }
        // En caso contrario, verifica si el evento esta abierto o no
        if (evento.getEstado()) {
            // Verificar si todos los documentos requeridos son entregados
            Presentacion presentacion = (Presentacion) evento;
            if (!presentacion.todosRequeridosEntregados()) {
                // Si no lo son verificar que usuario confirma operacion
                System.out.println(TextosConstantes.FALTAN_DOCUMENTOS);
                if (!InterfacesGenerales.interfazSiONo(TextosConstantes.CONFIRMAR_OPERACION)) return;
            }
            evento.setEstado(false);
            System.out.println(TextosConstantes.EXITO_OPERACION);
        } else {
            System.out.println(TextosConstantes.PRESENTACION_CERRADA);
        }
    }

    /**
     * Metodo que toma una entidad y asigna un nuevo cuentadante a ella
     * Pide identficador del cuentadante, lo valida y lo asigna
     */
    public static void actualizarNuevoCuentadante(Entidad entidad, Hashtable<String, Usuario> usuarios) {
        while (true) {
            // Pide el identificador de cuentadante y validalo
            Usuario usuario = GestorUsuario.pedirYVerificarIdUsuario(usuarios);
            if (usuario == null) return;
            if (!(usuario instanceof Cuentadante cuentadante)) {
                System.out.println(TextosConstantes.IDENTIFICADOR_NO_CUENTADANTE);
                continue;
            }
            // Si la entidad es fiscal, entonces asignalo con su metodo
            if (entidad instanceof Fiscal) {
                OperacionesRelaciones.updateFiscalACuentadante((Fiscal) entidad, cuentadante);
                // Si no, es un municipio
            } else {
                OperacionesRelaciones.updateMunicipioACuentadante(cuentadante, (Municipio) entidad);
            }
            break;
        }
    }

    /**
     * Metodo que pide una fecha la valida en el futuro y la devuelve como local date
     */
    public static LocalDate pedirYValidarFecha(String textoAImprimir) {
        LocalDate fecha;
        while (true) {
            // Pide y verifica la fecha de apertura y cierre
            System.out.println(textoAImprimir);
            String fechaString = InterfacesGenerales.pedirFecha();
            // Consigue la fecha como LocalDate y verifica que sea valida
            fecha = Verificadores.verificarFecha(fechaString);
            if (fecha == null) {
                System.out.println(TextosConstantes.ERROR_FECHA);
                continue;
            }
            // Devuelvela
            return fecha;
        }
    }
}
