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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
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
    private Graphics2D g2;
    
    // Variables for displaying save/load status
    private boolean showSaveLoadMessage = false;
    private String saveLoadMessage = "";
    private long messageStartTime = 0;
    private static final long MESSAGE_DURATION = 3000; // 3 seconds
    
    // Variables for drag and drop visual feedback
    private boolean dragOver = false;
    
    public TelaView() {
        Desenho.setCenario(this);
        initComponents();
        
        // Set size before creating buffer strategy
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
        
        this.setVisible(true);
        this.createBufferStrategy(2);
        
        // Setup drag and drop functionality
        setupDragAndDrop();
    }
    
    /**
     * Set up drag and drop functionality for the game window
     */
    private void setupDragAndDrop() {
        // Create a drop target for the frame
        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
                // Only accept if it's a file
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    dragOver = true;
                    repaint();
                } else {
                    dtde.rejectDrag();
                }
            }
            
            @Override
            public void dragExit(java.awt.dnd.DropTargetEvent dte) {
                dragOver = false;
                repaint();
            }
            
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    // Accept the drop
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    dragOver = false;
                    
                    // Get the dropped files
                    Transferable transferable = dtde.getTransferable();
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    
                    // Process each dropped file
                    for (File file : files) {
                        if (file.getName().toLowerCase().endsWith(".zip")) {
                            // Get the mouse position relative to the game grid
                            int mouseX = dtde.getLocation().x;
                            int mouseY = dtde.getLocation().y;
                            
                            // Convert to grid coordinates
                            int gridCol = mouseX / Consts.CELL_SIDE;
                            int gridRow = mouseY / Consts.CELL_SIDE;
                            
                            // Adjust for camera offset
                            gridRow += cameraManager.getCameraLinha();
                            gridCol += cameraManager.getCameraColuna();
                            
                            // Load villains from the zip file at the drop position
                            loadVillainsAtPosition(file.getAbsolutePath(), gridRow, gridCol);
                        }
                    }
                    
                    dtde.dropComplete(true);
                    repaint();
                } catch (UnsupportedFlavorException | IOException e) {
                    dtde.dropComplete(false);
                    System.out.println("Drop failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Load villains from a zip file and place them at the specified position
     */
    private void loadVillainsAtPosition(String zipFilePath, int row, int col) {
        try {
            // Create a custom villain loader that places villains at the drop position
            VillainLoader loader = new VillainLoader(controller) {
                @Override
                protected Personagem createVillainFromConfig(String villainName, java.util.Properties config) {
                    // Override the position with the drop position
                    config.setProperty("linha", String.valueOf(row));
                    config.setProperty("coluna", String.valueOf(col));
                    return super.createVillainFromConfig(villainName, config);
                }
            };
            
            // Load the villains
            List<Personagem> loadedVillains = loader.loadVillainsFromZip(zipFilePath);
            
            if (!loadedVillains.isEmpty()) {
                showSaveLoadMessage("Added " + loadedVillains.size() + " villains!");
            } else {
                showSaveLoadMessage("No villains found in zip file");
            }
        } catch (Exception e) {
            showSaveLoadMessage("Error loading villains: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    /**
     * Display a save/load status message for a few seconds
     * @param message The message to display
     */
    public void showSaveLoadMessage(String message) {
        this.saveLoadMessage = message;
        this.showSaveLoadMessage = true;
        this.messageStartTime = System.currentTimeMillis();
    }
    
    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                // Update the notifications
                SuccessoNotification.getInstance().update();
                FracassoNotification.getInstance().update();
                
                // Check if we need to hide the save/load message
                if (showSaveLoadMessage && System.currentTimeMillis() - messageStartTime > MESSAGE_DURATION) {
                    showSaveLoadMessage = false;
                }
                
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
        g2 = (Graphics2D) g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
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
        
        if (controller != null && controller.getControleDeJogo() != null && controller.getFaseAtual() != null) {
            controller.getControleDeJogo().processaTudo(controller.getFaseAtual());
            controller.getControleDeJogo().desenhaTudo(controller.getFaseAtual());
        }
        
        // Draw the fruit counter
        drawFruitCounter(g2);
        
        // Draw the level indicator
        drawLevelIndicator(g2);
        
        // Draw save/load message if active
        if (showSaveLoadMessage) {
            drawSaveLoadMessage(g2);
        }
        
        // Draw the success notification if it's visible
        if (SuccessoNotification.getInstance().isVisible()) {
            SuccessoNotification.getInstance().draw(g2, getWidth(), getHeight());
        }
        
        // Draw the failure notification if it's visible
        if (FracassoNotification.getInstance().isVisible()) {
            FracassoNotification.getInstance().draw(g2, getWidth(), getHeight());
        }
        
        // Draw drag-over visual feedback
        if (dragOver) {
            drawDragOverFeedback(g2);
        }
        
        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    public TelaController getController() {
        return controller;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023");
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




    // Contador de Frutas!!

    private void drawFruitCounter(Graphics2D g2) {
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();
        
        Font counterFont = new Font("Arial", Font.BOLD, 16);
        g2.setFont(counterFont);
        g2.setColor(Color.WHITE);
        
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
        if (frutasRestantes <= 0) {
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
    
    /**
     * Draws the level indicator in the top left corner of the screen
     */
    private void drawLevelIndicator(Graphics2D g2) {
        if (controller == null || controller.getFase() == null) {
            return; // No level to display
        }
        
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();
        
        // Use the same font as the fruit counter
        Font levelFont = new Font("Arial", Font.BOLD, 16);
        g2.setFont(levelFont);
        
        // Get the current level and max level (assuming 5 levels total)
        int currentLevel = controller.getFase().getLevel();
        int maxLevel = 5;
        
        String levelText = "Level: " + currentLevel + "/" + maxLevel;
        
        // Draw level indicator background
        g2.setColor(new Color(0, 0, 0, 160)); // Semi-transparent black
        g2.fillRoundRect(10, 10, 
                g2.getFontMetrics().stringWidth(levelText) + 20, 30, 10, 10);
        
        // Draw level indicator text
        g2.setColor(Color.CYAN); // Different color from fruit counter
        g2.drawString(levelText, 20, 30);
        
        // Restore original font and color
        g2.setFont(originalFont);
        g2.setColor(originalColor);
    }
    
    /**
     * Draws the save/load status message
     */
    private void drawSaveLoadMessage(Graphics2D g2) {
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();
        
        // Use a larger font for the save/load message
        Font messageFont = new Font("Arial", Font.BOLD, 18);
        g2.setFont(messageFont);
        
        // Calculate the position (center of screen)
        int textWidth = g2.getFontMetrics().stringWidth(saveLoadMessage);
        int x = (getWidth() - textWidth) / 2;
        int y = 60; // Position below the level indicator
        
        // Draw message background
        g2.setColor(new Color(0, 0, 0, 180)); // Semi-transparent black
        g2.fillRoundRect(x - 10, y - 20, textWidth + 20, 30, 10, 10);
        
        // Draw message text
        g2.setColor(Color.GREEN); // Green for save/load messages
        g2.drawString(saveLoadMessage, x, y);
        
        // Restore original font and color
        g2.setFont(originalFont);
        g2.setColor(originalColor);
    }
    
    /**
     * Draws visual feedback when dragging a file over the game window
     */
    private void drawDragOverFeedback(Graphics2D g2) {
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();
        
        // Draw a semi-transparent overlay
        g2.setColor(new Color(0, 100, 255, 50));
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw a message
        String message = "Drop ZIP file to add villains";
        Font messageFont = new Font("Arial", Font.BOLD, 24);
        g2.setFont(messageFont);
        
        // Calculate the position (center of screen)
        int textWidth = g2.getFontMetrics().stringWidth(message);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 2;
        
        // Draw message background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x - 10, y - 30, textWidth + 20, 40, 15, 15);
        
        // Draw message text
        g2.setColor(Color.WHITE);
        g2.drawString(message, x, y);
        
        // Restore original font and color
        g2.setFont(originalFont);
        g2.setColor(originalColor);
    }
}