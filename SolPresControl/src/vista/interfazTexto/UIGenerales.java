package vista.interfazTexto;

import controlador.herramientas.Verificadores;
import vista.TextosConstantes;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Clase que contiene interfaces de objetivo general
 */
public class UIGenerales {

    /**
     * Interfaz que muestra categoria actual, pide una nueva categoria y la devuelve
     */
    public static int pedirCategoria() {
        Scanner in = new Scanner(System.in);
        // Intenta pedir la categoria
        while (true) {
            try {
                System.out.println(TextosConstantes.MUNICIPIOS_PEDIR_CATEGORIA);
                String input = in.nextLine();
                // Si el input es vacio el usuario desea salir
                if (input.isEmpty()) return -1;
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Si usuario ingresa algo incorrecto informale y vuelve a pedirle categoria
                System.out.println(TextosConstantes.ERROR_SOLO_ENTEROS);
                System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
            }
        }
    }

    /**
     * Pide una fecha y la devuelve como string en formato "anio-mes-dia"
     */

    public static String pedirFecha() {
        String[] opciones = {TextosConstantes.INGRESE_NUEVA_FECHA_YEAR,
                TextosConstantes.INGRESE_NUEVA_FECHA_MONTH,
                TextosConstantes.INGRESE_NUEVA_FECHA_DAY};
        StringBuilder fechaIngresada = new StringBuilder();
        String[] input = interfazPedirDatosYEsperar(opciones);
        // Concatena y devuelve la fecha
        fechaIngresada.append(input[0].replaceAll("\\s+", ""))
                .append("-").append(input[1].replaceAll("\\s+", ""))
                .append("-").append(input[2].replaceAll("\\s+", ""));
        return fechaIngresada.toString();
    }

    /**
     * Interfaz que imprime documentos, pide nuevos, y los devuelve como string
     */
    public static String pedirDocumento(Hashtable<String, Boolean> documentos) {
        Scanner in = new Scanner(System.in);
        String input;
        // Imprime el estado de los documentos del evento (Convocatoria o presentacion)
        System.out.println(TextosConstantes.DOCUMENTOS);
        imprimirHashTableObjetoValor(documentos);
        System.out.println(TextosConstantes.PEDIR_DOCUMENTO);
        input = in.nextLine();
        return input;
    }

    /**
     * Interfaz que pide identificador y lo devuelve
     */

    public static String pedirIdentificador(String textoAImprimir) {
        Scanner in = new Scanner(System.in);
        String input;
        // Toma la entrada y devuelvelo acordemente
        System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
        System.out.println(textoAImprimir);
        input = in.nextLine().replaceAll("\\s+", "");
        return input;
    }

    /**
     * Interfaz que pide las opciones en el array y espera por el input en cada una
     * devuelve el input como un array de strings
     */
    public static String[] interfazPedirDatosYEsperar(String[] opciones) {
        Scanner in = new Scanner(System.in);
        String[] input = new String[opciones.length];
        int contador = 0;
        // Itera por las opciones pidiendolas al usuario y asignandolas a sus respectivos valores
        for (String opcion : opciones) {
            System.out.println(opcion);
            input[contador] = in.nextLine();
            contador++;
        }
        return input;
    }

    /**
     * Interfaz que imprime todas las opciones en el array y espera por un input entero en el rango de opciones
     */
    public static int interfazMostrarOpcionesPedirEntero(String[] opciones) {
        Scanner in = new Scanner(System.in);
        int input;
        while (true) {
            // Itera por las opciones imprimiendolas al usuario
            for (String opcion : opciones) {
                System.out.println(opcion);
            }
            // Pide el input en rango
            input = Verificadores.verificarEnteroEnRango(in, 0, opciones.length - 1);
            // Si es invalido pidelo de nuevo
            if (input == -1) {
                System.out.println(TextosConstantes.ERROR_SOLO_ENTEROS);
                continue;
            }
            break;
        }
        return input;
    }

    /**
     * Interfaz que verifica alguna operacion con respuesta si o no
     */
    public static boolean interfazSiONo(String textoAImprimir) {
        System.out.println(textoAImprimir);
        int input = interfazMostrarOpcionesPedirEntero(new String[]{TextosConstantes.SI, TextosConstantes.NO});
        return input == 1;
    }

    /**
     * Interfaz que imprime los registros del hashtable y sus valores con flechas
     */
    public static void imprimirHashTableObjetoValor(Hashtable<String, Boolean> hashtable) {
        //Itera por las llaves imprimiendo cada uno de ellos y su estado
        for (String llave : hashtable.keySet()) {
            System.out.println(llave + " -> " + hashtable.get(llave));
        }
        System.out.println("--------------------------------");
    }

    /**
     * Interfaz que pide una fecha la valida en el futuro y la devuelve como local date
     */
    public static LocalDate pedirYValidarFecha(String textoAImprimir) {
        LocalDate fecha;
        while (true) {
            // Pide y verifica la fecha de apertura y cierre
            System.out.println(textoAImprimir);
            String fechaString = pedirFecha();
            // Consigue la fecha como LocalDate y verifica que sea valida
            fecha = Verificadores.pasarALocalDate(fechaString);
            if (fecha == null) {
                System.out.println(TextosConstantes.ERROR_FECHA);
                continue;
            }
            // Devuelvela
            return fecha;
        }
    }
}
