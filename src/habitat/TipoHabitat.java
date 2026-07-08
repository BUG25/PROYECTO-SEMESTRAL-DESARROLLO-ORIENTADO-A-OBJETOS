package habitat;

public enum TipoHabitat {
    PERRERA(9500, 3),
    ARENERO(9500, 3),
    PECERA (15000, 5),
    JAULA_PAJARO(12000, 4),
    JAULA_HAMSTER(7500, 3);

    private final int valor;
    private final int capacidadMaxima;

    TipoHabitat(int valor, int capacidadMaxima) {
        this.valor = valor;
        this.capacidadMaxima = capacidadMaxima;
    }
    public int getValor() {
        return valor;
    }
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}
