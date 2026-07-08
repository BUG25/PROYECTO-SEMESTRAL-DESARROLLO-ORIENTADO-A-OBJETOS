package servicio;

import model.mascotas.Mascota;

import java.util.Random;

public class TiendaMascotas {
    private final Random random = new Random();
    private int reputacion = 100;

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

    public boolean hayCompradorInteresado(Mascota mascota) {
        double probabilidad = calcularProbabilidadInteres(mascota);
        return random.nextDouble() < probabilidad;
    }

    public boolean venderMascota(Usuario vendedor, Mascota mascota) {
        if (!vendedor.getMascotas().contains(mascota)) {
            return false;
        }

        if (mascota.estaMuerta()) {
            liberarMascota(vendedor, mascota);
            empeorarReputacion(20);
            return false;
        }

        if (!hayCompradorInteresado(mascota)) {
            return false;
        }

        int pago = calcularPago(mascota);
        liberarMascota(vendedor, mascota);
        vendedor.agregarDinero(pago);
        mejorarReputacion(10);
        return true;
    }

    private double calcularProbabilidadInteres(Mascota mascota) {
        double base = 0.20;
        base += mascota.getSalud() / 400.0;
        base += mascota.getFelicidad() / 500.0;
        base += reputacion / 500.0;
        return Math.min(0.90, base);
    }

    private int calcularPago(Mascota mascota) {
        int pagoBase = 2000;
        int bonoSalud = mascota.getSalud() * 18;
        int bonoFelicidad = mascota.getFelicidad() * 12;
        return pagoBase + bonoSalud + bonoFelicidad;
    }

    private void liberarMascota(Usuario vendedor, Mascota mascota) {
        vendedor.getMascotas().remove(mascota);
        mascota.liberarHabitat();
    }
}
