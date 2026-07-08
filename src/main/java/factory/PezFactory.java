package factory;
import model.mascotas.Mascota;
import model.mascotas.Pez;

import java.util.Random;

/**
 * se le indica que debe crear especificamente un pez y no otra mascota
 */

public class PezFactory extends MascotaFactory {
    private final Random random = new Random();

    @Override
    public Mascota crearMascota(String nombre){
        Pez pez = new Pez(nombre);
        pez.inicializarStatsAleatorios(random);
        return pez;
    }
}
