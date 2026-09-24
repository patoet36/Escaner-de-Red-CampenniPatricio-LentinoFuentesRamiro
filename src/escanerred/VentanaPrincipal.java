package escanerred;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.LineBorder;


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
    JComboBox<String> filtro;
    TableRowSorter<DefaultTableModel> ordenador;

    public VentanaPrincipal() {

        setTitle("Escáner de Red");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        // -------------------------
        // CAMPOS
        // -------------------------

        JPanel campos =
            new JPanel(
                new GridLayout(4, 2)
            );

        campos.add(
            new JLabel("IP de inicio:")
        );

        ipInicio =
            new JTextField("192.168.1.1");

        campos.add(ipInicio);

        campos.add(
            new JLabel("IP de fin:")
        );

        ipFin =
            new JTextField("192.168.1.5");

        campos.add(ipFin);
        
        validarMientrasEscribe(ipInicio);
        validarMientrasEscribe(ipFin);

        campos.add(
            new JLabel("Tiempo de espera (ms):")
        );

        espera =
            new JTextField("1000");

        campos.add(espera);

        campos.add(
            new JLabel("Número de reintentos:")
        );

        reintentos =
            new JTextField("1");

        campos.add(reintentos);

        add(
            campos,
            BorderLayout.NORTH
        );

        // -------------------------
        // TABLA
        // -------------------------

        DefaultTableModel modelo =
            new DefaultTableModel(
                new Object[]{
                    "IP",
                    "Nombre",
                    "Activo",
                    "Tiempo (ms)"
                },
                0
            );

        tabla = new JTable(modelo);

        ordenador =
            new TableRowSorter<DefaultTableModel>(
                modelo
            );

        tabla.setRowSorter(ordenador);

        add(
            new JScrollPane(tabla),
            BorderLayout.CENTER
        );

        // -------------------------
        // BOTONES
        // -------------------------

        JButton iniciar =
            new JButton("Iniciar escaneo");

        JButton detener =
            new JButton("Detener");

        JButton limpiar =
            new JButton("Limpiar");

        JButton guardar =
            new JButton("Guardar");

        etiquetaActivos =
            new JLabel("Activos: 0");
        
        filtro =
        	    new JComboBox<>(
        	        new String[]{
        	            "Todos",
        	            "Activos",
        	            "Inactivos"
        	        }
        	    );

        JPanel botones =
            new JPanel();

        botones.add(iniciar);
        botones.add(detener);
        botones.add(limpiar);
        botones.add(guardar);
        botones.add(etiquetaActivos);
        botones.add(filtro);
        
        filtro.addActionListener(e -> {

            String opcion =
                (String) filtro.getSelectedItem();

            if (opcion.equals("Todos")) {

                ordenador.setRowFilter(null);

            } else if (opcion.equals("Activos")) {

                ordenador.setRowFilter(
                    RowFilter.regexFilter(
                        "true",
                        2
                    )
                );

            } else {

                ordenador.setRowFilter(
                    RowFilter.regexFilter(
                        "false",
                        2
                    )
                );
            }
        });

        progreso =
        	    new JProgressBar(0, 100);

        	progreso.setStringPainted(true);

        	JPanel inferior =
        	    new JPanel(
        	        new BorderLayout()
        	    );

        	inferior.add(
        	    botones,
        	    BorderLayout.NORTH
        	);

        	inferior.add(
        	    progreso,
        	    BorderLayout.SOUTH
        	);

        	add(
        	    inferior,
        	    BorderLayout.SOUTH
        	);

        // -------------------------
        // EVENTOS
        // -------------------------

        iniciar.addActionListener(
            e -> iniciarEscaneo()
        );

        detener.addActionListener(e -> {

            escaneoDetenido = true;

            if (trabajador != null) {

                trabajador.cancel(true);
            }
        });

        limpiar.addActionListener(
            e -> limpiarTabla()
        );

        guardar.addActionListener(
            e -> guardarResultados()
        );
    }

    // ==================================================
    // INICIAR ESCANEO
    // ==================================================

    private void iniciarEscaneo() {
    	
    	if (trabajador != null && !trabajador.isDone()) {

    	    JOptionPane.showMessageDialog(
    	        this,
    	        "Ya hay un escaneo en curso."
    	    );

    	    return;
    	}

        // Validar IP

        if (!validarIP(ipInicio.getText()) ||
            !validarIP(ipFin.getText())) {

            JOptionPane.showMessageDialog(
                this,
                "Ingresá direcciones IP válidas."
            );

            return;
        }

        String inicio =
            ipInicio.getText();

        String fin =
            ipFin.getText();

        // Validar tiempo y reintentos

        int tiempoEspera;
        int cantidadReintentos;

        try {

            tiempoEspera =
                Integer.parseInt(
                    espera.getText()
                );

            cantidadReintentos =
                Integer.parseInt(
                    reintentos.getText()
                );

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

        // Validar rango de IP

        String baseInicio =
            inicio.substring(
                0,
                inicio.lastIndexOf(".")
            );

        String baseFin =
            fin.substring(
                0,
                fin.lastIndexOf(".")
            );

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
            (DefaultTableModel)
            tabla.getModel();

        modelo.setRowCount(0);

        escaneoDetenido = false;

        equiposActivos = 0;

        etiquetaActivos.setText(
            "Activos: 0"
        );

        int total =
            numeroFin -
            numeroInicio +
            1;

        // -------------------------
        // SWING WORKER
        // -------------------------

        trabajador =
            new SwingWorker<Void, Equipo>() {

            protected Void doInBackground() {

                EscanerRed escaner =
                    new EscanerRed();

                int actual = 0;

                for (
                    int i = numeroInicio;
                    i <= numeroFin &&
                    !escaneoDetenido;
                    i++
                ) {

                    String ip =
                        base + i;

                    Equipo equipo =
                        escaner.escanear(
                            ip,
                            tiempoEspera,
                            cantidadReintentos
                        );

                    publish(equipo);

                    actual++;

                    int porcentaje =
                        (actual * 100) /
                        total;

                    setProgress(
                        porcentaje
                    );
                }

                return null;
            }

            protected void process(
            	    java.util.List<Equipo> equipos
            	) {

            	    for (
            	        Equipo equipo :
            	        equipos
            	    ) {

            	        modelo.addRow(
            	            new Object[]{
            	                equipo.ip,
            	                equipo.nombre,
            	                equipo.activo,
            	                equipo.tiempo
            	            }
            	        );

            	        if (equipo.activo) {

            	            equiposActivos++;
            	        }
            	    }

            	    etiquetaActivos.setText(
            	        "Activos: " +
            	        equiposActivos
            	    );

            	    progreso.setValue(
            	        getProgress()
            	    );
            	}
        };

        trabajador.execute();
    }

    // ==================================================
    // LIMPIAR
    // ==================================================

    private void limpiarTabla() {

        DefaultTableModel modelo =
            (DefaultTableModel)
            tabla.getModel();

        modelo.setRowCount(0);

        progreso.setValue(0);

        equiposActivos = 0;

        etiquetaActivos.setText(
            "Activos: 0"
        );
    }

    // ==================================================
    // GUARDAR CSV
    // ==================================================

    private void guardarResultados() {

        try {

            JFileChooser archivo =
                new JFileChooser();

            if (
                archivo.showSaveDialog(this) ==
                JFileChooser.APPROVE_OPTION
            ) {
            	if (tabla.getRowCount() == 0) {

            	    JOptionPane.showMessageDialog(
            	        this,
            	        "No hay resultados para guardar."
            	    );

            	    return;
            	}

                PrintWriter escritor =
                    new PrintWriter(
                        new FileWriter(
                            archivo
                                .getSelectedFile()
                                + ".csv"
                        )
                    );

                escritor.println(
                    "IP,Nombre,Activo,Tiempo"
                );

                for (
                    int fila = 0;
                    fila < tabla.getRowCount();
                    fila++
                ) {

                    escritor.println(
                        tabla.getValueAt(
                            fila, 0
                        )
                        + "," +
                        tabla.getValueAt(
                            fila, 1
                        )
                        + "," +
                        tabla.getValueAt(
                            fila, 2
                        )
                        + "," +
                        tabla.getValueAt(
                            fila, 3
                        )
                    );
                }

                escritor.close();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "No se pudieron guardar los resultados."
            );
        }
    }

    // ==================================================
    // VALIDAR IP
    // ==================================================

    private boolean validarIP(String ip) {

        String[] partes =
            ip.split("\\.");

        if (partes.length != 4) {

            return false;
        }

        try {

            for (String parte : partes) {

                int numero =
                    Integer.parseInt(parte);

                if (
                    numero < 0 ||
                    numero > 255
                ) {

                    return false;
                }
            }

            return true;

        } catch (Exception e) {

            return false;
        }
    }
    private void validarMientrasEscribe(JTextField campo) {

        campo.getDocument().addDocumentListener(
            new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    revisar();
                }

                public void removeUpdate(DocumentEvent e) {
                    revisar();
                }

                public void changedUpdate(DocumentEvent e) {
                    revisar();
                }

                private void revisar() {

                    if (validarIP(campo.getText())) {

                        campo.setBorder(null);

                    } else {

                        campo.setBorder(
                            new LineBorder(Color.RED, 2)
                        );
                    }
                }
            }
        );
    }
}