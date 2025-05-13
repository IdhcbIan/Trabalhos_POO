package Modelo;

import Auxiliar.Consts;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fireball extends Personagem implements Serializable {
    private int rowDirection;
    private int colDirection;
    private static final double SCALE_FACTOR = 15;  // Smaller than villain
    private int lifespan = 100;  // Number of frames the fireball exists
    private boolean shouldBeRemoved = false;
    private boolean firstFrame = true;  // Add this flag
    
    public Fireball(String sNomeImagePNG, int rowDirection, int colDirection) {
        super(sNomeImagePNG);
        this.rowDirection = rowDirection;
        this.colDirection = colDirection;
        this.bMortal = true;  // Fireballs are deadly
        resizeImage();
    }
    
    /**
     * Resize the image with scaling, keeping it centered in the block.
     * Also mirror the image if the fireball is moving left.
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
            double scaleToFit = 1.0;
            if (desiredWidth > blockSize || desiredHeight > blockSize) {
                double scaleWidth = (double)blockSize / desiredWidth;
                double scaleHeight = (double)blockSize / desiredHeight;
                scaleToFit = Math.min(scaleWidth, scaleHeight);
            }
            
            // Final dimensions after applying SCALE_FACTOR and ensuring it fits in the cell
            int finalWidth = (int)(desiredWidth * scaleToFit);
            int finalHeight = (int)(desiredHeight * scaleToFit);
            
            // Create a temporary BufferedImage to draw the original image
            BufferedImage originalImg = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g1 = originalImg.createGraphics();
            g1.drawImage(img, 0, 0, finalWidth, finalHeight, null);
            g1.dispose();
            
            // If moving left (colDirection is negative), mirror the image horizontally
            if (colDirection < 0) {
                // Create mirrored version using AffineTransform
                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-originalImg.getWidth(null), 0);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                originalImg = op.filter(originalImg, null);
            }
            
            // Calculate offsets to center the image in the block
            int xOffset = (blockSize - finalWidth) / 2;
            int yOffset = (blockSize - finalHeight) / 2;
            
            // Create a new image with the block size dimensions
            BufferedImage resizedImg = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = resizedImg.createGraphics();
            
            // Important: Clear the graphics context with a fully transparent color
            g.setColor(new java.awt.Color(0, 0, 0, 0));
            g.fillRect(0, 0, blockSize, blockSize);
            
            // Draw the image centered in the block (original or mirrored)
            g.drawImage(originalImg, xOffset, yOffset, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        // Only move after the first draw
        if (!firstFrame) {
            // Move in the set direction
            boolean moved = this.setPosicao(pPosicao.getLinha() + rowDirection, pPosicao.getColuna() + colDirection);
            
            // If couldn't move or lifespan is over, mark for removal
            if (!moved || lifespan <= 0 || 
                pPosicao.getLinha() < 0 || 
                pPosicao.getLinha() >= Consts.MUNDO_ALTURA ||
                pPosicao.getColuna() < 0 || 
                pPosicao.getColuna() >= Consts.MUNDO_LARGURA) {
                // Mark for removal
                shouldBeRemoved = true;
            }
        } else {
            // First frame - don't move, just draw
            firstFrame = false;
        }
        
        // Decrease lifespan
        lifespan--;
        
        // Always draw the fireball
        super.autoDesenho();
    }
    
    // Helper method to check if this fireball should be removed
    public boolean shouldRemove() {
        return shouldBeRemoved;
    }
}
