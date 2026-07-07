package model.enfermedades;

import model.mascotas.TipoMascota;

public class Pulgas extends Enfermedad{
    public Pulgas(){
        super("Pulgas", 15);
    }
    @Override
    public boolean afecta(TipoMascota tipo) {
        return tipo == TipoMascota.PERRO || tipo == TipoMascota.GATO;
    }
}
