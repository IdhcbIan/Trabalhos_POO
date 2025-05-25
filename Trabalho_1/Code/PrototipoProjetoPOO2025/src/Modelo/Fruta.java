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
    private int maxSteps = 3;  // Default value
    private static int totalFrutas = 0;  // Track total fruits
    private static int frutasColetadas = 0;  // Track collected fruits
    private boolean coletada = false;  // Track if this fruit has been collected

    public Fruta(String sNomeImagePNG) {
        this(sNomeImagePNG, 3);  // Call the new constructor with default value
    }
    
    public Fruta(String sNomeImagePNG, int walkSteps) {
        super(sNomeImagePNG);
        bRight = true;
        steps = 0;
        maxSteps = walkSteps;
        resizeImage();
        totalFrutas++;  // Increment total fruits when a new one is created
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
        // Only move if the game is not over
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
        
        // Always draw the fruit, even when game is over
        super.autoDesenho();
    }

    // Modified method to handle fruit collection
    public void coletar() {
        if (!coletada) {
            coletada = true;
            frutasColetadas++;
            System.out.println("Fruta coletada! Total: " + frutasColetadas + " de " + totalFrutas);
            verificarSucesso();
        }
    }
    
    // Modified method to check for success
    public static void verificarSucesso() {
        int total = Fruta.getTotalFrutas() + FrutaVert.getTotalFrutasVert();
        int coletadas = Fruta.getFrutasColetadas() + FrutaVert.getFrutasVertColetadas();
        if (coletadas == total) {
            System.out.println("Sucesso! Todas as frutas foram coletadas!");
        }
    }
    
    // Modified method to show success message in the middle of the screen
    public static void mostrarSucesso() {
        // Create a reference to the notification system
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    // Add method to manually reset counters if needed
    public static void resetContadores() {
        System.out.println("DEBUG: Resetting fruit counters");
        totalFrutas = 0;
        frutasColetadas = 0;
    }
    
    // Add method to set counters directly (for loading saved games)
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

    // Add this method to allow FrutaVert to increment collected count
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

    // Add this method for adding to the total
    public static void incrementarTotalFrutas() {
        totalFrutas++;
    }
}
