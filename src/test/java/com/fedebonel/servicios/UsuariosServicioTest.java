package com.fedebonel.servicios;

import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.respositorios.UsuariosRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuariosServicioTest {

    @Mock
    UsuariosRepositorio usuariosRepositorio;

    UsuariosServicio usuariosServicio;

    @BeforeEach
    void setUp() {
        usuariosServicio = new UsuariosServicio(usuariosRepositorio);
    }

    /* leerTodo ----------------------------------------------------------------------------------------------------- */

    @Test
    void leerTodoTest() throws SQLException {
        // Dado
        LinkedList<Usuario> salidaEsperada = new LinkedList<>();
        salidaEsperada.add(new Usuario("nombre", "username", "1234", new RolUsuario(RolUsuario.ROL_ADMINISTRADOR_NOMBRE)));
        salidaEsperada.add(new Usuario("nombre", "username", "1234", new RolUsuario(RolUsuario.ROL_ADMINISTRADOR_NOMBRE)));
        when(usuariosRepositorio.leerTodo()).thenReturn(salidaEsperada);

        // Cuando
        LinkedList<Usuario> salidaReal = usuariosServicio.leerTodo();

        // Entonces
        assertNotNull(salidaReal);
        assertEquals(salidaEsperada.size(), salidaReal.size());
        verify(usuariosRepositorio, times(1)).leerTodo();
    }

    @Test
    void leerPorID() {
    }

    @Test
    void searchByName() {
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