package Controler;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.FracassoNotification;
import Modelo.Hero;
import Modelo.SuccessoNotification;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class TelaView extends JFrame {
    private TelaController controller;
    private CameraManager cameraManager;
    private Graphics g2;
    
    public TelaView() {
        Desenho.setCenario(this);
        initComponents();
        
        // Set size before creating buffer strategy
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
        
        this.setVisible(true);
        this.createBufferStrategy(2);
    }
    
    public void setController(TelaController controller) {
        this.controller = controller;
        this.addMouseListener(controller);
        this.addKeyListener(controller);
    }
    
    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }
    
    public Graphics getGraphicsBuffer() {
        return g2;
    }
    
    // Add these methods to make it compatible with Desenho.java and other classes
    public int getCameraLinha() {
        return cameraManager.getCameraLinha();
    }
    
    public int getCameraColuna() {
        return cameraManager.getCameraColuna();
    }
    
    public boolean ehPosicaoValida(auxiliar.Posicao p) {
        return controller.ehPosicaoValida(p);
    }
    
    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                // Update the notifications
                SuccessoNotification.getInstance().update();
                FracassoNotification.getInstance().update();
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }
    
    public void paint(Graphics gOld) {
        if (getBufferStrategy() == null) {
            // Create it if it doesn't exist yet
            this.createBufferStrategy(2);
            return;
        }
        
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /**
         * ***********Desenha cenário de fundo*************
         */
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraManager.getCameraLinha() + i;
                int mapaColuna = cameraManager.getCameraColuna() + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "Ice_BG.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(TelaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        if (controller.getFaseAtual() != null && !controller.getFaseAtual().isEmpty()) {
            controller.getControleDeJogo().desenhaTudo(controller.getFaseAtual());
            controller.getControleDeJogo().processaTudo(controller.getFaseAtual());
        }
        
        // Check if game is over to ensure notification is visible
        if (Hero.isGameOver()) {
            System.out.println("TelaView: Game is over, should show notification");
        }
        
        // Draw the success notification on top of everything
        if (SuccessoNotification.getInstance().isVisible()) {
            System.out.println("TelaView: Rendering success notification");
            SuccessoNotification.getInstance().render(g2, getWidth() - getInsets().right, getHeight() - getInsets().top);
        }
        
        // Draw the failure notification on top of everything
        if (FracassoNotification.getInstance().isVisible()) {
            System.out.println("TelaView: Rendering failure notification");
            FracassoNotification.getInstance().render(g2, getWidth() - getInsets().right, getHeight() - getInsets().top);
        } else if (Hero.isGameOver()) {
            // If game is over but notification is not visible, force show it
            System.out.println("TelaView: Game over but notification not visible, forcing it");
            FracassoNotification.getInstance().showFailureMessage("Game Over!\nPress R to restart");
            FracassoNotification.getInstance().render(g2, getWidth() - getInsets().right, getHeight() - getInsets().top);
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }
    
    public TelaController getController() {
        return controller;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}