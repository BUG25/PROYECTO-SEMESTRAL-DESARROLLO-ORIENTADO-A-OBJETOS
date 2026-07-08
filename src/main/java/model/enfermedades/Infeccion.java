package model.enfermedades;

import model.mascotas.TipoMascota;

public class Infeccion extends Enfermedad {
    public Infeccion() {
        super("Infeccion", 30);
    }
    @Override
    public boolean afecta(TipoMascota tipo) {
        return true;
    }
}
