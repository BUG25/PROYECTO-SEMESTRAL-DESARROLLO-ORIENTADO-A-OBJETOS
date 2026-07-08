package gui;

import excepciones.DineroInsuficienteException;
import excepciones.SinHabitatDisponibleException;
import model.alimentos.Alimento;
import model.alimentos.CalidadAlimento;
import model.alimentos.Escamas;
import model.alimentos.Pellet;
import model.alimentos.PiensoGato;
import model.alimentos.PiensoPerro;
import model.alimentos.Semillas;
import model.enfermedades.Enfermedad;
import model.habitat.Habitat;
import model.habitat.TipoHabitat;
import model.mascotas.Mascota;
import model.mascotas.TipoMascota;
import model.medicinas.Antibioticos;
import model.medicinas.Antiparasitario;
import model.medicinas.Antipulgas;
import model.medicinas.Medicina;
import servicio.CarritoCompra;
import servicio.PeriodoDia;
import servicio.SimuladorDia;
import servicio.Tienda;
import servicio.TiendaMascotas;
import servicio.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TiendaGUI extends JFrame {
    private static final Color FONDO = new Color(0xEEF6F7);
    private static final Color CABECERA = new Color(0x176B87);
    private static final Color CABECERA_OSCURA = new Color(0x0F4C5C);
    private static final Color PANEL = new Color(0xFFF9EA);
    private static final Color PANEL_ALT = new Color(0xE8F5E9);
    private static final Color BORDE = new Color(0x78A083);
    private static final Color TEXTO = new Color(0x243642);
    private static final Color BOTON = new Color(0xF57C51);
    private static final Color BOTON_ALT = new Color(0x4F9D69);
    private static final Color LISTA = new Color(0xFFFFFF);

    private final Usuario usuario = new Usuario(120000);
    private final Tienda tienda = new Tienda();
    private final TiendaMascotas tiendaMascotas = new TiendaMascotas();
    private final SimuladorDia simuladorDia = new SimuladorDia(tiendaMascotas);
    private final CarritoCompra carrito = tienda.crearCarrito();

    private final DefaultListModel<Mascota> mascotasModel = new DefaultListModel<>();
    private final DefaultListModel<Habitat> habitatsModel = new DefaultListModel<>();
    private final DefaultListModel<Alimento> alimentosModel = new DefaultListModel<>();
    private final DefaultListModel<Medicina> medicinasModel = new DefaultListModel<>();
    private final DefaultListModel<String> carritoModel = new DefaultListModel<>();
    private final DefaultListModel<String> eventosModel = new DefaultListModel<>();

    private final JList<Mascota> listaMascotas = new JList<>(mascotasModel);
    private final JList<Habitat> listaHabitats = new JList<>(habitatsModel);
    private final JList<Alimento> listaAlimentos = new JList<>(alimentosModel);
    private final JList<Medicina> listaMedicinas = new JList<>(medicinasModel);
    private final JList<String> listaCarrito = new JList<>(carritoModel);

    private final JComboBox<Alimento> comboAlimento = new JComboBox<>();
    private final JComboBox<Medicina> comboMedicina = new JComboBox<>();

    private final JLabel dineroLabel = new JLabel();
    private final JLabel reputacionLabel = new JLabel();
    private final JLabel carritoTotalLabel = new JLabel();

    // --- CORRECCIÓN: Botones de ciclo de día definidos globalmente ---
    private JButton btnAvanzarManana;
    private JButton btnAvanzarTarde;
    private JButton btnAvanzarNoche;
    private JButton btnAvanzarDia;

    public TiendaGUI() {
        super("Tienda de mascotas");
        configurarVentana();
        construirInterfaz();
        refrescarTodo();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(FONDO);
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        raiz.add(construirCabecera(), BorderLayout.NORTH);
        raiz.add(construirCarrito(), BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        tabs.setBackground(CABECERA_OSCURA);
        tabs.setForeground(TEXTO);
        tabs.addTab("Tienda", construirTabTienda());
        tabs.addTab("Habitats", construirTabHabitats());
        tabs.addTab("Adopcion", construirTabAdopcion());
        tabs.addTab("Mascotas", construirTabMascotas());
        tabs.addTab("Inventario", construirTabInventario());
        tabs.addTab("Dia", construirTabDia());
        raiz.add(tabs, BorderLayout.CENTER);

        setContentPane(raiz);
    }

    private JPanel construirCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CABECERA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CABECERA_OSCURA, 2),
                new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel titulo = new JLabel("🐾 Tienda de mascotas");
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, BorderLayout.WEST);

        JPanel estado = new JPanel(new GridLayout(2, 1, 0, 4));
        estado.setOpaque(false);
        dineroLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        dineroLabel.setForeground(new Color(0xFFF176));
        reputacionLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        reputacionLabel.setForeground(new Color(0xD6F6DD));
        estado.add(dineroLabel);
        estado.add(reputacionLabel);
        panel.add(estado, BorderLayout.EAST);
        return panel;
    }

    private JPanel construirCarrito() {
        JPanel panel = panelSeccion("🛒 Carrito");
        panel.setPreferredSize(new Dimension(280, 10));
        panel.setLayout(new BorderLayout(8, 8));
        panel.add(scrollColorido(listaCarrito), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridLayout(4, 1, 0, 6));
        acciones.setOpaque(false);
        acciones.add(carritoTotalLabel);
        acciones.add(boton("Pagar carrito 💰", this::pagarCarrito));
        acciones.add(boton("Vaciar carrito 🗑️", this::vaciarCarrito));
        acciones.add(boton("Refrescar 🔄", this::refrescarTodo));
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirTabTienda() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 12));
        panel.setBackground(FONDO);
        panel.add(construirCompraAlimentos());
        panel.add(construirCompraMedicinas());
        return panel;
    }

    private JPanel construirCompraAlimentos() {
        JPanel panel = panelSeccion("🍖 Comprar alimentos");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = constraints();

        JComboBox<TipoMascota> tipo = new JComboBox<>(TipoMascota.values());
        JComboBox<CalidadAlimento> calidad = new JComboBox<>(CalidadAlimento.values());
        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        agregarFila(panel, gbc, "Mascota", tipo);
        agregarFila(panel, gbc, "Calidad", calidad);
        agregarFila(panel, gbc, "Cantidad", cantidad);
        agregarAnchoCompleto(panel, gbc, boton("Agregar alimento al carrito ➕", () -> {
            for (int i = 0; i < (Integer) cantidad.getValue(); i++) {
                tienda.agregarAlimentoAlCarrito(carrito, crearAlimento((TipoMascota) tipo.getSelectedItem(),
                        (CalidadAlimento) calidad.getSelectedItem()));
            }
            refrescarCarrito();
        }));
        return panel;
    }

    private JPanel construirCompraMedicinas() {
        JPanel panel = panelSeccion("💊 Comprar medicinas");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = constraints();

        JComboBox<String> tipo = new JComboBox<>(new String[]{"Antibioticos", "Antiparasitario", "Antipulgas"});
        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        agregarFila(panel, gbc, "Medicina", tipo);
        agregarFila(panel, gbc, "Cantidad", cantidad);
        agregarAnchoCompleto(panel, gbc, boton("Agregar medicina al carrito ➕", () -> {
            for (int i = 0; i < (Integer) cantidad.getValue(); i++) {
                tienda.agregarMedicinaAlCarrito(carrito, crearMedicina((String) tipo.getSelectedItem()));
            }
            refrescarCarrito();
        }));
        return panel;
    }

    private JPanel construirTabHabitats() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);
        panel.add(construirCompraHabitats(), BorderLayout.NORTH);
        panel.add(scrollColorido(listaHabitats), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirCompraHabitats() {
        JPanel panel = panelSeccion("🏠 Comprar habitats");
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JComboBox<TipoHabitat> tipo = new JComboBox<>(TipoHabitat.values());
        tipo.setRenderer((list, value, index, selected, focus) -> new JLabel(textoHabitat(value)));
        panel.add(tipo);
        panel.add(boton("Agregar habitat al carrito ➕", () -> {
            tienda.agregarHabitatAlCarrito(carrito, (TipoHabitat) tipo.getSelectedItem());
            refrescarCarrito();
        }));
        panel.add(boton("Limpiar habitat seleccionado 🧹", () -> {
            Habitat habitat = listaHabitats.getSelectedValue();
            if (habitat == null) {
                mostrarError("Selecciona un habitat.");
                return;
            }
            habitat.limpiar();
            refrescarTodo();
        }));
        return panel;
    }

    private JPanel construirTabAdopcion() {
        JPanel panel = panelSeccion("🐱 Adoptar mascota");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = constraints();

        JComboBox<TipoMascota> tipo = new JComboBox<>(TipoMascota.values());
        JTextField nombre = new JTextField(18);

        agregarFila(panel, gbc, "Tipo", tipo);
        agregarFila(panel, gbc, "Nombre", nombre);
        agregarAnchoCompleto(panel, gbc, boton("Adoptar 🏠", () -> {
            String nombreMascota = nombre.getText().trim();
            if (nombreMascota.isEmpty()) {
                mostrarError("Ingresa un nombre.");
                return;
            }
            try {
                Mascota mascota = tienda.adoptarMascota(usuario, (TipoMascota) tipo.getSelectedItem(), nombreMascota);
                nombre.setText("");
                registrarEvento("Adoptaste a " + mascota.getNombre() + " (" + mascota.getTipoMascota() + ").");
                refrescarTodo();
            } catch (SinHabitatDisponibleException e) {
                mostrarError(e.getMessage());
            }
        }));
        return panel;
    }

    private JPanel construirTabMascotas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);

        configurarListaMascotas();

        JScrollPane scrollMascotas = new JScrollPane(listaMascotas);
        scrollMascotas.setBorder(BorderFactory.createLineBorder(BORDE));
        scrollMascotas.getViewport().setBackground(LISTA);
        scrollMascotas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollMascotas, BorderLayout.CENTER);
        panel.add(construirAccionesMascota(), BorderLayout.EAST);
        return panel;
    }

    private void configurarListaMascotas() {
        listaMascotas.setCellRenderer(new MascotaRenderer());
        listaMascotas.setFixedCellHeight(190);
        listaMascotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarEventosMascota();
    }

    private void configurarEventosMascota() {
        listaMascotas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Mascota mascota = listaMascotas.getSelectedValue();
                    if (mascota != null) {
                        mostrarEstadisticasDetalladas(mascota);
                    }
                }
            }
        });
    }

    private JPanel construirAccionesMascota() {
        JPanel panel = panelSeccion("🎮 Acciones");
        panel.setLayout(new GridLayout(0, 1, 0, 8));
        panel.setPreferredSize(new Dimension(260, 10));

        panel.add(new JLabel("🍖 Alimento disponible"));
        panel.add(comboAlimento);
        panel.add(boton("🔄 Actualizar opciones", () -> {
            cargarCombosInventario(comboAlimento, comboMedicina);
        }));
        panel.add(boton("🍽️ Alimentar", () -> alimentarMascota(comboAlimento)));
        panel.add(boton("🎯 Jugar", this::jugarConMascota));
        panel.add(new JLabel("💊 Medicina disponible"));
        panel.add(comboMedicina);
        panel.add(boton("💉 Tratar enfermedad", () -> tratarMascota(comboMedicina)));
        panel.add(boton("💰 Vender mascota", this::venderMascota));
        panel.add(boton("📊 Ver detalle", this::mostrarDetalleMascota));

        cargarCombosInventario(comboAlimento, comboMedicina);
        return panel;
    }

    private JPanel construirTabInventario() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 12));
        panel.setBackground(FONDO);
        panel.add(panelLista("🍖 Alimentos", listaAlimentos));
        panel.add(panelLista("💊 Medicinas", listaMedicinas));
        return panel;
    }

    // --- CORRECCIÓN: Botones instanciados para el control de activación ---
    private JPanel construirTabDia() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);
        JPanel acciones = panelSeccion("⏰ Tiempo y eventos");
        acciones.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));

        btnAvanzarManana = boton("🌅 Avanzar manana", () -> avanzarPeriodo(PeriodoDia.MANANA));
        btnAvanzarTarde = boton("☀️ Avanzar tarde", () -> avanzarPeriodo(PeriodoDia.TARDE));
        btnAvanzarNoche = boton("🌙 Avanzar noche", () -> avanzarPeriodo(PeriodoDia.NOCHE));
        btnAvanzarDia = boton("📅 Avanzar dia completo", this::avanzarDia);

        acciones.add(btnAvanzarManana);
        acciones.add(btnAvanzarTarde);
        acciones.add(btnAvanzarNoche);
        acciones.add(btnAvanzarDia);

        panel.add(acciones, BorderLayout.NORTH);
        panel.add(scrollColorido(new JList<>(eventosModel)), BorderLayout.CENTER);
        return panel;
    }

    private class MascotaRenderer implements ListCellRenderer<Mascota> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Mascota> list, Mascota value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            PanelMascota panel = new PanelMascota(value);

            if (isSelected) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x176B87), 3),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                panel.setBackground(new Color(0xE3F2FD));
            }

            return panel;
        }
    }

    private void mostrarEstadisticasDetalladas(Mascota mascota) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        ImageIcon icon = PanelMascota.cargarSpriteGrande(mascota.getTipoMascota().name().toLowerCase());
        JLabel imagenLabel = new JLabel(icon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        panel.add(imagenLabel, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel nombreLabel = new JLabel("📋 Estadísticas de " + mascota.getNombre());
        nombreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panel.add(nombreLabel, gbc);

        gbc.gridy++;
        panel.add(new JLabel("🐾 Tipo: " + mascota.getTipoMascota()), gbc);

        gbc.gridy++;
        JLabel saludLabel = new JLabel("❤ Salud: " + mascota.getSalud() + "/100");
        saludLabel.setForeground(mascota.getSalud() < 30 ? Color.RED : mascota.getSalud() < 60 ? Color.ORANGE : new Color(0x4CAF50));
        panel.add(saludLabel, gbc);

        gbc.gridy++;
        JLabel felicidadLabel = new JLabel("😊 Felicidad: " + mascota.getFelicidad() + "/100");
        felicidadLabel.setForeground(mascota.getFelicidad() < 30 ? Color.RED : mascota.getFelicidad() < 60 ? Color.ORANGE : new Color(0x4CAF50));
        panel.add(felicidadLabel, gbc);

        gbc.gridy++;
        JLabel comidaLabel = new JLabel("🍖 Comida: " + mascota.getComida() + "/100");
        comidaLabel.setForeground(mascota.getComida() < 30 ? Color.RED : mascota.getComida() < 60 ? Color.ORANGE : new Color(0x4CAF50));
        panel.add(comidaLabel, gbc);

        gbc.gridy++;
        Enfermedad enfermedad = mascota.getEnfermedadActual();
        JLabel enfermedadLabel = new JLabel("Enfermedad: " + (enfermedad == null ? "Ninguna ✅" : enfermedad.getNombre()));
        enfermedadLabel.setForeground(enfermedad != null ? Color.RED : new Color(0x4CAF50));
        panel.add(enfermedadLabel, gbc);

        gbc.gridy++;
        Habitat habitat = mascota.getHabitatAsignado();
        panel.add(new JLabel("Habitat: " + (habitat == null ? "Sin asignar" : habitat.getTipo() + " (Higiene: " + habitat.getHigiene() + ")")), gbc);

        gbc.gridy++;
        boolean estaMuerta = mascota.estaMuerta();
        JLabel estadoLabel = new JLabel("Estado: " + (estaMuerta ? "Fallecida" : "Viva"));
        estadoLabel.setForeground(estaMuerta ? Color.RED : new Color(0x4CAF50));
        panel.add(estadoLabel, gbc);

        JOptionPane.showMessageDialog(this, panel, "Detalles de " + mascota.getNombre(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void pagarCarrito() {
        if (carrito.estaVacio()) {
            mostrarError("El carrito esta vacio.");
            return;
        }
        try {
            int total = carrito.getTotal();
            tienda.pagarCarrito(usuario, carrito);
            registrarEvento("Compra pagada por $" + total + ".");
            refrescarTodo();
        } catch (DineroInsuficienteException e) {
            mostrarError(e.getMessage());
        }
    }

    private void vaciarCarrito() {
        carrito.vaciar();
        refrescarCarrito();
    }

    private void alimentarMascota(JComboBox<Alimento> combo) {
        Mascota mascota = mascotaSeleccionada();
        if (mascota == null) {
            return;
        }
        Alimento alimento = (Alimento) combo.getSelectedItem();
        if (alimento == null) {
            mostrarError("No tienes alimentos en inventario.");
            return;
        }
        try {
            // CORRECCIÓN: Se le pasa el periodo actual para validar horarios
            mascota.alimentar(alimento, simuladorDia.getPeriodoActual());
            usuario.getAlimentos().remove(alimento);
            registrarEvento("🍽️ " + mascota.getNombre() + " comio " + alimento.getNombre() + " en la " + simuladorDia.getPeriodoActual() + ".");
            refrescarTodo();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private void jugarConMascota() {
        Mascota mascota = mascotaSeleccionada();
        if (mascota == null) {
            return;
        }
        mascota.jugar();
        usuario.agregarDinero(250);
        registrarEvento("🎯 Jugaste con " + mascota.getNombre() + " y ganaste $250.");
        refrescarTodo();
    }

    private void tratarMascota(JComboBox<Medicina> combo) {
        Mascota mascota = mascotaSeleccionada();
        if (mascota == null) {
            return;
        }
        Medicina medicina = (Medicina) combo.getSelectedItem();
        if (medicina == null) {
            mostrarError("No tienes medicinas en inventario.");
            return;
        }
        if (simuladorDia.tratarMascota(mascota, medicina)) {
            usuario.getMedicinas().remove(medicina);
            registrarEvento("💉 " + mascota.getNombre() + " fue tratado con " + medicina.getNombre() + ".");
            refrescarTodo();
        } else {
            mostrarError("Esa medicina no cura la enfermedad actual.");
        }
    }

    private void venderMascota() {
        Mascota mascota = mascotaSeleccionada();
        if (mascota == null) {
            return;
        }

        double prob = tiendaMascotas.calcularProbabilidadInteres(mascota);
        int pagoBase = tiendaMascotas.calcularPago(mascota);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel probLabel = new JLabel(String.format("Probabilidad de venta: %.1f%%", prob * 100));
        probLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panel.add(probLabel, BorderLayout.NORTH);

        boolean hayComprador = Math.random() < prob;

        if (!hayComprador) {
            panel.add(new JLabel("❌ No hay compradores interesados en este momento."), BorderLayout.CENTER);
            JOptionPane.showMessageDialog(this, panel, "Vender a " + mascota.getNombre(), JOptionPane.INFORMATION_MESSAGE);
            registrarEvento("Nadie quiso comprar a " + mascota.getNombre() + " (Probabilidad: " + String.format("%.1f%%", prob * 100) + ").");
            return;
        }

        JPanel compradoresPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        compradoresPanel.setBorder(BorderFactory.createTitledBorder("Compradores interesados:"));

        int numCompradores = 1 + (int)(Math.random() * 3);
        String[] nombres = {"Ana", "Carlos", "Sofia", "Miguel", "Laura", "Pedro", "Juan", "Elena"};

        ButtonGroup bg = new ButtonGroup();
        java.util.List<JRadioButton> radios = new java.util.ArrayList<>();
        java.util.List<Integer> ofertas = new java.util.ArrayList<>();

        for (int i = 0; i < numCompradores; i++) {
            String nombre = nombres[(int)(Math.random() * nombres.length)];
            int variacion = (int)(pagoBase * (Math.random() * 0.2 - 0.1));
            int oferta = pagoBase + variacion;

            JRadioButton rb = new JRadioButton("👤 " + nombre + " ofrece $" + oferta);
            rb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
            bg.add(rb);
            compradoresPanel.add(rb);
            radios.add(rb);
            ofertas.add(oferta);
        }

        if (!radios.isEmpty()) {
            radios.get(0).setSelected(true);
        }

        panel.add(compradoresPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ofertas por " + mascota.getNombre(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < radios.size(); i++) {
                if (radios.get(i).isSelected()) {
                    tiendaMascotas.procesarVenta(usuario, mascota, ofertas.get(i));
                    registrarEvento("Vendiste a " + mascota.getNombre() + " por $" + ofertas.get(i) + ".");
                    refrescarTodo();
                    return;
                }
            }
        }
    }
    private void mostrarDetalleMascota() {
        Mascota mascota = mascotaSeleccionada();
        if (mascota == null) {
            return;
        }
        mostrarEstadisticasDetalladas(mascota);
    }

    private void avanzarPeriodo(PeriodoDia destino) {
        List<Mascota> muertas = simuladorDia.avanzarPeriodo(usuario, destino);
        registrarEvento("El tiempo avanzó. Ahora es " + simuladorDia.getPeriodoActual() + " del Día " + simuladorDia.getDiasTranscurridos());
        mostrarAlertaMuertes(muertas); // Llamamos al popup
        refrescarTodo();
    }

    private void avanzarDia() {
        List<Mascota> muertas = simuladorDia.avanzarDia(usuario);
        registrarEvento("Avanzaste un dia completo. Ahora es Día " + simuladorDia.getDiasTranscurridos());
        mostrarAlertaMuertes(muertas); // Llamamos al popup
        refrescarTodo();
    }
    private void mostrarAlertaMuertes(List<Mascota> muertas) {
        // Si nadie murió, no hacemos nada y la ventana no aparece
        if (muertas == null || muertas.isEmpty()) {
            return;
        }

        StringBuilder mensaje = new StringBuilder("¡Tragedia! Las siguientes mascotas han fallecido por descuido o enfermedad:\n\n");
        for (Mascota m : muertas) {
            mensaje.append("💀 ").append(m.getNombre()).append(" (").append(m.getTipoMascota()).append(")\n");
            registrarEvento("💀 " + m.getNombre() + " ha fallecido.");
        }

        JOptionPane.showMessageDialog(this, mensaje.toString(), "Mascotas fallecidas", JOptionPane.ERROR_MESSAGE);
    }
    private Mascota mascotaSeleccionada() {
        Mascota mascota = listaMascotas.getSelectedValue();
        if (mascota == null) {
            mostrarError("Selecciona una mascota.");
        }
        return mascota;
    }

    private void cargarCombosInventario(JComboBox<Alimento> alimentos, JComboBox<Medicina> medicinas) {
        alimentos.setModel(new DefaultComboBoxModel<>(usuario.getAlimentos().toArray(new Alimento[0])));
        medicinas.setModel(new DefaultComboBoxModel<>(usuario.getMedicinas().toArray(new Medicina[0])));
    }

    private void refrescarTodo() {
        dineroLabel.setText(String.format("Dinero: $%.0f", usuario.getDinero()));
        reputacionLabel.setText("Reputacion: " + tiendaMascotas.getReputacion() +
                " | Día: " + simuladorDia.getDiasTranscurridos() +
                " (" + simuladorDia.getPeriodoActual() + ")");

        // --- CORRECCIÓN: Habilitar o deshabilitar botones según el turno ---
        PeriodoDia actual = simuladorDia.getPeriodoActual();
        if (btnAvanzarManana != null) {
            // Solo deja ir a la tarde si es de mañana
            btnAvanzarTarde.setEnabled(actual == PeriodoDia.MANANA);
            // Solo deja ir a la noche si es de tarde
            btnAvanzarNoche.setEnabled(actual == PeriodoDia.TARDE);
            // Solo deja saltar a la mañana del dia siguiente si ya es de noche
            btnAvanzarManana.setEnabled(actual == PeriodoDia.NOCHE);
        }

        refrescarModelo(mascotasModel, usuario.getMascotas());
        refrescarModelo(habitatsModel, usuario.getHabitats());
        refrescarModelo(alimentosModel, usuario.getAlimentos());
        refrescarModelo(medicinasModel, usuario.getMedicinas());
        refrescarCarrito();

        cargarCombosInventario(comboAlimento, comboMedicina);
        listaMascotas.repaint();
    }

    private void refrescarCarrito() {
        carritoModel.clear();
        for (Alimento alimento : carrito.getAlimentos()) {
            carritoModel.addElement("Alimento: " + textoAlimento(alimento));
        }
        for (Medicina medicina : carrito.getMedicinas()) {
            carritoModel.addElement("Medicina: " + textoMedicina(medicina));
        }
        for (CarritoCompra.HabitatItem item : carrito.getHabitats()) {
            carritoModel.addElement("Habitat: " + textoHabitat(item.getHabitat().getTipo()));
        }
        carritoTotalLabel.setText("Total: $" + carrito.getTotal());
    }

    private <T> void refrescarModelo(DefaultListModel<T> modelo, List<T> datos) {
        modelo.clear();
        for (T dato : datos) {
            modelo.addElement(dato);
        }
    }

    private Alimento crearAlimento(TipoMascota tipo, CalidadAlimento calidad) {
        return switch (tipo) {
            case PERRO -> new PiensoPerro(calidad);
            case GATO -> new PiensoGato(calidad);
            case PEZ -> new Escamas(calidad);
            case HAMSTER -> new Pellet(calidad);
            case PAJARO -> new Semillas(calidad);
        };
    }

    private Medicina crearMedicina(String tipo) {
        return switch (tipo) {
            case "Antibioticos" -> new Antibioticos();
            case "Antiparasitario" -> new Antiparasitario();
            case "Antipulgas" -> new Antipulgas();
            default -> throw new IllegalArgumentException("Medicina no soportada: " + tipo);
        };
    }

    private String textoHabitat(TipoHabitat tipo) {
        return tipo + " - $" + tipo.getValor() + " - capacidad " + tipo.getCapacidadMaxima();
    }

    private String textoAlimento(Alimento alimento) {
        return alimento.getNombre() + " " + alimento.getCalidad() + " - $" + alimento.getCosto();
    }

    private String textoMedicina(Medicina medicina) {
        return medicina.getNombre() + " - $" + medicina.getCosto();
    }

    private void registrarEvento(String evento) {
        eventosModel.add(0, evento);
    }

    private JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setBackground(texto.contains("Pagar") || texto.contains("Adoptar") ||
                texto.contains("Agregar") || texto.contains("Adoptar") ? BOTON : BOTON_ALT);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                new EmptyBorder(7, 10, 7, 10)
        ));
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    private JPanel panelLista(String titulo, JList<?> lista) {
        JPanel panel = panelSeccion(titulo);
        panel.setLayout(new BorderLayout());
        panel.add(scrollColorido(lista), BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelSeccion(String titulo) {
        JPanel panel = new JPanel();
        panel.setBackground(titulo.hashCode() % 2 == 0 ? PANEL : PANEL_ALT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE, 2), titulo),
                new EmptyBorder(8, 8, 8, 8)
        ));
        return panel;
    }

    private JScrollPane scrollColorido(JList<?> lista) {
        lista.setBackground(LISTA);
        lista.setForeground(TEXTO);
        lista.setSelectionBackground(new Color(0xFFD166));
        lista.setSelectionForeground(new Color(0x1F2933));
        lista.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        lista.setFixedCellHeight(28);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(LISTA);
        return scroll;
    }

    private GridBagConstraints constraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, String etiqueta, Component componente) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel label = new JLabel(etiqueta);
        label.setForeground(TEXTO);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(componente, gbc);
        gbc.gridy++;
    }

    private void agregarAnchoCompleto(JPanel panel, GridBagConstraints gbc, Component componente) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(componente, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaGUI().setVisible(true));
    }
}