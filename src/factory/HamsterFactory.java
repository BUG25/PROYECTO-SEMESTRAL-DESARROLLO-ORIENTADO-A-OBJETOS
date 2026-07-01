package factory;
import model.Mascota;
import model.Hamster;

/**
 * se le indica que debe crear especificamente un hamster y no otra mascota
 */

public class HamsterFactory extends MascotaFactory {
    @Override
    public Mascota crearMascota(String nombre){
        return new Hamster(nombre);
    }
}
