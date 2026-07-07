package factory;
import model.mascotas.Mascota;
import model.mascotas.Gato;

/**
 * se le indica que debe crear especificamente un gato y no otra mascota
 */


public class GatoFactory extends MascotaFactory{
    @Override
    public Mascota crearMascota(String nombre){
        return new Gato(nombre);
    }
}
