package vista.interfazTexto;

import controlador.abstraccionNegocio.*;
import vista.TextosConstantes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a la informacion del sistema
 */
public class InterfazInformacion {

    /**
     * Interfaz de la informacion
     */
    public static void interfazInformacionSistema(Hashtable<String, Convocatoria> convocatorias, Hashtable<String, Municipio> municipios) {
        System.out.println(TextosConstantes.INFORMACION_CABECERA);
        // Mostrar convocatorias por estado de apertura y con cantidad de presentaciones
        System.out.println(TextosConstantes.CONVOCATORIAS_CABECERA);
        int[] cantidadPresentaciones = imprimirConvocatoriasPorEstado(convocatorias);
        System.out.println(TextosConstantes.PRESENTACIONES_CONVO_ABIERTAS);
        System.out.println(cantidadPresentaciones[0]);
        System.out.println(TextosConstantes.PRESENTACIONES_CONVO_CERRADAS);
        System.out.println(cantidadPresentaciones[1]);

        // Mostrar total de presentaciones por municipio con numero de documentos adjuntados
        System.out.println(TextosConstantes.PRESENTACIONES_CABECERA);
        imprimirPresentacionMunicipios(municipios);

    }

    /**
     * Imprime las convocatorias del hashtable por estado y devuelve la cantidad total de presentaciones por estado
     * [cantidadPorAbiertas, cantidadPorCerradas]
     */
    private static int[] imprimirConvocatoriasPorEstado(Hashtable<String, Convocatoria> convocatorias) {
        int cantidadAbiertas = 0, cantidadCerradas = 0;
        // Ordenalas por estado de apertura
        Comparator<Convocatoria> porEstado = Comparator.comparing(Convocatoria::getEstado).reversed();
        Convocatoria[] convocatoriasPorEstado = convocatorias.values().toArray(new Convocatoria[0]);
        Arrays.sort(convocatoriasPorEstado, porEstado);
        // Imprimelas en pantalla
        System.out.println(TextosConstantes.CONVOCATORIAS_GESTOR_CAMPOS);
        for (Convocatoria convocatoria : convocatoriasPorEstado) {
            imprimirEvento(convocatoria);
            if (convocatoria.getEstado()) cantidadAbiertas += convocatoria.getPresentaciones().size();
            else cantidadCerradas += convocatoria.getPresentaciones().size();
        }
        return new int[]{cantidadAbiertas, cantidadCerradas};
    }

    /**
     * Interfaz que imprime el evento pasado y sus atributos ordenadamente
     */
    public static void imprimirEvento(Evento evento) {
        // Obten sus datos como strings e imprimelos en pantalla
        String identficador = evento.getId();
        if (evento instanceof Convocatoria convocatoria) {
            boolean estado = convocatoria.getEstado();
            String documentosRequeridos = InterfacesGenerales.tablaHashVerdaderoAString(convocatoria.getDocumentos());
            String fechaApertura = convocatoria.getFechaInicio().toString();
            String fechaCierre = convocatoria.getFechaCierre().toString();

            System.out.println(identficador + " | " + estado + " | " + documentosRequeridos
                    + " | " + fechaApertura + " | " + fechaCierre);

        } else if (evento instanceof Presentacion presentacion) {
            boolean estado = presentacion.getEstado();
            boolean todoRequerido = presentacion.todosRequeridosEntregados();
            String documentosEntregados = InterfacesGenerales.tablaHashVerdaderoAString(presentacion.getDocumentos());
            String fechaCreacion = presentacion.getFechaInicio().toString();
            String fechaLimite = presentacion.getConvocatoria().getFechaCierre().toString();
            String usuarioPresentador = presentacion.getCuentadante().getId();

            System.out.println(identficador + " | " + estado + " | " + documentosEntregados
                    + " | " + todoRequerido + " | " + fechaCreacion + " | " + fechaLimite + " | " + usuarioPresentador);
        }
    }

    /**
     * Imprime los municipios y su cantidad de presentaciones y documentos totales
     */
    private static void imprimirPresentacionMunicipios(Hashtable<String, Municipio> municipios) {
        int totalDocumentos;
        Municipio municipioObj;
        Cuentadante cuentadanteObj;
        Presentacion presentacionObj;
        System.out.println(TextosConstantes.MUNICIPIOS_GESTOR_CAMPOS);

        for (String municipio : municipios.keySet()) {
            totalDocumentos = 0;
            // Obten el municipio y el cuentadante
            municipioObj = municipios.get(municipio);
            // Imprime su informacion basica
            imprimirMunicipio(municipioObj);
            // Imprime numero de presentaciones
            System.out.print("       " + TextosConstantes.PRESENTACIONES + "> ");
            if (municipioObj.hasCuentadante()) {
                cuentadanteObj = municipioObj.getCuentadante();
                System.out.println(cuentadanteObj.getPresentaciones().size());

                for (String presentacion : cuentadanteObj.getPresentaciones().keySet()) {
                    presentacionObj = cuentadanteObj.getPresentaciones().get(presentacion);
                    totalDocumentos += presentacionObj.getDocumentos().size();
                }
            } else {
                System.out.println(0);
            }
            // Imprime numero de documentos
            System.out.println("       " + TextosConstantes.DOCUMENTOS + "> " + totalDocumentos);
        }
    }

    /**
     * Interfaz que imprime el municipio pasado y sus atributos
     */
    public static void imprimirMunicipio(Municipio municipio) {
        String cuentadanteAsignado = TextosConstantes.MUNICIPIOS_CUENTADANTE_NO_ASIGNADO;
        // Obten sus datos como strings e imprimelos en pantalla
        String identificador = municipio.getId();
        int categoria = municipio.getCategoria();
        Cuentadante cuentadante = municipio.getCuentadante();
        // Si el cuentadante esta asignado toma su id
        if (municipio.hasCuentadante()) cuentadanteAsignado = cuentadante.getId();
        System.out.println(identificador + " | " + categoria + " | " + cuentadanteAsignado);
    }

    /**
     * Interfaz que imprime el usuario pasado y sus atributos
     */
    public static void imprimirUsuario(Usuario usuario) {
        Hashtable<String, Cuentadante> cuentadantesAsignados = null;
        Fiscal fiscalAsignado = null;
        // Obten sus datos como strings e imprimelos en pantalla
        String identficador = usuario.getId();
        String clave = usuario.getContra();
        String tipo = usuario.getTipo();
        // Si es fiscal toma sus cuentadantes
        if (usuario instanceof Fiscal fiscal) cuentadantesAsignados = fiscal.getCuentadantes();
        // Si es cuentadante toma su fiscal
        if (usuario instanceof Cuentadante cuentadante && cuentadante.hasFiscal())
            fiscalAsignado = cuentadante.getFiscal();
        System.out.print(identficador + " | " + clave + " | " + tipo);
        // Solo si los cuentadantes estan asignados imprimelos
        if (cuentadantesAsignados != null) System.out.print(" | " + cuentadantesAsignados.keySet());
        else System.out.print(" | null");
        // Solo si el fiscal esta asignado imprime su id
        if (fiscalAsignado != null) System.out.println(" | " + fiscalAsignado.getId());
        else System.out.print(" | null \n");
    }

}
