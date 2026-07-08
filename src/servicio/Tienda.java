package servicio;
import excepciones.SinHabitatDisponibleException;
import  factory.FabricaMascotas;
import excepciones.DineroInsuficienteException;
import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.alimentos.Alimento;
import model.mascotas.Mascota;
import model.mascotas.TipoMascota;
import model.medicinas.Medicina;

/**
 * representa la tienda, valida las reglas ( que haya suficiente dinero para comprar y model.habitat necesaria)
 * no guarda ninguna información del usuario
 */

public class Tienda {
    private final FabricaMascotas fabricaMascotas = new FabricaMascotas();

    public CarritoCompra crearCarrito() {
        return new CarritoCompra();
    }

    /**
     * EL usuario adquiere un model.habitat, se valida que tenga dinero suficiente, si lo tiene
     * se le descuenta y se agrega el model.habitat a su lista
     * @param tipo, tipo de habitata que adquirio
     * @throws DineroInsuficienteException, si el usuario no tiene suficiente dinero
     */
    public void agregarHabitatAlCarrito(CarritoCompra carrito, TipoHabitat tipo) {
        carrito.agregarHabitat(new Habitat(tipo, tipo.getCapacidadMaxima()));
    }

    public void agregarAlimentoAlCarrito(CarritoCompra carrito, Alimento alimento) {
        carrito.agregarAlimento(alimento);
    }

    public void agregarMedicinaAlCarrito(CarritoCompra carrito, Medicina medicina) {
        carrito.agregarMedicina(medicina);
    }

    public void pagarCarrito(Usuario usuario, CarritoCompra carrito) throws DineroInsuficienteException {
        int total = carrito.getTotal();
        if (usuario.getDinero() < total) {
            throw new DineroInsuficienteException("No tiene dinero suficiente para pagar el carrito");
        }

        usuario.descontarDinero(total);

        usuario.getAlimentos().addAll(carrito.getAlimentos());
        usuario.getMedicinas().addAll(carrito.getMedicinas());
        for (CarritoCompra.HabitatItem item : carrito.getHabitats()) {
            usuario.getHabitats().add(item.getHabitat());
        }

        carrito.vaciar();
    }

    /**
     * EL usuario adopta a una mascota nueva. Se crea la mascotacon el factory method, y luego se analiza entre
     * los habitats del usuario, se verifica que tenga el model.habitat correcto con capacidad suficiente
     * si no existe se lanza SinHabitatDisponibleException y no se puede adoptar a la mascota
     * @param usuario, quien adopta
     * @param tipo, especie que se quiere adoptar
     * @param nombre, nombre que se le da a la mascota
     * @return, la mascota recien adoptada
     * @throws excepciones.SinHabitatDisponibleException, si no hay model.habitat compatible con la especie
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
     * Bisca entre los habitats del usuario, que si tiene el model.habitat requerido y si tiene espacio libre
     * @return el model.habitat encontrado, sino debuelve null
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
