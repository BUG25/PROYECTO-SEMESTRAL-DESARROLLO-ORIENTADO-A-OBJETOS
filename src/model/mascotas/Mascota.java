package model.mascotas;

import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.alimentos.Alimento;
import model.alimentos.CalidadAlimento;
import model.enfermedades.Enfermedad;
import model.medicinas.Medicina;
import servicio.PeriodoDia;

import java.util.Random;

public abstract class Mascota {
    private final String nombre;
    private int comida = 100;
    private int salud = 100;
    private int felicidad = 100;
    private int diasContactoSinTratar = 0;
    private CalidadAlimento ultimaCalidadAlimento;
    private Enfermedad enfermedadActual;
    private Habitat habitatAsignado;

    protected Mascota(String nombre) {
        this.nombre = nombre;
    }

    public abstract TipoHabitat getTipoHabitatRequerido();

    public String getNombre() {
        return nombre;
    }

    public void inicializarStatsAleatorios(Random random) {
        salud = 30 + random.nextInt(31);
        felicidad = 30 + random.nextInt(31);
        comida = 30 + random.nextInt(31);
    }

    public int getComida() {
        return comida;
    }

    public int getSalud() {
        return salud;
    }

    public boolean estaMuerta() {
        return salud <= 0;
    }

    public int getFelicidad() {
        return felicidad;
    }

    public int getPrecioAdopcion() {
        int precioBase = 1500;
        int bonoSalud = salud * 20;
        int bonoFelicidad = felicidad * 10;
        return precioBase + bonoSalud + bonoFelicidad;
    }

    public int getDiasContactoSinTratar() {
        return diasContactoSinTratar;
    }

    public CalidadAlimento getUltimaCalidadAlimento() {
        return ultimaCalidadAlimento;
    }

    public boolean estaEnferma() {
        return enfermedadActual != null;
    }

    public Habitat getHabitatAsignado() {
        return habitatAsignado;
    }

    public void asignarHabitat(Habitat habitat) {
        this.habitatAsignado = habitat;
    }

    public void liberarHabitat() {
        if (habitatAsignado != null) {
            habitatAsignado.desocupar();
            habitatAsignado = null;
        }
    }

    public Enfermedad getEnfermedadActual() {
        return enfermedadActual;
    }

    public void alimentar(Alimento alimento) {
        comida = Math.min(100, comida + alimento.getValorNutricional());
        ultimaCalidadAlimento = alimento.getCalidad();

        if (alimento.getCalidad() == CalidadAlimento.PREMIUM) {
            salud = Math.min(100, salud + 8);
        } else if (alimento.getCalidad() == CalidadAlimento.BALANCEADO) {
            salud = Math.min(100, salud + 4);
        } else {
            salud = Math.min(100, salud + 1);
        }
    }

    public void jugar() {
        felicidad = Math.min(100, felicidad + 15);
        comida = Math.max(0, comida - 5);
    }

    public void degradarConElTiempo() {
        degradarConElTiempo(PeriodoDia.MANANA);
        degradarConElTiempo(PeriodoDia.TARDE);
        degradarConElTiempo(PeriodoDia.NOCHE);
    }

    public void degradarConElTiempo(PeriodoDia periodo) {
        comida = Math.max(0, comida - obtenerBajadaComida(periodo));
        felicidad = Math.max(0, felicidad - 5);
        if (comida == 0) {
            salud = Math.max(0, salud - 25);
        }
    }

    public void registrarContactoConEnfermo() {
        diasContactoSinTratar++;
    }

    public void tratarContacto() {
        diasContactoSinTratar = 0;
    }

    public void contagiar(Enfermedad enfermedad) {
        enfermedadActual = enfermedad;
    }

    public void curar() {
        enfermedadActual = null;
    }

    public boolean puedeSerCuradaCon(Medicina medicina) {
        return enfermedadActual != null && medicina.cura(enfermedadActual);
    }

    public void aplicarDañoDeEnfermedad() {
        if (enfermedadActual == null) {
            return;
        }
        salud = Math.max(0, salud - enfermedadActual.getDañoSalud());
    }

    public double calcularRiesgoEnfermedad(Habitat habitat) {
        double riesgo = 0.0;

        if (diasContactoSinTratar > 0) {
            riesgo += 0.12 * diasContactoSinTratar;
        }

        if (habitat.getHigiene() <= 40) {
            riesgo += 0.10;
        }

        if (habitat.getHigiene() <= 20) {
            riesgo += 0.20;
        }

        if (ultimaCalidadAlimento == CalidadAlimento.ESTANDAR) {
            riesgo += 0.08;
        } else if (ultimaCalidadAlimento == CalidadAlimento.BALANCEADO) {
            riesgo += 0.03;
        }

        return Math.min(0.85, riesgo);
    }

    private int obtenerBajadaComida(PeriodoDia periodo) {
        TipoMascota tipo = obtenerTipoMascota();

        if (tipo == TipoMascota.PERRO || tipo == TipoMascota.GATO) {
            return 10;
        }

        if (tipo == TipoMascota.PEZ) {
            return 9;
        }

        if (tipo == TipoMascota.HAMSTER || tipo == TipoMascota.PAJARO) {
            return 10;
        }

        return switch (periodo) {
            case MANANA, TARDE, NOCHE -> 8;
        };
    }

    private TipoMascota obtenerTipoMascota() {
        if (this instanceof Perro) {
            return TipoMascota.PERRO;
        }
        if (this instanceof Gato) {
            return TipoMascota.GATO;
        }
        if (this instanceof Pez) {
            return TipoMascota.PEZ;
        }
        if (this instanceof Hamster) {
            return TipoMascota.HAMSTER;
        }
        if (this instanceof Pajaro) {
            return TipoMascota.PAJARO;
        }
        throw new IllegalStateException("Tipo de mascota no soportado");
    }
}
