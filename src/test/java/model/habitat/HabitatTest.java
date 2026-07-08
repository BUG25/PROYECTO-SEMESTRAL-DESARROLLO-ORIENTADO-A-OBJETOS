package model.habitat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HabitatTest {
    @Test
    void tipoHabitatExponeValorYCapacidad() {
        assertEquals(9500, TipoHabitat.PERRERA.getValor());
        assertEquals(3, TipoHabitat.PERRERA.getCapacidadMaxima());
        assertEquals(15000, TipoHabitat.PECERA.getValor());
        assertEquals(5, TipoHabitat.PECERA.getCapacidadMaxima());
        assertEquals(12000, TipoHabitat.JAULA_PAJARO.getValor());
        assertEquals(4, TipoHabitat.JAULA_PAJARO.getCapacidadMaxima());
    }

    @Test
    void ocuparDesocuparYValidarCapacidad() {
        Habitat habitat = new Habitat(TipoHabitat.ARENERO, 1);

        assertEquals(TipoHabitat.ARENERO, habitat.getTipo());
        assertEquals(0, habitat.getOcupados());
        assertTrue(habitat.tieneEspaciosLibre());

        habitat.ocupar();

        assertEquals(1, habitat.getOcupados());
        assertFalse(habitat.tieneEspaciosLibre());
        assertThrows(IllegalArgumentException.class, habitat::ocupar);

        habitat.desocupar();
        habitat.desocupar();

        assertEquals(0, habitat.getOcupados());
        assertTrue(habitat.tieneEspaciosLibre());
    }

    @Test
    void higieneSeDegradaYLimpiaSinSalirDeLimites() {
        Habitat habitat = new Habitat(TipoHabitat.PECERA, 1);

        habitat.degradarConElTiempo();
        assertEquals(96, habitat.getHigiene());

        for (int i = 0; i < 30; i++) {
            habitat.degradarConElTiempo();
        }
        assertEquals(0, habitat.getHigiene());

        habitat.limpiar();
        assertEquals(25, habitat.getHigiene());

        for (int i = 0; i < 5; i++) {
            habitat.limpiar();
        }
        assertEquals(100, habitat.getHigiene());
    }
}
