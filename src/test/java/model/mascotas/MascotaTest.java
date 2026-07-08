package model.mascotas;

import model.alimentos.CalidadAlimento;
import model.alimentos.Escamas;
import model.alimentos.Pellet;
import model.alimentos.PiensoPerro;
import model.enfermedades.Infeccion;
import model.enfermedades.Pulgas;
import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.medicinas.Antibioticos;
import model.medicinas.Antipulgas;
import org.junit.jupiter.api.Test;
import servicio.PeriodoDia;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MascotaTest {
    @Test
    void cadaMascotaTieneHabitatRequerido() {
        assertEquals(TipoHabitat.PERRERA, new Perro("Firulais").getTipoHabitatRequerido());
        assertEquals(TipoHabitat.ARENERO, new Gato("Michi").getTipoHabitatRequerido());
        assertEquals(TipoHabitat.PECERA, new Pez("Nemo").getTipoHabitatRequerido());
        assertEquals(TipoHabitat.JAULA_HAMSTER, new Hamster("Hamtaro").getTipoHabitatRequerido());
        assertEquals(TipoHabitat.JAULA_PAJARO, new Pajaro("Piolin").getTipoHabitatRequerido());
    }

    @Test
    void cadaMascotaExponeSuTipo() {
        assertEquals(TipoMascota.PERRO, new Perro("Firulais").getTipoMascota());
        assertEquals(TipoMascota.GATO, new Gato("Michi").getTipoMascota());
        assertEquals(TipoMascota.PEZ, new Pez("Nemo").getTipoMascota());
        assertEquals(TipoMascota.HAMSTER, new Hamster("Hamtaro").getTipoMascota());
        assertEquals(TipoMascota.PAJARO, new Pajaro("Piolin").getTipoMascota());
    }

    @Test
    void inicializarStatsAleatoriosDejaValoresEnRangoEsperado() {
        Perro perro = new Perro("Firulais");

        perro.inicializarStatsAleatorios(new Random(1));

        assertTrue(perro.getSalud() >= 30 && perro.getSalud() <= 60);
        assertTrue(perro.getFelicidad() >= 30 && perro.getFelicidad() <= 60);
        assertTrue(perro.getComida() >= 30 && perro.getComida() <= 60);
    }

    @Test
    void alimentarAumentaComidaSaludYRegistraCalidadSinPasarDeCien() {
        Perro perro = new Perro("Firulais");

        perro.degradarConElTiempo(PeriodoDia.MANANA);
        perro.alimentar(new PiensoPerro(CalidadAlimento.BALANCEADO));

        assertEquals(100, perro.getComida());
        assertEquals(100, perro.getSalud());
        assertEquals(CalidadAlimento.BALANCEADO, perro.getUltimaCalidadAlimento());
    }

    @Test
    void alimentarConAlimentoIncompatibleLanzaExcepcionYNoCambiaEstado() {
        Perro perro = new Perro("Firulais");

        assertThrows(IllegalArgumentException.class,
                () -> perro.alimentar(new Escamas(CalidadAlimento.ESTANDAR)));

        assertEquals(100, perro.getComida());
        assertEquals(100, perro.getSalud());
        assertNull(perro.getUltimaCalidadAlimento());
    }

    @Test
    void jugarSubeFelicidadYBajaComidaDentroDeLimites() {
        Perro perro = new Perro("Firulais");

        perro.jugar();

        assertEquals(100, perro.getFelicidad());
        assertEquals(95, perro.getComida());
    }

    @Test
    void degradarConElTiempoReduceStatsYPuedeMatarPorHambre() {
        Pez pez = new Pez("Nemo");

        pez.degradarConElTiempo(PeriodoDia.MANANA);
        assertEquals(91, pez.getComida());
        assertEquals(95, pez.getFelicidad());

        for (int i = 0; i < 20; i++) {
            pez.degradarConElTiempo(PeriodoDia.NOCHE);
        }

        assertTrue(pez.estaMuerta());
        assertEquals(0, pez.getSalud());
    }

    @Test
    void contactoConEnfermoSeRegistraYSePuedeTratar() {
        Perro perro = new Perro("Firulais");

        perro.registrarContactoConEnfermo();
        perro.registrarContactoConEnfermo();
        assertEquals(2, perro.getDiasContactoSinTratar());

        perro.tratarContacto();
        assertEquals(0, perro.getDiasContactoSinTratar());
    }

    @Test
    void contagiarCurarYValidarMedicinaCorrecta() {
        Perro perro = new Perro("Firulais");

        perro.contagiar(new Pulgas());

        assertTrue(perro.estaEnferma());
        assertTrue(perro.puedeSerCuradaCon(new Antipulgas()));
        assertFalse(perro.puedeSerCuradaCon(new Antibioticos()));

        perro.curar();
        assertFalse(perro.estaEnferma());
    }

    @Test
    void aplicarDanoDeEnfermedadReduceSaludYMataSiLlegaACero() {
        Perro perro = new Perro("Firulais");
        perro.contagiar(new Infeccion());

        perro.aplicarDañoDeEnfermedad();
        assertEquals(70, perro.getSalud());

        perro.aplicarDañoDeEnfermedad();
        perro.aplicarDañoDeEnfermedad();
        perro.aplicarDañoDeEnfermedad();

        assertTrue(perro.estaMuerta());
        assertFalse(perro.estaEnferma());
    }

    @Test
    void morirLiberaHabitatYReseteaEstadoDeEnfermedadYContacto() {
        Perro perro = new Perro("Firulais");
        Habitat habitat = new Habitat(TipoHabitat.PERRERA, 1);
        habitat.ocupar();
        perro.asignarHabitat(habitat);
        perro.contagiar(new Pulgas());
        perro.registrarContactoConEnfermo();

        perro.morir();
        perro.morir();

        assertTrue(perro.estaMuerta());
        assertNull(perro.getHabitatAsignado());
        assertEquals(0, habitat.getOcupados());
        assertFalse(perro.estaEnferma());
        assertEquals(0, perro.getDiasContactoSinTratar());
    }

    @Test
    void calcularRiesgoEnfermedadConsideraContactoHigieneYAlimento() {
        Hamster hamster = new Hamster("Hamtaro");
        Habitat habitat = new Habitat(TipoHabitat.JAULA_HAMSTER, 1);

        for (int i = 0; i < 20; i++) {
            habitat.degradarConElTiempo();
        }
        hamster.registrarContactoConEnfermo();
        hamster.alimentar(new Pellet(CalidadAlimento.ESTANDAR));

        assertEquals(0.50, hamster.calcularRiesgoEnfermedad(habitat), 0.0001);

        for (int i = 0; i < 10; i++) {
            hamster.registrarContactoConEnfermo();
        }
        assertEquals(0.85, hamster.calcularRiesgoEnfermedad(habitat), 0.0001);
    }
}
