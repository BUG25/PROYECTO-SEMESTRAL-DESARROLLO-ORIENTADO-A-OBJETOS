package model.alimentos;

import model.mascotas.TipoMascota;

public class Pellet extends Alimento {
    public Pellet(CalidadAlimento calidad) {
        super("Pellet",
                obtenerCosto(calidad),
                obtenerValNut(calidad),
                calidad);
    }
    private static int obtenerCosto(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 3500;
            case BALANCEADO -> 7000;
            case PREMIUM -> 14000;
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
        return tipo == TipoMascota.HAMSTER;
    }
}
