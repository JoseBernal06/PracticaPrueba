import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MostrarDatos {

    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Buscar datos del estudiante");
        ventana.setSize(400, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new FlowLayout());

        // Componentes
        JLabel etiqueta = new JLabel("Ingrese el ID del estudiante:");
        JTextField campoID = new JTextField(20);
        JButton boton = new JButton("Buscar estudiante");

        // Acción del botón
        boton.addActionListener(e -> {
            String idIngresado = campoID.getText().trim();
            if (idIngresado.isEmpty()) {
                JOptionPane.showMessageDialog(ventana, "Por favor, ingrese un ID válido.");
            } else {
                // Aquí se hace la consulta al servidor UDP
                try {
                    DatagramSocket socketCliente = new DatagramSocket();
                    InetAddress ipServidor = InetAddress.getByName("172.29.19.236"); // IP del servidor
                    int puertoServidor = 8000;

                    byte[] bufferEnvio = idIngresado.getBytes();
                    DatagramPacket paqueteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ipServidor, puertoServidor);
                    socketCliente.send(paqueteEnvio);

                    // Esperar respuesta del servidor
                    byte[] bufferRespuesta = new byte[1024];
                    DatagramPacket paqueteRespuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);
                    socketCliente.receive(paqueteRespuesta);

                    String respuesta = new String(paqueteRespuesta.getData(), 0, paqueteRespuesta.getLength());

                    JOptionPane.showMessageDialog(ventana, respuesta, "Datos del Estudiante", JOptionPane.INFORMATION_MESSAGE);

                    socketCliente.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventana, "Error al contactar con el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Añadir componentes a la ventana
        ventana.add(etiqueta);
        ventana.add(campoID);
        ventana.add(boton);

        // Mostrar ventana
        ventana.setVisible(true);
    }
}

