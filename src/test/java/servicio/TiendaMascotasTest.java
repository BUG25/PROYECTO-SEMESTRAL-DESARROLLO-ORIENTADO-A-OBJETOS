package servicio;

import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.mascotas.Perro;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TiendaMascotasTest {
    @Test
    void reputacionSeMantieneEntreCeroYCien() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();

        tiendaMascotas.mejorarReputacion(50);
        assertEquals(100, tiendaMascotas.getReputacion());

        tiendaMascotas.empeorarReputacion(250);
        assertEquals(0, tiendaMascotas.getReputacion());

        tiendaMascotas.reputacionPasiva();
        assertEquals(1, tiendaMascotas.getReputacion());

        tiendaMascotas.registrarMuerte();
        assertEquals(0, tiendaMascotas.getReputacion());
    }

    @Test
    void venderMascotaRetornaFalseSiNoPerteneceAlUsuario() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();
        Usuario usuario = new Usuario(0);

        assertFalse(tiendaMascotas.venderMascota(usuario, new Perro("Firulais")));
        assertEquals(0, usuario.getDinero());
    }

    @Test
    void compradorInteresadoEsDeterministicoConRandomInyectado() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas(new Random(123));

        assertTrue(tiendaMascotas.hayCompradorInteresado(new Perro("Firulais")));
    }

    @Test
    void siVentaResultaExitosaQuitaMascotaLiberaHabitatYPaga() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas(new Random(123));
        Usuario usuario = new Usuario(0);
        Perro perro = new Perro("Firulais");
        Habitat habitat = new Habitat(TipoHabitat.PERRERA, 1);
        habitat.ocupar();
        perro.asignarHabitat(habitat);
        usuario.getMascotas().add(perro);

        assertTrue(tiendaMascotas.venderMascota(usuario, perro));

        assertTrue(usuario.getMascotas().isEmpty());
        assertNull(perro.getHabitatAsignado());
        assertEquals(0, habitat.getOcupados());
        assertEquals(5000, usuario.getDinero());
        assertEquals(100, tiendaMascotas.getReputacion());
    }
}
