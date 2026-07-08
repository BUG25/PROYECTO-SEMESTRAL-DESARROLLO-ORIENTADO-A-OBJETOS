package model.alimentos;

import model.mascotas.TipoMascota;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlimentoTest {
    @Test
    void piensoPerroDefineValoresYCompatibilidadPorCalidad() {
        assertValores(new PiensoPerro(CalidadAlimento.ESTANDAR), "Pienso para perros", 4500, 30, TipoMascota.PERRO);
        assertValores(new PiensoPerro(CalidadAlimento.BALANCEADO), "Pienso para perros", 9500, 40, TipoMascota.PERRO);
        assertValores(new PiensoPerro(CalidadAlimento.PREMIUM), "Pienso para perros", 15000, 50, TipoMascota.PERRO);
    }

    @Test
    void piensoGatoDefineValoresYCompatibilidadPorCalidad() {
        assertValores(new PiensoGato(CalidadAlimento.ESTANDAR), "Pienso para gatos", 4000, 30, TipoMascota.GATO);
        assertValores(new PiensoGato(CalidadAlimento.BALANCEADO), "Pienso para gatos", 6500, 40, TipoMascota.GATO);
        assertValores(new PiensoGato(CalidadAlimento.PREMIUM), "Pienso para gatos", 11000, 50, TipoMascota.GATO);
    }

    @Test
    void escamasDefineValoresYCompatibilidadPorCalidad() {
        assertValores(new Escamas(CalidadAlimento.ESTANDAR), "Escamas", 6000, 30, TipoMascota.PEZ);
        assertValores(new Escamas(CalidadAlimento.BALANCEADO), "Escamas", 9000, 40, TipoMascota.PEZ);
        assertValores(new Escamas(CalidadAlimento.PREMIUM), "Escamas", 12000, 50, TipoMascota.PEZ);
    }

    @Test
    void pelletDefineValoresYCompatibilidadPorCalidad() {
        assertValores(new Pellet(CalidadAlimento.ESTANDAR), "Pellet", 3500, 20, TipoMascota.HAMSTER);
        assertValores(new Pellet(CalidadAlimento.BALANCEADO), "Pellet", 7000, 25, TipoMascota.HAMSTER);
        assertValores(new Pellet(CalidadAlimento.PREMIUM), "Pellet", 14000, 33, TipoMascota.HAMSTER);
    }

    @Test
    void semillasDefineValoresYCompatibilidadPorCalidad() {
        assertValores(new Semillas(CalidadAlimento.ESTANDAR), "Semillas", 2500, 20, TipoMascota.PAJARO);
        assertValores(new Semillas(CalidadAlimento.BALANCEADO), "Semillas", 5000, 25, TipoMascota.PAJARO);
        assertValores(new Semillas(CalidadAlimento.PREMIUM), "Semillas", 10000, 33, TipoMascota.PAJARO);
    }

    private void assertValores(Alimento alimento, String nombre, int costo, int nutricion, TipoMascota compatible) {
        assertEquals(nombre, alimento.getNombre());
        assertEquals(costo, alimento.getCosto());
        assertEquals(nutricion, alimento.getValorNutricional());
        assertTrue(alimento.esCompatibleCon(compatible));

        for (TipoMascota tipo : TipoMascota.values()) {
            if (tipo != compatible) {
                assertFalse(alimento.esCompatibleCon(tipo));
            }
        }
    }
}
