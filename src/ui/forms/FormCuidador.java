package ui.forms;

import domain.Cuidador;
import service.CuidadorService;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para crear o editar un cuidador.
 */
public class FormCuidador extends JDialog {
    private final CuidadorService cuidadorService;

    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtRelacion;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private Cuidador cuidadorExistente;

    public FormCuidador(Frame parent, CuidadorService service) {
        this(parent, service, null);
    }

    public FormCuidador(Frame parent, CuidadorService service, Cuidador cuidador) {
        super(parent, cuidador == null ? "Nuevo Cuidador" : "Editar Cuidador", true);
        this.cuidadorService = service;
        this.cuidadorExistente = cuidador;

        initComponents();
        layoutComponents();
        setupListeners();

        if (cuidadorExistente != null) {
            cargarDatos();
        }

        setSize(450, 300);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        txtNombre = new JTextField(20);
        txtTelefono = new JTextField(15);
        txtRelacion = new JTextField(20);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(156, 39, 176));
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

        // Teléfono
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelForm.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtTelefono, gbc);

        // Relación
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Relacion:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtRelacion, gbc);

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
        txtNombre.setText(cuidadorExistente.getNombre());
        if (cuidadorExistente.getTelefono() != null) {
            txtTelefono.setText(cuidadorExistente.getTelefono());
        }
        if (cuidadorExistente.getRelacion() != null) {
            txtRelacion.setText(cuidadorExistente.getRelacion());
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
            if (cuidadorExistente == null) {
                Long resultado = cuidadorService.crearCuidador(
                    txtNombre.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtRelacion.getText().trim()
                );

                JOptionPane.showMessageDialog(this,
                    "Cuidador creado exitosamente con ID: " + resultado,
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                cuidadorService.actualizarNombre(
                    cuidadorExistente.getId(),
                    txtNombre.getText().trim()
                );

                JOptionPane.showMessageDialog(this,
                    "Cuidador actualizado exitosamente",
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

