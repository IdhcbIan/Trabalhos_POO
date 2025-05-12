package Modelo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class FracassoNotification {
    private static FracassoNotification instance;
    private boolean isVisible = false;
    private String message = "";
    private int displayTime = 0;
    private final int MAX_DISPLAY_TIME = Integer.MAX_VALUE; // Set to a very large value so it stays visible until R is pressed
    
    // Singleton pattern
    private FracassoNotification() {}
    
    public static FracassoNotification getInstance() {
        if (instance == null) {
            instance = new FracassoNotification();
        }
        return instance;
    }
    
    public void showFailureMessage(String message) {
        this.message = message;
        this.isVisible = true;
        this.displayTime = 0;
        System.out.println("FracassoNotification: Showing message: " + message); // Debug message
    }
    
    public void update() {
        if (isVisible) {
            displayTime++;
            // Debug message every 30 frames
            if (displayTime % 30 == 0) {
                System.out.println("FracassoNotification: Still visible, displayTime=" + displayTime);
            }
            if (displayTime > MAX_DISPLAY_TIME) {
                isVisible = false;
            }
        }
    }
    
    public void render(Graphics g, int width, int height) {
        if (!isVisible) return;
        
        System.out.println("FracassoNotification: Rendering message"); // Debug message
        
        // Semi-transparent background
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, width, height);
        
        // Draw failure text
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(Color.RED); // Red color for failure
        Font originalFont = g.getFont();
        Font largeFont = new Font("Arial", Font.BOLD, 48); // Slightly smaller font
        g2d.setFont(largeFont);
        
        // Handle multi-line text
        String[] lines = message.split("\n");
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        
        // Calculate vertical position for centered text block
        int totalHeight = lineHeight * lines.length;
        int startY = (height - totalHeight) / 2 + fm.getAscent();
        
        // Draw each line centered horizontally
        for (int i = 0; i < lines.length; i++) {
            int textWidth = fm.stringWidth(lines[i]);
            int x = (width - textWidth) / 2;
            int y = startY + (i * lineHeight);
            
            g2d.drawString(lines[i], x, y);
        }
        
        // Restore original settings
        g2d.setFont(originalFont);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    // Add method to hide the notification
    public void hide() {
        isVisible = false;
        System.out.println("FracassoNotification: Hidden"); // Debug message
    }
}