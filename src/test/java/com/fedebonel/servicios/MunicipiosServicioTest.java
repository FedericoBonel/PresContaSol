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

    @Test
    void leerPorID() {
    }

    @Test
    void registrar() {
    }

    @Test
    void eliminar() {
    }

    @Test
    void actualizar() {
    }
}