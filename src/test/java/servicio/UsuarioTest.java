package servicio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test
    void usuarioAdministraDineroYListasIniciales() {
        Usuario usuario = new Usuario(10000);

        assertEquals(10000, usuario.getDinero());
        assertTrue(usuario.getHabitats().isEmpty());
        assertTrue(usuario.getMascotas().isEmpty());
        assertTrue(usuario.getAlimentos().isEmpty());
        assertTrue(usuario.getMedicinas().isEmpty());

        usuario.descontarDinero(2500);
        usuario.agregarDinero(1000);

        assertEquals(8500, usuario.getDinero());
    }
}
