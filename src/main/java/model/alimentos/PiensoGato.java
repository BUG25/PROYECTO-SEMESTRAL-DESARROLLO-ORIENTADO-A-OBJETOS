package model.alimentos;

import model.mascotas.TipoMascota;
public class PiensoGato extends Alimento {
    public PiensoGato(CalidadAlimento calidad) {
        super("Pienso para gatos",
                obtenerCosto(calidad),
                obtenerValNut(calidad),
                calidad);
    }
    private static int obtenerCosto(CalidadAlimento calidad) {
        return switch (calidad) {
            case ESTANDAR -> 4000;
            case BALANCEADO -> 6500;
            case PREMIUM -> 11000;
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
        return tipo == TipoMascota.GATO;
    }
}
