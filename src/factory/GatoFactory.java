package factory;
import model.Mascota;
import model.Gato;

/**
 * se le indica que debe crear especificamente un gato y no otra mascota
 */


public class GatoFactory extends MascotaFactory{
    @Override
    public Mascota crearMascota(String nombre){
        return new Gato(nombre);
    }
}
