package model.enfermedades;

import model.mascotas.TipoMascota;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnfermedadTest {
    @Test
    void infeccionAfectaATodasLasMascotas() {
        Infeccion infeccion = new Infeccion();

        assertEquals("Infeccion", infeccion.getNombre());
        assertEquals(30, infeccion.getDañoSalud());
        for (TipoMascota tipo : TipoMascota.values()) {
            assertTrue(infeccion.afecta(tipo));
        }
    }

    @Test
    void parasitosAfectaATodasLasMascotas() {
        Parasitos parasitos = new Parasitos();

        assertEquals("Parasitos", parasitos.getNombre());
        assertEquals(15, parasitos.getDañoSalud());
        for (TipoMascota tipo : TipoMascota.values()) {
            assertTrue(parasitos.afecta(tipo));
        }
    }

    @Test
    void pulgasSoloAfectaPerrosYGatos() {
        Pulgas pulgas = new Pulgas();

        assertEquals("Pulgas", pulgas.getNombre());
        assertEquals(15, pulgas.getDañoSalud());
        assertTrue(pulgas.afecta(TipoMascota.PERRO));
        assertTrue(pulgas.afecta(TipoMascota.GATO));
        assertFalse(pulgas.afecta(TipoMascota.PEZ));
        assertFalse(pulgas.afecta(TipoMascota.HAMSTER));
        assertFalse(pulgas.afecta(TipoMascota.PAJARO));
    }

    @Test
    void resfriadoNoAfectaPeces() {
        Resfriado resfriado = new Resfriado();

        assertEquals("Resfriado", resfriado.getNombre());
        assertEquals(10, resfriado.getDañoSalud());
        assertTrue(resfriado.afecta(TipoMascota.PERRO));
        assertTrue(resfriado.afecta(TipoMascota.GATO));
        assertTrue(resfriado.afecta(TipoMascota.HAMSTER));
        assertTrue(resfriado.afecta(TipoMascota.PAJARO));
        assertFalse(resfriado.afecta(TipoMascota.PEZ));
    }
}
