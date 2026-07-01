package excepciones;

/**
 * Se activa cuando usuario intenta adoptar alguna mascota
 * sin tener el habitat compatible ya comprado
 */

public class SinHabitatDisponibleException extends Exception {
    public SinHabitatDisponibleException(String message) {
        super(message);
    }
}
