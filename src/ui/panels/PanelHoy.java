package ui.panels;

import domain.Recordatorio;
import service.RecordatorioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel que muestra los recordatorios del día actual.
 *
 * Funcionalidades:
 * - Lista de recordatorios pendientes/aplazados
 * - Botones para marcar como hecho, aplazar o cancelar
 * - Actualización automática cuando el TomaService notifica cambios
 */
public class PanelHoy extends JPanel {

    private final RecordatorioService recordatorioService;
    private JTable tablaRecordatorios;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstadisticas;

    public PanelHoy(RecordatorioService recordatorioService) {
        this.recordatorioService = recordatorioService;
        initUI();
        actualizarRecordatorios();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con título y estadísticas
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Panel central con tabla
        add(createTablePanel(), BorderLayout.CENTER);

        // Panel inferior con botones de acción
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Recordatorios de Hoy");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.BLACK);

        lblEstadisticas = new JLabel();
        lblEstadisticas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstadisticas.setForeground(Color.GRAY);

        panel.add(titulo, BorderLayout.WEST);
        panel.add(lblEstadisticas, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane createTablePanel() {
        // Modelo de tabla
        String[] columnas = {"ID", "Hora", "Estado", "Descripcion", "Ventana (min)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };

        tablaRecordatorios = new JTable(modeloTabla);
        tablaRecordatorios.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaRecordatorios.setRowHeight(30);
        tablaRecordatorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar anchos de columnas
        tablaRecordatorios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaRecordatorios.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaRecordatorios.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaRecordatorios.getColumnModel().getColumn(3).setPreferredWidth(300);
        tablaRecordatorios.getColumnModel().getColumn(4).setPreferredWidth(100);

        return new JScrollPane(tablaRecordatorios);
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnHecho = new JButton("Marcar como Hecho");
        btnHecho.setFont(new Font("Arial", Font.BOLD, 14));
        btnHecho.setBackground(new Color(34, 139, 34));
        btnHecho.setForeground(Color.BLACK);
        btnHecho.setFocusPainted(false);
        btnHecho.addActionListener(e -> marcarComoHecho());

        JButton btnAplazar = new JButton("Aplazar 15 min");
        btnAplazar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAplazar.setBackground(new Color(255, 165, 0));
        btnAplazar.setForeground(Color.BLACK);
        btnAplazar.setFocusPainted(false);
        btnAplazar.addActionListener(e -> aplazarRecordatorio());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(220, 20, 60));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> cancelarRecordatorio());

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnActualizar.addActionListener(e -> actualizarRecordatorios());

        panel.add(btnHecho);
        panel.add(btnAplazar);
        panel.add(btnCancelar);
        panel.add(btnActualizar);

        return panel;
    }

    public void actualizarRecordatorios() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Obtener recordatorios de hoy
        List<Recordatorio> recordatorios = recordatorioService.listarRecordatoriosHoy();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        int pendientes = 0;
        int aplazados = 0;
        int hechos = 0;

        for (Recordatorio r : recordatorios) {
            String hora = r.getProgramadoAt().format(timeFormatter);
            String descripcion = "Recordatorio #" + r.getId();

            Object[] fila = {
                r.getId(),
                hora,
                r.getEstado(),
                descripcion,
                r.getVentanaMin()
            };

            modeloTabla.addRow(fila);

            // Contar estados
            switch (r.getEstado()) {
                case "PENDIENTE": pendientes++; break;
                case "APLAZADO": aplazados++; break;
                case "HECHO": hechos++; break;
            }
        }

        // Actualizar estadísticas
        lblEstadisticas.setText(String.format(
            "Total: %d | Hechos: %d | Pendientes: %d | Aplazados: %d",
            recordatorios.size(), hechos, pendientes, aplazados
        ));
    }

    private void marcarComoHecho() {
        int selectedRow = tablaRecordatorios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecciona un recordatorio de la lista.",
                "Ningun recordatorio seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int recordatorioId = (int) modeloTabla.getValueAt(selectedRow, 0);
        String estado = (String) modeloTabla.getValueAt(selectedRow, 2);

        if ("HECHO".equals(estado)) {
            JOptionPane.showMessageDialog(this,
                "Este recordatorio ya esta marcado como hecho.",
                "Ya completado",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        recordatorioService.marcarHecho((long) recordatorioId);
        JOptionPane.showMessageDialog(this,
            "Recordatorio marcado como hecho.",
            "Exito",
            JOptionPane.INFORMATION_MESSAGE);

        actualizarRecordatorios();
    }

    private void aplazarRecordatorio() {
        int selectedRow = tablaRecordatorios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecciona un recordatorio de la lista.",
                "Ningun recordatorio seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int recordatorioId = (int) modeloTabla.getValueAt(selectedRow, 0);

        String input = JOptionPane.showInputDialog(this,
            "Cuantos minutos deseas aplazar?",
            "15");

        if (input != null && !input.trim().isEmpty()) {
            try {
                int minutos = Integer.parseInt(input.trim());
                recordatorioService.aplazarRecordatorio((long) recordatorioId, minutos);
                JOptionPane.showMessageDialog(this,
                    "Recordatorio aplazado " + minutos + " minutos.",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarRecordatorios();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Por favor, ingresa un numero valido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelarRecordatorio() {
        int selectedRow = tablaRecordatorios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecciona un recordatorio de la lista.",
                "Ningun recordatorio seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int recordatorioId = (int) modeloTabla.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Estas seguro de cancelar este recordatorio?",
            "Confirmar cancelacion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            recordatorioService.cancelarRecordatorio((long) recordatorioId);
            JOptionPane.showMessageDialog(this,
                "Recordatorio cancelado.",
                "Exito",
                JOptionPane.INFORMATION_MESSAGE);
            actualizarRecordatorios();
        }
    }
}

