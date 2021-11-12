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
     * Retorna usuario si es correcto, si no retorna null
     */
    public static Usuario verificarUsuario(String usuario, String contra, Hashtable<String, Usuario> usuarios) {
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
    public static boolean verificarFormatoIdentificador(String tipoDeEntidad, String identificador) {
        if (tipoDeEntidad.equals("Evento"))
            return identificador.length() <= 100;
        else if (tipoDeEntidad.equals("Usuario"))
            return 0 < identificador.length() && identificador.length() <= 10;
        else if (tipoDeEntidad.equals("Municipio"))
            return identificador.length() <= 30;
        else return false;
    }

    /**
     * Verificador de tipo, retorna si el tipo pasado corresponde a alguno valido
     */
    public static boolean verificarFormatoTipo(String nuevoTipo) {
        for (String tipo : Usuario.TIPOS_USUARIO) {
            if (tipo.equals(nuevoTipo)) return true;
        }
        return false;
    }


    /**
     * Verificador de formato de clave de usuarios
     */
    public static boolean verificarFormatoClave(String clave) {
        return 4 <= clave.length() && clave.length() <= 8;
    }


    /**
     * Verificador de input in rango de opciones,
     * toma el siguiente entero del scanner, lo verifica en los rangos, y lo devuelve
     */
    public static int verificarInputEnteroEnRango(Scanner fuenteDeInput, int limiteInferiorInclusive, int limiteSuperiorInclusive) {
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
        if (limiteInferiorInclusive <= input && input <= limiteSuperiorInclusive) return input;
        return -1;
    }

    /**
     * Verificador de fechas de apertura de convocatorias, la pide, la valida y la asigna
     */
    public static boolean verificarYAsignarFechaApertura(Convocatoria convocatoria, String fecha) {
        LocalDate fechaFinal;
        fechaFinal = verificarFecha(fecha);
        // Verifica que la fecha final no sea nula y
        // que sea antes de la fecha de cierre
        if (fechaFinal != null && fechaFinal.isBefore(convocatoria.getFechaCierre())) {
            // Asigna la fecha a la convocatoria
            convocatoria.setFechaInicio(fechaFinal);
            return true;
        }
        // En caso contrario no es una fecha valida
        return false;
    }

    /**
     * Verificador de fechas de cierre de convocatorias, la pide, la valida y la asigna
     */
    public static boolean verificarYAsignarFechaCierre(Convocatoria convocatoria, String fecha) {
        LocalDate fechaFinal;
        try {
            // Intenta pasar la fecha a localDate y a la convocatoria
            fechaFinal = LocalDate.parse(fecha);
            convocatoria.setFechaCierre(fechaFinal);
        } catch (Exception e) {
            // En caso de que la fecha sea invalida devuelve falso
            return false;
        }
        // en caso contrario (se pudo asignar) devuelve verdadero
        return true;
    }

    /**
     * Verificador y convertor de fechas String a LocalDate
     * Devuelve nulo en caso de fecha String en formato invalido
     */
    public static LocalDate verificarFecha(String fecha) {
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
    public static boolean arrayContainsString(String string, Object[] array) {
        //Si el documento no se encuentra en la lista de posibles documentos requeridos
        return Arrays.binarySearch(array, string) >= 0;
    }
}
