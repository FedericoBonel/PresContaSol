import abstraccionNegocio.*;
import logInUsuario.Ingresar;

import java.time.LocalDate;
import java.util.Hashtable;

public class Test {
    /**
     * Metodo main de prueba, este es usado para verificar el funcionamiento general del sistema.
     * NO es el programa final.
     *
     * @param args Argumentos pasados desde consola como un array de strings
     */
    public static void main (String[] args) {
        // Colecciones de datos
        ColeccionConvocatorias convocatorias = new ColeccionConvocatorias();
        ColeccionMunicipios municipios = new ColeccionMunicipios();
        ColeccionUsuarios usuarios = new ColeccionUsuarios();
        ColeccionPresentaciones presentaciones = new ColeccionPresentaciones();
        Hashtable<String, Boolean> docs = new Hashtable<>();
        for (String documento : Convocatoria.DOCUMENTOS_OPCIONES) docs.put(documento, true);

//      Administrador - Primer usuario en el sistema
        Administrador administrador = new Administrador("alvaro10", "1234");
        usuarios.addUsuario(administrador);
        // Ingresar con clave
        System.out.println("Administrador alvaro10 ingresa al sistema----------");
        Administrador usuarioAdmin = (Administrador) Ingresar.ingresarConClave("alvaro10", "1234", usuarios);
        // Crear usuario Fiscal General
        usuarioAdmin.creaFiscalGral("roberto1", "1234", usuarios);
        // Crear 2 usuarios Fiscales
        usuarioAdmin.creaFiscal("marcos1", "1234", usuarios);
        Fiscal fiscal1 = (Fiscal) usuarios.getUsuario("marcos1");
        usuarioAdmin.creaFiscal("marcos2", "1234", usuarios);
        Fiscal fiscal2 = (Fiscal) usuarios.getUsuario("marcos2");
        // Crear 2 Cuentadantes
        usuarioAdmin.creaCuentadante("federico1", "1234", usuarios);
        usuarioAdmin.creaCuentadante("federico2", "1234", usuarios);
        Cuentadante cuentadante1 = (Cuentadante) usuarios.getUsuario("federico1");
        Cuentadante cuentadante2 = (Cuentadante) usuarios.getUsuario("federico2");
        // Crear municipio
        usuarioAdmin.creaMunicipio("Shinagawa", 10, municipios);
        Municipio shinagawa = municipios.getMunicipio("Shinagawa");
        usuarioAdmin.creaMunicipio("Bunkyo", 55, municipios);
        // Asigna nueva categoria de municipio
        usuarioAdmin.asignaCategoria(shinagawa, 5);
        // Visualizar Municipios
        System.out.println("-----Municipios");
        for (Municipio municipio : administrador.getMunicipiosVisibles(municipios)) System.out.println(municipio);
        // Asigna cuentadante a municipio
        administrador.asignaNuevoRepresentanteAMunicipio(shinagawa,cuentadante1, usuarios);
        // Visualizar Usuarios
        System.out.println("-----Usuarios");
        for (Usuario usuario : administrador.getUsuariosVisibles(usuarios)) System.out.println(usuario.toStringConClave());
        // Crear convocatoria
        administrador.creaConvocatoria("C03", LocalDate.parse("2021-10-11"), LocalDate.parse("2021-12-31"), docs, "Descripcion", convocatorias);
        Convocatoria c03 = convocatorias.getConvocatoria("C03");
        administrador.creaConvocatoria("C04", LocalDate.parse("2022-11-11"), LocalDate.parse("2022-12-31"), docs, "Descripcion", convocatorias);
        // Visualizar Convocatorias
        System.out.println("-----Convocatorias");
        for (Convocatoria convocatoria : administrador.getConvocatoriasVisibles(convocatorias)) System.out.println(convocatoria);

//      Cuentadante
        // Ingresar con clave
        System.out.println("\nCuentadante federico ingresa al sistema1----------");
        cuentadante1 = (Cuentadante) Ingresar.ingresarConClave("federico1", "1234", usuarios);
        // Visualizar convocatorias
        System.out.println("-----Convocatorias");
        for (Convocatoria convocatoria : cuentadante1.getConvocatoriasVisibles(convocatorias)) System.out.println(convocatoria);
        // Crear presentacion
        cuentadante1.creaPresentacion("P01", c03, (Hashtable<String, Boolean>) docs.clone(), presentaciones);
        Presentacion p01 = presentaciones.getPresentacion("P01");
        // Agrega documentos adicionales
        cuentadante1.entregaDocumentoA(p01, "Facturas trimestrales");
        // Quita documentos
        cuentadante1.retiraDocumentoDe(p01, "Facturas trimestrales");
        // Elimina presentacion
        cuentadante1.eliminaPresentacion(p01, presentaciones);
        cuentadante1.creaPresentacion("P02", c03, (Hashtable<String, Boolean>) docs.clone(), presentaciones);
        Presentacion p02 = presentaciones.getPresentacion("P02");
        // Entrega presentacion
        cuentadante1.entregaPresentacion(p02);
        // Visualizar Presentaciones
        System.out.println("-----Presentaciones");
        for (Presentacion presentacion : cuentadante1.getPresentacionesVisibles(presentaciones)) System.out.println(presentacion);
        // Visualizar municipios
        System.out.println("-----Municipios");
        for (Municipio municipio : cuentadante1.getMunicipiosVisibles(municipios)) System.out.println(municipio);


//      Fiscal General
        // Ingresar con clave
        System.out.println("\nFiscal General roberto1 ingresa al sistema----------");
        FiscalGral fiscalGral = (FiscalGral) Ingresar.ingresarConClave("roberto1", "1234", usuarios);
        // Crear convocatorias
        fiscalGral.creaConvocatoria("C01", LocalDate.parse("2021-06-11"), LocalDate.parse("2021-12-31"), (Hashtable<String, Boolean>) docs.clone(), "Descripcion", convocatorias);
        Convocatoria c01 = convocatorias.getConvocatoria("C01");
        fiscalGral.creaConvocatoria("C02", LocalDate.parse("2022-01-11"), LocalDate.parse("2022-06-12"), (Hashtable<String, Boolean>) docs.clone(), "Descripcion", convocatorias);
        Convocatoria c02 = convocatorias.getConvocatoria("C02");
        // Eliminar convocatoria
        fiscalGral.eliminaConvocatoria(c02, convocatorias, presentaciones);
        // Cerrar convocatoria
        fiscalGral.complementaAperturaConvocatoria(c01);
        // Abrir convocatoria
        fiscalGral.complementaAperturaConvocatoria(c01);
        // Actualizar descripcion de convocatoria
        fiscalGral.asignaDescripcionDe(c01,"Nueva descripcion");
        // Quitar documentos requeridos de convocatoria
        fiscalGral.noRequiereDocumentoEn(c01, "Libro Diario");
        // Agregar documentos requeridos de convocatoria
        fiscalGral.requiereDocumentoEn(c01, "Libro Diario");
        // Cambiar fecha de cierre
        fiscalGral.asignaFechaCierreDe(c01, LocalDate.parse("2022-01-01"));
        // Cambiar fecha de apertura
        fiscalGral.asignaFechaAperturaDe(c01, LocalDate.parse("2021-09-12"));
        //Visualizar Convocatorias
        System.out.println("-----Convocatorias");
        for (Convocatoria convocatoria : fiscalGral.getConvocatoriasVisibles(convocatorias)) System.out.println(convocatoria);
        // Asinar cuentadante a municipio
        fiscalGral.asignaNuevoRepresentanteAMunicipio(shinagawa, cuentadante2, usuarios);
        // Nuevo cuentadante crea una presentacion para el municipio
        cuentadante2.creaPresentacion("P03", c01, (Hashtable<String, Boolean>) docs.clone(), presentaciones);
        //Visualizar Municipios
        System.out.println("-----Municipios");
        for (Municipio municipio : fiscalGral.getMunicipiosVisibles(municipios)) System.out.println(municipio);
        // Asignar fiscal a municipio
        fiscalGral.asignaMunicipioAFiscal(shinagawa, fiscal1);
        // Visualizar Usuarios
        System.out.println("-----Usuarios");
        for (Usuario usuario : fiscalGral.getUsuariosVisibles(usuarios)) System.out.println(usuario);
        // Cambiar el estado de apertura de las presentaciones
        fiscalGral.complementaAperturaPresentacion(p02);
        // Visualizar presentaciones
        System.out.println("-----Presentaciones");
        for (Presentacion presentacion : fiscalGral.getPresentacionesVisibles(presentaciones)) System.out.println(presentacion);


//      Fiscal
        // Ingresar con clave
        System.out.println("\nFiscal marcos1 ingresa al sistema----------");
        fiscal1 = (Fiscal) Ingresar.ingresarConClave("marcos1", "1234", usuarios);
        // Visualizar Convocatorias
        System.out.println("-----Convocatorias");
        for (Convocatoria convocatoria : fiscal1.getConvocatoriasVisibles(convocatorias)) System.out.println(convocatoria);
        // Visualizar Presentaciones
        System.out.println("-----Presentaciones");
        for (Presentacion presentacion : fiscal1.getPresentacionesVisibles(presentaciones)) System.out.println(presentacion);
        // Visualizar Municipios
        System.out.println("-----Municipios");
        for (Municipio municipio : fiscal1.getMunicipiosVisibles(municipios)) System.out.println(municipio);
        // Fiscal General actualiza el fiscal asignado a shinagawa
        fiscalGral.asignaMunicipioAFiscal(shinagawa, fiscal2);
        // Fiscal1 ya no puede acceder a las presentaciones porque no le corresponden
        System.out.println("-----Presentaciones despues de ser desasignado del municipio");
        for (Presentacion presentacion : fiscal1.getPresentacionesVisibles(presentaciones)) System.out.println(presentacion);

//      Fiscal2
        // Ingresar con clave
        System.out.println("\nFiscal marcos2 ingresa al sistema----------");
        fiscal2 = (Fiscal) Ingresar.ingresarConClave("marcos2", "1234", usuarios);
        // Visualizar Convocatorias
        System.out.println("-----Convocatorias");
        for (Convocatoria convocatoria : fiscal2.getConvocatoriasVisibles(convocatorias)) System.out.println(convocatoria);
        // Visualizar Presentaciones, ahora fiscal 2 tiene acceso a las presentaciones del municipio asignado
        System.out.println("-----Presentaciones");
        for (Presentacion presentacion : fiscal2.getPresentacionesVisibles(presentaciones)) System.out.println(presentacion);
        // Visualizar Municipios
        System.out.println("-----Municipios");
        for (Municipio municipio : fiscal2.getMunicipiosVisibles(municipios)) System.out.println(municipio);
    }
}
