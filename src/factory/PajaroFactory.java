package factory;
import model.mascotas.Mascota;
import model.mascotas.Pajaro;

import java.util.Random;

/**
 * se le indica que debe crear especificamente un pajaro y no otra mascota
 */

public class PajaroFactory extends MascotaFactory{
    private final Random random = new Random();

    @Override
    public Mascota crearMascota(String nombre){
        Pajaro pajaro = new Pajaro(nombre);
        pajaro.inicializarStatsAleatorios(random);
        return pajaro;
    }
}
