package model.enfermedades;

import model.mascotas.TipoMascota;

public abstract class Enfermedad {
    private final String nombre;
    private final int dañoSalud;

    protected Enfermedad(String nombre, int dañoSalud) {
        this.nombre = nombre;
        this.dañoSalud = dañoSalud;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDañoSalud() {
        return dañoSalud;
    }
    public abstract boolean afecta(TipoMascota tipo);
}
