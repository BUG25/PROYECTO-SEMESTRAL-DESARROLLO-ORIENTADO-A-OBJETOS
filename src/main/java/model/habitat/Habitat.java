package model.habitat;

/**
 * Representa un habitat que el cliente adquirio en la tienda
 * cada habitat tiene un tipo o capacidad maxima de mascotas
 */

public class Habitat {
    private final TipoHabitat tipo;
    private int ocupados;
    private final int capacidadMaxima;
    private int higiene = 100;
    /**
     * @param tipo, tipo de habitat (arenero, pecera, etc)
     * @param capacidadMaxima cuantas mascotas puede alojar como maximo
     */

    public Habitat(TipoHabitat tipo, int capacidadMaxima) {
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.ocupados = 0;
    }
    /**
     * @return true si entra otra mascota más
     */
    public boolean tieneEspaciosLibre(){
        return ocupados < capacidadMaxima;
    }
    /**
     * se ocupa un espacio del habitat
     */
    public void ocupar(){
        if(!tieneEspaciosLibre()){
            throw new IllegalArgumentException("Habitat sin espacio disponible: "+ tipo);
        }
        ocupados++;
    }

    public void desocupar() {
        if (ocupados > 0) {
            ocupados--;
        }
    }

    public TipoHabitat getTipo() {
        return tipo;
    }

    public int getOcupados() {
        return ocupados;
    }

    public int getHigiene() {
        return higiene;
    }

    public void limpiar() {
        higiene = Math.min(100, higiene + 25);
    }

    public void degradarConElTiempo() {
        higiene = Math.max(0, higiene - 4);
    }

    @Override
    public String toString() {
        return tipo.name() + " (" + ocupados + "/" + capacidadMaxima + " ocupados) - Higiene: " + higiene + "%";
    }
}