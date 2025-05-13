package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.util.Random;
import Auxiliar.Consts;
import java.util.ArrayList;

public class Villan_2 extends Personagem implements Serializable {
    private boolean bRight;
    private static final double SCALE_FACTOR = 30;  // Increased from 20 to 30
    private int cooldown;
    private int maxCooldown = 20;  // Time between fireball shots
    private static int totalVillans = 0;
    private static int villansColetadas = 0;
    private Random random = new Random();
    private Hero targetHero; // Reference to the hero for targeting
    
    // Static variable to hold the fireballs
    private static ArrayList<Fireball> pendingFireballs = new ArrayList<>();

    public Villan_2(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        cooldown = 0;
        resizeImage();
        totalVillans++;
    }
    
    // Set the hero target for aiming fireballs
    public void setTarget(Hero hero) {
        this.targetHero = hero;
    }
    
    /**
     * Resize the image with scaling, keeping it centered in the block.
     */
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int origWidth = img.getWidth(null);
            int origHeight = img.getHeight(null);
            
            // Get the cell size from constants
            int blockSize = Auxiliar.Consts.CELL_SIDE;
            
            // Apply the SCALE_FACTOR to the original dimensions
            int desiredWidth = (int)(origWidth * SCALE_FACTOR);
            int desiredHeight = (int)(origHeight * SCALE_FACTOR);
            
            // Calculate scale to fit within the cell while maintaining aspect ratio
            double scaleToFit = 1.5;
            if (desiredWidth > blockSize || desiredHeight > blockSize) {
                double scaleWidth = (double)blockSize / desiredWidth;
                double scaleHeight = (double)blockSize / desiredHeight;
                scaleToFit = Math.min(scaleWidth, scaleHeight);
            }
            
            // Final dimensions after applying SCALE_FACTOR and ensuring it fits in the cell
            int finalWidth = (int)(desiredWidth * scaleToFit);
            int finalHeight = (int)(desiredHeight * scaleToFit);
            
            // Calculate offsets to center the image in the block
            int xOffset = (blockSize - finalWidth) / 2;
            int yOffset = (blockSize - finalHeight) / 2;
            
            // Create a new image with the block size dimensions
            BufferedImage resizedImg = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = resizedImg.createGraphics();
            
            // Important: Clear the graphics context with a fully transparent color
            g.setColor(new java.awt.Color(0, 0, 0, 0));
            g.fillRect(0, 0, blockSize, blockSize);
            
            // Draw the image centered in the block
            g.drawImage(img, xOffset, yOffset, finalWidth, finalHeight, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        // Always process shooting logic, regardless of visibility
        if (!Hero.isGameOver() && targetHero != null) {
            cooldown++;
            if(cooldown >= maxCooldown) {
                shootFireball();
                cooldown = 0;
            }
        }
        
        // Draw the villain using parent method
        super.autoDesenho();
    }
    
    private void shootFireball() {
        if (targetHero == null) return;
        
        // Calculate direction toward hero - but only horizontally
        int villainRow = this.getPosicao().getLinha();
        int villainCol = this.getPosicao().getColuna();
        
        // Set direction vector - only horizontal (no vertical shooting)
        int rowDirection = 0; // Always 0 for horizontal shooting
        int colDirection = bRight ? 1 : -1; // 1 for right, -1 for left
        
        try {
            // Create the fireball
            Fireball fireball = new Fireball("Fireball.png", rowDirection, colDirection);
            
            // Position the fireball one cell away from the villain in the shooting direction
            fireball.setPosicao(villainRow, villainCol + colDirection);
            
            // Add to the pending fireballs list
            pendingFireballs.add(fireball);
            
            // Debug output
            System.out.println("Fireball created at position: " + villainRow + "," + (villainCol + colDirection) + 
                               " moving with direction: " + rowDirection + "," + colDirection);
        } catch (Exception e) {
            System.out.println("Error creating fireball: " + e.getMessage());
        }
    }
    
    // Static method to get and clear the pending fireballs
    public static ArrayList<Fireball> getPendingFireballs() {
        ArrayList<Fireball> fireballs = new ArrayList<>(pendingFireballs);
        pendingFireballs.clear();
        return fireballs;
    }
    
    // Modified method to show success message in the middle of the screen
    public static void mostrarSucesso() {
        // Create a reference to the notification system
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    // Add method to manually reset counters if needed
    public static void resetContadores() {
        totalVillans = 0;
        villansColetadas = 0;
    }

    public void matarHero(Hero hero) {
        System.out.println("Hero killed by villain!"); // Debug message
        hero.morrer();
    }

    // Add these methods to Villan_2 class
    public void setShootRate(int rate) {
        this.maxCooldown = rate;
    }

    public void setShootDirection(boolean shootRight) {
        this.bRight = shootRight;
    }

    public void processLogic() {
        if (targetHero != null) {
            cooldown++;
            if(cooldown >= maxCooldown) {
                shootFireball();
                cooldown = 0;
            }
        }
    }
}
