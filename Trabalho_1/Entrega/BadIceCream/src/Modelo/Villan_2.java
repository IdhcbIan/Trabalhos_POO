package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

public class Villan_2 extends Personagem implements Serializable {
    private boolean bRight;
    private static final double SCALE_FACTOR = 30;  
    private int cooldown;
    private int maxCooldown = 20;  
    private static int totalVillans = 0;
    private static int villansColetadas = 0;
    private Random random = new Random();
    private Hero targetHero; 
    
    private static ArrayList<Fireball> pendingFireballs = new ArrayList<>();

    public Villan_2(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        cooldown = 0;
        resizeImage();
        totalVillans++;
    }
    
    public void setTarget(Hero hero) {
        this.targetHero = hero;
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
            
            g.setColor(new java.awt.Color(0, 0, 0, 0));
            g.fillRect(0, 0, blockSize, blockSize);
            
            g.drawImage(img, xOffset, yOffset, finalWidth, finalHeight, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        boolean isGameFrozen = Hero.isGameOver() || 
                             (SuccessoNotification.getInstance().isVisible() && 
                              SuccessoNotification.getInstance().isGameFreeze());
        
        if (!isGameFrozen && targetHero != null) {
            cooldown++;
            if(cooldown >= maxCooldown) {
                shootFireball();
                cooldown = 0;
            }
        }
        
        super.autoDesenho();
    }
    
    private void shootFireball() {
        if (targetHero == null) return;
        
        int villainRow = this.getPosicao().getLinha();
        int villainCol = this.getPosicao().getColuna();
        
        int rowDirection = 0; 
        int colDirection = bRight ? 1 : -1; 
        
        try {
            Fireball fireball = new Fireball("Fireball.png", rowDirection, colDirection);
            
            fireball.setPosicao(villainRow, villainCol + colDirection);
            
            pendingFireballs.add(fireball);
            
            System.out.println("Fireball created at position: " + villainRow + "," + (villainCol + colDirection) + 
                               " moving with direction: " + rowDirection + "," + colDirection);
        } catch (Exception e) {
            System.out.println("Error creating fireball: " + e.getMessage());
        }
    }
    
    public static ArrayList<Fireball> getPendingFireballs() {
        ArrayList<Fireball> fireballs = new ArrayList<>(pendingFireballs);
        pendingFireballs.clear();
        return fireballs;
    }
    
    public static void mostrarSucesso() {
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    public static void resetContadores() {
        totalVillans = 0;
        villansColetadas = 0;
    }

    public void matarHero(Hero hero) {
        System.out.println("Hero killed by villain!"); 
        hero.morrer();
    }

    public void setShootRate(int rate) {
        this.maxCooldown = rate;
    }

    public void setShootDirection(boolean shootRight) {
        this.bRight = shootRight;
    }

    public void processLogic() {
        boolean isGameFrozen = Hero.isGameOver() || 
                             (SuccessoNotification.getInstance().isVisible() && 
                              SuccessoNotification.getInstance().isGameFreeze());
        
        if (!isGameFrozen && targetHero != null) {
            cooldown++;
            if(cooldown >= maxCooldown) {
                shootFireball();
                cooldown = 0;
            }
        }
    }
}
