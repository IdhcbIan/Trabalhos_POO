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
    private boolean isLevelCompletion = false; // Flag to determine if this is a level completion notification
    
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
        this.gameFreeze = true; // Freeze the game when success notification appears
        
        // Check if this is a level completion message
        this.isLevelCompletion = message.contains("Level Complete") || 
                                message.contains("All fruits collected") ||
                                message.contains("Congratulations") ||
                                message.contains("SUCESSO");
        
        System.out.println("DEBUG: SuccessoNotification.showSuccessMessage called, message: " + message + 
                          ", isLevelCompletion: " + isLevelCompletion);
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
    
    public void hide() {
        this.isVisible = false;
        this.gameFreeze = false;
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
        
        // Draw semi-transparent black overlay
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Draw success message
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Split message by newlines
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
        
        // Draw instruction based on notification type
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