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
            
            // Draw the image centered in the block
            g.drawImage(img, xOffset, yOffset, finalWidth, finalHeight, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        // Only shoot fireballs if the game is not over
        if (!Hero.isGameOver() && targetHero != null) {
            cooldown++;
            if(cooldown >= maxCooldown) {
                shootFireball();
                cooldown = 0;
            }
        }
        
        // Always draw the villain, even when game is over
        super.autoDesenho();
    }
    
    private void shootFireball() {
        if (targetHero == null) return;
        
        // Calculate direction toward hero
        int villainRow = this.getPosicao().getLinha();
        int villainCol = this.getPosicao().getColuna();
        int heroRow = targetHero.getPosicao().getLinha();
        int heroCol = targetHero.getPosicao().getColuna();
        
        // Calculate direction vector
        int rowDirection = 0;
        int colDirection = 0;
        
        // Determine primary direction (up, down, left, right)
        if (Math.abs(heroRow - villainRow) > Math.abs(heroCol - villainCol)) {
            // Vertical movement is primary
            rowDirection = heroRow > villainRow ? 1 : -1;
        } else {
            // Horizontal movement is primary
            colDirection = heroCol > villainCol ? 1 : -1;
        }
        
        try {
            // Create the fireball
            Fireball fireball = new Fireball("Fireball.png", rowDirection, colDirection);
            fireball.setPosicao(villainRow, villainCol);
            
            // Add to the pending fireballs list
            pendingFireballs.add(fireball);
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
}
