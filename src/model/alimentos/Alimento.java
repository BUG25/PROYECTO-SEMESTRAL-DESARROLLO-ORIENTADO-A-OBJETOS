package model.alimentos;
import model.mascotas.TipoMascota;

public abstract class Alimento {
    private final String nombre;
    private final int costo;
    private final int valorNutricional;
    private final CalidadAlimento calidad;

    protected Alimento(String nombre, int costo, int valorNutricional, CalidadAlimento calidad) {
        this.nombre = nombre;
        this.costo = costo;
        this.valorNutricional = valorNutricional;
        this.calidad = calidad;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCosto() {
        return costo;
    }

    public int getValorNutricional() {
        return valorNutricional;
    }
    public CalidadAlimento getCalidad() {
        return calidad;
    }

    public abstract boolean esCompatibleCon(TipoMascota tipo);
}