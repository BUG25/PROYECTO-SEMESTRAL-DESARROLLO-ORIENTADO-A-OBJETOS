package model.enfermedades;

import model.mascotas.TipoMascota;

public class Parasitos extends Enfermedad{
    public Parasitos(){
        super("Parasitos", 15);
    }

    @Override
    public boolean afecta(TipoMascota tipo) {
        return true;
    }
}
