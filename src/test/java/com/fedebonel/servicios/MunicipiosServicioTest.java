package com.fedebonel.servicios;

import com.fedebonel.modelo.evento.Convocatoria;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.respositorios.ConvocatoriasRepositorio;
import com.fedebonel.respositorios.MunicipiosRepositorio;
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
class MunicipiosServicioTest {

    @Mock
    MunicipiosRepositorio municipiosRepositorio;

    MunicipiosServicio municipiosServicio;

    @BeforeEach
    void setUp() {
        municipiosServicio = new MunicipiosServicio(municipiosRepositorio);
    }

    /* leerTodo ----------------------------------------------------------------------------------------------------- */

    @Test
    void leerTodoTest() throws SQLException {
        // Dado
        LinkedList<Municipio> salidaEsperada = new LinkedList<>();
        salidaEsperada.add(new Municipio("m1", "nombre1", 1));
        salidaEsperada.add(new Municipio("m2", "nombre1", 1));
        when(municipiosRepositorio.leerTodo()).thenReturn(salidaEsperada);

        // Cuando
        LinkedList<Municipio> salidaReal = municipiosServicio.leerTodo();

        // Entonces
        assertNotNull(salidaReal);
        assertEquals(salidaEsperada.size(), salidaReal.size());
        verify(municipiosRepositorio, times(1)).leerTodo();
    }

    /* leerPorID ---------------------------------------------------------------------------------------------------- */

    @Test
    void leerPorIDTest() throws SQLException {
        // Dado
        Municipio municipioEsperado = new Municipio("m1", "nombre1", 1);
        when(municipiosRepositorio.leerPorId(municipioEsperado.getId())).thenReturn(municipioEsperado);

        // Cuando
        Municipio municipioReal = municipiosServicio.leerPorID(municipioEsperado.getId());

        // Entonces
        assertNotNull(municipioReal);
        assertEquals(municipioEsperado.getId(), municipioReal.getId());
        verify(municipiosRepositorio, times(1)).leerPorId(municipioEsperado.getId());
    }

    @Test
    void leerPorIDNullTest() throws SQLException {
        // Dado
        when(municipiosRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Municipio municipioReal = municipiosServicio.leerPorID(" ");

        // Entonces
        assertNull(municipioReal);
        verify(municipiosRepositorio, times(1)).leerPorId(anyString());
    }

    /* registrar ---------------------------------------------------------------------------------------------------- */

    @Test
    void registrarTest() throws SQLException {
        // Dado
        when(municipiosRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Municipio municipio = new Municipio("m1", "nombre1", 1);
        municipiosServicio.registrar(municipio);

        // Entonces
        verify(municipiosRepositorio, times(1)).guardar(municipio);
    }

    @Test
    void registrarFalloTest() throws SQLException {
        // Dado
        Municipio municipio = new Municipio("m1", "nombre1", 1);
        when(municipiosRepositorio.leerPorId(municipio.getId())).thenReturn(municipio);

        // Cuando, Entonces
        assertThrows(IllegalArgumentException.class, () -> municipiosServicio.registrar(municipio));
        verify(municipiosRepositorio, times(0)).guardar(any());
    }

    @Test
    void eliminar() {
    }

    @Test
    void actualizar() {
    }
}