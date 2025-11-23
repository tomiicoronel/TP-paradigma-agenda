package ui.panels;

import domain.Medicamento;
import domain.Paciente;
import domain.PacienteMedicamento;
import infra.dao.PacienteMedicamentoDAO;
import infra.dao.impl.PacienteMedicamentoDAOImpl;
import service.MedicamentoService;
import service.PacienteService;
import ui.forms.FormPautaMedicacion;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para gestionar pautas de medicación.
 */
public class PanelMedicacion extends JPanel {
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final PacienteMedicamentoDAO pautaDAO;

    private JTextArea txtPautas;
    private JButton btnNuevaPauta;
    private JButton btnActualizar;

    public PanelMedicacion() {
        this.pacienteService = new PacienteService();
        this.medicamentoService = new MedicamentoService();
        this.pautaDAO = new PacienteMedicamentoDAOImpl();

        initUI();
        cargarPautas();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("💊 Pautas de Medicación");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.BLACK);
        panelSuperior.add(titulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNuevaPauta = new JButton("+ Nueva Pauta");
        btnNuevaPauta.setBackground(new Color(33, 150, 243));
        btnNuevaPauta.setForeground(Color.WHITE);
        btnNuevaPauta.setFont(new Font("Arial", Font.BOLD, 14));
        btnNuevaPauta.setFocusPainted(false);
        btnNuevaPauta.setPreferredSize(new Dimension(200, 40));
        btnNuevaPauta.addActionListener(e -> abrirFormNuevaPauta());

        btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnActualizar.setPreferredSize(new Dimension(150, 40));
        btnActualizar.addActionListener(e -> cargarPautas());

        panelBotones.add(btnNuevaPauta);
        panelBotones.add(btnActualizar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Área de texto
        txtPautas = new JTextArea();
        txtPautas.setEditable(false);
        txtPautas.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtPautas.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(txtPautas);
        scroll.setBorder(BorderFactory.createTitledBorder("Pautas Activas"));

        add(scroll, BorderLayout.CENTER);

        // Panel inferior
        JLabel lblInstrucciones = new JLabel(
            "<html><center>" +
            "<b>Instrucciones:</b><br>" +
            "1. Asegúrate de tener pacientes y medicamentos registrados en 'Gestión'<br>" +
            "2. Haz click en '+ Nueva Pauta' para asignar un medicamento a un paciente<br>" +
            "3. Las pautas activas se mostrarán aquí y generarán recordatorios automáticos" +
            "</center></html>"
        );
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInstrucciones.setForeground(Color.GRAY);
        lblInstrucciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(lblInstrucciones, BorderLayout.SOUTH);
    }

    private void abrirFormNuevaPauta() {
        // Verificar que haya pacientes y medicamentos
        try {
            List<Paciente> pacientes = pacienteService.listarTodos();
            List<Medicamento> medicamentos = medicamentoService.listarTodos();

            if (pacientes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Primero debes registrar al menos un paciente.\n" +
                    "Ve a la pestaña 'Gestión' → 'Nuevo Paciente'",
                    "No hay pacientes",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (medicamentos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Primero debes registrar al menos un medicamento.\n" +
                    "Ve a la pestaña 'Gestión' → 'Nuevo Medicamento'",
                    "No hay medicamentos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Abrir formulario
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            FormPautaMedicacion form = new FormPautaMedicacion(parent);
            form.setVisible(true);

            // Actualizar lista después de cerrar el formulario
            cargarPautas();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir el formulario: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPautas() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
            sb.append("                          PAUTAS DE MEDICACIÓN ACTIVAS\n");
            sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

            // Obtener todas las pautas
            List<Paciente> pacientes = pacienteService.listarTodos();
            List<PacienteMedicamento> todasLasPautas = new ArrayList<>();
            for (Paciente p : pacientes) {
                todasLasPautas.addAll(pautaDAO.findByPacienteId(p.getId()));
            }

            if (todasLasPautas.isEmpty()) {
                sb.append("No hay pautas configuradas todavía.\n\n");
                sb.append("Haz click en '+ Nueva Pauta' para comenzar.\n");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                int contador = 1;

                for (PacienteMedicamento pauta : todasLasPautas) {
                    // Obtener nombre del paciente
                    String nombrePaciente = "Desconocido";
                    try {
                        Paciente paciente = pacienteService.obtenerPaciente(pauta.getPacienteId()).orElse(null);
                        if (paciente != null) {
                            nombrePaciente = paciente.getNombre();
                        }
                    } catch (Exception e) {
                        // Ignorar
                    }

                    // Obtener nombre del medicamento
                    String nombreMedicamento = "Desconocido";
                    try {
                        Medicamento medicamento = medicamentoService.obtenerMedicamento(pauta.getMedicamentoId()).orElse(null);
                        if (medicamento != null) {
                            nombreMedicamento = medicamento.getNombreComercial();
                        }
                    } catch (Exception e) {
                        // Ignorar
                    }

                    sb.append(String.format("─── Pauta #%d %s───\n",
                        contador++,
                        pauta.isActivo() ? "✓ " : "✗ INACTIVA "));
                    sb.append(String.format("Paciente:     %s\n", nombrePaciente));
                    sb.append(String.format("Medicamento:  %s\n", nombreMedicamento));
                    sb.append(String.format("Dosis:        %.2f %s\n",
                        pauta.getDosis(),
                        pauta.getUnidad() != null ? pauta.getUnidad() : ""));
                    sb.append(String.format("Frecuencia:   Cada %d horas\n", pauta.getIntervaloMin() / 60));

                    if (pauta.getHoraInicio() != null) {
                        sb.append(String.format("Inicio:       %s\n", pauta.getHoraInicio().format(formatter)));
                    }

                    if (pauta.getProximaTomaAt() != null) {
                        sb.append(String.format("Próxima toma: %s\n", pauta.getProximaTomaAt().format(formatter)));
                    }

                    sb.append("\n");
                }

                sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append(String.format("Total: %d pauta(s) registrada(s)\n", todasLasPautas.size()));
            }

            txtPautas.setText(sb.toString());
            txtPautas.setCaretPosition(0);

        } catch (Exception e) {
            txtPautas.setText("Error al cargar pautas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refrescar() {
        cargarPautas();
    }
}

