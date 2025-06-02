package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Villan_1 extends Personagem implements Serializable {
    private boolean bRight;
    private static final double SCALE_FACTOR = 30;  
    private int steps;
    private int maxSteps = 12;  
    private static int totalVillans = 0;
    private static int villansColetadas = 0;
    private int moveCounter = 0;
    private int moveRate = 1;  

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
    
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int origWidth = img.getWidth(null);
            int origHeight = img.getHeight(null);
            
            int blockSize = Auxiliar.Consts.CELL_SIDE;
            
            int desiredWidth = (int)(origWidth * SCALE_FACTOR);
            int desiredHeight = (int)(origHeight * SCALE_FACTOR);
            
            double scaleToFit = 1.5;
            if (desiredWidth > blockSize || desiredHeight > blockSize) {
                double scaleWidth = (double)blockSize / desiredWidth;
                double scaleHeight = (double)blockSize / desiredHeight;
                scaleToFit = Math.min(scaleWidth, scaleHeight);
            }
            
            int finalWidth = (int)(desiredWidth * scaleToFit);
            int finalHeight = (int)(desiredHeight * scaleToFit);
            
            int xOffset = (blockSize - finalWidth) / 2;
            int yOffset = (blockSize - finalHeight) / 2;
            
            BufferedImage resizedImg = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = resizedImg.createGraphics();
            
            g.drawImage(img, xOffset, yOffset, finalWidth, finalHeight, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        boolean isGameFrozen = Hero.isGameOver() || 
                             (SuccessoNotification.getInstance().isVisible() && 
                              SuccessoNotification.getInstance().isGameFreeze());
        
        if (!isGameFrozen) {
            moveCounter++;
            if (moveCounter >= moveRate) {
                moveCounter = 0;
                
                int nextCol;
                if(bRight)
                    nextCol = pPosicao.getColuna() + 1;
                else
                    nextCol = pPosicao.getColuna() - 1;
                
                int currentCol = pPosicao.getColuna();
                int currentLinha = pPosicao.getLinha();
                
                this.setPosicao(currentLinha, nextCol);
                
                if (pPosicao.getColuna() != nextCol) {
                    bRight = !bRight;
                    steps = 0;
                    this.setPosicao(currentLinha, currentCol);
                } else {
                    steps++;
                    if(steps >= maxSteps) {
                        bRight = !bRight;
                        steps = 0;
                    }
                }
            }
        }
        
        super.autoDesenho();
    }

    
    public static void mostrarSucesso() {
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    public static void resetContadores() {
        totalVillans = 0;
        villansColetadas = 0;
    }

    public void matarHero(Hero hero) {
        if (SuccessoNotification.getInstance().isPlayerInvulnerable()) {
            System.out.println("Hero is invulnerable due to success notification, cannot be killed!");
            return;
        }
        
        System.out.println("Hero killed by villain!"); 
        hero.morrer();
    }

    public void setMoveRate(int moveRate) {
        this.moveRate = moveRate;
    }
}
