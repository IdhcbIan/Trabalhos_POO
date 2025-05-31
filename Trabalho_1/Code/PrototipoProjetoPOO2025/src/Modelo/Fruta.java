package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fruta extends Personagem implements Serializable, Coletavel {
    private boolean bRight;
    private static final double SCALE_FACTOR = 0.8; 
    private int steps;
    private int maxSteps = 3; 
    private static int totalFrutas = 0;  
    private static int frutasColetadas = 0;  
    private boolean coletada = false;  

    public Fruta(String sNomeImagePNG) {
        this(sNomeImagePNG, 3);  
    }
    
    public Fruta(String sNomeImagePNG, int walkSteps) {
        super(sNomeImagePNG);
        bRight = true;
        steps = 0;
        maxSteps = walkSteps;
        resizeImage();
        totalFrutas++;  
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
            if(bRight)
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna()+1);
            else
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna()-1);           
            
            steps++;
            if(steps >= maxSteps) {
                bRight = !bRight;
                steps = 0;
            }
        }
        
        super.autoDesenho();
    }

    public void coletar() {
        if (!coletada) {
            coletada = true;
            frutasColetadas++;
            System.out.println("Fruta coletada! Total: " + frutasColetadas + " de " + totalFrutas);
            verificarSucesso();
        }
    }
    
    public static void verificarSucesso() {
        int total = Fruta.getTotalFrutas() + FrutaVert.getTotalFrutasVert();
        int coletadas = Fruta.getFrutasColetadas() + FrutaVert.getFrutasVertColetadas();
        if (coletadas == total) {
            System.out.println("Sucesso! Todas as frutas foram coletadas!");
        }
    }
    
    public static void mostrarSucesso() {
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    public static void resetContadores() {
        System.out.println("DEBUG: Resetting fruit counters");
        totalFrutas = 0;
        frutasColetadas = 0;
    }
    
    public static void setContadores(int total, int coletadas) {
        System.out.println("DEBUG: Setting fruit counters to total=" + total + ", coletadas=" + coletadas);
        totalFrutas = total;
        frutasColetadas = coletadas;
    }

    public boolean isColetada() {
        return coletada;
    }

    @Override
    public boolean foiColetado() {
        return isColetada();
    }

    public static void incrementarFrutasColetadas() {
        frutasColetadas++;
        System.out.println("Fruta coletada via incrementarFrutasColetadas! Total: " + frutasColetadas + " de " + totalFrutas);
    }

    public static int getFrutasColetadas() {
        return frutasColetadas;
    }

    public static int getTotalFrutas() {
        return totalFrutas;
    }

    public static void incrementarTotalFrutas() {
        totalFrutas++;
    }
}
