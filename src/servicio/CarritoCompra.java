package servicio;

import habitat.Habitat;
import model.alimentos.Alimento;
import model.medicinas.Medicina;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    public static class HabitatItem {
        private final Habitat habitat;

        public HabitatItem(Habitat habitat) {
            this.habitat = habitat;
        }

        public Habitat getHabitat() {
            return habitat;
        }
    }

    private final List<Alimento> alimentos = new ArrayList<>();
    private final List<Medicina> medicinas = new ArrayList<>();
    private final List<HabitatItem> habitats = new ArrayList<>();

    public void agregarAlimento(Alimento alimento) {
        alimentos.add(alimento);
    }

    public void agregarMedicina(Medicina medicina) {
        medicinas.add(medicina);
    }

    public void agregarHabitat(Habitat habitat) {
        habitats.add(new HabitatItem(habitat));
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public List<Medicina> getMedicinas() {
        return medicinas;
    }

    public List<HabitatItem> getHabitats() {
        return habitats;
    }

    public int getTotal() {
        int total = 0;

        for (Alimento alimento : alimentos) {
            total += alimento.getCosto();
        }
        for (Medicina medicina : medicinas) {
            total += medicina.getCosto();
        }
        for (HabitatItem item : habitats) {
            total += item.getHabitat().getTipo().getValor();
        }

        return total;
    }

    public boolean estaVacio() {
        return alimentos.isEmpty() && medicinas.isEmpty() && habitats.isEmpty();
    }

    public void vaciar() {
        alimentos.clear();
        medicinas.clear();
        habitats.clear();
    }
}
