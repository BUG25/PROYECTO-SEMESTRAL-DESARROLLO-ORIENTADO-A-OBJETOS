package servicio;

import excepciones.DineroInsuficienteException;
import excepciones.SinHabitatDisponibleException;
import model.alimentos.CalidadAlimento;
import model.alimentos.PiensoPerro;
import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.mascotas.Mascota;
import model.mascotas.TipoMascota;
import model.medicinas.Antipulgas;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TiendaTest {
    @Test
    void crearYAgregarItemsAlCarritoDeleganCorrectamente() {
        Tienda tienda = new Tienda();
        CarritoCompra carrito = tienda.crearCarrito();

        tienda.agregarHabitatAlCarrito(carrito, TipoHabitat.PERRERA);
        tienda.agregarAlimentoAlCarrito(carrito, new PiensoPerro(CalidadAlimento.ESTANDAR));
        tienda.agregarMedicinaAlCarrito(carrito, new Antipulgas());

        assertEquals(1, carrito.getHabitats().size());
        assertEquals(1, carrito.getAlimentos().size());
        assertEquals(1, carrito.getMedicinas().size());
    }

    @Test
    void pagarCarritoDescuentaDineroTrasladaItemsYVaciaCarrito() throws Exception {
        Tienda tienda = new Tienda();
        Usuario usuario = new Usuario(20000);
        CarritoCompra carrito = tienda.crearCarrito();
        tienda.agregarHabitatAlCarrito(carrito, TipoHabitat.PERRERA);
        tienda.agregarAlimentoAlCarrito(carrito, new PiensoPerro(CalidadAlimento.ESTANDAR));

        tienda.pagarCarrito(usuario, carrito);

        assertEquals(6000, usuario.getDinero());
        assertEquals(1, usuario.getHabitats().size());
        assertEquals(1, usuario.getAlimentos().size());
        assertTrue(carrito.estaVacio());
    }

    @Test
    void pagarCarritoSinDineroLanzaExcepcionYNoModificaUsuarioNiCarrito() {
        Tienda tienda = new Tienda();
        Usuario usuario = new Usuario(1000);
        CarritoCompra carrito = tienda.crearCarrito();
        tienda.agregarHabitatAlCarrito(carrito, TipoHabitat.PERRERA);

        assertThrows(DineroInsuficienteException.class, () -> tienda.pagarCarrito(usuario, carrito));

        assertEquals(1000, usuario.getDinero());
        assertTrue(usuario.getHabitats().isEmpty());
        assertFalse(carrito.estaVacio());
    }

    @Test
    void adoptarMascotaAsignaHabitatDisponibleYOcupaEspacio() throws Exception {
        Tienda tienda = new Tienda();
        Usuario usuario = new Usuario(0);
        Habitat habitat = new Habitat(TipoHabitat.PERRERA, 1);
        usuario.getHabitats().add(habitat);

        Mascota mascota = tienda.adoptarMascota(usuario, TipoMascota.PERRO, "Firulais");

        assertEquals("Firulais", mascota.getNombre());
        assertSame(habitat, mascota.getHabitatAsignado());
        assertEquals(1, habitat.getOcupados());
        assertEquals(1, usuario.getMascotas().size());
    }

    @Test
    void adoptarMascotaSinHabitatCompatibleOEspacioLanzaExcepcion() throws Exception {
        Tienda tienda = new Tienda();
        Usuario usuario = new Usuario(0);
        usuario.getHabitats().add(new Habitat(TipoHabitat.PECERA, 1));

        assertThrows(SinHabitatDisponibleException.class,
                () -> tienda.adoptarMascota(usuario, TipoMascota.PERRO, "Firulais"));

        usuario.getHabitats().add(new Habitat(TipoHabitat.PERRERA, 1));
        tienda.adoptarMascota(usuario, TipoMascota.PERRO, "Uno");

        assertThrows(SinHabitatDisponibleException.class,
                () -> tienda.adoptarMascota(usuario, TipoMascota.PERRO, "Dos"));
    }
}
