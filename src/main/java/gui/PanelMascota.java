package gui;

import model.mascotas.Mascota;
import model.mascotas.TipoMascota;
import model.enfermedades.Enfermedad;
import model.habitat.Habitat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class PanelMascota extends JPanel {
    private final Mascota mascota;
    private final JLabel imagenLabel;
    private final JLabel nombreLabel;
    private final JLabel tipoLabel;
    private final JProgressBar saludBar;
    private final JProgressBar felicidadBar;
    private final JProgressBar comidaBar; // CORRECCIÓN: Nueva barra
    private final JLabel enfermedadLabel;
    private final JLabel habitatLabel;

    public PanelMascota(Mascota mascota) {
        this.mascota = mascota;
        setLayout(new BorderLayout(10, 5));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x78A083), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        setPreferredSize(new Dimension(280, 180)); // CORRECCIÓN: Altura expandida ligeramente

        // Panel izquierdo - Imagen
        JPanel imagenPanel = new JPanel(new BorderLayout());
        imagenPanel.setBackground(Color.WHITE);
        imagenPanel.setPreferredSize(new Dimension(80, 80));

        ImageIcon icon = cargarSprite(mascota.getTipoMascota().name().toLowerCase());
        imagenLabel = new JLabel(icon);
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenPanel.add(imagenLabel, BorderLayout.CENTER);
        add(imagenPanel, BorderLayout.WEST);

        // Panel derecho - Información
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 5);

        Font infoFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

        nombreLabel = new JLabel("🐾 " + mascota.getNombre());
        nombreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        infoPanel.add(nombreLabel, gbc);
        gbc.gridy++;

        tipoLabel = new JLabel("Tipo: " + mascota.getTipoMascota());
        tipoLabel.setFont(infoFont);
        infoPanel.add(tipoLabel, gbc);
        gbc.gridy++;

        // Barra de salud
        JPanel saludPanel = new JPanel(new BorderLayout(5, 0));
        saludPanel.setBackground(Color.WHITE);
        JLabel saludText = new JLabel("❤");
        saludText.setFont(infoFont);
        saludPanel.add(saludText, BorderLayout.WEST);
        saludBar = new JProgressBar(0, 100);
        saludBar.setPreferredSize(new Dimension(100, 14));
        saludBar.setStringPainted(true);
        saludBar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        saludBar.setForeground(new Color(0x4CAF50));
        saludPanel.add(saludBar, BorderLayout.CENTER);
        infoPanel.add(saludPanel, gbc);
        gbc.gridy++;

        // Barra de felicidad
        JPanel felicidadPanel = new JPanel(new BorderLayout(5, 0));
        felicidadPanel.setBackground(Color.WHITE);
        JLabel felicidadText = new JLabel("😊");
        felicidadText.setFont(infoFont);
        felicidadPanel.add(felicidadText, BorderLayout.WEST);
        felicidadBar = new JProgressBar(0, 100);
        felicidadBar.setPreferredSize(new Dimension(100, 14));
        felicidadBar.setStringPainted(true);
        felicidadBar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        felicidadBar.setForeground(new Color(0xFFA726));
        felicidadPanel.add(felicidadBar, BorderLayout.CENTER);
        infoPanel.add(felicidadPanel, gbc);
        gbc.gridy++;

        // CORRECCIÓN: Barra de comida añadida visualmente
        JPanel comidaPanel = new JPanel(new BorderLayout(5, 0));
        comidaPanel.setBackground(Color.WHITE);
        JLabel comidaText = new JLabel("🍖");
        comidaText.setFont(infoFont);
        comidaPanel.add(comidaText, BorderLayout.WEST);
        comidaBar = new JProgressBar(0, 100);
        comidaBar.setPreferredSize(new Dimension(100, 14));
        comidaBar.setStringPainted(true);
        comidaBar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        comidaPanel.add(comidaBar, BorderLayout.CENTER);
        infoPanel.add(comidaPanel, gbc);
        gbc.gridy++;

        enfermedadLabel = new JLabel("Enfermedad: Ninguna");
        enfermedadLabel.setFont(infoFont);
        infoPanel.add(enfermedadLabel, gbc);
        gbc.gridy++;

        habitatLabel = new JLabel("Habitat: Sin asignar");
        habitatLabel.setFont(infoFont);
        habitatLabel.setForeground(new Color(0x1565C0));
        infoPanel.add(habitatLabel, gbc);

        add(infoPanel, BorderLayout.CENTER);

        actualizarInformacion();
    }

    private ImageIcon cargarSprite(String tipo) {
        try {
            String ruta = "/sprites/" + tipo + ".png";
            URL url = getClass().getResource(ruta);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image imagen = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                return new ImageIcon(imagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar sprite para: " + tipo);
        }
        return crearSpriteGenerado(tipo);
    }

    private ImageIcon crearSpriteGenerado(String tipo) {
        BufferedImage image = new BufferedImage(70, 70, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colorBase = getColorForTipo(tipo);
        Color colorOscuro = colorBase.darker();
        Color colorClaro = colorBase.brighter();

        // Cuerpo principal
        g2.setColor(colorBase);
        g2.fillOval(15, 20, 40, 40);

        // Cabeza
        g2.fillOval(25, 10, 30, 30);

        // Ojos
        g2.setColor(Color.WHITE);
        g2.fillOval(28, 18, 8, 8);
        g2.fillOval(42, 18, 8, 8);

        g2.setColor(Color.BLACK);
        g2.fillOval(30, 20, 4, 4);
        g2.fillOval(44, 20, 4, 4);

        switch (tipo.toLowerCase()) {
            case "perro":
                g2.setColor(colorOscuro);
                g2.fillOval(18, 5, 12, 18);
                g2.fillOval(46, 5, 12, 18);
                g2.setColor(colorClaro);
                g2.fillOval(33, 28, 14, 10);
                g2.setColor(Color.BLACK);
                g2.fillOval(36, 32, 4, 4);
                g2.fillOval(42, 32, 4, 4);
                g2.setColor(new Color(0xE57373));
                g2.fillOval(38, 36, 6, 8);
                break;
            case "gato":
                int[] xPoints = {20, 25, 15};
                int[] yPoints = {15, 3, 3};
                g2.setColor(colorOscuro);
                g2.fillPolygon(xPoints, yPoints, 3);
                int[] xPoints2 = {45, 50, 40};
                g2.fillPolygon(xPoints2, yPoints, 3);
                g2.setColor(Color.BLACK);
                g2.drawLine(20, 32, 5, 28);
                g2.drawLine(20, 35, 5, 38);
                g2.drawLine(50, 32, 65, 28);
                g2.drawLine(50, 35, 65, 38);
                g2.setColor(new Color(0xE57373));
                g2.fillOval(38, 28, 6, 5);
                break;
            case "pez":
                g2.setColor(colorOscuro);
                int[] xPez = {45, 60, 50};
                int[] yPez = {25, 15, 35};
                g2.fillPolygon(xPez, yPez, 3);
                int[] xPez2 = {50, 65, 55};
                int[] yPez2 = {30, 35, 45};
                g2.fillPolygon(xPez2, yPez2, 3);
                g2.setColor(colorClaro);
                g2.drawOval(28, 30, 8, 8);
                g2.drawOval(38, 28, 8, 8);
                g2.drawOval(35, 38, 8, 8);
                g2.setColor(Color.WHITE);
                g2.fillOval(30, 20, 6, 6);
                g2.setColor(Color.BLACK);
                g2.fillOval(32, 22, 3, 3);
                g2.setColor(Color.BLACK);
                g2.drawArc(28, 32, 10, 5, 0, 180);
                break;
            case "hamster":
                g2.setColor(colorClaro);
                g2.fillOval(18, 24, 14, 12);
                g2.fillOval(44, 24, 14, 12);
                g2.setColor(colorOscuro);
                g2.fillOval(22, 8, 10, 10);
                g2.fillOval(44, 8, 10, 10);
                g2.setColor(new Color(0xFFB74D));
                g2.fillOval(24, 10, 6, 6);
                g2.fillOval(46, 10, 6, 6);
                g2.setColor(new Color(0xE57373));
                g2.fillOval(38, 30, 4, 4);
                g2.setColor(Color.WHITE);
                g2.fillRect(36, 34, 3, 4);
                g2.fillRect(40, 34, 3, 4);
                break;
            case "pajaro":
                g2.setColor(new Color(0xFF8F00));
                int[] xPico = {48, 60, 48};
                int[] yPico = {24, 28, 32};
                g2.fillPolygon(xPico, yPico, 3);
                g2.setColor(colorClaro);
                int[] xAla = {10, 25, 15};
                int[] yAla = {30, 35, 50};
                g2.fillPolygon(xAla, yAla, 3);
                int[] xAla2 = {50, 65, 55};
                int[] yAla2 = {30, 35, 50};
                g2.fillPolygon(xAla2, yAla2, 3);
                g2.setColor(colorOscuro);
                int[] xCola = {40, 45, 35, 40};
                int[] yCola = {50, 60, 60, 50};
                g2.fillPolygon(xCola, yCola, 4);
                g2.setColor(colorClaro);
                int[] xCresta = {35, 40, 45};
                int[] yCresta = {10, 3, 10};
                g2.fillPolygon(xCresta, yCresta, 3);
                break;
        }

        g2.dispose();
        return new ImageIcon(image);
    }

    private Color getColorForTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "perro" -> new Color(0x8D6E63);
            case "gato" -> new Color(0xFF8A65);
            case "pez" -> new Color(0x4FC3F7);
            case "hamster" -> new Color(0xFFD54F);
            case "pajaro" -> new Color(0x81C784);
            default -> new Color(0x9E9E9E);
        };
    }

    public static ImageIcon cargarSpriteGrande(String tipo) {
        try {
            String ruta = "/sprites/" + tipo + "_grande.png";
            URL url = PanelMascota.class.getResource(ruta);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image imagen = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                return new ImageIcon(imagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar sprite grande para: " + tipo);
        }
        return crearSpriteGrandeGenerado(tipo);
    }

    private static ImageIcon crearSpriteGrandeGenerado(String tipo) {
        BufferedImage image = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colorBase = getColorForTipoStatic(tipo);
        Color colorOscuro = colorBase.darker();
        Color colorClaro = colorBase.brighter();

        g2.setColor(colorBase);
        g2.fillOval(25, 35, 70, 70);
        g2.fillOval(40, 15, 50, 50);

        g2.setColor(Color.WHITE);
        g2.fillOval(45, 28, 14, 14);
        g2.fillOval(70, 28, 14, 14);

        g2.setColor(Color.BLACK);
        g2.fillOval(50, 32, 6, 6);
        g2.fillOval(75, 32, 6, 6);

        switch (tipo.toLowerCase()) {
            case "perro":
                g2.setColor(colorOscuro);
                g2.fillOval(30, 8, 20, 30);
                g2.fillOval(75, 8, 20, 30);
                g2.setColor(colorClaro);
                g2.fillOval(55, 45, 22, 16);
                g2.setColor(Color.BLACK);
                g2.fillOval(60, 50, 6, 6);
                g2.fillOval(72, 50, 6, 6);
                g2.setColor(new Color(0xE57373));
                g2.fillOval(64, 58, 10, 12);
                break;
            case "gato":
                int[] xGato = {35, 42, 25};
                int[] yGato = {25, 5, 5};
                g2.setColor(colorOscuro);
                g2.fillPolygon(xGato, yGato, 3);
                int[] xGato2 = {75, 82, 65};
                g2.fillPolygon(xGato2, yGato, 3);
                g2.setColor(Color.BLACK);
                g2.drawLine(35, 50, 10, 45);
                g2.drawLine(35, 55, 10, 60);
                g2.drawLine(85, 50, 110, 45);
                g2.drawLine(85, 55, 110, 60);
                g2.setColor(new Color(0xE57373));
                g2.fillOval(62, 45, 10, 8);
                break;
            case "pez":
                int[] xPez = {75, 100, 85};
                int[] yPez = {42, 25, 55};
                g2.setColor(colorOscuro);
                g2.fillPolygon(xPez, yPez, 3);
                int[] xPez2 = {85, 110, 95};
                int[] yPez2 = {50, 60, 75};
                g2.fillPolygon(xPez2, yPez2, 3);
                g2.setColor(colorClaro);
                g2.drawOval(45, 48, 14, 14);
                g2.drawOval(62, 45, 14, 14);
                g2.drawOval(55, 62, 14, 14);
                g2.setColor(Color.WHITE);
                g2.fillOval(50, 32, 10, 10);
                g2.setColor(Color.BLACK);
                g2.fillOval(54, 36, 5, 5);
                g2.setColor(Color.BLACK);
                g2.drawArc(45, 52, 18, 8, 0, 180);
                break;
            case "hamster":
                g2.setColor(colorClaro);
                g2.fillOval(30, 38, 22, 18);
                g2.fillOval(72, 38, 22, 18);
                g2.setColor(colorOscuro);
                g2.fillOval(38, 12, 16, 16);
                g2.fillOval(75, 12, 16, 16);
                g2.setColor(new Color(0xFFB74D));
                g2.fillOval(42, 16, 10, 10);
                g2.fillOval(79, 16, 10, 10);
                g2.setColor(new Color(0xE57373));
                g2.fillOval(62, 48, 8, 6);
                g2.setColor(Color.WHITE);
                g2.fillRect(58, 55, 5, 6);
                g2.fillRect(68, 55, 5, 6);
                break;
            case "pajaro":
                g2.setColor(new Color(0xFF8F00));
                int[] xPajaro = {78, 100, 78};
                int[] yPajaro = {38, 45, 52};
                g2.fillPolygon(xPajaro, yPajaro, 3);
                g2.setColor(colorClaro);
                int[] xAlaP = {15, 40, 25};
                int[] yAlaP = {48, 55, 80};
                g2.fillPolygon(xAlaP, yAlaP, 3);
                int[] xAlaP2 = {80, 105, 95};
                int[] yAlaP2 = {48, 55, 80};
                g2.fillPolygon(xAlaP2, yAlaP2, 3);
                g2.setColor(colorOscuro);
                int[] xColaP = {65, 75, 55, 65};
                int[] yColaP = {80, 100, 100, 80};
                g2.fillPolygon(xColaP, yColaP, 4);
                g2.setColor(colorClaro);
                int[] xCrestaP = {58, 65, 72};
                int[] yCrestaP = {15, 3, 15};
                g2.fillPolygon(xCrestaP, yCrestaP, 3);
                break;
        }

        g2.dispose();
        return new ImageIcon(image);
    }

    private static Color getColorForTipoStatic(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "perro" -> new Color(0x8D6E63);
            case "gato" -> new Color(0xFF8A65);
            case "pez" -> new Color(0x4FC3F7);
            case "hamster" -> new Color(0xFFD54F);
            case "pajaro" -> new Color(0x81C784);
            default -> new Color(0x9E9E9E);
        };
    }

    public void actualizarInformacion() {
        Enfermedad enfermedad = mascota.getEnfermedadActual();
        Habitat habitat = mascota.getHabitatAsignado();

        int salud = mascota.getSalud();
        int felicidad = mascota.getFelicidad();
        int comida = mascota.getComida(); // CORRECCIÓN: Actualizar Comida

        saludBar.setValue(salud);
        felicidadBar.setValue(felicidad);
        comidaBar.setValue(comida); // CORRECCIÓN: Barra de Comida

        // Actualizar colores según el nivel
        if (salud < 30) {
            saludBar.setForeground(new Color(0xE53935));
        } else if (salud < 60) {
            saludBar.setForeground(new Color(0xFFA726));
        } else {
            saludBar.setForeground(new Color(0x4CAF50));
        }

        if (felicidad < 30) {
            felicidadBar.setForeground(new Color(0xE53935));
        } else if (felicidad < 60) {
            felicidadBar.setForeground(new Color(0xFFA726));
        } else {
            felicidadBar.setForeground(new Color(0x4CAF50));
        }

        // CORRECCIÓN: Colores de la barra de comida
        if (comida < 30) {
            comidaBar.setForeground(new Color(0xE53935));
        } else if (comida < 60) {
            comidaBar.setForeground(new Color(0xFFA726));
        } else {
            comidaBar.setForeground(new Color(0x795548));
        }

        String enfermedadText = enfermedad == null ? "Ninguna" : enfermedad.getNombre();
        enfermedadLabel.setText("Enfermedad: " + enfermedadText);
        enfermedadLabel.setForeground(enfermedad == null ? new Color(0x4CAF50) : new Color(0xE53935));

        String habitatText = habitat == null ? "Sin asignar" : habitat.getTipo() + " (Higiene: " + habitat.getHigiene() + "/100)";
        habitatLabel.setText("Habitat: " + habitatText);
    }

    public Mascota getMascota() {
        return mascota;
    }
}