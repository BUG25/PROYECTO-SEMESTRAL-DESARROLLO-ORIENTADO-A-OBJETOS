package servicio;
import  factory.FabricaMascotas;
import excepciones.DineroInsuficienteException;
import habitat.Habitat;
import habitat.TipoHabitat;

/**
 * representa la tienda, valida las reglas ( que haya suficiente dinero para comprar y habitat necesaria)
 * no guarda ninguna información del usuario
 */

public class Tienda {
    private final FabricaMascotas fabricaMascotas = new FabricaMascotas();

    /**
     * EL usuario adquiere un habitat, se valida que tenga dinero suficiente, si lo tiene
     * se le descuenta y se agrega el habitat a su lista
     * @param usuario, quien esta comprando
     * @param tipo, tipo de habitata que adquirio
     * @param costo, precio del habitat
     * @throws DineroInsuficienteException, si el usuario no tiene suficiente dinero
     */
    public void comprarHabitat(Usuario usuario, TipoHabitat tipo, int capacidad, double costo)
            throws DineroInsuficienteException{
        if(usuario.getDinero() < costo){
            throw new DineroInsuficienteException(
                    "No tiene dinero suficiente para comprar " + tipo);
        }
        usuario.descontarDinero(costo);
        usuario.getHabitats().add(new Habitat(tipo,capacidad));
    }
}
