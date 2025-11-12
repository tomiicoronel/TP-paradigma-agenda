package ui;

import controller.TomaService;
import domain.Recordatorio;
import infra.dao.RecordatorioDAO;
import infra.dao.impl.RecordatorioDAOImpl;
import service.RecordatorioService;
import shared.observer.Observer;
import ui.panels.PanelHoy;
import ui.panels.PanelMedicacion;
import ui.panels.PanelHistorial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana principal de la aplicación.
 *
 * Responsabilidades:
 * - Contener las pestañas principales (Hoy, Medicación, Historial)
 * - Integrar con TomaService para recibir notificaciones
 * - Gestionar el ciclo de vida de la aplicación
 */
public class MainFrame extends JFrame implements Observer {

    private final TomaService tomaService;
    private final RecordatorioService recordatorioService;

    // Paneles principales
    private PanelHoy panelHoy;
    private PanelMedicacion panelMedicacion;
    private PanelGestion panelGestion;
    private PanelHistorial panelHistorial;

    public MainFrame(TomaService tomaService) {
        this.tomaService = tomaService;
        this.recordatorioService = new RecordatorioService();

        initUI();
        setupObservers();
        setupShutdownHook();
    }

    private void initUI() {
        setTitle("Agenda Accesible - Sistema de Recordatorios");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Crear paneles
        panelGestion = new PanelGestion();
        panelHoy = new PanelHoy(recordatorioService);
        panelMedicacion = new PanelMedicacion();
        panelHistorial = new PanelHistorial();

        // Crear pestañas
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 16));
        tabs.addTab("⚙️ Gestión", panelGestion);
        tabs.addTab("📅 Hoy", panelHoy);
        tabs.addTab("💊 Medicación", panelMedicacion);
        tabs.addTab("📊 Historial", panelHistorial);

        // Layout principal
        setLayout(new BorderLayout());
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(70, 130, 180)); // Steel blue
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Agenda Accesible");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Sistema de Recordatorios Inteligentes");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(240, 248, 255)); // Alice blue

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        footer.setBackground(new Color(245, 245, 245));

        JLabel status = new JLabel("✅ Sistema activo - Monitoreo automático cada 60s");
        status.setFont(new Font("Arial", Font.PLAIN, 12));
        status.setForeground(new Color(34, 139, 34)); // Forest green

        footer.add(status);

        return footer;
    }

    private void setupObservers() {
        // Registrar este frame como observer del TomaService
        tomaService.addObserver(this);
    }

    private void setupShutdownHook() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("🛑 Cerrando aplicación...");
                tomaService.stop();
                System.out.println("👋 Hasta luego!");
            }
        });
    }

    /**
     * Método del patrón Observer.
     * Se ejecuta cuando TomaService notifica cambios.
     */
    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            // Actualizar panel de hoy
            panelHoy.actualizarRecordatorios();

            // Mostrar notificación popup si hay recordatorios pendientes
            mostrarNotificacionesPendientes();
        });
    }

    private void mostrarNotificacionesPendientes() {
        RecordatorioDAO recordatorioDAO = new RecordatorioDAOImpl();
        var pendientes = recordatorioDAO.findProximosNMinutos(5);

        if (!pendientes.isEmpty()) {
            for (Recordatorio r : pendientes) {
                // Solo mostrar si es muy cercano (próximos 2 minutos)
                long minutosHasta = java.time.temporal.ChronoUnit.MINUTES.between(
                    java.time.LocalDateTime.now(),
                    r.getProgramadoAt()
                );

                if (minutosHasta >= 0 && minutosHasta <= 2) {
                    mostrarPopupRecordatorio(r);
                }
            }
        }
    }

    private void mostrarPopupRecordatorio(Recordatorio r) {
        // Crear ventana de notificación
        JDialog dialog = new JDialog(this, "🔔 Recordatorio", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel mensaje = new JLabel("<html><b>Es hora de tu medicación</b><br><br>" +
            "Programado: " + r.getProgramadoAt().toLocalTime() + "</html>");
        mensaje.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel botonesPanel = new JPanel(new FlowLayout());

        JButton btnHecho = new JButton("✅ Hecho");
        btnHecho.setFont(new Font("Arial", Font.BOLD, 12));
        btnHecho.setBackground(new Color(34, 139, 34));
        btnHecho.setForeground(Color.WHITE);
        btnHecho.addActionListener(e -> {
            recordatorioService.marcarHecho(r.getId());
            dialog.dispose();
            panelHoy.actualizarRecordatorios();
        });

        JButton btnAplazar = new JButton("⏰ Aplazar 15 min");
        btnAplazar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAplazar.addActionListener(e -> {
            recordatorioService.aplazarRecordatorio(r.getId(), 15);
            dialog.dispose();
            panelHoy.actualizarRecordatorios();
        });

        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancelar.addActionListener(e -> {
            recordatorioService.cancelarRecordatorio(r.getId());
            dialog.dispose();
            panelHoy.actualizarRecordatorios();
        });

        botonesPanel.add(btnHecho);
        botonesPanel.add(btnAplazar);
        botonesPanel.add(btnCancelar);

        panel.add(mensaje, BorderLayout.CENTER);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
