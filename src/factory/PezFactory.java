package factory;
import model.Mascota;
import model.Pez;

/**
 * se le indica que debe crear especificamente un pez y no otra mascota
 */

public class PezFactory extends MascotaFactory {
    @Override
    public Mascota crearMascota(String nombre){
        return new Pez(nombre);
    }
}
