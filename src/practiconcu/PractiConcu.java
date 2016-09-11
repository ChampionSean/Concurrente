/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practiconcu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

/**
 *
 * @author Marco Antonio Estrada Robles
 * @author Gerardo González Gutiérrez
 */
public class PractiConcu extends JPanel
        implements ActionListener,
        PropertyChangeListener {

    //Monitor de progreso
    private ProgressMonitor progressMonitor;
    //Botón que iniciará la ejecucuión
    private final JButton botonInicio;
    //Área para visualización del mensaje
    private final JTextArea parar;
    //Tarea a supervisar
    private Task tarea;
    //Thread sobre el cual se realiza el proceso
    private Thread t;
    
    class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            Random random = new Random();
            int i = 0;
            setProgress(0);
            try {
                Thread.sleep(1000);
                while (i < 100 && !isCancelled()) {
                    Thread.sleep(random.nextInt(1000));
                    i += random.nextInt(10);
                    setProgress(Math.min(i, 100));
                }
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            botonInicio.setEnabled(true);
            progressMonitor.setProgress(0);
        }
    }

    public PractiConcu() {
        super(new BorderLayout());

        botonInicio = new JButton("Empezar");
        botonInicio.setActionCommand("Empezar");
        botonInicio.addActionListener(this);

        parar = new JTextArea(7, 25);
        parar.setMargin(new Insets(7, 7, 7, 7));
        parar.setEditable(false);

        add(botonInicio, BorderLayout.PAGE_START);
        add(new JScrollPane(parar), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        progressMonitor = new ProgressMonitor(PractiConcu.this,
                "Ejecutando tarea",
                "", 0, 100);
        progressMonitor.setProgress(0);
        tarea = new Task();
        tarea.addPropertyChangeListener(this);
        tarea.execute();
        botonInicio.setEnabled(false);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int i = (Integer) evt.getNewValue();
            progressMonitor.setProgress(i);
            String m
                    = String.format("Completado %d%%.\n", i);
            progressMonitor.setNote(m);
            parar.append(m);
            if (progressMonitor.isCanceled() || tarea.isDone()) {
                Toolkit.getDefaultToolkit().beep();
                if (progressMonitor.isCanceled()) {
                    tarea.cancel(true);
                    parar.append("Tarea cancelada.\n");
                } else {
                    parar.append("Tarea completada.\n");
                }
                botonInicio.setEnabled(true);
            }
        }

    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TareaMonitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent p = new PractiConcu();
        p.setOpaque(true); //content panes must be opaque
        frame.setContentPane(p);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
