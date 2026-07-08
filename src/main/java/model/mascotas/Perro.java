package model.mascotas;
import model.habitat.TipoHabitat;

/**
 * Especie : Perro
 * Habitat: Perrera
 */

public class Perro extends Mascota {
    public Perro(String nombre){
        super(nombre);
    }

    @Override
    public TipoMascota getTipoMascota() {
        return TipoMascota.PERRO;
    }

    @Override
    public TipoHabitat getTipoHabitatRequerido() {
        return TipoHabitat.PERRERA;
    }

}
