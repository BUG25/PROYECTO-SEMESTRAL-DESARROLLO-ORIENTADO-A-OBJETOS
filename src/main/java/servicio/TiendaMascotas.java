package servicio;

import model.mascotas.Mascota;
import java.util.Random;

public class TiendaMascotas {
    private final Random random;
    private int reputacion = 100;

    public TiendaMascotas() {
        this(new Random());
    }

    public TiendaMascotas(Random random) {
        this.random = random;
    }

    public int getReputacion() {
        return reputacion;
    }

    public void mejorarReputacion(int puntos) {
        reputacion = Math.min(100, reputacion + puntos);
    }

    public void empeorarReputacion(int puntos) {
        reputacion = Math.max(0, reputacion - puntos);
    }

    public void reputacionPasiva() {
        mejorarReputacion(1);
    }

    public void registrarMuerte() {
        empeorarReputacion(15);
    }

    public double calcularProbabilidadInteres(Mascota mascota) {
        double base = 0.20;
        base += mascota.getSalud() / 400.0;
        base += mascota.getFelicidad() / 500.0;
        base += reputacion / 500.0;
        return Math.min(0.90, base);
    }

    public int calcularPago(Mascota mascota) {
        int pagoBase = 2000;
        int bonoSalud = mascota.getSalud() * 18;
        int bonoFelicidad = mascota.getFelicidad() * 12;
        return pagoBase + bonoSalud + bonoFelicidad;
    }

    public void procesarVenta(Usuario vendedor, Mascota mascota, int pago) {
        vendedor.getMascotas().remove(mascota);
        mascota.liberarHabitat();
        vendedor.agregarDinero(pago);
        mejorarReputacion(10);
    }
}