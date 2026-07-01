package factory;
import model.Mascota;

/**
 * Clase creadora con patrines de uso, patron Factory Method
 * se declara el metodo crearMascota, y que cada especie concreta la implementa
 * para devolver una mascota de su especie
 */
public abstract class MascotaFactory {
    public abstract Mascota crearMoscota(String nombre);
}
