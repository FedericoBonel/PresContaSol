package controlador.abstraccionNegocio;

import java.util.Hashtable;

/**
 * Clase que contiene todas las operaciones a realizar que involucran 1 o varias relaciones de objetos
 */
public class OperacionesRelaciones {

    /**
     * Remueve la presentacion de todas sus relaciones
     */
    public static void removePresentacion(Presentacion presentacion) {
        // Toma los objetos
        Cuentadante cuentadante = presentacion.getCuentadante();
        Convocatoria convocatoria = presentacion.getConvocatoria();
        Municipio municipio = presentacion.getMunicipio();
        // Actualiza los objetos
        convocatoria.removePresentacion(presentacion);
        cuentadante.removePresentacion(presentacion);
        municipio.removePresentacion(presentacion);
        // Actualiza el puntero
        presentacion.setCuentadante(null);
        presentacion.setConvocatoria(null);
        presentacion.setMunicipio(null);
    }

    /**
     * Remueve el municipio del cuentadante que tenga asignado
     */
    public static void removeDelCuentadante(Municipio municipio) {
        // Toma el cuentadante
        Cuentadante cuentadante = municipio.getCuentadante();
        // Elimina municipio de cuentadante
        if (cuentadante != null) cuentadante.setMunicipio(null);
        // Elimina cuentadante de municipio
        municipio.setCuentadante(null);
    }


    /**
     * Remueve el cuentadante de todas sus relaciones y presentaciones
     */
    public static void removeCuentadante(Cuentadante cuentadante){
        Presentacion presentacionObjeto;
        Convocatoria convocatoria;
        Municipio municipio;
        // Toma las presentaciones
        Hashtable<String, Presentacion> presentaciones = cuentadante.getPresentaciones();
        for (String presentacion : presentaciones.keySet()) {
            presentacionObjeto = presentaciones.get(presentacion);
            // Toma la convocatoria y municipio de la presentacion
            // (NO NECESARIAMENTE ES EL MISMO QUE EL ASIGNADO ACTUALMENTE)
            convocatoria = presentacionObjeto.getConvocatoria();
            municipio = presentacionObjeto.getMunicipio();
            // Actualiza la convocatoria y el municipio
            convocatoria.removePresentacion(presentacionObjeto);
            municipio.removePresentacion(presentacionObjeto);
        }
        // Actualiza el puntero
        presentaciones.clear();
        // Actualiza los punteros de los fiscales y el municipio actual
        removeDelFiscal(cuentadante);
        removeDelCuentadante(cuentadante.getMunicipio());
    }

    /**
     * Asigna el municipio al cuentadante indicado
     */
    public static void actualizaMuniACuenta(Cuentadante cuentadante, Municipio municipio) {
        //Toma el municipio antiguo y cuentadante antiguo
        Municipio antiguoMunicipio = cuentadante.getMunicipio();
        Cuentadante antiguoCuentadante = municipio.getCuentadante();
        //Actualiza municipio y cuentadante antiguo
        if (antiguoMunicipio != null) antiguoMunicipio.setCuentadante(null);
        if (antiguoCuentadante != null) antiguoCuentadante.setMunicipio(null);
        //Actualiza el nuevo municipio
        municipio.setCuentadante(cuentadante);
        //Actualiza el cuentadante
        cuentadante.setMunicipio(municipio);
    }

    /**
     * Remueve el municipio de todas sus relaciones y presentaciones
     */
    public static void removeMunicipio(Municipio municipio) {
        Presentacion presentacionObjeto;
        Cuentadante cuentadante;
        Convocatoria convocatoria;
        // Toma las presentaciones
        Hashtable<String, Presentacion> presentaciones = municipio.getPresentaciones();
        for (String presentacion : presentaciones.keySet()) {
            presentacionObjeto = presentaciones.get(presentacion);
            // Toma el cuentadante y la convocatoria
            // (NO NECESARIAMENTE ES EL MISMO QUE EL ASIGNADO ACTUALMENTE)
            convocatoria = presentacionObjeto.getConvocatoria();
            cuentadante = presentacionObjeto.getCuentadante();
            // Actualiza la convocatoria y el cuentadante
            convocatoria.removePresentacion(presentacionObjeto);
            cuentadante.removePresentacion(presentacionObjeto);
        }
        // Actualiza el puntero
        presentaciones.clear();
        removeDelCuentadante(municipio);
    }

    /**
     * Asigna el fiscal al cuentadante indicado
     */
    public static void actualizaFiscalACuenta(Fiscal fiscal, Cuentadante cuentadante) {
        // Toma el fiscal a actualizar
        Fiscal antiguo = cuentadante.getFiscal();
        // Actualiza el fiscal antiguo
        if (antiguo != null) antiguo.removeCuentadante(cuentadante);
        // Actualiza el nuevo fiscal
        fiscal.addCuentadante(cuentadante);
        // Actualiza el cuentadante
        cuentadante.setFiscal(fiscal);
    }

    /**
     * Remueve el fiscal del cuentadante
     */
    public static void removeDelFiscal(Cuentadante cuentadante) {
        // Toma el fiscal del cuentadante
        Fiscal fiscal = cuentadante.getFiscal();
        // Remueve el fiscal del cuentadante
        cuentadante.setFiscal(null);
        // Remueve el cuentadante del fiscal
        if (fiscal != null) fiscal.removeCuentadante(cuentadante);
    }

    /**
     * Remueve el fiscal de todas sus relaciones
     */
    public static void removeFiscal(Fiscal fiscal) {
        // Actualiza sus cuentadantes
        Cuentadante cuentadanteObjeto;
        for (String cuentadante : fiscal.getCuentadantes().keySet()) {
            cuentadanteObjeto = fiscal.getCuentadantes().get(cuentadante);
            // Actualiza el cuentadante
            cuentadanteObjeto.setFiscal(null);
        }
        // Actualiza el puntero
        fiscal.getCuentadantes().clear();
    }

    /**
     * Remueve la convocatoria de todas sus relaciones y presentaciones
     */
    public static void removeConvocatoria(Convocatoria convocatoria) {
        Presentacion presentacionObjeto;
        Cuentadante cuentadante;
        Municipio municipio;
        // Toma las presentaciones
        Hashtable<String, Presentacion> presentaciones = convocatoria.getPresentaciones();
        // Actualiza las presentaciones y municipios
        for (String presentacion : presentaciones.keySet()) {
            presentacionObjeto = presentaciones.get(presentacion);
            // Toma el cuentadante y municipio
            cuentadante = presentacionObjeto.getCuentadante();
            municipio = presentacionObjeto.getMunicipio();
            // Actualiza el cuentadante y municipio
            cuentadante.removePresentacion(presentacionObjeto);
            municipio.removePresentacion(presentacionObjeto);
        }
        // Actualiza el puntero
        presentaciones.clear();
    }

}
