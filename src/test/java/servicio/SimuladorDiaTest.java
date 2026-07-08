package servicio;

import model.enfermedades.Pulgas;
import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.mascotas.Perro;
import model.medicinas.Antipulgas;
import model.medicinas.Antiparasitario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorDiaTest {
    @Test
    void avanzarPeriodoDegradaHabitatYMascotaYMejoraReputacionPasiva() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();
        SimuladorDia simulador = new SimuladorDia(tiendaMascotas);
        Usuario usuario = new Usuario(0);
        Habitat habitat = new Habitat(TipoHabitat.PERRERA, 1);
        Perro perro = new Perro("Firulais");
        usuario.getHabitats().add(habitat);
        usuario.getMascotas().add(perro);

        tiendaMascotas.empeorarReputacion(10);
        simulador.avanzarPeriodo(usuario, PeriodoDia.MANANA);

        assertEquals(91, tiendaMascotas.getReputacion());
        assertEquals(96, habitat.getHigiene());
        assertEquals(90, perro.getComida());
        assertEquals(95, perro.getFelicidad());
    }

    @Test
    void avanzarDiaEjecutaTresPeriodos() {
        SimuladorDia simulador = new SimuladorDia(new TiendaMascotas());
        Usuario usuario = new Usuario(0);
        Habitat habitat = new Habitat(TipoHabitat.PERRERA, 1);
        Perro perro = new Perro("Firulais");
        usuario.getHabitats().add(habitat);
        usuario.getMascotas().add(perro);

        simulador.avanzarDia(usuario);

        assertEquals(88, habitat.getHigiene());
        assertEquals(70, perro.getComida());
        assertEquals(85, perro.getFelicidad());
    }

    @Test
    void avanzarPeriodoEliminaMascotasMuertasYBajaReputacion() {
        TiendaMascotas tiendaMascotas = new TiendaMascotas();
        SimuladorDia simulador = new SimuladorDia(tiendaMascotas);
        Usuario usuario = new Usuario(0);
        Perro perro = new Perro("Firulais");
        int comidaAntesDeMorir = perro.getComida();
        perro.morir();
        usuario.getMascotas().add(perro);

        simulador.avanzarPeriodo(usuario, PeriodoDia.MANANA);

        assertTrue(usuario.getMascotas().isEmpty());
        assertEquals(comidaAntesDeMorir, perro.getComida());
        assertEquals(85, tiendaMascotas.getReputacion());
    }

    @Test
    void tratarMascotaCuraSoloConMedicinaCorrectaYLimpiaContacto() {
        SimuladorDia simulador = new SimuladorDia(new TiendaMascotas());
        Perro perro = new Perro("Firulais");
        perro.contagiar(new Pulgas());
        perro.registrarContactoConEnfermo();

        assertFalse(simulador.tratarMascota(perro, new Antiparasitario()));
        assertTrue(perro.estaEnferma());
        assertEquals(1, perro.getDiasContactoSinTratar());

        assertTrue(simulador.tratarMascota(perro, new Antipulgas()));
        assertFalse(perro.estaEnferma());
        assertEquals(0, perro.getDiasContactoSinTratar());
    }
}
