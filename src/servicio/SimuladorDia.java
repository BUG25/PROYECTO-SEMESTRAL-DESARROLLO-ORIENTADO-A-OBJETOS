package servicio;

import habitat.Habitat;
import model.mascotas.Mascota;
import model.mascotas.PeriodoDia;

/**
 * Coordina el paso del tiempo dentro de un dia.
 * Cada dia se divide en mañana, tarde y noche.
 */
public class SimuladorDia {

    public void avanzarPeriodo(Usuario usuario, PeriodoDia periodo) {
        for (Habitat habitat : usuario.getHabitats()) {
            habitat.degradarConElTiempo();
        }

        for (Mascota mascota : usuario.getMascotas()) {
            mascota.degradarConElTiempo(periodo);
        }
    }

    public void avanzarDia(Usuario usuario) {
        avanzarPeriodo(usuario, PeriodoDia.MAÑANA);
        avanzarPeriodo(usuario, PeriodoDia.TARDE);
        avanzarPeriodo(usuario, PeriodoDia.NOCHE);
    }
}
