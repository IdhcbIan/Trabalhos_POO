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
    
    private FracassoNotification() {
        System.out.println("FracassoNotification: Instance created");
    }
    
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
        System.out.println("FracassoNotification: Showing message: " + message + ", isVisible set to: " + isVisible);
    }
    
    public void update() {
        if (isVisible) {
            displayTime++;
            if (displayTime % 30 == 0) {
                System.out.println("FracassoNotification: Still visible, displayTime=" + displayTime + ", message: " + message);
            }
            if (displayTime > MAX_DISPLAY_TIME) {
                isVisible = false;
                System.out.println("FracassoNotification: Maximum display time reached, hiding notification");
            }
        }
    }
    
    /**
     * Draw the failure notification on the screen
     * @param g2d The Graphics2D context to draw on
     * @param screenWidth The width of the screen
     * @param screenHeight The height of the screen
     */
    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (!isVisible) {
            return;
        }
        
        System.out.println("FracassoNotification: Drawing message: " + message + 
                          ", Screen dimensions: " + screenWidth + "x" + screenHeight);
        
        // Draw semi-transparent black overlay
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Draw failure message
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        
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
        
        // Draw instruction
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        String instruction = "Press 'R' to restart";
        fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(instruction);
        int x = (screenWidth - textWidth) / 2;
        g2d.drawString(instruction, x, screenHeight - 50);
    }
    
    /**
     * Legacy render method for backward compatibility
     * @deprecated Use draw(Graphics2D, int, int) instead
     */
    @Deprecated
    public void render(Graphics g, int width, int height) {
        if (!isVisible) return;
        
        System.out.println("FracassoNotification: Legacy render method called, forwarding to draw method");
        // Call the new draw method with a Graphics2D object
        draw((Graphics2D) g, width, height);
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void hide() {
        System.out.println("FracassoNotification: Hiding notification, was visible: " + isVisible);
        isVisible = false;
    }
}