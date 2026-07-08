package factory;
import model.mascotas.Mascota;
import model.mascotas.Perro;

import java.util.Random;

/**
 * se le indica que debe crear especificamente un perro y no otra mascota
 */

public class PerroFactory extends MascotaFactory {
    private final Random random = new Random();

    @Override
    public Mascota crearMascota(String nombre){
        Perro perro = new Perro(nombre);
        perro.inicializarStatsAleatorios(random);
        return perro;
    }
}
