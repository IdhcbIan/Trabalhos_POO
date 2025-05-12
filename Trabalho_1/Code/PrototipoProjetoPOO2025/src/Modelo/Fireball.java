package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fireball extends Personagem implements Serializable {
    private int rowDirection;
    private int colDirection;
    private static final double SCALE_FACTOR = 15;  // Smaller than villain
    private int lifespan = 100;  // Number of frames the fireball exists
    
    public Fireball(String sNomeImagePNG, int rowDirection, int colDirection) {
        super(sNomeImagePNG);
        this.rowDirection = rowDirection;
        this.colDirection = colDirection;
        resizeImage();
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
            double scaleToFit = 1.0;
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
        // Move in the set direction
        this.setPosicao(pPosicao.getLinha() + rowDirection, pPosicao.getColuna() + colDirection);
        
        // Decrease lifespan
        lifespan--;
        
        // Remove if lifespan is over or if the fireball is out of bounds
        if (lifespan <= 0 || 
            pPosicao.getLinha() < 0 || 
            pPosicao.getLinha() >= Auxiliar.Consts.RES ||
            pPosicao.getColuna() < 0 || 
            pPosicao.getColuna() >= Auxiliar.Consts.RES) {
            Desenho.removerPersonagem(this);
            return;
        }
        
        // Check for collision with hero
        Hero hero = (Hero)Desenho.acessoAoHeroi();
        if (hero != null && hero.getPosicao().igual(pPosicao)) {
            hero.morrer();
            Desenho.removerPersonagem(this);
            return;
        }
        
        super.autoDesenho();
    }
}
