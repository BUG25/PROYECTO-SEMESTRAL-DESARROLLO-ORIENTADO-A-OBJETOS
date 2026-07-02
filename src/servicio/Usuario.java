package servicio;
import habitat.Habitat;
import model.Mascota;
import java.util.ArrayList;
import java.util.List;

/**
 * representa al jugador(cliente), su dinero, mascotas adoptadas
 * habitats adquirido, accesorios o comida
 */

public class Usuario {
    private double dinero;
    private final List<Habitat> habitats = new ArrayList<>();
    private final List<Mascota> mascotas = new ArrayList<>();

    public Usuario(double dineroInicial){
        this.dinero = dineroInicial;
    }

    /**
     * descuenta dinero al usuario al comprar algo
     * la validación de si el usuario tiene suficiente dinero para comprar algo
     * llama a este metodo
     * @param monto cantidad que se descontara
     */
    public void descontarDinero(double monto){
        this.dinero -= monto;
    }

    /**
     * Agrega/da dinero como recompensa al usuario al alimentar,
     * limpiar o jugar con la mascota
     * @param monto cantidad que se da como recompensa
     */
    public void agregarDinero(double monto){
        this.dinero += monto;
    }

    public double getDinero() {
        return dinero;
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }
}
