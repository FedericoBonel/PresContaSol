package controlador.herramientas;

import controlador.abstraccionNegocio.*;

import java.time.LocalDate;
import java.util.Hashtable;

/**
 * Clase temporal que contiene los metodos para inicializar las variables localmente y de manera constante
 * En el futuro esto se hara con la base datos
 */

public class InicializadoresVariables {
    //Usuarios
    public static final String[] NOMBRE_USUARIOS = {"Fefe", "toto", "marcos", "lorenzo", "miguel", "lucas"};
    public static final String[] CONTRAS_USUARIOS = {"1234", "1234", "1234", "1234", "1234", "1234"};
    public static final String[] TIPOS_USUARIOS = {"Administrador", "Cuentadante", "Cuentadante", "Fiscal", "FiscalGeneral", "Administrador"};

    //Municipios
    public static final String[] IDENTIFICADORES_MUNICIPIOS = {"Minato", "Chuo", "Shinagawa", "Tokyo", "Shinjuku", "Bunkyo"};
    public static final int[] CATEGORIA_MUNICIPIOS = {1, 2, 13, 6, 6, 28};

    //Convocatorias
    public static final String[] IDENTIFICADORES_CONVOCATORIAS = {"c01", "c02", "c03", "c04", "c05", "c06"};
    public static final Hashtable<String, Boolean> DOCUMENTOS_REQUERIDOS_TODOS = new Hashtable<>();
    public static final LocalDate[] FECHAS_APERTURA_CONVOCATORIAS =
                    {LocalDate.of(2022, 12, 25),
                    LocalDate.of(2021, 5, 2),
                    LocalDate.of(2022, 1, 30),
                    LocalDate.of(2022, 4, 2),
                    LocalDate.of(2022, 12, 8),
                    LocalDate.of(2023, 1, 2)};
    public static final LocalDate[] FECHAS_CIERRE_CONVOCATORIAS =
                    {LocalDate.of(2023, 1, 25),
                    LocalDate.of(2022, 6, 2),
                    LocalDate.of(2022, 3, 3),
                    LocalDate.of(2022, 5, 2),
                    LocalDate.of(2023, 1, 8),
                    LocalDate.of(2023, 2, 2)};

    //Presentaciones
    public static final String[] IDENTIFICADORES_PRESENTACIONES = {"p01", "p02", "p03", "p04", "p05", "p06"};
    public static final Hashtable<String, Boolean> DOCUMENTOS_ENTREGADOS = new Hashtable<>();

    /**
     * Inicializados de usuarios itera por los datos creando los usuarios y agregandolos a la tabla hash
     */
    public static void inicializarUsuarios(Hashtable<String, Usuario> usuarios) {
        for (int i = 0; i < NOMBRE_USUARIOS.length; i++) {
            if (TIPOS_USUARIOS[i].equals("Fiscal")) {
                usuarios.put(NOMBRE_USUARIOS[i], new Fiscal(NOMBRE_USUARIOS[i], CONTRAS_USUARIOS[i]));
                OperacionesRelaciones.updateFiscalACuentadante((Fiscal) usuarios.get(NOMBRE_USUARIOS[i]), (Cuentadante) usuarios.get("marcos"));
                continue;
            }
            if (TIPOS_USUARIOS[i].equals("Cuentadante")) {
                usuarios.put(NOMBRE_USUARIOS[i], new Cuentadante(NOMBRE_USUARIOS[i], CONTRAS_USUARIOS[i]));
                continue;
            }
            usuarios.put(NOMBRE_USUARIOS[i], new Usuario(NOMBRE_USUARIOS[i], CONTRAS_USUARIOS[i], TIPOS_USUARIOS[i]));
        }
    }

    /**
     * Inicializador de municipios Itera por los municipios creando los municipios y agregandolos a la tabla hash
     */
    public static void inicializarMunicipios(Hashtable<String, Municipio> municipios) {
        for (int i = 0; i < IDENTIFICADORES_MUNICIPIOS.length; i++) {
            municipios.put(IDENTIFICADORES_MUNICIPIOS[i],
                    new Municipio(IDENTIFICADORES_MUNICIPIOS[i], CATEGORIA_MUNICIPIOS[i]));
        }
    }

    /**
     * Inicializador de convocatorias Itera por las convocatorias creando las convocatorias y agregandolas a la tabla hash
     */
    public static void inicializarConvocatorias(Hashtable<String, Convocatoria> convocatorias) {
        boolean estado;
        try {
            //Pon todos los documentos como requeridos
            for (String documento : Evento.DOCUMENTOS_OPCIONES) DOCUMENTOS_REQUERIDOS_TODOS.put(documento, true);
            for (int i = 0; i < IDENTIFICADORES_CONVOCATORIAS.length; i++) {
                convocatorias.put(IDENTIFICADORES_CONVOCATORIAS[i], new Convocatoria
                                (IDENTIFICADORES_CONVOCATORIAS[i],
                                FECHAS_APERTURA_CONVOCATORIAS[i],
                                FECHAS_CIERRE_CONVOCATORIAS[i],
                                (Hashtable<String, Boolean>) DOCUMENTOS_REQUERIDOS_TODOS.clone()));
            }
            // Setea el estado como corresponda
            for (String identificador : convocatorias.keySet()) {
                convocatorias.get(identificador).
                        setEstado(!LocalDate.now().isBefore(convocatorias.get(identificador).getFechaInicio())
                        && !LocalDate.now().isAfter(convocatorias.get(identificador).getFechaCierre()));
            }
        } catch (Exception e) {
            System.out.println("ERROR INCIALIZANDO CONVOCATORIAS");
        }
    }

    /**
     * Inicializador de presentaciones Itera por las convocatorias creando las presentaciones y agregandolas a la tabla hash
     */
    public static void inicializarPresentaciones(Hashtable<String, Presentacion> presentaciones, Hashtable<String, Convocatoria> convocatorias, Hashtable<String, Usuario> usuarios) {
        try {
            //Pon todos los documentos como no entregados
            for (String documento : Evento.DOCUMENTOS_OPCIONES) DOCUMENTOS_ENTREGADOS.put(documento, false);
            for (String identificadoresPresentacione : IDENTIFICADORES_PRESENTACIONES) {
                presentaciones.put(identificadoresPresentacione,
                        new Presentacion(identificadoresPresentacione,
                                LocalDate.now(),
                                convocatorias.get(IDENTIFICADORES_CONVOCATORIAS[1]),
                                (Cuentadante) usuarios.get("marcos"),
                                (Hashtable<String, Boolean>) DOCUMENTOS_ENTREGADOS.clone()));

            }
        } catch (Exception e) {
            System.out.println("ERROR INCIALIZANDO PRESENTACIONES");
        }
    }
}
