package factory;
import model.mascotas.Mascota;
import model.mascotas.Hamster;

/**
 * se le indica que debe crear especificamente un hamster y no otra mascota
 */

public class HamsterFactory extends MascotaFactory {
    @Override
    public Mascota crearMascota(String nombre){
        return new Hamster(nombre);
    }
}
