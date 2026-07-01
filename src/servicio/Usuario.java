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
