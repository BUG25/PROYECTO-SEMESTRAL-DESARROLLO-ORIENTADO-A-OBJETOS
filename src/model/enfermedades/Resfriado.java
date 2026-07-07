package model.enfermedades;

import model.mascotas.TipoMascota;

public class Resfriado extends Enfermedad {
    public Resfriado() {
        super("Resfriado", 10);
    }
    @Override
    public boolean afecta(TipoMascota tipo) {
        return tipo == TipoMascota.PERRO
            || tipo == TipoMascota.PAJARO
            || tipo == TipoMascota.GATO
            || tipo == TipoMascota.HAMSTER;
    }
}
