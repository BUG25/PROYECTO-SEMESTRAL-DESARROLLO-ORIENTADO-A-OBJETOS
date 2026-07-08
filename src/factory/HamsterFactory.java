package factory;
import model.mascotas.Mascota;
import model.mascotas.Hamster;

import java.util.Random;

/**
 * se le indica que debe crear especificamente un hamster y no otra mascota
 */

public class HamsterFactory extends MascotaFactory {
    private final Random random = new Random();

    @Override
    public Mascota crearMascota(String nombre){
        Hamster hamster = new Hamster(nombre);
        hamster.inicializarStatsAleatorios(random);
        return hamster;
    }
}
