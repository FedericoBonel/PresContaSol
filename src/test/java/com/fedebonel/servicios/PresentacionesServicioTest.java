package com.fedebonel.servicios;

import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.modelo.evento.Presentacion;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.respositorios.PresentacionesRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresentacionesServicioTest {

    // Convocatoria a la cual se presentara
    Convocatoria convocatoria;
    // Autor de la presentacion
    Usuario usuario;
    // Municipio de la presentacion
    Municipio municipio;


    @Mock
    PresentacionesRepositorio presentacionesRepositorio;

    PresentacionesServicio presentacionesServicio;

    @BeforeEach
    void setUp() {
        presentacionesServicio = new PresentacionesServicio(presentacionesRepositorio);
        convocatoria = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        usuario = new Usuario("nombre1",
                "username",
                "1234",
                new RolUsuario(RolUsuario.ROL_CUENTADANTE_NOMBRE));
        municipio = new Municipio("m1", "nombre1", 1);
    }

    /* leerTodo ----------------------------------------------------------------------------------------------------- */

    @Test
    void leerTodoTest() throws SQLException {
        // Dado
        LinkedList<Presentacion> salidaEsperada = new LinkedList<>();
        salidaEsperada.add(new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>()));
        salidaEsperada.add(new Presentacion("p2",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>()));
        when(presentacionesRepositorio.leerTodo()).thenReturn(salidaEsperada);

        // Cuando
        LinkedList<Presentacion> salidaReal = presentacionesServicio.leerTodo();

        // Entonces
        assertNotNull(salidaReal);
        assertEquals(salidaEsperada.size(), salidaReal.size());
        verify(presentacionesRepositorio, times(1)).leerTodo();
    }

    /* leerPorID ---------------------------------------------------------------------------------------------------- */

    @Test
    void leerPorIDTest() throws SQLException {
        // Dado
        Presentacion presentacionEsperada = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        when(presentacionesRepositorio.leerPorId(presentacionEsperada.getId())).thenReturn(presentacionEsperada);

        // Cuando
        Presentacion presentacionReal = presentacionesServicio.leerPorID(presentacionEsperada.getId());

        // Entonces
        assertNotNull(presentacionReal);
        assertEquals(presentacionEsperada.getId(), presentacionReal.getId());
        verify(presentacionesRepositorio, times(1)).leerPorId(presentacionEsperada.getId());
    }

    @Test
    void leerPorIDNullTest() throws SQLException {
        // Dado
        when(presentacionesRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Presentacion presentacionReal = presentacionesServicio.leerPorID(" ");

        // Entonces
        assertNull(presentacionReal);
        verify(presentacionesRepositorio, times(1)).leerPorId(anyString());
    }

    /* registrar ---------------------------------------------------------------------------------------------------- */

    @Test
    void registrarTest() throws SQLException {
        // Dado
        when(presentacionesRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        presentacionesServicio.registrar(presentacion);

        // Entonces
        verify(presentacionesRepositorio, times(1)).guardar(presentacion);
    }

    @Test
    void registrarFalloTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        when(presentacionesRepositorio.leerPorId(presentacion.getId())).thenReturn(presentacion);

        // Cuando, Entonces
        assertThrows(IllegalArgumentException.class, () -> presentacionesServicio.registrar(presentacion));
        verify(presentacionesRepositorio, times(0)).guardar(any());
    }

    /* eliminar ----------------------------------------------------------------------------------------------------- */

    @Test
    void eliminarTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        when(presentacionesRepositorio.leerPorId(presentacion.getId())).thenReturn(presentacion);

        // Cuando
        presentacionesServicio.eliminar(presentacion);

        // Entonces
        verify(presentacionesRepositorio, times(1)).eliminarPorId(presentacion.getId());
    }

    @Test
    void eliminarFalloTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        when(presentacionesRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando, entonces
        assertThrows(IllegalArgumentException.class, () -> presentacionesServicio.eliminar(presentacion));
        verify(presentacionesRepositorio, times(0)).eliminarPorId(anyString());
    }

    /* actualizar --------------------------------------------------------------------------------------------------- */

    @Test
    void actualizarTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        String campo = "apertura";
        String valor = "false";
        when(presentacionesRepositorio.leerPorId(presentacion.getId())).thenReturn(presentacion);

        // Cuando
        presentacionesServicio.actualizar(presentacion, campo, valor);

        // Entonces
        verify(presentacionesRepositorio, times(1)).actualizarPorId(presentacion.getId(), campo, valor);
    }

    @Test
    void actualizarFalloTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        String campo = "apertura";
        String valor = "false";
        when(presentacionesRepositorio.leerPorId(presentacion.getId())).thenReturn(null);

        // Cuando, entonces
        assertThrows(IllegalArgumentException.class,
                () -> presentacionesServicio.actualizar(presentacion, campo, valor));
        verify(presentacionesRepositorio, times(0)).actualizarPorId(anyString(), anyString(), anyString());
    }

    /* agregarDocumento --------------------------------------------------------------------------------------------- */

    @Test
    void agregarDocumentoTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        String documento = "documento";
        when(presentacionesRepositorio.leerPorId(presentacion.getId())).thenReturn(presentacion);

        // Cuando
        presentacionesServicio.agregarDocumento(presentacion, documento);

        // Entonces
        verify(presentacionesRepositorio, times(1)).agregarDocPresentacion(presentacion, documento);
    }

    @Test
    void agregarDocumentoFalloTest() throws SQLException {
        // Dado
        Presentacion presentacion = new Presentacion("p1",
                LocalDate.of(2022, 2, 11),
                true,
                convocatoria,
                usuario,
                municipio,
                new LinkedList<>());
        String documento = "documento";
        when(presentacionesRepositorio.leerPorId(presentacion.getId())).thenReturn(null);

        // Cuando, entonces
        assertThrows(IllegalArgumentException.class,
                () -> presentacionesServicio.agregarDocumento(presentacion, documento));
        verify(presentacionesRepositorio, times(0)).agregarDocPresentacion(presentacion, documento);
    }

    @Test
    void eliminarDocumento() {
    }
}