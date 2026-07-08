package servicio;

import model.habitat.Habitat;
import model.enfermedades.Enfermedad;
import model.enfermedades.Infeccion;
import model.enfermedades.Parasitos;
import model.enfermedades.Pulgas;
import model.enfermedades.Resfriado;
import model.mascotas.Mascota;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimuladorDia {
    private final Random random = new Random();
    private final List<Enfermedad> enfermedades = List.of(
            new Infeccion(),
            new Parasitos(),
            new Pulgas(),
            new Resfriado()
    );
    private final TiendaMascotas tiendaMascotas;

    private PeriodoDia periodoActual = PeriodoDia.MANANA;
    private int diasTranscurridos = 1;

    public SimuladorDia(TiendaMascotas tiendaMascotas) {
        this.tiendaMascotas = tiendaMascotas;
    }

    public PeriodoDia getPeriodoActual() {
        return periodoActual;
    }

    public int getDiasTranscurridos() {
        return diasTranscurridos;
    }

    public List<Mascota> avanzarPeriodo(Usuario usuario, PeriodoDia destino) {
        // 1. Ejecutamos degradación del periodo actual
        tiendaMascotas.reputacionPasiva();
        for (Habitat habitat : usuario.getHabitats()) {
            habitat.degradarConElTiempo();
        }
        for (Mascota mascota : usuario.getMascotas()) {
            if (mascota.estaMuerta()) continue;

            mascota.degradarConElTiempo(periodoActual);
            evaluarContagio(usuario, mascota);
            if (mascota.estaEnferma()) {
                mascota.aplicarDañoDeEnfermedad();
            }
        }

        // Filtramos y guardamos las mascotas que murieron
        List<Mascota> mascotasMuertas = usuario.getMascotas().stream()
                .filter(Mascota::estaMuerta)
                .collect(Collectors.toList());

        // Las eliminamos de la tienda
        usuario.getMascotas().removeIf(Mascota::estaMuerta);

        // Registramos la penalización por cada una
        for (Mascota muerta : mascotasMuertas) {
            tiendaMascotas.registrarMuerte();
        }

        // 2. Transición de estado (Ciclo cerrado)
        if (periodoActual == PeriodoDia.MANANA) {
            periodoActual = PeriodoDia.TARDE;
        } else if (periodoActual == PeriodoDia.TARDE) {
            periodoActual = PeriodoDia.NOCHE;
        } else if (periodoActual == PeriodoDia.NOCHE) {
            iniciarNuevoDia(usuario);
            periodoActual = PeriodoDia.MANANA;
            diasTranscurridos++;
        }

        return mascotasMuertas;
    }

    public List<Mascota> avanzarDia(Usuario usuario) {
        List<Mascota> todasLasMuertas = new ArrayList<>();
        // Avanza periodos rápidos hasta que vuelva a ser MAÑANA
        do {
            todasLasMuertas.addAll(avanzarPeriodo(usuario, null));
        } while (periodoActual != PeriodoDia.MANANA);

        return todasLasMuertas;
    }

    private void iniciarNuevoDia(Usuario usuario) {
        // Resetea a las mascotas para que vuelvan a poder comer
        for(Mascota m : usuario.getMascotas()) {
            if (!m.estaMuerta()) {
                m.resetearAlimentacionDia();
            }
        }
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
        if (mascota.estaEnferma()) return;

        Habitat habitat = buscarHabitatCompatible(usuario, mascota);
        if (habitat == null) return;

        double riesgo = mascota.calcularRiesgoEnfermedad(habitat);
        if (random.nextDouble() >= riesgo) return;

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