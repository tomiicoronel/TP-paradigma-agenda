package ui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para gestionar medicamentos y pautas.
 *
 * TODO: Implementar formularios CRUD de medicamentos
 */
public class PanelMedicacion extends JPanel {

    public PanelMedicacion() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("💊 Gestión de Medicamentos");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel placeholder = new JLabel("<html><center>" +
            "Aquí podrás gestionar medicamentos y pautas.<br><br>" +
            "Funcionalidades próximamente:<br>" +
            "• Registrar nuevos medicamentos<br>" +
            "• Asignar medicamentos a pacientes<br>" +
            "• Configurar pautas de toma<br>" +
            "• Editar/eliminar medicamentos<br>" +
            "</center></html>");
        placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setForeground(Color.GRAY);

        add(titulo, BorderLayout.NORTH);
        add(placeholder, BorderLayout.CENTER);
    }
}

