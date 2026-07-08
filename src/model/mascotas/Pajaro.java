package model.mascotas;
import model.habitat.TipoHabitat;

/**
 * Especie : Pajaro
 * Habitat: Jaula_pajaro
 */

public class Pajaro extends Mascota {
    public Pajaro(String nombre){
        super(nombre);
    }

    @Override
    public TipoMascota getTipoMascota() {
        return TipoMascota.PAJARO;
    }

    @Override
    public TipoHabitat getTipoHabitatRequerido() {
        return TipoHabitat.JAULA_PAJARO;
    }
}
