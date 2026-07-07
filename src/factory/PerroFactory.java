package factory;
import model.mascotas.Mascota;
import model.mascotas.Perro;

/**
 * se le indica que debe crear especificamente un perro y no otra mascota
 */

public class PerroFactory extends MascotaFactory {
    @Override
    public Mascota crearMascota(String nombre){
        return new Perro(nombre);
    }
}
