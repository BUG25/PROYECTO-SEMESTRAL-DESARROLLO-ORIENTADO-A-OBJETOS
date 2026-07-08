package gui;

import model.mascotas.Mascota;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 * Representa un sprite animado de una mascota que se mueve por el espacio.
 */
public class SpriteMascota extends JPanel implements Runnable {
    private final Mascota mascota;
    private int x, y;
    private int velocidadX, velocidadY;
    private int direccion = 1;
    private boolean animando = true;
    private final Random random = new Random();
    private int frameAnimacion = 0;
    private int contadorMovimiento = 0;
    private final int TAMANO_SPRITE = 48;
    private final int VELOCIDAD_BASE = 2;
    private final int ANCHO_PADRE, ALTO_PADRE;
    private final Color colorCuerpo;

    public SpriteMascota(Mascota mascota, int anchoPadre, int altoPadre) {
        this.mascota = mascota;
        this.ANCHO_PADRE = anchoPadre;
        this.ALTO_PADRE = altoPadre;

        this.x = 20 + random.nextInt(Math.max(1, anchoPadre - TAMANO_SPRITE - 40));
        this.y = 20 + random.nextInt(Math.max(1, altoPadre - TAMANO_SPRITE - 40));

        this.velocidadX = VELOCIDAD_BASE + random.nextInt(3);
        this.velocidadY = VELOCIDAD_BASE + random.nextInt(3);

        this.colorCuerpo = obtenerColorMascota(mascota);

        setOpaque(false);
        setPreferredSize(new Dimension(TAMANO_SPRITE, TAMANO_SPRITE));
        setBounds(x, y, TAMANO_SPRITE, TAMANO_SPRITE);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarInfoMascota();
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

    private void mostrarInfoMascota() {
        String info = String.format(
                "<html><div style='text-align: center;'>" +
                        "<h2>🐾 %s</h2>" +
                        "<b>Especie:</b> %s<br>" +
                        "<b>❤️ Salud:</b> %d%%<br>" +
                        "<b>🍖 Comida:</b> %d%%<br>" +
                        "<b>😊 Felicidad:</b> %d%%<br>" +
                        "<b>🏠 Hábitat:</b> %s<br>" +
                        "</div></html>",
                mascota.getNombre(),
                mascota.getClass().getSimpleName(),
                mascota.getSalud(),
                mascota.getComida(),
                mascota.getFelicidad(),
                "Sin hábitat"
        );

        JOptionPane.showMessageDialog(
                this,
                info,
                "📋 Información de " + mascota.getNombre(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void run() {
        while (animando) {
            mover();
            repaint();

            try {
                Thread.sleep(50);
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

        if (random.nextInt(100) < 3) {
            velocidadX = (random.nextInt(5) + 1) * (random.nextBoolean() ? 1 : -1);
            velocidadY = (random.nextInt(5) + 1) * (random.nextBoolean() ? 1 : -1);
        }

        setBounds(x, y, TAMANO_SPRITE, TAMANO_SPRITE);
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
        g2.setColor(colorCuerpo);
        g2.fillOval(w / 3, h / 3, w / 2, h / 2);
        g2.fillOval(w / 4, h / 6, w / 3, h / 3);
        g2.fillOval(w / 8, h / 6, w / 8, h / 4);
        g2.fillOval(w / 2, h / 6, w / 8, h / 4);

        g2.setColor(Color.WHITE);
        g2.fillOval(w / 4 + 4, h / 6 + 4, 6, 6);
        g2.fillOval(w / 4 + 14, h / 6 + 4, 6, 6);
        g2.setColor(Color.BLACK);
        g2.fillOval(w / 4 + 6, h / 6 + 6, 3, 3);
        g2.fillOval(w / 4 + 16, h / 6 + 6, 3, 3);

        g2.setColor(new Color(0x4A2C17));
        g2.fillOval(w / 3 + 2, h / 4 + 6, 8, 6);
        g2.fillOval(w / 3 + 10, h / 4 + 6, 8, 6);

        g2.setColor(Color.BLACK);
        g2.fillOval(w / 3 + 6, h / 4 + 10, 6, 4);

        int colaX = w * 3 / 4 + (int) (Math.sin(frameAnimacion * 0.5) * 4);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(w * 3 / 4, h / 2, colaX, h / 4);
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
