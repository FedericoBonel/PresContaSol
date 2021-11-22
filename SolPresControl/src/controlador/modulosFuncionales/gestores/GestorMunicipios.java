package controlador.modulosFuncionales.gestores;

import controlador.abstraccionNegocio.*;
import controlador.herramientas.*;
import vista.TextosConstantes;
import vista.interfazTexto.*;

import java.util.Hashtable;

/**
 * Contenedor de metodos relacionados a la gestion de Municipios
 */
public class GestorMunicipios {

    /**
     * Gestor de municipios, lanza la interfaz del gestor segun usuario y lanza los modulos requeridos
     */
    public static void gestorMunicipios(Usuario usuario, Hashtable<String, Municipio> municipios,
                                        Hashtable<String, Usuario> usuarios, Hashtable<String, Presentacion>presentaciones) {
        int input = -1;
        while (input != 0) {
            input = UIMunicipios.interfazMunicipios(usuario, municipios);
            switch (input) {
                case 1 -> modificarMunicipio(usuarios, municipios);
                case 2 -> eliminarMunicipio(municipios, presentaciones);
                case 3 -> crearMunicipio(municipios);
            }
        }
    }

    /**
     * Metodo que modifica municipios, pide el municipio y atributo a modificar
     */
    private static void modificarMunicipio(Hashtable<String, Usuario> usuarios, Hashtable<String, Municipio> municipios) {
        int input;
        Municipio municipio;
        //Pide el identificador
        municipio = pedirIdMunicipios(municipios);
        // Si municipio nulo el usuario desea abortar la operacion
        if (municipio == null) return;
        // Imprime el menu y lanza el modulo que corresponda
        input = UIMunicipios.modificarMunicipioInterfaz();
        switch (input) {
            case 1 -> modificarCategoria(municipio);
            case 2 -> OperacionesGenerales.actualizarNuevoCuentadante(municipio, usuarios);
        }
    }

    /**
     * Metodo que pide id de municipio EXISTENTE, la verifica como existente en el hashtable pasado y la devuelve
     */
    public static Municipio pedirIdMunicipios(Hashtable<String, Municipio> municipios) {
        Municipio municipio;
        String identificador;
        while (true) {
            //Pide el identificador
            identificador = UIGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_MUNICIPIO);
            // Verifica si es valido
            if (identificador.isEmpty()) return null;
            if (!Verificadores.verificarId("Municipio", identificador)) {
                // Si es invalido, informa al usuario y vuelve a pedirlo
                System.out.println(TextosConstantes.EVENTOS_ERROR_FORMATO);
                continue;
            }
            // Toma el objeto de la municipio
            municipio = municipios.get(identificador);
            //Si no existe la municipio, informe al usuario y vuelve a pedirla
            if (municipio == null) {
                System.out.println(TextosConstantes.ERROR_ID_NO_ENCONTRADO);
                continue;
            }
            break;
        }
        // Si el identificador es validado devuelve el objeto al que hace referencia
        return municipio;
    }

    /**
     * Metodo que pide una nueva categoria para el municipio, la verifica y la asigna
     */
    private static void modificarCategoria(Municipio municipio) {
        // Muestra la categoria actual y pide la nueva
        System.out.println(TextosConstantes.MUNICIPIOS_ACTUAL_CATEGORIA + municipio.getCategoria());
        int input = UIGenerales.pedirCategoria();
        // Verifica que el municipio sea correcto
        if (input == -1) return;
        // Asigna la categoria nueva al municipio
        municipio.setCategoria(input);
        // Informa al usuario
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo que pide un municipio existente, lo verifica, limpia sus relaciones y elimina de la tabla de municipios
     */
    private static void eliminarMunicipio(Hashtable<String, Municipio> municipios, Hashtable<String, Presentacion> presentaciones) {
        Municipio municipio = pedirIdMunicipios(municipios);
        //Si el municipio pasado es nulo el usuario desea abortar la operacion
        if (municipio == null) return;
        // Elimina sus presentaciones del sistema
        for (String presentacion : municipio.getPresentaciones().keySet()) presentaciones.remove(presentacion);
        // Desasigna sus relaciones
        OperacionesRelaciones.removeMunicipio(municipio);
        // Remuevelo del sistema
        municipios.remove(municipio.getId());
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo que crea municipios, pide nuevos datos, los valida, y crea el objeto municipio
     */
    private static void crearMunicipio(Hashtable<String, Municipio> municipios) {
        String identificador;
        // Pide el identificador del municipio y verifica que sea valida y unico
        while (true) {
            identificador = UIGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_MUNICIPIO);
            if (identificador.isEmpty()) return;
            if (!Verificadores.verificarId("Municipio", identificador) || municipios.containsKey(identificador)) {
                System.out.println(TextosConstantes.ERROR_FORMATO_O_YA_EXISTE);
                continue;
            }
            break;
        }
        // Pide nueva categoria del municipio
        int categoria = UIGenerales.pedirCategoria();
        if (categoria == -1) return;
        // Si no crea el objeto y agregalo a las municipios
        Municipio municipio = new Municipio(
                identificador,
                categoria);
        // Ponlo en las Municipios
        municipios.put(identificador, municipio);
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

}
