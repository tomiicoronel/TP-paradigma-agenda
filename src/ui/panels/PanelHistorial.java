package ui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para visualizar historial de adherencia.
 *
 * TODO: Implementar graficos y reportes de adherencia
 */
public class PanelHistorial extends JPanel {

    public PanelHistorial() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Historial de Adherencia");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.BLACK);

        JLabel placeholder = new JLabel("<html><center>" +
            "Aqui podras consultar el historial de adherencia.<br><br>" +
            "Funcionalidades proximamente:<br>" +
            "- Graficos de adherencia mensual<br>" +
            "- Reporte por medicamento<br>" +
            "- Estadisticas de cumplimiento<br>" +
            "- Exportar a PDF<br>" +
            "</center></html>");
        placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setForeground(Color.GRAY);

        add(titulo, BorderLayout.NORTH);
        add(placeholder, BorderLayout.CENTER);
    }
}

