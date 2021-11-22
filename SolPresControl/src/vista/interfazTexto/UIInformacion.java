package vista.interfazTexto;

import controlador.abstraccionNegocio.*;
import vista.TextosConstantes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

/**
 * Coleccion de interfaces de texto relacionadas a la informacion del sistema
 */
public class UIInformacion {

    /**
     * Interfaz de la informacion
     */
    public static void interfazInfoGnral(Hashtable<String, Convocatoria> convocatorias, Hashtable<String, Municipio> municipios) {
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
        imprimirPresentacionPorMunicipios(municipios);

    }

    /**
     * Imprime las convocatorias del hashtable por estado y devuelve la cantidad total de presentaciones por estado
     * [cantidadPorAbiertas, cantidadPorCerradas]
     */
    private static int[] imprimirConvocatoriasPorEstado(Hashtable<String, Convocatoria> convocatorias) {
        int cantidadAbiertas = 0, cantidadCerradas = 0;
        // Ordenalas por estado de apertura
        Comparator<Convocatoria> porEstado = Comparator.comparing(Convocatoria::isAbierto).reversed();
        Convocatoria[] convocatoriasPorEstado = convocatorias.values().toArray(new Convocatoria[0]);
        Arrays.sort(convocatoriasPorEstado, porEstado);
        // Imprimelas en pantalla
        System.out.println(TextosConstantes.CONVOCATORIAS_GESTOR_CAMPOS);
        for (Convocatoria convocatoria : convocatoriasPorEstado) {
            System.out.println(convocatoria.toString());
            if (convocatoria.isAbierto()) cantidadAbiertas += convocatoria.getPresentaciones().size();
            else cantidadCerradas += convocatoria.getPresentaciones().size();
        }
        return new int[]{cantidadAbiertas, cantidadCerradas};
    }

    /**
     * Imprime los municipios y su cantidad de presentaciones y documentos totales
     */
    private static void imprimirPresentacionPorMunicipios(Hashtable<String, Municipio> municipios) {
        int totalDocumentos;
        Municipio municipioObj;
        Presentacion presentacionObj;
        Hashtable<String, Boolean> documentos;

        System.out.println(TextosConstantes.MUNICIPIOS_GESTOR_CAMPOS);
        for (String municipio : municipios.keySet()) {
            totalDocumentos = 0;
            // Obten el municipio y el cuentadante
            municipioObj = municipios.get(municipio);
            // Imprime su informacion basica
            System.out.println(municipioObj.toString());
            // Imprime numero de presentaciones
            System.out.println("       " + TextosConstantes.PRESENTACIONES + "> "
                    + municipioObj.getPresentaciones().size());
            // Por cada presentacion cuenta la cantidad de documentos marcados como entregados
            for (String presentacion : municipioObj.getPresentaciones().keySet()) {
                presentacionObj = municipioObj.getPresentaciones().get(presentacion);
                documentos = presentacionObj.getDocumentos();
                for (String documento : documentos.keySet()) {
                    if (documentos.get(documento)) totalDocumentos += 1;
                }
            }
            // Imprime numero de documentos
            System.out.println("       " + TextosConstantes.DOCUMENTOS + "> " + totalDocumentos);
        }

    }
}
