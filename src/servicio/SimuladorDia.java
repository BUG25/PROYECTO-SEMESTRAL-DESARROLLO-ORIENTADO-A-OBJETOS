package servicio;

import habitat.Habitat;
import model.enfermedades.Enfermedad;
import model.enfermedades.Infeccion;
import model.enfermedades.Parasitos;
import model.enfermedades.Pulgas;
import model.enfermedades.Resfriado;
import model.mascotas.Mascota;
import model.mascotas.TipoMascota;

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

    public void avanzarPeriodo(Usuario usuario, PeriodoDia periodo) {
        for (Habitat habitat : usuario.getHabitats()) {
            habitat.degradarConElTiempo();
        }

        for (Mascota mascota : usuario.getMascotas()) {
            mascota.degradarConElTiempo(periodo);
            evaluarContagio(usuario, mascota);
            if (mascota.estaEnferma()) {
                mascota.aplicarDañoDeEnfermedad();
            }
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

        TipoMascota tipoMascota = obtenerTipoMascota(mascota);
        for (Enfermedad enfermedad : enfermedades) {
            if (enfermedad.afecta(tipoMascota)) {
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

    private TipoMascota obtenerTipoMascota(Mascota mascota) {
        String nombreClase = mascota.getClass().getSimpleName();
        return switch (nombreClase) {
            case "Perro" -> TipoMascota.PERRO;
            case "Gato" -> TipoMascota.GATO;
            case "Pez" -> TipoMascota.PEZ;
            case "Hamster" -> TipoMascota.HAMSTER;
            case "Pajaro" -> TipoMascota.PAJARO;
            default -> throw new IllegalStateException("Tipo de mascota no soportado");
        };
    }
}
