package com.fedebonel.servicios;

import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.respositorios.ConvocatoriasRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests para el servicio de convocatorias
 */
@ExtendWith(MockitoExtension.class)
class ConvocatoriasServicioTest {

    @Mock
    ConvocatoriasRepositorio convocatoriasRepositorio;

    ConvocatoriasServicio convocatoriasServicio;

    @BeforeEach
    void setUp() {
        convocatoriasServicio = new ConvocatoriasServicio(convocatoriasRepositorio);
    }

    /* leerTodo ----------------------------------------------------------------------------------------------------- */

    @Test
    void leerTodoTest() throws SQLException {
        // Dado
        LinkedList<Convocatoria> salidaEsperada = new LinkedList<>();
        salidaEsperada.add(new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), ""));
        salidaEsperada.add(new Convocatoria("c2",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), ""));
        when(convocatoriasRepositorio.leerTodo()).thenReturn(salidaEsperada);

        // Cuando
        LinkedList<Convocatoria> salidaReal = convocatoriasServicio.leerTodo();

        // Entonces
        assertNotNull(salidaReal);
        assertEquals(salidaEsperada.size(), salidaReal.size());
        verify(convocatoriasRepositorio, times(1)).leerTodo();
    }

    /* leerPorID ---------------------------------------------------------------------------------------------------- */

    @Test
    void leerPorIDTest() throws SQLException {
        // Dado
        Convocatoria convocatoriaEsperada
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        when(convocatoriasRepositorio.leerPorId(convocatoriaEsperada.getId())).thenReturn(convocatoriaEsperada);

        // Cuando
        Convocatoria convocatoriaReal = convocatoriasServicio.leerPorID(convocatoriaEsperada.getId());

        // Entonces
        assertNotNull(convocatoriaReal);
        assertEquals(convocatoriaEsperada.getId(), convocatoriaReal.getId());
        verify(convocatoriasRepositorio, times(1)).leerPorId(convocatoriaEsperada.getId());
    }

    @Test
    void leerPorIDNullTest() throws SQLException {
        // Dado
        when(convocatoriasRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Convocatoria convocatoriaReal = convocatoriasServicio.leerPorID(" ");

        // Entonces
        assertNull(convocatoriaReal);
        verify(convocatoriasRepositorio, times(1)).leerPorId(anyString());
    }

    /* registrar ---------------------------------------------------------------------------------------------------- */

    @Test
    void registrarTest() throws SQLException {
        // Dado
        when(convocatoriasRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        convocatoriasServicio.registrar(convocatoria);

        // Entonces
        verify(convocatoriasRepositorio, times(1)).guardar(convocatoria);
    }

    @Test
    void registrarFalloTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        when(convocatoriasRepositorio.leerPorId(convocatoria.getId())).thenReturn(convocatoria);

        // Cuando, Entonces
        assertThrows(IllegalArgumentException.class, () -> convocatoriasServicio.registrar(convocatoria));
        verify(convocatoriasRepositorio, times(0)).guardar(any());
    }

    /* eliminar ----------------------------------------------------------------------------------------------------- */

    @Test
    void eliminarTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        when(convocatoriasRepositorio.leerPorId(convocatoria.getId())).thenReturn(convocatoria);

        // Cuando
        convocatoriasServicio.eliminar(convocatoria);

        // Entonces
        verify(convocatoriasRepositorio, times(1)).eliminarPorId(convocatoria.getId());
    }

    @Test
    void eliminarFalloTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        when(convocatoriasRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando, entonces
        assertThrows(IllegalArgumentException.class, () -> convocatoriasServicio.eliminar(convocatoria));
        verify(convocatoriasRepositorio, times(0)).eliminarPorId(anyString());
    }

    /* actualizar --------------------------------------------------------------------------------------------------- */

    @Test
    void actualizarTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        String campo = "descripcion";
        String valor = "nuevaDescripcion";
        when(convocatoriasRepositorio.leerPorId(convocatoria.getId())).thenReturn(convocatoria);

        // Cuando
        convocatoriasServicio.actualizar(convocatoria, campo, valor);

        // Entonces
        verify(convocatoriasRepositorio, times(1)).actualizarPorId(convocatoria.getId(), campo, valor);
    }

    @Test
    void actualizarFalloTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        String campo = "descripcion";
        String valor = "nuevaDescripcion";
        when(convocatoriasRepositorio.leerPorId(convocatoria.getId())).thenReturn(null);

        // Cuando, entonces
        assertThrows(IllegalArgumentException.class,
                () -> convocatoriasServicio.actualizar(convocatoria, campo, valor));
        verify(convocatoriasRepositorio, times(0)).actualizarPorId(anyString(), anyString(), anyString());
    }

    /* agregarDocumento --------------------------------------------------------------------------------------------- */

    @Test
    void agregarDocumentoTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        String documento = "documento";
        when(convocatoriasRepositorio.leerPorId(convocatoria.getId())).thenReturn(convocatoria);

        // Cuando
        convocatoriasServicio.agregarDocumento(convocatoria, documento);

        // Entonces
        verify(convocatoriasRepositorio, times(1)).agregarDocConvocatoria(convocatoria, documento);
    }

    @Test
    void agregarDocumentoFalloTest() throws SQLException {
        // Dado
        Convocatoria convocatoria
                = new Convocatoria("c1",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 12),
                new LinkedList<>(), "");
        String documento = "documento";
        when(convocatoriasRepositorio.leerPorId(convocatoria.getId())).thenReturn(null);

        // Cuando, entonces
        assertThrows(IllegalArgumentException.class,
                () -> convocatoriasServicio.agregarDocumento(convocatoria, documento));
        verify(convocatoriasRepositorio, times(0)).agregarDocConvocatoria(convocatoria, documento);
    }


}