package excepciones;

/**
 * se activa cuando el usuario intenta comprar algo y no tiene dinero suficiente
 */

public class DineroInsuficienteException extends Exception {
    public DineroInsuficienteException(String message) {
        super(message);
    }
}
