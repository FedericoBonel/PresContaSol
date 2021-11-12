package controlador.abstraccionNegocio;

import java.util.Hashtable;

/**
 * Clase que contiene todas las operaciones a realizar que involucran 1 o relaciones de objetos
 */
public class OperacionesRelaciones {

    /**
     * Presentaciones
     */

    public static void removePresentacion(Presentacion presentacion) {
        // Toma los objetos
        Cuentadante cuentadante = presentacion.getCuentadante();
        Convocatoria convocatoria = presentacion.getConvocatoria();
        // Actualiza los objetos
        convocatoria.removePresentacion(presentacion);
        cuentadante.removePresentacion(presentacion);
        // Actualiza el puntero
        presentacion.setCuentadante(null);
        presentacion.setConvocatoria(null);
    }

    public static void updatePresentacionConvo(Presentacion presentacion, Convocatoria convocatoria) {
        // Toma el objeto a desasignar
        Convocatoria antigua = presentacion.getConvocatoria();
        // Actualiza la convocatoria a desasignar
        antigua.removePresentacion(presentacion);
        // Actualiza la nueva convocatoria
        convocatoria.addPresentacion(presentacion);
        // Actualiza la presentacion
        presentacion.setConvocatoria(convocatoria);
    }

    /**
     * Municipios
     */

    public static void desasignarMunicipioDelCuentadante(Municipio municipio) {
        // Toma el cuentadante
        Cuentadante cuentadante = municipio.getCuentadante();
        // Elimina municipio de cuentadante
        if (cuentadante != null) cuentadante.setMunicipio(null);
        // Elimina cuentadante de municipio
        municipio.setCuentadante(null);
    }

    public static void updateMunicipioACuentadante(Cuentadante cuentadante, Municipio municipio) {
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
     * Fiscal
     */

    public static void updateFiscalACuentadante(Fiscal fiscal, Cuentadante cuentadante) {
        // Toma el fiscal a actualizar
        Fiscal antiguo = cuentadante.getFiscal();
        // Actualiza el fiscal antiguo
        if (antiguo != null) antiguo.removeCuentadante(cuentadante);
        // Actualiza el nuevo fiscal
        fiscal.addCuentadante(cuentadante);
        // Actualiza el cuentadante
        cuentadante.setFiscal(fiscal);
    }

    public static void removeCuentadanteDeFiscal(Cuentadante cuentadante) {
        // Toma el fiscal del cuentadante
        Fiscal fiscal = cuentadante.getFiscal();
        // Remueve el fiscal del cuentadante
        cuentadante.setFiscal(null);
        // Remueve el cuentadante del fiscal
        if (fiscal != null) fiscal.removeCuentadante(cuentadante);
    }

    /**
     * Convocatoria
     */
    //Remueve la convocatoria y todos sus presentaciones del sistema
    public static void removeConvocatoria(Convocatoria convocatoria) {
        Presentacion presentacionObjeto;
        // Toma las presentaciones
        Hashtable<String, Presentacion> presentaciones = convocatoria.getPresentaciones();
        // Actualiza las presentaciones
        for (String presentacion : presentaciones.keySet()) {
            presentacionObjeto = presentaciones.get(presentacion);
            // Toma el cuentadante
            Cuentadante cuentadante = presentacionObjeto.getCuentadante();
            // Actualiza el cuentadante
            cuentadante.removePresentacion(presentacionObjeto);
        }
        // Actualiza el puntero
        presentaciones.clear();
    }

}
