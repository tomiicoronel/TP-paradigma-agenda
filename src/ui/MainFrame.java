package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Agenda Accesible");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Hoy", new JPanel(new BorderLayout()));       // TODO
        tabs.addTab("Medicación", new JPanel(new BorderLayout())); // TODO
        tabs.addTab("Rutina", new JPanel(new BorderLayout()));     // TODO
        tabs.addTab("Historial", new JPanel(new BorderLayout()));  // TODO

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
