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
    private static final double SCALE_FACTOR = 15; 
    private int lifespan = 100; 
    private boolean shouldBeRemoved = false;
    private boolean firstFrame = true; 
    
    public Fireball(String sNomeImagePNG, int rowDirection, int colDirection) {
        super(sNomeImagePNG);
        this.rowDirection = rowDirection;
        this.colDirection = colDirection;
        this.bMortal = true; 
        resizeImage();
    }
    
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int origWidth = img.getWidth(null);
            int origHeight = img.getHeight(null);
            
            int blockSize = Auxiliar.Consts.CELL_SIDE;
            
            int desiredWidth = (int)(origWidth * SCALE_FACTOR);
            int desiredHeight = (int)(origHeight * SCALE_FACTOR);
            
            double scaleToFit = 1.0;
            if (desiredWidth > blockSize || desiredHeight > blockSize) {
                double scaleWidth = (double)blockSize / desiredWidth;
                double scaleHeight = (double)blockSize / desiredHeight;
                scaleToFit = Math.min(scaleWidth, scaleHeight);
            }
            
            int finalWidth = (int)(desiredWidth * scaleToFit);
            int finalHeight = (int)(desiredHeight * scaleToFit);
            
            BufferedImage originalImg = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g1 = originalImg.createGraphics();
            g1.drawImage(img, 0, 0, finalWidth, finalHeight, null);
            g1.dispose();
            
            if (colDirection < 0) {
                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-originalImg.getWidth(null), 0);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                originalImg = op.filter(originalImg, null);
            }
            
            int xOffset = (blockSize - finalWidth) / 2;
            int yOffset = (blockSize - finalHeight) / 2;
            
            BufferedImage resizedImg = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = resizedImg.createGraphics();
            
            g.setColor(new java.awt.Color(0, 0, 0, 0));
            g.fillRect(0, 0, blockSize, blockSize);
            
            g.drawImage(originalImg, xOffset, yOffset, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        if (!firstFrame) {
            boolean moved = this.setPosicao(pPosicao.getLinha() + rowDirection, pPosicao.getColuna() + colDirection);
            
            if (!moved || lifespan <= 0 || 
                pPosicao.getLinha() < 0 || 
                pPosicao.getLinha() >= Consts.MUNDO_ALTURA ||
                pPosicao.getColuna() < 0 || 
                pPosicao.getColuna() >= Consts.MUNDO_LARGURA) {
                shouldBeRemoved = true;
            }
        } else {
            firstFrame = false;
        }
        
        lifespan--;
        
        super.autoDesenho();
    }
    
    public boolean shouldRemove() {
        return shouldBeRemoved;
    }
}
