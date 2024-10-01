/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication1;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.BoxLayout;

public class Simulacion extends JFrame {
    private JComboBox<String> categoriaComboBox;
    private JComboBox<String> servicioComboBox;
    private JTextField cedulaTextField;
    private JLabel horaLabel;
    private JLabel contadorLabel;
    private JSlider velocidadSlider;
    private List<Paciente> cola;
    private int contadorPacientes;
    private Random random;
    private JLabel proximoPacienteLabel;
    private JPanel colaPanel;
    private boolean atendiendoPaciente = false;

    public Simulacion() {
        cola = new ArrayList<>();
        contadorPacientes = 0;
        random = new Random();

        JPanel ingresoPanel = new JPanel();
        ingresoPanel.setLayout(new GridLayout(5, 2));
        ingresoPanel.add(new JLabel("Cédula:"));
        cedulaTextField = new JTextField(10);
        ingresoPanel.add(cedulaTextField);
        ingresoPanel.add(new JLabel("Categoría:"));
        categoriaComboBox = new JComboBox<>(new String[]{"Persona menor a 60 años", "Adulto mayor", "Persona con discapacidad"});
        ingresoPanel.add(categoriaComboBox);
        ingresoPanel.add(new JLabel("Servicio:"));
        servicioComboBox = new JComboBox<>(new String[]{"Consulta médica general", "Consulta médica especializada", "Consulta de laboratorio", "Imágenes de diagnóstico"});
        ingresoPanel.add(servicioComboBox);
       

        JButton registrarButton = new JButton("Registrar");
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPaciente();
            }
        });
        ingresoPanel.add(registrarButton);

 
        colaPanel = new JPanel();
        colaPanel.setLayout(new BoxLayout(colaPanel, BoxLayout.Y_AXIS));
        colaPanel.add(new JLabel("Pacientes en cola:"));
        add(colaPanel, BorderLayout.CENTER);


        JPanel proximoPacientePanel = new JPanel();
        proximoPacienteLabel = new JLabel("Proximo paciente:");
        proximoPacientePanel.add(proximoPacienteLabel);
        add(proximoPacientePanel, BorderLayout.EAST);


        velocidadSlider = new JSlider(1, 5, 3); // Velocidad de 1 (más rápida) a 5 (más lenta)
        velocidadSlider.setMajorTickSpacing(1);
        velocidadSlider.setPaintTicks(true);
        velocidadSlider.setPaintLabels(true);
        JPanel velocidadPanel = new JPanel();
        velocidadPanel.add(new JLabel("Velocidad de atención:"));
        velocidadPanel.add(velocidadSlider);
        add(velocidadPanel, BorderLayout.SOUTH);
       

        add(ingresoPanel, BorderLayout.WEST);
       

        horaLabel = new JLabel("00:00:00");
        contadorLabel = new JLabel("0 pacientes");
        add(horaLabel, BorderLayout.NORTH);
        add(contadorLabel, BorderLayout.NORTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void registrarPaciente() {
        try {
    
            String cedulaInput = cedulaTextField.getText();
            if (!cedulaInput.matches("\\d{10}")) {
                throw new NumberFormatException("La cédula debe ser un número de 10 dígitos.");
            }
            int cedula = Integer.parseInt(cedulaInput);
            String categoria = (String) categoriaComboBox.getSelectedItem();
            String servicio = (String) servicioComboBox.getSelectedItem();
            Date horaLlegada = new Date();
            Paciente paciente = new Paciente(cedula, categoria, servicio, horaLlegada);
            cola.add(paciente);
            contadorPacientes++;
            contadorLabel.setText(contadorPacientes + " pacientes");

            actualizarColaVisual();


            if (contadorPacientes >= 10 && !atendiendoPaciente) {
                iniciarAtencion();
            }
        } catch (NumberFormatException ex) {
    
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void iniciarAtencion() {

        new Thread(() -> {
            while (!cola.isEmpty()) {
                atendiendoPaciente = true;
                Paciente paciente = cola.remove(0);
                long tiempoAtencion = (random.nextInt(30) + 15) * 1000; // 15 a 45 minutos simulados (escalados a segundos)
                tiempoAtencion /= 60; // Un minuto de simulación = 1 segundo real
                long inicioAtencion = System.currentTimeMillis();
                long finAtencion = inicioAtencion + tiempoAtencion * ajustarVelocidad(); // Aplicar el ajuste de velocidad

                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long tiempoRestante = finAtencion - System.currentTimeMillis();
                        if (tiempoRestante <= 0) {
                            ((Timer) e.getSource()).stop();
                            actualizarColaVisual();
                            if (!cola.isEmpty()) {
                                iniciarAtencion();
                            } else {
                                proximoPacienteLabel.setText("No hay pacientes en cola.");
                                atendiendoPaciente = false;
                            }
                        } else {
                            long minutosRestantes = tiempoRestante / 60000;
                            long segundosRestantes = (tiempoRestante % 60000) / 1000;
                            proximoPacienteLabel.setText("Paciente atendido: " + paciente.getCedula() +
                                ", faltan: " + minutosRestantes + "m " + segundosRestantes + "s");
                        }
                    }
                });
                timer.start();
                try {
                    Thread.sleep(tiempoAtencion);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void actualizarColaVisual() {
        colaPanel.removeAll();
        colaPanel.add(new JLabel("Pacientes en cola:"));
        for (Paciente p : cola) {
            JLabel pacienteLabel = new JLabel("Cédula: " + p.getCedula() +
                    ", Categoria: " + p.getCategoria() +
                    ", Servicio: " + p.getServicio() +
                    ", Hora de llegada: " + new SimpleDateFormat("HH:mm").format(p.getHoraLlegada()));
            colaPanel.add(pacienteLabel);
        }
        colaPanel.revalidate();
        colaPanel.repaint();
    }

    private int ajustarVelocidad() {
        int velocidad = velocidadSlider.getValue();
        return velocidad; // Escala la velocidad según el valor del slider (1 es la más rápida, 5 la más lenta)
    }

    public static void main(String[] args) {
        new Simulacion();
    }
}

class Paciente {
    private int cedula;
    private String categoria;
    private String servicio;
    private Date horaLlegada;

    public Paciente(int cedula, String categoria, String servicio, Date horaLlegada) {
        this.cedula = cedula;
        this.categoria = categoria;
        this.servicio = servicio;
        this.horaLlegada = horaLlegada;
    }

    public int getCedula() {
        return cedula;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getServicio() {
        return servicio;
    }

    public Date getHoraLlegada() {
        return horaLlegada;
    }
}
