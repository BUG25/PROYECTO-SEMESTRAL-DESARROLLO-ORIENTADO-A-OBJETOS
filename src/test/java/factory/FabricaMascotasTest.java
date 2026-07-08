package factory;

import model.habitat.TipoHabitat;
import model.mascotas.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FabricaMascotasTest {
    @Test
    void fabricaCreaCadaTipoConNombreYStatsInicialesAleatoriosEnRango() {
        FabricaMascotas fabrica = new FabricaMascotas();

        assertMascota(fabrica.crear(TipoMascota.PERRO, "Firulais"), Perro.class, "Firulais", TipoHabitat.PERRERA);
        assertMascota(fabrica.crear(TipoMascota.GATO, "Michi"), Gato.class, "Michi", TipoHabitat.ARENERO);
        assertMascota(fabrica.crear(TipoMascota.PEZ, "Nemo"), Pez.class, "Nemo", TipoHabitat.PECERA);
        assertMascota(fabrica.crear(TipoMascota.HAMSTER, "Hamtaro"), Hamster.class, "Hamtaro", TipoHabitat.JAULA_HAMSTER);
        assertMascota(fabrica.crear(TipoMascota.PAJARO, "Piolin"), Pajaro.class, "Piolin", TipoHabitat.JAULA_PAJARO);
    }

    private void assertMascota(Mascota mascota, Class<?> clase, String nombre, TipoHabitat habitat) {
        assertInstanceOf(clase, mascota);
        assertEquals(nombre, mascota.getNombre());
        assertEquals(habitat, mascota.getTipoHabitatRequerido());
        assertTrue(mascota.getSalud() >= 30 && mascota.getSalud() <= 60);
        assertTrue(mascota.getFelicidad() >= 30 && mascota.getFelicidad() <= 60);
        assertTrue(mascota.getComida() >= 30 && mascota.getComida() <= 60);
    }
}
