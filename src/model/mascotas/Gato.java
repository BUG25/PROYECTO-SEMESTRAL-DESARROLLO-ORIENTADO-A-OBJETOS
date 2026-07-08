package model.mascotas;
import model.habitat.TipoHabitat;

/**
 * Especie : Gato
 * Habitat: Arenero
 */

public class Gato extends Mascota {
    public Gato(String nombre){
        super(nombre);
    }

    @Override
    public TipoMascota getTipoMascota() {
        return TipoMascota.GATO;
    }

    @Override
    public TipoHabitat getTipoHabitatRequerido(){
        return TipoHabitat.ARENERO;
    }
}
