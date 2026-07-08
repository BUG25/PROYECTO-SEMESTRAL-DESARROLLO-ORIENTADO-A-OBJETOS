package model.medicinas;
import model.enfermedades.Enfermedad;

public abstract class Medicina {
    private final String nombre;
    private final int costo;

    protected Medicina(String nombre, int costo) {
        this.nombre = nombre;
        this.costo = costo;
    }
    public String getNombre() {
        return nombre;
    }
    public int getCosto() {
        return costo;
    }
    public abstract boolean cura(Enfermedad enfermedad);
}
