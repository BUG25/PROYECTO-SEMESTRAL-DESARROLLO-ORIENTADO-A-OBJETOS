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
    private boolean muerta = false;

    // --- CORRECCIÓN: Control de alimentación diaria ---
    private boolean alimentadoManana = false;
    private boolean alimentadoTarde = false;
    private boolean alimentadoNoche = false;

    protected Mascota(String nombre) {
        this.nombre = nombre;
    }

    public abstract TipoMascota getTipoMascota();

    public abstract TipoHabitat getTipoHabitatRequerido();

    public String getNombre() {
        return nombre;
    }

    public void inicializarStatsAleatorios(Random random) {
        salud = 30 + random.nextInt(31);
        felicidad = 30 + random.nextInt(31);
        comida = 30 + random.nextInt(31);
    }

    public int getComida() { return comida; }
    public int getSalud() { return salud; }
    public boolean estaMuerta() { return muerta; }
    public int getFelicidad() { return felicidad; }
    public int getDiasContactoSinTratar() { return diasContactoSinTratar; }
    public CalidadAlimento getUltimaCalidadAlimento() { return ultimaCalidadAlimento; }
    public boolean estaEnferma() { return enfermedadActual != null; }
    public Habitat getHabitatAsignado() { return habitatAsignado; }
    public Enfermedad getEnfermedadActual() { return enfermedadActual; }

    public void asignarHabitat(Habitat habitat) {
        this.habitatAsignado = habitat;
    }

    public void liberarHabitat() {
        if (habitatAsignado != null) {
            habitatAsignado.desocupar();
            habitatAsignado = null;
        }
    }

    public void morir() {
        if (muerta) return;
        muerta = true;
        liberarHabitat();
        enfermedadActual = null;
        diasContactoSinTratar = 0;
    }

    public void resetearAlimentacionDia() {
        alimentadoManana = false;
        alimentadoTarde = false;
        alimentadoNoche = false;
    }

    // Método original mantenido por si es usado en tests antiguos
    public void alimentar(Alimento alimento) {
        alimentar(alimento, PeriodoDia.MANANA);
    }

    public void alimentar(Alimento alimento, PeriodoDia periodoActual) {
        if (!alimento.esCompatibleCon(getTipoMascota())) {
            throw new IllegalArgumentException("Alimento incompatible con " + getTipoMascota());
        }

        TipoMascota tipo = getTipoMascota();

        // 1. Validar que no haya comido ya en este mismo periodo
        if (periodoActual == PeriodoDia.MANANA && alimentadoManana) {
            throw new IllegalArgumentException("Ya comió en la mañana. Espera a la tarde o noche.");
        }
        if (periodoActual == PeriodoDia.TARDE && alimentadoTarde) {
            throw new IllegalArgumentException("Ya comió en la tarde. Espera a la noche.");
        }
        if (periodoActual == PeriodoDia.NOCHE && alimentadoNoche) {
            throw new IllegalArgumentException("Ya comió en la noche. Espera a mañana.");
        }

        // 2. Validar restricciones de especie (Perro, Gato y Pez no comen en la tarde)
        if ((tipo == TipoMascota.PERRO || tipo == TipoMascota.GATO || tipo == TipoMascota.PEZ)
                && periodoActual == PeriodoDia.TARDE) {
            throw new IllegalArgumentException(tipo + " solo puede comer en la mañana y en la noche (máximo 2 veces al día).");
        }

        // 3. Registrar comida según turno
        if (periodoActual == PeriodoDia.MANANA) alimentadoManana = true;
        else if (periodoActual == PeriodoDia.TARDE) alimentadoTarde = true;
        else if (periodoActual == PeriodoDia.NOCHE) alimentadoNoche = true;

        // 4. Aplicar beneficios nutricionales
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
            if (salud == 0) {
                morir();
            }
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
        if (enfermedadActual == null) return;

        salud = Math.max(0, salud - enfermedadActual.getDañoSalud());
        if (salud == 0) {
            morir();
        }
    }

    public double calcularRiesgoEnfermedad(Habitat habitat) {
        double riesgo = 0.2;
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
        TipoMascota tipo = getTipoMascota();
        if (tipo == TipoMascota.PERRO || tipo == TipoMascota.GATO) return 10;
        if (tipo == TipoMascota.PEZ) return 9;
        if (tipo == TipoMascota.HAMSTER || tipo == TipoMascota.PAJARO) return 10;

        return switch (periodo) {
            case MANANA, TARDE, NOCHE -> 8;
        };
    }
}