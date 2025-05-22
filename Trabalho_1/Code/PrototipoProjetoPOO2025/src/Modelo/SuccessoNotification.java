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
    private final int MAX_DISPLAY_TIME = 120; // Frames to display message (2 seconds at 60fps)
    private boolean gameFreeze = false; // Flag to indicate if the game should be frozen
    
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
        this.gameFreeze = true; // Freeze the game when success notification appears
        System.out.println("DEBUG: SuccessoNotification.showSuccessMessage called, message: " + message);
    }
    
    public void update() {
        if (isVisible) {
            displayTime++;
            if (displayTime > MAX_DISPLAY_TIME) {
                isVisible = false;
                // Don't automatically unfreeze the game when notification disappears
                // The game will remain frozen until player presses 'n'
            }
        }
    }
    
    /**
     * Properly hide the notification and reset its state
     */
    public void hide() {
        this.isVisible = false;
        this.gameFreeze = false;
        this.message = "";
        this.displayTime = 0;
    }
    
    public void render(Graphics2D g2, int width, int height) {
        if (!isVisible) return;
        int boxWidth = 400;
        int boxHeight = 100;
        int x = (width - boxWidth) / 2;
        int y = (height - boxHeight) / 2;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.setColor(Color.GREEN);
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g2.getFontMetrics();
        String[] lines = message.split("\n");
        int lineHeight = fm.getHeight();
        int totalTextHeight = lineHeight * lines.length;
        int startY = y + (boxHeight - totalTextHeight) / 2 + fm.getAscent();

        for (int i = 0; i < lines.length; i++) {
            int textWidth = fm.stringWidth(lines[i]);
            int textX = x + (boxWidth - textWidth) / 2;
            int textY = startY + i * lineHeight;
            g2.drawString(lines[i], textX, textY);
        }
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public boolean isGameFreeze() {
        return gameFreeze;
    }
    
    public void setGameFreeze(boolean freeze) {
        this.gameFreeze = freeze;
    }
} 