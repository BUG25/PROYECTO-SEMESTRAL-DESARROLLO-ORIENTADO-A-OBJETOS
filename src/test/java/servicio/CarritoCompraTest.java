package servicio;

import model.alimentos.CalidadAlimento;
import model.alimentos.PiensoPerro;
import model.habitat.TipoHabitat;
import model.medicinas.Antipulgas;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarritoCompraTest {
    @Test
    void carritoAgregaItemsCalculaTotalYSeVacia() {
        CarritoCompra carrito = new CarritoCompra();

        assertTrue(carrito.estaVacio());

        carrito.agregarAlimento(new PiensoPerro(CalidadAlimento.ESTANDAR));
        carrito.agregarMedicina(new Antipulgas());
        carrito.agregarHabitat(TipoHabitat.PERRERA);

        assertFalse(carrito.estaVacio());
        assertEquals(1, carrito.getAlimentos().size());
        assertEquals(1, carrito.getMedicinas().size());
        assertEquals(1, carrito.getHabitats().size());
        assertEquals(TipoHabitat.PERRERA, carrito.getHabitats().get(0).getHabitat().getTipo());
        assertEquals(0, carrito.getHabitats().get(0).getHabitat().getOcupados());
        assertEquals(16000, carrito.getTotal());

        carrito.vaciar();
        assertTrue(carrito.estaVacio());
        assertEquals(0, carrito.getTotal());
    }
}
