package model.alimentos;

import model.mascotas.TipoMascota;

public class Escamas extends Alimento {
    public Escamas(CalidadAlimento calidad) {
        super("Escamas",
                obtenerCosto(calidad),
                obtenerValNut(calidad),
                calidad);
    }
    private static int obtenerCosto(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 6000;
            case BALANCEADO -> 9000;
            case PREMIUM -> 12000;
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
        return tipo == TipoMascota.PEZ;
    }
}
