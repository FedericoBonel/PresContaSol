package controlador.modulosFuncionales.gestores;

import controlador.abstraccionNegocio.*;
import controlador.herramientas.OperacionesGenerales;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;
import vista.interfazTexto.InterfacesGenerales;
import vista.interfazTexto.InterfazUsuarios;

import java.util.Hashtable;

/**
 * Contenedor de metodos relacionados al gestor de presentaciones
 */
public class GestorUsuario {

    /**
     * Gestor de usuarios
     * Muestra menu segun el usuario y lanza los modulos necesarios
     */
    public static void gestorUsuario(Usuario usuario, Hashtable<String, Usuario> usuarios, Hashtable<String, Presentacion> presentaciones) {
        int input = -1;
        while (input != 0) {
            input = InterfazUsuarios.usuariosInterfaz(usuario, usuarios);
            switch (input) {
                case 1 -> modificarUsuario(usuario, usuarios);
                case 2 -> eliminarUsuario(usuarios, presentaciones);
                case 3 -> crearUsuario(usuarios);
            }
        }
    }

    /**
     * Metodo modificador de usuarios, pide Id, la verifica como existente y lanza el modulo de modificacion necesario
     */
    private static void modificarUsuario(Usuario usuario, Hashtable<String, Usuario> usuarios) {
        int input;
        Usuario usuarioAmodificar;
        // Pide el identificador
        usuarioAmodificar = pedirYVerificarIdUsuario(usuarios);
        // Si el usuario a modificar es nulo el usuario desea abortar la operacion
        if (usuarioAmodificar == null) return;
        // Imprime el menu segun el tipo de usuario a modificar y lanza el modulo necesario
        input = InterfazUsuarios.modificarUsuarioInterfaz(usuario, usuarioAmodificar);
        if (input == 1 && usuarioAmodificar instanceof Fiscal fiscal) {
            OperacionesGenerales.actualizarNuevoCuentadante(fiscal, usuarios);
        }
        if (input == 1 && usuarioAmodificar instanceof Cuentadante cuentadante) {
            modificarFiscal(cuentadante, usuarios);
        }
        if (input == 2) modificarContra(usuarioAmodificar);
    }

    /**
     * Metodo que pide un nuevo fiscal, lo valida y lo asigna al usuario cuentadante
     */
    private static void modificarFiscal(Cuentadante cuentadante, Hashtable<String, Usuario> usuarios) {
        while (true) {
            // Pide nuevo fiscal
            Usuario nuevoFiscal = pedirYVerificarIdUsuario(usuarios);
            if (nuevoFiscal == null) return;
            // Validalo como fiscal
            if (!(nuevoFiscal instanceof Fiscal fiscal)) {
                System.out.println(TextosConstantes.IDENTIFICADOR_NO_FISCAL);
                continue;
            }
            // Asignalo al usuario
            OperacionesRelaciones.updateFiscalACuentadante(fiscal, cuentadante);
            break;
        }
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }


    /**
     * Metodo que pide una nueva clave, la valida y la asigna al usuario pasado
     */
    private static void modificarContra(Usuario usuario) {
        // Pedir nueva clave
        String clave = InterfazUsuarios.modificarContraInterfaz();
        if (clave == null) return;
        // Asignarla
        usuario.setContra(clave);
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo eliminador de usuarios, pide Id, la verifica como existente y lo elimina del sistema
     */
    private static void eliminarUsuario(Hashtable<String, Usuario> usuarios, Hashtable<String, Presentacion> presentaciones) {
        // Pide y busca el usuario a eliminar
        Usuario usuario = pedirYVerificarIdUsuario(usuarios);
        if (usuario == null) return;
        // Si es fiscal desasignalo de sus cuentadantes antes de eliminarlo
        if (usuario instanceof Fiscal fiscal) {
            for (String cuentadante : fiscal.getCuentadantes().keySet())
                OperacionesRelaciones.removeCuentadanteDeFiscal(fiscal.getCuentadantes().get(cuentadante));
        }
        // Si es cuentadante elimina sus presentaciones, desasignalo de su fiscal y de su municipio
        else if (usuario instanceof Cuentadante cuentadante) {
            // Copia su set de llaves para evitar concurrent modification exception
            String[] presentacionesLlaves = cuentadante.getPresentaciones().keySet().toArray(new String[0]);
            for (String presentacion : presentacionesLlaves) {
                OperacionesRelaciones.removePresentacion(cuentadante.getPresentaciones().get(presentacion));
                presentaciones.remove(presentacion);
            }
            OperacionesRelaciones.removeCuentadanteDeFiscal(cuentadante);
            OperacionesRelaciones.desasignarMunicipioDelCuentadante(cuentadante.getMunicipio());
        }
        // Eliminalo del sistema
        usuarios.remove(usuario.getId());
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo creador de usuarios, pide Id, la verifica como NO existente y lo agrega al sistema
     */
    private static void crearUsuario(Hashtable<String, Usuario> usuarios) {
        String identificador;
        while (true) {
            // Pide el identificador
            identificador = InterfacesGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_USUARIO);
            if (identificador.isEmpty()) return;
            // Validalo como nuevo y no existente
            if (!Verificadores.verificarFormatoIdentificador("Usuario", identificador) || usuarios.containsKey(identificador)) {
                System.out.println(TextosConstantes.ERROR_FORMATO_O_YA_EXISTE);
                continue;
            }
            break;
        }
        // Pide datos del nuevo usuario
        String[] claveYTipo = pedirDatosUsuario();
        // Crea el objeto y asignalo al hashtable
        if (claveYTipo[1].equals("Cuentadante")) {
            usuarios.put(identificador, new Cuentadante(identificador, claveYTipo[0]));
        } else if (claveYTipo[1].equals("Fiscal")) {
            usuarios.put(identificador, new Fiscal(identificador, claveYTipo[0]));
        } else {
            usuarios.put(identificador, new Usuario(identificador, claveYTipo[0], claveYTipo[1]));
        }
        System.out.println(TextosConstantes.EXITO_OPERACION);
    }

    /**
     * Metodo que pide los datos de nuevo usuario necesarios para construirlo
     * los retorna como array [clave, tipo]
     */
    private static String[] pedirDatosUsuario() {
        while (true) {
            // Pide los datos
            String[] datos = InterfacesGenerales.interfazPedirDatosYEsperar(
                    new String[]{TextosConstantes.USUARIOS_NUEVA_CONTRA, TextosConstantes.USUARIOS_INGRESE_TIPO});
            // Quitale los espacios
            datos[0] = datos[0].replaceAll("\\s+", "");
            datos[1] = datos[1].replaceAll("\\s+", "");
            // Validalos
            if (!Verificadores.verificarFormatoClave(datos[0])) {
                System.out.println(TextosConstantes.USUARIO_CLAVE_FORMATO_ERROR);
                continue;
            }
            if (!Verificadores.verificarFormatoTipo(datos[1])) {
                System.out.println(TextosConstantes.USUARIOS_ERROR_TIPO_FORMATO);
                continue;
            }
            return datos;
        }
    }

    /**
     * Metodo que pide id de usuario EXISTENTE, lo verifica como existente en el hashtable pasado y lo devuelve
     */
    public static Usuario pedirYVerificarIdUsuario(Hashtable<String, Usuario> usuarios) {
        Usuario usuario;
        String identificador;
        while (true) {
            //Pide el identificador
            identificador = InterfacesGenerales.pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_USUARIO);
            if (identificador.isEmpty()) return null;
            if (!Verificadores.verificarFormatoIdentificador("Usuario", identificador)) {
                System.out.println(TextosConstantes.USUARIOS_ERROR_FORMATO);
                continue;
            }
            // Toma el objeto del usuario
            usuario = usuarios.get(identificador);
            if (usuario == null) {
                System.out.println(TextosConstantes.ERROR_ID_NO_ENCONTRADO);
                continue;
            }
            break;
        }
        // Si el identificador es validado devuelve el objeto al que hace referencia
        return usuario;
    }
}
