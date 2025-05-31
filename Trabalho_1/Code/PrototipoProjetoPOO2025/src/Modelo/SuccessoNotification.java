package Modelo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class SuccessoNotification {
    private static SuccessoNotification instance;
    private boolean isVisible = false;
    private String message = "";
    private int displayTime = 0;
    private final int MAX_DISPLAY_TIME = 120; 
    private boolean gameFreeze = false;
    private boolean isLevelCompletion = false; 
    private boolean playerInvulnerable = false; 
    
    private SuccessoNotification() {}
    
    public static SuccessoNotification getInstance() {
        if (instance == null) {
            instance = new SuccessoNotification();
        }
        return instance;
    }
    
    public void showSuccessMessage(String message) {
        this.message = message;
        this.isVisible = true;
        this.displayTime = 0;
        this.gameFreeze = true; 
        this.playerInvulnerable = true; 
        
        this.isLevelCompletion = message.contains("Level Complete") || 
                                message.contains("All fruits collected") ||
                                message.contains("Congratulations") ||
                                message.contains("SUCESSO");
        
        System.out.println("DEBUG: SuccessoNotification.showSuccessMessage called, message: " + message + 
                          ", isLevelCompletion: " + isLevelCompletion + 
                          ", playerInvulnerable: " + playerInvulnerable);
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public boolean isGameFreeze() {
        return gameFreeze;
    }
    
    public boolean isLevelCompletion() {
        return isLevelCompletion;
    }
    
    public boolean isPlayerInvulnerable() {
        return playerInvulnerable;
    }
    
    public void hide() {
        this.isVisible = false;
        this.gameFreeze = false;
        this.playerInvulnerable = false; 
        System.out.println("DEBUG: SuccessoNotification.hide called");
    }
    
    public void update() {
        if (isVisible) {
            displayTime++;
            if (displayTime >= MAX_DISPLAY_TIME) {
                hide();
            }
        }
    }
    
    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (!isVisible) {
            return;
        }
        
        // Desenhando O Overlay Semi-Transparente
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Dresenhando mensagem de sucesso
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        
        String[] lines = message.split("\n");
        FontMetrics fm = g2d.getFontMetrics();
        
        int totalHeight = lines.length * fm.getHeight();
        int y = (screenHeight - totalHeight) / 2;
        
        for (String line : lines) {
            int textWidth = fm.stringWidth(line);
            int x = (screenWidth - textWidth) / 2;
            g2d.drawString(line, x, y + fm.getAscent());
            y += fm.getHeight();
        }
        
        // Desenhando instrução baseada
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        String instruction;
        
        if (isLevelCompletion) {
            instruction = "Press 'N' to continue to next level";
        } else {
            instruction = "Spacebar to continue playing";
        }
        
        fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(instruction);
        int x = (screenWidth - textWidth) / 2;
        g2d.drawString(instruction, x, screenHeight - 50);
    }
}