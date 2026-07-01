package model;

/**
 * Representa una mascota generica adoptada desde la tienda
 * es abstracta ya que nunca se crea una "mascota" especial si no
 * que se sea cea de una especie concreta y se hereda de esta clase
 */

public abstract class Mascota {
    /** Nombre de la mascota, se le asigan al ser adoptada */
    private final String nombre;

    /** Nivel de comida, baja con el tiempo, sube al alimentarla */
    private int comida = 100;

    /** Nivel de salud, baja con el tiempo, depende de la comida y limpieza */
    private int salud = 100;

    /** Nivel de felicidad, sube al jugar con la mascota */
    private int felicidad = 100;

    /**
     * Constructor protegido, solo las subclases (perro, gato,...) pueden llamarlo
     * mediante super(nombre)
     * @param nombre  nombre de la mascota
     */

    protected Mascota(String nombre) {
        this.nombre = nombre;
    }
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
     * Alimentar a la mascota : sube el nivel de comida
     * tambien sube un poco en salud
     * ningun valor puede superar 100
     */
    public void alimentar () {
        comida = Math.min(100, comida+20);
        salud = Math.min(100, salud+5);
    }
}
