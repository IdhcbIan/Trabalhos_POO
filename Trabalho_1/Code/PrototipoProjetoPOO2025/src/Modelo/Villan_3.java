package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Villan_3 extends Personagem implements Serializable {
    private static final double SCALE_FACTOR = 1.5; 
    private Hero targetHero; 
    private int moveCounter = 0;
    private int moveRate = 1; 

    public Villan_3(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true; 
        resizeImage();
    }
    
    // Set the hero target for following
    public void setTarget(Hero hero) {
        this.targetHero = hero;
    }
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int origWidth = img.getWidth(null);
            int origHeight = img.getHeight(null);
            
            int blockSize = Auxiliar.Consts.CELL_SIDE;
            
            int finalWidth = (int)(origWidth * SCALE_FACTOR);
            int finalHeight = (int)(origHeight * SCALE_FACTOR);
            
            int xOffset = (blockSize - finalWidth) / 2;
            int yOffset = 0; 
            
            BufferedImage resizedImg = new BufferedImage(
                Math.max(blockSize, finalWidth), 
                Math.max(blockSize, finalHeight), 
                BufferedImage.TYPE_INT_ARGB
            );
            Graphics g = resizedImg.createGraphics();
            
            g.setColor(new java.awt.Color(0, 0, 0, 0));
            g.fillRect(0, 0, resizedImg.getWidth(), resizedImg.getHeight());
            
            g.drawImage(img, xOffset, yOffset, finalWidth, finalHeight, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    @Override
    public void autoDesenho() {
        boolean isGameFrozen = Hero.isGameOver() || 
                             (SuccessoNotification.getInstance().isVisible() && 
                              SuccessoNotification.getInstance().isGameFreeze());
        
        if (!isGameFrozen && targetHero != null) {
            moveCounter++;
            if (moveCounter >= moveRate) {
                followHero();
                moveCounter = 0;
            }
        }
        
        super.autoDesenho();
    }
    
    private void followHero() {
        if (targetHero == null) return;
        
        int villainRow = this.getPosicao().getLinha();
        int villainCol = this.getPosicao().getColuna();
        int heroRow = targetHero.getPosicao().getLinha();
        int heroCol = targetHero.getPosicao().getColuna();
        
        if (villainRow == heroRow && villainCol == heroCol) {
            System.out.println("Hero killed by Villain 3!");
            targetHero.morrer();
            return;
        }
        
        int rowDiff = heroRow - villainRow;
        int colDiff = heroCol - villainCol;
        
        if (Math.abs(rowDiff) > Math.abs(colDiff)) {
            if (rowDiff > 0) {
                this.setPosicao(villainRow + 1, villainCol); 
            } else if (rowDiff < 0) {
                this.setPosicao(villainRow - 1, villainCol); 
            }
        } else {
            if (colDiff > 0) {
                this.setPosicao(villainRow, villainCol + 1); 
            } else if (colDiff < 0) {
                this.setPosicao(villainRow, villainCol - 1); 
            }
        }
        
        villainRow = this.getPosicao().getLinha();
        villainCol = this.getPosicao().getColuna();
        if (villainRow == heroRow && villainCol == heroCol) {
            System.out.println("Hero killed by Villain 3!");
            targetHero.morrer();
        }
    }
    
    public void matarHero(Hero hero) {
        System.out.println("Hero killed by following villain!"); 
        hero.morrer();
    }
    
    public void setMoveRate(int moveRate) {
        this.moveRate = moveRate;
    }
} 