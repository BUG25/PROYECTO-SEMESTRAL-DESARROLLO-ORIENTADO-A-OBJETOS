package model.alimentos;
import model.mascotas.TipoMascota;

public class Semillas extends Alimento {
    public Semillas(CalidadAlimento calidad) {
        super("Semillas",
                obtenerCosto(calidad),
                obtenerValNut(calidad),
                calidad);
    }
    private static int obtenerCosto(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 2500;
            case BALANCEADO -> 5000;
            case PREMIUM -> 10000;
            default -> throw new IllegalArgumentException("Calidad no válida");
        };
    }
    private static int obtenerValNut(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 20;
            case BALANCEADO -> 25;
            case PREMIUM -> 33;
            default -> throw new IllegalArgumentException("Calidad no válida");
        };
    }

    @Override
    public boolean esCompatibleCon(TipoMascota tipo) {
        return tipo == TipoMascota.PAJARO;
    }
}
