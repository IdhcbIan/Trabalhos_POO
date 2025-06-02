package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class FrutaVert extends Personagem implements Serializable, Coletavel {
    private boolean bDown;  
    private static final double SCALE_FACTOR = 0.8; 
    private int steps;
    private int maxSteps = 3;  
    private boolean coletada = false;  
    private static int totalFrutasVert = 0;
    private static int frutasVertColetadas = 0;

    public FrutaVert(String sNomeImagePNG) {
        this(sNomeImagePNG, 3);  
    }
    
    public FrutaVert(String sNomeImagePNG, int walkSteps) {
        super(sNomeImagePNG);
        bDown = true;  
        steps = 0;
        maxSteps = walkSteps;
        resizeImage();
        Fruta.incrementarTotalFrutas(); 
        totalFrutasVert++; 
    }
    
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int width = (int)(img.getWidth(null) * SCALE_FACTOR);
            int height = (int)(img.getHeight(null) * SCALE_FACTOR);
            
            BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = resizedImg.createGraphics();
            g.drawImage(img, 0, 0, width, height, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        if (!Hero.isGameOver()) {
            if(bDown)
                this.setPosicao(pPosicao.getLinha()+1, pPosicao.getColuna());  // Move down
            else
                this.setPosicao(pPosicao.getLinha()-1, pPosicao.getColuna());  // Move up
            
            steps++;
            if(steps >= maxSteps) {
                bDown = !bDown;  
                steps = 0;
            }
        }
        
        super.autoDesenho();
    }

    public void coletar() {
        if (!coletada) {
            coletada = true;
            Fruta.incrementarFrutasColetadas(); 
            frutasVertColetadas++; 
            System.out.println("Fruta vertical coletada! Total coletadas: " + 
                               getFrutasVertColetadas() + " de " + getTotalFrutasVert());
            Fruta.verificarSucesso(); 
        }
    }

    public static void incrementarFrutasVertColetadas() {
        frutasVertColetadas++;
    }
    
    public static void setContadores(int total, int coletadas) {
        System.out.println("DEBUG: Setting vertical fruit counters to total=" + total + ", coletadas=" + coletadas);
        totalFrutasVert = total;
        frutasVertColetadas = coletadas;
    }

    public boolean isColetada() {
        return coletada;
    }

    @Override
    public boolean foiColetado() {
        return isColetada();
    }

    public static int getTotalFrutasVert() {
        return totalFrutasVert;
    }

    public static int getFrutasVertColetadas() {
        return frutasVertColetadas;
    }
}
