package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class FrutaVert extends Personagem implements Serializable {
    private boolean bDown;  // Changed from bRight to bDown
    private static final double SCALE_FACTOR = 0.8; 
    private int steps;
    private int maxSteps = 3;
    private static int totalFrutas = 0;  // Track total fruits
    private static int frutasColetadas = 0;  // Track collected fruits
    private boolean coletada = false;  // Track if this fruit has been collected

    public FrutaVert(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bDown = true;  // Start moving down
        steps = 0;
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
            frutasColetadas++;
            System.out.println("Frutas verticais coletadas: " + frutasColetadas + " de " + totalFrutas);
            verificarSucesso();
        }
    }
    
    // Add method to check for success
    public static void verificarSucesso() {
        if (frutasColetadas >= totalFrutas && totalFrutas > 0) {
            System.out.println("Todas as frutas verticais foram coletadas!");
            mostrarSucesso();
        }
    }
    
    // Method to show success message in the middle of the screen
    public static void mostrarSucesso() {
        // Create a reference to the notification system
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    // Method to manually reset counters if needed
    public static void resetContadores() {
        totalFrutas = 0;
        frutasColetadas = 0;
    }
}
