package Modelo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SuccessoNotification {
    private static SuccessoNotification instance;
    private boolean isVisible = false;
    private String message = "";
    private int displayTime = 0;
    private final int MAX_DISPLAY_TIME = 120; // Frames to display message (2 seconds at 60fps)
    
    // Singleton pattern
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
    }
    
    public void update() {
        if (isVisible) {
            displayTime++;
            if (displayTime > MAX_DISPLAY_TIME) {
                isVisible = false;
            }
        }
    }
    
    public void render(Graphics g, int width, int height) {
        if (!isVisible) return;
        
        // Calculate animation progress (0.0 to 1.0)
        float progress = Math.min(1.0f, displayTime / 20.0f); // Fade in during first 20 frames
        float fadeOut = (displayTime > MAX_DISPLAY_TIME - 20) ? 
                        1.0f - ((displayTime - (MAX_DISPLAY_TIME - 20)) / 20.0f) : 1.0f; // Fade out during last 20 frames
        float alpha = Math.min(progress, fadeOut);
        
        // Semi-transparent background
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f * alpha));
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, width, height);
        
        // Draw success text
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.GREEN);
        Font originalFont = g.getFont();
        Font largeFont = new Font("Arial", Font.BOLD, 72);
        g2d.setFont(largeFont);
        
        // Center the text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height + textHeight) / 2;
        
        g2d.drawString(message, x, y);
        
        // Restore original settings
        g2d.setFont(originalFont);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    public boolean isVisible() {
        return isVisible;
    }
} 