package model.mascotas;
import habitat.TipoHabitat;

/**
 * Representa una mascota generica adoptada desde la tienda
 * es abstracta ya que nunca se crea una "mascota" especial si no
 * que se sea cea de una especie concreta y se hereda de esta clase
 */

public abstract class Mascota {
    /** Nombre de la mascota, se le asignan al ser adoptada */
    private final String nombre;

    /** Nivel de comida, baja con el tiempo, sube al alimentarla */
    private int comida = 100;

    /** Nivel de salud, baja con el tiempo, depende de la comida y limpieza */
    private int salud = 100;

    /** Nivel de felicidad, sube al jugar con la mascota */
    private int felicidad = 100;

    /**
     * Constructor protegido, solo las subclases (perro, gato, ...) pueden llamarlo
     * mediante super(nombre)
     * @param nombre  nombre de la mascota
     */

    protected Mascota(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Indica que tipo de habitat necesita cada especia para
     * poder ser adoptada, cada subclase concreta debe definir su propio requisito
     */
    public abstract TipoHabitat getTipoHabitatRequerido ();

    public String getNombre() {
        return nombre;
    }
    public int getComida(){
        return comida;
    }
    public int getSalud(){
        return salud;
    }
    public int getFelicidad() {
        return felicidad;
    }
    /**
     * Alimentar a la mascota: sube el nivel de comida
     * tambien sube un poco en salud
     * ningun valor puede superar 100
     */
    public void alimentar () {
        comida = Math.min(100, comida+20);
        salud = Math.min(100, salud+5);
    }

    /**
     * Limpiar el habitat: sube la salud y felciidad
     */
    public void limpiarHabitat() {
        salud = Math.min(100, salud+10);
        felicidad = Math.min(100, felicidad+5);
    }

    /**
     * Jugar con la mascota: sube la felicidad pero baja la comida
     * ningun valor puede ser menor a 0
     */
    public void jugar() {
        felicidad= Math.min(100, felicidad+15);
        comida = Math.max(0, comida-5);
    }

    /**
     * Hacemos la simulación del desgaste natural de la mascota (salud, felicidad y hambre)
     * que pasa con el tiempo, mientras el juego este ejecución
     */
    public void degradarConElTiempo() {
        comida = Math.max(0,comida-2);
        felicidad = Math.max(0,felicidad-1);
        if (comida == 0){
            salud = Math.max(0,salud-5);
        }
    }
}
