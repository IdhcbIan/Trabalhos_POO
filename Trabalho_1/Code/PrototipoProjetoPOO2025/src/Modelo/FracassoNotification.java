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
    private final int MAX_DISPLAY_TIME = Integer.MAX_VALUE; 
    
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
        
        System.out.println("FracassoNotification: Rendering message");
        
        // Transparencia!!
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, width, height);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(Color.RED); 
        Font originalFont = g.getFont();
        Font largeFont = new Font("Arial", Font.BOLD, 48); 
        g2d.setFont(largeFont);
        
        String[] lines = message.split("\n");
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        
        int totalHeight = lineHeight * lines.length;
        int startY = (height - totalHeight) / 2 + fm.getAscent();
        
        for (int i = 0; i < lines.length; i++) {
            int textWidth = fm.stringWidth(lines[i]);
            int x = (width - textWidth) / 2;
            int y = startY + (i * lineHeight);
            
            g2d.drawString(lines[i], x, y);
        }
        
        g2d.setFont(originalFont);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void hide() {
        isVisible = false;
    }
}