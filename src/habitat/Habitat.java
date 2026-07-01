package habitat;

/**
 * Representa un habiatt que el cliente adquirio en la tienda
 * cada habitat tiene un tipo o capacidad maxima de mascotas
 */

public class Habitat {
    private final TipoHabitat tipo;
    private final int capacidadMaxima;
    private int ocupados;
    /**
     * @param tipo, tipo de habitat (arenero, pecera, etc)
     * @param capacidadMaxima cuantas mascotas puede alojar como maximo
     */

    public Habitat(TipoHabitat tipo, int capacidadMaxima){
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.ocupados = 0;
    }
    /**
     * @return treu si entra otra mascota más
     */
    public boolean tieneEspaciosLibre(){
        return ocupados < capacidadMaxima;
    }

    public TipoHabitat getTipo() {
        return tipo;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getOcupados() {
        return ocupados;
    }
}
