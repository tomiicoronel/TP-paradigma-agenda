package ui.forms;

import domain.Medicamento;
import service.MedicamentoService;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para crear o editar un medicamento.
 */
public class FormMedicamento extends JDialog {
    private final MedicamentoService medicamentoService;

    private JTextField txtNombre;
    private JTextField txtVia;
    private JTextField txtUnidadDosis;
    private JTextArea txtNotas;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private Medicamento medicamentoExistente;

    public FormMedicamento(Frame parent, MedicamentoService service) {
        this(parent, service, null);
    }

    public FormMedicamento(Frame parent, MedicamentoService service, Medicamento medicamento) {
        super(parent, medicamento == null ? "Nuevo Medicamento" : "Editar Medicamento", true);
        this.medicamentoService = service;
        this.medicamentoExistente = medicamento;

        initComponents();
        layoutComponents();
        setupListeners();

        if (medicamentoExistente != null) {
            cargarDatos();
        }

        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        txtNombre = new JTextField(20);
        txtVia = new JTextField(15);
        txtUnidadDosis = new JTextField(15);
        txtNotas = new JTextArea(5, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(255, 152, 0));
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

        // Via
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelForm.add(new JLabel("Via (ej: oral):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtVia, gbc);

        // Unidad dosis
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Unidad dosis (ej: mg):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtUnidadDosis, gbc);

        // Notas
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelForm.add(new JLabel("Notas:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        panelForm.add(scrollNotas, gbc);

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
        txtNombre.setText(medicamentoExistente.getNombreComercial());
        if (medicamentoExistente.getVia() != null) {
            txtVia.setText(medicamentoExistente.getVia());
        }
        if (medicamentoExistente.getUnidadDosis() != null) {
            txtUnidadDosis.setText(medicamentoExistente.getUnidadDosis());
        }
        if (medicamentoExistente.getNotas() != null) {
            txtNotas.setText(medicamentoExistente.getNotas());
        }
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre es obligatorio",
                "Error de validacion",
                JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        try {
            if (medicamentoExistente == null) {
                // Crear nuevo
                Long resultado = medicamentoService.crearMedicamento(
                    txtNombre.getText().trim(),
                    txtVia.getText().trim(),
                    txtUnidadDosis.getText().trim(),
                    txtNotas.getText().trim()
                );

                JOptionPane.showMessageDialog(this,
                    "Medicamento creado exitosamente con ID: " + resultado,
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar existente
                medicamentoExistente.setNombreComercial(txtNombre.getText().trim());
                medicamentoExistente.setVia(txtVia.getText().trim());
                medicamentoExistente.setUnidadDosis(txtUnidadDosis.getText().trim());
                medicamentoExistente.setNotas(txtNotas.getText().trim());

                JOptionPane.showMessageDialog(this,
                    "Medicamento actualizado exitosamente",
                    "Exito",
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
}

