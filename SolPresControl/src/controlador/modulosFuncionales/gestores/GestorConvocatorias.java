package controlador.modulosFuncionales.gestores;

import controlador.abstraccionNegocio.*;
import controlador.herramientas.OperacionesGenerales;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;
import vista.interfazTexto.InterfacesGenerales;
import vista.interfazTexto.InterfazConvocatorias;

import java.time.LocalDate;
import java.util.Hashtable;

/**
 * Contenedor de metodos relacionados a la gestion de convocatorias
 */
public class GestorConvocatorias {

    /**
     * Gestor de convocatorias
     * Muestra menu segun el usuario y lanza los modulos necesarios
     */
    public static void gestorConvocatorias(Usuario usuario, Hashtable<String, Convocatoria> convocatorias, Hashtable<String, Presentacion> presentaciones) {
        int input = -1;
        while (input != 0) {
            input = InterfazConvocatorias.convocatoriasInterfaz(usuario, convocatorias);
            switch (input) {
                case 1 -> modificarConvocatoria(usuario, convocatorias);
                case 2 -> eliminarConvocatoria(convocatorias, presentaciones);
                case 3 -> crearConvocatoria(usuario, convocatorias);
            }
        }
    }


    /**
     * Metodo que modifica convocatorias, muestra las opciones y hace las llamadas requeridas
     */
    private static void modificarConvocatoria(Usuario usuario, Hashtable<String, Convocatoria> convocatorias) {
        int input;
        Convocatoria convocatoria;
        //Pide el identificador
        convocatoria = pedirYVerificarIdConvo(convocatorias);
        // Si convocatoria es nula el usuario desea abortar la operacion
        if (convocatoria == null) return;
        // Imprime el menu y lanza el modulo que corresponda
        input = InterfazConvocatorias.modificarConvocatoriasInterfaz();
        switch (input) {
            case 1 -> modificarFechaAperturaConvocatoria(convocatoria);
            case 2 -> modificarFechaCierreConvocatoria(convocatoria);
            case 3 -> OperacionesGenerales.modificarDocumentos(usuario, convocatoria);
            case 4 -> OperacionesGenerales.modificarEstado(usuario, convocatoria);
        }
    }

    /**
     * Metodo que modifica la fecha de apertura de la convocatoria pasada
     */
    private static void modificarFechaAperturaConvocatoria(Convocatoria convocatoria) {
        String nuevaFecha;
        while (true) {
            // Pide la nueva fecha al usuario
            nuevaFecha = InterfacesGenerales.pedirFecha();
            //Verifica y asigna la fecha
            if (!Verificadores.verificarYAsignarFechaApertura(convocatoria, nuevaFecha)) {
                // En caso de fecha invalida,informa al usuario y vuelve a pedirla
                System.out.println(TextosConstantes.CONVOCATORIA_ERROR_FECHA_APERTURA);
                continue;
            }
            //En caso de que sea correcta rompe el loop y vuelve al llamador
            System.out.println(TextosConstantes.EXITO_OPERACION);
            break;
        }
    }

    /**
     * Metodo que modifica la fecha de cierre de la convocatoria pasada
     */
    private static void modificarFechaCierreConvocatoria(Convocatoria convocatoria) {
        String nuevaFecha;
        while (true) {
            // Pide la nueva fecha al usuario
            nuevaFecha = InterfacesGenerales.pedirFecha();
            //Verifica y asigna la fecha
            if (!Verificadores.verificarYAsignarFechaCierre(convocatoria, nuevaFecha)) {
                // En caso de fecha invalida,informa al usuario y vuelve a pedirla
                System.out.println(TextosConstantes.CONVOCATORIA_ERROR_FECHA_CIERRE);
                continue;
            }
            //En caso de que sea correcta rompe el loop y vuelve al llamador
            System.out.println(TextosConstantes.EXITO_OPERACION);
            break;
        }
    }

    /**
     * Metodo que elimina convocatorias, pide su identificador, lo verifica y la elimina
     */
    private static void eliminarConvocatoria(Hashtable<String, Convocatoria> convocatorias, Hashtable<String, Presentacion> presentaciones) {
        Convocatoria convocatoria = pedirYVerificarIdConvo(convocatorias);
        //Si la convocatoria pasada es nula el usuario desea abortar la operacion
        if (convocatoria == null) return;
        //Remueve todas sus presentaciones del sistema
        for (String idPresen : convocatoria.getPresentaciones().keySet()) presentaciones.remove(idPresen);
        // Desasigna sus relaciones de los objetos
        OperacionesRelaciones.removeConvocatoria(convocatoria);
        // Remuevela del sistema
        convocatorias.remove(convocatoria.getId());
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo que crea convocatorias, pide los datos, los valida y los asigna a un nuevo objeto
     */
    private static void crearConvocatoria(Usuario usuario, Hashtable<String, Convocatoria> convocatorias) {
        String identificador;
        Object[] datosParaConvocatoria;
        // Pide el identificador y validalo como unico y no existente
        while (true) {
            identificador = InterfacesGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_CONVOCATORIA);
            if (identificador.isEmpty()) return;
            if (!Verificadores.verificarFormatoIdentificador("Evento", identificador) || convocatorias.containsKey(identificador)) {
                System.out.println(TextosConstantes.ERROR_FORMATO_O_YA_EXISTE);
                continue;
            }
            break;
        }
        // Pide los datos requeridos para la nueva convocatoria
        datosParaConvocatoria = pedirDatosConvo(usuario);
        // Crea el objeto
        try {
            Convocatoria convocatoria = new Convocatoria(
                    identificador,
                    (LocalDate) datosParaConvocatoria[0],
                    (LocalDate) datosParaConvocatoria[1],
                    (Hashtable<String, Boolean>) datosParaConvocatoria[2]);
            // Ponlo en las convocatorias
            convocatorias.put(identificador, convocatoria);
        } catch (Exception e) {
            // Es imposible que pase este caso ya que validamos el input con anterioridad
        }
    }

    /**
     * Metodo que pide datos de nueva convocatoria, los valida y los devuelve como un array de objetos
     * [Fecha de apertura, Fecha de cierre, estado de apertura]
     */
    private static Object[] pedirDatosConvo(Usuario usuario) {
        Hashtable<String, Boolean> documentosRequeridos = new Hashtable<>();
        LocalDate fechaAperturaFinal, fechaCierreFinal;
        while (true) {
            // Pide las fechas requeridas
            fechaAperturaFinal = OperacionesGenerales.pedirYValidarFecha(TextosConstantes.CONVOCATORIA_FECHA_APERTURA);
            fechaCierreFinal = OperacionesGenerales.pedirYValidarFecha(TextosConstantes.CONVOCATORIA_FECHA_CIERRE);
            // Verifica de ambas
            if (!fechaCierreFinal.isAfter(fechaAperturaFinal)) {
                System.out.println(TextosConstantes.ERROR_OPERACION_INVALIDA);
                continue;
            }
            break;
        }
        // Agrega todas las opciones
        for (String documento : Evento.DOCUMENTOS_OPCIONES) documentosRequeridos.put(documento, false);
        // Pide y agrega los documentos requeridos
        OperacionesGenerales.agregarDocumento(documentosRequeridos, usuario);
        // Devuelve todos los datos
        return new Object[]{fechaAperturaFinal, fechaCierreFinal, documentosRequeridos};
    }

    /**
     * Metodo que pide id de convocatoria EXISTENTE, la verifica como existente en el hashtable pasado y la devuelve
     */
    public static Convocatoria pedirYVerificarIdConvo(Hashtable<String, Convocatoria> convocatorias) {
        Convocatoria convocatoria;
        String identificador;
        while (true) {
            //Pide el identificador
            identificador = InterfacesGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_CONVOCATORIA);
            if (identificador.isEmpty()) return null;
            // Verifica si es valido
            if (!Verificadores.verificarFormatoIdentificador("Evento", identificador)) {
                // Si es invalido, informa al usuario y vuelve a pedirlo
                System.out.println(TextosConstantes.EVENTOS_ERROR_FORMATO);
                continue;
            }
            // Toma el objeto de la convocatoria
            convocatoria = convocatorias.get(identificador);
            //Si no existe la convocatoria, informe al usuario y vuelve a pedirla
            if (convocatoria == null) {
                System.out.println(TextosConstantes.ERROR_ID_NO_ENCONTRADO);
                continue;
            }
            break;
        }
        // Si el identificador es validado devuelve el objeto al que hace referencia
        return convocatoria;
    }


}
