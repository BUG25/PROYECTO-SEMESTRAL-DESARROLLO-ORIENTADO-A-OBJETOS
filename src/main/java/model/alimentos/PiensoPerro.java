package model.alimentos;

import model.mascotas.TipoMascota;
public class PiensoPerro extends Alimento {
    public PiensoPerro(CalidadAlimento calidad) {
        super("Pienso para perros",
                obtenerCosto(calidad),
                obtenerValNut(calidad),
                calidad);
    }
    private static int obtenerCosto(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 4500;
            case BALANCEADO -> 9500;
            case PREMIUM -> 15000;
            default -> throw new IllegalArgumentException("Calidad no válida");
        };
    }
    private static int obtenerValNut(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 30;
            case BALANCEADO -> 40;
            case PREMIUM -> 50;
            default -> throw new IllegalArgumentException("Calidad no válida");
        };
    }

    @Override
    public boolean esCompatibleCon(TipoMascota tipo) {
        return tipo == TipoMascota.PERRO;
    }
}
