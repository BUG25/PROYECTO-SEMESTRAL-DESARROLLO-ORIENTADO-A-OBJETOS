package gui;

import excepciones.DineroInsuficienteException;
import excepciones.SinHabitatDisponibleException;
import habitat.Habitat;
import habitat.TipoHabitat;
import model.mascotas.Mascota;
import model.mascotas.TipoMascota;
import servicio.Tienda;
import servicio.Usuario;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Interfaz gráfica (Swing) para el proyecto "Tienda de Mascotas".
 * No cambia ninguna regla de negocio: solo llama a Tienda / Usuario / Mascota
 * tal como están definidas. Ambientada como una tienda física de verdad:
 * paredes de ladrillo, piso de baldosas, entrada con puerta y tapete,
 * campanita al entrar, carrito visible y recibos de compra. 🐾
 */
public class TiendaGUI extends JFrame {

    // ---- Lógica real del proyecto ----
    private final Usuario usuario = new Usuario(150.0);
    private final Tienda tienda = new Tienda();

    // ---- Paleta "tienda física": maderas, ladrillo, baldosas y latón ----
    private static final Color MADERA_OSCURA = new Color(0x4A2C17);
    private static final Color MADERA_MEDIA  = new Color(0x7C4A24);
    private static final Color MADERA_CLARA  = new Color(0xA9713F);
    private static final Color MADERA_VETA   = new Color(0x6B3E1E);
    private static final Color PISO_TIENDA   = new Color(0xEFDFC2);
    private static final Color PAPEL         = new Color(0xFBF3E1);
    private static final Color CARTON        = new Color(0xF1E2BE);
    private static final Color TINTA         = new Color(0x3B2A1A);
    private static final Color LATON         = new Color(0xC9A24B);
    private static final Color TOLDO_A       = new Color(0xA13D3D);
    private static final Color TOLDO_B       = new Color(0xF2E9D8);
    private static final Color VERDE_LETRERO = new Color(0x2F6B4F);
    private static final Color HILO          = new Color(0x9C8A6B);
    private static final Color LADRILLO      = new Color(0xA9542E);
    private static final Color MORTERO       = new Color(0xE4D3B0);
    private static final Color BALDOSA_OSCURA = new Color(0xE0C89C);

    // ---- Compatibilidad con nombres usados en el resto de la clase ----
    private static final Color FONDO = PISO_TIENDA;
    private static final Color PRIMARIO = TOLDO_A;
    private static final Color SECUNDARIO = VERDE_LETRERO;
    private static final Color TARJETA = PAPEL;
    private static final Color TEXTO = TINTA;
    private static final Color BORDE = MADERA_CLARA;

    // ---- Datos auxiliares (emojis / precios) ----
    private final Map<TipoMascota, String> emojiMascota = new EnumMap<>(TipoMascota.class);
    private final Map<TipoHabitat, String> emojiHabitat = new EnumMap<>(TipoHabitat.class);
    private final Map<TipoHabitat, Double> costoHabitat = new EnumMap<>(TipoHabitat.class);
    private static final int CAPACIDAD_HABITAT = 3;

    // ---- Navegación entre "entrada" y "tienda" ----
    private final CardLayout cardLayout = new CardLayout();
    private JPanel panelRaiz;

    // ---- Carrito / historial de compras ----
    private final List<String> historialCompras = new ArrayList<>();
    private double totalGastado = 0.0;
    private JPanel panelCarritoLista;
    private JLabel lblTotalCarrito;

    // ---- Componentes que se refrescan ----
    private JLabel lblDinero;
    private JPanel panelMascotas;
    private JPanel panelHabitats;
    private JLabel lblTotalMascotas;
    private JLabel lblPromedioSalud;
    private JLabel lblPromedioFelicidad;
    
    // ---- Panel lateral para mascotas seleccionadas ----
    private JPanel panelLateralRaiz;
    private Mascota mascotaSeleccionada;

    public TiendaGUI() {
        super("🐾 Tienda de Mascotas 🐾");
        inicializarDatosAuxiliares();
        configurarVentana();
        construirInterfaz();
        iniciarSimulacionDelTiempo();
    }

    private void inicializarDatosAuxiliares() {
        emojiMascota.put(TipoMascota.PERRO, "🐶");
        emojiMascota.put(TipoMascota.GATO, "🐱");
        emojiMascota.put(TipoMascota.PEZ, "🐟");
        emojiMascota.put(TipoMascota.HAMSTER, "🐹");
        emojiMascota.put(TipoMascota.PAJARO, "🐦");

        emojiHabitat.put(TipoHabitat.PERRERA, "🏠");
        emojiHabitat.put(TipoHabitat.ARENERO, "🪟");
        emojiHabitat.put(TipoHabitat.PECERA, "🐠");
        emojiHabitat.put(TipoHabitat.JAULA_PAJARO, "🌳");
        emojiHabitat.put(TipoHabitat.JAULA_HAMSTER, "🎡");

        costoHabitat.put(TipoHabitat.PERRERA, 30.0);
        costoHabitat.put(TipoHabitat.ARENERO, 25.0);
        costoHabitat.put(TipoHabitat.PECERA, 20.0);
        costoHabitat.put(TipoHabitat.JAULA_PAJARO, 15.0);
        costoHabitat.put(TipoHabitat.JAULA_HAMSTER, 15.0);
    }

    private void configurarVentana() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
    }

    private void construirInterfaz() {
        panelRaiz = new JPanel(cardLayout);
        panelRaiz.add(construirPantallaEntrada(), "entrada");
        panelRaiz.add(construirPantallaTienda(), "tienda");
        setContentPane(panelRaiz);
        cardLayout.show(panelRaiz, "entrada");

        mascotaSeleccionada = null;
        mostrarCarrito();
        refrescarMascotas();
        refrescarHabitats();
    }

    // =========================================================
    // PANTALLA DE ENTRADA: puerta, tapete "BIENVENIDO" y campana
    // =========================================================
    private JComponent construirPantallaEntrada() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(MADERA_OSCURA);

        EntradaGraficoPanel grafico = new EntradaGraficoPanel();
        raiz.add(grafico, BorderLayout.CENTER);

        JPanel pie = new JPanel();
        pie.setBackground(MADERA_OSCURA);
        pie.setBorder(new EmptyBorder(16, 16, 22, 16));
        JButton btnEntrar = botonBonito("🔔 Tocar la campana y entrar", SECUNDARIO);
        btnEntrar.addActionListener(e -> {
            reproducirCampana();
            cardLayout.show(panelRaiz, "tienda");
        });
        pie.add(btnEntrar);
        raiz.add(pie, BorderLayout.SOUTH);

        return raiz;
    }

    // =========================================================
    // PANTALLA PRINCIPAL DE LA TIENDA
    // =========================================================
    private JComponent construirPantallaTienda() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        raiz.add(construirCabecera(), BorderLayout.NORTH);
        raiz.add(construirCarritoLateral(), BorderLayout.WEST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setUI(new PasilloTabbedPaneUI());
        tabs.setFont(new Font("Serif", Font.BOLD, 15));
        tabs.setForeground(PAPEL);
        tabs.setBackground(MADERA_MEDIA);
        tabs.addTab("🏠 Pasillo 1 · Hábitats", construirTabHabitats());
        tabs.addTab("🐾 Pasillo 2 · Mascotas", construirTabAdopcion());
        tabs.addTab("❤️ Mostrador · Mis Mascotas", construirTabMisMascotas());
        raiz.add(tabs, BorderLayout.CENTER);

        return raiz;
    }

    // =========================================================
    // CABECERA: cartel de madera colgante + caja registradora
    // =========================================================
    private JComponent construirCabecera() {
        CarteloTiendaPanel cabecera = new CarteloTiendaPanel();
        cabecera.setLayout(new BorderLayout());
        cabecera.setBorder(new EmptyBorder(26, 28, 16, 28));
        cabecera.setPreferredSize(new Dimension(10, 118));

        JLabel titulo = new JLabel("🐾 TIENDA DE MASCOTAS 🐾");
        titulo.setFont(new Font("Serif", Font.BOLD, 28));
        titulo.setForeground(LATON);

        JLabel subtitulo = new JLabel("Abierto todos los días  ·  ¡Bienvenido/a!");
        subtitulo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        subtitulo.setForeground(new Color(0xE9D7B0));

        JPanel oeste = new JPanel();
        oeste.setOpaque(false);
        oeste.setLayout(new BoxLayout(oeste, BoxLayout.Y_AXIS));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        oeste.add(titulo);
        oeste.add(Box.createVerticalStrut(2));
        oeste.add(subtitulo);
        cabecera.add(oeste, BorderLayout.WEST);

        lblDinero = new JLabel();
        lblDinero.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblDinero.setForeground(new Color(0x39FF6A));
        lblDinero.setHorizontalAlignment(SwingConstants.CENTER);
        actualizarDinero();

        CajaRegistradoraPanel caja = new CajaRegistradoraPanel(lblDinero);
        cabecera.add(caja, BorderLayout.EAST);

        return cabecera;
    }

    private void actualizarDinero() {
        lblDinero.setText(String.format("$ %07.2f", usuario.getDinero()));
    }

    // =========================================================
    // CARRITO LATERAL: lo que llevas puesto siempre a la vista
    // =========================================================
    private JComponent construirCarritoLateral() {
        panelLateralRaiz = new TarjetaPanel();
        panelLateralRaiz.setLayout(new BorderLayout(6, 6));
        panelLateralRaiz.setPreferredSize(new Dimension(230, 10));
        
        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setOpaque(false);
        envoltura.setBorder(new EmptyBorder(16, 12, 16, 4));
        envoltura.add(panelLateralRaiz, BorderLayout.CENTER);
        return envoltura;
    }
    
    private void mostrarCarrito() {
        if (panelLateralRaiz == null) return;
        panelLateralRaiz.removeAll();
        mascotaSeleccionada = null;
        
        JLabel titulo = new JLabel("🛒 Tu Carrito", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 16));
        titulo.setForeground(TEXTO);
        panelLateralRaiz.add(titulo, BorderLayout.NORTH);

        panelCarritoLista = new JPanel();
        panelCarritoLista.setLayout(new BoxLayout(panelCarritoLista, BoxLayout.Y_AXIS));
        panelCarritoLista.setOpaque(false);
        JScrollPane scroll = new JScrollPane(panelCarritoLista);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        panelLateralRaiz.add(scroll, BorderLayout.CENTER);

        lblTotalCarrito = new JLabel();
        lblTotalCarrito.setFont(new Font("Monospaced", Font.BOLD, 13));
        lblTotalCarrito.setForeground(TEXTO);
        lblTotalCarrito.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalCarrito.setBorder(new EmptyBorder(6, 0, 0, 0));
        panelLateralRaiz.add(lblTotalCarrito, BorderLayout.SOUTH);
        
        panelLateralRaiz.revalidate();
        panelLateralRaiz.repaint();
        refrescarCarrito();
    }
    
    private void mostrarInfoMascota(Mascota mascota) {
        mascotaSeleccionada = mascota;
        panelLateralRaiz.removeAll();
        
        JLabel titulo = new JLabel("🐾 " + mascota.getNombre(), SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 16));
        titulo.setForeground(TEXTO);
        panelLateralRaiz.add(titulo, BorderLayout.NORTH);
        
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        addFilaInfo(info, "Especie:", mascota.getClass().getSimpleName());
        addFilaInfo(info, "❤️ Salud:", mascota.getSalud() + "%");
        addFilaInfo(info, "🍖 Comida:", mascota.getComida() + "%");
        addFilaInfo(info, "😊 Felicidad:", mascota.getFelicidad() + "%");
        
        JScrollPane scroll = new JScrollPane(info);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        panelLateralRaiz.add(scroll, BorderLayout.CENTER);
        
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        botones.setOpaque(false);
        
        JButton btnAlimentar = botonPequeno("🍖 Alimentar", PRIMARIO);
        btnAlimentar.addActionListener(e -> {
            mascota.alimentar();
            refrescarMascotas();
            mostrarInfoMascota(mascota);
        });
        botones.add(btnAlimentar);
        
        JButton btnJugar = botonPequeno("🎾 Jugar", SECUNDARIO);
        btnJugar.addActionListener(e -> {
            mascota.jugar();
            refrescarMascotas();
            mostrarInfoMascota(mascota);
        });
        botones.add(btnJugar);
        
        JButton btnVolver = botonPequeno("← Volver", new Color(0x7C7C7C));
        btnVolver.addActionListener(e -> mostrarCarrito());
        botones.add(btnVolver);
        
        panelLateralRaiz.add(botones, BorderLayout.SOUTH);
        
        panelLateralRaiz.revalidate();
        panelLateralRaiz.repaint();
    }
    
    private void addFilaInfo(JPanel panel, String etiqueta, String valor) {
        JLabel lbl = new JLabel(etiqueta + " " + valor);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(3, 0, 3, 0));
        panel.add(lbl);
    }

    private void refrescarCarrito() {
        panelCarritoLista.removeAll();
        if (historialCompras.isEmpty()) {
            JLabel vacio = new JLabel("<html><i>Tu carrito está vacío.<br>¡Compra algo! 🛍️</i></html>");
            vacio.setForeground(new Color(0x7A6A52));
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 12));
            vacio.setBorder(new EmptyBorder(20, 10, 10, 10));
            panelCarritoLista.add(vacio);
        } else {
            for (String linea : historialCompras) {
                JLabel lbl = new JLabel("<html>" + linea + "</html>");
                lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
                lbl.setForeground(TEXTO);
                lbl.setBorder(new EmptyBorder(4, 4, 4, 4));
                lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCarritoLista.add(lbl);
                panelCarritoLista.add(new JSeparator());
            }
        }
        lblTotalCarrito.setText(String.format("Gastado: $%.2f", totalGastado));
        panelCarritoLista.revalidate();
        panelCarritoLista.repaint();
    }

    // =========================================================
    // TAB: COMPRAR HABITAT (pasillo de estantes)
    // =========================================================
    private JComponent construirTabHabitats() {
        JPanel raiz = new PasilloPanel();
        raiz.setLayout(new BorderLayout(12, 12));
        raiz.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel compra = new TarjetaPanel();
        compra.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel etiqueta = new JLabel("🧾 Elige un hábitat de exhibición:");
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setForeground(TEXTO);

        JComboBox<TipoHabitat> combo = new JComboBox<>(TipoHabitat.values());
        combo.setRenderer(new ComboEmojiRenderer<TipoHabitat>(t -> emojiHabitat.get(t) + "  " + formatearNombre(t.name())
                + "   ($" + costoHabitat.get(t).intValue() + ", capacidad " + CAPACIDAD_HABITAT + ")"));
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton btnComprar = botonBonito("🛒 Comprar", SECUNDARIO);
        btnComprar.addActionListener(e -> {
            TipoHabitat tipo = (TipoHabitat) combo.getSelectedItem();
            double costo = costoHabitat.get(tipo);
            try {
                tienda.comprarHabitat(usuario, tipo, CAPACIDAD_HABITAT, costo);
                actualizarDinero();
                refrescarHabitats();
                historialCompras.add(emojiHabitat.get(tipo) + " " + formatearNombre(tipo.name())
                        + " &nbsp;<b>$" + String.format("%.2f", costo) + "</b>");
                totalGastado += costo;
                refrescarCarrito();
                mostrarReciboCompraHabitat(tipo, costo);
            } catch (DineroInsuficienteException ex) {
                mostrarError("😢 " + ex.getMessage());
            }
        });

        compra.add(etiqueta);
        compra.add(combo);
        compra.add(btnComprar);
        raiz.add(compra, BorderLayout.NORTH);

        panelHabitats = new JPanel();
        panelHabitats.setLayout(new BoxLayout(panelHabitats, BoxLayout.Y_AXIS));
        panelHabitats.setOpaque(false);
        JScrollPane scroll = new JScrollPane(panelHabitats);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        raiz.add(scroll, BorderLayout.CENTER);

        return raiz;
    }

    private void refrescarHabitats() {
        panelHabitats.removeAll();
        if (usuario.getHabitats().isEmpty()) {
            panelHabitats.add(mensajeVacio("Este estante está vacío. ¡Compra un hábitat arriba! 🏗️"));
        } else {
            for (Habitat h : usuario.getHabitats()) {
                panelHabitats.add(tarjetaHabitat(h));
                panelHabitats.add(new EstantePlank());
                panelHabitats.add(Box.createVerticalStrut(10));
            }
        }
        panelHabitats.revalidate();
        panelHabitats.repaint();
    }

    private JComponent tarjetaHabitat(Habitat h) {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(10, 4));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));

        JLabel emoji = new JLabel(emojiHabitat.get(h.getTipo()));
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 30));
        tarjeta.add(emoji, BorderLayout.WEST);

        JPanel centro = new JPanel(new GridLayout(2, 1));
        centro.setOpaque(false);
        JLabel nombre = new JLabel(formatearNombre(h.getTipo().name()));
        nombre.setFont(new Font("Serif", Font.BOLD, 15));
        nombre.setForeground(TEXTO);
        centro.add(nombre);

        JProgressBar barra = new JProgressBar(0, h.getCapacidadMaxima());
        barra.setValue(h.getOcupados());
        barra.setStringPainted(true);
        barra.setString("👤 " + h.getOcupados() + " / " + h.getCapacidadMaxima() + " ocupados");
        barra.setForeground(SECUNDARIO);
        centro.add(barra);

        tarjeta.add(centro, BorderLayout.CENTER);
        tarjeta.add(new EtiquetaPrecio("EN CASA"), BorderLayout.EAST);
        return tarjeta;
    }

    // =========================================================
    // TAB: ADOPTAR MASCOTA (mostrador de adopción)
    // =========================================================
    private JComponent construirTabAdopcion() {
        JPanel raiz = new PasilloPanel();
        raiz.setLayout(new GridBagLayout());
        raiz.setBorder(new EmptyBorder(30, 30, 30, 30));

        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setPreferredSize(new Dimension(440, 270));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titulo = new JLabel("😻 Mostrador de adopciones 😻");
        titulo.setFont(new Font("Serif", Font.BOLD, 18));
        titulo.setForeground(TEXTO);
        tarjeta.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        tarjeta.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1;
        JComboBox<TipoMascota> comboMascota = new JComboBox<>(TipoMascota.values());
        comboMascota.setRenderer(new ComboEmojiRenderer<TipoMascota>(t -> emojiMascota.get(t) + "  " + formatearNombre(t.name())));
        tarjeta.add(comboMascota, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        tarjeta.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        JTextField campoNombre = new JTextField(14);
        tarjeta.add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnAdoptar = botonBonito("🐾 Adoptar", PRIMARIO);
        btnAdoptar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarError("Ponle un nombre a tu nueva mascota primero 😊");
                return;
            }
            TipoMascota tipo = (TipoMascota) comboMascota.getSelectedItem();
            try {
                Mascota m = tienda.adoptarMascota(usuario, tipo, nombre);
                refrescarMascotas();
                campoNombre.setText("");
                historialCompras.add(emojiMascota.get(tipo) + " Adopción: <b>" + m.getNombre() + "</b>");
                refrescarCarrito();
                mostrarReciboAdopcion(tipo, m.getNombre());
            } catch (SinHabitatDisponibleException ex) {
                mostrarError("😿 " + ex.getMessage());
            }
        });
        tarjeta.add(btnAdoptar, gbc);

        raiz.add(tarjeta);
        return raiz;
    }

    // =========================================================
    // TAB: MIS MASCOTAS (exhibidor / estantería principal)
    // =========================================================
    private JComponent construirTabMisMascotas() {
        JPanel raiz = new PasilloPanel();
        raiz.setLayout(new BorderLayout());
        raiz.setBorder(new EmptyBorder(16, 16, 16, 16));

        panelMascotas = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                pintarLadrillos(g2, 0, 0, w, Math.min(60, Math.max(30, h / 6)), LADRILLO, MORTERO);
                pintarPisoBaldosas(g2, 0, Math.min(60, Math.max(30, h / 6)), w, h - Math.min(60, Math.max(30, h / 6)), PISO_TIENDA, BALDOSA_OSCURA);
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRect(0, Math.min(60, Math.max(30, h / 6)), w, 3);
                g2.dispose();
            }
        };
        panelMascotas.setLayout(null);
        panelMascotas.setOpaque(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(0x6D4C41));
        infoPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));

        lblTotalMascotas = new JLabel("🐾 Total: 0");
        lblTotalMascotas.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotalMascotas.setForeground(Color.WHITE);
        infoPanel.add(lblTotalMascotas);

        lblPromedioSalud = new JLabel("❤️ Salud: 0%");
        lblPromedioSalud.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPromedioSalud.setForeground(Color.WHITE);
        infoPanel.add(lblPromedioSalud);

        lblPromedioFelicidad = new JLabel("😊 Felicidad: 0%");
        lblPromedioFelicidad.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPromedioFelicidad.setForeground(Color.WHITE);
        infoPanel.add(lblPromedioFelicidad);

        JButton btnOrdenar = new JButton("🔄 Reorganizar");
        btnOrdenar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnOrdenar.setBackground(new Color(0x4E342E));
        btnOrdenar.setForeground(Color.WHITE);
        btnOrdenar.setBorder(new EmptyBorder(6, 12, 6, 12));
        btnOrdenar.setFocusPainted(false);
        btnOrdenar.addActionListener(e -> refrescarMascotas());
        infoPanel.add(btnOrdenar);

        raiz.add(infoPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(panelMascotas);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        raiz.add(scroll, BorderLayout.CENTER);

        return raiz;
    }

    private void refrescarMascotas() {
        for (Component comp : panelMascotas.getComponents()) {
            if (comp instanceof SpriteMascota) {
                ((SpriteMascota) comp).detener();
            }
        }
        panelMascotas.removeAll();

        if (usuario.getMascotas().isEmpty()) {
            JLabel mensaje = new JLabel("🐾 No hay mascotas en el parque. ¡Adopta una! 🌟", SwingConstants.CENTER);
            mensaje.setFont(new Font("Serif", Font.ITALIC, 20));
            mensaje.setForeground(Color.WHITE);
            mensaje.setBounds(0, 0, panelMascotas.getWidth(), panelMascotas.getHeight());
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            panelMascotas.add(mensaje);

            lblTotalMascotas.setText("🐾 Total: 0");
            lblPromedioSalud.setText("❤️ Salud: 0%");
            lblPromedioFelicidad.setText("😊 Felicidad: 0%");
        } else {
            int ancho = Math.max(panelMascotas.getWidth(), 400);
            int alto = Math.max(panelMascotas.getHeight(), 300);

            double saludProm = 0;
            double felicidadProm = 0;

            for (Mascota m : usuario.getMascotas()) {
                saludProm += m.getSalud();
                felicidadProm += m.getFelicidad();

                SpriteMascota sprite = new SpriteMascota(m, ancho, alto, this::refrescarMascotas);
                panelMascotas.add(sprite);
            }

            int total = usuario.getMascotas().size();
            saludProm = total > 0 ? saludProm / total : 0;
            felicidadProm = total > 0 ? felicidadProm / total : 0;

            lblTotalMascotas.setText("🐾 Total: " + total);
            lblPromedioSalud.setText(String.format("❤️ Salud: %.0f%%", saludProm));
            lblPromedioFelicidad.setText(String.format("😊 Felicidad: %.0f%%", felicidadProm));
        }

        panelMascotas.revalidate();
        panelMascotas.repaint();
    }

    private JComponent tarjetaMascota(Mascota m) {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(14, 8));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 154));

        JLabel emoji = new JLabel(emojiDeMascota(m));
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 46));
        emoji.setBorder(new EmptyBorder(0, 6, 0, 6));
        tarjeta.add(emoji, BorderLayout.WEST);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel nombre = new JLabel(m.getNombre() + "  (" + formatearNombre(m.getClass().getSimpleName()) + ")");
        nombre.setFont(new Font("Serif", Font.BOLD, 16));
        nombre.setForeground(TEXTO);
        centro.add(nombre);
        centro.add(Box.createVerticalStrut(6));

        centro.add(barraEstado("🍖 Comida", m.getComida()));
        centro.add(barraEstado("💊 Salud", m.getSalud()));
        centro.add(barraEstado("😊 Felicidad", m.getFelicidad()));

        tarjeta.add(centro, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(3, 1, 0, 6));
        botones.setOpaque(false);
        JButton btnAlimentar = botonPequeno("🍖 Alimentar", SECUNDARIO);
        JButton btnJugar = botonPequeno("🎾 Jugar", PRIMARIO);
        JButton btnLimpiar = botonPequeno("🧼 Limpiar", new Color(0x3E6E96));

        btnAlimentar.addActionListener(e -> { m.alimentar(); refrescarMascotas(); });
        btnJugar.addActionListener(e -> { m.jugar(); refrescarMascotas(); });
        btnLimpiar.addActionListener(e -> { m.limpiarHabitat(); refrescarMascotas(); });

        botones.add(btnAlimentar);
        botones.add(btnJugar);
        botones.add(btnLimpiar);
        tarjeta.add(botones, BorderLayout.EAST);

        return tarjeta;
    }

    private JComponent barraEstado(String etiqueta, int valor) {
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setPreferredSize(new Dimension(90, 18));
        fila.add(lbl, BorderLayout.WEST);

        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(valor);
        barra.setStringPainted(true);
        barra.setString(valor + "%");
        if (valor >= 60) {
            barra.setForeground(new Color(0x4C8C5B));
        } else if (valor >= 30) {
            barra.setForeground(new Color(0xC99A34));
        } else {
            barra.setForeground(new Color(0xB33A3A));
        }
        fila.add(barra, BorderLayout.CENTER);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return fila;
    }

    private String emojiDeMascota(Mascota m) {
        for (TipoMascota t : TipoMascota.values()) {
            if (m.getClass().getSimpleName().equalsIgnoreCase(formatearNombre(t.name()))) {
                return emojiMascota.get(t);
            }
        }
        return "🐾";
    }

    // =========================================================
    // SIMULACIÓN DEL PASO DEL TIEMPO (desgaste natural)
    // =========================================================
    private void iniciarSimulacionDelTiempo() {
        Timer timer = new Timer(8000, e -> {
            for (Mascota m : usuario.getMascotas()) {
                m.degradarConElTiempo();
            }
            refrescarMascotas();
        });
        timer.start();
    }

    // =========================================================
    // RECIBOS DE COMPRA (ticket de papel con bordes dentados)
    // =========================================================
    private void mostrarReciboCompraHabitat(TipoHabitat tipo, double costo) {
        int ancho = 28;
        List<String> lineas = new ArrayList<>();
        lineas.add(centrarTexto("TIENDA DE MASCOTAS", ancho));
        lineas.add(centrarTexto("Boleta de compra", ancho));
        lineas.add("-".repeat(ancho));
        lineas.add(fechaHoraActual());
        lineas.add("");
        lineas.add(String.format("%-18s $%6.2f", formatearNombre(tipo.name()), costo));
        lineas.add("-".repeat(ancho));
        lineas.add(String.format("%-18s $%6.2f", "TOTAL", costo));
        lineas.add(String.format("%-18s $%6.2f", "Saldo disponible", usuario.getDinero()));
        lineas.add("");
        lineas.add(centrarTexto("¡Gracias por su compra!", ancho));
        mostrarRecibo(lineas);
    }

    private void mostrarReciboAdopcion(TipoMascota tipo, String nombre) {
        int ancho = 28;
        List<String> lineas = new ArrayList<>();
        lineas.add(centrarTexto("TIENDA DE MASCOTAS", ancho));
        lineas.add(centrarTexto("Certificado de adopción", ancho));
        lineas.add("-".repeat(ancho));
        lineas.add(fechaHoraActual());
        lineas.add("");
        lineas.add(String.format("%-14s %s", "Especie:", formatearNombre(tipo.name())));
        lineas.add(String.format("%-14s %s", "Nombre:", nombre));
        lineas.add(String.format("%-18s $%6.2f", "Costo adopción", 0.0));
        lineas.add("-".repeat(ancho));
        lineas.add(centrarTexto("¡Feliz nueva familia!", ancho));
        mostrarRecibo(lineas);
    }

    private void mostrarRecibo(List<String> lineas) {
        JDialog dialog = new JDialog(this, "🧾 Recibo", true);
        dialog.getContentPane().setBackground(PISO_TIENDA);
        dialog.setLayout(new BorderLayout());

        ReciboPanel recibo = new ReciboPanel(lineas);
        JPanel envoltura = new JPanel(new FlowLayout());
        envoltura.setBackground(PISO_TIENDA);
        envoltura.add(recibo);
        dialog.add(envoltura, BorderLayout.CENTER);

        JButton btnCerrar = botonBonito("✅ Guardar recibo", SECUNDARIO);
        btnCerrar.addActionListener(e -> dialog.dispose());
        JPanel pie = new JPanel();
        pie.setBackground(PISO_TIENDA);
        pie.add(btnCerrar);
        dialog.add(pie, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private static String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int espacios = ancho - texto.length();
        int izquierda = espacios / 2;
        int derecha = espacios - izquierda;
        return " ".repeat(izquierda) + texto + " ".repeat(derecha);
    }

    private static String fechaHoraActual() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // CAMPANITA DE LA PUERTA
    private void reproducirCampana() {
        new Thread(() -> {
            try {
                reproducirTono(880.0, 140);
                Thread.sleep(30);
                reproducirTono(659.0, 220);
            } catch (Exception ignorado) {
                // Si el equipo no tiene salida de audio disponible, simplemente no suena.
            }
        }, "campana-tienda").start();
    }

    private void reproducirTono(double frecuenciaHz, int duracionMs) throws Exception {
        float sampleRate = 44100f;
        int numSamples = (int) (duracionMs * sampleRate / 1000);
        byte[] buffer = new byte[numSamples];
        for (int i = 0; i < numSamples; i++) {
            double angulo = 2.0 * Math.PI * i * frecuenciaHz / sampleRate;
            double envolvente = 1.0 - (double) i / numSamples;
            buffer[i] = (byte) (Math.sin(angulo) * envolvente * 90);
        }
        AudioFormat formato = new AudioFormat(sampleRate, 8, 1, true, true);
        SourceDataLine linea = null;
        try {
            linea = AudioSystem.getSourceDataLine(formato);
            linea.open(formato);
            linea.start();
            linea.write(buffer, 0, buffer.length);
            linea.drain();
        } finally {
            if (linea != null) linea.close();
        }
    }

    // =========================================================
    // UTILIDADES DE ESTILO
    // =========================================================
    private JButton botonBonito(String texto, Color color) {
        JButton boton = new BotonMadera(texto, color);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBorder(new EmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton botonPequeno(String texto, Color color) {
        JButton boton = new BotonMadera(texto, color);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBorder(new EmptyBorder(6, 10, 6, 10));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JComponent mensajeVacio(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lbl.setForeground(new Color(0x7A6A52));
        lbl.setBorder(new EmptyBorder(30, 10, 10, 10));
        return lbl;
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "😀 Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "⚠️ Atención", JOptionPane.WARNING_MESSAGE);
    }

    private String formatearNombre(String enumName) {
        String[] partes = enumName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : partes) {
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    // =========================================================
    // PATRONES REUTILIZABLES: ladrillo y baldosas
    // =========================================================
    private static void pintarLadrillos(Graphics2D g2, int x, int y, int w, int h, Color ladrillo, Color mortero) {
        g2.setColor(mortero);
        g2.fillRect(x, y, w, h);
        int altoLadrillo = 16, anchoLadrillo = 46, junta = 3;
        g2.setColor(ladrillo);
        int fila = 0;
        for (int fy = y; fy < y + h; fy += altoLadrillo + junta) {
            int offset = (fila % 2 == 0) ? 0 : -anchoLadrillo / 2;
            for (int fx = x + offset; fx < x + w; fx += anchoLadrillo + junta) {
                g2.fillRoundRect(fx, fy, anchoLadrillo, altoLadrillo, 2, 2);
            }
            fila++;
        }
    }

    private static void pintarPisoBaldosas(Graphics2D g2, int x, int y, int w, int h, Color clara, Color oscura) {
        int lado = 34;
        for (int fy = y; fy < y + h; fy += lado) {
            for (int fx = x; fx < x + w; fx += lado) {
                boolean par = ((fx / lado) + (fy / lado)) % 2 == 0;
                g2.setColor(par ? clara : oscura);
                g2.fillRect(fx, fy, lado, lado);
            }
        }
        g2.setColor(new Color(0, 0, 0, 15));
        for (int fx = x; fx < x + w; fx += lado) g2.drawLine(fx, y, fx, y + h);
        for (int fy = y; fy < y + h; fy += lado) g2.drawLine(x, fy, x + w, fy);
    }

    private static void dibujarTextoCentrado(Graphics2D g2, String texto, int x, int y, int w, int h) {
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(texto);
        int tx = x + (w - tw) / 2;
        int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(texto, tx, ty);
    }

    // =========================================================
    // COMPONENTES VISUALES "TIENDA FÍSICA"
    // =========================================================

    /** Fondo de un pasillo: pared de ladrillo arriba y piso de baldosas abajo, siempre visibles. */
    private static class PasilloPanel extends JPanel {
        PasilloPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int alturaPared = Math.min(60, Math.max(30, h / 6));
            pintarLadrillos(g2, 0, 0, w, alturaPared, LADRILLO, MORTERO);
            pintarPisoBaldosas(g2, 0, alturaPared, w, h - alturaPared, PISO_TIENDA, BALDOSA_OSCURA);
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRect(0, alturaPared, w, 3);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Tabla/plancha de estante de madera que "sostiene" cada tarjeta de la lista. */
    private static class EstantePlank extends JComponent {
        EstantePlank() {
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
            setPreferredSize(new Dimension(10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRect(4, 0, w - 8, 3);
            GradientPaint madera = new GradientPaint(0, 3, MADERA_MEDIA, 0, 9, MADERA_OSCURA);
            g2.setPaint(madera);
            g2.fillRoundRect(0, 3, w, 6, 4, 4);
            g2.setColor(MADERA_VETA);
            for (int x = 10; x < w - 10; x += 34) {
                g2.drawLine(x, 4, x + 14, 8);
            }
            g2.dispose();
        }
    }

    /** Tarjeta tipo "producto en papel de estraza" con esquinas redondeadas y sombra suave. */
    private static class TarjetaPanel extends JPanel {
        TarjetaPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(12, 14, 12, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fill(new RoundRectangle2D.Double(3, 4, getWidth() - 6, getHeight() - 6, 16, 16));
            g2.setColor(TARJETA);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, 16, 16));
            g2.setColor(BORDE);
            g2.setStroke(new BasicStroke(1.4f));
            g2.draw(new RoundRectangle2D.Double(0.7, 0.7, getWidth() - 7.4, getHeight() - 7.4, 16, 16));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Etiqueta de precio de papel colgando de un hilo, como en una tienda real. */
    private static class EtiquetaPrecio extends JComponent {
        private final String texto;

        EtiquetaPrecio(String texto) {
            this.texto = texto;
            setOpaque(false);
            setPreferredSize(new Dimension(84, 60));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            g2.setColor(HILO);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(cx, 2, cx, 16);

            int tagW = 68, tagH = 34;
            int x0 = cx - tagW / 2, y0 = 16;
            GeneralPath tag = new GeneralPath();
            tag.moveTo(x0 + 10, y0);
            tag.lineTo(x0 + tagW, y0);
            tag.lineTo(x0 + tagW, y0 + tagH);
            tag.lineTo(x0 + 10, y0 + tagH);
            tag.lineTo(x0, y0 + tagH / 2.0);
            tag.closePath();

            g2.setColor(new Color(0, 0, 0, 30));
            g2.translate(1.5, 2);
            g2.fill(tag);
            g2.translate(-1.5, -2);

            g2.setColor(CARTON);
            g2.fill(tag);
            g2.setColor(MADERA_CLARA);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(tag);

            g2.setColor(TINTA);
            g2.fill(new Ellipse2D.Double(x0 + 4, y0 + tagH / 2.0 - 2, 4, 4));

            g2.setColor(TINTA);
            g2.setFont(new Font("Serif", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(texto);
            g2.drawString(texto, x0 + 10 + (tagW - 10 - tw) / 2, y0 + tagH / 2 + fm.getAscent() / 2 - 2);

            g2.dispose();
        }
    }

    /** Cartel de madera colgante para la cabecera, con toldo, cadenas y marco de latón. */
    private static class CarteloTiendaPanel extends JPanel {
        CarteloTiendaPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            int franjas = 14;
            int anchoFranja = Math.max(1, w / franjas);
            for (int i = 0; i <= franjas; i++) {
                g2.setColor(i % 2 == 0 ? TOLDO_A : TOLDO_B);
                g2.fillRect(i * anchoFranja, 0, anchoFranja, 8);
            }

            g2.setColor(LATON);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(60, 8, 60, 22);
            g2.drawLine(w - 60, 8, w - 60, 22);
            g2.fillOval(56, 18, 8, 8);
            g2.fillOval(w - 64, 18, 8, 8);

            GradientPaint madera = new GradientPaint(0, 22, MADERA_MEDIA, 0, h, MADERA_OSCURA);
            g2.setPaint(madera);
            g2.fillRoundRect(0, 22, w, h - 22, 10, 10);

            g2.setColor(new Color(0, 0, 0, 40));
            for (int x = 20; x < w; x += 90) {
                g2.drawLine(x, 30, x + 30, h - 10);
            }

            g2.setColor(LATON);
            g2.setStroke(new BasicStroke(2.2f));
            g2.drawRoundRect(4, 26, w - 8, h - 30, 8, 8);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Panel con apariencia de caja registradora: pantalla LED con el dinero disponible. */
    private static class CajaRegistradoraPanel extends JPanel {
        CajaRegistradoraPanel(JLabel lblDinero) {
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(4, 4, 4, 4));

            JPanel pantalla = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0x0D0D0D));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(new Color(0x2E8B45));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            pantalla.setOpaque(false);
            pantalla.setBorder(new EmptyBorder(8, 12, 8, 12));

            JLabel rotulo = new JLabel("💵 CAJA", SwingConstants.CENTER);
            rotulo.setFont(new Font("SansSerif", Font.BOLD, 10));
            rotulo.setForeground(new Color(0x8A8A8A));

            pantalla.add(rotulo, BorderLayout.NORTH);
            pantalla.add(lblDinero, BorderLayout.CENTER);

            add(pantalla, BorderLayout.CENTER);
        }
    }

    /** Botón con relieve de madera barnizada en lugar de un botón plano. */
    private static class BotonMadera extends JButton {
        private final Color base;

        BotonMadera(String texto, Color base) {
            super(texto);
            this.base = base;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean presionado = getModel().isPressed();
            boolean sobre = getModel().isRollover();

            Color claro = base.brighter();
            Color oscuro = base.darker();
            GradientPaint gp = presionado
                    ? new GradientPaint(0, 0, oscuro, 0, h, base)
                    : new GradientPaint(0, 0, sobre ? claro : base, 0, h, oscuro);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(1, 1, w - 3, h / 2, 10, 10);

            g2.setColor(LATON);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Fachada de entrada: ladrillo, piso, letrero colgante, puerta de madera y tapete. */
    private static class EntradaGraficoPanel extends JComponent {
        EntradaGraficoPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            pintarLadrillos(g2, 0, 0, w, h, LADRILLO, MORTERO);
            int alturaPiso = Math.max(70, h / 5);
            pintarPisoBaldosas(g2, 0, h - alturaPiso, w, alturaPiso, PISO_TIENDA, BALDOSA_OSCURA);
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRect(0, h - alturaPiso, w, 4);

            int letreroW = Math.min(440, w - 80), letreroH = 68;
            int letreroX = w / 2 - letreroW / 2, letreroY = 26;
            g2.setColor(LATON);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(letreroX + 30, 0, letreroX + 30, letreroY);
            g2.drawLine(letreroX + letreroW - 30, 0, letreroX + letreroW - 30, letreroY);
            g2.fillOval(letreroX + 26, letreroY - 6, 8, 8);
            g2.fillOval(letreroX + letreroW - 34, letreroY - 6, 8, 8);

            GradientPaint maderaSign = new GradientPaint(0, letreroY, MADERA_MEDIA, 0, letreroY + letreroH, MADERA_OSCURA);
            g2.setPaint(maderaSign);
            g2.fillRoundRect(letreroX, letreroY, letreroW, letreroH, 14, 14);
            g2.setColor(LATON);
            g2.drawRoundRect(letreroX, letreroY, letreroW, letreroH, 14, 14);
            g2.setColor(new Color(0xF5E6C8));
            g2.setFont(new Font("Serif", Font.BOLD, 24));
            dibujarTextoCentrado(g2, "🐾 TIENDA DE MASCOTAS 🐾", letreroX, letreroY, letreroW, letreroH);

            int doorW = 170;
            int doorH = Math.max(150, Math.min(260, h - alturaPiso - letreroY - letreroH - 30));
            int doorX = w / 2 - doorW / 2;
            int doorY = h - alturaPiso - doorH + 30;
            GradientPaint maderaDoor = new GradientPaint(doorX, doorY, MADERA_CLARA, doorX + doorW, doorY + doorH, MADERA_OSCURA);
            g2.setPaint(maderaDoor);
            g2.fillRoundRect(doorX, doorY, doorW, doorH, 10, 30);
            g2.setColor(MADERA_OSCURA);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(doorX, doorY, doorW, doorH, 10, 30);

            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(doorX + 16, doorY + 18, doorW - 32, doorH * 2 / 5, 8, 8);
            g2.drawRoundRect(doorX + 16, doorY + doorH / 2, doorW - 32, doorH * 2 / 5 - 10, 8, 8);

            g2.setColor(new Color(0xBEE3F8));
            g2.fillRoundRect(doorX + doorW / 2 - 22, doorY + 30, 44, 34, 6, 6);
            g2.setColor(MADERA_OSCURA);
            g2.drawRoundRect(doorX + doorW / 2 - 22, doorY + 30, 44, 34, 6, 6);
            g2.drawLine(doorX + doorW / 2, doorY + 30, doorX + doorW / 2, doorY + 64);
            g2.drawLine(doorX + doorW / 2 - 22, doorY + 47, doorX + doorW / 2 + 22, doorY + 47);

            g2.setColor(LATON);
            g2.fillOval(doorX + doorW - 28, doorY + doorH / 2 - 6, 12, 12);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
            g2.drawString("🔔", doorX + doorW - 8, doorY - 6);

            int matW = Math.min(220, w - 60), matH = 42;
            int matX = w / 2 - matW / 2, matY = h - alturaPiso / 2 - matH / 2 + 6;
            g2.setColor(new Color(0x7A3B2E));
            g2.fillRoundRect(matX, matY, matW, matH, 10, 10);
            g2.setColor(new Color(0x5C2A20));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(matX, matY, matW, matH, 10, 10);
            for (int fx = matX + 6; fx < matX + matW - 4; fx += 10) {
                g2.drawLine(fx, matY + matH, fx, matY + matH + 5);
                g2.drawLine(fx, matY - 5, fx, matY);
            }
            g2.setColor(new Color(0xF1E2BE));
            g2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 16));
            dibujarTextoCentrado(g2, "¡BIENVENIDO/A!", matX, matY, matW, matH);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Ticket de papel con bordes dentados, como un recibo impreso de verdad. */
    private static class ReciboPanel extends JPanel {
        private final List<String> lineas;

        ReciboPanel(List<String> lineas) {
            this.lineas = lineas;
            setOpaque(false);
            int alto = 44 + lineas.size() * 20 + 20;
            setPreferredSize(new Dimension(320, alto));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int zig = 8;

            GeneralPath forma = new GeneralPath();
            forma.moveTo(0, zig);
            boolean arriba = true;
            for (int x = 0; x <= w; x += zig) {
                forma.lineTo(x, arriba ? 0 : zig);
                arriba = !arriba;
            }
            arriba = true;
            for (int x = w; x >= 0; x -= zig) {
                forma.lineTo(x, arriba ? h : h - zig);
                arriba = !arriba;
            }
            forma.closePath();

            g2.setColor(new Color(0, 0, 0, 45));
            g2.translate(3, 4);
            g2.fill(forma);
            g2.translate(-3, -4);

            g2.setColor(PAPEL);
            g2.fill(forma);

            g2.setColor(TINTA);
            g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            int y = zig + 20;
            for (String linea : lineas) {
                g2.drawString(linea, 18, y);
                y += fm.getHeight() + 3;
            }
            g2.dispose();
        }
    }

    /** UI de pestañas estilo "letreros de pasillo" colgando de la tabla superior. */
    private static class PasilloTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabAreaInsets = new Insets(6, 8, 0, 8);
            selectedTabPadInsets = new Insets(2, 2, 2, 2);
            tabInsets = new Insets(10, 16, 10, 16);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color color = isSelected ? MADERA_CLARA : MADERA_MEDIA;
            GradientPaint gp = new GradientPaint(x, y, color.brighter(), x, y + h, color.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x + 2, y + 2, w - 4, h + 8, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(LATON);
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawRoundRect(x + 2, y + 2, w - 4, h + 8, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // Sin borde extra: el contenido ya tiene su propio fondo de pasillo.
        }

        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 6;
        }
    }

    /** Renderer genérico para mostrar emojis + texto legible en un JComboBox. */
    private static class ComboEmojiRenderer<T> extends DefaultListCellRenderer {
        private final java.util.function.Function<T, String> formateador;

        ComboEmojiRenderer(java.util.function.Function<T, String> formateador) {
            this.formateador = formateador;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
            String texto = value == null ? "" : formateador.apply((T) value);
            return super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
        }
    }

    private class SpriteMascota extends JPanel implements Runnable {
        private final Mascota mascota;
        private int x, y;
        private int velocidadX, velocidadY;
        private int direccion = 1;
        private boolean animando = true;
        private final java.util.Random random = new java.util.Random();
        private int frameAnimacion = 0;
        private int contadorMovimiento = 0;
        private final int TAMANO_SPRITE = 48;
        private final int VELOCIDAD_BASE = 1;
        private final int ANCHO_PADRE;
        private final int ALTO_PADRE;
        private final Color colorCuerpo;
        private final Runnable onActualizar;

        private SpriteMascota(Mascota mascota, int anchoPadre, int altoPadre, Runnable onActualizar) {
            this.mascota = mascota;
            this.ANCHO_PADRE = anchoPadre;
            this.ALTO_PADRE = altoPadre;
            this.onActualizar = onActualizar;

            this.x = 20 + random.nextInt(Math.max(1, anchoPadre - TAMANO_SPRITE - 40));
            this.y = 20 + random.nextInt(Math.max(1, altoPadre - TAMANO_SPRITE - 40));
            this.velocidadX = VELOCIDAD_BASE + random.nextInt(2);
            this.velocidadY = VELOCIDAD_BASE + random.nextInt(2);
            this.colorCuerpo = obtenerColorMascota(mascota);

            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(80, 64));
            setBounds(x, y, 80, 64);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mostrarInfoMascota(mascota);
                }
            });

            new Thread(this).start();
        }

        private Color obtenerColorMascota(Mascota mascota) {
            String nombre = mascota.getClass().getSimpleName().toLowerCase();
            if (nombre.contains("perro")) return new Color(0x8B6B4D);
            if (nombre.contains("gato")) return new Color(0x6A6A6A);
            if (nombre.contains("pez")) return new Color(0xFF6B35);
            if (nombre.contains("hamster")) return new Color(0xD4A373);
            if (nombre.contains("pajaro")) return new Color(0x4A90D9);
            return new Color(0x7B8D8E);
        }

        @Override
        public void run() {
            while (animando) {
                mover();
                repaint();
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        private void mover() {
            x += velocidadX;
            y += velocidadY;
            contadorMovimiento++;

            if (contadorMovimiento % 6 == 0) {
                frameAnimacion = (frameAnimacion + 1) % 8;
            }

            if (x <= 0 || x >= ANCHO_PADRE - TAMANO_SPRITE) {
                velocidadX = -velocidadX;
                direccion = -direccion;
                x = Math.max(0, Math.min(x, ANCHO_PADRE - TAMANO_SPRITE));
            }

            if (y <= 0 || y >= ALTO_PADRE - TAMANO_SPRITE) {
                velocidadY = -velocidadY;
                y = Math.max(0, Math.min(y, ALTO_PADRE - TAMANO_SPRITE));
            }

            if (random.nextInt(100) < 2) {
                velocidadX = (random.nextInt(3) + 1) * (random.nextBoolean() ? 1 : -1);
                velocidadY = (random.nextInt(3) + 1) * (random.nextBoolean() ? 1 : -1);
            }

            setBounds(x, y, 86, 84);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillOval(4, h - 8, w - 8, 6);
            dibujarMascota(g2, w, h);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            String nombre = mascota.getNombre();
            int nombreW = fm.stringWidth(nombre);
            g2.drawString(nombre, (w - nombreW) / 2, -2);

            g2.dispose();
        }

        private void dibujarMascota(Graphics2D g2, int w, int h) {
            String tipo = mascota.getClass().getSimpleName().toLowerCase();
            if (tipo.contains("perro")) {
                dibujarPerro(g2, w, h);
            } else if (tipo.contains("gato")) {
                dibujarGato(g2, w, h);
            } else if (tipo.contains("pez")) {
                dibujarPez(g2, w, h);
            } else if (tipo.contains("hamster")) {
                dibujarHamster(g2, w, h);
            } else if (tipo.contains("pajaro")) {
                dibujarPajaro(g2, w, h);
            } else {
                dibujarMascotaGenerica(g2, w, h);
            }
        }

        private void dibujarPerro(Graphics2D g2, int w, int h) {
            // Pixel art del perro en escala
            int px = 2; // Tamaño de cada "píxel"
            
            // Colores
            Color marrón = new Color(0x8B6B4D);
            Color crema = new Color(0xEED9B6);
            Color naranja = new Color(0xD2691E);
            Color negro = Color.BLACK;
            
            g2.setColor(marrón);
            // Cuerpo principal (en dos partes para simular perspectiva)
            g2.fillRect(w/4, h/2, w/2, h/3);
            
            // Cabeza
            g2.fillRect(w/3, h/4, w/3, h/3);
            
            // Orejas
            g2.fillRect(w/4 + 2, h/4, px*3, px*2);
            g2.fillRect(w*3/4 - 6, h/4, px*3, px*2);
            
            // Hocico/Morro (crema)
            g2.setColor(crema);
            g2.fillRect(w/3 + 4, h/3 + 4, w/6 - 2, h/6);
            
            // Nariz/Boca (naranja/rojo)
            g2.setColor(naranja);
            g2.fillRect(w/3 + 8, h/3 + 8, px*2, px);
            g2.fillRect(w/3 + 6, h/3 + 10, px*4, px);
            
            // Ojos
            g2.setColor(negro);
            g2.fillRect(w/3 + 6, h/4 + 4, px, px);
            g2.fillRect(w/2 + 2, h/4 + 4, px, px);
            
            // Pupilas blancas
            g2.setColor(Color.WHITE);
            g2.fillRect(w/3 + 6, h/4 + 4, 1, 1);
            g2.fillRect(w/2 + 2, h/4 + 4, 1, 1);
            
            // Patas
            g2.setColor(marrón);
            g2.fillRect(w/4 + 2, h*2/3, px*2, h/4);
            g2.fillRect(w/3 + 4, h*2/3, px*2, h/4);
            g2.fillRect(w/2 + 2, h*2/3, px*2, h/4);
            g2.fillRect(w*2/3 + 2, h*2/3, px*2, h/4);
            
            // Cola animada
            g2.setColor(marrón);
            int colaOffset = (int) (Math.sin(frameAnimacion * 0.5) * 4);
            g2.fillRect(w*3/4 + 2 + colaOffset, h/3, px*2, h/4);
        }

        private void dibujarGato(Graphics2D g2, int w, int h) {
            g2.setColor(colorCuerpo);
            g2.fillOval(w / 3, h / 3, w / 2, h / 2);
            g2.fillOval(w / 4, h / 6, w / 3, h / 3);

            int[] xPoints = {w / 4, w / 4 + 4, w / 4 + 8};
            int[] yPoints = {h / 6, h / 6 - 8, h / 6};
            g2.fillPolygon(xPoints, yPoints, 3);

            int[] xPoints2 = {w / 2 - 4, w / 2 + 4, w / 2 + 8};
            g2.fillPolygon(xPoints2, yPoints, 3);

            g2.setColor(new Color(0x4CAF50));
            g2.fillOval(w / 4 + 4, h / 6 + 4, 8, 8);
            g2.fillOval(w / 4 + 16, h / 6 + 4, 8, 8);
            g2.setColor(Color.BLACK);
            g2.fillOval(w / 4 + 7, h / 6 + 7, 3, 3);
            g2.fillOval(w / 4 + 19, h / 6 + 7, 3, 3);

            g2.setColor(Color.BLACK);
            g2.drawLine(w / 4 + 2, h / 4 + 6, w / 4 - 6, h / 4 + 2);
            g2.drawLine(w / 4 + 2, h / 4 + 10, w / 4 - 6, h / 4 + 10);
            g2.drawLine(w / 2 - 2, h / 4 + 6, w / 2 + 6, h / 4 + 2);
            g2.drawLine(w / 2 - 2, h / 4 + 10, w / 2 + 6, h / 4 + 10);

            g2.setStroke(new BasicStroke(3));
            int colaX = w * 3 / 4 + (int) (Math.sin(frameAnimacion * 0.7) * 6);
            g2.drawLine(w * 3 / 4, h / 2, colaX, h / 4 - 4);
        }

        private void dibujarPez(Graphics2D g2, int w, int h) {
            g2.setColor(colorCuerpo);
            g2.fillOval(w / 4, h / 3, w / 2, h / 3);

            int[] tailX = {w / 4, w / 4 - 8, w / 4 - 8, w / 4};
            int[] tailY = {h / 2 - 4, h / 2 - 12, h / 2 + 12, h / 2 + 4};
            g2.fillPolygon(tailX, tailY, 4);

            g2.setColor(Color.WHITE);
            g2.fillOval(w / 2 + 4, h / 3 + 8, 8, 8);
            g2.setColor(Color.BLACK);
            g2.fillOval(w / 2 + 8, h / 3 + 10, 4, 4);

            g2.fillOval(w / 3 + 4, h / 4, 12, 8);

            g2.setColor(new Color(255, 255, 255, 50));
            for (int i = 0; i < 3; i++) {
                g2.drawArc(w / 3 + i * 10, h / 3 + 4, 8, 6, 0, 180);
            }
        }

        private void dibujarHamster(Graphics2D g2, int w, int h) {
            g2.setColor(colorCuerpo);
            g2.fillOval(w / 4, h / 4, w / 2, h / 2);
            g2.fillOval(w / 3, h / 6, w / 3, h / 3);

            g2.fillOval(w / 3 - 2, h / 6 - 4, 8, 8);
            g2.fillOval(w / 2 - 2, h / 6 - 4, 8, 8);

            g2.setColor(Color.BLACK);
            g2.fillOval(w / 3 + 6, h / 6 + 8, 5, 5);
            g2.fillOval(w / 3 + 16, h / 6 + 8, 5, 5);

            g2.setColor(new Color(0xFFB6C1));
            g2.fillOval(w / 3 + 8, h / 4 + 4, 10, 6);

            g2.setColor(new Color(0xFF6B6B));
            g2.fillOval(w / 3 + 12, h / 4 + 6, 4, 3);

            g2.fillOval(w / 3 + 2, h / 2 + 4, 8, 6);
            g2.fillOval(w / 2 - 4, h / 2 + 4, 8, 6);
        }

        private void dibujarPajaro(Graphics2D g2, int w, int h) {
            g2.setColor(colorCuerpo);
            g2.fillOval(w / 3, h / 3, w / 3, h / 3);
            g2.fillOval(w / 2 - 4, h / 4, w / 4, h / 4);

            g2.setColor(new Color(0xFF6B35));
            int[] picoX = {w / 2 + 8, w / 2 + 18, w / 2 + 8};
            int[] picoY = {h / 4 + 4, h / 4 + 8, h / 4 + 12};
            g2.fillPolygon(picoX, picoY, 3);

            g2.setColor(Color.BLACK);
            g2.fillOval(w / 2 + 2, h / 4 + 4, 4, 4);

            g2.setColor(colorCuerpo.darker());
            int alaY = h / 3 + (int) (Math.sin(frameAnimacion * 0.8) * 6);
            int[] alaL = {w / 3, w / 6, w / 3 + 4};
            int[] alaLY = {h / 3 + 6, alaY, h / 3 + 12};
            g2.fillPolygon(alaL, alaLY, 3);

            int[] alaR = {w / 2 + 4, w * 2 / 3 + 4, w / 2 + 8};
            int[] alaRY = {h / 3 + 6, alaY + 2, h / 3 + 12};
            g2.fillPolygon(alaR, alaRY, 3);

            g2.setColor(colorCuerpo.darker());
            int[] colaX = {w / 4, w / 8, w / 4 + 4};
            int[] colaY = {h / 2 - 2, h / 2 + 4, h / 2 + 6};
            g2.fillPolygon(colaX, colaY, 3);
        }

        private void dibujarMascotaGenerica(Graphics2D g2, int w, int h) {
            g2.setColor(colorCuerpo);
            g2.fillOval(4, 4, w - 8, h - 8);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
            g2.drawString("🐾", w / 2 - 10, h / 2 + 6);
        }

        public void detener() {
            animando = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaGUI().setVisible(true));
    }
}
