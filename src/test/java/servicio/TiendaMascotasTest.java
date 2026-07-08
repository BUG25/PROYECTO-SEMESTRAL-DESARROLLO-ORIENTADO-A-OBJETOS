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
    void testCalcularProbabilidadInteres() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();
        Perro perro = new Perro("Firulais");

        // Con salud=100, felicidad=100, reputacion=100
        // Probabilidad = 0.20 + 100/400 + 100/500 + 100/500 = 0.20 + 0.25 + 0.20 + 0.20 = 0.85
        double probabilidad = tiendaMascotas.calcularProbabilidadInteres(perro);
        assertEquals(0.85, probabilidad, 0.001);
    }

    @Test
    void testCalcularPago() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();
        Perro perro = new Perro("Firulais");

        // Con salud=100, felicidad=100
        // Pago = 2000 + 100*18 + 100*12 = 2000 + 1800 + 1200 = 5000
        int pago = tiendaMascotas.calcularPago(perro);
        assertEquals(5000, pago);
    }

    @Test
    void testProcesarVenta() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();
        Usuario usuario = new Usuario(0);
        Perro perro = new Perro("Firulais");
        Habitat habitat = new Habitat(TipoHabitat.PERRERA, 1);
        habitat.ocupar();
        perro.asignarHabitat(habitat);
        usuario.getMascotas().add(perro);

        int pago = tiendaMascotas.calcularPago(perro);
        tiendaMascotas.procesarVenta(usuario, perro, pago);

        assertTrue(usuario.getMascotas().isEmpty());
        assertNull(perro.getHabitatAsignado());
        assertEquals(0, habitat.getOcupados());
        assertEquals(pago, usuario.getDinero());
        assertEquals(100, tiendaMascotas.getReputacion());
    }

    @Test
    void testReputacionPasivaNoSuperaCien() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();

        // Ya está en 100, reputacionPasiva no debería subir más
        tiendaMascotas.reputacionPasiva();
        assertEquals(100, tiendaMascotas.getReputacion());

        // Bajar a 95 y luego subir
        tiendaMascotas.empeorarReputacion(5);
        assertEquals(95, tiendaMascotas.getReputacion());

        tiendaMascotas.reputacionPasiva();
        assertEquals(96, tiendaMascotas.getReputacion());
    }

    @Test
    void testRegistrarMuerteNoBajaDeCero() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();

        tiendaMascotas.empeorarReputacion(100);
        assertEquals(0, tiendaMascotas.getReputacion());

        tiendaMascotas.registrarMuerte();
        assertEquals(0, tiendaMascotas.getReputacion());
    }

    @Test
    void testProbabilidadInteresConLimites() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas(new Random(123));
        Perro perro = new Perro("Firulais");

        // La probabilidad no debe superar 0.90
        double probabilidad = tiendaMascotas.calcularProbabilidadInteres(perro);
        assertTrue(probabilidad <= 0.90);
        assertTrue(probabilidad >= 0.20);
    }
}