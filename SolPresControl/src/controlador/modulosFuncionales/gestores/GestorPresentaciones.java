package controlador.modulosFuncionales.gestores;

import controlador.abstraccionNegocio.*;
import controlador.herramientas.OperacionesGenerales;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;
import vista.interfazTexto.UIGenerales;
import vista.interfazTexto.UIPresentaciones;

import java.time.LocalDate;
import java.util.Hashtable;

/**
 * Contenedor de metodos relacionados a la gestion de presentaciones
 */

public class GestorPresentaciones {

    /**
     * Gestor de Presentaciones
     * Muestra menu segun el usuario y lanza los modulos necesarios
     */
    public static void gestorPresentaciones(Usuario usuario, Hashtable<String, Convocatoria> convocatorias, Hashtable<String, Presentacion> presentaciones) {
        int input = -1;
        while(input != 0) {
            input = UIPresentaciones.interfazPresentaciones(usuario, presentaciones);
            switch (input) {
                case 1 -> modificarPresentacion(usuario, presentaciones);
                case 2 -> eliminarPresentacion(usuario, presentaciones);
                case 3 -> crearPresentacion((Cuentadante) usuario, presentaciones, convocatorias);
            }
        }
    }

    /**
     * Metodo que modifica presentaciones, muestra las opciones y hace las llamadas requeridas
     */
    private static void modificarPresentacion(Usuario usuario, Hashtable<String, Presentacion> presentaciones){
        int input;
        //Pide el identificador
        Presentacion presentacion = pedirIdPresen(presentaciones, usuario);
        // Si presentacion es nula el usuario desea abortar la operacion
        if (presentacion == null) return;
        // Imprime el menu y lanza el modulo que corresponda
        input = UIPresentaciones.interfazModificarPresentacion(usuario);
        switch (input) {
            case 1 -> OperacionesGenerales.modificarApertura(usuario, presentacion);
            case 2 -> OperacionesGenerales.modificarDocumentos(usuario, presentacion);

        }
    }

    /**
     * Metodo que elimina presentaciones, pide su identificador, la verfica y la elimina
     */
    private static void eliminarPresentacion(Usuario usuario, Hashtable<String, Presentacion> presentaciones) {
        Presentacion presentacion = pedirIdPresen(presentaciones, usuario);
        //Si la presentacion pasada es nula el usuario desea abortar la operacion
        if (presentacion == null) return;
        // Desasigna sus relaciones
        OperacionesRelaciones.removePresentacion(presentacion);
        // Remuevela del sistema
        presentaciones.remove(presentacion.getId());
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo que creador de presentaciones, pide los datos a asignar, los valida y crea el objeto requerido
     */
    private static void crearPresentacion(Cuentadante usuario, Hashtable<String, Presentacion> presentaciones, Hashtable<String, Convocatoria> convocatorias) {
        String identificador;
        //Pide el identificador, validalo como unico y no existente
        while (true) {
            identificador = UIGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_PRESENTACIONES);
            if (identificador.isEmpty()) return;
            if (!Verificadores.verificarId("Evento", identificador) || presentaciones.containsKey(identificador)) {
                System.out.println(TextosConstantes.ERROR_FORMATO_O_YA_EXISTE);
                continue;
            }
            break;
        }
        //Pide los datos requeridos para la nueva presentacion
        Object[] datos = pedirDatosPresen(usuario, convocatorias);
        if (datos.length == 0) return;
        // Crea el objeto y agregalo a las presentaciones
        Presentacion presentacion = new Presentacion(
                identificador,
                LocalDate.now(),
                (Convocatoria) datos[0],
                usuario,
                usuario.getMunicipio(),
                (Hashtable<String, Boolean>) datos[1]);
        presentaciones.put(identificador, presentacion);
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo que pide los datos de convocatoria y documentos entregados de nueva presentacion
     */
    private static Object[] pedirDatosPresen(Cuentadante usuario, Hashtable<String, Convocatoria> convocatorias){
        Hashtable<String, Boolean> documentosEntregados = new Hashtable<>();
        // Pide la convocatoria a la que postula
        Convocatoria convocatoria = GestorConvocatorias.pedirIdConvo(convocatorias);
        if (convocatoria == null) return new Object[]{};
        // Si la convocatoria ya esta cerrada informa al usuario y aborta la operacion
        if (!convocatoria.isAbierto()) {
            System.out.println(TextosConstantes.ERROR_CONVOCATORIA_CERRADA);
            return new Object[]{};
        }
        // Imprime los documentos requeridos
        System.out.println(TextosConstantes.DOCUMENTOS_REQUERIDOS);
        UIGenerales.imprimirHashTableObjetoValor(convocatoria.getDocumentos());
        for (String documento : Evento.DOCUMENTOS_OPCIONES) documentosEntregados.put(documento, false);
        // Pide y agrega los documentos a entregar
        OperacionesGenerales.agregarDocumento(documentosEntregados, usuario);
        return new Object[]{convocatoria, documentosEntregados};
    }

    /**
     * Metodo que pide id de presentacion EXISTENTE, la verifica como existente en el hashtable pasado y la devuelve
     */
    public static Presentacion pedirIdPresen(Hashtable<String, Presentacion> presentaciones, Usuario usuario) {
        Presentacion presentacion;
        String identificador;
        while(true) {
            //Pide el identificador
            identificador = UIGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_PRESENTACIONES);
            // Verifica si es valido
            if (identificador.isEmpty()) return null;
            if (!Verificadores.verificarId("Evento", identificador)) {
                System.out.println(TextosConstantes.EVENTOS_ERROR_FORMATO);
                continue;
            }
            // Toma el objeto de la presentacion
            presentacion = presentaciones.get(identificador);
            //Si no existe la presentacion, informe al usuario y vuelve a pedirla
            if (presentacion == null){
                System.out.println(TextosConstantes.ERROR_ID_NO_ENCONTRADO);
                continue;
            }
            // Si el usuario es cuentadante y la presentacion no es suya
            if (usuario.getTipo().equals("Cuentadante")) {
                Cuentadante cuentadante = (Cuentadante) usuario;
                if (!cuentadante.containsPresentacion(presentacion)){
                    System.out.println(TextosConstantes.ERROR_ID_NO_ENCONTRADO);
                    continue;
                }
                //Si la presentacion se encuentra cerrada tampoco puede obtenerla
                if (!presentacion.isAbierto()) {
                    System.out.println(TextosConstantes.PRESENTACION_CERRADA);
                    continue;
                }
            }
            break;
        }
        // Si el identificador es validado devuelve el objeto al que hace referencia
        return presentacion;
    }



}
