package model.mascotas;
import habitat.TipoHabitat;

/**
 * Especie : Pez
 * Habitat: Pecera
 */

public class Pez extends Mascota {
    public Pez(String nombre){
        super(nombre);
    }
    @Override
    public TipoHabitat getTipoHabitatRequerido(){
        return TipoHabitat.PECERA;
    }
}
