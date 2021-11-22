package controlador.herramientas;

import controlador.abstraccionNegocio.Convocatoria;
import controlador.abstraccionNegocio.Usuario;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contenedor de verificadores generales
 */
public class Verificadores {

    /**
     * Verificador de usuarios con clave
     * Retorna usuario si es valido, si no retorna null
     */
    public static Usuario ingresarConClave(String usuario, String contra, Hashtable<String, Usuario> usuarios) {
        //Verifica que el usuario corresponda con la contraseña
        if (usuarios.containsKey(usuario)) {
            Usuario usuarioAVerificar = usuarios.get(usuario);
            if (usuarioAVerificar.getContra().equals(contra)) return usuarioAVerificar;
        }
        return null;
    }

    /**
     * Verificador de formato identificador
     */
    public static boolean verificarId(String tipoDeEntidad, String identificador) {
        return switch (tipoDeEntidad) {
            case "Evento" -> identificador.length() <= 100;
            case "Usuario" -> 0 < identificador.length() && identificador.length() <= 10;
            case "Municipio" -> identificador.length() <= 30;
            default -> false;
        };
    }

    /**
     * Verificador de tipo, retorna si el tipo pasado corresponde a alguno valido
     */
    public static boolean verificarTipo(String nuevoTipo) {
        for (String tipo : Usuario.TIPOS_USUARIO) {
            if (tipo.equals(nuevoTipo)) return true;
        }
        return false;
    }


    /**
     * Verificador de formato de clave de usuarios
     */
    public static boolean verificarClave(String clave) {
        return 4 <= clave.length() && clave.length() <= 8;
    }


    /**
     * Verificador de entrada en rango de opciones,
     * toma el siguiente entero del scanner, lo verifica en los rangos INCLUSIVE, y lo devuelve
     */
    public static int verificarEnteroEnRango(Scanner fuenteDeInput, int limiteInferior, int limiteSuperior) {
        int input;
        try {
            // Pide el input
            input = fuenteDeInput.nextInt();
        } catch (InputMismatchException e) {
            // En caso de que input no sea entero, limpia el scanner y devuelve flag erroneo
            fuenteDeInput.next();
            return -1;
        }
        // Verificacion de entrada en rango
        if (limiteInferior <= input && input <= limiteSuperior) return input;
        return -1;
    }

    /**
     * Verificador de fechas de apertura de convocatorias, la pide, la valida y la asigna
     */
    public static boolean asignarFechaAperturaConvo(Convocatoria convocatoria, String fechaString) {
        LocalDate fechaRes;
        fechaRes = pasarALocalDate(fechaString);
        // Verifica que la fecha final no sea nula y
        // que sea antes de la fecha de cierre
        if (fechaRes != null && fechaRes.isBefore(convocatoria.getFechaCierre())) {
            // Asigna la fecha a la convocatoria
            convocatoria.setFechaInicio(fechaRes);
            return true;
        }
        // En caso contrario no es una fecha valida
        return false;
    }

    /**
     * Verificador de fechas de cierre de convocatorias, la pide, la valida y la asigna
     */
    public static boolean asignarFechaCierreConvo(Convocatoria convocatoria, String fechaString) {
        LocalDate fechaRes;
        try {
            // Intenta pasar la fecha a localDate y a la convocatoria
            fechaRes = LocalDate.parse(fechaString);
            convocatoria.setFechaCierre(fechaRes);
        } catch (Exception e) {
            // En caso de que la fecha sea invalida devuelve falso
            return false;
        }
        // en caso contrario (se pudo asignar) devuelve verdadero
        return true;
    }

    /**
     * Verificador y conversor de fechas String a LocalDate
     * Devuelve nulo en caso de fecha String en formato invalido
     */
    public static LocalDate pasarALocalDate(String fecha) {
        LocalDate fechaFinal;
        try {
            // Intenta pasarlo a LocalDate
            fechaFinal = LocalDate.parse(fecha);
        } catch (Exception e) {
            // En caso de que la fecha sea un formato invalido devuelve nulo
            return null;
        }
        // Si es una fecha en el pasado es invalida
        if (LocalDate.now().isAfter(fechaFinal)) return null;
        // Caso contrario devuelve la fecha como LocalDate
        return fechaFinal;
    }

    /**
     * Verifica si el string pasado esta en el array ordenado o no
     */
    public static boolean arrayContieneString(String string, Object[] array) {
        //Si el documento no se encuentra en la lista de posibles documentos requeridos
        return Arrays.binarySearch(array, string) >= 0;
    }
}
