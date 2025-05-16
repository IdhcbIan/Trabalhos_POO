package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Villan_3 extends Personagem implements Serializable {
    private static final double SCALE_FACTOR = 1.5; // Increased to make villain significantly larger
    private Hero targetHero; // Reference to the hero to follow
    private int moveCounter = 0;
    private int moveRate = 1; // Changed from 3 to 1 for movement every tick

    public Villan_3(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true; // Make the villain deadly on contact
        resizeImage();
    }
    
    // Set the hero target for following
    public void setTarget(Hero hero) {
        this.targetHero = hero;
    }
    
    /**
     * Resize the image with scaling, allowing it to exceed cell boundaries.
     */
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int origWidth = img.getWidth(null);
            int origHeight = img.getHeight(null);
            
            // Get the cell size from constants
            int blockSize = Auxiliar.Consts.CELL_SIDE;
            
            // Apply the SCALE_FACTOR to the original dimensions without fitting constraint
            int finalWidth = (int)(origWidth * SCALE_FACTOR);
            int finalHeight = (int)(origHeight * SCALE_FACTOR);
            
            // Calculate offsets to center horizontally but align to top of cell
            int xOffset = (blockSize - finalWidth) / 2;
            int yOffset = 0; // Set to 0 to align to top instead of centering vertically
            
            // Create a new image with the dimensions large enough for the scaled image
            BufferedImage resizedImg = new BufferedImage(
                Math.max(blockSize, finalWidth), 
                Math.max(blockSize, finalHeight), 
                BufferedImage.TYPE_INT_ARGB
            );
            Graphics g = resizedImg.createGraphics();
            
            // Clear the graphics context with a fully transparent color
            g.setColor(new java.awt.Color(0, 0, 0, 0));
            g.fillRect(0, 0, resizedImg.getWidth(), resizedImg.getHeight());
            
            // Draw the image aligned to top of cell
            g.drawImage(img, xOffset, yOffset, finalWidth, finalHeight, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    @Override
    public void autoDesenho() {
        // Follow the hero if game is not over and target is set
        if (!Hero.isGameOver() && targetHero != null) {
            moveCounter++;
            if (moveCounter >= moveRate) {
                followHero();
                moveCounter = 0;
            }

        }
        
        // Draw the villain using parent method
        super.autoDesenho();
    }
    
    /**
     * Logic to follow the hero - first try to match x-coordinate, then y
     */
    private void followHero() {
        // Only follow if we have a valid hero target
        if (targetHero == null) return;
        
        // Get positions
        int villainRow = this.getPosicao().getLinha();
        int villainCol = this.getPosicao().getColuna();
        int heroRow = targetHero.getPosicao().getLinha();
        int heroCol = targetHero.getPosicao().getColuna();
        
        // CHANGED: Only kill the hero when they're in the exact same position
        // Not when they're adjacent
        if (villainRow == heroRow && villainCol == heroCol) {
            // We're directly on top of the hero - kill the hero
            System.out.println("Hero killed by Villain 3!");
            targetHero.morrer();
            return;
        }
        
        // Determine direction to move (simple following logic)
        // Move horizontally or vertically based on which distance is greater
        int rowDiff = heroRow - villainRow;
        int colDiff = heroCol - villainCol;
        
        // Only move one step at a time
        if (Math.abs(rowDiff) > Math.abs(colDiff)) {
            // Move vertically
            if (rowDiff > 0) {
                this.setPosicao(villainRow + 1, villainCol); // Move down
            } else if (rowDiff < 0) {
                this.setPosicao(villainRow - 1, villainCol); // Move up
            }
        } else {
            // Move horizontally
            if (colDiff > 0) {
                this.setPosicao(villainRow, villainCol + 1); // Move right
            } else if (colDiff < 0) {
                this.setPosicao(villainRow, villainCol - 1); // Move left
            }
        }
        
        // Check if after moving we're now on top of the hero
        villainRow = this.getPosicao().getLinha();
        villainCol = this.getPosicao().getColuna();
        if (villainRow == heroRow && villainCol == heroCol) {
            System.out.println("Hero killed by Villain 3!");
            targetHero.morrer();
        }
    }
    
    public void matarHero(Hero hero) {
        System.out.println("Hero killed by following villain!"); // Debug message
        hero.morrer();
    }
    
    public void setMoveRate(int moveRate) {
        this.moveRate = moveRate;
    }
} 