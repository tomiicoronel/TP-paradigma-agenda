package ui.forms;

import domain.Cuidador;
import domain.Paciente;
import service.CuidadorService;
import service.PacienteService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Formulario para crear o editar un paciente.
 */
public class FormPaciente extends JDialog {
    private final PacienteService pacienteService;
    private final CuidadorService cuidadorService;

    private JTextField txtNombre;
    private JTextField txtFechaNacimiento;
    private JTextField txtDiagnostico;
    private JTextArea txtPreferencias;
    private JComboBox<CuidadorItem> cboCuidador;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private Paciente pacienteExistente;

    public FormPaciente(Frame parent, PacienteService service) {
        this(parent, service, null);
    }

    public FormPaciente(Frame parent, PacienteService service, Paciente paciente) {
        super(parent, paciente == null ? "Nuevo Paciente" : "Editar Paciente", true);
        this.pacienteService = service;
        this.cuidadorService = new CuidadorService();
        this.pacienteExistente = paciente;

        initComponents();
        layoutComponents();
        setupListeners();

        if (pacienteExistente != null) {
            cargarDatos();
        }

        setSize(550, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        txtNombre = new JTextField(20);
        txtFechaNacimiento = new JTextField(15);
        txtDiagnostico = new JTextField(20);
        txtPreferencias = new JTextArea(4, 20);
        txtPreferencias.setLineWrap(true);
        txtPreferencias.setWrapStyleWord(true);

        // Combo de cuidadores
        cboCuidador = new JComboBox<>();
        cboCuidador.addItem(new CuidadorItem(null, "Sin cuidador asignado"));

        try {
            List<Cuidador> cuidadores = cuidadorService.listarTodos();
            for (Cuidador c : cuidadores) {
                cboCuidador.addItem(new CuidadorItem(c.getId(), c.getNombre()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar cuidadores: " + e.getMessage());
        }

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(76, 175, 80));
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

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panelForm.add(txtNombre, gbc);

        // Fecha de nacimiento
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelForm.add(new JLabel("Fecha Nacimiento:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFecha.add(txtFechaNacimiento);
        panelFecha.add(new JLabel("(formato: dd/MM/yyyy)"));
        panelForm.add(panelFecha, gbc);

        // Diagnóstico
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Diagnóstico:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtDiagnostico, gbc);

        // Cuidador
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Cuidador:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(cboCuidador, gbc);

        // Preferencias de accesibilidad
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelForm.add(new JLabel("Preferencias:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane scrollPreferencias = new JScrollPane(txtPreferencias);
        panelForm.add(scrollPreferencias, gbc);

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
        txtNombre.setText(pacienteExistente.getNombre());

        if (pacienteExistente.getFechaNacimiento() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtFechaNacimiento.setText(pacienteExistente.getFechaNacimiento().format(formatter));
        }

        if (pacienteExistente.getDiagnostico() != null) {
            txtDiagnostico.setText(pacienteExistente.getDiagnostico());
        }

        if (pacienteExistente.getPreferenciasAccesibilidad() != null) {
            txtPreferencias.setText(pacienteExistente.getPreferenciasAccesibilidad());
        }

        // Seleccionar cuidador
        if (pacienteExistente.getCuidadorId() != null) {
            for (int i = 0; i < cboCuidador.getItemCount(); i++) {
                CuidadorItem item = cboCuidador.getItemAt(i);
                if (item.getId() != null && item.getId().equals(pacienteExistente.getCuidadorId())) {
                    cboCuidador.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void guardar() {
        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre es obligatorio",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        // Parsear fecha de nacimiento si está presente
        LocalDate fechaNacimiento = null;
        if (!txtFechaNacimiento.getText().trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fechaNacimiento = LocalDate.parse(txtFechaNacimiento.getText().trim(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 15/03/1980)",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
                txtFechaNacimiento.requestFocus();
                return;
            }
        }

        try {
            CuidadorItem cuidadorSeleccionado = (CuidadorItem) cboCuidador.getSelectedItem();
            Long cuidadorId = cuidadorSeleccionado != null ? cuidadorSeleccionado.getId() : null;

            if (pacienteExistente == null) {
                // Crear nuevo paciente
                Paciente nuevoPaciente = new Paciente();
                nuevoPaciente.setNombre(txtNombre.getText().trim());
                nuevoPaciente.setFechaNacimiento(fechaNacimiento);
                nuevoPaciente.setDiagnostico(txtDiagnostico.getText().trim());
                nuevoPaciente.setPreferenciasAccesibilidad(txtPreferencias.getText().trim());
                nuevoPaciente.setCuidadorId(cuidadorId);

                Long resultado = pacienteService.crearPaciente(
                    nuevoPaciente.getNombre(),
                    nuevoPaciente.getPreferenciasAccesibilidad(),
                    cuidadorId
                );

                // Actualizar campos adicionales
                if (fechaNacimiento != null || !txtDiagnostico.getText().trim().isEmpty()) {
                    Paciente paciente = pacienteService.obtenerPaciente(resultado).orElse(null);
                    if (paciente != null) {
                        paciente.setFechaNacimiento(fechaNacimiento);
                        paciente.setDiagnostico(txtDiagnostico.getText().trim());
                        pacienteService.actualizarPaciente(paciente);
                    }
                }

                JOptionPane.showMessageDialog(this,
                    "Paciente creado exitosamente con ID: " + resultado,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar existente
                pacienteExistente.setNombre(txtNombre.getText().trim());
                pacienteExistente.setFechaNacimiento(fechaNacimiento);
                pacienteExistente.setDiagnostico(txtDiagnostico.getText().trim());
                pacienteExistente.setPreferenciasAccesibilidad(txtPreferencias.getText().trim());
                pacienteExistente.setCuidadorId(cuidadorId);

                pacienteService.actualizarPaciente(pacienteExistente);

                JOptionPane.showMessageDialog(this,
                    "Paciente actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Clase auxiliar para el combo de cuidadores.
     */
    private static class CuidadorItem {
        private final Long id;
        private final String nombre;

        public CuidadorItem(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() {
            return id;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}

