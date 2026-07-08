import servicio.Tienda;
import servicio.Usuario;

/**
 * Se prueba en la consola para verificar que todo funcione correctamente juntos
 */
public class Main {
    public static void main(String[] args){
        Usuario usuario = new Usuario(100.0);
        Tienda tienda = new Tienda();
        System.out.println("Dinero inicial: " + usuario.getDinero());
    }
}
