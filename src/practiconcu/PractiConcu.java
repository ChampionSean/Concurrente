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
    private ProgressMonitor progressMonitor2;
    private ProgressMonitor progressMonitor3;
    //Botón que iniciará la ejecucuión
    private final JButton botonInicio;
    //Área para visualización del mensaje
    private final JTextArea parar;
    //Tarea a supervisar
    private Task tarea;
    
    private Task tarea2;
    private Task tarea3;
     private Thread t,h,i;        
            
    
    class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            Random random = new Random();
            int i = 0;
            setProgress(0);
           Thread m = new Thread(new Runnable(){
               public void run(){
                  
                }
                   
               
            });
            try {
                m.sleep(1000);
                while (i < 100 && !isCancelled()) {
                    m.sleep(random.nextInt(1000));
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
        progressMonitor2 = new ProgressMonitor(PractiConcu.this,
                "Ejecutando tarea",
                "", 0, 100);
        progressMonitor3 = new ProgressMonitor(PractiConcu.this,
                "Ejecutando tarea",
                "", 0, 100);
        progressMonitor.setProgress(0);
        progressMonitor2.setProgress(0);
        progressMonitor3.setProgress(0);
        tarea = new Task();
        tarea2 = new Task();
        tarea3 = new Task();
        tarea.addPropertyChangeListener(this);
        tarea2.addPropertyChangeListener(this);
        tarea3.addPropertyChangeListener(this);
        tarea.execute();
        tarea2.execute();
        tarea3.execute();
        botonInicio.setEnabled(false);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int i = (Integer) evt.getNewValue();
            progressMonitor.setProgress(tarea.getProgress());
            progressMonitor2.setProgress(tarea2.getProgress());
            progressMonitor3.setProgress(tarea3.getProgress());
            String m= String.format("Completado proceso1 %d%%.\n", tarea.getProgress());
            String n= String.format("Completado proceso2 %d%%.\n", tarea2.getProgress());
            String k= String.format("Completado proceso3 %d%%.\n", tarea3.getProgress());
            progressMonitor.setNote(m);
            progressMonitor2.setNote(n);
            progressMonitor3.setNote(k);
            parar.append(m);
            parar.append(n);
            parar.append(k);
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
        JFrame frame = new JFrame("TareaMonitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent p = new PractiConcu();
        p.setOpaque(true); 
        frame.setContentPane(p);

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
