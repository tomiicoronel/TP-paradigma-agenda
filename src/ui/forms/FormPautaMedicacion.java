package ui.forms;

import domain.Medicamento;
import domain.Paciente;
import domain.PacienteMedicamento;
import service.MedicamentoService;
import service.PacienteService;
import service.PautaMedicacionService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Formulario para crear o editar una pauta de medicación.
 * Asigna un medicamento a un paciente con dosis, frecuencia y horarios.
 */
public class FormPautaMedicacion extends JDialog {
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final PautaMedicacionService pautaService;

    private JComboBox<PacienteItem> cboPaciente;
    private JComboBox<MedicamentoItem> cboMedicamento;
    private JTextField txtDosis;
    private JTextField txtUnidad;
    private JTextField txtIntervaloHoras;
    private JTextField txtHoraInicio;
    private JCheckBox chkActiva;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private PacienteMedicamento pautaExistente;

    public FormPautaMedicacion(Frame parent) {
        this(parent, null);
    }

    public FormPautaMedicacion(Frame parent, PacienteMedicamento pauta) {
        super(parent, pauta == null ? "Nueva Pauta de Medicación" : "Editar Pauta", true);
        this.pacienteService = new PacienteService();
        this.medicamentoService = new MedicamentoService();
        this.pautaService = new PautaMedicacionService();
        this.pautaExistente = pauta;

        initComponents();
        layoutComponents();
        setupListeners();

        if (pautaExistente != null) {
            cargarDatos();
        }

        setSize(600, 550);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        // Combo de pacientes
        cboPaciente = new JComboBox<>();
        try {
            List<Paciente> pacientes = pacienteService.listarTodos();
            for (Paciente p : pacientes) {
                String edad = p.getEdad() != null ? " (" + p.getEdad() + " años)" : "";
                cboPaciente.addItem(new PacienteItem(p.getId(), p.getNombre() + edad));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar pacientes: " + e.getMessage());
        }

        // Combo de medicamentos
        cboMedicamento = new JComboBox<>();
        try {
            List<Medicamento> medicamentos = medicamentoService.listarTodos();
            for (Medicamento m : medicamentos) {
                String info = m.getNombreComercial();
                if (m.getVia() != null && !m.getVia().isEmpty()) {
                    info += " - " + m.getVia();
                }
                cboMedicamento.addItem(new MedicamentoItem(m.getId(), info));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos: " + e.getMessage());
        }

        txtDosis = new JTextField(10);
        txtUnidad = new JTextField(10);
        txtIntervaloHoras = new JTextField(10);
        txtHoraInicio = new JTextField(15);
        chkActiva = new JCheckBox("Pauta activa", true);

        btnGuardar = new JButton("Guardar Pauta");
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnCancelar = new JButton("Cancelar");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Título con instrucciones
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblInstrucciones = new JLabel("<html><b>Configurar pauta de medicación</b><br>" +
            "Asigna un medicamento a un paciente con horarios específicos</html>");
        lblInstrucciones.setForeground(new Color(100, 100, 100));
        panelForm.add(lblInstrucciones, gbc);

        // Separador
        gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(new JSeparator(), gbc);

        // Paciente
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panelForm.add(cboPaciente, gbc);

        // Medicamento
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelForm.add(new JLabel("Medicamento:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(cboMedicamento, gbc);

        // Dosis
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Dosis:"), gbc);
        gbc.gridx = 1;
        JPanel panelDosis = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDosis.add(txtDosis);
        panelDosis.add(new JLabel("Unidad:"));
        panelDosis.add(txtUnidad);
        panelDosis.add(new JLabel("(ej: mg, ml, comprimidos)"));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(panelDosis, gbc);

        // Intervalo
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Frecuencia:"), gbc);
        gbc.gridx = 1;
        JPanel panelIntervalo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIntervalo.add(new JLabel("Cada"));
        panelIntervalo.add(txtIntervaloHoras);
        panelIntervalo.add(new JLabel("horas"));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(panelIntervalo, gbc);

        // Hora de inicio
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Hora inicio:"), gbc);
        gbc.gridx = 1;
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHora.add(txtHoraInicio);
        panelHora.add(new JLabel("(formato: dd/MM/yyyy HH:mm)"));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(panelHora, gbc);

        // Estado activa
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(chkActiva, gbc);

        // Panel de ejemplo
        gbc.gridy = 8;
        JPanel panelEjemplo = new JPanel(new BorderLayout(5, 5));
        panelEjemplo.setBorder(BorderFactory.createTitledBorder("Ejemplo"));
        JLabel lblEjemplo = new JLabel("<html>" +
            "<b>Ejemplo de configuración:</b><br>" +
            "Dosis: 500, Unidad: mg<br>" +
            "Frecuencia: Cada 8 horas<br>" +
            "Hora inicio: 12/11/2025 08:00<br><br>" +
            "<i>Esto generará tomas a las 08:00, 16:00 y 00:00 cada día</i>" +
            "</html>");
        lblEjemplo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEjemplo.setForeground(new Color(100, 100, 100));
        panelEjemplo.add(lblEjemplo);
        panelForm.add(panelEjemplo, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarDatos() {
        // Seleccionar paciente
        for (int i = 0; i < cboPaciente.getItemCount(); i++) {
            PacienteItem item = cboPaciente.getItemAt(i);
            if (item.getId().equals(pautaExistente.getPacienteId())) {
                cboPaciente.setSelectedIndex(i);
                break;
            }
        }

        // Seleccionar medicamento
        for (int i = 0; i < cboMedicamento.getItemCount(); i++) {
            MedicamentoItem item = cboMedicamento.getItemAt(i);
            if (item.getId().equals(pautaExistente.getMedicamentoId())) {
                cboMedicamento.setSelectedIndex(i);
                break;
            }
        }

        txtDosis.setText(String.valueOf(pautaExistente.getDosis()));
        if (pautaExistente.getUnidad() != null) {
            txtUnidad.setText(pautaExistente.getUnidad());
        }

        // Convertir minutos a horas para mostrar
        int horas = pautaExistente.getIntervaloMin() / 60;
        txtIntervaloHoras.setText(String.valueOf(horas));

        if (pautaExistente.getHoraInicio() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            txtHoraInicio.setText(pautaExistente.getHoraInicio().format(formatter));
        }

        chkActiva.setSelected(pautaExistente.isActivo());
    }

    private void guardar() {
        // Validar paciente
        PacienteItem paciente = (PacienteItem) cboPaciente.getSelectedItem();
        if (paciente == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un paciente",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar medicamento
        MedicamentoItem medicamento = (MedicamentoItem) cboMedicamento.getSelectedItem();
        if (medicamento == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un medicamento",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar dosis
        if (txtDosis.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La dosis es obligatoria",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtDosis.requestFocus();
            return;
        }

        double dosis;
        try {
            dosis = Double.parseDouble(txtDosis.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La dosis debe ser un número válido",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtDosis.requestFocus();
            return;
        }

        // Validar intervalo
        if (txtIntervaloHoras.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El intervalo es obligatorio",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtIntervaloHoras.requestFocus();
            return;
        }

        int intervaloHoras;
        try {
            intervaloHoras = Integer.parseInt(txtIntervaloHoras.getText().trim());
            if (intervaloHoras <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El intervalo debe ser un número mayor a 0",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtIntervaloHoras.requestFocus();
            return;
        }

        // Parsear hora de inicio
        LocalDateTime horaInicio;
        if (!txtHoraInicio.getText().trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                horaInicio = LocalDateTime.parse(txtHoraInicio.getText().trim(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha/hora inválido. Use dd/MM/yyyy HH:mm (ejemplo: 12/11/2025 08:00)",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
                txtHoraInicio.requestFocus();
                return;
            }
        } else {
            // Si no se especifica, usar la hora actual
            horaInicio = LocalDateTime.now();
        }

        try {
            // Mostrar confirmación antes de guardar
            String mensaje = String.format(
                "Pauta configurada:\n\n" +
                "Paciente: %s\n" +
                "Medicamento: %s\n" +
                "Dosis: %.2f %s\n" +
                "Frecuencia: Cada %d horas\n" +
                "Hora inicio: %s\n" +
                "Estado: %s\n\n" +
                "¿Desea guardar esta pauta?",
                paciente.getNombre(),
                medicamento.getNombre(),
                dosis,
                txtUnidad.getText().trim(),
                intervaloHoras,
                horaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                chkActiva.isSelected() ? "Activa" : "Inactiva"
            );

            int confirmacion = JOptionPane.showConfirmDialog(this,
                mensaje,
                "Confirmar Pauta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Guardar usando el servicio
                boolean guardado = pautaService.crearPauta(
                    paciente.getId(),
                    medicamento.getId(),
                    dosis,
                    txtUnidad.getText().trim(),
                    intervaloHoras,
                    horaInicio
                );

                if (guardado) {
                    JOptionPane.showMessageDialog(this,
                        "¡Pauta guardada exitosamente!\n\n" +
                        "La pauta ya está activa y comenzará a generar recordatorios.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error: No se pudo guardar la pauta en la base de datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Clase auxiliar para items de pacientes en el combo.
     */
    private static class PacienteItem {
        private final Long id;
        private final String nombre;

        public PacienteItem(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }

        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
     * Clase auxiliar para items de medicamentos en el combo.
     */
    private static class MedicamentoItem {
        private final Long id;
        private final String nombre;

        public MedicamentoItem(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }

        @Override
        public String toString() {
            return nombre;
        }
    }
}

