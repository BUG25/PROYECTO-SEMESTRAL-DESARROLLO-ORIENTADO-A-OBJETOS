package factory;
import model.mascotas.Mascota;
import model.mascotas.Gato;

import java.util.Random;

/**
 * se le indica que debe crear especificamente un gato y no otra mascota
 */


public class GatoFactory extends MascotaFactory{
    private final Random random = new Random();

    @Override
    public Mascota crearMascota(String nombre){
        Gato gato = new Gato(nombre);
        gato.inicializarStatsAleatorios(random);
        return gato;
    }
}
