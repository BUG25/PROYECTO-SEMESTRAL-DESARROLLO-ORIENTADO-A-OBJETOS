import gui.TiendaGUI;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada principal de la aplicación.
 * Abre la interfaz gráfica de la tienda.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TiendaGUI gui = new TiendaGUI();
            gui.setVisible(true);
        });
    }
}
