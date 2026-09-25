import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraGUI extends JFrame implements ActionListener {
    private JTextField pantalla;
    private double num1 = 0, num2 = 0, resultado = 0;
    private char operador;

    public CalculadoraGUI() {
        // Configuración de la ventana principal
        setTitle("Calculadora Java");
        setSize(320, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // Pantalla de texto (Display)
        pantalla = new JTextField();
        pantalla.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        pantalla.setBackground(Color.WHITE);
        pantalla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(pantalla, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 4, 8, 8));

        String[] distribucionBotones = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        for (String texto : distribucionBotones) {
            JButton boton = new JButton(texto);
            boton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            boton.setFocusable(false);
            boton.addActionListener(this);
            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        // Si se presiona un número (0-9)
        if (comando.charAt(0) >= '0' && comando.charAt(0) <= '9') {
            pantalla.setText(pantalla.getText() + comando);
        } 
        // Si se presiona Limpiar ('C')
        else if (comando.equals("C")) {
            pantalla.setText("");
            num1 = num2 = resultado = 0;
        } 
        // Si se presiona Igual ('=')
        else if (comando.equals("=")) {
            if (pantalla.getText().isEmpty()) return;
            num2 = Double.parseDouble(pantalla.getText());

            switch (operador) {
                case '+': resultado = num1 + num2; break;
                case '-': resultado = num1 - num2; break;
                case '*': resultado = num1 * num2; break;
                case '/': 
                    if (num2 != 0) {
                        resultado = num1 / num2;
                    } else {
                        pantalla.setText("Error: Div 0");
                        return;
                    }
                    break;
            }
            pantalla.setText(String.valueOf(resultado));
        } 
        // Si se presiona un operador (+, -, *, /)
        else {
            if (pantalla.getText().isEmpty()) return;
            num1 = Double.parseDouble(pantalla.getText());
            operador = comando.charAt(0);
            pantalla.setText("");
        }
    }

    public static void main(String[] args) {
        // Lanzar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI calc = new CalculadoraGUI();
            calc.setVisible(true);
        });
    }
}