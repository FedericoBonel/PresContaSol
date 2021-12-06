package abstraccionNegocio;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Clase que abstrae a todos los Fiscales Generales del sistema
 *
 * @author Bonel Federico
 */
public class FiscalGral extends Usuario {
    /**
     * Constante que mantiene el tipo de usuario
     */
    public static final String TIPO_FISCAL_GRAL = "Fiscal General";

    /**
     * Constructor para instanciar Fiscales Generales
     *
     * @param usuario Identificador del usuario: Debe tener entre 1 y 10 caracteres inclusive
     * @param clave   Clave a asignar para el ingreso al sistema del usuario: Debe tener entre 4 y 8 caracteres inclusive
     */
    public FiscalGral(String usuario, String clave) throws IllegalArgumentException {
        super(usuario, clave);
    }

    /**
     * Crea una convocatoria y la agrega a la coleccion de convocatorias pasada
     *
     * @param id            Identificador alfanumerico unico de convocatoria: Puede tener desde 1 caracter hasta 100 caracteres
     * @param fechaInicio   Fecha de apertura planeada como objeto LocalDate
     * @param fechaCierre   Fecha de cierre de la convocatoria como objeto LocalDate: No puede ser antes de fechaInicio
     * @param docsReq       Documentos requeridos de la convocatoria para las presentaciones: Solo pueden ser los establecidos
     *                      en Evento.DOCUMENTOS_OPCIONES
     * @param descripcion   Descripcion de la convocatoria: Debe tener como maximo 2000 caracteres
     * @param convocatorias Coleccion de todas las convocatorias donde se agregara la nueva convocatoria
     * @throws IllegalArgumentException si alguno de los parametros es invalido o la convocatoria se halla registrada
     */
    public void creaConvocatoria(String id, LocalDate fechaInicio,
                                 LocalDate fechaCierre, Hashtable<String, Boolean> docsReq, String descripcion, ColeccionConvocatorias convocatorias) throws IllegalArgumentException {
        Convocatoria nuevaConvocatoria = new Convocatoria(id, fechaInicio, fechaCierre, docsReq, descripcion);
        convocatorias.addConvocatoria(nuevaConvocatoria);
    }

    /**
     * Elimina la convocatoria de la colección pasada
     *
     * @param convocatoria   Convocatoria que se desea eliminar como objeto
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @param convocatorias  Coleccion de todas las convocatorias desde donde se eliminara la convocatoria
     * @throws IllegalArgumentException si convocatoria no se halla en lista de convocatorias
     * @throws IllegalCallerException   si el usuario no puede eliminar la convocatoria
     */
    public void eliminaConvocatoria(Convocatoria convocatoria,
                                    ColeccionConvocatorias convocatorias, ColeccionPresentaciones presentaciones) throws IllegalArgumentException {
        if (!puedeEliminarConvocatoria(convocatoria, presentaciones))
            throw new IllegalCallerException("Convocatoria no eliminable");
        convocatorias.removeConvocatoria(convocatoria, presentaciones);
    }

    /**
     * Verifica si la convocatoria puede ser eliminada por el usuario Fiscal general
     *
     * @param convocatoria Convocatoria a verificar
     * @return true si la convocatoria no tiene presentaciones, falso de otra forma
     */
    protected boolean puedeEliminarConvocatoria(Convocatoria convocatoria, ColeccionPresentaciones presentaciones) {
        return convocatoria.getSusPresentacionesDe(presentaciones).isEmpty();
    }

    /**
     * Complementa el estado actual de apertura de la convocatoria. Si estaba cerrada, se abre y si estaba abierta, se cierra
     *
     * @param convocatoria Convocatoria en la cual se desea complementar el estado
     */
    public void complementaAperturaConvocatoria(Convocatoria convocatoria) {
        convocatoria.setAbierto(!convocatoria.isAbierto());
    }

    /**
     * Establece el documento de la convocatoria como requerido
     *
     * @param convocatoria Convocatoria a la cual se desea agregar el documento
     * @param documento    Documento a establecer como requerido: debe estar en la lista de opciones
     * @throws IllegalArgumentException Si el documento no esta en la lista de opciones
     */
    public void requiereDocumentoEn(Convocatoria convocatoria, String documento) throws IllegalArgumentException {
        convocatoria.marcaElDocumentoComoVerdadero(documento);
    }

    /**
     * Establece el documento de la convocatoria como no requerido
     *
     * @param convocatoria Convocatoria de la cual se desea eliminar el documento
     * @param documento    Documento a establecer como no requerido: debe estar en la lista de opciones
     * @throws IllegalArgumentException Si el documento no esta en la lista de opciones
     */
    public void noRequiereDocumentoEn(Convocatoria convocatoria, String documento) throws IllegalArgumentException {
        convocatoria.marcaDocumentoComoFalso(documento);
    }

    /**
     * Establece la fecha pasada como parametro como fecha de cierre en la convocatoria
     *
     * @param convocatoria Convocatoria en la cual se desea establecer fecha de cierre
     * @param fechaCierre  Fecha de cierre como ObjetoLocal date a establecer
     * @throws IllegalArgumentException Si la fecha de cierre es no valida: Antes de la fecha de inicio
     */
    public void asignaFechaCierreDe(Convocatoria convocatoria, LocalDate fechaCierre) throws IllegalArgumentException {
        convocatoria.setFechaCierre(fechaCierre);
    }

    /**
     * Establece la fecha pasada como parámetro como fecha de apertura en la convocatoria especificada
     *
     * @param convocatoria  Convocatoria en la cual se desea establecer fecha de apertura
     * @param fechaApertura Fecha de apertura como ObjetoLocal date a establecer
     * @throws IllegalArgumentException Si la fecha de apertura es invalida
     */
    public void asignaFechaAperturaDe(Convocatoria convocatoria, LocalDate fechaApertura) throws IllegalArgumentException {
        convocatoria.setFechaInicio(fechaApertura);
    }

    /**
     * Asigna la descripcion a la convocatoria pasada
     *
     * @param convocatoria Convocatoria en la cual se desea establecer una nueva descripcion
     * @param descripcion  Descripcion a establecer como String
     * @throws IllegalArgumentException Si la descripcion no cumple con las condiciones de formato: Maximo 2000 caracteres
     */
    public void asignaDescripcionDe(Convocatoria convocatoria, String descripcion) throws IllegalArgumentException {
        convocatoria.setDescripcion(descripcion);
    }

    /**
     * Devuelve los usuarios que deben ser visibles para los fiscales generales de la colección pasada de usuarios, estos son:
     * Los usuarios Fiscales (para asignarlos a los municipios)
     *
     * @param usuarios Coleccion de todas los usuarios del sistema
     * @return Un linked list con todas los usuarios que corresponden al usuario Fiscal Gral
     */
    public LinkedList<Usuario> getUsuariosVisibles(ColeccionUsuarios usuarios) {
        LinkedList<Usuario> usuariosVisibles = new LinkedList<>();
        for (String usuarioId : usuarios.getHashtable().keySet()) {
            if (usuarios.getUsuario(usuarioId) instanceof Fiscal) {
                usuariosVisibles.add(usuarios.getUsuario(usuarioId));
            }
        }
        return usuariosVisibles;
    }

    /**
     * Asigna el municipio al fiscal
     *
     * @param municipio Municipio que se desee asignar
     * @param fiscal    Fiscal que se desee asignar
     */
    public void asignaMunicipioAFiscal(Municipio municipio, Fiscal fiscal) {
        municipio.tomaNuevoSupervisorFiscal(fiscal);
    }

    /**
     * Devuelve las presentaciones que deben ser visibles para los fiscales generales y administradores, estas son:
     * todas las presentaciones del sistema
     *
     * @param presentaciones Coleccion de todas las presentaciones del sistema
     * @return Un linked list con todas las presentaciones que corresponden al usuario Fiscal Gral y Administrador
     */
    @Override
    public LinkedList<Presentacion> getPresentacionesVisibles(ColeccionPresentaciones presentaciones) {
        return presentaciones.getPresentacionesLinkedList();
    }

    /**
     * Verifica si la presentacion puede ser eliminada por el Fiscal Gral
     *
     * @param presentacion Presentacion a verificar
     * @return false, el Fiscal Gral nunca puede eliminar las presentaciones
     */
    @Override
    protected boolean puedeEliminarPresentacion(Presentacion presentacion) {
        return false;
    }

    /**
     * Complementa el estado actual de apertura de la presentacion
     *
     * @param presentacion Presentacion en la cual se desea complementar el estado
     */
    public void complementaAperturaPresentacion(Presentacion presentacion) {
        presentacion.setAbierto(!presentacion.isAbierto());
    }

    /**
     * Verifica si el Fiscal Gral puede entregar el documento en la presentacion
     *
     * @param presentacion Presentacion a la cual se desea agregar el documento
     * @param documento    Documento a establecer como entregado
     * @return false, el Fiscal Gral nunca puede entregar documentos
     */
    @Override
    protected boolean puedeEntregarDocumentoA(Presentacion presentacion, String documento) {
        return false;
    }

    /**
     * Verifica si el Fiscal Gral puede retirar el documento de la presentacion
     *
     * @param presentacion Presentacion de la cual se desea retirar el documento
     * @param documento    Documento a retirar
     * @return false, el Fiscal Gral nunca puede retirar documentos
     */
    @Override
    protected boolean puedeRetirarDocumentoDe(Presentacion presentacion, String documento) {
        return false;
    }

    /**
     * Devuelve los municipios que deben ser visibles para los Fiscales Grales y Administradores, estos son:
     * Todos los municipios del sistema
     *
     * @param municipios Coleccion de todos los municipios del sistema
     * @return Un linked list con los municipios que corresponden al usuario Fiscal Gral y Administrador
     */
    @Override
    public LinkedList<Municipio> getMunicipiosVisibles(ColeccionMunicipios municipios) {
        return municipios.getMunicipiosLinkedList();
    }

    /**
     * Asigna el cuentadante pasado como representante del municipio en las presentaciones realizadas para el.
     *
     * @param municipio     Municipio a actualizar
     * @param representante Cuentadante a asignar
     * @param usuarios      Coleccion de todos los usuarios del sistema
     */
    public void asignaNuevoRepresentanteAMunicipio(Municipio municipio, Cuentadante representante, ColeccionUsuarios usuarios) {
        representante.tomaResponsabilidadDeMunicipio(municipio, usuarios);
    }

    /**
     * Remueve el municipio actual del cuentadante
     *
     * @param representante Cuentadante a actualizar
     */
    public void removeMunicipioDeRepresentante(Cuentadante representante) {
        representante.abandonaMunicipio();
    }

    @Override
    public String toStringConClave() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + super.getClave() + '\'' +
                ", tipo='" + TIPO_FISCAL_GRAL + '\'' +
                "} ";
    }

    @Override
    public String toString() {
        return "{" +
                "nombre de usuario:" + super.getId() +
                ", clave='" + "ESCONDIDA" + '\'' +
                ", tipo='" + TIPO_FISCAL_GRAL + '\'' +
                "} ";
    }
}
