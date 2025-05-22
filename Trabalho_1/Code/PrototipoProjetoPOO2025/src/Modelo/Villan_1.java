package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Villan_1 extends Personagem implements Serializable {
    private boolean bRight;
    private static final double SCALE_FACTOR = 30;  // Increased from 20 to 30
    private int steps;
    private int maxSteps = 12;  // Default value
    private static int totalVillans = 0;
    private static int villansColetadas = 0;
    private int moveCounter = 0;
    private int moveRate = 1;  // Default: move every tick

    public Villan_1(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        steps = 0;
        resizeImage();
        totalVillans++;
    }
    
    public Villan_1(String sNomeImagePNG, int walkBlocks) {
        super(sNomeImagePNG);
        bRight = true;
        steps = 0;
        maxSteps = walkBlocks;
        resizeImage();
        totalVillans++;
    }
    
    public Villan_1(String sNomeImagePNG, int walkBlocks, int moveRate) {
        super(sNomeImagePNG);
        bRight = true;
        steps = 0;
        maxSteps = walkBlocks;
        this.moveRate = moveRate;
        resizeImage();
        totalVillans++;
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
        // Check if game is over or frozen due to success notification
        boolean isGameFrozen = Hero.isGameOver() || 
                             (SuccessoNotification.getInstance().isVisible() && 
                              SuccessoNotification.getInstance().isGameFreeze());
        
        // Only move if the game is not over and not frozen
        if (!isGameFrozen) {
            moveCounter++;
            if (moveCounter >= moveRate) {
                moveCounter = 0;
                
                int nextCol;
                if(bRight)
                    nextCol = pPosicao.getColuna() + 1;
                else
                    nextCol = pPosicao.getColuna() - 1;
                
                // Save current position
                int currentCol = pPosicao.getColuna();
                int currentLinha = pPosicao.getLinha();
                
                // Try to move
                this.setPosicao(currentLinha, nextCol);
                
                // Check if movement was blocked (by comparing if position actually changed)
                if (pPosicao.getColuna() != nextCol) {
                    // Movement was blocked, change direction
                    bRight = !bRight;
                    steps = 0;
                    // Reset to original position
                    this.setPosicao(currentLinha, currentCol);
                } else {
                    // Movement was successful
                    steps++;
                    if(steps >= maxSteps) {
                        bRight = !bRight;
                        steps = 0;
                    }
                }
            }
        }
        
        // Always draw the villain, even when game is over
        super.autoDesenho();
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

    // Add setter method for moveRate
    public void setMoveRate(int moveRate) {
        this.moveRate = moveRate;
    }
}
