package factory;
import model.mascotas.Mascota;
import model.mascotas.Pajaro;

/**
 * se le indica que debe crear especificamente un pajaro y no otra mascota
 */

public class PajaroFactory extends MascotaFactory{
    @Override
    public Mascota crearMascota(String nombre){
        return new Pajaro(nombre);
    }
}
