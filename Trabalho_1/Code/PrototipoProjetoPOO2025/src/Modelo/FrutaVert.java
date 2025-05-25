package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class FrutaVert extends Personagem implements Serializable, Coletavel {
    private boolean bDown;  // Changed from bRight to bDown
    private static final double SCALE_FACTOR = 0.8; 
    private int steps;
    private int maxSteps = 3;  // Default value
    private boolean coletada = false;  // Track if this fruit has been collected
    // Add static counters for FrutaVert
    private static int totalFrutasVert = 0;
    private static int frutasVertColetadas = 0;

    public FrutaVert(String sNomeImagePNG) {
        this(sNomeImagePNG, 3);  // Call the new constructor with default value
    }
    
    public FrutaVert(String sNomeImagePNG, int walkSteps) {
        super(sNomeImagePNG);
        bDown = true;  // Start moving down
        steps = 0;
        maxSteps = walkSteps;
        resizeImage();
        Fruta.incrementarTotalFrutas(); // <-- Existing line
        totalFrutasVert++; // <-- Add this line
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
            if(bDown)
                this.setPosicao(pPosicao.getLinha()+1, pPosicao.getColuna());  // Move down
            else
                this.setPosicao(pPosicao.getLinha()-1, pPosicao.getColuna());  // Move up
            
            steps++;
            if(steps >= maxSteps) {
                bDown = !bDown;  // Switch direction
                steps = 0;
            }
        }
        
        // Always draw the fruit, even when game is over
        super.autoDesenho();
    }

    // Method to handle fruit collection
    public void coletar() {
        if (!coletada) {
            coletada = true;
            Fruta.incrementarFrutasColetadas(); // Existing
            frutasVertColetadas++; // <-- Add this line
            System.out.println("Fruta vertical coletada! Total coletadas: " + 
                               getFrutasVertColetadas() + " de " + getTotalFrutasVert());
            Fruta.verificarSucesso(); // Use the verificarSucesso from Fruta class
        }
    }

    // Add this method to allow FrutaVert to increment collected count
    public static void incrementarFrutasVertColetadas() {
        frutasVertColetadas++;
    }
    
    // Add method to set counters directly (for loading saved games)
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

    // Static getters for counters
    public static int getTotalFrutasVert() {
        return totalFrutasVert;
    }

    public static int getFrutasVertColetadas() {
        return frutasVertColetadas;
    }
}
