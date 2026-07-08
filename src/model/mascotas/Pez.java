package model.mascotas;
import model.habitat.TipoHabitat;

/**
 * Especie : Pez
 * Habitat: Pecera
 */

public class Pez extends Mascota {
    public Pez(String nombre){
        super(nombre);
    }

    @Override
    public TipoMascota getTipoMascota() {
        return TipoMascota.PEZ;
    }

    @Override
    public TipoHabitat getTipoHabitatRequerido(){
        return TipoHabitat.PECERA;
    }
}
