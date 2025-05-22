package Controler;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.FracassoNotification;
import Modelo.Fruta;
import Modelo.FrutaVert;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.SuccessoNotification;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
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
        // Update camera to follow hero before drawing
        if (controller != null && cameraManager != null && controller.getHero() != null) {
            cameraManager.atualizaCamera(controller.getHero());
        }
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
        
        // Draw fruit counter in the top-left corner
        drawFruitCounter((Graphics2D) g2);
        
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

    // Add this new method to draw the fruit counter
    private void drawFruitCounter(Graphics2D g2) {
        // Save original font and color
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();
        
        // Set font and color for counter
        Font counterFont = new Font("Arial", Font.BOLD, 16);
        g2.setFont(counterFont);
        g2.setColor(Color.WHITE);
        
        // Create counter text (sum Fruta and FrutaVert)
        int totalColetadas = 0;
        if (controller != null && controller.getFaseAtual() != null) {
            List<Personagem> faseAtual = controller.getFaseAtual();
            for (Personagem p : faseAtual) {
                if ((p instanceof Fruta || p instanceof FrutaVert) && p instanceof Modelo.Coletavel) {
                    Modelo.Coletavel coletavel = (Modelo.Coletavel) p;
                    if (coletavel.foiColetado()) {
                        totalColetadas++;
                    }
                }
            }
        }

        // Calculate total number of fruits in the current phase
        int totalFrutas = 0;
        if (controller != null && controller.getFaseAtual() != null) {
            List<Personagem> faseAtual = controller.getFaseAtual();
            for (Personagem p : faseAtual) {
                if (p instanceof Fruta || p instanceof FrutaVert) {
                    totalFrutas++;
                }
            }
        }
        
        // Calculate remaining fruits
        int frutasRestantes = totalFrutas - totalColetadas;
        
        // Check if all fruits have been collected and show success notification
        if (frutasRestantes <= 0 && totalFrutas > 0) {
            System.out.println("TelaView: No fruits remaining, showing success notification");
            SuccessoNotification.getInstance().showSuccessMessage("Level Complete!\nAll fruits collected!");
        }
        
        String counterText = "Frutas restantes: " + frutasRestantes;
        
        // Draw counter background
        g2.setColor(new Color(0, 0, 0, 160)); // Semi-transparent black
        g2.fillRoundRect(10, getHeight() - getInsets().bottom - 40, 
                g2.getFontMetrics().stringWidth(counterText) + 20, 30, 10, 10);
        
        // Draw counter text
        g2.setColor(Color.YELLOW);
        g2.drawString(counterText, 20, getHeight() - getInsets().bottom - 20);
        
        // Restore original font and color
        g2.setFont(originalFont);
        g2.setColor(originalColor);
    }
}