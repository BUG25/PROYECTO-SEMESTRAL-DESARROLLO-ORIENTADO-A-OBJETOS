package model.medicinas;

import model.enfermedades.Infeccion;
import model.enfermedades.Parasitos;
import model.enfermedades.Pulgas;
import model.enfermedades.Resfriado;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicinaTest {
    @Test
    void antibioticosCuraInfeccionYResfriado() {
        Antibioticos antibioticos = new Antibioticos();

        assertEquals("Antibioticos", antibioticos.getNombre());
        assertEquals(12500, antibioticos.getCosto());
        assertTrue(antibioticos.cura(new Infeccion()));
        assertTrue(antibioticos.cura(new Resfriado()));
        assertFalse(antibioticos.cura(new Parasitos()));
        assertFalse(antibioticos.cura(new Pulgas()));
    }

    @Test
    void antiparasitarioSoloCuraParasitos() {
        Antiparasitario antiparasitario = new Antiparasitario();

        assertEquals("Antiparasitario", antiparasitario.getNombre());
        assertEquals(9500, antiparasitario.getCosto());
        assertTrue(antiparasitario.cura(new Parasitos()));
        assertFalse(antiparasitario.cura(new Infeccion()));
    }

    @Test
    void antipulgasSoloCuraPulgas() {
        Antipulgas antipulgas = new Antipulgas();

        assertEquals("Antipulgas", antipulgas.getNombre());
        assertEquals(2000, antipulgas.getCosto());
        assertTrue(antipulgas.cura(new Pulgas()));
        assertFalse(antipulgas.cura(new Resfriado()));
    }
}
