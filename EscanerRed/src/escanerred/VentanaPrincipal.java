package escanerred;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

public class VentanaPrincipal extends JFrame {

    JTextField ipInicio;
    JTextField ipFin;
    JTextField espera;
    JTextField reintentos;

    JTable tabla;
    JProgressBar progreso;
    boolean escaneoDetenido = false;
    SwingWorker<Void, Equipo> trabajador;
    int equiposActivos = 0;
    JLabel etiquetaActivos;

    public VentanaPrincipal() {

        setTitle("Escáner de Red");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());


        // CAMPOS

        JPanel datos = new JPanel(new GridLayout(4, 2));

        datos.add(new JLabel("IP de inicio:"));
        ipInicio = new JTextField("192.168.1.1");
        datos.add(ipInicio);

        datos.add(new JLabel("IP de fin:"));
        ipFin = new JTextField("192.168.1.5");
        datos.add(ipFin);

        datos.add(new JLabel("Tiempo de espera (ms):"));
        espera = new JTextField("1000");
        datos.add(espera);

        datos.add(new JLabel("Número de reintentos:"));
        reintentos = new JTextField("1");
        datos.add(reintentos);

        panel.add(datos, BorderLayout.NORTH);


        // TABLA

        String[] columnas = {
            "IP",
            "Nombre",
            "Activo",
            "Tiempo (ms)"
        };

        DefaultTableModel modelo =
            new DefaultTableModel(columnas, 0);

        tabla = new JTable(modelo);

        panel.add(
            new JScrollPane(tabla),
            BorderLayout.CENTER
        );


        // BOTONES

        JPanel botones = new JPanel();

        JButton iniciar =
            new JButton("Iniciar escaneo");

        JButton detener =
            new JButton("Detener");

        JButton limpiar =
            new JButton("Limpiar");

        JButton guardar =
            new JButton("Guardar");

        botones.add(iniciar);
        botones.add(detener);
        botones.add(limpiar);
        botones.add(guardar);
        
        limpiar.addActionListener(e -> limpiarTabla());
        guardar.addActionListener(e -> guardarResultados());
        panel.add(etiquetaActivos);
        panel.add(botones, BorderLayout.SOUTH);


        // BARRA DE PROGRESO

        progreso = new JProgressBar(0, 100);
        progreso.setStringPainted(true);
        etiquetaActivos = new JLabel("Activos: 0");

        panel.add(progreso, BorderLayout.WEST);


        // EVENTO DEL BOTÓN

        iniciar.addActionListener(e -> iniciarEscaneo());
        detener.addActionListener(e -> {

            escaneoDetenido = true;

            if (trabajador != null) {
                trabajador.cancel(true);
            }
        });


        add(panel);
    }


    private void iniciarEscaneo() {
    	
    	if (!validarIP(ipInicio.getText()) ||
    		    !validarIP(ipFin.getText())) {

    		    javax.swing.JOptionPane.showMessageDialog(
    		        this,
    		        "Ingresá direcciones IP válidas."
    		    );

    		    return;
    		}

        String inicio = ipInicio.getText();
        String fin = ipFin.getText();

        int tiempoEspera;
        int cantidadReintentos;

        try {

            tiempoEspera =
                Integer.parseInt(espera.getText());

            cantidadReintentos =
                Integer.parseInt(reintentos.getText());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "El tiempo de espera y los reintentos deben ser números."
            );

            return;
        }

        if (tiempoEspera <= 0) {

            JOptionPane.showMessageDialog(
                this,
                "El tiempo de espera debe ser mayor a 0."
            );

            return;
        }

        if (cantidadReintentos < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Los reintentos no pueden ser negativos."
            );

            return;
        }

        String baseInicio =
        	    inicio.substring(0, inicio.lastIndexOf("."));

        	String baseFin =
        	    fin.substring(0, fin.lastIndexOf("."));

        	if (!baseInicio.equals(baseFin)) {

        	    JOptionPane.showMessageDialog(
        	        this,
        	        "Las IP deben pertenecer al mismo rango."
        	    );

        	    return;
        	}

        	int numeroInicio =
        	    Integer.parseInt(
        	        inicio.substring(
        	            inicio.lastIndexOf(".") + 1
        	        )
        	    );

        	int numeroFin =
        	    Integer.parseInt(
        	        fin.substring(
        	            fin.lastIndexOf(".") + 1
        	        )
        	    );

        	if (numeroInicio > numeroFin) {

        	    JOptionPane.showMessageDialog(
        	        this,
        	        "La IP inicial no puede ser mayor que la IP final."
        	    );

        	    return;
        	}

        String base =
            inicio.substring(
                0,
                inicio.lastIndexOf(".") + 1
            );

        DefaultTableModel modelo =
            (DefaultTableModel) tabla.getModel();

        modelo.setRowCount(0);

        escaneoDetenido = false;
        equiposActivos = 0;
        etiquetaActivos.setText("Activos: 0");

        int total = numeroFin - numeroInicio + 1;

        trabajador = new SwingWorker<Void, Equipo>() {

            protected Void doInBackground() {

                EscanerRed escaner =
                    new EscanerRed();

                int actual = 0;

                for (int i = numeroInicio;
                     i <= numeroFin && !escaneoDetenido;
                     i++) {

                    String ip = base + i;

                    Equipo equipo =
                        escaner.escanear(
                            ip,
                            tiempoEspera,
                            cantidadReintentos
                        );

                    publish(equipo);

                    actual++;

                    int porcentaje =
                        (actual * 100) / total;

                    setProgress(porcentaje);
                }

                return null;
            }


            protected void process(
                java.util.List<Equipo> equipos) {

                for (Equipo equipo : equipos) {

                    modelo.addRow(
                        new Object[]{
                            equipo.ip,
                            equipo.nombre,
                            equipo.activo,
                            equipo.tiempo
                        }
                    );
                }

                progreso.setValue(getProgress());
            }
        };

        trabajador.execute();
    }
    private void limpiarTabla() {

        DefaultTableModel modelo =
            (DefaultTableModel) tabla.getModel();

        modelo.setRowCount(0);

        progreso.setValue(0);
    }
    private void guardarResultados() {

        try {

            JFileChooser archivo = new JFileChooser();

            if (archivo.showSaveDialog(this) ==
                JFileChooser.APPROVE_OPTION) {

                PrintWriter escritor =
                    new PrintWriter(
                        new FileWriter(
                            archivo.getSelectedFile() + ".csv"
                        )
                    );

                escritor.println("IP,Nombre,Activo,Tiempo");

                for (int fila = 0; fila < tabla.getRowCount(); fila++) {

                    escritor.println(
                        tabla.getValueAt(fila, 0) + "," +
                        tabla.getValueAt(fila, 1) + "," +
                        tabla.getValueAt(fila, 2) + "," +
                        tabla.getValueAt(fila, 3)
                    );
                }

                escritor.close();
            }

        } catch (Exception e) {

            System.out.println("Error al guardar");
        }
    }
    private boolean validarIP(String ip) {

        String[] partes = ip.split("\\.");

        if (partes.length != 4) {
            return false;
        }

        try {

            for (String parte : partes) {

                int numero = Integer.parseInt(parte);

                if (numero < 0 || numero > 255) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}