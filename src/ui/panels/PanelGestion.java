package ui.panels;

import service.CuidadorService;
import service.MedicamentoService;
import service.PacienteService;
import ui.forms.FormCuidador;
import ui.forms.FormMedicamento;
import ui.forms.FormPaciente;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de gestion con botones para abrir formularios de creacion.
 */
public class PanelGestion extends JPanel {
    private final PacienteService pacienteService;
    private final CuidadorService cuidadorService;
    private final MedicamentoService medicamentoService;

    public PanelGestion() {
        this.pacienteService = new PacienteService();
        this.cuidadorService = new CuidadorService();
        this.medicamentoService = new MedicamentoService();

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titulo
        JLabel lblTitulo = new JLabel("Gestion de Datos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Seccion Pacientes
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblPacientes = new JLabel("Pacientes");
        lblPacientes.setFont(new Font("Arial", Font.BOLD, 18));
        panelCentral.add(lblPacientes, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        JButton btnNuevoPaciente = crearBoton("+ Nuevo Paciente", new Color(76, 175, 80));
        btnNuevoPaciente.addActionListener(e -> abrirFormPaciente());
        panelCentral.add(btnNuevoPaciente, gbc);

        gbc.gridx = 1;
        JButton btnListarPacientes = crearBoton("Ver Lista de Pacientes", new Color(33, 150, 243));
        btnListarPacientes.addActionListener(e -> mostrarListaPacientes());
        panelCentral.add(btnListarPacientes, gbc);

        // Seccion Cuidadores
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel lblCuidadores = new JLabel("Cuidadores");
        lblCuidadores.setFont(new Font("Arial", Font.BOLD, 18));
        panelCentral.add(lblCuidadores, gbc);

        gbc.gridy = 3; gbc.gridwidth = 1;
        JButton btnNuevoCuidador = crearBoton("+ Nuevo Cuidador", new Color(156, 39, 176));
        btnNuevoCuidador.addActionListener(e -> abrirFormCuidador());
        panelCentral.add(btnNuevoCuidador, gbc);

        gbc.gridx = 1;
        JButton btnListarCuidadores = crearBoton("Ver Lista de Cuidadores", new Color(103, 58, 183));
        btnListarCuidadores.addActionListener(e -> mostrarListaCuidadores());
        panelCentral.add(btnListarCuidadores, gbc);

        // Seccion Medicamentos
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JLabel lblMedicamentos = new JLabel("Medicamentos");
        lblMedicamentos.setFont(new Font("Arial", Font.BOLD, 18));
        panelCentral.add(lblMedicamentos, gbc);

        gbc.gridy = 5; gbc.gridwidth = 1;
        JButton btnNuevoMedicamento = crearBoton("+ Nuevo Medicamento", new Color(255, 152, 0));
        btnNuevoMedicamento.addActionListener(e -> abrirFormMedicamento());
        panelCentral.add(btnNuevoMedicamento, gbc);

        gbc.gridx = 1;
        JButton btnListarMedicamentos = crearBoton("Ver Lista de Medicamentos", new Color(255, 193, 7));
        btnListarMedicamentos.addActionListener(e -> mostrarListaMedicamentos());
        panelCentral.add(btnListarMedicamentos, gbc);

        add(panelCentral, BorderLayout.CENTER);

        // Footer con instrucciones
        JLabel lblInstrucciones = new JLabel(
            "<html><center>Utiliza estos formularios para gestionar los datos basicos del sistema.<br>" +
            "Luego podras configurar pautas de medicacion en la pestana correspondiente.</center></html>"
        );
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstrucciones.setForeground(Color.GRAY);
        lblInstrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblInstrucciones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    private void abrirFormPaciente() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        FormPaciente form = new FormPaciente(parent, pacienteService);
        form.setVisible(true);
    }

    private void abrirFormCuidador() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        FormCuidador form = new FormCuidador(parent, cuidadorService);
        form.setVisible(true);
    }

    private void abrirFormMedicamento() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        FormMedicamento form = new FormMedicamento(parent, medicamentoService);
        form.setVisible(true);
    }

    private void mostrarListaPacientes() {
        try {
            var pacientes = pacienteService.listarTodos();
            if (pacientes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay pacientes registrados todavia.",
                    "Lista vacia",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("<html><body>");
            sb.append("<h2>Pacientes Registrados</h2>");
            sb.append("<table border='1' cellpadding='5'>");
            sb.append("<tr><th>ID</th><th>Nombre</th><th>Edad</th></tr>");

            for (var p : pacientes) {
                sb.append("<tr>");
                sb.append("<td>").append(p.getId()).append("</td>");
                sb.append("<td>").append(p.getNombre()).append("</td>");
                sb.append("<td>").append(p.getEdad()).append("</td>");
                sb.append("</tr>");
            }

            sb.append("</table></body></html>");

            JOptionPane.showMessageDialog(this,
                new JLabel(sb.toString()),
                "Lista de Pacientes",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar pacientes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarListaCuidadores() {
        try {
            var cuidadores = cuidadorService.listarTodos();
            if (cuidadores.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay cuidadores registrados todavia.",
                    "Lista vacia",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("<html><body>");
            sb.append("<h2>Cuidadores Registrados</h2>");
            sb.append("<table border='1' cellpadding='5'>");
            sb.append("<tr><th>ID</th><th>Nombre</th><th>Telefono</th><th>Relacion</th></tr>");

            for (var c : cuidadores) {
                sb.append("<tr>");
                sb.append("<td>").append(c.getId()).append("</td>");
                sb.append("<td>").append(c.getNombre()).append("</td>");
                sb.append("<td>").append(c.getTelefono() != null ? c.getTelefono() : "-").append("</td>");
                sb.append("<td>").append(c.getRelacion() != null ? c.getRelacion() : "-").append("</td>");
                sb.append("</tr>");
            }

            sb.append("</table></body></html>");

            JOptionPane.showMessageDialog(this,
                new JLabel(sb.toString()),
                "Lista de Cuidadores",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar cuidadores: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarListaMedicamentos() {
        try {
            var medicamentos = medicamentoService.listarTodos();
            if (medicamentos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay medicamentos registrados todavia.",
                    "Lista vacia",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("<html><body>");
            sb.append("<h2>Medicamentos Registrados</h2>");
            sb.append("<table border='1' cellpadding='5'>");
            sb.append("<tr><th>ID</th><th>Nombre</th><th>Via</th><th>Unidad Dosis</th></tr>");

            for (var m : medicamentos) {
                sb.append("<tr>");
                sb.append("<td>").append(m.getId()).append("</td>");
                sb.append("<td>").append(m.getNombreComercial()).append("</td>");
                sb.append("<td>").append(m.getVia() != null ? m.getVia() : "-").append("</td>");
                sb.append("<td>").append(m.getUnidadDosis() != null ? m.getUnidadDosis() : "-").append("</td>");
                sb.append("</tr>");
            }

            sb.append("</table></body></html>");

            JOptionPane.showMessageDialog(this,
                new JLabel(sb.toString()),
                "Lista de Medicamentos",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar medicamentos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

