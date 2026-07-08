package model.medicinas;

import model.enfermedades.Enfermedad;
import model.enfermedades.Parasitos;

public class Antiparasitario extends Medicina {
    public Antiparasitario() {
        super("Antiparasitario", 9500);
    }

    @Override
    public boolean cura(Enfermedad enfermedad) {
        return enfermedad instanceof Parasitos;
    }
}
