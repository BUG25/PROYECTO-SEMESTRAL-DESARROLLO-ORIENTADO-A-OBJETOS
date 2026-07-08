package model.mascotas;
import model.habitat.TipoHabitat;

/**
 * Especie : Hamster
 * Habitat: Jaula_hamster
 */

public class Hamster extends Mascota {
    public Hamster(String nombre){
        super(nombre);
    }
    @Override
    public TipoHabitat getTipoHabitatRequerido(){
        return TipoHabitat.JAULA_HAMSTER;
    }
}
