package model.medicinas;

import model.enfermedades.Enfermedad;
import model.enfermedades.Pulgas;

public class Antipulgas extends Medicina {
    public Antipulgas() {
        super("Antipulgas", 2000);
    }
    @Override
    public boolean cura(Enfermedad enfermedad){
        return enfermedad instanceof Pulgas;
    }
}
