import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TodoListApp extends JFrame {
	private DefaultListModel modeloLista;
	private JList listaTareas;
	private JTextField campoTarea;
	private JButton botonAgregar, botonCompletar, botonEliminar;
	
	// Archivo donde se guardarán las tareas
	private final String ARCHIVO_DATOS = "tareas.txt";

	public TodoListApp() {
		// Configuración de la ventana principal
		setTitle("Gestor de Tareas (To-Do List)");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Estilos de colores
		Color colorPrincipal = new Color(52, 152, 219);
		Color colorFondo = new Color(245, 247, 250);

		// --- PANEL SUPERIOR: Entrada de Texto ---
		JPanel panelEntrada = new JPanel(new BorderLayout(8, 8));
		panelEntrada.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
		panelEntrada.setBackground(colorFondo);

		campoTarea = new JTextField();
		campoTarea.setFont(new Font("Segoe UI", Font.PLAIN, 15));

		botonAgregar = new JButton("Agregar");
		botonAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
		botonAgregar.setBackground(colorPrincipal);
		botonAgregar.setForeground(Color.WHITE);
		botonAgregar.setFocusable(false);

		panelEntrada.add(campoTarea, BorderLayout.CENTER);
		panelEntrada.add(botonAgregar, BorderLayout.EAST);

		add(panelEntrada, BorderLayout.NORTH);

		// --- PANEL CENTRAL: Lista de Tareas ---
        modeloLista = new DefaultListModel<>();
        listaTareas = new JList<>(modeloLista);
        listaTareas.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        listaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTareas.setFixedCellHeight(35);

        JScrollPane scrollPane = new JScrollPane(listaTareas);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Mis Tareas "));

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        panelCentro.setBackground(colorFondo);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

		// --- PANEL INFERIOR: Botones de Acción ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(colorFondo);

        botonCompletar = new JButton("✓ Marcar Completada");
        botonCompletar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        botonCompletar.setFocusable(false);

        botonEliminar = new JButton("🗑 Eliminar");
        botonEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        botonEliminar.setBackground(new Color(231, 76, 60));
        botonEliminar.setForeground(Color.WHITE);
        botonEliminar.setFocusable(false);

        panelInferior.add(botonCompletar);
        panelInferior.add(botonEliminar);

        add(panelInferior, BorderLayout.SOUTH);

        // --- EVENTOS ---
        botonAgregar.addActionListener(e -> agregarTarea());
        campoTarea.addActionListener(e -> agregarTarea());
        botonCompletar.addActionListener(e -> marcarCompletada());
        botonEliminar.addActionListener(e -> eliminarTarea());

        // Cargar las tareas al iniciar
        cargarTareasDesdeArchivo();
	}
	private void agregarTarea() {
        String texto = campoTarea.getText().trim();
        if (!texto.isEmpty()) {
            modeloLista.addElement("[ ] " + texto);
            campoTarea.setText("");
            guardarTareasEnArchivo();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor escribe una tarea.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void marcarCompletada() {
        int indice = listaTareas.getSelectedIndex();
        if (indice != -1) {
            String tareaActual = modeloLista.getElementAt(indice).toString();
            if (tareaActual.startsWith("[ ] ")) {
                String tareaCompletada = "[✓] " + tareaActual.substring(4);
                modeloLista.set(indice, tareaCompletada);
            }
            guardarTareasEnArchivo();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea de la lista.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarTarea() {
        int indice = listaTareas.getSelectedIndex();
        if (indice != -1) {
            modeloLista.remove(indice);
            guardarTareasEnArchivo();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // --- PERSISTENCIA EN ARCHIVO TXT ---
    private void guardarTareasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (int i = 0; i < modeloLista.getSize(); i++) {
                writer.write(modeloLista.getElementAt(i).toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar tareas: " + e.getMessage());
        }
    }

    private void cargarTareasDesdeArchivo() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                modeloLista.addElement(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar tareas: " + e.getMessage());
        }
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			TodoListApp app = new TodoListApp();
			app.setVisible(true);
		});
	}
}