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

    /* leerPorID ---------------------------------------------------------------------------------------------------- */

    @Test
    void leerPorIDTest() throws SQLException {
        // Dado
        Usuario usuarioEsperado = new Usuario(
                "nombre",
                "username",
                "1234",
                new RolUsuario(RolUsuario.ROL_ADMINISTRADOR_NOMBRE));
        when(usuariosRepositorio.leerPorId(usuarioEsperado.getId())).thenReturn(usuarioEsperado);

        // Cuando
        Usuario usuarioReal = usuariosServicio.leerPorID(usuarioEsperado.getId());

        // Entonces
        assertNotNull(usuarioReal);
        assertEquals(usuarioEsperado.getId(), usuarioReal.getId());
        verify(usuariosRepositorio, times(1)).leerPorId(usuarioEsperado.getId());
    }

    @Test
    void leerPorIDNullTest() throws SQLException {
        // Dado
        when(usuariosRepositorio.leerPorId(anyString())).thenReturn(null);

        // Cuando
        Usuario usuarioEsperado = usuariosServicio.leerPorID(" ");

        // Entonces
        assertNull(usuarioEsperado);
        verify(usuariosRepositorio, times(1)).leerPorId(anyString());
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