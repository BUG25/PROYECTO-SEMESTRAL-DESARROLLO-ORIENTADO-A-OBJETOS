package servicio;

import model.habitat.Habitat;
import model.enfermedades.Enfermedad;
import model.enfermedades.Infeccion;
import model.enfermedades.Parasitos;
import model.enfermedades.Pulgas;
import model.enfermedades.Resfriado;
import model.mascotas.Mascota;

import java.util.List;
import java.util.Random;

/**
 * Coordina el paso del tiempo dentro de un dia.
 * Cada dia se divide en manana, tarde y noche.
 */
public class SimuladorDia {
    private final Random random = new Random();
    private final List<Enfermedad> enfermedades = List.of(
            new Infeccion(),
            new Parasitos(),
            new Pulgas(),
            new Resfriado()
    );
    private final TiendaMascotas tiendaMascotas;

    public SimuladorDia(TiendaMascotas tiendaMascotas) {
        this.tiendaMascotas = tiendaMascotas;
    }

    public void avanzarPeriodo(Usuario usuario, PeriodoDia periodo) {
        tiendaMascotas.reputacionPasiva();

        for (Habitat habitat : usuario.getHabitats()) {
            habitat.degradarConElTiempo();
        }

        for (Mascota mascota : usuario.getMascotas()) {
            if (mascota.estaMuerta()) {
                continue;
            }

            mascota.degradarConElTiempo(periodo);
            evaluarContagio(usuario, mascota);
            if (mascota.estaEnferma()) {
                mascota.aplicarDañoDeEnfermedad();
            }
        }

        long muertas = usuario.getMascotas().stream().filter(Mascota::estaMuerta).count();
        usuario.getMascotas().removeIf(Mascota::estaMuerta);
        for (long i = 0; i < muertas; i++) {
            tiendaMascotas.registrarMuerte();
        }
    }

    public void avanzarDia(Usuario usuario) {
        avanzarPeriodo(usuario, PeriodoDia.MANANA);
        avanzarPeriodo(usuario, PeriodoDia.TARDE);
        avanzarPeriodo(usuario, PeriodoDia.NOCHE);
    }

    public boolean tratarMascota(Mascota mascota, model.medicinas.Medicina medicina) {
        if (mascota.puedeSerCuradaCon(medicina)) {
            mascota.curar();
            mascota.tratarContacto();
            return true;
        }
        return false;
    }

    private void evaluarContagio(Usuario usuario, Mascota mascota) {
        if (mascota.estaEnferma()) {
            return;
        }

        Habitat habitat = buscarHabitatCompatible(usuario, mascota);
        if (habitat == null) {
            return;
        }

        double riesgo = mascota.calcularRiesgoEnfermedad(habitat);
        if (random.nextDouble() >= riesgo) {
            return;
        }

        for (Enfermedad enfermedad : enfermedades) {
            if (enfermedad.afecta(mascota.getTipoMascota())) {
                mascota.contagiar(enfermedad);
                break;
            }
        }
    }

    private Habitat buscarHabitatCompatible(Usuario usuario, Mascota mascota) {
        for (Habitat habitat : usuario.getHabitats()) {
            if (habitat.getTipo() == mascota.getTipoHabitatRequerido()) {
                return habitat;
            }
        }
        return null;
    }
}
