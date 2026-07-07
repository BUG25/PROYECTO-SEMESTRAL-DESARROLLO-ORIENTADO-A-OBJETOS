package model.medicinas;

import model.enfermedades.Enfermedad;
import model.enfermedades.Infeccion;
import model.enfermedades.Resfriado;

public class Antibioticos extends Medicina {
    public Antibioticos() {
        super("Antibioticos", 12500);
    }

    @Override
    public boolean cura(Enfermedad enfermedad) {
        return enfermedad instanceof Infeccion || enfermedad instanceof Resfriado;
    }
}
