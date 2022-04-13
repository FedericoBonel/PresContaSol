package com.fedebonel.modelo.accesodatos;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Singleton utilizado para conectarse a la base de datos del programa
 */
public class ConexionDB {

    /**
     * String constante que posee el error a mostrar cuando hubo fallo en acceso a base de datos
     */
    public static final String ERROR_ACCESO_BASE_DATOS = "Error en acceso a base de datos: ";
    /**
     * Tiempo de espera en segundos antes de lanzar error de conexion
     */
    private static final int TIME_OUT = 10;
    /**
     * Objeto conexion del acceso a la base de datos
     */
    private static Connection conn = null;
    /**
     * Driver a utilizar para la conexion
     */
    private String driver;
    /**
     * URL donde esta almacenada la base de datos
     */
    private String url;
    /**
     * Contenedor del usuario a conectarse a la base de datos
     */
    private String usuario;
    /**
     * Contenedor de la clave del usuario
     */
    private String password;

    /**
     * Constructor privado del singleton, se llama automaticamente al llamar al metodo publico getConnection()
     */
    private ConexionDB() {
        try {
            Properties prop = new Properties();
            FileInputStream ip = new FileInputStream("config.properties");
            prop.load(ip);
            url = prop.getProperty("url");
            driver = prop.getProperty("driver");
            usuario = prop.getProperty("usuario");
            password = prop.getProperty("clave");
            ip.close();
            Class.forName(driver);
            DriverManager.setLoginTimeout(TIME_OUT);
            conn = DriverManager.getConnection(url, usuario, password);
        } catch (Exception e) {
            System.out.println(ERROR_ACCESO_BASE_DATOS);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo utilizado para obtener el acceso a la base de datos
     *
     * @return Objeto Connection que contiene la conexion a la base de datos
     */
    public static Connection getConnection() {
        if (conn == null) {
            new ConexionDB();
        }
        return conn;
    }
}
