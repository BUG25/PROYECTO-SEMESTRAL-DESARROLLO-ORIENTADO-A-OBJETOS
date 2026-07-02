package servicio;
import excepciones.SinHabitatDisponibleException;
import  factory.FabricaMascotas;
import excepciones.DineroInsuficienteException;
import habitat.Habitat;
import habitat.TipoHabitat;
import model.Mascota;
import model.TipoMascota;

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
     * @param capacidad, cuantas mascotas puede tener
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
    /**
     * EL usuario adopta a una mascota nueva. Se crea la mascotacon el factory method, y luego se analiza entre
     * los habitats del usuario, se verifica que tenga el habitat correcto con capacidad suficiente
     * si no existe se lanza SinHabitatDisponibleException y no se puede adoptar a la mascota
     * @param usuario, quien adopta
     * @param tipo, especie que se quiere adoptar
     * @param nombre, nombre que se le da a la mascota
     * @return, la mascota recien adoptada
     * @throws excepciones.SinHabitatDisponibleException, si no hay habitat compatible con la especie
     */
    public Mascota adoptarMascota(Usuario usuario, TipoMascota tipo, String nombre)
        throws SinHabitatDisponibleException{
        Mascota nueva = fabricaMascotas.crear(tipo, nombre);
        TipoHabitat tipoRequerido = nueva.getTipoHabitatRequerido();
        Habitat habitatLibre = buscarHabitatConEspacio(usuario,tipoRequerido);
        if(habitatLibre == null){
            throw new SinHabitatDisponibleException(
                    "No tienes un" + tipoRequerido + "con espacio para adoptar a " + tipo);
        }
        habitatLibre.ocupar();
        usuario.getMascotas().add(nueva);
        return nueva;
    }
    /**
     * Bisca entre los habitats del usuario, que si tiene el habitat requerido y si tiene espacio libre
     * @return el habitat encontrado, sino debuelve null
     */
    private Habitat buscarHabitatConEspacio(Usuario usuario, TipoHabitat tipoRequerido){
        for (Habitat h : usuario.getHabitats()){
            if (h.getTipo() == tipoRequerido && h.tieneEspaciosLibre()){
                return h;
            }
        }
        return null;
    }
}
