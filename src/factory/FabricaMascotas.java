package factory;
import model.mascotas.Mascota;
import model.mascotas.TipoMascota;
import  java.util.EnumMap;
import  java.util.Map;

/**
 * acceso unico a los 5 tipo de especie creadoras, en vez de que se escriba
 * new especiefactory(), se le pide la mascota a etsa clase y solo indicando el
 * tipoMascota(enum) que quiere
 */

public class FabricaMascotas {
    /**
     * hacemos uso de Map (diccionario) para asocial el valor (tipoMascota) con el valor de fabrica
     * que seria crear una especie, perrofactory, gatofactory,etc...
     */
    private final Map<TipoMascota,MascotaFactory> fabricas = new EnumMap<>(TipoMascota.class);
    /**
     * El cosntructor se ejecuta una sola vez, y al crear una masocta
     * se registra en el diccionario, que fabrica corresponde a cada tipo de mascota
     */
    public FabricaMascotas() {
        fabricas.put(TipoMascota.PERRO, new PerroFactory());
        fabricas.put(TipoMascota.GATO, new GatoFactory());
        fabricas.put(TipoMascota.PEZ, new PezFactory());
        fabricas.put(TipoMascota.PAJARO, new PajaroFactory());
        fabricas.put(TipoMascota.HAMSTER, new HamsterFactory());
    }

    /**
     * Método público que se usará en todo el  programa para crear una mascota
     * sin saber qué clase concreta de fábrica existe por dentro.
     * @param tipo   especie de mascota que se quiere crear
     * @param nombre nombre que tendrá la mascota
     * @return la mascota recién creada
     */
    public Mascota crear(TipoMascota tipo, String nombre){
        MascotaFactory fabrica = fabricas.get(tipo);
        if (fabrica == null){
            throw new IllegalArgumentException("Tipo de mascota no disponible ");
        }
        return fabrica.crearMascota(nombre);
    }
}
